package gui

import R
import findNode
import godot.Label
import godot.Node2D
import godot.annotation.Export
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.annotation.RegisterProperty
import godot.core.Vector2

@RegisterClass
class FloatingLabel : Node2D() {
	@Export
	@RegisterProperty
	var direction: Vector2 = Vector2.ZERO

	var text: String = ""

	@RegisterFunction
	override fun _ready() {
		findNode<Label>(R.node.label)?.text = text
	}

	@RegisterFunction
	override fun _process(delta: Double) {
		setPosition(position + direction)
	}
}