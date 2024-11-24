package entity

import R
import connect
import findNode
import godot.AnimatedSprite2D
import godot.Area2D
import godot.AudioStreamPlayer
import godot.Node2D
import godot.annotation.RegisterFunction
import log
import java.util.*

abstract class DamageDealer : Node2D(), Directable {
	override var direction: Int = 1
	open var damage: Int = 0
	open var attackSpeed: Float = 0f
	open var attackFrame: Int = 0
		set(value) {
			field = value
			if (value > 10) {
				hasDoubleAttack = true
				attackFrames = listOf(attackFrame / 10, attackFrame % 10)
			}
		}

	protected abstract fun dealDamage(target: Damageable)
	protected abstract fun playAttackAnimation()
	protected abstract fun stopAttackAnimation(): DamageDealerState

	protected var hasDoubleAttack: Boolean = false
	protected var attackFrames: List<Int>? = null

	protected var sprite: AnimatedSprite2D? = null
	protected var soundAttack: AudioStreamPlayer? = null

	protected var attackRange: Area2D? = null

	private val targets = PriorityQueue<Damageable>()
	val hasTarget: Boolean
		get() = targets.isNotEmpty()
	val currentTarget: Damageable?
		get() = targets.peek()

	private var _attackSpeed = 99999f

	protected var state: DamageDealerState = DamageDealerState.Idle

	@RegisterFunction
	override fun _ready() {
		soundAttack = findNode(R.node.attackSound)
		sprite = findNode(R.node.sprite)
		attackRange = findNode(R.node.attackRange)
		attackRange?.areaEntered?.connect(::onAttackRangeEntered)
		attackRange?.areaExited?.connect(::onAttackRangeExited)
		sprite?.frameChanged?.connect(::onFrameChanged)

		_attackSpeed = attackSpeed
	}

	@RegisterFunction
	fun onAttackRangeEntered(area: Area2D) {
		if (area.getParent() !is Damageable
			|| (area.getParent() as? Damageable)?.direction == direction
			|| area.getParent() is Ground
		)
			return

		log.d("$this target spotted [${area.getParent()}]")
		targets.add(area.getParent() as Damageable)
	}

	@RegisterFunction
	fun onAttackRangeExited(area: Area2D) {
		if (area.getParent() !is Damageable
			|| (area.getParent() as? Damageable)?.direction == direction
		)
			return

		log.d("$this target lost [${area.getParent()}]")
		targets.remove(area.getParent() as Damageable)
	}

	@RegisterFunction
	open fun onFrameChanged() {
		val frame = sprite?.frame ?: 0

		val performAttack = if (hasDoubleAttack)
			(state is DamageDealerState.Attacking && frame == attackFrames!![0]) || frame == attackFrames!![1]
		else
			state is DamageDealerState.Attacking && frame == attackFrame

		if (performAttack) {
			if (targets.size > 0) {
				val target = targets.peek()
				dealDamage(target)
				log.d("$this dealDamage [$target]")
			}
			state = DamageDealerState.Reloading
		}
	}

	@RegisterFunction
	override fun _process(delta: Double) {
		_attackSpeed -= delta.toFloat()

		if (_attackSpeed < 0) {
			_attackSpeed = attackSpeed
			if (state is DamageDealerState.Reloading) {
				state = stopAttackAnimation()
			}
		}

		if (targets.size > 0) {
			if (_attackSpeed == attackSpeed) {
				if (state is DamageDealerState.CanAttack) {
					state = DamageDealerState.Attacking
					playAttackAnimation()
				}
			}
		}
	}
}

sealed interface DamageDealerState {
	interface CanAttack : DamageDealerState
	data object Idle : CanAttack

	data object Attacking : DamageDealerState
	data object Reloading : DamageDealerState
}