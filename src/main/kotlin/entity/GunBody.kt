package entity

import R
import Scene
import findMainNode
import findNode
import godot.Node2D
import godot.annotation.Export
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.annotation.RegisterProperty
import godot.core.StringName
import godot.core.Vector2
import godot.core.toGodotName
import log
import kotlin.math.abs
import kotlin.math.atan

@RegisterClass
class GunBody : DamageDealer() {
	var projectileScene: Scene? = null

	var projectileSpeed: Int = 0

	@Export
	@RegisterProperty
	var rotateToTarget: Boolean = true

	private var projectilePosition1: Node2D? = null
	private var projectilePosition2: Node2D? = null
	private var secondAttack: Boolean = false

	@RegisterFunction
	override fun _ready() {
		super._ready()
		if (hasDoubleAttack) {
			projectilePosition1 = sprite?.findNode(R.node.projectilePosition1)
			projectilePosition2 = sprite?.findNode(R.node.projectilePosition2)
		} else {
			projectilePosition1 = sprite?.findNode(R.node.projectilePosition)
		}
		log.d("$this ready (damage=$damage, attackSpeed=$attackSpeed), attackFrames=$attackFrames, projectileScene=${projectileScene?.path}")
	}

	@RegisterFunction
	override fun _process(delta: Double) {
		super._process(delta)

		if (hasTarget && rotateToTarget) {
			val nodeTarget = currentTarget as Node2D
			val w = abs(nodeTarget.globalPosition.x - globalPosition.x)
			val h = abs(nodeTarget.globalPosition.y - globalPosition.y)
			val tg = h / w
			sprite?.setRotation(atan(tg).toFloat())
		} else
			sprite?.setRotation(0f)
	}

	override fun dealDamage(target: Damageable) {
		val projectile = projectileScene?.load?.instantiate()

		projectile?.let {
			projectile as Projectile
			projectile.target = target
			projectile.direction = direction
			projectile.damage = damage
			projectile.speed = projectileSpeed
			val pos = Vector2(((if (secondAttack) projectilePosition2 else projectilePosition1) ?: this).globalPosition)
			projectile.startPosition = pos
			findMainNode()?.addChild(projectile)
		}
		if (hasDoubleAttack)
			secondAttack = !secondAttack
		soundAttack?.play()
	}

	override fun playAttackAnimation() {
		sprite?.stop()
		sprite?.play(Animation.attack.name)
	}

	override fun stopAttackAnimation(): DamageDealerState {
		sprite?.stop()
		sprite?.play(Animation.default.name)
		return DamageDealerState.Idle
	}

	@JvmInline
	value class Animation private constructor(val name: StringName) {
		constructor(s: String) : this(s.toGodotName())

		companion object {
			val attack = Animation(R.animation.attack)
			val default = Animation(R.animation.default)
		}
	}
}
