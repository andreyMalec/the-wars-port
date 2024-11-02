package util

import entity.Gun
import log

class GunControllerImpl(
	private val maxGunCount: Int
) : GunController {
	private val _guns = Array<Gun?>(maxGunCount) { null }
	override val guns: Array<Gun?>
		get() = _guns

	override fun addGun(gun: Gun, position: Int): Gun? {
		if (position < 0 || position >= maxGunCount)
			return null

		val previousGun = removeGun(position)
		_guns[position] = gun
		log.d("add gun=$gun, position=$position", "previousGun=$previousGun")
		return previousGun
	}

	override fun removeGun(position: Int): Gun? {
		if (position < 0 || position >= maxGunCount)
			return null

		val previousGun = guns[position]
		log.d("remove gun position=$position", "previousGun=$previousGun")
		_guns[position] = null
		return previousGun
	}
}