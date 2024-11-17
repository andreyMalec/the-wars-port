package entity

import godot.annotation.Export
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.annotation.RegisterProperty

@RegisterClass
class Killer : CreepBody() {
	override var maxHp = 9999999
	override var hp = 9999999
	override var attackFrame = 1
	override var attackSpeed = 0.1f
	override var damage = 99999999

	init {
		speed = 0
		defaultSpeed = 0
	}

	@Export
	@RegisterProperty
	override var direction = 1

	@RegisterFunction
	override fun _ready() {
		super._ready()
	}
}
