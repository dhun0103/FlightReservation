package flightReservation;

public class Flight {
	
	private String id;
	private String date;
	private String dept;
	private String dest;
	private String comp;
	private String dir;
	private int[] prices;
	
	Flight(){}
	
	Flight(String id, String date, String dept, String dest, String comp, String dir){
		this.id = id;
		this.date = date;
		this.dept = dept;
		this.dest = dest;
		this.comp = comp;
		this.dir = dir;
	}
	
	Flight(String id, String date, String dept, String dest, String comp, String dir, int[] prices){
		this.id = id;
		this.date = date;
		this.dept = dept;
		this.dest = dest;
		this.comp = comp;
		this.dir = dir;
		this.prices = prices;
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getDate() {
		return this.date;
	}
	
	public void setDept(String dept) {
		this.dept = dept;
	}
	
	public String getDept() {
		return this.dept;
	}
	
	public void setDest(String dest) {
		this.dest = dest;
	}
	
	public String getDest() {
		return this.dest;
	}
	
	public String getComp() {
		return this.comp;
	}
	
	public String getDir() {
		return this.dir;
	}
	
	public int getPriceByClass(String clas) {
		if (clas.equals("이코노미")) return this.prices[0];
		else if (clas.equals("비즈니스")) return this.prices[1];
		else return this.prices[2];//퍼스트
	}

}
