package model;

import java.io.FileOutputStream;
import java.io.IOException;

public class Util {
	public final static int SIZE = 10000;
	public final static int NUM = 100;
	public static int judge = 0;	//end of a case;
	public static int M, N, R, K, T;
	public static int[] monster_lifeValue = new int[5];
	public static int[] monster_atatckPower = new int[5];
	public final static int[][] Order = { { 2, 3, 4, 1, 0 }, { 3, 0, 1, 2, 4 } };	//0 dragon 1 ninja 2 iceman 3 lion 4 wolf;
	public final static String[][] nameOrder = { { "iceman", "lion", "wolf", "ninja", "dragon" }, { "lion", "dragon", "ninja", "iceman", "wolf" } };
	public static FileOutputStream outputStream;
	
	public static void run(Headquarter p1, Headquarter p2, City p[], int n, int t) {		//lions run away
		if(p1.self == 1){		//there is a warrior in the headquarter
			if(p1.warrior[p1.num_warrior].isRunAway()){
				p1.warrior[p1.num_warrior].available = 0;
				p1.self = 0;
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":05 red lion ");
				sb.append(String.valueOf(p1.num_warrior) + " ran away\n");
				try {
					Util.outputStream.write(sb.toString().getBytes("GBK"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		for(int i = 1; i <= n; i++){
			if(p[i].num_warrior >= 1){
				if(p[i].warrior[0].isRunAway()){
					p[i].warrior[0].available = 0;
					String s = (p[i].warrior[0].group == 1) ? " red " : " blue ";
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":05");
					sb.append(s + "lion " + String.valueOf(p[i].warrior[0].number) + " ran away\n");
					try {
						Util.outputStream.write(sb.toString().getBytes("GBK"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			if(p[i].num_warrior == 2){
				if(p[i].warrior[1].isRunAway()){
					p[i].warrior[1].available = 0;
					String s = (p[i].warrior[1].group == 1) ? " red " : " blue ";
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":05");
					sb.append(s + "lion " + String.valueOf(p[i].warrior[1].number) + " ran away\n");
					try {
						Util.outputStream.write(sb.toString().getBytes("GBK"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}				
			}
		}
		if(p2.self == 1){		//there is a warrior in the headquarter
			if(p2.warrior[p2.num_warrior].isRunAway()){
				p2.warrior[p2.num_warrior].available = 0;
				p2.self = 0;
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":05 blue lion ");
				sb.append(String.valueOf(p2.num_warrior) + " ran away\n");
				try {
					Util.outputStream.write(sb.toString().getBytes("GBK"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void refreashCities(Headquarter p1, Headquarter p2, City p[], int n){
		for(int i = 1; i <= n; i++){		//initialze all the cities
			p[i].num_warrior = 0;
			p[i].record = 0;				//record successive win 
			p[i].warrior[0] = null;
			p[i].warrior[1] = null;
		}
		for(int i = 1; i <= p1.num_warrior; i++){	//reposite all red monsters
			if(p1.warrior[i].available == 1){
				p[p1.warrior[i].position].warrior[p[p1.warrior[i].position].num_warrior++] = p1.warrior[i];
				p1.warrior[i].city = p[p1.warrior[i].position];
			}
			if(p1.warrior[i].arrowtime == 0) p1.warrior[i].weapon[2] = 0;
			if(p1.warrior[i].swordPower == 0) p1.warrior[i].weapon[0] = 0;
		}
		for(int i = 1; i <= p2.num_warrior; i++){	//reposite all blue monsters
			if(p2.warrior[i].available == 1){
				p[p2.warrior[i].position].warrior[p[p2.warrior[i].position].num_warrior++] = p2.warrior[i];
				p2.warrior[i].city = p[p2.warrior[i].position];
			}
			if(p2.warrior[i].arrowtime == 0) p2.warrior[i].weapon[2] = 0;
			if(p2.warrior[i].swordPower == 0) p2.warrior[i].weapon[0] = 0;
		}
	}
	
	public static boolean hasRed(City p){		//judge if there is a red warrior in the city
		return (p.num_warrior >= 1 && p.warrior[0].group == 1 && p.warrior[0].available == 1);
	}
	
	public static boolean hasBlue(City p){		//judge if there is a blue warrior in the city
		return ((p.num_warrior == 1 && p.warrior[0].group == 2 && p.warrior[0].available == 1) ||
				(p.num_warrior == 2 && p.warrior[1].group == 2 && p.warrior[1].available == 1));
	}
	
	public static void forward(Headquarter p1, Headquarter p2, City p[], int n, int t){		//every monster move forward
		if(hasBlue(p[1])){		//blue warrior move into red headquarter
			int j = (p[1].num_warrior == 1 && p[1].warrior[0].group == 2 && p[1].warrior[0].available == 1) ? 0 : 1;
			p1.arrived++;
			p[1].warrior[j].available = 0;
			p[1].warrior[j].arrived = 1;
			p[1].warrior[j].Move();
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":10 blue ");
			sb.append(Util.nameOrder[1][(p[1].warrior[j].number - 1) % 5] + " " + String.valueOf(p[1].warrior[j].number));
			sb.append(" reached red headquarter with " + String.valueOf(p[1].warrior[j].lifeValue));
			sb.append(" elements and force " + String.valueOf(p[1].warrior[j].attackPower) + "\n");
			if(p1.arrived == 2){
				sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":10 red headquarter was taken\n");
				Util.judge = 1;
			}
			try {
				Util.outputStream.write(sb.toString().getBytes("GBK"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(p1.self == 1){	//move warrior in the red headquarter to city 1
			p1.warrior[p1.num_warrior].Move();
			p1.self = 0;
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":10 red ");
			sb.append(Util.nameOrder[0][(p1.num_warrior - 1) % 5] + " " + String.valueOf(p1.num_warrior));
			sb.append(" marched to city 1 with " + String.valueOf(p1.warrior[p1.num_warrior].lifeValue));
			sb.append(" elements and force " + String.valueOf(p1.warrior[p1.num_warrior].attackPower) + "\n");
			try {
				Util.outputStream.write(sb.toString().getBytes("GBK"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(n > 1){			//mobe warrior from city 2 to city 1
			if(hasBlue(p[2])){
				int j = (p[2].num_warrior == 1 && p[2].warrior[0].group == 2 && p[2].warrior[0].available == 1) ? 0 : 1;
				p[2].warrior[j].Move();
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":10 blue ");
				sb.append(Util.nameOrder[1][(p[2].warrior[j].number - 1) % 5] + " " + String.valueOf(p[2].warrior[j].number));
				sb.append(" marched to city 1 with " + String.valueOf(p[2].warrior[j].lifeValue));
				sb.append(" elements and force " + String.valueOf(p[2].warrior[j].attackPower) + "\n");
				try {
					Util.outputStream.write(sb.toString().getBytes("GBK"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		for(int i = 2; i <= n - 1; i++){	//move red and blue warriors to city 2 to N - 1
			if(hasRed(p[i -1])){
				p[i - 1].warrior[0].Move();
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":10 red ");
				sb.append(Util.nameOrder[0][(p[i - 1].warrior[0].number - 1) % 5] + " " + String.valueOf(p[i - 1].warrior[0].number));
				sb.append(" marched to city " + String.valueOf(i) + " with " + String.valueOf(p[i - 1].warrior[0].lifeValue));
				sb.append(" elements and force " + String.valueOf(p[i - 1].warrior[0].attackPower) + "\n");
				try {
					Util.outputStream.write(sb.toString().getBytes("GBK"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(hasBlue(p[i + 1])){
				int j = (p[i + 1].num_warrior == 1 && p[i + 1].warrior[0].group == 2 && p[i + 1].warrior[0].available == 1) ? 0 : 1;
				p[i + 1].warrior[j].Move();
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":10 blue ");
				sb.append(Util.nameOrder[1][(p[i + 1].warrior[j].number - 1) % 5] + " " + String.valueOf(p[i + 1].warrior[j].number));
				sb.append(" marched to city " + String.valueOf(i) + " with " + String.valueOf(p[i + 1].warrior[j].lifeValue));
				sb.append(" elements and force " + String.valueOf(p[i + 1].warrior[j].attackPower) + "\n");
				try {
					Util.outputStream.write(sb.toString().getBytes("GBK"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if(n > 1){			//mobe warrior from city N - 1 to city N
			if(hasRed(p[n - 1])){
				p[n -1].warrior[0].Move();
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":10 red ");
				sb.append(Util.nameOrder[0][(p[n - 1].warrior[0].number - 1) % 5] + " " + String.valueOf(p[n - 1].warrior[0].number));
				sb.append(" marched to city " + String.valueOf(n) + " with " + String.valueOf(p[n - 1].warrior[0].lifeValue));
				sb.append(" elements and force " + String.valueOf(p[n - 1].warrior[0].attackPower) + "\n");
				try {
					Util.outputStream.write(sb.toString().getBytes("GBK"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if(p2.self == 1){	//move warrior in the blue camp to city N
			p2.warrior[p2.num_warrior].Move();
			p2.self = 0;
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":10 red ");
			sb.append(Util.nameOrder[1][(p2.num_warrior - 1) % 5] + " " + String.valueOf(p2.num_warrior));
			sb.append(" marched to city " + String.valueOf(n) + " with " + String.valueOf(p2.warrior[p2.num_warrior].lifeValue));
			sb.append(" elements and force " + String.valueOf(p2.warrior[p1.num_warrior].attackPower) + "\n");
			try {
				Util.outputStream.write(sb.toString().getBytes("GBK"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(hasRed(p[n])){	//move red warrior to the blue camp
			p2.arrived++;
			p[n].warrior[0].available = 0;
			p[n].warrior[0].arrived = 1;
			p[n].warrior[0].Move();
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":10 red ");
			sb.append(Util.nameOrder[0][(p[n].warrior[0].number - 1) % 5] + " " + String.valueOf(p[n].warrior[0].number));
			sb.append(" reached blue headquarter with " + String.valueOf(p[n].warrior[0].lifeValue));
			sb.append(" elements and force " + String.valueOf(p[n].warrior[0].attackPower) + "\n");
			if(p1.arrived == 2){
				sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":10 blue headquarter was taken\n");
				Util.judge = 1;
			}
			try {
				Util.outputStream.write(sb.toString().getBytes("GBK"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		refreashCities(p1, p2, p, n);		//leave out the dead warrior
	}
	
	public static void citiesGenerateLifeValue(City p[], int n){
		for(int i = 1; i <= n; i++) p[i].lifeValue_City += 10;
	}
	
	public static void getCityValue(City p[], int n, int t){
		for(int i = 1; i <= n; i++){
			if(p[i].num_warrior == 1 && p[i].lifeValue_City > 0){
				String s = (p[i].warrior[0].group == 1) ? " red " : " blue ";
				p[i].warrior[0].headquarter.lifeValue_Headquarter += p[i].lifeValue_City;
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":30");
				sb.append(s + Util.nameOrder[p[i].warrior[0].group - 1][(p[i].warrior[0].number - 1) % 5]);
				sb.append(" " + String.valueOf(p[i].warrior[0].number));
				sb.append(" earned " + String.valueOf(p[i].lifeValue_City) + " elements for his headquarter\n");
				try {
					Util.outputStream.write(sb.toString().getBytes("GBK"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				p[i].lifeValue_City = 0;
			}
		}
	}
	
	public static void shoot(City p[], int n, int r, int t){
		if(n > 1){
			for(int i =1; i <= n; i++){
				if(i != n){		
					if(p[i].num_warrior > 0 && p[i + 1].num_warrior == 1){ //red[0] shoot blue[0]
						if(p[i].warrior[0].group == 1 && p[i].warrior[0].weapon[2] == 1 && p[i].warrior[0].arrowtime > 0 && p[i + 1].warrior[0].group == 2){
							p[i + 1].warrior[0].lifeValue -= r;
							p[i].warrior[0].arrowtime--;
							if(p[i].warrior[0].arrowtime == 0) p[i].warrior[0].weapon[2] = 0;
							StringBuilder sb = new StringBuilder();
							if(p[i + 1].warrior[0].isDead()){
								p[i + 1].warrior[0].available = 0;								
								sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":35 red ");
								sb.append(Util.nameOrder[0][(p[i].warrior[0].number - 1) % 5] + " ");
								sb.append(String.valueOf(p[i].warrior[0].number) + " shot and killed blue ");
								sb.append(Util.nameOrder[1][(p[i + 1].warrior[0].number - 1) % 5] + " ");
								sb.append(String.valueOf(p[i + 1].warrior[0].number) + "\n");
							}
							else{
								sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":35 red ");
								sb.append(Util.nameOrder[0][(p[i].warrior[0].number - 1) % 5] + " ");
								sb.append(String.valueOf(p[i].warrior[0].number) + " shot\n");
							}
							try {
								Util.outputStream.write(sb.toString().getBytes("GBK"));
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					if(p[i].num_warrior > 0 && p[i + 1].num_warrior == 2){ //red[0] shoot blue[1]
						if(p[i].warrior[0].group == 1 && p[i].warrior[0].weapon[2] == 1 && p[i].warrior[0].arrowtime > 0 && p[i + 1].warrior[1].group == 2){
							p[i + 1].warrior[1].lifeValue -= r;
							p[i].warrior[0].arrowtime--;
							if(p[i].warrior[0].arrowtime == 0) p[i].warrior[0].weapon[2] = 0;
							StringBuilder sb = new StringBuilder();
							if(p[i + 1].warrior[1].isDead()){
								p[i + 1].warrior[1].available = 0;								
								sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":35 red ");
								sb.append(Util.nameOrder[0][(p[i].warrior[0].number - 1) % 5] + " ");
								sb.append(String.valueOf(p[i].warrior[0].number) + " shot and killed blue ");
								sb.append(Util.nameOrder[1][(p[i + 1].warrior[1].number - 1) % 5] + " ");
								sb.append(String.valueOf(p[i + 1].warrior[1].number) + "\n");
							}
							else{
								sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":35 red ");
								sb.append(Util.nameOrder[0][(p[i].warrior[0].number - 1) % 5] + " ");
								sb.append(String.valueOf(p[i].warrior[0].number) + " shot\n");
							}
							try {
								Util.outputStream.write(sb.toString().getBytes("GBK"));
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
				if(i != 1){
					if(p[i].num_warrior == 1 && p[i - 1].num_warrior > 0){	//blue[0] shoot red[0]
						if(p[i].warrior[0].group == 2 && p[i].warrior[0].weapon[2] == 1 && p[i].warrior[0].arrowtime > 0 && p[i - 1].warrior[0].group == 1){
							p[i - 1].warrior[0].lifeValue -= r;
							p[i].warrior[0].arrowtime--;
							if(p[i].warrior[0].arrowtime == 0) p[i].warrior[0].weapon[2] = 0;
							StringBuilder sb = new StringBuilder();
							if(p[i - 1].warrior[0].isDead()){
								p[i - 1].warrior[0].available = 0;								
								sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":35 blue ");
								sb.append(Util.nameOrder[1][(p[i].warrior[0].number - 1) % 5] + " ");
								sb.append(String.valueOf(p[i].warrior[0].number) + " shot and killed red ");
								sb.append(Util.nameOrder[0][(p[i - 1].warrior[0].number - 1) % 5] + " ");
								sb.append(String.valueOf(p[i - 1].warrior[0].number) + "\n");
							}
							else{
								sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":35 blue ");
								sb.append(Util.nameOrder[1][(p[i].warrior[0].number - 1) % 5] + " ");
								sb.append(String.valueOf(p[i].warrior[0].number) + " shot\n");
							}
							try {
								Util.outputStream.write(sb.toString().getBytes("GBK"));
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					if(p[i].num_warrior == 2 && p[i - 1].num_warrior > 0){	//blue[1] shoot red[0]
						if(p[i].warrior[1].group == 2 && p[i].warrior[1].weapon[2] == 1 && p[i].warrior[1].arrowtime > 0 && p[i - 1].warrior[0].group == 1){
							p[i - 1].warrior[0].lifeValue -= r;
							p[i].warrior[1].arrowtime--;
							if(p[i].warrior[1].arrowtime == 0) p[i].warrior[1].weapon[2] = 0;
							StringBuilder sb = new StringBuilder();
							if(p[i - 1].warrior[0].isDead()){
								p[i - 1].warrior[0].available = 0;								
								sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":35 blue ");
								sb.append(Util.nameOrder[1][(p[i].warrior[1].number - 1) % 5] + " ");
								sb.append(String.valueOf(p[i].warrior[1].number) + " shot and killed red ");
								sb.append(Util.nameOrder[0][(p[i - 1].warrior[0].number - 1) % 5] + " ");
								sb.append(String.valueOf(p[i - 1].warrior[0].number) + "\n");
							}
							else{
								sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":35 blue ");
								sb.append(Util.nameOrder[1][(p[i].warrior[1].number - 1) % 5] + " ");
								sb.append(String.valueOf(p[i].warrior[1].number) + " shot\n");
							}
							try {
								Util.outputStream.write(sb.toString().getBytes("GBK"));
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
	
	public static int whoWillDie(City p){		//judge who will die after a war, 0 for no one, 1 for red, 2 for blue
		int lifered = p.warrior[0].lifeValue;
		int lifeblue = p.warrior[1].lifeValue;
		if(p.flag == 1 || (p.flag == 0 && (p.num % 2 == 1))){
			lifeblue -= (p.warrior[0].attackPower + (p.warrior[0].weapon[0] == 1 ? p.warrior[0].swordPower : 0));
			if(lifeblue <= 0) return 2;
			if((p.warrior[1].number - 1) % 5 != 2){		//blue warrior is not ninja
				lifered -= ((int)(p.warrior[1].attackPower / 2) + (p.warrior[1].weapon[0] == 1 ? p.warrior[1].swordPower : 0));
				if(lifered <= 0) return 1;
			}
		}
		else{
			lifered -= (p.warrior[1].attackPower + (p.warrior[1].weapon[0] == 1 ? p.warrior[0].swordPower : 0));	
			if(lifered <= 0) return 1;
			if((p.warrior[0].number - 1) % 5 != 3){		//red warrior is not ninja
				lifeblue -= ((int)(p.warrior[0].attackPower / 2) + (p.warrior[0].weapon[0] == 1 ? p.warrior[0].swordPower : 0));
				if(lifeblue <= 0) return 2;
			}
		}
		return 0;
	}
	
	public static void useBomb(City p[], int n, int t) {
		for(int i = 1; i <= n; i++){
			if(p[i].num_warrior == 2 && p[i].warrior[0].available == 1 && p[i].warrior[1].available == 1){
				if(p[i].warrior[0].weapon[1] == 1 && whoWillDie(p[i]) == 1){	//red warrior use bomb
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":38 red ");
					sb.append(Util.nameOrder[0][(p[i].warrior[0].number - 1) % 5] + " ");
					sb.append(String.valueOf(p[i].warrior[0].number) + " used a bomb and killed blue ");
					sb.append(Util.nameOrder[1][(p[i].warrior[1].number - 1) % 5] + " ");
					sb.append(String.valueOf(p[i].warrior[1].number) + "\n");
					try {
						Util.outputStream.write(sb.toString().getBytes("GBK"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					p[i].warrior[0].lifeValue = 0;
					p[i].warrior[1].lifeValue = 0;
					p[i].warrior[0].available = 0;
					p[i].warrior[1].available = 0;
				}
				else if(p[i].warrior[1].weapon[1] == 1 && whoWillDie(p[i]) == 2){	//blue warrior use bomb
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":38 blue ");
					sb.append(Util.nameOrder[1][(p[i].warrior[1].number - 1) % 5] + " ");
					sb.append(String.valueOf(p[i].warrior[1].number) + " used a bomb and killed red ");
					sb.append(Util.nameOrder[0][(p[i].warrior[0].number - 1) % 5] + " ");
					sb.append(String.valueOf(p[i].warrior[0].number) + "\n");
					try {
						Util.outputStream.write(sb.toString().getBytes("GBK"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					p[i].warrior[0].lifeValue = 0;
					p[i].warrior[1].lifeValue = 0;
					p[i].warrior[0].available = 0;
					p[i].warrior[1].available = 0;
				}
			}
		}
	}
	
	public static void praiseWarrior(Headquarter p1, Headquarter p2, City p[], int n){
		for(int i = 1; i <= n; i++){
			if(p[i].num_warrior == 2 && p[i].record == 1 && p1.lifeValue_Headquarter >= 8){
				p[i].warrior[0].lifeValue += 8;
				p1.lifeValue_Headquarter -= 8;
			}
		}
		for(int i = n; i >= 1; i--){
			if(p[i].num_warrior == 2 && p[i].record == 2 && p2.lifeValue_Headquarter >= 8){
				p[i].warrior[1].lifeValue += 8;
				p2.lifeValue_Headquarter -= 8;
			}
		}
	}
	
	public static void getBattleValue(Headquarter p1, Headquarter p2, City p[], int n, int t){
		for(int i = 1; i <= n; i++){
			if(p[i].num_warrior == 2){
				if(p[i].record == 1){
					p1.lifeValue_Headquarter += p[i].lifeValue_City;
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":40 red ");
					sb.append(Util.nameOrder[0][(p[i].warrior[0].number - 1) % 5] + " ");
					sb.append(String.valueOf(p[i].warrior[0].number) + " earned ");
					sb.append(String.valueOf(p[i].lifeValue_City) + " elements for his headquarter\n");
					try {
						Util.outputStream.write(sb.toString().getBytes("GBK"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else if(p[i].record == 2){
					p2.lifeValue_Headquarter += p[i].lifeValue_City;
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":40 blue ");
					sb.append(Util.nameOrder[1][(p[i].warrior[1].number - 1) % 5] + " ");
					sb.append(String.valueOf(p[i].warrior[1].number) + " earned ");
					sb.append(String.valueOf(p[i].lifeValue_City) + " elements for his headquarter\n");
					try {
						Util.outputStream.write(sb.toString().getBytes("GBK"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public static void flagRise(City p[], int n, int t){
		for(int i = 1; i <= n; i++){
			if(p[i].num_warrior == 2){
				if(p[i].record == 1 && p[i].state == -2){
					p[i].flag = 1;
					p[i].state = 0;
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":40 red flag raised in city ");
					sb.append(String.valueOf(i) + "\n");
					try {
						Util.outputStream.write(sb.toString().getBytes("GBK"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else if(p[i].record == 2 && p[i].state == 2){
					p[i].flag = 2;
					p[i].state = 0;
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":40 blue flag raised in city ");
					sb.append(String.valueOf(i) + "\n");
					try {
						Util.outputStream.write(sb.toString().getBytes("GBK"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public static boolean whoWillAttack(City p) {		//judge who will attack first, true for red, false for blue
		if(p.flag == 1 || (p.flag == 0 && p.num % 2 == 1)) return true;
		else return false;
	}
	
	public static void battle(Headquarter p1, Headquarter p2, City p[], int n, int t) {
		int redlife = 0;
		int bluelife = 0;
		for(int i = 1; i <= n; i++){
			if(p[i].num_warrior == 2){		//happen only when two warriors in a city
				int whowillattack = 0;		//judge who attack first, 0 for no attack, 1 for red, 2 for blue
				if(p[i].warrior[0].available == 1 && p[i].warrior[1].available == 0){	//blue warrior shot dead by arrow
					if(p[i].state > 0) p[i].state = -1;
					else p[i].state--;
					p[i].record = 1;
					p[i].warrior[0].moralIncrease();
					if(whoWillAttack(p[i])) p[i].warrior[0].yell(t);
					p[i].warrior[0].getWeapon(p[i].warrior[1]);
					if(p[i].lifeValue_City > 0){
						StringBuilder sb = new StringBuilder();
						sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":40 red ");
						sb.append(Util.nameOrder[0][(p[i].warrior[0].number - 1) % 5] + " ");
						sb.append(String.valueOf(p[i].warrior[0].number) + " earned ");
						sb.append(String.valueOf(p[i].lifeValue_City) + " elements for his headquarter\n");
						try {
							Util.outputStream.write(sb.toString().getBytes("GBK"));
						} catch (IOException e) {
							e.printStackTrace();
						}
						redlife += p[i].lifeValue_City;
						p[i].lifeValue_City = 0;
					}
					if(p[i].state == -2 && p[i].flag != 1){
						StringBuilder sb = new StringBuilder();
						sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":40 red flag raised in city ");
						sb.append(String.valueOf(i) + "\n");
						try {
							Util.outputStream.write(sb.toString().getBytes("GBK"));
						} catch (IOException e) {
							e.printStackTrace();
						}
						p[i].flag = 1;
					}
				}
				else if(p[i].warrior[1].available == 1 && p[i].warrior[0].available == 0){	//red warrior shot dead by arrow
					if(p[i].state < 0) p[i].state = 1;
					else p[i].state++;
					p[i].record = 2;
					p[i].warrior[1].moralIncrease();
					if(!whoWillAttack(p[i])) p[i].warrior[1].yell(t);
					p[i].warrior[1].getWeapon(p[i].warrior[0]);
					if(p[i].lifeValue_City > 0){
						StringBuilder sb = new StringBuilder();
						sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":40 blue ");
						sb.append(Util.nameOrder[1][(p[i].warrior[1].number - 1) % 5] + " ");
						sb.append(String.valueOf(p[i].warrior[1].number) + " earned ");
						sb.append(String.valueOf(p[i].lifeValue_City) + " elements for his headquarter\n");
						try {
							Util.outputStream.write(sb.toString().getBytes("GBK"));
						} catch (IOException e) {
							e.printStackTrace();
						}
						bluelife += p[i].lifeValue_City;
						p[i].lifeValue_City = 0;
					}
					if(p[i].state == 2 && p[i].flag != 2){
						StringBuilder sb = new StringBuilder();
						sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":40 blue flag raised in city ");
						sb.append(String.valueOf(i) + "\n");
						try {
							Util.outputStream.write(sb.toString().getBytes("GBK"));
						} catch (IOException e) {
							e.printStackTrace();
						}
						p[i].flag = 2;
					}
				}
				else if(p[i].warrior[1].available == 1 && p[i].warrior[0].available == 1){	//both alive, then battle
					if(whoWillAttack(p[i])){	//red warrior attack first
						whowillattack = 1;
						StringBuilder sb = new StringBuilder();
						sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":40 red ");
						sb.append(Util.nameOrder[0][(p[i].warrior[0].number - 1) % 5] + " ");
						sb.append(String.valueOf(p[i].warrior[0].number) + " attacked blue ");
						sb.append(Util.nameOrder[1][(p[i].warrior[1].number - 1) % 5] + " ");
						sb.append(String.valueOf(p[i].warrior[1].number) + " in city ");
						sb.append(String.valueOf(i) + " with " + String.valueOf(p[i].warrior[0].lifeValue));
						sb.append(" elements and force " + String.valueOf(p[i].warrior[0].attackPower) + "\n");
						p[i].warrior[0].Fight(p[i].warrior[1]);
						if(p[i].warrior[1].isDead()){		//blue warrior killed
							sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":40 blue ");
							sb.append(Util.nameOrder[1][(p[i].warrior[1].number - 1) % 5] + " ");
							sb.append(String.valueOf(p[i].warrior[1].number) + " was killed in city ");
							sb.append(String.valueOf(i) + "\n");
							if(p[i].state > 0) p[i].state = -1;
							else p[i].state--;
							p[i].record = 1;
							p[i].warrior[0].moralIncrease();
							p[i].warrior[0].yell(t);
							p[i].warrior[0].getWeapon(p[i].warrior[1]);
							if(p[i].lifeValue_City > 0){
								sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":40 red ");
								sb.append(Util.nameOrder[0][(p[i].warrior[0].number - 1) % 5] + " ");
								sb.append(String.valueOf(p[i].warrior[0].number) + " earned ");
								sb.append(String.valueOf(p[i].lifeValue_City) + " elements for his headquarter\n");
								redlife += p[i].lifeValue_City;
								p[i].lifeValue_City = 0;
							}
							if(p[i].state == -2 && p[i].flag != 1){
								sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":40 red flag raised in city ");
								sb.append(String.valueOf(i) + "\n");
								p[i].flag = 1;
							}
						}
						else if((p[i].warrior[1].number - 1) % 5 != 2){	//blue warrior not killed and is not ninja
							sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":40 blue ");
							sb.append(Util.nameOrder[1][(p[i].warrior[1].number - 1) % 5] + " ");
							sb.append(String.valueOf(p[i].warrior[1].number) + " fought back against red ");
							sb.append(Util.nameOrder[0][(p[i].warrior[0].number - 1) % 5] + " ");
							sb.append(String.valueOf(p[i].warrior[0].number) + " in city ");
							sb.append(String.valueOf(i) + "\n");
							p[i].warrior[1].FightBack(p[i].warrior[0]);
							if(p[i].warrior[0].isDead()){	//red warrior killed
								p[i].warrior[0].available = 0;
								sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":40 red ");
								sb.append(Util.nameOrder[0][(p[i].warrior[0].number - 1) % 5] + " ");
								sb.append(String.valueOf(p[i].warrior[0].number) + " was killed in city ");
								sb.append(String.valueOf(i) + "\n");
								if(p[i].state < 0) p[i].state = 1;
								else p[i].state++;
								p[i].record = 2;
								p[i].warrior[1].moralIncrease();
								p[i].warrior[1].getWeapon(p[i].warrior[0]);
								if(p[i].lifeValue_City > 0){
									sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":40 blue ");
									sb.append(Util.nameOrder[1][(p[i].warrior[1].number - 1) % 5] + " ");
									sb.append(String.valueOf(p[i].warrior[1].number) + " earned ");
									sb.append(String.valueOf(p[i].lifeValue_City) + " elements for his headquarter\n");
									bluelife += p[i].lifeValue_City;
									p[i].lifeValue_City = 0;
								}
								if(p[i].state == 2 && p[i].flag != 2){
									sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":40 blue flag raised in city ");
									sb.append(String.valueOf(i) + "\n");
									p[i].flag = 2;
								}
							}
						}
						try {
							Util.outputStream.write(sb.toString().getBytes("GBK"));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					else{	//blue warrior attack first
						whowillattack = 2;
						StringBuilder sb = new StringBuilder();
						sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":40 blue ");
						sb.append(Util.nameOrder[1][(p[i].warrior[1].number - 1) % 5] + " ");
						sb.append(String.valueOf(p[i].warrior[1].number) + " attacked red ");
						sb.append(Util.nameOrder[0][(p[i].warrior[0].number - 1) % 5] + " ");
						sb.append(String.valueOf(p[i].warrior[0].number) + " in city ");
						sb.append(String.valueOf(i) + " with " + String.valueOf(p[i].warrior[1].lifeValue));
						sb.append(" elements and force " + String.valueOf(p[i].warrior[1].attackPower) + "\n");
						p[i].warrior[1].Fight(p[i].warrior[0]);
						if(p[i].warrior[0].isDead()){		//red warrior killed
							sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":40 red ");
							sb.append(Util.nameOrder[0][(p[i].warrior[0].number - 1) % 5] + " ");
							sb.append(String.valueOf(p[i].warrior[0].number) + " was killed in city ");
							sb.append(String.valueOf(i) + "\n");
							if(p[i].state < 0) p[i].state = 1;
							else p[i].state++;
							p[i].record = 2;
							p[i].warrior[1].moralIncrease();
							p[i].warrior[1].yell(t);
							p[i].warrior[1].getWeapon(p[i].warrior[0]);
							if(p[i].lifeValue_City > 0){
								sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":40 blue ");
								sb.append(Util.nameOrder[1][(p[i].warrior[0].number - 1) % 5] + " ");
								sb.append(String.valueOf(p[i].warrior[1].number) + " earned ");
								sb.append(String.valueOf(p[i].lifeValue_City) + " elements for his headquarter\n");
								bluelife += p[i].lifeValue_City;
								p[i].lifeValue_City = 0;
							}
							if(p[i].state == 2 && p[i].flag != 2){
								sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":40 blue flag raised in city ");
								sb.append(String.valueOf(i) + "\n");
								p[i].flag = 2;
							}
						}
						else if((p[i].warrior[0].number - 1) % 5 != 3){	//red warrior not killed and is not ninja
							sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":40 red ");
							sb.append(Util.nameOrder[0][(p[i].warrior[0].number - 1) % 5] + " ");
							sb.append(String.valueOf(p[i].warrior[0].number) + " fought back against blue ");
							sb.append(Util.nameOrder[1][(p[i].warrior[1].number - 1) % 5] + " ");
							sb.append(String.valueOf(p[i].warrior[1].number) + " in city ");
							sb.append(String.valueOf(i) + "\n");
							p[i].warrior[0].FightBack(p[i].warrior[1]);
							if(p[i].warrior[1].isDead()){	//blue warrior killed
								p[i].warrior[1].available = 0;
								sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":40 blue ");
								sb.append(Util.nameOrder[1][(p[i].warrior[1].number - 1) % 5] + " ");
								sb.append(String.valueOf(p[i].warrior[1].number) + " was killed in city ");
								sb.append(String.valueOf(i) + "\n");
								if(p[i].state > 0) p[i].state = -1;
								else p[i].state--;
								p[i].record = 1;
								p[i].warrior[0].moralIncrease();
								p[i].warrior[0].getWeapon(p[i].warrior[1]);
								if(p[i].lifeValue_City > 0){
									sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":40 red ");
									sb.append(Util.nameOrder[0][(p[i].warrior[0].number - 1) % 5] + " ");
									sb.append(String.valueOf(p[i].warrior[0].number) + " earned ");
									sb.append(String.valueOf(p[i].lifeValue_City) + " elements for his headquarter\n");
									redlife += p[i].lifeValue_City;
									p[i].lifeValue_City = 0;
								}
								if(p[i].state == -2 && p[i].flag != 1){
									sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":40 red flag raised in city ");
									sb.append(String.valueOf(i) + "\n");
									p[i].flag = 1;
								}
							}
						}
						try {
							Util.outputStream.write(sb.toString().getBytes("GBK"));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}	//both are not shot dead by arrow
				if(p[i].record == 0 && (p[i].warrior[0].available == 1 || p[i].warrior[1].available == 1)){	//a war that no one dies
					p[i].state = 0;
					p[i].warrior[0].moralDecrease();
					p[i].warrior[1].moralDecrease();
					if(whowillattack == 1) p[i].warrior[0].yell(t);	//red dragon yells
					else if(whowillattack == 2) p[i].warrior[1].yell(t);	//blue dragon yells
				}
			}	//if num_warrior == 2
		}	//for
		praiseWarrior(p1, p2, p, n);
		p1.lifeValue_Headquarter += redlife;
		p2.lifeValue_Headquarter += bluelife;
	}
	
	public static void reportElements(Headquarter p1, Headquarter p2, int t){
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":50 ");
		sb.append(String.valueOf(p1.lifeValue_Headquarter) + " elements in red headquarter\n");
		sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":50 ");
		sb.append(String.valueOf(p2.lifeValue_Headquarter) + " elements in blue headquarter\n");
		try {
			Util.outputStream.write(sb.toString().getBytes("GBK"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void reportWeapon(Headquarter p1, Headquarter p2, City p[], int n, int t){
		for(int i = 1; i <= n; i++){	//output red warriors' weapon
			if(p[i].num_warrior > 0 && p[i].warrior[0].group == 1 && p[i].warrior[0].available == 1){
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":55 red ");
				sb.append(Util.nameOrder[0][(p[i].warrior[0].number - 1) % 5] + " ");
				sb.append(String.valueOf(p[i].warrior[0].number) + " has ");
				if(p[i].warrior[0].weapon[2] == 1){
					sb.append("arrow(" + String.valueOf(p[i].warrior[0].arrowtime) + ")");
					if(p[i].warrior[0].weapon[1] == 1 || p[i].warrior[0].weapon[0] == 1) sb.append(",");
				}
				if(p[i].warrior[0].weapon[1] == 1){
					sb.append("bomb");
					if(p[i].warrior[0].weapon[0] == 1) sb.append(",");
				}
				if(p[i].warrior[0].weapon[0] == 1) sb.append("sword(" + String.valueOf(p[i].warrior[0].swordPower) + ")");
				if(p[i].warrior[0].weapon[2] == 0 && p[i].warrior[0].weapon[1] == 0 && p[i].warrior[0].weapon[0] == 0){
					sb.append("no weapon");
				}
				sb.append("\n");
				try {
					Util.outputStream.write(sb.toString().getBytes("GBK"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		for(int i = 1; i <= p1.num_warrior; i++){	//red warriors that arrived blue headquarter
			if(p1.warrior[i].arrived == 1){
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":55 red ");
				sb.append(Util.nameOrder[0][(p1.warrior[i].number - 1) % 5] + " ");
				sb.append(String.valueOf(p1.warrior[i].number) + " has ");
				if(p1.warrior[i].weapon[2] == 1){
					sb.append("arrow(" + String.valueOf(p1.warrior[i].arrowtime) + ")");
					if(p1.warrior[i].weapon[1] == 1 || p1.warrior[i].weapon[0] == 1) sb.append(",");
				}
				if(p1.warrior[i].weapon[1] == 1){
					sb.append("bomb");
					if(p1.warrior[i].weapon[0] == 1) sb.append(",");
				}
				if(p1.warrior[i].weapon[0] == 1) sb.append("sword(" + String.valueOf(p1.warrior[i].swordPower) + ")");
				if(p1.warrior[i].weapon[2] == 0 && p1.warrior[i].weapon[1] == 0 && p1.warrior[i].weapon[0] == 0){
					sb.append("no weapon");
				}
				sb.append("\n");
				try {
					Util.outputStream.write(sb.toString().getBytes("GBK"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		for(int i = 1; i <= p2.num_warrior; i++){	//blue warriors that arrived red headquarter
			if(p2.warrior[i].arrived == 1){
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":55 blue ");
				sb.append(Util.nameOrder[1][(p2.warrior[i].number - 1) % 5] + " ");
				sb.append(String.valueOf(p2.warrior[i].number) + " has ");
				if(p2.warrior[i].weapon[2] == 1){
					sb.append("arrow(" + String.valueOf(p2.warrior[i].arrowtime) + ")");
					if(p2.warrior[i].weapon[1] == 1 || p2.warrior[i].weapon[0] == 1) sb.append(",");
				}
				if(p2.warrior[i].weapon[1] == 1){
					sb.append("bomb");
					if(p2.warrior[i].weapon[0] == 1) sb.append(",");
				}
				if(p2.warrior[i].weapon[0] == 1) sb.append("sword(" + String.valueOf(p2.warrior[i].swordPower) + ")");
				if(p2.warrior[i].weapon[2] == 0 && p2.warrior[i].weapon[1] == 0 && p2.warrior[i].weapon[0] == 0){
					sb.append("no weapon");
				}
				sb.append("\n");
				try {
					Util.outputStream.write(sb.toString().getBytes("GBK"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		for(int i = 1; i <= n; i++){	//output blue warriors' weapon
			if((p[i].num_warrior == 1 && p[i].warrior[0].group == 2) || (p[i].num_warrior == 2 && p[i].warrior[1].group == 2)){
				int j = (p[i].num_warrior == 1 && p[i].warrior[0].group == 2) ? 0 : 1;
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("%3s", String.valueOf(t)).replace(' ', '0') + ":55 blue ");
				sb.append(Util.nameOrder[1][(p[i].warrior[j].number - 1) % 5] + " ");
				sb.append(String.valueOf(p[i].warrior[j].number) + " has ");
				if(p[i].warrior[j].weapon[2] == 1){
					sb.append("arrow(" + String.valueOf(p[i].warrior[j].arrowtime) + ")");
					if(p[i].warrior[j].weapon[1] == 1 || p[i].warrior[j].weapon[0] == 1) sb.append(",");
				}
				if(p[i].warrior[j].weapon[1] == 1){
					sb.append("bomb");
					if(p[i].warrior[j].weapon[0] == 1) sb.append(",");
				}
				if(p[i].warrior[j].weapon[0] == 1) sb.append("sword(" + String.valueOf(p[i].warrior[j].swordPower) + ")");
				if(p[i].warrior[j].weapon[2] == 0 && p[i].warrior[j].weapon[1] == 0 && p[i].warrior[j].weapon[0] == 0){
					sb.append("no weapon");
				}
				sb.append("\n");
				try {
					Util.outputStream.write(sb.toString().getBytes("GBK"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
