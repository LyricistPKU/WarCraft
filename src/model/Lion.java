package model;

import java.io.IOException;

public class Lion extends Warrior{
	private int loyalty;
	private int Kvalue;
	public Lion(int _group, int _number, int _lifeValue, int _attackPower,
			Headquarter _headquarter, int _K) {
		super(_group, _number, _lifeValue, _attackPower, _headquarter);
		Kvalue = _K;
		loyalty = _headquarter.lifeValue_Headquarter;
	}

	@Override
	public void Fight(Warrior warrior) {
		warrior.Hurt(attackPower + (weapon[0] == 1 ? swordPower : 0));
		if(warrior.lifeValue > 0){
			loyalty -= Kvalue;
		}
		swordPower = (int)(swordPower * 0.8);
		if(swordPower == 0) weapon[0] = 0;
	}

	@Override
	public void Hurt(int power) {
		if(power >= lifeValue){
			if(group == 1) city.warrior[1].lifeValue += lifeValue;
			else if(group == 2) city.warrior[0].lifeValue += lifeValue;
		}
		lifeValue -= power;
	}

	@Override
	public void FightBack(Warrior warrior) {
		warrior.Hurt((int)(attackPower / 2) + (weapon[0] == 1 ? swordPower : 0));
		if(warrior.lifeValue > 0) loyalty -= Kvalue;
		swordPower = (int)(swordPower * 0.8);
		if(swordPower == 0) weapon[0] = 0;
	}

	@Override
	public void Born(int t) {
		String s = (group == 1) ? " red " : " blue ";
		StringBuffer sb =  new StringBuffer();
		sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":00");
		sb.append(s + "lion " + String.valueOf(number) + " born\n");
		sb.append("Its loyalty is " + String.valueOf(loyalty) + "\n");
		try {
			Util.outputStream.write(sb.toString().getBytes("GBK"));
		} catch (IOException e) {
			System.out.println("print lion born message error.");
			e.printStackTrace();
		}	
	}
	
	@Override
	public boolean isRunAway() {
		return (loyalty <= 0);
	}

	@Override
	public void yell(int t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moralIncrease() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moralDecrease() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getWeapon(Warrior warrior) {
		// TODO Auto-generated method stub
		
	}
	
}
