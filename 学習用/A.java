package jp.co.internous.action;


public class A {

		public static void main(String[] args){	//通常のJavaはスタート地点、strutsはexcuteからスタート
			
			String item = "回復アイテム";
			String ken = "エクスカリバー";
			
			//new BでBという設計図から実体を生成して変数名をobjectBとする。
			B objectB = new B();

			//new BでBという設計図から実体を生成して変数名をobjectCとする。
			//さらに実体を生成する際にitemとkenを持たせた状態で生成している
			B objectC = new B(item,ken);
			
			objectB.print();
			objectC.print();
			
			int count = 5;			
			System.out.println("Aクラスのcount:"+count);
			objectB.count = 100;
			count = objectB.count(count);
			System.out.println("Aクラスのcount:"+count);
			objectB.print2();
			
			hyakubai(count);//countはこのとき150
			System.out.println("100倍後 : "+count);
			
		}
		public static void hyakubai(int count) {
			count = count * 100;
			System.out.println("現在のcount : "+count);
		}
	}
