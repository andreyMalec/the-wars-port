using Godot;
using System;
using static Constants;
using System.Collections.Generic;

public partial class AI : Node
{
	
	public int baseUpgrade = 0;
	int curPlayerExp = 0;
	public readonly int[] nextEpochExp = {0, 1200, 8000, 40000, 80000, 200000, 200000};
	int nextepoch = 0;
	
	// 1-Aggressive 2-Defence 3-Combined
	int tactics = 0; 
	
	Random rnd;
	static float AI_ticker = 3f;
	float ticker = 0;
	string log = "";
	AIPercents aiPercents;
	int epoch  = 1;
	SpawnBusy spawnArea;
	
	public CreepsQueue creepsQueue = new CreepsQueue();
	
	Node curCreep = null;
	private Node baseAI;
	private Node control;
	float curProgress = 0f;
	public float barProgress = 0f;
	public int creepNum = 0;
	public int[] totalCreeps;
	
	// epochs diff
	// number of characters and position
	
	private void setLog(){
		log = "";
		switch (tactics){
			case 1:
				log += "Aggressive ";
				break;
			case 2:
				log += "Defence ";
				break;
			case 3:
				log += "Combined ";
				break;
			default:
				log += "Sth strange ";
				break;
		}
		log += aiPercents.getInfo();
		int[] res = new int[5] {0, 0, 0, 0, 0};
		res = creepsQueue.Count();
		log += " Queue:" + res[0] + " " + res[1] + " " + res[2] + " " + res[3];
		log += " BU:" + baseUpgrade;
		log += " nextEpoch:" + nextepoch;
	}
	

	public void doAction(int act){
		log += " Act:" + act.ToString();
		//act = 3; //TODO DEBUG DELETE
		switch(act){
			case 1:
				if(baseUpgrade<3){
					baseUpgrade++;
				}
				break;
			case 2:
				int selectedGun = rnd.Next(1,5);
				int position = rnd.Next(0, baseUpgrade);
				Gun gun = (Gun) ((PackedScene) (GD.Load("res://epoch"+epoch+"/guns/"+selectedGun+"/Gun"+epoch+"_"+selectedGun+".tscn"))).Instance();
				((Base)baseAI).addGunForce(gun, position);
				break;
			case 3:
				int selectedCreep  = rnd.Next(1, 4);
				Node creep = ((PackedScene) (GD.Load("res://epoch"+epoch+"/creeps/"+selectedCreep+"/Creep"+epoch+"_"+selectedCreep+".tscn"))).Instance();
				((Creep)creep).direction = -1;
				GD.Print("Enq" + selectedCreep);
				creepsQueue.Enqueue(creep);
				break;
			case 4:
				int num  = rnd.Next(1, 3);
				if (num == 2) num = 3;
				Node creepc = ((PackedScene) (GD.Load("res://epoch"+epoch+"/creeps/"+num+"/Creep"+epoch+"_"+num+".tscn"))).Instance();
				((Creep)creepc).direction = -1;
				creepsQueue.Enqueue(creepc);
				num = 2;
				creepc = ((PackedScene) (GD.Load("res://epoch"+epoch+"/creeps/"+num+"/Creep"+epoch+"_"+num+".tscn"))).Instance();
				((Creep)creepc).direction = -1;
				creepsQueue.Enqueue(creepc);
				break;
			case  5:
				if (epoch < 6){
					epoch++;
					upgradeBaseAI(epoch);
					int step = (int)(nextEpochExp[epoch] * 0.1f);
					nextepoch = rnd.Next(-step, step);
				}
				
				break;
		}
		if (act == 3){
			
		}
	}
	
	private void applyUpgradde(){
		Node baseNode = ((PackedScene) (GD.Load("res://epoch"+(++epoch)+"/base/Base"+epoch+".tscn"))).Instance();
		
		
		Base oldBase = null;
		foreach (Node node in base.GetChildren())
		{
			if (node.GetType() == typeof(Base))
				oldBase = (Base) node;
		}
		if (oldBase == null) return;

		var newHp = oldBase.health + baseHpIncrease[epoch];
		((Base) baseNode).health = newHp;
		var guns = new Dictionary<int, Gun>(oldBase.guns);
		foreach (var element in guns) {
			Node oldParent = ((Node) element.Value).GetParent();
			oldParent.RemoveChild((Node) element.Value);
		}
		((Base) baseNode).guns = guns;
		oldBase.QueueFree();
		baseAI.AddChild(baseNode);
	}

	// Called when the node enters the scene tree for the first time.
	public override void _Ready()
	{
		rnd = new Random();
		tactics = rnd.Next(1, 3);
		int step = (int)(nextEpochExp[epoch] * 0.1f);
		nextepoch = rnd.Next(-step, step);
		
		
		baseAI = GetNode<Node2D>("/root/Node2D/BaseAI/Base");
		control = GetNode<Node>("/root/Node2D/Controls/Control");
		spawnArea = (SpawnBusy)(GetNode<Area2D>("/root/Node2D/BaseAI/Entry"));
		//For debug
		tactics = 1;
		
		aiPercents = new AIPercents(tactics, 1);
		
	}


//  // Called every frame. 'delta' is the elapsed time since the previous frame.
	public override void _Process(float delta)
	{
		creepSpawn(delta);
		curPlayerExp = ((Control)control).experience;
		ticker += delta;
		if (ticker >= AI_ticker){
			ticker = 0;
			setLog();
			int act = aiPercents.genAction();
			if((curPlayerExp + nextepoch > nextEpochExp[epoch]) & (epoch<6)) act = 5;
			doAction(act);
			GD.Print(log);
		}
				
		
	}
	
	
	public void creepSpawn(float delta){
		
		if (curCreep!=null){
			curProgress += delta; // count time 
			if (curProgress > ((Creep)curCreep).buildTime & spawnArea.spawnBusy) curProgress = ((Creep)curCreep).buildTime;
			
			if (curProgress > ((Creep)curCreep).buildTime & !spawnArea.spawnBusy) { //create creep when time
				GD.Print("Add creep");
				this.AddChild(curCreep);
				curCreep=null;
				curProgress = 0;
			}
		}
		if (!(creepsQueue.isEmpty()) & (curCreep==null)){ //get creep from queue 
			GD.Print("curCreepset");
			curCreep = creepsQueue.Dequeue();
			curProgress = 0f;
		}
	}
	
	
	void upgradeBaseAI(int epoch){
		Node baseNode = ((PackedScene) (GD.Load("res://epoch"+(epoch)+"/base/Base"+epoch+".tscn"))).Instance();
		
		Base oldBase = null;
		foreach (Node node in this.GetChildren())
		{
			if (node.GetType() == typeof(Base))
				oldBase = (Base) node;
		}
		if (oldBase == null) return;

		
		var newHp = oldBase.health + Constants.baseHpIncrease[epoch];
		((Base) baseNode).health = newHp;
		var guns = new Dictionary<int, Gun>(oldBase.guns);
		foreach (var element in guns) {
			Node oldParent = ((Node) element.Value).GetParent();
			oldParent.RemoveChild((Node) element.Value);
		}
		((Base) baseNode).guns = guns;
		((Base) baseNode).direction = -1;
		oldBase.QueueFree();
		this.AddChild(baseNode);
	}
	
}


public partial class AIPercents{
	public int updGun;
	public int buyGun;
	public int buyCreep;
	public int buyCombo;
	public int nothing;
	
	public AIPercents(int tactics, int diff){
		switch (tactics){
			case 1:
				updGun = 1;
				buyGun = 4;
				buyCreep = 30;
				buyCombo = 33;
				
				break;
			case 2:
				updGun = 5;
				buyGun = 5;
				buyCreep = 2;
				buyCombo = 5;
				
				break;
			case 3:
				updGun = 2;
				buyGun = 2;
				buyCreep = 2;
				buyCombo = 2;
				
				break;
			
		}
		nothing = 100 - updGun - buyGun - buyCreep - buyCombo;
	}
	
	public int genAction(){
		Random r = new Random();
		int act = r.Next(0, 100);
		if (act < updGun){
			return 1;
		} 
		if (act < buyGun){
			return 2;
		}
		if (act < buyCreep){
			return 3;
		}
		if (act < buyCombo){
			return 4;
		}
		return 0;
	}
	
	public string getInfo(){
		string res = "";
		res += "updGun:" + updGun.ToString() + " buyGun:" + buyGun.ToString() + " buyCreep:" + buyCreep.ToString() + " buyCombo:" + buyCombo.ToString() + " nothing:" + nothing.ToString();
		return res;
	}
	
}
