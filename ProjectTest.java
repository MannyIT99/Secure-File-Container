public class ProjectTest {

	public static void main(String[] args) {

		//Prima implementazione
		Manager1<String> m = new Manager1<String>();
		
		//Seconda implementazione
		//Manager2<String> m = new Manager2<String>();
		
		m.createUser("Emanuele", "Urselli"); //Mario Rossi6
		m.createUser("Francesca", "Levi"); //Luca Verdi
		m.createUser("Giancarlo", "Bigi"); //Sara Pr2
		m.createUser("Giacomo", "Tommei"); //Mary Pizza23
		m.createUser("Maurizio", "Bonuccelli"); //Bobby abcd
		
		m.put("Francesca", "Levi", "Java");
		m.put("Francesca", "Levi", "OCaml");
		m.put("Francesca", "Tommei", "C");
		m.put("Francesca", "Levi", "Interprete");
		m.put("Emanuele", "Urselli", "Cyber");
		m.put("Giancarlo", "Bigi", "Programmazione");
		m.put("Maurizio", "Bonuccelli", "Sistemi operativi");
		m.put("Maurizio", "Bonuccelli", "Assembler");
		m.put("Giancarlo", "Bigi", "BellmanFord");
		m.put("Maurizio", "Bonuccelli", "Firmware");
		m.put("Maurizio", "Bonuccelli", "Cache");
		
		System.out.print("numero di elementi di Francesca: ");
		System.out.println(m.getSize("Francesca", "Levi"));
		System.out.print("numero di elementi di Maurizio: ");
		System.out.println(m.getSize("Maurizio","Bonuccelli"));
		System.out.println();
		
		//stampo gli utenti
		System.out.println("Utenti:");
		for(int i=0;i<m.users.size();i++)
			System.out.println(m.getIdentity(i));
		System.out.println();
		
		System.out.println("elementi di Francesca:");
		m.getIterator("Francesca", "Levi");
		
		//passo un utente con la password sbagliata
		m.getIterator("Giancarlo", "Bonuccelli");
		
		System.out.println();
		//leggo un elemento di Francesca
		System.out.print("Lettura di un elemento: ");
		System.out.println(m.get("Francesca", "Levi", "Java"));
		
		System.out.println();
		
		//provo la rimozione
		m.remove("Francesca", "Tommei", "Interprete");
		m.remove("Francesca", "Bonuccelli", "Java");
		m.copy("Francesca", "Levi", "Java");
		m.remove("Francesca", "Levi", "Java");
		
		System.out.println("numero di elementi di Francesca: " +m.getSize("Francesca","Levi"));
		System.out.println("numero di elementi di Emanuele: " +m.getSize("Emanuele","Urselli"));
		System.out.println();
		
		System.out.println("elementi di Francesca: ");
		m.getIterator("Francesca", "Levi");
		
        System.out.println("elementi di Maurizio: ");
		m.getIterator("Maurizio", "Bonuccelli");
		
		System.out.println();
		
		//testo la condivisione
		
		m.share("Maurizio", "Bonuccelli", "Giancarlo", "Assembler");
		m.share("Maurizio", "Bonuccelli", "Giacomo", "Sistemi operativi");
		m.share("Maurizio", "Bonuccelli", "Emanuele", "Sistemi operativi");
		m.share("Maurizio", "Bonuccelli", "Maruizio", "Sistemi operativi");
		
		System.out.println("Lettura di Giacomo del dato condiviso :");
		System.out.println(m.get("Giacomo", "Tommei", "Sistemi operativi"));
		System.out.println();
		
		// stampo gli utenti che hanno Sistemi operativi condiviso
        System.out.println("Stampo gli utenti che hanno accesso in lettura al dato 'Sistemi operativi'");
		m.shared("Maurizio","Bonuccelli","Sistemi operativi");
		
		//dato non condiviso con nessuno
		m.shared("Maurizio","Bonuccelli","Cache");
		
		//se l'utente ha accesso al dato, prima dell'operazione, non lo //riaggiungo
		m.share("Maurizio", "Bonuccelli", "Giacomo", "Assembler");
		
		//tolgo la mia condivisione al dato
		m.remove("Giacomo", "Tommei", "Sistemi operativi");
        System.out.println("Rimuovo l'accesso al dato a Giacomo Tommei: ");
		m.shared("Maurizio", "Bonuccelli", "Sistemi operativi");
		
		m.share("Maurizio", "Bonuccelli", "Giacomo", "Sistemi operativi");
        System.out.println("Permetto di nuovo l'acccesso al dato a Giacomo Tommei: ");
		m.shared("Maurizio", "Bonuccelli", "Sistemi operativi");
		
		//stampo gli elementi di Giacomo
		//Giacomo non ha elementi: non stampo niente
		m.getIterator("Giacomo", "Tommei");
		
		m.put("Giacomo", "Tommei", "Markov");
		m.remove("Giacomo", "Tommei", "Sistemi operativi");
		
		//stampo gli elementi degli utenti
		
		System.out.print("Elementi di Emanuele: ");
		m.getIterator("Emanuele", "Urselli");
		System.out.print("Elementi di Francesca: ");
		m.getIterator("Francesca", "Levi");
		System.out.print("Elementi di Giancarlo: ");
		m.getIterator("Giancarlo", "Bigi");
		System.out.print("Elementi di Giacomo: ");
		m.getIterator("Giacomo", "Tommei");
		System.out.print("Elementi di Maurizio: ");
		m.getIterator("Maurizio", "Bonuccelli");
		
	}
}
