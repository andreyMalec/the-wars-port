@tool

extends Control

@export var background: Texture2D
@export var foreground: Texture2D

@export var progress = 0.0: set = _progress

func _progress(new_value):
	progress = new_value
	queue_redraw()

func _draw() -> void:
	draw_texture(background, Vector2())
	var rect
	if (progress > 0):
		rect = Rect2(0, 0, foreground.get_width() * progress, foreground.get_height())
	else:
		rect = Rect2(foreground.get_width() * (progress + 1), 0, foreground.get_width() * (-progress), foreground.get_height())
	draw_texture_rect_region(foreground, rect, rect)

func _ready() -> void:
	queue_redraw()
