package gui.domain

import Drawable
import R
import gui.QueueProgress

data class MenuItem(
	val background: Drawable? = R.drawable.background_button,
	val icon: Drawable? = null,
	val cost: Int? = null,
	val action: Action? = null,
	val progressData: QueueProgress.Data? = null
) {
	@JvmInline
	value class Action private constructor(val value: Int) {
		companion object {
			val add = Action(0)
			val remove = Action(1)
		}
	}
}