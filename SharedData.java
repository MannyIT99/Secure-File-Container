import java.util.*;

public class SharedData {

	private int indexData;
	private Vector<Integer> indexUser;
	
	public SharedData(int indexData, int indexUser){
		this.indexData = indexData;
		this.indexUser = new Vector<>();
		this.indexUser.add(indexUser);
	}
	
	public void setUser(int user){
		this.indexUser.add(user);
	}
	
	public int getUser(int user){
		return this.indexUser.get(user);
	}
	
	public int setUserDel(int user){
		return this.indexUser.remove(user);
	}
	
	public int getDim(){
		return this.indexUser.size();
	}
	
	public int getIndexData(){
		return this.indexData;
	}
	
}
