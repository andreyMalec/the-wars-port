package entity.projectile

import R
import connect
import entity.Damageable
import findNode
import godot.AnimatedSprite2D
import godot.Area2D
import godot.Node2D
import godot.annotation.Export
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.annotation.RegisterProperty
import godot.core.Vector2
import log
import kotlin.math.abs
import kotlin.math.atan

@RegisterClass
class ProjectileNotBallistic : Node2D(), Projectile {
	override var direction: Int = 1
	override var startPosition: Vector2 = Vector2.ZERO
	override var damage: Int = 0
	override var speed: Int = 300
	override var target: Damageable? = null
	override var parent: Class<*>? = null

	var startImpulse: Vector2? = null
		set(value) {
			field = value
			if (value != null)
				lastVelocity = value
		}

	protected var touchArea: Area2D? = null

	private var lastVelocity = Vector2.ZERO

	@Export
	@RegisterProperty
	var applyVerticalVelocity: Boolean = false

	@Export
	@RegisterProperty
	override var explosive: Boolean = false

	@RegisterFunction
	override fun _ready() {
		touchArea = findNode(R.node.touchArea)
		touchArea?.areaEntered?.connect(::onTouchAreaEntered)
		val sprite = findNode<AnimatedSprite2D>(R.node.sprite)
		sprite?.play()
		if (startImpulse == null)
			sprite?.scaleMutate {
				x = direction.toDouble()
			}

		setGlobalPosition(startPosition)
		rotateToTarget()
	}

	@RegisterFunction
	override fun _process(delta: Double) {
		super._process(delta)

		val velocity = calculateVelocity() ?: lastVelocity
		lastVelocity = Vector2(velocity)

		globalPositionMutate {
			x += velocity.x * delta
			y += velocity.y * delta
		}
		if (applyVerticalVelocity)
			rotateToTarget()
	}

	private fun calculateVelocity(): Vector2? {
		return if (target as? Node2D == null || this.target?.hp == 0) {
			//цель умерла до того как мы начали движение, появляется редко при большой скорости стрельбы
			if (lastVelocity == Vector2.ZERO)
				queueFree()

			null
		} else {
			//Для тех кто уворачивается от снарядов рассчитываем траекторию один раз
			if (this.target?.projectileAvoidance == true) {
				val velocity = velocityToTarget()
				target = null
				velocity
			} else
				velocityToTarget()
		}
	}

	private fun velocityToTarget(): Vector2? {
		val target = target as? Node2D ?: return null

		return try {
			if (applyVerticalVelocity)
				(target.globalPosition - globalPosition).normalized() * speed
			else
				Vector2(speed * direction, 0.0)
		} catch (e: Exception) {
			log.d(e)
			null
		}
	}

	private fun rotateToTarget() {
		val target = target as? Node2D
		if (target != null && this.target?.hp != 0) {
			val w = abs(target.globalPosition.x - globalPosition.x)
			val h = abs(target.globalPosition.y - globalPosition.y)
			val tg = h / w
			setRotation(atan(tg * direction).toFloat())
		}
	}

	@RegisterFunction
	fun onTouchAreaEntered(area: Area2D) {
		(area.getParent() as? Damageable)?.let { target ->
			dealDamage(target)
		}
	}
}