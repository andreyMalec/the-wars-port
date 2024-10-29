using Godot;
using System;

public partial class ProjectileSuper6 : Node2D, Projectile
{
	[Export] public int damage {get; set;}
	[Export] public int direction {get; set;}
	[Export] public int speed = 50;
	
	public Vector2 position {get; set;}
	public Node2D target {get; set;}
	
	private float delay = 0.3f;
	private Area2D touchArea;

	public override void _Ready() {
		Position = new Vector2(position.x, position.y + 10);
		touchArea = GetNode<Area2D>("TouchArea");
	}

	public override void _Process(float delta) {
		var velocity = new Vector2
		{
			x = speed * direction,
			y = 0
		};
		Position += velocity * delta;
	}

	private void _on_TouchArea_area_entered(object area) {
		var target = (Damageable)((Node)area).GetParent();
		if(target.direction != direction){
			target.health-=damage;
			QueueFree();
		}
		if (target.GetType() == typeof(Ground)) {
			QueueFree();
		}
	}
}
