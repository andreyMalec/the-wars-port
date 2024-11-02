import godot.core.asNodePath
import godot.core.toGodotName

object R {
	object drawable {
		val background_button = Drawable("res://assets/controls/background_button.png")
		val background_button_back = Drawable("res://assets/controls/background_button_back.png")
		val background_button_pressed = Drawable("res://assets/controls/background_button_pressed.png")

		val ic_add = Drawable("res://assets/controls/ic_add.png")
		val ic_remove = Drawable("res://assets/controls/ic_remove.png")
		val ic_upgrade = Drawable("res://assets/controls/ic_upgrade.png")
		val ic_creep = Drawable("res://assets/controls/ic_creep.png")
		val ic_gun = Drawable("res://assets/controls/ic_gun.png")

		val moon = Drawable("res://assets/entity/moon.png")
		val sun = Drawable("res://assets/entity/sun.png")

		val empty = Drawable("res://assets/controls/empty.png")
	}

	object scene {
		val explosion = Scene("res://scene/Explosion.tscn")
		val blood = Scene("res://scene/Blood.tscn")
		val hpProgress = Scene("res://scene/HpProgress.tscn")
		val floatingLabel = Scene("res://scene/FloatingLabel.tscn")
	}

	object animation {
		val walk = "walk".toGodotName()
		val attack = "attack".toGodotName()
		val attackStatic = "attack_static".toGodotName()
		val death = "death".toGodotName()
		val idle = "idle".toGodotName()
		val default = "default".toGodotName()
	}

	object node {
		val main = "/root/Main".asNodePath()

		val baseEntry = "Entry".asNodePath()
		val baseBodyPosition = "BodyPosition".asNodePath()

		val label = "Label".asNodePath()

		val touchArea = "TouchArea".asNodePath()
		val attackRange = "AttackRange".asNodePath()
		val collisionShape = "CollisionShape2D".asNodePath()
		val collisionPolygon = "CollisionPolygon2D".asNodePath()
		val sprite = "AnimatedSprite2D".asNodePath()

		val projectilePosition = "ProjectilePosition".asNodePath()
		val projectilePosition1 = "ProjectilePosition1".asNodePath()
		val projectilePosition2 = "ProjectilePosition2".asNodePath()

		val globalSounds = "GlobalSounds".asNodePath()
		val hitSound = "HitSound".asNodePath()
		val attackSound = "AttackSound".asNodePath()
		val deathSound = "DeathSound".asNodePath()
		val walkSound = "WalkSound".asNodePath()
	}
}