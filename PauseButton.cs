using Godot;
using System;

public partial class PauseButton : TextureButton
{
	private bool pause;

	public override void _Ready()
	{
	}

	private void _on_PauseButton_pressed()
	{
		pause = !pause;
		GetTree().Paused = pause;
	}
}
