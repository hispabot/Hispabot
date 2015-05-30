/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hispatwitter;

/**
 *
 * @author user
 */
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import twitter4j.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class Hispatwitter {

    public static String Version = "3.0.0.1";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws TwitterException, URISyntaxException, FileNotFoundException, IOException, InterruptedException {
        // TODO code application logic here
        //iniciando
        Configuracion("pPTSfNAps9L3vcaN0rr5AH3Tp", "cyEYcte4x67UMZkxCx9XTupyWyEDp1COmWSiRrH9aGmQP189mT");

        File file = new File("configuracion/configuracion.properties");
        Properties prop1 = new Properties();
        InputStream is = null;
        is = new FileInputStream(file);
        prop1.load(is);


        final String Filtro = prop1.getProperty("Filtro", "Hispachan");
        final String Hashtag = prop1.getProperty("Hashtag", "Hispachan");
        final Integer MinutosTolerancia = Integer.parseInt(prop1.getProperty("MinutosTolerancia", "10"));
        final Integer NumeroTwitts = Integer.parseInt(prop1.getProperty("NumeroTwitts", "10"));
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        final String RespondeTwitts = prop1.getProperty("RespondeTwitts", "true");
        



        Twitter twitter = new TwitterFactory().getInstance();


        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        //Twitter twitter = new TwitterFactory().getInstance();
        System.out.println("*********************************");
        System.out.println("*        H I S P A B O T        *");
        System.out.println("*           " + Version + "             *");
        System.out.println("*                               *");
        System.out.println("*   https://www.hispachan.org   *");
        System.out.println("*                               *");
        System.out.println("*********************************\n\n");
        
        
        System.out.println("     ---- Parametros ----      ");
        System.out.println("Filtro=["+Filtro+"]");
        System.out.println("Hashtag=["+Hashtag+"]");
        System.out.println("MinutosTolerancia=["+MinutosTolerancia+"]");
        System.out.println("NumeroTwitts=["+NumeroTwitts+"]");
        System.out.println("RespondeTwitts=["+RespondeTwitts+"]");
        
        boolean conexion= false;
        do{
            System.out.println("\nVerificando el Internet...");
            try { 

            URL ruta=new URL("http://www.google.es"); 
            URLConnection rutaC=ruta.openConnection(); 
            rutaC.connect(); 
            conexion=true; 
            System.out.println("Internet OK");
           }catch(Exception e){ 
            conexion=false; 
            System.out.println("No hay internet... ");
            Thread.sleep(10000);
        } 
            
        }while(!conexion);
        
        


        final String UserName = twitter.getScreenName();

        Status status = twitter.updateStatus(UserName + "\nTraduciendo memes...\nCargando la coca...\nHispabot esta Activo!! \n" + simpleDateFormat.format(new Date()));
        System.out.println("Estado del Twitt [" + status.getText() + "]");


        StatusListener listener = new StatusListener() {
            Twitter twitter = new TwitterFactory().getInstance();
            Twitts twitts = new Twitts(Hashtag,RespondeTwitts);
            BanHammer banHammer = new BanHammer();

            public void publica(String Mensaje) throws TwitterException {
                Status status = twitter.updateStatus(Mensaje);

                System.out.println(simpleDateFormat.format(new Date()) + "\t - Publicado:" + status.getText());


            }

            @Override
            public void onStatus(Status status) {


                if (!status.getUser().getScreenName().equals(UserName)) {
                    //el ban hamer
                    System.out.println(simpleDateFormat.format(new Date()) + "\t @" + status.getUser().getScreenName() + " - " + status.getText());
                    String cadbanHammer = banHammer.getBanHammer(status.getUser().getScreenName(), MinutosTolerancia, NumeroTwitts);
                    String cancer = "";
                    if (cadbanHammer.equals("OK")) {
                        cancer = twitts.getTwitt(status.getUser().getScreenName());
                    } else if (!cadbanHammer.equals("BANHAMMER")) {
                        cancer = cadbanHammer;
                    }

                    if (!cancer.isEmpty()) {
                        try {
                            publica(cancer);
                        } catch (TwitterException ex) {
                            java.util.logging.Logger.getLogger(Hispatwitter.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }


                }

            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };



        twitterStream.addListener(listener);
        ArrayList<Long> follow = new ArrayList<Long>();
        ArrayList<String> track = new ArrayList<String>();

        track.addAll(Arrays.asList(Filtro.split(",")));

        long[] followArray = new long[follow.size()];
        for (int i = 0; i < follow.size(); i++) {
            followArray[i] = follow.get(i);
        }
        String[] trackArray = track.toArray(new String[track.size()]);

        twitterStream.filter(new FilterQuery(0, followArray, trackArray));

    }

    public static void Configuracion(String consumerKey, String consumerSecret) throws URISyntaxException {
        File file = new File("twitter4j.properties");
        Properties prop = new Properties();
        InputStream is = null;
        OutputStream os = null;
        try {
            if (file.exists()) {
                is = new FileInputStream(file);
                prop.load(is);
                return;
            }
            if (consumerKey.isEmpty() || consumerSecret.isEmpty()) {
                if (null == prop.getProperty("oauth.consumerKey")
                        && null == prop.getProperty("oauth.consumerSecret")) {
                    // consumer key/secret are not set in twitter4j.properties
                    System.out.println(
                            "Usage: java twitter4j.examples.oauth.GetAccessToken [consumer key] [consumer secret]");
                    System.exit(-1);
                }
            } else {
                prop.setProperty("oauth.consumerKey", consumerKey);
                prop.setProperty("oauth.consumerSecret", consumerSecret);
                os = new FileOutputStream("twitter4j.properties");
                prop.store(os, "twitter4j.properties");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(-1);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignore) {
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ignore) {
                }
            }
        }
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            RequestToken requestToken = twitter.getOAuthRequestToken();
            System.out.println("request token.");
            System.out.println("Request token: " + requestToken.getToken());
            System.out.println("Request token secret: " + requestToken.getTokenSecret());
            AccessToken accessToken = null;

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (null == accessToken) {
                System.out.println("Abra la siguiente URL y permitir el acceso a tu cuenta:");
                System.out.println(requestToken.getAuthorizationURL());
                try {
                    Desktop.getDesktop().browse(new URI(requestToken.getAuthorizationURL()));
                } catch (UnsupportedOperationException ignore) {
                } catch (IOException ignore) {
                } catch (URISyntaxException e) {
                    throw new AssertionError(e);
                }
                System.out.print("Introduzca el PIN (si está disponible) y pulsa ENTER [PIN].:");
                String pin = br.readLine();
                try {
                    if (pin.length() > 0) {
                        accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                    } else {
                        accessToken = twitter.getOAuthAccessToken(requestToken);
                    }
                } catch (TwitterException te) {
                    if (401 == te.getStatusCode()) {
                        System.out.println("No se puede obtener el token de acceso.");
                    } else {
                        te.printStackTrace();
                    }
                }
            }
            System.out.println("Consiguió el access token.");
            System.out.println("Access token: " + accessToken.getToken());
            System.out.println("Access token secret: " + accessToken.getTokenSecret());

            try {
                prop.setProperty("oauth.accessToken", accessToken.getToken());
                prop.setProperty("oauth.accessTokenSecret", accessToken.getTokenSecret());
                os = new FileOutputStream(file);
                prop.store(os, "twitter4j.properties");
                os.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.exit(-1);
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException ignore) {
                    }
                }
            }
            System.out.println("Almacenado con éxito access token en " + file.getAbsolutePath() + ".");
            return;
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get accessToken: " + te.getMessage());
            System.exit(-1);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Failed to read the system input.");
            System.exit(-1);
        }
    }
}
