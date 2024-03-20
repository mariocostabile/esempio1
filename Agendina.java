package poo.agendina;
import java.io.*;
import java.util.Iterator;
import java.util.StringTokenizer;

import poo.util.ArrayVector;
import poo.util.Vector;


public interface Agendina extends Iterable<Nominativo>{
	
	default int size() {
		Iterator<Nominativo> it = iterator();
		int c=0;
		while(it.hasNext()) {
			it.next();
			c++;
		}
		return c;
	}//size
	
	default void svuota() {
		Iterator<Nominativo> it = iterator();
		while(it.hasNext()) {
			it.next();
			it.remove();
		}
	}//svuota
	
	void aggiungi( Nominativo n );
	
	default void rimuovi( Nominativo n ) {
		Iterator<Nominativo> it = iterator();
		while(it.hasNext()) {
			Nominativo x=it.next();
			if( x.equals(n)) { it.remove(); return; }  //return equivale a break 
			if( x.compareTo(n)>0 ) return;  //è ordinato quindi ottimizzo così
		}
	}//rimuovi
	
	default Nominativo cerca( Nominativo n ){
		for(Nominativo x: this) {
			if(x.equals(n)) 
				return x;
			if( x.compareTo(n)>0 ) return null;
		}
		return null;
	}//per cognome-nome

	default Nominativo cerca( String prefisso, String telefono ) {
		for( Nominativo x: this ) {
			if( x.getPrefisso().equals(prefisso) && x.getTelefono().equals(telefono) )
				return x;
		}
		return null;
	}//cerca
	
	default void salva(String nomeFile) throws IOException{
		PrintWriter pw=new PrintWriter( new FileWriter(nomeFile) );
		for( Nominativo x: this ) {
			pw.println(x);
		}
		pw.close();
	}//salva
	
	default void ripristina(String nomeFile) throws IOException{
		BufferedReader br=new BufferedReader( new FileReader(nomeFile) );
		Vector<Nominativo> tmp=new ArrayVector<>();
		boolean okLettura=true; //ottimismo
		try {
			for(;;) {
				String linea=br.readLine();
				if( linea==null ) break;
				StringTokenizer st=new StringTokenizer(linea," -");
				String cog=st.nextToken();
				String nom=st.nextToken();
				String pre=st.nextToken();
				String tel=st.nextToken();
				Nominativo n=new Nominativo(cog,nom,pre,tel);
				tmp.add(n);
			}
		}catch( Exception e ) {
			e.printStackTrace();
			okLettura=false;
			throw e; //propagazione di e al caller
		}finally {
			br.close();
		}
		if( okLettura ) { //se lo legge correttamente lo carica 
			svuota();
			for( Nominativo y: tmp ) {
				aggiungi(y);
			}
		}
	}//ripristina 
	
}//Agendina