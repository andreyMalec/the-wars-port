using Godot;
using System;

public partial class Super3 : Node2D
{
	[Export]
	public PackedScene projectileScene;
	[Export]
	public int damage = 100;
	
	int ballsCount = 32;
	
	Random rnd = new Random();
	
	float delay = 0.5f;
	
	public override void _Process(float delta) {
		if (delay > 0) {
			delay -= delta;
		} else {
			delay = 0.2f;
			if (ballsCount >= 0) {
				for (int i = 0; i < 2; i++) {
					ballsCount--;
					var projectile = (ProjectileBallistic)projectileScene.Instance();
					projectile.direction = 1;

					projectile.startImpulse = new Vector2(rnd.Next(100, 640),-500);
					projectile.damage = damage;
					projectile.position = new Vector2(0, 0);
					AddChild((Node2D)projectile);
				}
			}
		}
	}
}
