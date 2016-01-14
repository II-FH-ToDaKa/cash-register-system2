import cashRegisterSystem.*;
import sun.nio.ch.Net;

public class Main {

    public static void main(String[] args) {
        cashRegisterSystem Netto = new cashRegisterSystem();
        char[] barcode={'1','1','2','6','3','7','4','5','6','5','8','7','4','4'};

        Netto.new_item(barcode,"Birne",0.99,4,true);

        cart KundeKarl =new cart();

        char [] barcode2={'2','1','2','6','3','7','6','5','6','5','8','7','4','4'};
        Netto.new_item(barcode2,"Apfel",1.99,3, true);

        Netto.addArticle(barcode, 2, KundeKarl);
        Netto.addArticle(barcode2, 2, KundeKarl);

        Netto.display_article(barcode,KundeKarl);
        Netto.display_article(barcode2,KundeKarl);

        Netto.statistic();

    }
}


