package jp.co.internous.action;

public class player {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		Monster[] monsters = new Monster[3];
		//Slime, Goblin, DeathBatはMonsterクラスを継承している。
		//Slime, Goblin, DeathBatはrun()メソッドを上書きして、独自の逃げ方をする
		monsters[0] = new Slime();
		monsters[1] = new Goblin();
		monsters[2] = new DeathBat();
		monsters[0].run();
		monsters[1].run();
		monsters[2].run();
		for(int i=0;i<monsters.length;i++) {
			monster[i].run();
		}
		
		Slime slime = new Slime();
		Golblin goblin = new Goblin();
		DeathBat deathbat = new DeathBat();
		slime.run();
		goblin.run();
		deathbat.run(;)
		for(Monster m : monsters) {
			m.run();
		}
	}

}
