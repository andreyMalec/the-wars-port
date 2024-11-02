package gui

import NodeWrapper
import PropertyDelegate
import godot.CanvasItem

class ExpProgress(override val node: CanvasItem) : NodeWrapper {
	var progress by PropertyDelegate<Float>()
}