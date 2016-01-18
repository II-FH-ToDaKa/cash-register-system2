import cashRegisterSystem.*;
import sun.nio.ch.Net;

public class Main {

    public static void main(String[] args) {
        cashRegisterSystem Netto = new cashRegisterSystem();

        char[] barcode={'4','0','1','4','3','4','8','9','1','6','1','5','8'};

        Netto.newItem(barcode,"BASF GLYSANTIN G48 1,5L",13.99,4,false);

        cart KundeKarl =new cart();

        char [] barcode2={'5','0','0','0','1','1','2','5','6','3','7','3','3'};
        Netto.newItem(barcode2,"Relentless Energy Drink",1.99,3, true);
        char [] barcode3={'4','0','6','2','4','0','0','1','1','5','4','8','3'};
        Netto.newItem(barcode3,"SIERRA Tequila Silver",11.99,3, true);


        Netto.inventory();
        Netto.update();

    }
}


