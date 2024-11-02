package entity

import R
import findMainNode
import findNode
import godot.AudioStreamPlayer
import godot.Node2D
import godot.core.Vector2
import log

interface Projectile : Directable {
	var damage: Int
	var speed: Int
	var target: Damageable?
	override var direction: Int
	var startPosition: Vector2

	var explosive: Boolean

	fun dealDamage(target: Damageable) {
		if (target.direction != direction) {
			target.takeDamage(damage)
			log.d("$this target hit [${target}]")
			this as Node2D
			val hitSound = findNode<AudioStreamPlayer>(R.node.hitSound)
			val main = findMainNode()
			val globalPlayer = main?.findNode<AudioStreamPlayer>(R.node.globalSounds)
			globalPlayer?.setStream(hitSound?.getStream())
			globalPlayer?.play()
			if (explosive) {
				val explosion = R.scene.explosion.instance<VisualEffect>()
				explosion.startPosition = globalPosition
				main?.addChild(explosion)
			}
			if (target.canBleed) {
				val blood = R.scene.blood.instance<VisualEffect>()
				blood.startPosition = globalPosition
				(target as Node2D).addChild(blood)
			}
			queueFree()
		}
	}
}
