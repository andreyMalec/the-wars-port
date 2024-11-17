@tool

extends Control

@export var foreground: Texture2D

@export var progress = 0.0: set = _progress

func _progress(new_value):
	progress = new_value
	queue_redraw()

func _draw() -> void:
	var rect = Rect2(0, foreground.get_height() * (1 - progress), foreground.get_width(), foreground.get_height() * progress)
	draw_texture_rect_region(foreground, rect, rect)

func _ready() -> void:
	queue_redraw()
