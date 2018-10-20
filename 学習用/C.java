package jp.co.internous.action;


public class C {

		public static void main(String[] args){
			
			String item = "回復アイテム";
			String ken = "エクスカリバー";
			int count;
			
			D objectB = new D();
			D objectC = new D(item,ken);
			
			objectB.print();
			objectC.print();
			System.out.println("初期値："+objectB.getCount());
			objectB.setCount(100); 			//セッターで値を渡す。直接操作はしない。
			count = objectB.getCount();		//処理結果を受け取る
			objectC.setItem("お茶");
			item = objectC.getItem();
			System.out.println(item);
			
			System.out.println(objectB.getItem());
			
			System.out.println("更新値："+count);
			
		}
		
	}
