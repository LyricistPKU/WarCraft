package model;

import java.io.IOException;

public class Iceman extends Warrior{
	private int step;
	public Iceman(int _group, int _number, int _lifeValue, int _attackPower,
			Headquarter _headquarter) {
		super(_group, _number, _lifeValue, _attackPower, _headquarter);
		step = 0;
		weapon[number % 3] = 1;
		if(number % 3 == 0) swordPower = (int)(attackPower * 0.2);
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
		sb.append(s + "iceman " + String.valueOf(number) + " born\n");
		try {
			Util.outputStream.write(sb.toString().getBytes("GBK"));
		} catch (IOException e) {
			System.out.println("print iceman born message error.");
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
	}

	@Override
	public void Move() {
		if (group == 1){
			position++;
		}
		else if(group == 2){
			position--;
		}
		step++;
		if(step == 2){
			if(lifeValue > 9){
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

}
