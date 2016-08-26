package model;

import java.io.IOException;
import java.text.DecimalFormat;

public class Dragon extends Warrior{
	public double moral;
	public Dragon(int _group, int _number, int _lifeValue, int _attackPower,
			Headquarter _headquarter) {
		super(_group, _number, _lifeValue, _attackPower, _headquarter);
		moral = (double)(_headquarter.lifeValue_Headquarter) / (double)(lifeValue);
		weapon[number % 3] =1;
		if(number % 3 == 0) swordPower = (int) (attackPower * 0.2);
		
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
		sb.append(s + "dragon " + String.valueOf(number) + " born\n");
		DecimalFormat df = new DecimalFormat("#.00");
		sb.append("Its morale is " + String.valueOf(df.format(moral)) + "\n");
		try {
			Util.outputStream.write(sb.toString().getBytes("GBK"));
		} catch (IOException e) {
			System.out.println("print dragon born message error.");
			e.printStackTrace();
		}
	}
	@Override
	public void yell(int t) {
		if(moral > 0.8 && available == 1){
			String s = (group == 1) ? " red " : " blue ";
			StringBuffer sb =  new StringBuffer();
			sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":40");
			sb.append(s + "dragon " + String.valueOf(number) + " yelled in city " + String.valueOf(city.num) + "\n");
			try {
				Util.outputStream.write(sb.toString().getBytes("GBK"));
			} catch (IOException e) {
				System.out.println("print dragon born message error.");
				e.printStackTrace();
			}			
		}		
	}
	@Override
	public void moralIncrease() {
		moral += (double)0.8;
	}
	@Override
	public void moralDecrease() {
		moral -= (double)0.2;
	}
	@Override
	public void getWeapon(Warrior warrior) {
	}	
}
