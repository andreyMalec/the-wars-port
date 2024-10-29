using Godot;
using System;

public partial class SpecialWeapon : TextureButton
{
	private int sizeReady = 90;
	private float reloadTime = 77f;
	private float elapsedTime;

	private Vector2 currentSize;

	private Control control;

	public override void _Ready()
	{
		Disabled = true;
		currentSize = Size;
		currentSize.y = 0;
		Size = currentSize;
		elapsedTime = 77f; //0

		control = (Control) GetParent();
	}

	public override void _Process(float delta)
	{
		elapsedTime += delta;
		var percent = elapsedTime / reloadTime;
		if (percent > 1) percent = 1;
		currentSize.y = percent * sizeReady;
		Size = currentSize;
		if (percent == 1)
		{
			Disabled = false;
		}
	}

	private void _on_SpecialButton_pressed()
	{
		elapsedTime = 77f; //0

		Node super;
		switch (control.epoch)
		{
			case 1:
				super = ((PackedScene) (GD.Load("res://epoch1/super/Super1.tscn"))).Instance();
				break;
			case 2:
				super = ((PackedScene) (GD.Load("res://epoch2/super/Super2.tscn"))).Instance();
				break;
			case 3:
				super = ((PackedScene) (GD.Load("res://epoch3/super/Super3.tscn"))).Instance();
				break;
			case 4:
				super = ((PackedScene) (GD.Load("res://epoch4/super/Super4.tscn"))).Instance();
				break;
			case 5:
				super = ((PackedScene) (GD.Load("res://epoch5/super/Super5.tscn"))).Instance();
				break;
			default:
				super = ((PackedScene) (GD.Load("res://epoch6/super/Super6.tscn"))).Instance();
				break;
		}

		GetNode<Node2D>("/root/Node2D/SuperWeapon").AddChild(super);
	}
}
