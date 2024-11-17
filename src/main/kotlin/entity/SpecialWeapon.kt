package entity

import Scene

data class SpecialWeapon(
	val epoch: Int
) {

	val scene
		get() = Scene("res://epoch$epoch/super/Super${epoch}.tscn")

	val projectileScene
		get() = Scene("res://epoch$epoch/super/projectile/ProjectileSuper${epoch}.tscn")

	var body: SpecialWeaponBody? = null
		set(value) {
			field = value
			val b = entity.Balance.specialWeapons[epoch - 1]
			value?.projectileScene = projectileScene
			value?.projectileSpeed = entity.Balance.projectileSpeed(epoch)
			value?.damage = b.damage
			value?.attackSpeed = b.attackSpeed
			value?.attackFrame = b.attackFrame
		}

	override fun toString(): String {
		return "SpecialWeapon(epoch=$epoch)"
	}

	data class Balance(
		val damage: Int,
		val attackSpeed: Float,
		val attackFrame: Int
	)
}
