package entity

import Drawable
import Scene

class Creep(
    val epoch: Int,
    val type: Int
) {
    val cost = entity.Balance.creeps[epoch - 1][type - 1]?.cost

    val icon = if (cost != null)
        Drawable("res://epoch$epoch/creeps/$type/creep${epoch}_$type.png")
    else
        null

    val buildTime = entity.Balance.creeps[epoch - 1][type - 1]?.buildTime

    val scene
        get() = Scene("res://epoch$epoch/creeps/$type/creep${epoch}_$type.tscn")

    val projectileScene
        get() = Scene("res://epoch$epoch/creeps/$type/projectile/ProjectileCreep${epoch}_$type.tscn")

    var body: CreepBody? = null
        set(value) {
            field = value
            val b = entity.Balance.creeps[epoch - 1][type - 1] ?: return
            value?.damage = b.damage
            value?.attackSpeed = b.attackSpeed
            value?.attackFrame = b.attackFrame
            value?.speed = b.speed
            value?.maxHp = b.maxHp
            value?.cost = cost!!
            value?.onDied = {
                onDied(this)
            }
            if (value is RangedCreepBody) {
                value.projectileScene = projectileScene
                value.projectileSpeed = entity.Balance.projectileSpeed(epoch)
            }
        }

    var onDied: (Creep) -> Unit = {}

    override fun toString(): String {
        return "Creep(epoch=$epoch, type=$type, icon=${icon?.path})"
    }

    data class Balance(
        val cost: Int,
        val buildTime: Float,
        val damage: Int,
        val attackSpeed: Float,
        val attackFrame: Int,
        val speed: Int,
        val maxHp: Int
    )
}