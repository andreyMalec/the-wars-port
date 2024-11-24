package entity

import entity.base.Gun
import entity.creep.Creep
import entity.specialweapon.SpecialWeapon
import kotlin.math.sqrt
import kotlin.time.Duration.Companion.seconds

object Balance {

	val specialWeaponReloadingTime = 30.seconds

	const val ATTACK_RANGE = 120.0

	fun projectileSpeed(epoch: Int) = (250 * sqrt(epoch.toDouble())).toInt()

	//region =========================  Guns Epoch 1 =========================

	private val gun1_1 = Gun.Balance(
		cost = 100,
		damage = 1,
		attackSpeed = 1f,
		attackFrame = 5
	)
	private val gun1_2 = Gun.Balance(
		cost = 200,
		damage = 1,
		attackSpeed = 0.7f,
		attackFrame = 4
	)
	private val gun1_3 = Gun.Balance(
		cost = 400,
		damage = 4,
		attackSpeed = 3.0f,
		attackFrame = 2
	)
	private val gun1_4 = Gun.Balance(
		cost = 600,
		damage = 8,
		attackSpeed = 3.0f,
		attackFrame = 3
	)

	//endregion

	//region =========================  Guns Epoch 2 =========================

	private val gun2_1 = Gun.Balance(
		cost = 500,
		damage = 1,
		attackSpeed = 0.7f,
		attackFrame = 2
	)
	private val gun2_2 = Gun.Balance(
		cost = 750,
		damage = 2,
		attackSpeed = 2.0f,
		attackFrame = 2
	)
	private val gun2_3 = Gun.Balance(
		cost = 1500,
		damage = 4,
		attackSpeed = 3.0f,
		attackFrame = 2
	)
	private val gun2_4 = Gun.Balance(
		cost = 2000,
		damage = 6,
		attackSpeed = 3.0f,
		attackFrame = 2
	)

	//endregion

	//region =========================  Guns Epoch 3 =========================

	private val gun3_1 = Gun.Balance(
		cost = 1500,
		damage = 2,
		attackSpeed = 1.2f,
		attackFrame = 2
	)
	private val gun3_2 = Gun.Balance(
		cost = 3000,
		damage = 4,
		attackSpeed = 1.6f,
		attackFrame = 2
	)
	private val gun3_3 = Gun.Balance(
		cost = 4500,
		damage = 8,
		attackSpeed = 4.0f,
		attackFrame = 1
	)
	private val gun3_4 = Gun.Balance(
		cost = 6000,
		damage = 16,
		attackSpeed = 3.0f,
		attackFrame = 24
	)

	//endregion

	//region =========================  Guns Epoch 4 =========================

	private val gun4_1 = Gun.Balance(
		cost = 3000,
		damage = 3,
		attackSpeed = 1.2f,
		attackFrame = 1
	)
	private val gun4_2 = Gun.Balance(
		cost = 6000,
		damage = 3,
		attackSpeed = 0.4f,
		attackFrame = 1
	)
	private val gun4_3 = Gun.Balance(
		cost = 7500,
		damage = 20,
		attackSpeed = 3.0f,
		attackFrame = 1
	)
	private val gun4_4 = Gun.Balance(
		cost = 9000,
		damage = 40,
		attackSpeed = 3.0f,
		attackFrame = 13
	)

	//endregion

	//region =========================  Guns Epoch 5 =========================

	private val gun5_1 = Gun.Balance(
		cost = 7000,
		damage = 4,
		attackSpeed = 0.5f,
		attackFrame = 1
	)
	private val gun5_2 = Gun.Balance(
		cost = 9000,
		damage = 4,
		attackSpeed = 0.25f,
		attackFrame = 13
	)
	private val gun5_3 = Gun.Balance(
		cost = 11500,
		damage = 40,
		attackSpeed = 3.0f,
		attackFrame = 1
	)
	private val gun5_4 = Gun.Balance(
		cost = 14000,
		damage = 90,
		attackSpeed = 3.0f,
		attackFrame = 0
	)

	//endregion

	//region =========================  Guns Epoch 6 =========================

	private val gun6_1 = Gun.Balance(
		cost = 24000,
		damage = 8,
		attackSpeed = 0.5f,
		attackFrame = 0
	)
	private val gun6_2 = Gun.Balance(
		cost = 40000,
		damage = 8,
		attackSpeed = 0.25f,
		attackFrame = 0
	)
	private val gun6_3 = Gun.Balance(
		cost = 80000,
		damage = 60,
		attackSpeed = 2.0f,
		attackFrame = 0
	)
	private val gun6_4 = Gun.Balance(
		cost = 100000,
		damage = 120,
		attackSpeed = 3.0f,
		attackFrame = 0
	)

	//endregion

	val guns = arrayOf(
		arrayOf(gun1_1, gun1_2, gun1_3, gun1_4),
		arrayOf(gun2_1, gun2_2, gun2_3, gun2_4),
		arrayOf(gun3_1, gun3_2, gun3_3, gun3_4),
		arrayOf(gun4_1, gun4_2, gun4_3, gun4_4),
		arrayOf(gun5_1, gun5_2, gun5_3, gun5_4),
		arrayOf(gun6_1, gun6_2, gun6_3, gun6_4)
	)

	//1.5 2.5 7
	private val buildTimes = arrayOf(
		arrayOf(0.5f, 0.3f, 0.1f, 0.05f)
	)

	//region =========================  Creeps Epoch 1 =========================

	private val creep1_1 = Creep.Balance(
		cost = 15,
		buildTime = 0.5f,
		damage = 1,
		attackSpeed = 1f,
		attackFrame = 3,
		speed = 20,
		maxHp = 10
	)

	private val creep1_2 = Creep.Balance(
		cost = 25,
		buildTime = 0.3f,
		damage = 1,
		attackSpeed = 1f,
		attackFrame = 4,
		speed = 20,
		maxHp = 4
	)

	private val creep1_3 = Creep.Balance(
		cost = 100,
		buildTime = 0.1f,
		damage = 4,
		attackSpeed = 1f,
		attackFrame = 2,
		speed = 20,
		maxHp = 20
	)

	//endregion

	//region =========================  Creeps Epoch 2 =========================

	private val creep2_1 = Creep.Balance(
		cost = 50,
		buildTime = 0.5f,
		damage = 2,
		attackSpeed = 1f,
		attackFrame = 2,
		speed = 20,
		maxHp = 20
	)

	private val creep2_2 = Creep.Balance(
		cost = 75,
		buildTime = 0.3f,
		damage = 4,
		attackSpeed = 1f,
		attackFrame = 1,
		speed = 20,
		maxHp = 8
	)

	private val creep2_3 = Creep.Balance(
		cost = 500,
		buildTime = 0.1f,
		damage = 8,
		attackSpeed = 1f,
		attackFrame = 1,
		speed = 20,
		maxHp = 40
	)

	//endregion

	//region =========================  Creeps Epoch 3 =========================

	private val creep3_1 = Creep.Balance(
		cost = 200,
		buildTime = 0.5f,
		damage = 4,
		attackSpeed = 1f,
		attackFrame = 2,
		speed = 20,
		maxHp = 20
	)

	private val creep3_2 = Creep.Balance(
		cost = 400,
		buildTime = 0.3f,
		damage = 8,
		attackSpeed = 1f,
		attackFrame = 2,
		speed = 20,
		maxHp = 30
	)

	private val creep3_3 = Creep.Balance(
		cost = 1000,
		buildTime = 0.1f,
		damage = 16,
		attackSpeed = 1f,
		attackFrame = 2,
		speed = 20,
		maxHp = 40
	)

	//endregion

	//region =========================  Creeps Epoch 4 =========================

	private val creep4_1 = Creep.Balance(
		cost = 600,
		buildTime = 0.5f,
		damage = 6,
		attackSpeed = 1f,
		attackFrame = 2,
		speed = 20,
		maxHp = 60
	)

	private val creep4_2 = Creep.Balance(
		cost = 1000,
		buildTime = 0.3f,
		damage = 12,
		attackSpeed = 1f,
		attackFrame = 1,
		speed = 20,
		maxHp = 90
	)

	private val creep4_3 = Creep.Balance(
		cost = 4000,
		buildTime = 0.1f,
		damage = 24,
		attackSpeed = 1f,
		attackFrame = 1,
		speed = 20,
		maxHp = 200
	)

	//endregion

	//region =========================  Creeps Epoch 5 =========================

	private val creep5_1 = Creep.Balance(
		cost = 1500,
		buildTime = 0.5f,
		damage = 8,
		attackSpeed = 1f,
		attackFrame = 2,
		speed = 20,
		maxHp = 100
	)

	private val creep5_2 = Creep.Balance(
		cost = 2000,
		buildTime = 0.3f,
		damage = 16,
		attackSpeed = 1f,
		attackFrame = 2,
		speed = 20,
		maxHp = 150
	)

	private val creep5_3 = Creep.Balance(
		cost = 7000,
		buildTime = 0.1f,
		damage = 32,
		attackSpeed = 1f,
		attackFrame = 1,
		speed = 20,
		maxHp = 400
	)

	//endregion

	//region =========================  Creeps Epoch 6 =========================

	private val creep6_1 = Creep.Balance(
		cost = 5000,
		buildTime = 0.5f,
		damage = 16,
		attackSpeed = 1f,
		attackFrame = 1,
		speed = 20,
		maxHp = 300
	)

	private val creep6_2 = Creep.Balance(
		cost = 6000,
		buildTime = 0.3f,
		damage = 32,
		attackSpeed = 1f,
		attackFrame = 1,
		speed = 20,
		maxHp = 500
	)

	private val creep6_3 = Creep.Balance(
		cost = 20000,
		buildTime = 0.1f,
		damage = 64,
		attackSpeed = 1f,
		attackFrame = 1,
		speed = 20,
		maxHp = 1500
	)

	private val creep6_4 = Creep.Balance(
		cost = 150000,
		buildTime = 0.05f,
		damage = 500,
		attackSpeed = 1f,
		attackFrame = 1,
		speed = 20,
		maxHp = 20000
	)

	//endregion

	val creeps = arrayOf(
		arrayOf(creep1_1, creep1_2, creep1_3, null),
		arrayOf(creep2_1, creep2_2, creep2_3, null),
		arrayOf(creep3_1, creep3_2, creep3_3, null),
		arrayOf(creep4_1, creep4_2, creep4_3, null),
		arrayOf(creep5_1, creep5_2, creep5_3, null),
		arrayOf(creep6_1, creep6_2, creep6_3, creep6_4),
	)


	//region =========================  Special Weapons =========================

	private val specialWeapon1 = SpecialWeapon.Balance(
		damage = 30,
		attackSpeed = 0.2f,
		attackFrame = 13,
	)

	private val specialWeapon2 = SpecialWeapon.Balance(
		damage = 60,
		attackSpeed = 0.5f,
		attackFrame = 0,
	)

	private val specialWeapon3 = SpecialWeapon.Balance(
		damage = 100,
		attackSpeed = 0.2f,
		attackFrame = 0,
	)

	private val specialWeapon4 = SpecialWeapon.Balance(
		damage = 250,
		attackSpeed = 0.1f,
		attackFrame = 13,
	)

	private val specialWeapon5 = SpecialWeapon.Balance(
		damage = 500,
		attackSpeed = 0.1f,
		attackFrame = 13,
	)

	private val specialWeapon6 = SpecialWeapon.Balance(
		damage = 1500,
		attackSpeed = 0.1f,
		attackFrame = 13,
	)

	//endregion

	val specialWeapons = arrayOf(
		specialWeapon1,
		specialWeapon2,
		specialWeapon3,
		specialWeapon4,
		specialWeapon5,
		specialWeapon6
	)
}