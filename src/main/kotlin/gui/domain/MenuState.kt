package gui.domain

@JvmInline
value class MenuState(val value: Int) {
    val canGoBack: Boolean
        get() = value != 0

    val back: MenuState
        get() = if (value % 10 == 0)
            MenuState(value / 10)
        else
            main

    companion object {
        val main = MenuState(0)
        val selectCreep = MenuState(1)
        val selectGun = MenuState(2)
        val selectGunPosition = MenuState(20)
        val removeGun = MenuState(3)
    }
}