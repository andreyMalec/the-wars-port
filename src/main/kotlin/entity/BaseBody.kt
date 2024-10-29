package entity

import godot.Node2D
import godot.annotation.Export
import godot.annotation.RegisterClass
import godot.annotation.RegisterProperty

@RegisterClass
class BaseBody : Node2D(), Damageable {

    @Export
    @RegisterProperty
    override var hp: Int = 500

    @Export
    @RegisterProperty
    override var maxHp: Int = 500

    override val canBleed = false

    override var direction: Int = 1

    override fun takeDamage(damage: Int) {
        hp -= damage
    }
}