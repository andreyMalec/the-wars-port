package entity

import R
import Scene
import findMainNode
import findNode
import godot.Node2D
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.core.Vector2
import util.Chance

@RegisterClass
class FlyingSpecialWeapon : DamageDealer(), Damageable, SpecialWeaponBody {
	private val speed = SPEED
	private var projectilePosition: Node2D? = null

	override var projectileScene: Scene? = null
	override var projectileSpeed: Int = 0

	override fun dealDamage(target: Damageable) {
		if (target is BaseBody)
			return

		if (Chance(0.5).proc) {
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
		}
	}

	override fun playAttackAnimation() {}

	override fun stopAttackAnimation() = DamageDealerState.Reloading

	@RegisterFunction
	override fun _ready() {
		super._ready()
		projectilePosition = sprite?.findNode(R.node.projectilePosition)
		sprite?.play(R.animation.default)
	}

	@RegisterFunction
	override fun _process(delta: Double) {
		super._process(delta)
		positionMutate {
			x += speed * delta
		}

		if (hp <= 0)
			queueFree()
	}

	override var hp = 1
	override val maxHp = 1
	override val canBleed = false

	override fun takeDamage(damage: Int) {
		hp -= damage
	}

	companion object {
		const val SPEED = 50
	}
}