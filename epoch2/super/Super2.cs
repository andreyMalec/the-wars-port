using Godot;
using System;

public partial class Super2 : Node2D
{
	[Export]
	public PackedScene projectileScene;
	[Export]
	public int damage = 60;
	
	int arrowCount = 64;
	
	Random rnd = new Random();
	
	float delay = 0.5f;
	
	public override void _Process(float delta) {
		if (delay > 0) {
			delay -= delta;
		} else {
			delay = 0.5f;
			if (arrowCount >= 0) {
				for (int i = 0; i < 4; i++) {
					arrowCount--;
					var projectile = (ProjectileBallistic)projectileScene.Instance();
					projectile.direction = 1;
					float rotation = rnd.Next(-174, 174)/1000f;
					float impulseX = (float) -rotation*500;
					float impulseY = 0;
					projectile.startImpulse = new Vector2(impulseX, impulseY);
					projectile.damage = damage;
					((Node2D)projectile).Rotation = rotation;
					projectile.position = new Vector2(rnd.Next(0, 640), rnd.Next(-80, -10));
					GetParent().GetParent().AddChild((Node2D)projectile);
				}
			}
		}
	}
}
