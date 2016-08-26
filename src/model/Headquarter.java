package model;

public class Headquarter {
	public int group;		//1 for red and 2 for blue;
	public int num_warrior;		//number of warrior;
	public int lifeValue_Headquarter;
	public int arrived;		//number of enemy warrior arrived;
	public int self;		//number of all generated warrior;
	Warrior warrior[] = new Warrior[Util.SIZE];
	//set Warrior as City's friend class
	public Headquarter(int _group, int _lifeValue_Headquarter) {
		group = _group;
		lifeValue_Headquarter = _lifeValue_Headquarter;
		num_warrior = 0;
		arrived = 0;
		self = 0;
	}
	public void generateMonster(int _group, int _number, int _lifeValue, int _attackPower, Headquarter headquarter, int _K, int t){
		switch (Util.Order[_group - 1][(_number - 1) % 5]) {
		case 0:
			warrior[_number] = new Dragon(_group, _number, _lifeValue, _attackPower, this);
			warrior[_number].Born(t);
			break;
		case 1:
			warrior[_number] = new Ninja(_group, _number, _lifeValue, _attackPower, this);
			warrior[_number].Born(t);
			break;
		case 2:
			warrior[_number] = new Iceman(_group, _number, _lifeValue, _attackPower, this);
			warrior[_number].Born(t);
			break;
		case 3:
			warrior[_number] = new Lion(_group, _number, _lifeValue, _attackPower, this, _K);
			warrior[_number].Born(t);
			break;
		case 4:
			warrior[_number] = new Wolf(_group, _number, _lifeValue, _attackPower, this);
			warrior[_number].Born(t);
			break;
		default:
			break;
		}
		num_warrior++;
		self = 1;
	}
}
