@tool

extends Control

@export var background: Texture2D
@export var foreground: Texture2D
@export var queue: Texture2D

@export var progress = 0.0: set = _progress
@export var queueSize = 0: set = _queueSize

func _progress(new_value):
	progress = new_value
	queue_redraw()

func _queueSize(new_value):
	queueSize = new_value
	queue_redraw()

func _draw() -> void:
	draw_texture(background, Vector2())
	var rect = Rect2(0, foreground.get_height() * (1 - progress), foreground.get_width(), foreground.get_height() * progress)
	draw_texture_rect_region(foreground, rect, rect)
	
	for i in queueSize:
		draw_texture(queue, Vector2(foreground.get_width(), i*queue.get_height()))

func _ready() -> void:
	queue_redraw()
