package model;

public class City {
	public int num;		//city number from 0 to N-1;
	public int lifeValue_City;
	public int flag;	//0 for no flag, 1 for red, 2 for blue;
	public int state;	//-2 to 2 indicating which camp successively win twice, initial 0 ;
	public int num_warrior;		//number of warriors in this city;
	public int record;		//record who won in the latest battle, 0 for none, 1 for red, 2 for blue;
	Warrior warrior[] = new Warrior[2];		//2 warriors at most;
	//set Warrior as City's friend class
	public City(int i) {
		num = i;
		lifeValue_City = 0;
		flag = 0;
		state = 0;
		num_warrior = 0;
		record = 0;
		warrior[0] = null;
		warrior[1] = null;
	}
}
