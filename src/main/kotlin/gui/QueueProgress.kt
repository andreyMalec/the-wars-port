package gui

import NodeWrapper
import PropertyDelegate
import godot.CanvasItem

class QueueProgress(override val node: CanvasItem) : NodeWrapper {
    var progress by PropertyDelegate<Float>()
    var queueSize by PropertyDelegate<Int>()

    fun set(data: Data?) {
        progress = data?.progress ?: 0f
        queueSize = data?.queueSize ?: 0
    }

    data class Data(
        val progress: Float,
        val queueSize: Int
    )
}