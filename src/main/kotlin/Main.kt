import entity.base.Base
import godot.Control
import godot.Node
import godot.annotation.Export
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.annotation.RegisterProperty
import gui.MenuControl

@RegisterClass
class Main : Node() {

	@Export
	@RegisterProperty
	lateinit var base: Node

	@Export
	@RegisterProperty
	lateinit var enemyBase: Node

	@Export
	@RegisterProperty
	lateinit var baseHp: Control

	@Export
	@RegisterProperty
	lateinit var enemyBaseHp: Control

	@Export
	@RegisterProperty
	lateinit var menu: Node

	@Export
	@RegisterProperty
	var enemyMenu: Node? = null

	private val state = GameState()
	private val enemyState = GameState()

	@RegisterFunction
	override fun _ready() {
		state.money = 5000000
		state.experience = 5000000
		enemyState.money = 5000000
		enemyState.experience = 5000000

		val menu = menu as MenuControl
		val base = base as Base
		base.epoch = state.epoch
		base.enemyState = enemyState
		base.hp = baseHp
		menu.base = base
		menu.updateGameState(state)

		val enemyMenu = enemyMenu as? MenuControl
		val enemyBase = enemyBase as Base
		enemyBase.epoch = enemyState.epoch
		enemyBase.enemyState = state
		enemyBase.hp = enemyBaseHp
		enemyMenu?.base = enemyBase
		enemyMenu?.updateGameState(enemyState)
	}

	@RegisterFunction
	override fun _process(delta: Double) {
		val base = base as Base
		base.epoch = state.epoch

		val enemyBase = enemyBase as Base
		enemyBase.epoch = enemyState.epoch

		if ((base.body?.hp ?: 1) <= 0) {
			log.d("You lose")
			getTree()?.paused = true
		}

		if ((enemyBase.body?.hp ?: 1) <= 0) {
			log.d("You win")
			getTree()?.paused = true
		}
	}
}