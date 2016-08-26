import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import model.*;

public class Test {

	public static void main(String[] args) {
		int t;
		int minute[] = { 0, 5, 10, 20, 30, 35, 38, 40, 50, 55 };		
		int count = 1;	//case numer
		City pcity[] = new City[Util.NUM];
		try {
			Util.outputStream = new FileOutputStream("myfile.txt");
		} catch (FileNotFoundException e) {
			System.out.println("File OutputStream error.");
			e.printStackTrace();
		}
		Scanner in=new Scanner(System.in);
		t = in.nextInt();
		while(t-- > 0){
			int time = 0;	//record the time
			int tmp = 0;	//record the interval
			int hour = 0;
			Util.judge = 0;
			Util.M = in.nextInt();
			Util.N = in.nextInt();
			Util.R = in.nextInt();
			Util.K = in.nextInt();
			Util.T = in.nextInt();
			for(int i = 0; i < 5; i++) Util.monster_lifeValue[i] = in.nextInt();
			for(int i = 0; i < 5; i++) Util.monster_atatckPower[i] = in.nextInt();
			StringBuilder sb = new StringBuilder();
			sb.append("Case " + String.valueOf(count++) + ":\n");
			try {
				Util.outputStream.write(sb.toString().getBytes("GBK"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			Headquarter Red = new Headquarter(1, Util.M);
			Headquarter Blue = new Headquarter(2, Util.M);
			for(int i = 1; i <= Util.N; i++){
				pcity[i] = new City(i);
			}
			while(time <= Util.T){
				switch (minute[tmp % 10]) {
				case 0:	//each headquarter generater their monsters if there is enough life value remanined
					if(Red.lifeValue_Headquarter >= Util.monster_lifeValue[Util.Order[0][Red.num_warrior % 5]]){
						Red.lifeValue_Headquarter -= Util.monster_lifeValue[Util.Order[0][Red.num_warrior % 5]];
						Red.generateMonster(1, Red.num_warrior + 1, Util.monster_lifeValue[Util.Order[0][Red.num_warrior % 5]]
								, Util.monster_atatckPower[Util.Order[0][Red.num_warrior % 5]], Red, Util.K, hour);
					}
					if(Blue.lifeValue_Headquarter >= Util.monster_lifeValue[Util.Order[1][Blue.num_warrior % 5]]){
						Blue.lifeValue_Headquarter -= Util.monster_lifeValue[Util.Order[1][Blue.num_warrior % 5]];
						Blue.generateMonster(2, Blue.num_warrior + 1, Util.monster_lifeValue[Util.Order[1][Blue.num_warrior % 5]]
								, Util.monster_atatckPower[Util.Order[1][Blue.num_warrior % 5]], Blue, Util.K, hour);
					}
					break;
				case 5:	//lions run away
					Util.run(Red, Blue, pcity, Util.N, hour);
					break;
				case 10:	//move forward
					Util.forward(Red, Blue, pcity, Util.N, hour);
					break;
				case 20:	//cities generate life value
					Util.citiesGenerateLifeValue(pcity, Util.N);
					break;
				case 30:	//get cities' life value if there is only one warrior in the city
					Util.getCityValue(pcity, Util.N, hour);
					break;
				case 35:	//shoot arrows
					Util.shoot(pcity, Util.N, Util.R, hour);
					break;
				case 38:	//use bomb
					Util.useBomb(pcity, Util.N, hour);
					break;
				case 40:
					Util.battle(Red, Blue, pcity, Util.N, hour);
					break;
				case 50:
					Util.reportElements(Red, Blue, hour);
					break;
				case 55:
					Util.reportWeapon(Red, Blue, pcity, Util.N, hour);
					break;
				default:
					break;
				}
				if(Util.judge == 1){
					break;
				}
				hour += (tmp % 10 == 9) ? 1 : 0;
				tmp++;
				time = hour * 60 + minute[tmp % 10];
			}
		}
		in.close();
		System.out.println("Program finished!");
	}
}
