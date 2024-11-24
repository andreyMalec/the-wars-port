package entity.base

import R
import entity.Damageable
import findNode
import godot.AudioStreamPlayer
import godot.Node2D
import godot.annotation.Export
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.annotation.RegisterProperty

@RegisterClass
class BaseBody : Node2D(), Damageable {

	@Export
	@RegisterProperty
	override var hp: Int = 500

	@Export
	@RegisterProperty
	override var maxHp: Int = 500

	override val canBleed = false

	override var direction: Int = 1

	private var soundHit: AudioStreamPlayer? = null

	@RegisterFunction
	override fun _ready() {
		super._ready()

		soundHit = findNode(R.node.hitSound)
	}

	override fun takeDamage(damage: Int) {
		hp -= damage
		soundHit?.play()
	}
}