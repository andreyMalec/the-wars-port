package entity

import godot.Node2D
import godot.annotation.RegisterClass

@RegisterClass
class Ground : Node2D(), Damageable {
    override val hp = 1
    override val maxHp = 1
    override val canBleed = false

    override fun takeDamage(damage: Int) {}

    override val direction = 0
}