package entity

import Drawable
import Scene

data class Gun(
	val epoch: Int,
	val type: Int
) {
	val cost
		get() = entity.Balance.guns[epoch - 1][type - 1].cost

	val icon
		get() = Drawable("res://epoch$epoch/guns/$type/gun${epoch}_$type.png")

	val scene
		get() = Scene("res://epoch$epoch/guns/$type/Gun${epoch}_$type.tscn")

	val projectileScene
		get() = Scene("res://epoch$epoch/guns/$type/projectile/ProjectileGun${epoch}_$type.tscn")

	var body: GunBody? = null
		set(value) {
			field = value
			val b = entity.Balance.guns[epoch - 1][type - 1]
			value?.projectileScene = projectileScene
			value?.projectileSpeed = entity.Balance.projectileSpeed(epoch)
			value?.damage = b.damage
			value?.attackSpeed = b.attackSpeed
			value?.attackFrame = b.attackFrame
		}

	override fun toString(): String {
		return "Gun(epoch=$epoch, type=$type, icon=${icon.path})"
	}

	data class Balance(
		val cost: Int,
		val damage: Int,
		val attackSpeed: Float,
		val attackFrame: Int
	)
}
