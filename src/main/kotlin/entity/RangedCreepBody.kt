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
import godot.core.Rect2
import godot.core.Vector2

@RegisterClass
class RangedCreepBody : CreepBody() {

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