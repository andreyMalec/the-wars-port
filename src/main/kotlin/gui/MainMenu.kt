package gui

import R
import findNode
import forEachChild
import godot.Button
import godot.Control
import godot.Node
import godot.OS
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import log
import setOnClickListener

@RegisterClass
class MainMenu : Control() {

	private val items = mutableMapOf<String, Control?>()

	private var state: State = State.mainMenu
		set(value) {
			log.d("MainMenu: $field goto $value")
			field = value
			forEachChild<Control> {
				it.visible = false
			}
			items[value]?.visible = true
		}

	@RegisterFunction
	override fun _ready() {
		init(State.mainMenu)?.let { main ->
			main.findNode<Button>(R.menu.newGame)?.let {
				it.setOnClickListener {
					state = State.newGame
				}
			}
			main.findNode<Button>(R.menu.options)?.let {
				it.setOnClickListener {
					state = State.options
				}
			}
			main.findNode<Button>(R.menu.about)?.let {
				it.setOnClickListener {
					OS.shellOpen("https://github.com/andreyMalec/the-wars-port")
				}
			}
		}
		init(State.newGame)?.let { newGame ->
			newGame.findNode<Button>(R.menu.back)?.let {
				it.setOnClickListener {
					state = State.mainMenu
				}
			}
			newGame.findNode<Button>(R.menu.startGame)?.let {
				it.setOnClickListener {
					getTree()?.root?.addChild(R.scene.main.instance())
					this@MainMenu.queueFree()
				}
			}
			newGame.findNode<Button>(R.menu.multiplayer)?.let {
				it.setOnClickListener {
					state = State.multiplayer
				}
			}
		}
		init(State.options)?.let { options ->
			options.findNode<Button>(R.menu.back)?.let {
				it.setOnClickListener {
					state = State.mainMenu
				}
			}
		}
		init(State.multiplayer)?.let { options ->
			options.findNode<Button>(R.menu.back)?.let {
				it.setOnClickListener {
					state = State.newGame
				}
			}
		}
		state = State.mainMenu
	}

	@JvmInline
	private value class State(val value: String) {
		override fun toString(): String {
			return value
		}

		companion object {
			val mainMenu = State("MainMenu")
			val newGame = State("NewGame")
			val options = State("Options")
			val multiplayer = State("Multiplayer")
		}
	}

	private operator fun Map<String, Control?>.get(state: State): Control? = items[state.value + "Container"]

	private fun Node.init(state: State): Control? {
		val name = state.value + "Container"
		val node = findNode(name) as? Control
		items[name] = node
		return node
	}
}