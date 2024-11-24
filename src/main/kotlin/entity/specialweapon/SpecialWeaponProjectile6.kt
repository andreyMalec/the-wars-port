package entity.specialweapon

import R
import connect
import entity.Damageable
import entity.VisualEffect
import entity.projectile.Projectile
import findNode
import godot.AnimatedSprite2D
import godot.Area2D
import godot.Node2D
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.core.Vector2

@RegisterClass
class SpecialWeaponProjectile6 : Node2D(), Projectile {
	override var direction: Int = 1
	override var startPosition: Vector2 = Vector2.ZERO
	override var explosive = false
	override var damage: Int = 0
	override var parent: Class<*>? = null
	override var speed: Int = FlyingSpecialWeapon.SPEED
		set(_) {
			field = FlyingSpecialWeapon.SPEED
		}
	override var target: Damageable? = null
	override val antiProjectileAvoidance = true

	private var sprite: AnimatedSprite2D? = null
	protected var touchArea: Area2D? = null

	@RegisterFunction
	override fun _ready() {
		touchArea = findNode(R.node.touchArea)
		touchArea?.areaEntered?.connect(::onTouchAreaEntered)

		setGlobalPosition(startPosition)
		sprite = findNode<AnimatedSprite2D>(R.node.sprite)
		sprite?.frameChanged?.connect(::onFrameChanged)
		sprite?.play()
	}

	@RegisterFunction
	override fun _process(delta: Double) {
		globalPositionMutate {
			x += speed * delta * direction
		}
	}

	@RegisterFunction
	fun onFrameChanged() {
		if (sprite?.frame == (sprite?.spriteFrames?.getFrameCount(R.animation.default) ?: 0) - 1)
			queueFree()
	}

	@RegisterFunction
	fun onTouchAreaEntered(area: Area2D) {
		(area.getParent() as? Damageable)?.let { target ->
			dealDamage(target)
		}
	}

	override fun Node2D.destroySelf() {}

	override fun Node2D.makeThemBleed(target: Damageable) {
		target as Node2D
		val blood = R.scene.blood.instance<VisualEffect>()
		blood.startPosition = target.globalPosition
		target.addChild(blood)
	}
}