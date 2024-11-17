package gui

import Progress
import connect
import entity.Base
import entity.SpecialWeapon
import findNode
import findNodeWrapper
import godot.Control
import godot.TextureButton
import godot.TextureRect
import godot.annotation.Export
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.annotation.RegisterProperty

@RegisterClass
class SuperButton : Control() {

	@Export
	@RegisterProperty
	lateinit var base: Base

	private var progress: Progress = 0f

	private var button: TextureButton? = null
	private var pressed: TextureRect? = null
	private var progressBar: ProgressBar? = null

	@RegisterFunction
	override fun _ready() {
		button = findNode("SpecialButton")
		pressed = findNode("Pressed")
		progressBar = findNodeWrapper("Progress")
		button?.pressed?.connect(::onButtonPressed)
	}

	@RegisterFunction
	fun onButtonPressed() {
		progress = 0f
		base.releaseSpecialWeapon()
	}

	@RegisterFunction
	override fun _process(delta: Double) {
		if (progress < 1)
			progress += delta.toFloat()
		if (progress > 1)
			progress = 1f

		button?.disabled = progress != 1f
		pressed?.visible = button?.buttonPressed ?: false

		progressBar?.progress = progress
	}
}