public class User {

	private String id, passw;
	private int start, end;
	
	public User(String id, String passw){
		this.id = id;
		this.passw = passw;
	}
	
	public void index(int dim){
		this.start = dim;
		this.end = dim-1;
	}
	
	public int getStart(){
		return this.start;
	}
	
	public int getEnd(){
		return this.end;
	}
	
	public void setEnd(int one){
		this.end = this.end + one;
	}
	
	public void setStart(int one){
		this.start = this.start + one;
	}
	
	public String getId(){
		return this.id;
	}
	
	public String getPassw(){
		return this.passw;
	}
}
