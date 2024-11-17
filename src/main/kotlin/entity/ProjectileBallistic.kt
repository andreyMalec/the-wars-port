package entity

import R
import connect
import findNode
import godot.AnimatedSprite2D
import godot.Area2D
import godot.Node2D
import godot.RigidBody2D
import godot.annotation.Export
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.annotation.RegisterProperty
import godot.core.Vector2
import kotlin.math.abs
import kotlin.math.sqrt

@RegisterClass
class ProjectileBallistic : RigidBody2D(), Projectile {
	override var direction: Int = 1
	override lateinit var startPosition: Vector2
	override var damage: Int = 0
	override var speed: Int = 0
	override var target: Damageable? = null

	var startImpulse: Vector2? = null

	private var sprite: AnimatedSprite2D? = null

	@Export
	@RegisterProperty
	var autoRotateSpeed: Int = 0

	@Export
	@RegisterProperty
	override var explosive: Boolean = false

	protected var touchArea: Area2D? = null

	@RegisterFunction
	override fun _ready() {
		touchArea = findNode(R.node.touchArea)
		touchArea?.areaEntered?.connect(::onTouchAreaEntered)
		setGlobalPosition(startPosition)
		sprite = findNode(R.node.sprite)
		sprite?.play()

		val target = target
		if (target != null) {
			target as Node2D
			//кароче хуй знает как это должно работать, тут все подобрано под текущие настройки
			val h = abs(target.globalPosition.y - startPosition.y)
			val v = sqrt(2 * 9.8f * h)
			val p = mass * v

			val x = target.globalPosition.x - startPosition.x

			applyCentralImpulse(Vector2(x * da(p), -p))
		} else {
			if (startImpulse != null)
				applyCentralImpulse(startImpulse!!)
		}
	}

	/**
	 * Да, я использую прибавочные константы, зависящие от масштаба, для корректировки, что ты мне сделаешь
	 */
	private fun da(x: Double): Double = ((x / 20 * x / 20) + 75) / x

	@RegisterFunction
	override fun _process(delta: Double) {
		super._process(delta)
		if (autoRotateSpeed > 0)
			sprite?.rotate(delta.toFloat() * autoRotateSpeed)
	}

	@RegisterFunction
	fun onTouchAreaEntered(area: Area2D) {
		(area.getParent() as? Damageable)?.let { target ->
			dealDamage(target)
		}
	}
}