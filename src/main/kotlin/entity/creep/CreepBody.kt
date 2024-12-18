package entity.creep

import R
import connect
import entity.DamageDealer
import entity.DamageDealerState
import entity.Damageable
import entity.VisualEffect
import entity.base.Base
import entity.base.BaseBody
import findNode
import godot.*
import godot.annotation.Export
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.annotation.RegisterProperty
import godot.core.StringName
import godot.core.Vector2
import godot.core.toGodotName
import gui.FloatingLabel
import gui.ProgressBar

typealias Walking = DamageDealerState.Idle

@RegisterClass
open class CreepBody : DamageDealer(), Damageable {
	var speed = 20
	var cost = 0
	var onDied: (() -> Unit) = {}

	override var hp: Int = 20
		set(value) {
			field = value
			hpProgress?.progress = value / maxHp.toFloat() * direction
		}
	override var maxHp: Int = 20
		set(value) {
			field = value
			hp = value
		}

	@Export
	@RegisterProperty
	override var canBleed: Boolean = true

	@Export
	@RegisterProperty
	override var projectileAvoidance: Boolean = false

	override var direction: Int = 1

	private var recentlySpawned = false

	protected var dead = false
	protected var defaultSpeed = speed

	private var pathBusy: Float = -1f

	protected var touchArea: Area2D? = null
	private var hpProgress: ProgressBar? = null
	protected var soundHit: AudioStreamPlayer? = null
	protected var soundDeath: AudioStreamPlayer? = null
	protected var soundWalk: AudioStreamPlayer? = null

	private var deathTimer = 0.0

	override fun dealDamage(target: Damageable) {
		target.takeDamage(damage)
		if (target.canBleed) {
			val blood = R.scene.blood.instance<VisualEffect>()
			(target as Node2D).addChild(blood)
		}
		soundAttack?.play()
	}

	override fun playAttackAnimation() {
		if (!dead)
			sprite?.play(Animation.attack.name)
	}

	override fun stopAttackAnimation(): DamageDealerState {
		if (!dead)
			if (speed > 0)
				sprite?.play(Animation.walk.name)
			else
				sprite?.play(Animation.idle.name)

		return Walking
	}

	override fun takeDamage(damage: Int) {
		hp -= damage
		soundHit?.play()
	}

	@RegisterFunction
	override fun _ready() {
		super._ready()
		val progress = R.scene.hpProgress.wrap<ProgressBar>()
		addChild(progress?.node)
		hpProgress = progress
		touchArea = findNode(R.node.touchArea)
		touchArea?.areaEntered?.connect(::onTouchAreaEntered)
		touchArea?.areaExited?.connect(::onTouchAreaExited)
		sprite?.play(Animation.walk.name)
		soundHit = findNode(R.node.hitSound)
		soundWalk = findNode(R.node.walkSound)
		soundDeath = findNode(R.node.deathSound)
	}

	@RegisterFunction
	fun onTouchAreaEntered(area: Area2D) {
		val target = area.getParent() as? Damageable
		if (area.getParent() is Base)
			recentlySpawned = true

		val targetIsBase = target is BaseBody && target.direction != direction
		if (targetIsBase)
			defaultSpeed = 0//намертво останавливаемся если дошли до вражеской базы
		val targetIsCreep = target is CreepBody
		if (targetIsCreep && target?.direction == direction && if (direction > 0)
				area.globalPosition.x < (touchArea?.globalPosition?.x ?: 0.0)
			else
				area.globalPosition.x > (touchArea?.globalPosition?.x ?: 0.0)
		) {
			return
		}
		pathBusy = 1f
		if (target != null && (targetIsBase || targetIsCreep)) {
			speed = 0
			if (hasTarget && state is DamageDealerState.CanAttack)
				sprite?.play(Animation.idle.name)
		}
	}

	@RegisterFunction
	fun onTouchAreaExited(area: Area2D) {
		val target = area.getParent() as? Damageable
		if (area.getParent() is Base)
			recentlySpawned = false

		val enemyKilled = target?.direction != direction
		val allyBeforeUsKilled =
			target?.direction == direction && if (direction > 0)
				area.globalPosition.x > (touchArea?.globalPosition?.x ?: 0.0)
			else
				area.globalPosition.x < (touchArea?.globalPosition?.x ?: 0.0)
		if (defaultSpeed != 0 && (enemyKilled || allyBeforeUsKilled))
			pathBusy = -0.3f
	}


	@RegisterFunction
	override fun onFrameChanged() {
		super.onFrameChanged()
		if (sprite?.frame == 0 && speed > 0)
			soundWalk?.play()
	}

	@RegisterFunction
	override fun _process(delta: Double) {
		if (hp < 0)
			hp = 0

		if (!dead) {
			positionMutate {
				x += speed * delta * if (recentlySpawned) 0.75 else 1.0
			}
			if (speed == 0 && !hasTarget)
				sprite?.play(Animation.idle.name)
			super._process(delta)

			pathBusy += delta.toFloat()
			if ((pathBusy > 0) and (pathBusy < 1f)) {
				if (!dead) {
					sprite?.play(Animation.walk.name)
					speed = defaultSpeed
				}
			}

			if (hp <= 0) {
				dead = true
				deathActions()
			}
		} else {
			deathTimer += delta

			modulateMutate {
				a = 1 - deathTimer / DESPAWN_TIME
			}

			if (deathTimer >= DESPAWN_TIME) {
				queueFree()
			}
		}

		// DEBUG ONLY
		val polygon = touchArea?.findNode<CollisionPolygon2D>(R.node.collisionPolygon)?.polygon
		polygon?.let {
			if (Geometry2D.isPointInPolygon(getGlobalMousePosition() - globalPosition, polygon))
				hp = 0
		}
	}

	private fun deathActions() {
		onDied()
		soundDeath?.play()
		soundAttack?.stop()
		soundWalk?.queueFree()
		touchArea?.findNode<CollisionPolygon2D>(R.node.collisionPolygon)?.disabled = true
		attackRange?.findNode<CollisionShape2D>(R.node.collisionShape)?.disabled = true
		sprite?.play(Animation.death.name)
		hpProgress?.node?.visible = false

		val addedMoney = R.scene.floatingLabel.instance<FloatingLabel>()
		addedMoney.text = cost.toString()
		addedMoney.scale = Vector2(direction, 1)
		addChild(addedMoney)
	}

	@JvmInline
	value class Animation private constructor(val name: StringName) {
		constructor(s: String) : this(s.toGodotName())

		companion object {
			val walk = Animation(R.animation.walk)
			val attack = Animation(R.animation.attack)
			val attackStatic = Animation(R.animation.attackStatic)
			val death = Animation(R.animation.death)
			val idle = Animation(R.animation.idle)
		}
	}

	private companion object {
		const val DESPAWN_TIME = 3.0
	}
}
