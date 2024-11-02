import godot.Node
import godot.core.*
import godot.global.GD
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KFunction0
import kotlin.reflect.KFunction1

/**
 * Progress value in float [0..1]
 */
typealias Progress = Float

inline fun <reified T : Node> Node.findNode(name: String): T? {
	val node = getNode(name.asNodePath())
	return node as? T
}

inline fun <reified T : Node> Node.findNode(name: NodePath): T? {
	val node = getNode(name)
	return node as? T
}

fun Node.findMainNode(): Main? {
	val node = getNode(R.node.main)
	return node as? Main
}

inline fun <reified T : NodeWrapper> Node.findNodeWrapper(name: String): T? {
	val node = getNode(name.asNodePath())
	return node?.let {
		val c = T::class.constructors.last()
		c.call(node)
	}
}

inline fun <reified T : NodeWrapper> Node.findNodeWrapper(name: NodePath): T? {
	val node = getNode(name)
	return node?.let {
		val c = T::class.constructors.last()
		c.call(node)
	}
}

inline fun <reified T> Signal1<T>.connect(function: KFunction1<T, Unit>) {
	connect(function.asCallable())
}

fun Signal0.connect(function: KFunction0<Unit>) {
	connect(function.asCallable())
}

fun <T> listOfNulls(size: Int) = MutableList<T?>(size) { null }

inline fun <reified T> variantArrayOfNulls(size: Int) = listOfNulls<T>(size).toVariantArray()

object log {
	private val format = SimpleDateFormat("hh:mm:ss:SSS")

	fun d(vararg any: Any) = GD.prints(format.format(Date()), *any)
}
