package util

import entity.Gun

interface GunController {
    val guns: Array<Gun?>

    /**
     * @return removed gun or null
     */
    fun addGun(gun: Gun, position: Int): Gun?

    /**
     * @return removed gun or null
     */
    fun removeGun(position: Int): Gun?
}