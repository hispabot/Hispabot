/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hispatwitter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author user
 */
public class BanHammer {

    private HashMap<String, NegrosTwitteros> controlNegros = new HashMap();
    static final long ONE_MINUTE_IN_MILLIS = 60000;
    static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public String getBanHammer(String user, int Minutos, int NumeroTwitts) {

        String ban = "OK";
        if (controlNegros.containsKey(user)) {
            //control de tiempo

            if (controlNegros.get(user).getNumeroTtitt() < NumeroTwitts) {
                NegrosTwitteros negrosTwitteros = controlNegros.get(user);
                negrosTwitteros.setUltimoTwitt(new Date());
                negrosTwitteros.setNumeroTtitt(controlNegros.get(user).getNumeroTtitt() + 1);
                controlNegros.put(user, negrosTwitteros);
            } else if (controlNegros.get(user).getNumeroTtitt() == NumeroTwitts) {
                NegrosTwitteros negrosTwitteros = controlNegros.get(user);
                negrosTwitteros.setUltimoTwitt(new Date());
                negrosTwitteros.setNumeroTtitt(controlNegros.get(user).getNumeroTtitt() + 1);
                controlNegros.put(user, negrosTwitteros);
                ban = "BANHAMMER";
                System.out.println(simpleDateFormat.format(new Date()) + "\t - Usuario Banneado[" + user+"]");
            } else if (controlNegros.get(user).getNumeroTtitt() > NumeroTwitts) {
                Date ultimo = new Date(controlNegros.get(user).getUltimoTwitt().getTime() + (Minutos * ONE_MINUTE_IN_MILLIS));
                if (new Date().before(ultimo)) {
                    ban = "BANHAMMER";
                    
                } else {
                    NegrosTwitteros negrosTwitteros = controlNegros.get(user);
                    negrosTwitteros.setUltimoTwitt(new Date());
                    negrosTwitteros.setNumeroTtitt(1);
                    controlNegros.put(user, negrosTwitteros);

                }

            }

        } else {
            NegrosTwitteros negrosTwitteros = new NegrosTwitteros();
            negrosTwitteros.setUsername(user);
            negrosTwitteros.setUltimoTwitt(new Date());
            negrosTwitteros.setNumeroTtitt(1);
            controlNegros.put(user, negrosTwitteros);

        }
        return ban;


    }
}
