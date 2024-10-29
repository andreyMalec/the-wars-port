package entity

import godot.Node2D

interface Damageable : Directable, Comparable<Damageable> {
    val hp: Int
    val maxHp: Int

    val canBleed: Boolean

    fun takeDamage(damage: Int)

    override fun compareTo(other: Damageable): Int {
        return when {
            other is BaseBody -> -10000
            this is BaseBody -> 10000
            else -> {
                this as Node2D
                other as Node2D
                if (direction > 0)
                    other.globalPosition.x.compareTo(this.globalPosition.x)
                else
                    this.globalPosition.x.compareTo(other.globalPosition.x)
            }
        }
    }
}