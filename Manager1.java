import java.util.*;

public class Manager1<E> implements SecureDataContainer<E>{
	
	/*
        OVERVIEW: collezione di dati di dimensione variabile che descrive un insieme di dati di tipo E non modificabili 
        
	    Typical Element: <{E1, E2, ..., En}, 
                          {U1, ..., Um}, 
                          {<x1, {y1,...,yj}>, ..., <xt, {z1, ..., zh}>>
                          
	    FA(c) = {c.info.get(i), c.users.get(j), c.sd.get(k) | 0<=i<c.info.size() && 0<=j<c.users.size() && 0<=k<c.sd.size()}
        
	    IR(c)= c.users!=null && c.info!=null && c.sd!=null && 
		forall i. 0<=i<c.users.size() => c.users.get(i) di tipo User forall i, j. 0<=i<j<c.users.size() => c.users.get(i).getId()!=c.users.get(j).getId() && 
        forall k. 0<=k<c.info.size() => c.info.get(k) di tipo E && forall h. 0<=h<c.sd.size() => c.sd.get(h) di tipo SharedData &&
        forall h, t. (0<=h && t<c.sd.size()) => c.sd.get(h).getIndexData()!=c.sd.get(t).getIndexData()
    */
	
	
		public Vector<User> users;
		public Vector<E> info;
		public Vector<SharedData> sd;
	
		
		//EFFECTS: inizializza i vettori utenti, elem, sd a null
		//costruttore
		public Manager1(){
			this.info = new Vector<>();
			this.users = new Vector<>();
			this.sd = new Vector<>();
		}
		
		
		//REQUIRES: id!=null && passw!=null
		//EFFECTS: crea un oggetto utente con id e password e inizializza //gli indici che danno il range degli oggetti della collezione //posseduti da questo utente 
		//THROWS: solleva NullPointerException() se id e/o passw sono //nulli e solleva IllegalArgumentException se si inserisce un id //presente
		//MODIFIES: users
		public void createUser(String id,String passw){
			if(id==null || passw==null)
				throw new NullPointerException();
			for(int i=0;i<users.size();i++)
				if(id.equals(users.get(i).getId()))
					throw new IllegalArgumentException();
			User u = new User(id, passw);
			u.index(info.size());
			users.add(u);
		}
		
		//REQUIRES: identity!=null && password!=null
		//EFFECTS: restituisce l'indice dell'utente che fa l'accesso nel //caso esista. Altrimenti restituisce -1
		//THROWS: solleva NullPointerException() se id e/o passw sono //nulli
		public int login(String identity, String password){
			if(identity==null || password==null)
				throw new NullPointerException();
			for(int i=0; i<users.size(); i++)
				if(identity.equals(users.get(i).getId()))
					if(password.equals(users.get(i).getPassw()))
							return i;
					else
						return -1;
			return -1;
		}
		
		//EFFECTS: restituisco il numero di elementi posseduti //dall'utente
		public int getSize(String Owner, String passw){
			int address = login(Owner, passw);
			if(address != -1)
				return (users.get(address).getEnd() - users.get(address).getStart() + 1);
			return -1;
		}
		
		
		//REQUIRES: data!=null 
		//EFFECTS: aggiunge un elemento al vettore info e aggiorna gli //indici degli utenti
		//THROWS: se data==null solleva NullPointerException
		//MODIFIES: info, users
		public boolean put (String Owner, String passw, E data){
			if(data==null)
				throw new NullPointerException();
			int address = login(Owner, passw);
			if(address != -1){
				int posE = users.get(address).getEnd() + 1;
				info.add(posE, data);
				users.get(address).setEnd(1);
				for(int i=address+1; i<users.size(); i++){
					users.get(i).setStart(1);
					users.get(i).setEnd(1);
				}
				return true;
			}
			return false;
		}
		
		
		//REQUIRES: data!=null
		//EFFECTS: restituisce una copia del dato
		//THROWS: se l'utente non possiede il dato richiesto solleva //l'eccezione IllegalArgumentException && se il data==null //solleva NullPointerException
		public E get(String Owner,String passw, E data){
			if(data == null)
				throw new NullPointerException();
			int address = login(Owner,passw);
			if(address != -1){
				//se sono il proprietario
				int s = users.get(address).getStart();
				int e = users.get(address).getEnd();
				for(int i=s; i<e+1; i++)
					if(data.equals(info.get(i)))
						return data;
				//se non sono il proprietario
				for(int j=0; j<sd.size(); j++)
                    if(data.equals(info.get(sd.get(j).getIndexData())))
						for(int k=0; k<sd.get(j).getDim(); k++)
							if(address == sd.get(j).getUser(k))
								return data;
				
				throw new IllegalArgumentException();
			}
			return null;
		}
		
		
		//REQUIRES: data!=null
		//EFFECTS: rimuove il dato solo il proprietario. Se un 
        //utente ha solo accesso in lettura al dato, cancella il permesso
		//THROWS: se data==null solleva NullPointerException
		//MODIFIES: info, users, sd
		public E remove (String Owner, String passw, E data){
			if(data == null)
				throw new NullPointerException();
			int address = login(Owner, passw);
			if(address != -1){
				//se sono il proprietario, allora rimuovilo
				int s = users.get(address).getStart();
				int e = users.get(address).getEnd();
				for(int i=s; i<e+1; i++){
					if(data.equals(info.get(i))){
						info.remove(i);
						users.get(address).setEnd(-1);
						for(int k=address+1; k<users.size(); k++){
							users.get(k).setStart(-1);
							users.get(k).setEnd(-1);
						}
						for(int j=0; j<sd.size(); j++)
							if(i == sd.get(j).getIndexData())
								sd.remove(j);
						return data;
					}
				}
				//se non sono il proprietario del dato, togli il mio //indice
				for(int i=0; i<sd.size(); i++){
					if(data.equals(info.get(sd.get(i).getIndexData()))){
						for(int j=0; j<sd.get(i).getDim(); j++){
							if(address == sd.get(i).getUser(j)){
								sd.get(i).setUserDel(j);
								return data;
							}
						}
					}
				}
				return null;
			}
			return null;
		}
		
		
		//REQUIRES: data!=null
		//EFFECTS: fa una copia del dato posseduto dall'utente 
		//THROWS: se data==null solleva NullPointerException
		//MODIFIES: info, users
		public void copy(String Owner, String passw, E data){
			if(data==null)
				throw new NullPointerException();
			int address = login(Owner, passw);
			if(address != -1)
				for(int i=users.get(address).getStart(); i<users.get(address).getEnd()+1; i++)
					if(data.equals(info.get(i))){
						put(Owner, passw, data);
						return;
					}
		}
		
		//REQUIRES: data!=null 
		//EFFECTS: l'utente condivide il proprio dato con altri utenti
		//THROWS: se data==null solleva NullPointerException, se condivide un dato
		//			che non possiede solleva IllegalArgumentException
		//MODIFIES: sd
		public void share(String Owner, String passw, String Other, E  data){
			if(data == null)
				throw new NullPointerException();
			int i, j, k;
			int address = login(Owner, passw);
			if(address != -1){
				if(Owner.equals(Other))
					return;
				int s = users.get(address).getStart();
				int e = users.get(address).getEnd();
				//se posseggo il dato in pos i
				for(i=s; i<e+1; i++)
					if(data.equals(info.get(i)))
						break;
				//non posseggo il dato
				if(i == (e+1))
					throw new IllegalArgumentException();
				//se esiste l'utente con cui lo voglio condividere in pos j
				for(j=0; j<users.size(); j++)
					if(Other.equals(users.get(j).getId()))
						break;
				//non esiste l'utente Other
				if(j == users.size())
					return;
				//se non l'ho ancora condiviso con nessuno aggiungo al vettore 
				//un nuovo oggetto per il dato
				for(k=0; k<sd.size(); k++)
					if(i == sd.get(k).getIndexData()){
						//aggiungo solo l'utente
						//se non ancora presente
						for(int h=0; h<sd.get(k).getDim(); h++)
							if(j == sd.get(k).getUser(h))
								return;
						sd.get(k).setUser(j);
						return;
					}
				SharedData dati = new SharedData(i,j);
				sd.add(dati);
				return;
			}
			
		}
		
		//EFFECTS: restitituisce un iteratore di elementi di tipo E
		//			che genera tutti i dati dell'utente e li stampa
		public Iterator<E> getIterator(String Owner, String passw){
			int address = login(Owner, passw);
			if(address != -1){
				int i = 0;
				E element;
				Iterator<E> iterator = info.iterator();
				while(iterator.hasNext()){
					element = iterator.next();
					if(i>=users.get(address).getStart() && i<=users.get(address).getEnd())
						System.out.print(element + " ");
					i++;
				}
				System.out.println();
			}
			return null;
			
		}
	
		//REQUIRES: 0<=pos<c.users.size()
		//EFFECTS: restituisce l'Id dell'utente in posizione pos nel //vettore users
		//THROWS: se pos<0 || pos>=c.users.size() solleva IllegalArgumentException
		public String getIdentity(int pos){
			if(pos>=0 && pos<users.size())
				return users.get(pos).getId();
			throw new IllegalArgumentException();
		}
		
		
		//REQUIRES: dato != null
		//EFFECTS: l'utente proprietario stampa gli utenti che hanno 
		//			accesso in lettura al dato 
		//THROWS: se dato == null solleva NullPointerException && 
		//			se non posseggo il dato da condividere solleva 
		//			IllegalArgumentException
		//MODIFIES: sd
		public int shared(String Owner, String password, E data){
			if(data == null)
				throw new NullPointerException();
			int address = login(Owner, password);
			if(address != -1){
				//se posseggo il dato in pos i
				for(int i=users.get(address).getStart(); i<users.get(address).getEnd()+1; i++)
					if(data.equals(info.get(i))){
						for(int j=0;j<sd.size();j++)
							if(i==sd.get(j).getIndexData()){
								System.out.print(data + " condiviso con : ");
								for(int k=0; k<sd.get(j).getDim(); k++)
									System.out.print( users.get(sd.get(j).getUser(k)).getId() + "  ");
								System.out.println();
								break;
							}					
						return -1;
					}	
				throw new IllegalArgumentException();
			}
			return -1;
		}
		
}
