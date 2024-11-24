package gui

import R
import findNode
import findNodeWrapper
import godot.Label
import godot.TextureButton
import godot.TextureRect
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import gui.domain.MenuItem

@RegisterClass
class MenuButton : TextureButton() {

	var item: MenuItem? = null
		set(value) {
			if (field == value)
				return
			field = value
			redraw = true
		}

	private var redraw = true

	private var icon: TextureRect? = null
	private var cost: Label? = null
	private var action: TextureRect? = null
	private var queueProgress: QueueProgress? = null
	private var pressedTexture: TextureRect? = null

	@RegisterFunction
	override fun _ready() {
		icon = findNode("Clip/Icon")
		cost = findNode("Cost")
		action = findNode("Action")
		queueProgress = findNodeWrapper("QueueProgress")
		pressedTexture = findNode("Pressed")
	}

	@RegisterFunction
	override fun _process(delta: Double) {
		pressedTexture?.visible = buttonPressed

		if (redraw) {
			redraw = false

			disabled = item?.background == null

			textureNormal = item?.background?.load
			cost?.text = item?.cost?.toString() ?: ""
			icon?.texture = item?.icon?.load
			action?.texture = when (item?.action) {
				MenuItem.Action.add -> R.drawable.ic_add.load
				MenuItem.Action.remove -> R.drawable.ic_remove.load

				else -> null
			}
			val inProgress = item?.progressData != null
			queueProgress?.node?.visible = inProgress
			queueProgress?.set(item?.progressData)
		}
	}
}