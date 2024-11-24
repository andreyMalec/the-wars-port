package entity.specialweapon

import R
import Scene
import entity.DamageDealer
import entity.DamageDealerState
import entity.Damageable
import entity.base.BaseBody
import entity.projectile.Projectile
import findMainNode
import findNode
import godot.Node2D
import godot.annotation.Export
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.annotation.RegisterProperty
import godot.core.Vector2
import util.Chance

@RegisterClass
open class FlyingSpecialWeapon : DamageDealer(), Damageable, SpecialWeaponBody {
	private val speed = SPEED
	private var projectilePosition: Node2D? = null

	override var projectileScene: Scene? = null
	override var projectileSpeed: Int = 0

	override val projectileAvoidance = true

	@Export
	@RegisterProperty
	var attackChance = 0.5

	override fun dealDamage(target: Damageable) {
		if (target is BaseBody)
			return

		if (Chance(attackChance).proc) {
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