package gui

import NodeWrapper
import PropertyDelegate
import godot.Control

class ProgressBar(override val node: Control) : NodeWrapper {
	var progress by PropertyDelegate<Float>()
}