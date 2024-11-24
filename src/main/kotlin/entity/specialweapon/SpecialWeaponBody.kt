package entity.specialweapon

import Scene
import entity.Directable

interface SpecialWeaponBody : Directable {
	override var direction: Int
	var projectileScene: Scene?
	var projectileSpeed: Int
	var damage: Int
	var attackSpeed: Float
	var attackFrame: Int
}