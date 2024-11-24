package entity.creep

import R
import Scene
import entity.Balance.ATTACK_RANGE
import entity.Damageable
import entity.projectile.Projectile
import findMainNode
import findNode
import godot.CollisionShape2D
import godot.Node2D
import godot.annotation.Export
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.annotation.RegisterProperty
import godot.core.Rect2
import godot.core.Vector2

@RegisterClass
open class CreepBodyRanged : CreepBody() {

	@Export
	@RegisterProperty
	var onlyRanged: Boolean = false

	@Export
	@RegisterProperty
	override var canBleed: Boolean = true

	var projectileScene: Scene? = null
	var projectileSpeed: Int = 0

	private var projectilePosition: Node2D? = null
	private var touchAreaRect: Rect2 = Rect2()

	@RegisterFunction
	override fun _ready() {
		super._ready()
		projectilePosition = sprite?.findNode(R.node.projectilePosition)
		val attackRangeShape = attackRange?.findNode<CollisionShape2D>("CollisionShape2D")
		attackRangeShape?.setScale(Vector2(ATTACK_RANGE / 10.0, 1.0))
		attackRangeShape?.positionMutate {
			x = ATTACK_RANGE / 2
		}
	}

	override fun dealDamage(target: Damageable) {
		if (!dead && (onlyRanged || attackType == AttackType.Ranged)) {
			val projectile = projectileScene?.load?.instantiate()

			projectile?.let {
				projectile as Projectile
				projectile.target = target
				projectile.direction = direction
				projectile.damage = damage
				projectile.speed = projectileSpeed
				projectile.parent = this::class.java
				val pos = Vector2((projectilePosition ?: this).globalPosition)
				projectile.startPosition = pos
				findMainNode()?.addChild(projectile)
			}
			soundAttack?.play()
		} else
			super.dealDamage(target)
	}

	override fun playAttackAnimation() {
		if (!dead)
			if (speed > 0)
				sprite?.play(Animation.attack.name)
			else
				sprite?.play(Animation.attackStatic.name)
	}

	private val attackType: AttackType?
		get() {
			val target = (currentTarget ?: return null) as Node2D
			val distance = (target.globalPosition - globalPosition).length()
			return if (distance > touchAreaRect.size.x)
				AttackType.Ranged
			else
				AttackType.Melee
		}

	private sealed interface AttackType {
		data object Melee : AttackType
		data object Ranged : AttackType
	}
}