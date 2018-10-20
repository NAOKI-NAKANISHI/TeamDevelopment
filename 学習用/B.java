package jp.co.internous.action;

public class B {
	String item;	//値を与えなければ勝手にnullが与えられる
	String ken;
	int count;			//値を与えなければ勝手に0が与えられる
	
	//情報をもたない状態で生成する。
	//デフォルトコンストラクタは省略可能だが、他にコンストラクタが定義されていないときに限る。
	public B() {
		
	}
	//itemとkenを持った状態で生成する
	public B(String item, String ken) {
		this.item = item;
		this.ken = ken;
	}

	
	public void print() {
		System.out.println("現在の所持品："+item);
		System.out.println("現在の所持品："+ken);

	}
	public void print2() {
		System.out.println("Bクラスのcount:"+count);
	}
	public int count(int count) {
		this.count = this.count + count * 10;
		return this.count;
	}
}