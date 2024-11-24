package gui

import Progress
import connect
import entity.Balance
import entity.base.Base
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

	private val progressMax = Balance.specialWeaponReloadingTime.inWholeSeconds

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
		if (progress < progressMax)
			progress += delta.toFloat()
		if (progress > progressMax)
			progress = progressMax.toFloat()

		button?.disabled = progress != progressMax.toFloat()
		pressed?.visible = button?.buttonPressed ?: false

		progressBar?.progress = progress / progressMax
	}
}