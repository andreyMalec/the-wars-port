package entity.specialweapon

import Scene
import entity.projectile.ProjectileBallistic
import findMainNode
import godot.Node2D
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.core.Vector2
import kotlin.random.Random

@RegisterClass
class SpecialWeapon3 : Node2D(), SpecialWeaponBody {
	override var direction: Int = 1
	override var projectileScene: Scene? = null
	override var projectileSpeed: Int = 0
	override var damage: Int = 0
	override var attackSpeed: Float = 0f
	override var attackFrame: Int = 0

	private var _attackSpeed = 0f

	private var projectileCount = 32

	@RegisterFunction
	override fun _process(delta: Double) {
		if (_attackSpeed > 0) {
			_attackSpeed -= delta.toFloat()
		} else {
			_attackSpeed = attackSpeed
			projectileCount--
			if (projectileCount == 0)
				queueFree()

			val projectile = projectileScene?.instance<ProjectileBallistic>()

			projectile?.let {
				projectile.direction = direction
				projectile.damage = damage
				projectile.parent = this::class.java
				projectile.startImpulse = Vector2(Random.nextInt(100, 640), -500)
				projectile.startPosition = globalPosition
				findMainNode()?.addChild(projectile)
			}
		}
	}
}