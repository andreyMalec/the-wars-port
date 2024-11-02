import godot.Node
import godot.core.asStringName
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Use this for GDScript classes
 */
interface NodeWrapper {
	val node: Node
}

class PropertyDelegate<Value : Any?> : ReadWriteProperty<NodeWrapper, Value?> {
	@Suppress("UNCHECKED_CAST")
	override operator fun getValue(thisRef: NodeWrapper, property: KProperty<*>): Value? {
		return thisRef.node.get(property.name.asStringName()) as? Value
	}

	override operator fun setValue(thisRef: NodeWrapper, property: KProperty<*>, value: Value?) {
		thisRef.node.set(property.name.asStringName(), value)
	}
}