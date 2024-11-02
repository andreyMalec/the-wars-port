package gui

import Progress
import godot.TextureButton
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction

@RegisterClass
class SuperButton : TextureButton() {

	private var progress: Progress = 0f

	@RegisterFunction
	override fun _ready() {

	}
}