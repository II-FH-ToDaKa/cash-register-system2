
package cashRegisterSystem;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by karls_000 on 18.11.2015.
 */
public class cashRegisterSystem

{
    private static final int MAX_BARCODE_LENGTH=14;
    public class inventoryArticle
    {
        private char barcode[];
        private String name;
        private int amount;
        private double price;

        public boolean isFood() {
            return isFood;
        }

        public void setFood(boolean food) {
            isFood = food;
        }

        private boolean isFood;

        public inventoryArticle(char barcode[], String name, int amount, double price)
        {
            this.barcode = barcode;
            this.name = name;
            this.amount = amount;
            this.price = price;
        }

        public char[] getBarcode() {
            return barcode;
        }

        public void setBarcode(char[] barcode) {
            this.barcode = barcode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }

    private List <inventoryArticle> inventory = new ArrayList<inventoryArticle>();
    /*
     *add an Article to your Cart returns if it was successful
     */
    public boolean addArticle(char[] iBarcode, int iAmount, cart ccart)
    {
        cart.article newcart=null;
        inventoryArticle tempart;
        tempart=search_Article(iBarcode);

        if(tempart!=null)
        {

            newcart.setBarcode(tempart.barcode);
            newcart.setName(tempart.getName());
            newcart.setAmount(iAmount);
            ccart.setArticle(newcart);

            return true;
        }
        else
        {
            //ERROR OUTPUT
            //no article was found with that specific barcode//
        }

        return false;
    }
    /*
    *deletes an Article from your Cart, returns if it was successful
    */
    boolean delArticle(char[] cBarcode, int iAmount, cart ccart)
    {
        int isizeInventory=inventory.size();
        int isearchArticle=-1;
        List <cart.article> alList=new ArrayList<cart.article>();
        alList=ccart.getArticle();

        for (int i = 0; i < isizeInventory; i++)
        {
            if (compare_Barcode(cBarcode, alList.get(i).getBarcode())) //find the article number in the cart
            {
                isearchArticle=i;
                break;
            }
            else
            {
                //ERROR NOT FOUND
                return false;
            }
        }

        if (alList.get(isearchArticle).getAmount()==iAmount) //decide delete one / more or all items of the article in the list
        {
            //delete complete articles from ccart
            ccart.removeArticle(isearchArticle);
            removeFromPrice(ccart,cBarcode,iAmount);

            return true;
        }
        else
        {
            removeFromPrice(ccart,cBarcode,iAmount);
            //delete a specific amount from ccart
            removeFromPrice(ccart,cBarcode,iAmount);

            ccart.setSpecificArticle(isearchArticle,alList.get(isearchArticle).getBarcode()
                    ,alList.get(isearchArticle).getName()
                    ,iAmount);
            return true;
        }


    }
    /*
    *give an discount to the whole cart (Prices)
    */
    boolean discount(cart ActualCart, int iAmount)
    {
        ActualCart.setiDiscount(iAmount);
        return true;
    }
    /*
    *give the product an individual price to an article
    */
    public boolean other_price(cart ActualCart, char cBarcode[], double dprice)
    {

        //Search Article in Cart
        int iCurrentCart=0;

        while(!compare_Barcode(ActualCart.getArticle().get(iCurrentCart).getBarcode(),cBarcode))
        {

            if(iCurrentCart==ActualCart.getArticle().size())
            {
                wrong_article(cBarcode);
                return false;
            }
            iCurrentCart++;
        }

        //search Article in Inventory
        /*int iCurrentInventory=0;
        while(!compare_Barcode(inventory.get(iCurrentInventory).getBarcode(),cBarcode)&&(iCurrentInventory<inventory.size()))
        {
            iCurrentInventory++;
        }

        double dNormalPrice;
        dNormalPrice=inventory.get(iCurrentCart).getPrice();
        */
        ActualCart.getArticle().get(iCurrentCart).setPrice(dprice);
        return true;

    }

    //add Price when scanned
    public void addToPrice(cart ccart, char[] barcode, int amount)
    {
        inventoryArticle currentArticle;
        currentArticle = search_Article(barcode);
        if(currentArticle.isFood())
        {
            ccart.setdFullPrice(ccart.getdFullPrice() + (currentArticle.getPrice() * amount) * ccart.TAX_FOOD);
        }
        else
        {
            ccart.setdFullPrice(ccart.getdFullPrice() + (currentArticle.getPrice() * amount) * ccart.TAX_NORMAL);
        }
    }

    public void removeFromPrice(cart ccart, char[] barcode, int amount)
    {
        inventoryArticle currentArticle;
        currentArticle = search_Article(barcode);
        if(currentArticle.isFood())
        {
            ccart.setdFullPrice(ccart.getdFullPrice() - (currentArticle.getPrice() * amount) * ccart.TAX_FOOD);
        }
        else
        {
            ccart.setdFullPrice(ccart.getdFullPrice() - (currentArticle.getPrice() * amount) * ccart.TAX_NORMAL);
        }
    }
    /*
     * adds item to the inventory
     */
    public boolean new_item(char[] cBarcode, String sName, double dPrice,int iAmount, boolean bisFood )
    {
        inventoryArticle newArticle=null;
        newArticle.setBarcode(cBarcode);
        newArticle.setPrice(iAmount);
        newArticle.setName(sName);
        newArticle.setFood(bisFood);

        inventory.add(newArticle);
        write_inventory() ;
        return false;
    }
    /*
     * removes item from the inventory
     */
    public boolean remove_item(char[] barcode)
    {
        inventoryArticle toBeRemoved;
        toBeRemoved = search_Article(barcode);
        if(toBeRemoved != null)
        {
            inventory.remove(toBeRemoved);
            write_inventory();
            return true;
        }
        else
        {
            return false;
        }
    }
    //Display Barcode, Name, Price, new Price, Amount (and Picture) from a Article
    public void display_article (char cBarcode[], cart ActualCart)
    {
        //searchArticle in Cart
        int iCurrentCart=0;

        while(!compare_Barcode(ActualCart.getArticle().get(iCurrentCart).getBarcode(),cBarcode))
        {

            if(iCurrentCart==ActualCart.getArticle().size())
            {
                wrong_article(cBarcode);
            }
            iCurrentCart++;
        }

        double dNewPrice=ActualCart.getArticle().get(iCurrentCart).getPrice();
        int iAmount=ActualCart.getArticle().get(iCurrentCart).getAmount();

        //search Article in Inventory
        int iCurrentInventory=0;
        while(!compare_Barcode(inventory.get(iCurrentInventory).getBarcode(),cBarcode)&&(iCurrentInventory<inventory.size()))
        {
            if(iCurrentCart==ActualCart.getArticle().size())
            {
                wrong_article(cBarcode);
            }
            iCurrentInventory++;
        }

        double dNormalPrice=inventory.get(iCurrentCart).getPrice();
        String sName=inventory.get(iCurrentCart).getName();

        //Output

        System.out.println(sName+"("+dNormalPrice+"$)");
        System.out.println(cBarcode+"\t"+iAmount+"\tx\t"+dNewPrice+"$");

    }
    public void inventory()
    {
        char cBarcode[];
        String SName;
        int iAmount;
        double dPrice;
        System.out.println("Barcode\tArtikel\t\tMenge\tPreis");
        for(int iInventoryCounter=0; iInventoryCounter<inventory.size();iInventoryCounter++)
        {
            cBarcode= inventory.get(iInventoryCounter).getBarcode();
            SName = inventory.get(iInventoryCounter).getName();
            iAmount = inventory.get(iInventoryCounter).getAmount();
            dPrice =inventory.get(iInventoryCounter).getPrice();
            System.out.println(cBarcode+"\t"+SName+"\t"+iAmount+"\t"+dPrice);
        }
    }
    public boolean update()
    {
        //Fals if any deviation
        boolean bCorrect=true;
        cashRegisterSystem invetoryData =new cashRegisterSystem();

        invetoryData.read_inventory();

        int iCurrentInventory;
        char cBarcode[];
        //Go the each article
        for(int iCurrentInventoryData=0; iCurrentInventoryData<invetoryData.inventory.size(); iCurrentInventoryData++)
        {
            cBarcode=invetoryData.inventory.get(iCurrentInventoryData).getBarcode();
            //search the Barcode from the extern Data in the Inventory


            for(iCurrentInventory=0; !compare_Barcode(cBarcode,inventory.get(iCurrentInventory).getBarcode())&&iCurrentInventory<inventory.size();iCurrentInventory++)
            {

            }
            if(!compare_Barcode(cBarcode,inventory.get(iCurrentInventory).getBarcode()))
            {
                //Article dont exist
                //Create new Article

                inventory.add(new inventoryArticle(cBarcode, invetoryData.inventory.get(iCurrentInventoryData).getName(),invetoryData.inventory.get(iCurrentInventoryData).getAmount(),invetoryData.inventory.get(iCurrentInventoryData).getPrice()));

                System.out.println("Artikle "+invetoryData.inventory.get(iCurrentInventoryData).getName()+" wurde hinzugefügt");
                bCorrect=false;
            }
            else
            {
                if(invetoryData.inventory.get(iCurrentInventoryData).getPrice()!=inventory.get(iCurrentInventory).getPrice())
                {
                    //Price is changed

                    //Check if price <=0
                    bCorrect = false;
                    inventory.get(iCurrentInventory).setPrice(invetoryData.inventory.get(iCurrentInventoryData).getPrice());
                }
                else
                {

                }
                if(invetoryData.inventory.get(iCurrentInventoryData).getAmount()<inventory.get(iCurrentInventory).getAmount())
                {
                    //More Amount in Inventory as in extern Inventory
                    bCorrect = false;
                }
                inventory.get(iCurrentInventory).setAmount(invetoryData.inventory.get(iCurrentInventoryData).getAmount());

                if(!inventory.get(iCurrentInventory).getName().equals(invetoryData.inventory.get(iCurrentInventoryData).getName()))
                {
                    //The Name is changed

                    bCorrect = false;
                    inventory.get(iCurrentInventory).setName(invetoryData.inventory.get(iCurrentInventoryData).getName());
                }


            }

        }
        System.out.println("---------------------------------");
        System.out.println("Update komplett");
        return bCorrect;
    }
    public void statistic() {
        int iNewArticle = 0;
        cashRegisterSystem invetoryData = new cashRegisterSystem();
        invetoryData.read_inventory();

        boolean bTableHeader = false;
        int iAmountDifference;
        double dEarning = 0;

        for (int iCurrentInventory = 0; inventory.size() > iCurrentInventory; iCurrentInventory++) {
            //search in extern Data
            for (int iCurrrentInventoryData = 0; invetoryData.inventory.size() > iCurrrentInventoryData; iCurrrentInventoryData++) {
                if (compare_Barcode(invetoryData.inventory.get(iCurrrentInventoryData).getBarcode(), inventory.get(iCurrentInventory).getBarcode())) {
                    if (invetoryData.inventory.get(iCurrrentInventoryData).getAmount() > inventory.get(iCurrentInventory).getAmount())

                    {
                        if (bTableHeader = false) {
                            System.out.println("Es wurde folgende Artikel verkauft:");
                            System.out.println("Artikel\t\t\t\tAnzahl");
                            bTableHeader = true;
                        }
                        iAmountDifference = invetoryData.inventory.get(iCurrrentInventoryData).getAmount() - inventory.get(iCurrentInventory).getAmount();
                        System.out.println(inventory.get(iCurrentInventory).getName() + "\t " + iAmountDifference);

                        dEarning = dEarning + (iAmountDifference * inventory.get(iCurrentInventory).getPrice());
                    }
                }
            }
        }
        if (dEarning > 0)
        {
            System.out.println("------------------");
            System.out.println("Sie haben "+dEarning+" eingenommen.");
        }
        else
        {
            System.out.println("Es wurde kein Artikel verkauft");
        }
    }
    public void OutputInventory()
    {
        int iTotalProducts=0;
        for(int iCurrentInventory=0; iCurrentInventory<inventory.size();iCurrentInventory++)
        {
            if(iTotalProducts==0)
            {
                System.out.println("Barcode\t Artikel\t\t\t\tMenge\tPreis");
            }
            iTotalProducts++;

            System.out.println(inventory.get(iCurrentInventory).getBarcode()+"\t"+inventory.get(iCurrentInventory).getName()+"\t"+inventory.get(iCurrentInventory).getAmount()+"\t"+inventory.get(iCurrentInventory).getPrice());

        }
        if(iTotalProducts==0)
        {
            System.out.println("Es befindet sich kein Artikel in Bestand");
        }
        else
        {
            System.out.println("------------------");
            System.out.println("Es befinden sich "+iTotalProducts+ "im Bestand");
        }


    }

    public void wrong_article(char[] barcode)
    {
        System.out.println(barcode+" nicht vorhanden!");
    }

    //Helpfunction that reads the inventory file and puts it in a list for easier use
    private void read_inventory()
    {
        try
        {
            FileReader reader = new FileReader("inventory.txt");
            BufferedReader br = new BufferedReader(reader);

            String row = br.readLine();
            while (row != null)
            {
                String articleinfo[] = row.split("|");

                char barcode[] = articleinfo[0].toCharArray();
                String name = articleinfo[1];
                int amount = Integer.parseInt(articleinfo[2]);
                double price = Double.parseDouble(articleinfo[3]);
                boolean food = Boolean.parseBoolean(articleinfo[4]);

                inventory.add(new inventoryArticle(barcode, name, amount, price));
                row = br.readLine();
            }

            br.close();
        }
        catch (IOException e)
        {
            //Error Handling exit with Error 1
            System.out.println("Error" + e.getMessage());
            System.exit(1);
        }
    }

    //Helpfunction that puts the list in the file
    private void write_inventory()
    {
        try
        {
            File file = new File("inventory.txt");
            if(file.delete()) {}
            else
            {
                System.out.println("Delete operation is failed.");
            }

            FileWriter writer = new FileWriter("inventory.txt");


            for(int counter = 0; counter<inventory.size(); counter++)
            {
                writer.write(inventory.get(counter)+"|"
                        +inventory.get(counter).getName()+"|"
                        +String.valueOf(inventory.get(counter).getAmount())+"|"
                        +String.valueOf(inventory.get(counter).getPrice())+"|"
                        +String.valueOf(inventory.get(counter).isFood()));
            }

            writer.close();
        }
        catch(IOException e)
        {
            //Error Handling exit with Error 2
            System.out.println("Error" + e.getMessage());
            System.exit(2);
        }


    }
    private inventoryArticle search_Article(char barcode[])
    {

        for(int iArticleCount=0;iArticleCount<inventory.size();iArticleCount++)
        {
            if(compare_Barcode(barcode, inventory.get(iArticleCount).barcode))
            {
                return inventory.get(iArticleCount);
            }

        }
        return null;
    }
    private boolean compare_Barcode(char barcodeA[], char barcodeB[])
    {
        for(int iBarcodeCount=0; iBarcodeCount<MAX_BARCODE_LENGTH; iBarcodeCount++)
        {
            if(barcodeA[iBarcodeCount]!=barcodeB[iBarcodeCount])
            {
                return false;
            }
        }
        return true;
    }
}

