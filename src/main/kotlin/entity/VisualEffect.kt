package entity

import R
import connect
import godot.AnimatedSprite2D
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.core.Vector2

@RegisterClass
class VisualEffect : AnimatedSprite2D() {

    protected var frameCount = 0
    var startPosition: Vector2? = null

    @RegisterFunction
    override fun _ready() {
        play()
        frameCount = spriteFrames?.getFrameCount(R.animation.default) ?: 0
        frameChanged.connect(::onFrameChanged)
        startPosition?.let { setGlobalPosition(it) }
    }

    @RegisterFunction
    fun onFrameChanged() {
        if (frame == frameCount - 1) {
            queueFree()
        }
    }
}