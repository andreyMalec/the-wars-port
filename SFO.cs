using Godot;
using System;

//базовый класс для летающего супер оружия
public partial class SFO : DamageDealer, Damageable
{
	private const float speed = 50f;
	public int health { get; set; }
	public int maxHealth { get; set; }

	[Export] public PackedScene projectileScene;
	
	AudioStreamPlayer projectileAudio;

	public override void _Process(float delta)
	{
		base._Process(delta);
		var velocity = new Vector2
		{
			x = speed,
			y = 0
		};
		Position += velocity * delta;
		
		if (Position.x > 800)
			QueueFree();
	}

	protected override void attack(Damageable target)
	{
		var projectile = (Projectile) projectileScene.Instance();
		projectile.direction = direction;
		projectile.damage = damage;
		projectile.position = Position;
		if (projectile.GetType() == typeof(ProjectileBallistic))
			((ProjectileBallistic)projectile).isNeedToRotate = true;
		GetParent().AddChild((Node) projectile);
		projectileAudio = (AudioStreamPlayer) GetNode<AudioStreamPlayer>("AudioProjectilePop");
		projectileAudio.Play();
	}

	protected override void stopAttackAnimation()
	{
	}

	protected override void playAttackAnimation()
	{
	}
	
	public void playHitSound(){}
}
