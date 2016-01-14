import cashRegisterSystem.*;

public class Main {

    public static void main(String[] args) {
        cashRegisterSystem Netto = new cashRegisterSystem();
        char[] barcode={'1','1','2','6','3','7','4','5','6','5','8','7','4','4'};

        Netto.new_item(barcode,"Birne",0.99,4,true);

        cart KundeKarl =new cart();

        Netto.addArticle(barcode,2,KundeKarl);
        char [] barcode2={'1','1','2','6','3','7','6','5','6','5','8','7','4','4'};

        Netto.display_article(barcode2,KundeKarl);

    }
}


