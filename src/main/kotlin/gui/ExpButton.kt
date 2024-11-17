package gui

import GameState
import findNode
import findNodeWrapper
import godot.AnimatedSprite2D
import godot.Label
import godot.TextureButton
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.core.Callable
import godot.core.toGodotName

@RegisterClass
class ExpButton : TextureButton() {

	var gameState: GameState? = null

	private var newEpochNotification: AnimatedSprite2D? = null
	private var expLabel: Label? = null
	private var expProgress: ProgressBar? = null

	@RegisterFunction
	override fun _ready() {
		newEpochNotification = findNode("NewEpochNotification")
		newEpochNotification?.play()
		expLabel = findNode("ExpLabel")
		expProgress = findNodeWrapper("ExpProgress")

		pressed.connect(Callable(this, "onExpButtonPressed".toGodotName()))
	}

	@RegisterFunction
	fun onExpButtonPressed() {
		gameState?.nextEpoch()
	}

	@RegisterFunction
	override fun _process(delta: Double) {
		val state = gameState
		if (state != null) {
			expLabel?.text = state.experience.toString()
			newEpochNotification?.visible = state.canChangeEpoch
			expProgress?.progress = state.experienceProgress
		}
	}
}