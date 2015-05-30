/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hispatwitter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author user
 */
public class Twitts {

    private Random randomGenerator;
    private ArrayList<String> catalogoCancer = new ArrayList();
    private String Hashtag;
    private boolean RespondeTwitts;

    public void cargaTwitts() {

        try {
            File archivo = null;
            FileReader fr = null;
            BufferedReader br = null;
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            archivo = new File("configuracion/listatwitts.txt");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.isEmpty()) {
                    this.catalogoCancer.add(linea);
                }
            }
            System.out.println(linea);
        } catch (Exception e) {
        }




    }

    public Twitts(String Hashtag, String RespondeTwitts ) {
        this.catalogoCancer = new ArrayList();
        this.randomGenerator = new Random();
        this.RespondeTwitts =RespondeTwitts.equals("true");
        String temporal = "";
        for (String cadena : Hashtag.split(",")) {
            temporal = temporal + "#" + cadena + " ";
        }

        this.Hashtag = temporal;
        cargaTwitts();
        

    }

    public String getTwitt(String usuario) {
        String cancer;
        do {
            int index = this.randomGenerator.nextInt(this.catalogoCancer.size());
            if (RespondeTwitts){
                cancer = "@" + usuario + " " + this.catalogoCancer.get(index) + "\n" + this.Hashtag;
            }
            else{
                cancer = this.catalogoCancer.get(index) + "\n" + this.Hashtag;
            }
            
            this.catalogoCancer.remove(index);
            if (this.catalogoCancer.isEmpty()) {
                System.out.println("Se termino la lista de cancer");

                cargaTwitts();
            }
        } while (cancer.length() > 140);

        return cancer;
    }
}
