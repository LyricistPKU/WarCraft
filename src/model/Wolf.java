package model;

import java.io.IOException;

public class Wolf extends Warrior{

	public Wolf(int _group, int _number, int _lifeValue, int _attackPower,
			Headquarter _headquarter) {
		super(_group, _number, _lifeValue, _attackPower, _headquarter);
	}

	@Override
	public void Fight(Warrior warrior) {
		warrior.Hurt(attackPower + (weapon[0] == 1 ? swordPower : 0));
		swordPower = (int)(swordPower * 0.8);
		if(swordPower == 0) weapon[0] = 0;
	}

	@Override
	public void Hurt(int power) {
		lifeValue -= power;
	}

	@Override
	public void FightBack(Warrior warrior) {
		warrior.Hurt((int)(attackPower / 2) + (weapon[0] == 1 ? swordPower : 0));
		swordPower = (int)(swordPower * 0.8);
		if(swordPower == 0) weapon[0] = 0;
	}

	@Override
	public void Born(int t) {
		String s = (group == 1) ? " red " : " blue ";
		StringBuffer sb =  new StringBuffer();
		sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":00");
		sb.append(s + "wolf " + String.valueOf(number) + " born\n");
		try {
			Util.outputStream.write(sb.toString().getBytes("GBK"));
		} catch (IOException e) {
			System.out.println("print wolf born message error.");
			e.printStackTrace();
		}	
	}

	@Override
	public void yell(int t) {
	}

	@Override
	public void moralIncrease() {	
	}

	@Override
	public void moralDecrease() {	
	}

	@Override
	public void getWeapon(Warrior warrior) {
		if(warrior.available == 0){
			if(weapon[0] == 0){
				weapon[0] = warrior.weapon[0];
				swordPower = warrior.swordPower;
			}
			if(weapon[1] == 0) weapon[1] = warrior.weapon[1];
			if(weapon[2] == 0){
				weapon[2] = warrior.weapon[2];
				arrowtime = warrior.arrowtime;
			}
		}
	}
	
}
