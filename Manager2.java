import java.util.*;

public class Manager2<E> implements SecureDataContainer<E> {
	
	//OVERVIEW: collezione di dati di dimensione variabile che descrive
	//			un insieme di dati di tipo E non modificabili 
	//Typical Element: <{E1,E2,...,En}, 
	//					{<U1,<Ex,{y1,...,yj}>>, ..., <Um,<Ey,{z1,..., zi}>>}>
	//FA(c)={c.info.get(i), c.users.get(j)| 0<=i<c.info.size() && 0<=j<c.users.size()
	//		&& c.users.get(j).getSizeHash()<c.info.size()}
	//IR(c)= c.users!=null && c.info!=null && 
	//		forall i. 0<=i<c.users.size() c.users.get(i) di tipo User1 e per tutti
	//		gli i, j tali che 0<=i<j<c.users.size() 
	//		c.users.get(i).getId()!=c.users.get(j).getId() &&
	//		forall k. 0<=k<c.info.size() c.info.get(k) di tipo E &&
	//		c.users.get(i).getSizeHash()<c.info.size()
	
	
	public Vector<E> info;
	@SuppressWarnings("rawtypes")
	public Vector<User1> users;
	    
	
	//EFFECTS: inizializzo a null i vettori info e users
	//costruttore
	public Manager2() {
	    this.info = new Vector<>();
	    this.users = new Vector<>();
	}
	
	//REQUIRES: id!=null && passw!=null
	//EFFECTS: aggiunge un utente al vettore users
	//THROWS: se id == null || passw == null => NullPointerException e
	// 			se l'id esiste solleva IllegalArgumentException
	//MODIFIES: users
	@SuppressWarnings("rawtypes")
	public void createUser(String Id, String passw) {
	    if(Id == null || passw == null) 
	        throw new NullPointerException();
	    for(int i=0; i<users.size(); i++) 
	        if(Id.equals(users.get(i).getId()))
	            throw new IllegalArgumentException();
	    User1 u = new User1(Id, passw);
	    users.add(u);
	}

	//REQUIRES: username!=null && password!=null
	//EFFECTS: restituisce l'indice dell'utente che fa l'accesso nel caso esista.
	//			Altrimenti restituisce -1
	//THROWS: solleva NullPointerException() se id e/o passw sono nulli
	//			faccio il login
	public int login(String username, String password) {
	    if(username == null || password == null)
	        throw new NullPointerException();
	    for(int i=0; i<users.size(); i++)
	        if(username.equals(users.get(i).getId()))
	        	if(password.equals(users.get(i).getPassw()))
	                return i;
	        	else
					return -1;
	    return -1;
	}

	//EFFECTS: restituisco il numero di elementi posseduti dall'utente
	public int getSize(String Owner, String passw) {
	    int address = login(Owner, passw);
	    if(address != -1) {
	    	return users.get(address).getSizeHash();
	    }
	    return -1;
	}
	
	
	//REQUIRES: data!=null 
	//EFFECTS: aggiunge un elemento al vettore info e alla tabella Hash dell'utente
	//THROWS: se data==null solleva NullPointerException
	//MODIFIES: info, users
	@SuppressWarnings("unchecked")
	public boolean put (String Owner, String passw, E data){
		if(data == null)
			throw new NullPointerException();
		int address=login(Owner,passw);
		if(address!=-1){
			info.add(data);
			users.get(address).addElHash(data);
			return true;
		}
		return false;
	}

	//REQUIRES: data!=null
	//EFFECTS: restituisce una copia del dato
	//THROWS: se l'utente non possiede il dato richiesto solleva l'eccezione
	//			IllegalArgumentException && se il data==null solleva NullPointerException
	@SuppressWarnings("unchecked")
	public E get(String Owner,String passw, E data){
		if(data==null){
			throw new NullPointerException();
		}
		int address=login(Owner,passw);
		if(address!=-1){
			//se sono il proprietario
			if(users.get(address).KeyInHash(data)){
				return data;
			}
			//se non sono il proprietario
			for(int i=0;i<users.size();i++){
				if(users.get(i).KeyInHash(data)){
					for(int j=1;j<users.get(i).getSizeV(data);j++){
						if(address==users.get(i).getElem(data,j)){
							return data;
						}		
					}
				}
			}
			throw new IllegalArgumentException();
		}
		return null;
	}
		
	//REQUIRES: data!=null
	//EFFECTS: rimuove il dato solo il proprietario. Se un utente 
	//			ha solo accesso in lettura al dato, cancella il permesso
	//THROWS: se data==null solleva NullPointerException
	//MODIFIES: info, users
	@SuppressWarnings("unchecked")
	public E remove (String Owner, String passw, E data){
		if(data == null)
			throw new NullPointerException();
		int address=login(Owner,passw);
		if(address!=-1){
			//se sono il proprietario
			if(users.get(address).KeyInHash(data)){
				for(int i=0;i<info.size();i++)
					if(data.equals(info.get(i)))
						info.remove(i);
				if(users.get(address).getElem(data, 0)==0){
					users.get(address).DelInHash(data);
					return data;
				}
				else{
					users.get(address).setFirst(data, -1);
					return data;
				}
			}
			//se non sono il proprietario
			for(int i=0; i<users.size(); i++)
				if(users.get(i).KeyInHash(data))
					for(int j=1; j<users.get(i).getSizeV(data); j++)
						if(address == users.get(i).getElem(data, j)){
							users.get(i).setVRemove(data, j);
							return data;
						}
			return null;
		}
		return null;
	}
		
	
	
	//REQUIRES: data!=null
	//EFFECTS: fa una copia del dato posseduto dall'utente 
	//THROWS: se data==null solleva NullPointerException
	//MODIFIES: info, users
	@SuppressWarnings("unchecked")
	public void copy(String Owner, String passw, E data){
		if(data == null)
			throw new NullPointerException();
		int address = login(Owner,passw);
		if(address != -1){
			if(users.get(address).KeyInHash(data)){
				users.get(address).setFirst(data, 1);
				info.add(data);
			}
		}
	}
		
	//REQUIRES: data!=null 
	//EFFECTS: l'utente condivide il proprio dato con altri utenti
	//THROWS: se data==null solleva NullPointerException
	//MODIFIES: users
	@SuppressWarnings("unchecked")
	public void share(String Owner, String passw, String Other, E  data){
		if(data == null)
			throw new NullPointerException();
		int address=login(Owner,passw);
		if(address!=-1){
			int i;
			if(Owner.equals(Other))
				return;
			for(i=0; i<users.size(); i++){
				if(Other.equals(users.get(i).getId())){
					if(users.get(address).KeyInHash(data)){
						//aggiungo solo se l'utente non esiste
						for(int j=1; j<users.get(address).getSizeV(data); j++)
							if(i == users.get(address).getElem(data, j))
								return;
						users.get(address).setAddUtente(data, i);
						return;
					}
				}
			}
		}
	}
		
	//EFFECTS: restitituisce un iteratore di elementi di tipo E
	//			che genera tutti i dati dell'utente e li stampa
	public Iterator<E> getIterator(String Owner, String passw){
		int address=login(Owner,passw);
		if(address != -1){
			E element;
			@SuppressWarnings("unchecked")
			Iterator<E> iterator = users.get(address).getKeys().iterator();
			while(iterator.hasNext()){
				element = iterator.next();
				System.out.print(element + " ");
			}
			System.out.println();
		}
		return null;
	}
	
	//REQUIRES: 0<=pos<c.users.size()
	//EFFECTS: restituisce l'Id dell'utente in posizione pos nel vettore users
	//THROWS: se pos<0 || pos>=c.users.size() solleva IllegalArgumentException
	public String getIdentity(int pos){
		if(pos>=0 && pos<users.size())
			return users.get(pos).getId();
		throw new IllegalArgumentException();
	}
	
	
	//REQUIRES: dato!=null
	//EFFECTS: l'utente proprietario stampa gli utenti che hanno 
	//			accesso in lettura al dato 
	//THROWS: se dato==null solleva NullPointerException && 
	//			se non posseggo il dato da condividere solleva 
	//			IllegalArgumentException
	//MODIFIES: users
	@SuppressWarnings("unchecked")
	public int shared(String Owner, String password, E data){
		if(data == null)
			throw new NullPointerException();
		int address = login(Owner,password);
		if(address != -1){
			//se posseggo il dato in pos i
			if(users.get(address).KeyInHash(data)){
				if(users.get(address).getSizeV(data)>1){
					System.out.print(data + " condiviso con : ");
					for(int i=1; i<users.get(address).getSizeV(data); i++){
						System.out.print(
								users.get(users.get(address).getElem(data, i)).getId()+"  ");
					}
					System.out.println();
					return -1;
				}
				if(users.get(address).getSizeV(data)==1){
					return -1;
				}
			}
			throw new IllegalArgumentException();
		}
		return -1;
	}
	
}
