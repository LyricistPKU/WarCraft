package model;

public abstract class Warrior {
	public int group;		//1 for red and 2 for blue;
	public int number;
	public int lifeValue;
	public int attackPower;
	public int weapon[] = new int[3];
	public int swordPower;	//power of a sword;
	public int arrowtime;	//remaining using times of arrow;
	public int position;
	public int available;	//initial 1, dead or arrived enemy's headquarter 0;
	public int arrived;		//initial 0, arrive enemy's headquarter 1;
	public Headquarter headquarter;
	public City city;
	public Warrior(int _group, int _number, int _lifeValue, int _attackPower, Headquarter _headquarter) {
		group = _group;
		number = _number;
		lifeValue = _lifeValue;
		attackPower = _attackPower;
		headquarter = _headquarter;
		weapon[0] = 0;
		weapon[1] = 0;
		weapon[2] = 0;
		swordPower = 0;
		arrowtime = 3;
		position = _group == 1 ? 0 : Util.N + 1;
		available = 1;
		arrived = 0;
		city = null;
	}
	public abstract void Fight(Warrior warrior);
	public abstract void Hurt(int power);
	public abstract void FightBack(Warrior warrior);
	public abstract void Born(int t);		//t indicates the current time;
	public boolean isRunAway(){return false;};
	public void Move(){
		if (group == 1){
			position++;
		}
		else if(group == 2){
			position--;
		}
	};
	public abstract void yell(int t);
	public abstract void moralIncrease();
	public abstract void moralDecrease();
	public abstract void getWeapon(Warrior warrior);
	public boolean isDead(){return lifeValue <= 0;};
}

