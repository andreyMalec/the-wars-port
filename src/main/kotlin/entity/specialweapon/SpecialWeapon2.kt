package entity.specialweapon

import Main
import Scene
import entity.projectile.ProjectileNotBallistic
import findMainNode
import godot.Node2D
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.core.Vector2
import godot.global.GD
import kotlin.random.Random

@RegisterClass
class SpecialWeapon2 : Node2D(), SpecialWeaponBody {
	override var direction: Int = 1
	override var projectileScene: Scene? = null
	override var projectileSpeed: Int = 0
	override var damage: Int = 0
	override var attackSpeed: Float = 0f
	override var attackFrame: Int = 0

	private var _attackSpeed = 0f

	private var projectileCount = 64

	/**
	 * from -spreadAngle to +spreadAngle
	 */
	private val spreadAngle = 7

	private var screenW = 0
	private var main: Main? = null

	@RegisterFunction
	override fun _ready() {
		screenW = getViewport()?.getVisibleRect()?.size?.x?.toInt() ?: 0
		main = findMainNode()
	}

	@RegisterFunction
	override fun _process(delta: Double) {
		if (_attackSpeed > 0) {
			_attackSpeed -= delta.toFloat()
		} else {
			_attackSpeed = attackSpeed
			repeat(4) {
				projectileCount--
				if (projectileCount == 0)
					queueFree()

				val projectile = projectileScene?.instance<ProjectileNotBallistic>()

				projectile?.let {
					projectile.direction = direction
					projectile.damage = damage
					projectile.parent = this::class.java

					val rotation = Random.nextInt(-spreadAngle, spreadAngle) + 90f
					projectile.startImpulse = Vector2((90 - rotation) * 5, projectileSpeed)
					projectile.setRotation(GD.degToRad(rotation).toFloat())
					projectile.startPosition = Vector2(Random.nextInt(40, screenW - 40), Random.nextInt(-120, -10))
					main?.addChild(projectile)
				}
			}
		}
	}
}