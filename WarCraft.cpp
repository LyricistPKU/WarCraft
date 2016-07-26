#include<iostream>
#include<string>
#include<iomanip>
#include<fstream>
using namespace std;

const int SIZE = 10000;
const int NUM = 100;
int judge = 0;		//end of one case
int M, N, R, K, T;
int monster_lifevalue[5];
int monster_attackpower[5];
int Order[2][5] = { { 2, 3, 4, 1, 0 }, { 3, 0, 1, 2, 4 } };	//0 dragon 1 ninja 2 iceman 3 lion 4 wolf 
string nameOrder[2][5] = { { "iceman", "lion", "wolf", "ninja", "dragon" }, { "lion", "dragon", "ninja", "iceman", "wolf" } };
class City;
class Headquarter;
class Warrior;
class Weapon;

ofstream outfile;

City *pCity[NUM];

class Warrior{
public:
	int group;		//1 for red and 2 for blue
	int number;
	int lifeValue;
	int attackPower;
	int weapon[3];
	int swordPower;	//power of a sword
	int arrowtime;	//remained times of a arrow
	int position;
	int available;	//initial 1, dead or arrived enemy's headquarter 0
	int arrived;	//initial 0, arrive enemy's headquarter 1
	Headquarter *pHeadquarter;
	City *pCity;
	Warrior(int _group, int _number, int _lifeValue, int _attackPower, Headquarter *_pHeadquarter) :group(_group), number(_number),
		lifeValue(_lifeValue), attackPower(_attackPower), pHeadquarter(_pHeadquarter){
		weapon[0] = 0;
		weapon[1] = 0;
		weapon[3] = 0;
		swordPower = 0;
		arrowtime = 3;
		position = ((_group == 1) ? 0 : N + 1);
		available = 1;
		arrived = 0;
		pCity = NULL;
	};
	virtual void Fight(Warrior * pWarrior) = 0;
	virtual void Hurt(int power) = 0;
	virtual void FightBack(Warrior * pWarrior) = 0;
	virtual void Born(int t) = 0;		//t indicates the current time
	virtual bool IsRunAway(){		//lions run away
		return 0;
	}
	virtual void Move(){
		if (group == 1){
			position++;
		}
		else if (group == 2){
			position--;
		}
	}
	virtual void yell(int t){

	}
	virtual void moralIncrease(){

	}
	virtual void moralDecrease(){

	}
	virtual void getWeapon(Warrior *pWarrior){

	}
	bool IsDead(){
		return (lifeValue <= 0);
	}
};

class Dragon : public Warrior{
private:
	double moral;
public:
	Dragon(int _group, int _number, int _lifeValue, int _attackPower, Headquarter *_pHeadquarter);
	virtual void Fight(Warrior * pWarrior);
	virtual void Hurt(int power);
	virtual void FightBack(Warrior * pWarrior);
	virtual void Born(int t);
	virtual void yell(int t);
	virtual void moralIncrease();
	virtual void moralDecrease();
};

class Ninja : public Warrior{
public:
	Ninja(int _group, int _number, int _lifeValue, int _attackPower, Headquarter *_pHeadquarter);
	virtual void Fight(Warrior * pWarrior);
	virtual void Hurt(int power);
	virtual void FightBack(Warrior *pWarrior);
	virtual void Born(int t);
};

class Iceman : public Warrior{
private:
	int step;
public:
	Iceman(int _group, int _number, int _lifeValue, int _attackPower, Headquarter *_pHeadquarter);
	virtual void Fight(Warrior * pWarrior);
	virtual void Hurt(int power);
	virtual void FightBack(Warrior * pWarrior);
	virtual void Born(int t);
	virtual void Move();
};

class Lion : public Warrior{
private:
	int loyalty;
	int Kvalue;
public:
	Lion(int _group, int _number, int _lifeValue, int _attackPower, Headquarter *_pHeadquarter, int _K);
	virtual void Fight(Warrior * pWarrior);
	virtual void Hurt(int power);
	virtual void FightBack(Warrior * pWarrior);
	virtual void Born(int t);
	virtual bool IsRunAway();
};

class Wolf : public Warrior{
public:
	Wolf(int _group, int _number, int _lifeValue, int _attackPower, Headquarter *_pHeadquarter);
	virtual void Fight(Warrior * pWarrior);
	virtual void Hurt(int power);
	virtual void FightBack(Warrior * pWarrior);
	virtual void Born(int t);
	virtual void getWeapon(Warrior *pWarrior);
};

class City{
public:
	int num;		//city number from 0 to N - 1
	int lifeValue_City;
	int flag;		//initial 0,1 for red and 2 for blue
	int state;		//-2 to 2 indicating which camp successively twice, initial 0
	int num_warrior;		//number of warriors in this city
	int record;		//record who won in the latest battle, 0 for none, 1 for red, 2 for blue
	Warrior *pWarrior[2];		//2 warriors most at one city
	friend class Warrior;
	City(int i){
		num = i;
		lifeValue_City = 0;
		flag = 0;
		state = 0;
		num_warrior = 0;
		record = 0;
		pWarrior[0] = NULL;		//mind if the pointer is null
		pWarrior[1] = NULL;
	}
};

class Headquarter{
public:
	int group;		//1 for red and 2 for blue
	int num_warrior;	//number of warrior
	int lifeValue_Headquarter;
	int arrived;		//number of enemy warriors arrived
	int self;			//number of all generated warrior
	Warrior *pWarrior[SIZE];		//pointing to the newly generated warrior, mind whether it has contents because it is not initialized in the construct function
	friend class Warrior;
	Headquarter(int _group, int _lifeValue_Headquarter){
		group = _group;
		lifeValue_Headquarter = _lifeValue_Headquarter;
		num_warrior = 0;
		arrived = 0;
		self = 0;
	};
	void generateMonster(int _group, int _number, int _lifeValue, int _attackPower, Headquarter *_pHeadquarter, int _K, int t){
		switch (Order[_group - 1][(_number - 1) % 5]){
		case 0:
			pWarrior[_number] = new Dragon(_group, _number, _lifeValue, _attackPower, this);
			pWarrior[_number]->Born(t);
			break;
		case 1:
			pWarrior[_number] = new Ninja(_group, _number, _lifeValue, _attackPower, this);
			pWarrior[_number]->Born(t);
			break;
		case 2:
			pWarrior[_number] = new Iceman(_group, _number, _lifeValue, _attackPower, this);
			pWarrior[_number]->Born(t);
			break;
		case 3:
			pWarrior[_number] = new Lion(_group, _number, _lifeValue, _attackPower, this, _K);
			pWarrior[_number]->Born(t);
			break;
		case 4:
			pWarrior[_number] = new Wolf(_group, _number, _lifeValue, _attackPower, this);
			pWarrior[_number]->Born(t);
			break;
		default:
			break;
		}
		num_warrior++;
		self = 1;
	}
};

Dragon::Dragon(int _group, int _number, int _lifeValue, int _attackPower, Headquarter *_pHeadquarter) :Warrior(_group, _number, _lifeValue, _attackPower, _pHeadquarter){
	moral = double(_pHeadquarter->lifeValue_Headquarter) / double(lifeValue);
	weapon[number % 3] = 1;
	if (number % 3 == 0){
		swordPower = int(attackPower * 0.2);
	}
}

void Dragon::Fight(Warrior *pWarrior){
	pWarrior->Hurt(attackPower + ((weapon[0]) ? swordPower : 0));
	swordPower = int(swordPower * 0.8);
	if (swordPower == 0){
		weapon[0] = 0;
	}
}

void Dragon::Hurt(int _power){
	lifeValue -= _power;
}

void Dragon::FightBack(Warrior *pWarrior){
	pWarrior->Hurt(int(attackPower / 2) + ((number % 3 == 0) ? swordPower : 0));
	swordPower = int(swordPower * 0.8);
	if (swordPower == 0){
		weapon[0] = 0;
	}
}

void Dragon::Born(int t){
	string s = (group == 1) ? " red " : " blue ";
	outfile << setw(3) << setfill('0') << t << ":00" << s
		<< "dragon " << number << " born" << endl;
	outfile << "Its morale is " << setprecision(2) << fixed << moral << endl;
}

void Dragon::yell(int t){
	if (moral > 0.8 && available){
		string s = (group == 1) ? " red " : " blue ";
		outfile << setw(3) << setfill('0') << t << ":40" << s << "dragon " << number << " yelled in city " << pCity->num << endl;
	}
}

void Dragon::moralIncrease(){
	moral = moral + double(0.8);
}

void Dragon::moralDecrease(){
	moral = moral - double(0.2);
}

Ninja::Ninja(int _group, int _number, int _lifeValue, int _attackPower, Headquarter *_pHeadquarter) :Warrior(_group, _number, _lifeValue, _attackPower, _pHeadquarter){
	weapon[number % 3] = 1;
	weapon[(number + 1) % 3] = 1;
	if (number % 3 == 0 || ((number + 1) % 3 == 0)){
		swordPower = int(attackPower * 0.2);
	}
}

void Ninja::Fight(Warrior *pWarrior){
	pWarrior->Hurt(attackPower + ((weapon[0]) ? swordPower : 0));
	swordPower = int(swordPower * 0.8);
	if (swordPower == 0){
		weapon[0] = 0;
	}
}

void Ninja::Hurt(int _power){
	lifeValue -= _power;
}

void Ninja::FightBack(Warrior *pWarrior){

}

void Ninja::Born(int t){
	string s = (group == 1) ? " red " : " blue ";
	outfile << setw(3) << setfill('0') << t << ":00" << s
		<< "ninja " << number << " born" << endl;
}

Iceman::Iceman(int _group, int _number, int _lifeValue, int _attackPower, Headquarter *_pHeadquarter) :Warrior(_group, _number, _lifeValue, _attackPower, _pHeadquarter){
	step = 0;
	weapon[number % 3] = 1;
	if (number % 3 == 0){
		swordPower = int(attackPower * 0.2);
	}
}

void Iceman::Fight(Warrior *pWarrior){
	pWarrior->Hurt(attackPower + ((weapon[0]) ? swordPower : 0));
	swordPower = int(swordPower * 0.8);
	if (swordPower == 0){
		weapon[0] = 0;
	}
}

void Iceman::Hurt(int _power){
	lifeValue -= _power;
}

void Iceman::FightBack(Warrior *pWarrior){
	pWarrior->Hurt(int(attackPower / 2) + ((weapon[0]) ? swordPower : 0));
	swordPower = int(swordPower * 0.8);
	if (swordPower == 0){
		weapon[0] = 0;
	}
}

void Iceman::Born(int t){
	string s = (group == 1) ? " red " : " blue ";
	outfile << setw(3) << setfill('0') << t << ":00" << s
		<< "iceman " << number << " born" << endl;
}

void Iceman::Move(){
	if (group == 1){
		position++;
	}
	else if (group == 2){
		position--;
	}
	step++;
	if (step == 2){
		if (lifeValue > 9){
			lifeValue -= 9;
			attackPower += 20;
		}
		else{
			lifeValue = 1;
			attackPower += 20;
		}
		step = 0;
	}
}

Lion::Lion(int _group, int _number, int _lifeValue, int _attackPower, Headquarter *_pHeadquarter, int _K) :Warrior(_group, _number, _lifeValue, _attackPower, _pHeadquarter){
	Kvalue = _K;
	loyalty = _pHeadquarter->lifeValue_Headquarter;
}

void Lion::Fight(Warrior *pWarrior){
	pWarrior->Hurt(attackPower + ((weapon[0]) ? swordPower : 0));
	if (pWarrior->lifeValue > 0){
		loyalty -= Kvalue;
	}
	swordPower = int(swordPower * 0.8);
	if (swordPower == 0){
		weapon[0] = 0;
	}
}

void Lion::Hurt(int _power){	//the life value of lion will transfer to its enemy if lion is killed
	if (_power >= lifeValue)
	{
		if (group == 1)
		{
			pCity->pWarrior[1]->lifeValue += lifeValue;		//mind if City pointer has a value
		}
		else if (group == 2)
		{
			pCity->pWarrior[0]->lifeValue += lifeValue;
		}
	}
	lifeValue -= _power;
}

void Lion::FightBack(Warrior *pWarrior){
	pWarrior->Hurt(int(attackPower / 2) + ((weapon[0]) ? swordPower : 0));
	if (pWarrior->lifeValue > 0){
		loyalty -= Kvalue;
	}
	swordPower = int(swordPower * 0.8);
	if (swordPower == 0){
		weapon[0] = 0;
	}
}

void Lion::Born(int t){
	string s = (group == 1) ? " red " : " blue ";
	outfile << setw(3) << setfill('0') << t << ":00" << s
		<< "lion " << number << " born" << endl;
	outfile << "Its loyalty is " << loyalty << endl;
}

bool Lion::IsRunAway(){
	return (loyalty <= 0);
}

Wolf::Wolf(int _group, int _number, int _lifeValue, int _attackPower, Headquarter *_pHeadquarter) :Warrior(_group, _number, _lifeValue, _attackPower, _pHeadquarter){

}

void Wolf::Fight(Warrior *pWarrior){
	pWarrior->Hurt(attackPower + ((weapon[0]) ? swordPower : 0));
	swordPower = int(swordPower * 0.8);
	if (swordPower == 0){
		weapon[0] = 0;
	}
}

void Wolf::Hurt(int _power)
{
	lifeValue -= _power;
}

void Wolf::FightBack(Warrior *pWarrior)
{
	pWarrior->Hurt(int(attackPower / 2) + ((weapon[0]) ? swordPower : 0));
	swordPower = int(swordPower * 0.8);
	if (swordPower == 0){
		weapon[0] = 0;
	}
}

void Wolf::Born(int t){
	string s = (group == 1) ? " red " : " blue ";
	outfile << setw(3) << setfill('0') << t << ":00" << s
		<< "wolf " << number << " born" << endl;
}

void Wolf::getWeapon(Warrior *pWarrior){
	if (!pWarrior->available){
		if (!weapon[0]){
			weapon[0] = pWarrior->weapon[0];
			swordPower = pWarrior->swordPower;
		}
		if (!weapon[1]){
			weapon[1] = pWarrior->weapon[1];
		}
		if (!weapon[2]){
			weapon[2] = pWarrior->weapon[2];
			arrowtime = pWarrior->arrowtime;
		}
	}
}


void run(Headquarter *p1, Headquarter *p2, City *p[], int n, int t){		//lions run away
	if (p1->self){
		if (p1->pWarrior[p1->num_warrior]->IsRunAway()){
			p1->pWarrior[p1->num_warrior]->available = 0;
			p1->self = 0;
			outfile << setw(3) << setfill('0') << t << ":05 red lion " << p1->num_warrior << " ran away" << endl;
		}
	}
	for (int i = 1; i <= n; i++)
	{
		if (p[i]->num_warrior == 1){
			if (p[i]->pWarrior[0]->IsRunAway()){
				p[i]->pWarrior[0]->available = 0;
				string s = (p[i]->pWarrior[0]->group == 1) ? "red" : "blue";
				outfile << setw(3) << setfill('0') << t << ":05 " << s << " lion " << p[i]->pWarrior[0]->number << " ran away" << endl;
			}
		}
		else if (p[i]->num_warrior == 2){
			if (p[i]->pWarrior[0]->IsRunAway()){
				p[i]->pWarrior[0]->available = 0;
				string s = (p[i]->pWarrior[0]->group == 1) ? "red" : "blue";
				outfile << setw(3) << setfill('0') << t << ":05 " << s << " lion " << p[i]->pWarrior[0]->number << " ran away" << endl;
			}
			if (p[i]->pWarrior[1]->IsRunAway()){
				p[i]->pWarrior[1]->available = 0;
				string s = (p[i]->pWarrior[1]->group == 1) ? "red" : "blue";
				outfile << setw(3) << setfill('0') << t << ":05 " << s << " lion " << p[i]->pWarrior[1]->number << " ran away" << endl;
			}
		}
	}
	if (p2->self){
		if (p2->pWarrior[p2->num_warrior]->IsRunAway()){
			p2->pWarrior[p2->num_warrior]->available = 0;
			p2->self = 0;
			outfile << setw(3) << setfill('0') << t << ":05 blue lion " << p2->num_warrior << " ran away" << endl;
		}
	}
}

void refreshCities(Headquarter *p1, Headquarter *p2, City *p[], int n){
	for (int i = 1; i <= n; i++){		//initialize all the cities
		p[i]->num_warrior = 0;
		p[i]->record = 0;		//record means which camp wins the battle last time in this city
		p[i]->pWarrior[0] = NULL;
		p[i]->pWarrior[1] = NULL;
	}
	for (int i = 1; i <= p1->num_warrior; i++)		//reposite red monsters;
	{
		if (p1->pWarrior[i]->available){
			p[p1->pWarrior[i]->position]->pWarrior[p[p1->pWarrior[i]->position]->num_warrior++] = p1->pWarrior[i];
			p1->pWarrior[i]->pCity = p[p1->pWarrior[i]->position];		//set warrior's city pointer
		}
		if (p1->pWarrior[i]->arrowtime == 0)		//warrior lose arrow
		{
			p1->pWarrior[i]->weapon[2] = 0;
		}
		if (p1->pWarrior[i]->swordPower == 0)		//warrior lose sword
		{
			p1->pWarrior[i]->weapon[0] = 0;
		}
	}
	for (int i = 1; i <= p2->num_warrior; i++)
	{
		if (p2->pWarrior[i]->available){
			p[p2->pWarrior[i]->position]->pWarrior[p[p2->pWarrior[i]->position]->num_warrior++] = p2->pWarrior[i];	//set city's warrior pointer
			p2->pWarrior[i]->pCity = p[p2->pWarrior[i]->position];		//set warrior's city pointer
		}
		if (p2->pWarrior[i]->arrowtime == 0)		//warrior lose arrow
		{
			p2->pWarrior[i]->weapon[2] = 0;
		}
		if (p2->pWarrior[i]->swordPower == 0)		//warrior lose sword
		{
			p2->pWarrior[i]->weapon[0] = 0;
		}
	}
}

int hasRed(City *p){		//judge if there is a red warrior on the city 
	if (p->num_warrior){
		if (p->pWarrior[0]->group == 1 && p->pWarrior[0]->available){
			return 1;
		}
	}
	return 0;
}

int hasBlue(City *p){
	if (p->num_warrior == 1){
		if (p->pWarrior[0]->group == 2 && p->pWarrior[0]->available){
			return 1;
		}
	}
	else if (p->num_warrior == 2){
		if (p->pWarrior[1]->group == 2 && p->pWarrior[1]->available){
			return 2;
		}
	}
	return 0;
}

void forward(Headquarter *p1, Headquarter *p2, City *p[], int n, int t){	//change the position of every city
	if (hasBlue(p[1])){		//blue warrior in city in 1 move to red headquater
		int j = ((hasBlue(p[1]) - 1) ? 1 : 0);
		p1->arrived++;
		p[1]->pWarrior[j]->available = 0;
		p[1]->pWarrior[j]->arrived = 1;
		p[1]->pWarrior[j]->Move();
		outfile << setw(3) << setfill('0') << t << ":10 blue " << nameOrder[1][(p[1]->pWarrior[j]->number - 1) % 5]
			<< " " << p[1]->pWarrior[j]->number << " reached red headquarter with " << p[1]->pWarrior[j]->lifeValue <<
			" elements and force " << p[1]->pWarrior[j]->attackPower << endl;
		if (p1->arrived == 2)
		{
			outfile << setw(3) << setfill('0') << t << ":10 red headquarter was taken" << endl;
			judge = 1;
		}
	}
	if (p1->self == 1){		//move monster in the red camp to city 1
		p1->pWarrior[p1->num_warrior]->Move();
		p1->self = 0;
		outfile << setw(3) << setfill('0') << t << ":10" << " red " << nameOrder[0][(p1->num_warrior - 1) % 5] << " " <<
			p1->num_warrior << " marched to city 1 with " << p1->pWarrior[p1->num_warrior]->lifeValue << " elements and force "
			<< p1->pWarrior[p1->num_warrior]->attackPower << endl;
	}
	if (n > 1){
		if (hasBlue(p[2])){		//move blue warriors in the city 2 to city 1
			int j = ((hasBlue(p[2]) - 1) ? 1 : 0);
			p[2]->pWarrior[j]->Move();
			outfile << setw(3) << setfill('0') << t << ":10 blue " << nameOrder[1][(p[2]->pWarrior[j]->number - 1) % 5]
				<< " " << p[2]->pWarrior[j]->number << " marched to city 1 with " <<
				p[2]->pWarrior[j]->lifeValue << " elements and force " << p[2]->pWarrior[j]->attackPower << endl;
		}
	}
	for (int i = 2; i <= n - 1; i++){		//move red and blue warriors to city i
		if (hasRed(p[i - 1])){
			p[i - 1]->pWarrior[0]->Move();
			outfile << setw(3) << setfill('0') << t << ":10 red " << nameOrder[0][(p[i - 1]->pWarrior[0]->number - 1) % 5] <<
				" " << p[i - 1]->pWarrior[0]->number << " marched to city " << i << " with " << p[i - 1]->pWarrior[0]->lifeValue <<
				" elements and force " << p[i - 1]->pWarrior[0]->attackPower << endl;
		}
		if (hasBlue(p[i + 1])){
			int j = ((hasBlue(p[i + 1]) - 1) ? 1 : 0);
			p[i + 1]->pWarrior[j]->Move();
			outfile << setw(3) << setfill('0') << t << ":10 blue " << nameOrder[1][(p[i + 1]->pWarrior[j]->number - 1) % 5]
				<< " " << p[i + 1]->pWarrior[j]->number << " marched to city " << i << " with " <<
				p[i + 1]->pWarrior[j]->lifeValue << " elements and force " << p[i + 1]->pWarrior[j]->attackPower << endl;
		}
	}
	if (n > 1){		//red warriors move from N - 1 to N
		if (hasRed(p[n - 1])){
			p[n - 1]->pWarrior[0]->Move();
			outfile << setw(3) << setfill('0') << t << ":10 red " << nameOrder[0][(p[n - 1]->pWarrior[0]->number - 1) % 5]
				<< " " << p[n - 1]->pWarrior[0]->number << " marched to city " << n << " with " <<
				p[n - 1]->pWarrior[0]->lifeValue << " elements and force " << p[n - 1]->pWarrior[0]->attackPower << endl;
		}
	}
	if (p2->self == 1){		//move monster in the blue camp
		p2->pWarrior[p2->num_warrior]->Move();
		p2->self = 0;
		outfile << setw(3) << setfill('0') << t << ":10" << " blue " << nameOrder[1][(p2->num_warrior - 1) % 5] << " " <<
			p2->num_warrior << " marched to city " << N << " with " << p2->pWarrior[p2->num_warrior]->lifeValue <<
			" elements and force " << p2->pWarrior[p2->num_warrior]->attackPower << endl;
	}
	if (hasRed(p[N])){
		p2->arrived++;
		p[N]->pWarrior[0]->available = 0;
		p[N]->pWarrior[0]->arrived = 1;
		p[N]->pWarrior[0]->Move();
		outfile << setw(3) << setfill('0') << t << ":10 red " << nameOrder[0][(p[N]->pWarrior[0]->number - 1) % 5]
			<< " " << p[N]->pWarrior[0]->number << " reached blue headquarter with " << p[N]->pWarrior[0]->lifeValue <<
			" elements and force " << p[N]->pWarrior[0]->attackPower << endl;
		if (p2->arrived == 2)
		{
			outfile << setw(3) << setfill('0') << t << ":10 blue headquarter was taken" << endl;
			judge = 1;
		}
	}
	refreshCities(p1, p2, p, N);		//leave out the dead warriors
}

void citiesGennerateLifeValue(City *p[], int n){
	for (int i = 1; i <= n; i++){
		p[i]->lifeValue_City += 10;
	}
}

void getCityValue(City *p[], int n, int t){
	for (int i = 1; i <= n; i++){
		if (p[i]->num_warrior == 1){
			if (p[i]->lifeValue_City > 0){
				string s = (p[i]->pWarrior[0]->group == 1) ? " red " : " blue ";
				p[i]->pWarrior[0]->pHeadquarter->lifeValue_Headquarter += p[i]->lifeValue_City;
				outfile << setw(3) << setfill('0') << t << ":30" << s << nameOrder[p[i]->pWarrior[0]->group - 1][(p[i]->pWarrior[0]->number - 1) % 5]
					<< " " << p[i]->pWarrior[0]->number << " earned " << p[i]->lifeValue_City << " elements for his headquarter" << endl;
				p[i]->lifeValue_City = 0;
			}
		}
	}
}

void shoot(City *p[], int n, int r, int t){
	if (n > 1){
		for (int i = 1; i <= n; i++)
		{	
			if (i != n){
				if (p[i]->num_warrior && p[i + 1]->num_warrior == 1){	//red[0] shoot blue[0] in city 1 to N
					if (p[i]->pWarrior[0]->group == 1 && p[i]->pWarrior[0]->weapon[2] && p[i]->pWarrior[0]->arrowtime && p[i + 1]->pWarrior[0]->group == 2)
					{
						p[i + 1]->pWarrior[0]->lifeValue -= r;
						p[i]->pWarrior[0]->arrowtime--;
						if (!p[i]->pWarrior[0]->arrowtime){
							p[i]->pWarrior[0]->weapon[2] = 0;
						}
						if (p[i + 1]->pWarrior[0]->IsDead()){
							p[i + 1]->pWarrior[0]->available = 0;
							outfile << setw(3) << setfill('0') << t << ":35 red " << nameOrder[0][(p[i]->pWarrior[0]->number - 1) % 5] <<
								" " << p[i]->pWarrior[0]->number << " shot and killed blue " << nameOrder[1][(p[i + 1]->pWarrior[0]->number - 1) % 5]
								<< " " << p[i + 1]->pWarrior[0]->number << endl;
						}
						else{
							outfile << setw(3) << setfill('0') << t << ":35 red " << nameOrder[0][(p[i]->pWarrior[0]->number - 1) % 5] <<
								" " << p[i]->pWarrior[0]->number << " shot" << endl;
						}
					}
				}
				if (p[i]->num_warrior && p[i + 1]->num_warrior == 2){	//red[0] shoot blue[1]
					if (p[i]->pWarrior[0]->group == 1 && p[i]->pWarrior[0]->weapon[2] && p[i]->pWarrior[0]->arrowtime && p[i + 1]->pWarrior[1]->group == 2)
					{
						p[i + 1]->pWarrior[1]->lifeValue -= r;
						p[i]->pWarrior[0]->arrowtime--;
						if (!p[i]->pWarrior[0]->arrowtime){
							p[i]->pWarrior[0]->weapon[2] = 0;
						}
						if (p[i + 1]->pWarrior[1]->IsDead()){
							p[i + 1]->pWarrior[1]->available = 0;
							outfile << setw(3) << setfill('0') << t << ":35 red " << nameOrder[0][(p[i]->pWarrior[0]->number - 1) % 5] <<
								" " << p[i]->pWarrior[0]->number << " shot and killed blue " << nameOrder[1][(p[i + 1]->pWarrior[1]->number - 1) % 5]
								<< " " << p[i + 1]->pWarrior[1]->number << endl;
						}
						else{
							outfile << setw(3) << setfill('0') << t << ":35 red " << nameOrder[0][(p[i]->pWarrior[0]->number - 1) % 5] <<
								" " << p[i]->pWarrior[0]->number << " shot" << endl;
						}
					}
				}
			}
			if (i != 1){
				if (p[i]->num_warrior == 1 && p[i - 1]->num_warrior){		//blue[0] shoot red[0]
					if (p[i]->pWarrior[0]->group == 2 && p[i]->pWarrior[0]->weapon[2] && p[i]->pWarrior[0]->arrowtime && p[i - 1]->pWarrior[0]->group == 1)
					{
						p[i - 1]->pWarrior[0]->lifeValue -= r;
						p[i]->pWarrior[0]->arrowtime--;
						if (!p[i]->pWarrior[0]->arrowtime){
							p[i]->pWarrior[0]->weapon[2] = 0;
						}
						if (p[i - 1]->pWarrior[0]->IsDead()){
							p[i - 1]->pWarrior[0]->available = 0;
							outfile << setw(3) << setfill('0') << t << ":35 blue " << nameOrder[1][(p[i]->pWarrior[0]->number - 1) % 5] <<
								" " << p[i]->pWarrior[0]->number << " shot and killed red " << nameOrder[0][(p[i - 1]->pWarrior[0]->number - 1) % 5]
								<< " " << p[i - 1]->pWarrior[0]->number << endl;
						}
						else{
							outfile << setw(3) << setfill('0') << t << ":35 blue " << nameOrder[1][(p[i]->pWarrior[0]->number - 1) % 5] <<
								" " << p[i]->pWarrior[0]->number << " shot" << endl;
						}
					}
				}
				if (p[i]->num_warrior == 2 && p[i - 1]->num_warrior){		//blue[1] shoot red[0]
					if (p[i]->pWarrior[1]->group == 2 && p[i]->pWarrior[1]->weapon[2] && p[i]->pWarrior[1]->arrowtime && p[i - 1]->pWarrior[0]->group == 1)
					{
						p[i - 1]->pWarrior[0]->lifeValue -= r;
						p[i]->pWarrior[1]->arrowtime--;
						if (!p[i]->pWarrior[1]->arrowtime){
							p[i]->pWarrior[1]->weapon[2] = 0;
						}
						if (p[i - 1]->pWarrior[0]->IsDead()){
							p[i - 1]->pWarrior[0]->available = 0;
							outfile << setw(3) << setfill('0') << t << ":35 blue " << nameOrder[1][(p[i]->pWarrior[1]->number - 1) % 5] <<
								" " << p[i]->pWarrior[1]->number << " shot and killed red " << nameOrder[0][(p[i - 1]->pWarrior[0]->number - 1) % 5]
								<< " " << p[i - 1]->pWarrior[0]->number << endl;
						}
						else{
							outfile << setw(3) << setfill('0') << t << ":35 blue " << nameOrder[1][(p[i]->pWarrior[1]->number - 1) % 5] <<
								" " << p[i]->pWarrior[1]->number << " shot" << endl;
						}
					}
				}
			}
		}
	}
}

int whoWillDie(City *p){		//juedge who will die after a war, 0 for no one, 1 for red and 2 for blue
	int lifered = p->pWarrior[0]->lifeValue;
	int lifeblue = p->pWarrior[1]->lifeValue;
	if (p->flag == 1 || (p->flag == 0 && (p->num % 2 == 1)))
	{
		lifeblue -= ((p->pWarrior[0]->attackPower) + ((p->pWarrior[0]->weapon[0]) ? p->pWarrior[0]->swordPower : 0));
		if (lifeblue <= 0){
			return 2;
		}
		if (((p->pWarrior[1]->number - 1) % 5) != 2){		//blue warrior is not ninja
			lifered -= (int((p->pWarrior[1]->attackPower) / 2) + ((p->pWarrior[1]->weapon[0]) ? p->pWarrior[1]->swordPower : 0));
			if (lifered <= 0){
				return 1;
			}
		}
	}
	else{
		lifered -= ((p->pWarrior[1]->attackPower) + ((p->pWarrior[1]->weapon[0]) ? p->pWarrior[1]->swordPower : 0));
		if (lifered <= 0){
			return 1;
		}
		if (((p->pWarrior[0]->number - 1) % 5) != 3){		//red warrior is not ninja
			lifeblue -= (int((p->pWarrior[0]->attackPower) / 2) + ((p->pWarrior[0]->weapon[0]) ? p->pWarrior[0]->swordPower : 0));
			if (lifeblue <= 0){
				return 2;
			}
		}
	}
	return 0;
}

void useBomb(City *p[], int n, int t){
	for (int i = 1; i <= n; i++){
		if (p[i]->num_warrior == 2)		//if there is two warrior alive in one city
		{
			if (p[i]->pWarrior[0]->available && p[i]->pWarrior[1]->available){
				if (p[i]->pWarrior[0]->weapon[1] && whoWillDie(p[i]) == 1)
				{
					outfile << setw(3) << setfill('0') << t << ":38 red " << nameOrder[0][(p[i]->pWarrior[0]->number - 1) % 5] <<
						" " << p[i]->pWarrior[0]->number << " used a bomb and killed blue " << nameOrder[1][(p[i]->pWarrior[1]->number - 1) % 5]
						<< " " << p[i]->pWarrior[1]->number << endl;
					p[i]->pWarrior[0]->lifeValue = 0;
					p[i]->pWarrior[1]->lifeValue = 0;
					p[i]->pWarrior[0]->available = 0;
					p[i]->pWarrior[1]->available = 0;
				}
				else if (p[i]->pWarrior[1]->weapon[1] && whoWillDie(p[i]) == 2)
				{
					outfile << setw(3) << setfill('0') << t << ":38 blue " << nameOrder[1][(p[i]->pWarrior[1]->number - 1) % 5] <<
						" " << p[i]->pWarrior[1]->number << " used a bomb and killed red " << nameOrder[0][(p[i]->pWarrior[0]->number - 1) % 5]
						<< " " << p[i]->pWarrior[0]->number << endl;
					p[i]->pWarrior[0]->lifeValue = 0;
					p[i]->pWarrior[1]->lifeValue = 0;
					p[i]->pWarrior[0]->available = 0;
					p[i]->pWarrior[1]->available = 0;
				}
			}
		}
	}
}

void praiseWarrior(Headquarter *p1, Headquarter *p2, City *p[], int n){
	for (int i = 1; i <= N; i++){
		if (p[i]->num_warrior == 2){
			if (p[i]->record == 1 && p1->lifeValue_Headquarter >= 8)
			{
				p[i]->pWarrior[0]->lifeValue += 8;
				p1->lifeValue_Headquarter -= 8;
			}
		}
	}
	for (int i = N; i >= 1; i--){
		if (p[i]->num_warrior == 2){
			if (p[i]->record == 2 && p2->lifeValue_Headquarter >= 8)
			{
				p[i]->pWarrior[1]->lifeValue += 8;
				p2->lifeValue_Headquarter -= 8;
			}
		}
	}
}

void getBattleValue(Headquarter *p1, Headquarter *p2, City *p[], int n, int t){
	for (int i = 1; i <= N; i++){
		if (p[i]->num_warrior == 2){
			if (p[i]->record == 1){
				p1->lifeValue_Headquarter += p[i]->lifeValue_City;
				outfile << setw(3) << setfill('0') << t << ":40 red " << nameOrder[0][(p[i]->pWarrior[0]->number - 1) % 5]
					<< " " << p[i]->pWarrior[0]->number << " earned " << p[i]->lifeValue_City << " elements for his headquarter" << endl;
				p[i]->lifeValue_City = 0;
			}
			if (p[i]->record == 2){
				p2->lifeValue_Headquarter += p[i]->lifeValue_City;
				outfile << setw(3) << setfill('0') << t << ":40 blue " << nameOrder[1][(p[i]->pWarrior[1]->number - 1) % 5]
					<< " " << p[i]->pWarrior[1]->number << " earned " << p[i]->lifeValue_City << " elements for his headquarter" << endl;
				p[i]->lifeValue_City = 0;
			}
		}
	}
}

void flagRise(City *p[], int n, int t){
	for (int i = 1; i <= n; i++){
		if (p[i]->num_warrior == 2){
			if (p[i]->record == 1 && p[i]->state == -2){
				p[i]->flag = 1;
				p[i]->state = 0;
				outfile << setw(3) << setfill('0') << t << ":40 red flag raised in city " << i << endl;
			}
			else if (p[i]->record == 2 && p[i]->state == 2){
				p[i]->flag = 2;
				p[i]->state = 0;
				outfile << setw(3) << setfill('0') << t << ":40 blue flag raised in city " << i << endl;
			}
		}
	}
}

bool whoWillAttack(City *p){		//judge who will attack first, true for  red, flase for blue
	if (p->flag == 1 || (p->flag == 0 && (p->num % 2 == 1)))
	{
		return true;
	}
	else{
		return false;
	}
}

void battle(Headquarter *p1, Headquarter *p2, City *p[], int n, int t){
	int redlife = 0;
	int bluelife = 0;
	for (int i = 1; i <= n; i++){
		if (p[i]->num_warrior == 2){
			int whowillyell = 0;		//judge who attack first, 0 for no attack, 1 for red, 2 for blue
			if (p[i]->pWarrior[0]->available && !p[i]->pWarrior[1]->available){		//blue warrior shot dead by arrow
				if (p[i]->state > 0){
					p[i]->state = -1;
				}
				else{
					p[i]->state--;
				}
				p[i]->record = 1;
				p[i]->pWarrior[0]->moralIncrease();	
				if (whoWillAttack(p[i])){
					p[i]->pWarrior[0]->yell(t);
				}
				p[i]->pWarrior[0]->getWeapon(p[i]->pWarrior[1]);					//wolf get weapon
				if (p[i]->lifeValue_City){
					outfile << setw(3) << setfill('0') << t << ":40 red " << nameOrder[0][(p[i]->pWarrior[0]->number - 1) % 5] <<
						" " << p[i]->pWarrior[0]->number << " earned " << p[i]->lifeValue_City << " elements for his headquarter" << endl;
					redlife += p[i]->lifeValue_City;
					p[i]->lifeValue_City = 0;
				}
				if (p[i]->state == -2 && p[i]->flag != 1){		//raise flag
					outfile << setw(3) << setfill('0') << t << ":40 red flag raised in city " << i << endl;
					p[i]->flag = 1;
				}
			}
			else if (!p[i]->pWarrior[0]->available && p[i]->pWarrior[1]->available){	//red warrior shot dead by arrow
				if (p[i]->state < 0)
				{
					p[i]->state = 1;
				}
				else{
					p[i]->state++;
				}
				p[i]->record = 2;
				p[i]->pWarrior[1]->moralIncrease();
				if (!whoWillAttack(p[i])){
					p[i]->pWarrior[1]->yell(t);
				}
				p[i]->pWarrior[1]->getWeapon(p[i]->pWarrior[0]);					//wolf get weapon
				if (p[i]->lifeValue_City){
					outfile << setw(3) << setfill('0') << t << ":40 blue " << nameOrder[1][(p[i]->pWarrior[1]->number - 1) % 5] <<
						" " << p[i]->pWarrior[1]->number << " earned " << p[i]->lifeValue_City << " elements for his headquarter" << endl;
					bluelife += p[i]->lifeValue_City;
					p[i]->lifeValue_City = 0;
				}
				if (p[i]->state == 2 && p[i]->flag != 2){
					outfile << setw(3) << setfill('0') << t << ":40 blue flag raised in city " << i << endl;
					p[i]->flag = 2;
				}
			}
			else if (p[i]->pWarrior[0]->available && p[i]->pWarrior[1]->available){
				if (whoWillAttack(p[i])){		//red warrior attack first
					whowillyell = 1;
					outfile << setw(3) << setfill('0') << t << ":40 red " << nameOrder[0][(p[i]->pWarrior[0]->number - 1) % 5] <<
						" " << p[i]->pWarrior[0]->number << " attacked blue " << nameOrder[1][(p[i]->pWarrior[1]->number - 1) % 5] <<
						" " << p[i]->pWarrior[1]->number << " in city " << i << " with " << p[i]->pWarrior[0]->lifeValue <<
						" elements and force " << p[i]->pWarrior[0]->attackPower << endl;
					p[i]->pWarrior[0]->Fight(p[i]->pWarrior[1]);
					if (p[i]->pWarrior[1]->IsDead()){		//blue warrior killed by red fight, not fight back
						p[i]->pWarrior[1]->available = 0;
						outfile << setw(3) << setfill('0') << t << ":40 blue " << nameOrder[1][(p[i]->pWarrior[1]->number - 1) % 5] <<
							" " << p[i]->pWarrior[1]->number << " was killed in city " << i << endl;
						if (p[i]->state > 0){
							p[i]->state = -1;
						}
						else{
							p[i]->state--;
						}
						p[i]->record = 1;
						p[i]->pWarrior[0]->moralIncrease();
						p[i]->pWarrior[0]->yell(t);											//dragon yell
						p[i]->pWarrior[0]->getWeapon(p[i]->pWarrior[1]);					//wolf get weapon
						if (p[i]->lifeValue_City){
							outfile << setw(3) << setfill('0') << t << ":40 red " << nameOrder[0][(p[i]->pWarrior[0]->number - 1) % 5] <<
								" " << p[i]->pWarrior[0]->number << " earned " << p[i]->lifeValue_City << " elements for his headquarter" << endl;
							redlife += p[i]->lifeValue_City;
							p[i]->lifeValue_City = 0;
						}
						if (p[i]->state == -2 && p[i]->flag != 1){
							outfile << setw(3) << setfill('0') << t << ":40 red flag raised in city " << i << endl;
							p[i]->flag = 1;
						}
					}
					else if (((p[i]->pWarrior[1]->number - 1) % 5) != 2) {	//blue warrior is not dead and it's not a ninja
						outfile << setw(3) << setfill('0') << t << ":40 blue " << nameOrder[1][(p[i]->pWarrior[1]->number - 1) % 5] <<
							" " << p[i]->pWarrior[1]->number << " fought back against red " << nameOrder[0][(p[i]->pWarrior[0]->number - 1) % 5] <<
							" " << p[i]->pWarrior[0]->number << " in city " << i << endl;
						p[i]->pWarrior[1]->FightBack(p[i]->pWarrior[0]);
						if (p[i]->pWarrior[0]->IsDead()){
							p[i]->pWarrior[0]->available = 0;
							outfile << setw(3) << setfill('0') << t << ":40 red " << nameOrder[0][(p[i]->pWarrior[0]->number - 1) % 5] <<
								" " << p[i]->pWarrior[0]->number << " was killed in city " << i << endl;
							if (p[i]->state < 0)
							{
								p[i]->state = 1;
							}
							else{
								p[i]->state++;
							}
							p[i]->record = 2;
							p[i]->pWarrior[1]->moralIncrease();
							p[i]->pWarrior[1]->getWeapon(p[i]->pWarrior[0]);					//wolf get weapon
							if (p[i]->lifeValue_City){
								outfile << setw(3) << setfill('0') << t << ":40 blue " << nameOrder[1][(p[i]->pWarrior[1]->number - 1) % 5] <<
									" " << p[i]->pWarrior[1]->number << " earned " << p[i]->lifeValue_City << " elements for his headquarter" << endl;
								bluelife += p[i]->lifeValue_City;
								p[i]->lifeValue_City = 0;
							}
							if (p[i]->state == 2 && p[i]->flag != 2){
								outfile << setw(3) << setfill('0') << t << ":40 blue flag raised in city " << i << endl;
								p[i]->flag = 2;
							}
						}
					}
				}
				else{		//blue warrior attack first
					whowillyell = 2;
					outfile << setw(3) << setfill('0') << t << ":40 blue " << nameOrder[1][(p[i]->pWarrior[1]->number - 1) % 5] <<
						" " << p[i]->pWarrior[1]->number << " attacked red " << nameOrder[0][(p[i]->pWarrior[0]->number - 1) % 5] <<
						" " << p[i]->pWarrior[0]->number << " in city " << i << " with " << p[i]->pWarrior[1]->lifeValue <<
						" elements and force " << p[i]->pWarrior[1]->attackPower << endl;
					p[i]->pWarrior[1]->Fight(p[i]->pWarrior[0]);
					if (p[i]->pWarrior[0]->IsDead()){
						p[i]->pWarrior[0]->available = 0;
						outfile << setw(3) << setfill('0') << t << ":40 red " << nameOrder[0][(p[i]->pWarrior[0]->number - 1) % 5] <<
							" " << p[i]->pWarrior[0]->number << " was killed in city " << i << endl;
						if (p[i]->state < 0)
						{
							p[i]->state = 1;
						}
						else{
							p[i]->state++;
						}
						p[i]->record = 2;
						p[i]->pWarrior[1]->moralIncrease();
						p[i]->pWarrior[1]->yell(t);											//dragon yell
						p[i]->pWarrior[1]->getWeapon(p[i]->pWarrior[0]);					//wolf get weapon
						if (p[i]->lifeValue_City){
							outfile << setw(3) << setfill('0') << t << ":40 blue " << nameOrder[1][(p[i]->pWarrior[1]->number - 1) % 5] <<
								" " << p[i]->pWarrior[1]->number << " earned " << p[i]->lifeValue_City << " elements for his headquarter" << endl;
							bluelife += p[i]->lifeValue_City;
							p[i]->lifeValue_City = 0;
						}
						if (p[i]->state == 2 && p[i]->flag != 2){
							outfile << setw(3) << setfill('0') << t << ":40 blue flag raised in city " << i << endl;
							p[i]->flag = 2;
						}
					}
					else if (((p[i]->pWarrior[0]->number - 1) % 5) != 3) {	//red warrior is not dead and it's not a ninja
						outfile << setw(3) << setfill('0') << t << ":40 red " << nameOrder[0][(p[i]->pWarrior[0]->number - 1) % 5] <<
							" " << p[i]->pWarrior[0]->number << " fought back against blue " << nameOrder[1][(p[i]->pWarrior[1]->number - 1) % 5] <<
							" " << p[i]->pWarrior[1]->number << " in city " << i << endl;
						p[i]->pWarrior[0]->FightBack(p[i]->pWarrior[1]);
						if (p[i]->pWarrior[1]->IsDead()){
							p[i]->pWarrior[1]->available = 0;
							outfile << setw(3) << setfill('0') << t << ":40 blue " << nameOrder[1][(p[i]->pWarrior[1]->number - 1) % 5] <<
								" " << p[i]->pWarrior[1]->number << " was killed in city " << i << endl;
							if (p[i]->state > 0){
								p[i]->state = -1;
							}
							else{
								p[i]->state--;
							}
							p[i]->record = 1;
							p[i]->pWarrior[0]->moralIncrease();
							p[i]->pWarrior[0]->getWeapon(p[i]->pWarrior[1]);					//wolf get weapon
							if (p[i]->lifeValue_City){
								outfile << setw(3) << setfill('0') << t << ":40 red " << nameOrder[0][(p[i]->pWarrior[0]->number - 1) % 5] <<
									" " << p[i]->pWarrior[0]->number << " earned " << p[i]->lifeValue_City << " elements for his headquarter" << endl;
								redlife += p[i]->lifeValue_City;
								p[i]->lifeValue_City = 0;
							}
							if (p[i]->state == -2 && p[i]->flag != 1){
								outfile << setw(3) << setfill('0') << t << ":40 red flag raised in city " << i << endl;
								p[i]->flag = 1;
							}
						}
					}
				}
			}
			if (p[i]->record == 0 && (p[i]->pWarrior[0]->available || p[i]->pWarrior[1]->available)){		//a war that no one dies
				p[i]->state = 0;
				p[i]->pWarrior[0]->moralDecrease();
				p[i]->pWarrior[1]->moralDecrease();
				if (whowillyell == 1){
					p[i]->pWarrior[0]->yell(t);		//red dragons yell
				}
				else if (whowillyell == 2){
					p[i]->pWarrior[1]->yell(t);		//blue dragons yell
				}
			}
		}		//if num warrior is 2
	}		// for city 1 to N
	praiseWarrior(p1, p2, p, n);
	p1->lifeValue_Headquarter += redlife;
	p2->lifeValue_Headquarter += bluelife;
}

void reportElements(Headquarter *p1, Headquarter *p2, int t){
	outfile << setw(3) << setfill('0') << t << ":50 " << p1->lifeValue_Headquarter << " elements in red headquarter" << endl;
	outfile << setw(3) << setfill('0') << t << ":50 " << p2->lifeValue_Headquarter << " elements in blue headquarter" << endl;
}

void reportWeapon(Headquarter *p1, Headquarter *p2, City *p[], int n, int t){
	for (int i = 1; i <= n; i++){		//output red warriors' weapon info
		if (p[i]->num_warrior == 2){
			if (p[i]->pWarrior[0]->available){
				outfile << setw(3) << setfill('0') << t << ":55 red " << nameOrder[0][(p[i]->pWarrior[0]->number - 1) % 5] <<
					" " << p[i]->pWarrior[0]->number << " has ";
				if (p[i]->pWarrior[0]->weapon[2]){
					outfile << "arrow(" << p[i]->pWarrior[0]->arrowtime << ")";
					if (p[i]->pWarrior[0]->weapon[1] || p[i]->pWarrior[0]->weapon[0]){
						outfile << ",";
					}
				}
				if (p[i]->pWarrior[0]->weapon[1]){
					outfile << "bomb";
					if (p[i]->pWarrior[0]->weapon[0]){
						outfile << ",";
					}
				}
				if (p[i]->pWarrior[0]->weapon[0]){
					outfile << "sword(" << p[i]->pWarrior[0]->swordPower << ")";
				}
				if (!p[i]->pWarrior[0]->weapon[1] && !p[i]->pWarrior[0]->weapon[2] && !p[i]->pWarrior[0]->weapon[0]){
					outfile << "no weapon";
				}
				outfile << endl;
			}
		}
		else if (p[i]->num_warrior == 1){
			if (p[i]->pWarrior[0]->group == 1 && p[i]->pWarrior[0]->available){
				outfile << setw(3) << setfill('0') << t << ":55 red " << nameOrder[0][(p[i]->pWarrior[0]->number - 1) % 5] <<
					" " << p[i]->pWarrior[0]->number << " has ";
				if (p[i]->pWarrior[0]->weapon[2]){
					outfile << "arrow(" << p[i]->pWarrior[0]->arrowtime << ")";
					if (p[i]->pWarrior[0]->weapon[1] || p[i]->pWarrior[0]->weapon[0]){
						outfile << ",";
					}
				}
				if (p[i]->pWarrior[0]->weapon[1]){
					outfile << "bomb";
					if (p[i]->pWarrior[0]->weapon[0]){
						outfile << ",";
					}
				}
				if (p[i]->pWarrior[0]->weapon[0]){
					outfile << "sword(" << p[i]->pWarrior[0]->swordPower << ")";
				}
				if (!p[i]->pWarrior[0]->weapon[1] && !p[i]->pWarrior[0]->weapon[2] && !p[i]->pWarrior[0]->weapon[0]){
					outfile << "no weapon";
				}
				outfile << endl;
			}
		}
	}
	for (int i = 1; i <= p1->num_warrior; i++){		//red warriors that arrived blue headquarter
		if (p1->pWarrior[i]->arrived){
			outfile << setw(3) << setfill('0') << t << ":55 red " << nameOrder[0][(p1->pWarrior[i]->number - 1) % 5] <<
				" " << p1->pWarrior[i]->number << " has ";
			if (p1->pWarrior[i]->weapon[2]){
				outfile << "arrow(" << p1->pWarrior[i]->arrowtime << ")";
				if (p1->pWarrior[i]->weapon[1] || p1->pWarrior[i]->weapon[0]){
					outfile << ",";
				}
			}
			if (p1->pWarrior[i]->weapon[1]){
				outfile << "bomb";
				if (p1->pWarrior[i]->weapon[0]){
					outfile << ",";
				}
			}
			if (p1->pWarrior[i]->weapon[0]){
				outfile << "sword(" << p1->pWarrior[i]->swordPower << ")";
			}
			if (!p1->pWarrior[i]->weapon[1] && !p1->pWarrior[i]->weapon[2] && !p1->pWarrior[i]->weapon[0]){
				outfile << "no weapon";
			}
			outfile << endl;
		}
	}
	for (int i = 1; i <= p2->num_warrior; i++){		//blue warriors that arrived red headquarter
		if (p2->pWarrior[i]->arrived){
			outfile << setw(3) << setfill('0') << t << ":55 blue " << nameOrder[1][(p2->pWarrior[i]->number - 1) % 5] <<
				" " << p2->pWarrior[i]->number << " has ";
			if (p2->pWarrior[i]->weapon[2]){
				outfile << "arrow(" << p2->pWarrior[i]->arrowtime << ")";
				if (p2->pWarrior[i]->weapon[1] || p2->pWarrior[i]->weapon[0]){
					outfile << ",";
				}
			}
			if (p2->pWarrior[i]->weapon[1]){
				outfile << "bomb,";
				if (p2->pWarrior[i]->weapon[0]){
					outfile << ",";
				}
			}
			if (p2->pWarrior[i]->weapon[0]){
				outfile << "sword(" << p2->pWarrior[i]->swordPower << ")";
			}
			if (!p2->pWarrior[i]->weapon[1] && !p2->pWarrior[i]->weapon[2] && !p2->pWarrior[i]->weapon[0]){
				outfile << "no weapon";
			}
			outfile << endl;
		}
	}
	for (int i = 1; i <= n; i++){		//output blue warriors' weapon info in city 1 to N
		if (p[i]->num_warrior == 2){
			if (p[i]->pWarrior[1]->available){
				outfile << setw(3) << setfill('0') << t << ":55 blue " << nameOrder[1][(p[i]->pWarrior[1]->number - 1) % 5] <<
					" " << p[i]->pWarrior[1]->number << " has ";
				if (p[i]->pWarrior[1]->weapon[2]){
					outfile << "arrow(" << p[i]->pWarrior[1]->arrowtime << ")";
					if (p[i]->pWarrior[1]->weapon[1] || p[i]->pWarrior[1]->weapon[0]){
						outfile << ",";
					}
				}
				if (p[i]->pWarrior[1]->weapon[1]){
					outfile << "bomb";
					if (p[i]->pWarrior[1]->weapon[0]){
						outfile << ",";
					}
				}
				if (p[i]->pWarrior[1]->weapon[0]){
					outfile << "sword(" << p[i]->pWarrior[1]->swordPower << ")";
				}
				if (!p[i]->pWarrior[1]->weapon[1] && !p[i]->pWarrior[1]->weapon[2] && !p[i]->pWarrior[1]->weapon[0]){
					outfile << "no weapon";
				}
				outfile << endl;
			}
		}
		else if (p[i]->num_warrior == 1){
			if (p[i]->pWarrior[0]->group == 2 && p[i]->pWarrior[0]->available){
				outfile << setw(3) << setfill('0') << t << ":55 blue " << nameOrder[1][(p[i]->pWarrior[0]->number - 1) % 5] <<
					" " << p[i]->pWarrior[0]->number << " has ";
				if (p[i]->pWarrior[0]->weapon[2]){
					outfile << "arrow(" << p[i]->pWarrior[0]->arrowtime << ")";
					if (p[i]->pWarrior[0]->weapon[1] || p[i]->pWarrior[0]->weapon[0]){
						outfile << ",";
					}
				}
				if (p[i]->pWarrior[0]->weapon[1]){
					outfile << "bomb";
					if (p[i]->pWarrior[0]->weapon[0]){
						outfile << ",";
					}
				}
				if (p[i]->pWarrior[0]->weapon[0]){
					outfile << "sword(" << p[i]->pWarrior[0]->swordPower << ")";
				}
				if (!p[i]->pWarrior[0]->weapon[1] && !p[i]->pWarrior[0]->weapon[2] && !p[i]->pWarrior[0]->weapon[0]){
					outfile << "no weapon";
				}
				outfile << endl;
			}
		}
	}
}

int main(){
	int t;
	int minute[10] = { 0, 5, 10, 20, 30, 35, 38, 40, 50, 55 };
	int count = 1;		//canse number
	outfile.open("myfile.txt");
	cin >> t;
	while (t-- > 0){
		int time = 0;		//record the time
		int tmp = 0;		//select the time interval
		int hour = 0;
		judge = 0;
		cin >> M >> N >> R >> K >> T;
		cin >> monster_lifevalue[0] >> monster_lifevalue[1] >> monster_lifevalue[2] >> monster_lifevalue[3] >> monster_lifevalue[4];
		cin >> monster_attackpower[0] >> monster_attackpower[1] >> monster_attackpower[2] >> monster_attackpower[3] >> monster_attackpower[4];
		outfile << "Case " << count++ << ":" << endl;
		Headquarter Red(1, M), Blue(2, M);
		for (int i = 1; i <= N; i++){
			pCity[i] = new City(i);		//setting numbers for every city from 1 to N
		}
		while (time <= T){
			switch (minute[tmp % 10]){
			case 0:		//each headquarter generater their monsters if there is enough life value remanined
				if (Red.lifeValue_Headquarter >= monster_lifevalue[Order[0][Red.num_warrior % 5]]){
					Red.lifeValue_Headquarter -= monster_lifevalue[Order[0][Red.num_warrior % 5]];
					Red.generateMonster(1, Red.num_warrior + 1, monster_lifevalue[Order[0][Red.num_warrior % 5]],
						monster_attackpower[Order[0][Red.num_warrior % 5]], &Red, K, hour);
				}
				if (Blue.lifeValue_Headquarter >= monster_lifevalue[Order[1][Blue.num_warrior % 5]]){
					Blue.lifeValue_Headquarter -= monster_lifevalue[Order[1][Blue.num_warrior % 5]];
					Blue.generateMonster(2, Blue.num_warrior + 1, monster_lifevalue[Order[1][Blue.num_warrior % 5]],
						monster_attackpower[Order[1][Blue.num_warrior % 5]], &Blue, K, hour);
				}
				break;
			case 5:	//lions run away
				run(&Red, &Blue, pCity, N, hour);
				break;
			case 10:	//move forward
				forward(&Red, &Blue, pCity, N, hour);
				break;
			case 20:	//cities generate life value
				citiesGennerateLifeValue(pCity, N);
				break;
			case 30:	//get cities' life value if there is only one warrior in the city
				getCityValue(pCity, N, hour);
				break;
			case 35:	//shoot arrows
				shoot(pCity, N, R, hour);
				break;
			case 38:	//use bomb
				useBomb(pCity, N, hour);
				break;
			case 40:	//battle occurs
				battle(&Red, &Blue, pCity, N, hour);
				break;
			case 50:
				reportElements(&Red, &Blue, hour);
				break;
			case 55:
				reportWeapon(&Red, &Blue, pCity, N, hour);
				break;
			default:
				break;
			}
			if (judge){
				break;
			}
			hour += ((tmp % 10 == 9) ? 1 : 0);
			tmp++;
			time = hour * 60 + minute[tmp % 10];
		}
	}
	system("pause");
	return 0;
}