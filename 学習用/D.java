package jp.co.internous.action;

public class D {
	private String item;	//値を与えなければ勝手にnullが与えられる
	private String ken;
	private int count;			//値を与えなければ勝手に0が与えられる
	
	//情報をもたない状態で生成する。
	//デフォルトコンストラクタは省略可能だが、他にコンストラクタが定義されていないときに限る。
	public D() {
		
	}
	//itemとkenを持った状態で生成する
	public D(String item, String ken) {
		this.item = item;
		this.ken = ken;
	}

	
	public void print() {
		System.out.println("現在の所持品："+item);
		System.out.println("現在の所持品："+ken);

	}
	public void print2() {
		System.out.println("現在の個数:"+count);
	}
	public int count(int countFromA) {
		count = count + countFromA * 10;
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getCount() {
		return count;
	}
	public void setItem(String item) {
		this.item = item;
	}public String getItem() {
		return item;
	}
	public void setKen(String ken) {
		this.ken = ken;
	}
	public String getKen() {
		return ken;
	}
}