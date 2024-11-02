package gui

import R
import findNode
import godot.Node2D
import godot.PointLight2D
import godot.Sprite2D
import godot.Texture2D
import godot.annotation.Export
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.annotation.RegisterProperty
import godot.core.Vector2
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin


@RegisterClass
class DayNight : Node2D() {

	@Export
	@RegisterProperty
	var speedScale: Float = 1f

	private var currentTime = 0.0

	private var mask: PointLight2D? = null
	private var skyLight: Sprite2D? = null

	private var sunTexture: Texture2D? = null
	private var moonTexture: Texture2D? = null
	private var nightTexture: Texture2D? = null

	private var alpha = 180.0

	@RegisterFunction
	override fun _ready() {
		sunTexture = R.drawable.sun.load
		moonTexture = R.drawable.moon.load
//        nightTexture = R.drawable.night.load
		mask = findNode("NightMask")
		skyLight = findNode("SkyLightPosition/SkyLight")
		mask?.energy = 0f
	}

	@RegisterFunction
	override fun _process(delta: Double) {
		val x: Double = a * cos(alpha)
		val y: Double = b * sin(alpha)
		skyLight?.setPosition(Vector2(x.toFloat().toDouble(), (y.toFloat() - 60f).toDouble()))

		currentTime += delta * speedScale

		if (currentTime > 70) {
			val e = (currentTime - 70) / 7.5 // чтоб энергия не была 100%
			mask?.energy = e.toFloat()
			mask?.enabled = true
		}

		if (currentTime <= 0 && currentTime > -7) {
			val e = abs(currentTime) / 7.5f
			mask?.energy = e.toFloat()
			mask?.enabled = false
		}

		if (currentTime >= dayTime) {
			currentTime = nightTime
			skyLight?.texture = moonTexture
		}

		if (currentTime > 0)
			skyLight?.texture = sunTexture
		val percent = if (currentTime > 0) {
			currentTime / dayTime
		} else
			currentTime / nightTime

		alpha = -(1 - percent) * Math.PI
	}

	private companion object {

		const val dayTime = 77.0
		const val nightTime = -60.0

		const val a = 350f
		const val b = 120f
	}
}