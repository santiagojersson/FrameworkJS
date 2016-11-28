package co.usa.edu.adf.FrameworkJS;

import java.util.ArrayList;

public class asdasd {

	public static void main(String[] args) {
		DatabaseJS db= new DatabaseJS();
                String ruta="localhost/sonora";
                db.createConnection(ruta, "root", "root");
                ArrayList<Object> arre= db.CreateSelectAll(canciones.class);
                /*System.out.println("size "+arre.size());
                for (Object object : arre) {
                    canciones c= (canciones) object;
                    System.out.println("Cancion "+c.getCNOMBRE());
                }
                System.out.println("------");*/
                canciones c= new canciones();
                c.setCNOMBRE("24 k Magic");
                c.setCREPRODUCCIONES(200);
                c.setCDESCARGAS(300);
                db.insert(c);
                
	}
	
}
