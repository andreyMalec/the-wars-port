package gui

import GameState
import R
import entity.base.Base
import entity.base.Gun
import entity.base.MAX_UPGRADE_COUNT
import entity.creep.Creep
import findNode
import godot.AudioStreamPlayer
import godot.Control
import godot.Label
import godot.annotation.Export
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.annotation.RegisterProperty
import godot.core.Callable
import godot.core.toGodotName
import gui.domain.MenuItem
import gui.domain.MenuState
import log

const val MAX_BUTTON_COUNT = 5
const val CREEP_TYPES_COUNT = MAX_BUTTON_COUNT - 1
private const val GUN_TYPES_COUNT = MAX_BUTTON_COUNT - 1

private val buttonIndices = 1..MAX_BUTTON_COUNT
private val creepIndices = 1..CREEP_TYPES_COUNT
private val gunIndices = 1..GUN_TYPES_COUNT

private const val SELL_COST_MULTIPLIER = 2

@RegisterClass
class MenuControl : Control() {

	@Export
	@RegisterProperty
	var base: Base = Base()
		set(value) {
			field = value
			findNode<SuperButton>("SpecialWeaponButton")?.base = base
		}

	private lateinit var gameState: GameState

	@Export
	@RegisterProperty
	var _menuState: Int = MenuState.main.value
	private val menuState: MenuState
		get() = MenuState(_menuState)

	private var selectedGun: Gun? = null

	private val buttons = mutableListOf<MenuButton?>()
	private var clickSound: AudioStreamPlayer? = null
	private var errorSound: AudioStreamPlayer? = null
	private var moneyLabel: Label? = null

	fun updateGameState(gameState: GameState) {
		this.gameState = gameState
		val expButton = findNode<ExpButton>("ExpButton")
		expButton?.gameState = gameState
	}

	@RegisterFunction
	override fun _ready() {
		for (index in buttonIndices) {
			buttons.add(findNode<MenuButton>("MenuButton$index")?.apply {
				pressed.connect(Callable(this@MenuControl, "onMenuButton${index}Pressed".toGodotName()))
			})
		}

		errorSound = findNode("ErrorSound")
		clickSound = findNode("ClickSound")
		moneyLabel = findNode("MoneyLabel")
	}

	@RegisterFunction
	fun onMenuButton1Pressed() {
		clickSound?.play()
		if (menuState == MenuState.main)
			_menuState = MenuState.selectCreep.value
		else
			clickButton(1)
	}

	@RegisterFunction
	fun onMenuButton2Pressed() {
		if (menuState == MenuState.main && base.canUpgrade) {
			val cost = base.upgradeCost ?: 0
			if (gameState.money >= cost) {
				clickSound?.play()
				base.upgradeCount++
				gameState.money -= cost
				log.d("base upgraded", " upgradeCount=${base.upgradeCount}")
			} else {
				errorSound?.play()
				log.d("not enough money for upgrade", "money=${gameState.money}", "cost=$cost")
			}
		} else {
			clickSound?.play()
			clickButton(2)
		}
	}

	@RegisterFunction
	fun onMenuButton3Pressed() {
		clickSound?.play()
		if (menuState == MenuState.main)
			_menuState = MenuState.selectGun.value
		else
			clickButton(3)
	}

	@RegisterFunction
	fun onMenuButton4Pressed() {
		clickSound?.play()
		if (menuState == MenuState.main)
			_menuState = MenuState.removeGun.value
		else
			clickButton(4)
	}

	@RegisterFunction
	fun onMenuButton5Pressed() {
		selectedGun = null
		clickSound?.play()
		if (menuState.canGoBack)
			_menuState = menuState.back.value
	}

	private fun clickButton(position: Int) {
		when (menuState) {
			MenuState.selectCreep -> {
				val creep = Creep(gameState.epoch, position)
				val cost = creep.cost
				if (cost != null && gameState.money >= creep.cost && base.queueCreep(creep)) {
					gameState.money -= cost
				} else {
					errorSound?.play()
					log.d("not enough money for creep", "money=${gameState.money}", "cost=$cost")
				}
			}

			MenuState.selectGun -> {
				val gun = Gun(gameState.epoch, position)
				if (gameState.money >= gun.cost) {
					selectedGun = gun
					_menuState = MenuState.selectGunPosition.value
				} else {
					errorSound?.play()
					log.d("not enough money for gun", "money=${gameState.money}", "cost=${gun.cost}")
				}
			}

			MenuState.selectGunPosition -> {
				val gun = selectedGun
				if (gun != null) {
					if (gameState.money >= gun.cost) {
						selectedGun = null
						gameState.money -= gun.cost
						val prevGun = base.addGun(gun, position - 1)
						if (prevGun != null) {
							gameState.money += prevGun.cost / SELL_COST_MULTIPLIER
						}
					} else {
						errorSound?.play()
						log.d("not enough money for gun", "money=${gameState.money}", "cost=${gun.cost}")
					}
				}
				_menuState = MenuState.selectGun.value
			}

			MenuState.removeGun -> {
				val prevGun = base.removeGun(position - 1)
				if (prevGun != null) {
					gameState.money += prevGun.cost / SELL_COST_MULTIPLIER
				}
			}
		}
	}

	@RegisterFunction
	override fun _process(delta: Double) {
		moneyLabel?.text = gameState.money.toString()

		val items = when (menuState) {
			MenuState.main -> main()
			MenuState.selectCreep -> selectCreep()
			MenuState.selectGun -> selectGun()
			MenuState.selectGunPosition -> selectGunPosition()
			MenuState.removeGun -> removeGun()
			else -> listOf()
		}
		buttons.forEachIndexed { index, button ->
			if (index == buttons.lastIndex) {
				button?.item = if (menuState.canGoBack)
					back
				else
					null
			} else
				button?.item = items[index]
		}
	}

	private fun main() = listOf(
		MenuItem(
			icon = R.drawable.ic_creep,
			action = MenuItem.Action.add
		),
		MenuItem(
			icon = R.drawable.ic_upgrade,
			cost = base.upgradeCost,
			action = MenuItem.Action.add
		),
		MenuItem(
			icon = R.drawable.ic_gun,
			action = MenuItem.Action.add
		),
		MenuItem(
			icon = R.drawable.ic_gun,
			action = MenuItem.Action.remove
		)
	)

	private fun selectCreep() = creepIndices.map { type ->
		val creep = Creep(epoch = gameState.epoch, type = type)
		val bg = if (creep.icon == null)
			null
		else
			R.drawable.background_button

		val currentCreep = base.currentCreep
		val queueSize = base.creepQueueCount[type - 1]
		val progressData = if (queueSize > 0) {
			val progress = if (currentCreep?.type == type)
				base.spawnProgress
			else
				0f
			QueueProgress.Data(
				progress = progress,
				queueSize = queueSize - 1
			)
		} else
			null

		MenuItem(
			background = bg,
			icon = creep.icon,
			cost = creep.cost,
			progressData = progressData
		)
	}

	private fun selectGun() = gunIndices.map { type ->
		val gun = Gun(epoch = gameState.epoch, type = type)
		MenuItem(icon = gun.icon, cost = gun.cost)
	}

	private fun selectGunPosition(): List<MenuItem> {
		val items = mutableListOf<MenuItem>()

		for (position in 0..base.upgradeCount) {
			val gun = base.guns[position]
			items.add(MenuItem(icon = gun?.icon))
		}
		for (position in (base.upgradeCount + 1)..MAX_UPGRADE_COUNT) {
			items.add(MenuItem(background = null))
		}

		return items
	}

	private fun removeGun(): List<MenuItem> {
		val items = mutableListOf<MenuItem>()

		for (position in 0..base.upgradeCount) {
			val gun = base.guns[position]
			items.add(
				MenuItem(
					icon = gun?.icon,
					action = if (gun != null) MenuItem.Action.remove else null
				)
			)
		}
		for (position in (base.upgradeCount + 1)..MAX_UPGRADE_COUNT) {
			items.add(MenuItem(background = null))
		}

		return items
	}

	private companion object {
		val back = MenuItem(background = R.drawable.background_button_back)

	}
}
