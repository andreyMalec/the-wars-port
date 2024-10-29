import godot.Node
import godot.PackedScene
import godot.Texture2D
import godot.extensions.instantiateAs
import godot.global.GD

interface Resource {
    val path: String

    val load: Any
        get() = GD.load(path)!!
}

@JvmInline
value class Drawable(override val path: String) : Resource {
    override val load: Texture2D
        get() = GD.load(path)!!
}

@JvmInline
value class Scene(override val path: String) : Resource {
    override val load: PackedScene
        get() = GD.load(path)!!

    fun <T : Node> instance() = load.instantiateAs<T>()!!

    inline fun <reified T : NodeWrapper> wrap() = load.instantiate()?.let {
        val c = T::class.constructors.last()
        c.call(it)
    }
}