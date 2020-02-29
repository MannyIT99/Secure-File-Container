import java.util.*;

public class User1<E> {
	
	private String username, password;
	private Hashtable<E, Vector<Integer>> ht;
	   
	public User1(String username, String password) {
	    this.username = username;
	    this.password = password;
	    ht = new Hashtable<>();
	}
	    
	public String getId() {
	    return this.username;
	}
	    
	public String getPassw() {
	    return this.password;
	}
	    
	public int getSizeHash() {
	    return this.ht.size();
	}
	
	public void addElHash(E data){
		Vector<Integer> v= new Vector<>();
		//il primo elemento mi indica il numero di copie
		v.add(0);
		this.ht.put(data, v);
		
	}
	
	public boolean KeyInHash(E data){
		return this.ht.containsKey(data);
	}
	
	public void DelInHash(E data){
		this.ht.remove(data);
	}
	
	public int getSizeV(E data){
		return this.ht.get(data).size();
	}
	
	public int getElem(E data, int ind){
		return this.ht.get(data).get(ind);
	}
	
	public void setFirst(E data,int op){
		int r = this.ht.get(data).get(0);
		this.ht.get(data).setElementAt(r + op,0);
		
	}
	
	public void setVRemove(E data, int ind){
		this.ht.get(data).remove(ind);
	}
	
	public void setAddUtente(E data,int ind){
		this.ht.get(data).add(ind);
	}
	
	
	public Set<E> getKeys(){
		return this.ht.keySet();
	}
}
