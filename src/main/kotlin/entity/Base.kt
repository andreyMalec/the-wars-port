package entity

import GameState
import R
import Scene
import connect
import findNode
import godot.Area2D
import godot.Node2D
import godot.annotation.Export
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.annotation.RegisterProperty
import godot.core.Vector2
import log
import util.GunController
import util.GunControllerImpl
import util.QueueController
import util.QueueControllerImpl

private val costs = intArrayOf(1000, 3000, 7500)
private val hpIncrease = intArrayOf(600, 1100, 1700, 2400, 4400)

const val MAX_UPGRADE_COUNT = 3
const val MAX_GUN_COUNT = MAX_UPGRADE_COUNT + 1

@RegisterClass
class Base : Node2D(), QueueController, GunController {

    var body: BaseBody? = null

    lateinit var enemyState: GameState

    var epoch: Int = 0
        set(value) {
            if (value > field) {
                field = value
                obtainNewBody()
            }
        }

    var upgradeCount: Int = 0

    val canUpgrade: Boolean
        get() = upgradeCount < MAX_UPGRADE_COUNT

    val upgradeCost: Int?
        get() = if (upgradeCount < costs.size) costs[upgradeCount] else null

    private val scene
        get() = Scene("res://epoch$epoch/base/Base${epoch}.tscn")

    private val gunContainers = Array<Node2D?>(MAX_GUN_COUNT) { null }

    @Export
    @RegisterProperty
    var direction: Int = 1
        set(value) {
            field = value
            body?.direction = value
        }

    private var spawnBusy = false

    //region =========================  Queue Controller =========================

    private val queueController = QueueControllerImpl(
        spawnBusy = { spawnBusy },
        creepReady = {
            val creepBody = it.scene.instance<CreepBody>()
            creepBody.direction = direction
            it.body = creepBody
            it.onDied = { creep ->
                enemyState.money += creep.cost ?: 0
                enemyState.experience += (creep.cost ?: 0) * creep.epoch
            }
            addChild(creepBody)
            log.d("creep spawned $it")
        }
    )

    override val creepQueueCount: List<Int>
        get() = queueController.creepQueueCount

    override val spawnProgress: Float
        get() = queueController.spawnProgress

    override val currentCreep: Creep?
        get() = queueController.currentCreep

    override fun queueCreep(creep: Creep): Boolean {
        return queueController.queueCreep(creep)
    }

    //endregion

    //region =========================  Gun Controller =========================

    private val gunController = GunControllerImpl(MAX_GUN_COUNT)

    override val guns: Array<Gun?>
        get() = gunController.guns

    override fun addGun(gun: Gun, position: Int): Gun? {
        val prevGun = gunController.addGun(gun, position)
        if (prevGun != null) {
            gunContainers[position]?.removeChild(prevGun.body)
            prevGun.body?.queueFree()
        }
        val gunBody = gun.scene.instance<GunBody>()
        gunBody.direction = direction
        gun.body = gunBody
        gunContainers[position]?.addChild(gunBody)
        return prevGun
    }

    override fun removeGun(position: Int): Gun? {
        return gunController.removeGun(position)?.also {
            gunContainers[position]?.removeChild(it.body)
            it.body?.queueFree()
            it.body = null
        }
    }

    //endregion

    @RegisterFunction
    override fun _ready() {
        log.d("$this ready direction=$direction")

        val entry = findNode<Area2D>(R.node.baseEntry)
        entry?.areaEntered?.connect(::onEntryEntered)
        entry?.areaExited?.connect(::onEntryExited)

        scale = Vector2(direction, 1)
    }

    @RegisterFunction
    fun onEntryEntered(a: Area2D) {
        spawnBusy = true
    }

    @RegisterFunction
    fun onEntryExited(a: Area2D) {
        spawnBusy = false
    }

    @RegisterFunction
    override fun _process(delta: Double) {
        for (i in gunContainers.indices) {
            gunContainers[i]?.visible = i <= upgradeCount
        }
        queueController.processQueue(delta)
    }

    private fun obtainNewBody() {
        log.d("epoch changed(${epoch - 1}->$epoch), obtainNewBody")

        val oldBody = body
        val bodyPosition = findNode<Node2D>(R.node.baseBodyPosition)
        if (oldBody == null) {
            val newBody = scene.instance<BaseBody>()
            newBody.direction = direction
            bodyPosition?.addChild(newBody)
            body = newBody
            initGunContainers()
        } else {
            val newBody = scene.instance<BaseBody>()
            newBody.hp += oldBody.hp
            newBody.maxHp += oldBody.maxHp
            newBody.direction = direction
            val oldGuns = gunContainers.map {
                if (it?.getChildCount() != 0) {
                    val gunBody = it?.getChild(0) as? GunBody
                    it?.removeChild(gunBody)
                    gunBody
                } else
                    null
            }
            bodyPosition?.removeChild(oldBody)
            bodyPosition?.addChild(newBody)
            body = newBody
            initGunContainers()
            for (i in oldGuns.indices) {
                val gunBody = oldGuns[i]
                if (gunBody != null)
                    gunContainers[i]?.addChild(gunBody)
            }
            oldBody.queueFree()
        }
    }

    private fun initGunContainers() {
        for (i in gunContainers.indices) {
            gunContainers[i] = body?.findNode<Node2D>("gunContainer$i")
        }
    }
}