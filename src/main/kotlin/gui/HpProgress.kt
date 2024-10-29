package gui

import NodeWrapper
import PropertyDelegate
import godot.Control

class HpProgress(override val node: Control) : NodeWrapper {
    var progress by PropertyDelegate<Float>()
}