/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hispatwitter;

/**
 *
 * @author user
 */
public class test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Twitts twitts = new Twitts("TETS1,Test2","false");

        BanHammer banHammer = new BanHammer();

        for (int i = 0; i < 100; i++) {
            String cadbanHammer = banHammer.getBanHammer("test", 10, 15);
            String cancer = "";
            if (cadbanHammer.equals("OK")) {
                cancer = twitts.getTwitt("test");
            } else if (!cadbanHammer.equals("BANHAMMER")) {
                cancer = cadbanHammer;
            }
            System.out.println(i + cancer);


        }

    }
}
