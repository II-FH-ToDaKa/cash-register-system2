package cashRegisterSystem;
import java.util.ArrayList;
import java.util.List;

/**
 * Class: cart
 * @author Karl
 *
 * this is our cart which will be the cart of the customer
 * here are all informations saved which are connected with the items the customer ordered.
 * Here are the length of the barcode, and the taxes for the price calculation
 * also the cartlist which is filled with the articles the customer buyed
 *
 * */


public class cart
{
    public static final int MAX_CHAR=14;
    public static final double TAX_NORMAL=1.19;
    public static final double TAX_FOOD=1.07;

    private List<article> alcartInventory= new ArrayList<article>();
    private int     iDiscount=0;
    private double  dFullPrice;
    private double  dPricewoTax;
    private double  dTax;

    static public class article
    {
        private char cBarcode[] =new char [MAX_CHAR];
        private String sName;
        private int iAmount;
        private double dPrice;

        public article(char cBarcode_new[],String sName_new,int iAmount_new,double dPrice_new)
        {
            this.cBarcode=cBarcode_new;
            this.sName=sName_new;
            this.iAmount=iAmount_new;
            this.dPrice=dPrice_new;
        }
        public char[] getBarcode() {
            return cBarcode;
        }

        public void setBarcode(char[] barcode) {
            cBarcode = barcode;
        }

        public String getName() {
            return sName;
        }

        public void setName(String name) {
            sName = name;
        }

        public int getAmount() {
            return iAmount;
        }

        public void setAmount(int amount) {
            this.iAmount = amount;
        }

        public  void setPrice(double Price)
        {
            this.dPrice=Price;
        }
        public double getPrice()
        {
            return dPrice;
        }
    }

    /*
     * //////////////////////////////// set / get Article //////////////////////////////////
     */
    public void setArticle(article new_article)
    {
        this.alcartInventory.add(new_article);
    }
    public List<article> getArticle()
    {
        return alcartInventory;
    }
    public void removeArticle(int element)
    {
        alcartInventory.remove(element);
    }
    public void setSpecificArticle(int element,char[] cBarcode,String sName,int iAmount)
    {
        alcartInventory.get(element).setBarcode(cBarcode);
        alcartInventory.get(element).setName(sName);
        alcartInventory.get(element).setBarcode(cBarcode);
        alcartInventory.get(element).setAmount(iAmount);


    }
    /*
     * //////////////////////////////// set / get discount ////////////////////////////////
     */
    public void setiDiscount(int iDiscount)
    {
        this.iDiscount = iDiscount;
    }

    public int getiDiscount()
    {

        return iDiscount;
    }

    /*
     * /////////////////////////////// set / get FullPrice ////////////////////////////////
     */
    public double getdFullPrice()
    {
        return dFullPrice;
    }

    public void setdFullPrice(double dFullPrice)
    {
        this.dFullPrice = dFullPrice;
    }

    /*
     * //////////////////////////////// set / get PricewoTax ////////////////////////////////
     */

    public double getdPricewoTax()
    {
        return dPricewoTax;
    }

    public void setdPricewoTax(double dPricewoTax)
    {
        this.dPricewoTax = dPricewoTax;
    }
    /*
     * //////////////////////////////// set / get Tax //////////////////////////////////////
     */

    public double getdTax()
    {
        return dTax;
    }

    public void setdTax(double dTax)
    {
        this.dTax = dTax;
    }

}
