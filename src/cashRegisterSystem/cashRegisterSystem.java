
package cashRegisterSystem;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Class: cashRegisterSystem
 * This is the main class with all functions which are needed to operate the cash register
 *
 */

public class cashRegisterSystem

{
    /**
     * constant: self explaining, length of the barcodes
     */

    private static final int MAX_BARCODE_LENGTH=13;

    /**
     * Class: inventoryArticle
     * this is the class to define the stock which is currently in the store
     * will be the datatype for a list
     * the stock is listed in a txt file asnd will have the structure like the variables in this class
     */
    public class inventoryArticle
    {
        private char barcode[];
        private String name;
        private int amount;
        private double price;
        private boolean isFood;

        public boolean isFood() {
            return isFood;
        }

        public void setFood(boolean food) {
            isFood = food;
        }

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

    /**
     * List: inventoryArticle
     * this is the stocklist which every function works with
     * it will be filled with information from the inventory.txt
     * and at the end it will be copied again into the txt for the difference what was selled
     */
    private List <inventoryArticle> inventory = new ArrayList<inventoryArticle>();

    /**
     * Function: addArticle
     * @param cBarcode
     * @param iAmount
     * @param ccart
     * @return successful if adding to the cart
     *
     * adds a article to the cart
     * current problem: doesnt remove from inventory
     */
    public boolean addArticle(char[] cBarcode, int iAmount, cart ccart)
    {
        cart.article newcart=new cart.article(cBarcode,"",iAmount,iAmount);
        inventoryArticle tempart;
        tempart=search_Article(cBarcode);

        if(tempart!=null)
        {

            newcart.setBarcode(tempart.barcode);
            newcart.setName(tempart.getName());
            newcart.setAmount(iAmount);
            ccart.setArticle(newcart);
            /**
             * add to price einfügen da sonst nur article hinzugefügt wird
             * und remove from inventory
             */

            return true;
        }
        else
        {
            //ERROR OUTPUT
            //no article was found with that specific barcode//
        }

        return false;
    }
    /**Function: delArticle
     * @param cBarcode
     * @param iAmount
     * @param ccart
     * @return successful if removing from the cart
     *
     * removes an article from the cart
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

    /**
     * Function: discount
     * @param ActualCart
     * @param iDiscount
     * @return successful if changed discount in the cart(this affects the whole order)
     */
    public boolean discount(cart ActualCart, int iDiscount)
    {
        ActualCart.setiDiscount(iDiscount);
        return true;
    }

    /**
     * Function: otherPrice
     * @param ActualCart
     * @param cBarcode
     * @param dprice
     * @return successful if the price was changed manually, returns false if the article(barcode) isn't in the cart with
     * an error message for the wrong article
     *
     * searches for the specific item in the cart, so it has to be added before
     * after the successful found of the article the price will be changed by the given parameter
     */
    public boolean otherPrice(cart ActualCart, char cBarcode[], double dprice)
    {
        int iCurrentCart=0;

        iCurrentCart=searchArticleInCart(ActualCart,cBarcode);

        if(iCurrentCart==-1)
        {
            wrong_article(cBarcode);
            return false;
        }
        else
        {
            ActualCart.getArticle().get(iCurrentCart).setPrice(dprice);
            return true;
        }
    }

    //add Price when scanned

    /**
     * Function: addToPrice
     * @param ccart
     * @param barcode
     * @param amount
     * @return no return value
     *
     * this function will be called if an article is added to the cart
     * it adds the old price + the new article
     * combination with add article
     */
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

    /**
     * Function: removeFromPrice
     * @param ccart
     * @param barcode
     * @param amount
     * @return no return value
     *
     * removes the price of an article from the cart
     * combination with add article
     */
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

    /**
     * Function: newItem
     * @param cBarcode
     * @param sName
     * @param dPrice
     * @param iAmount
     * @param bisFood
     * @return if a article was added successfully
     *
     * adds a new article into the inventory list
     */
    public boolean newItem(char[] cBarcode, String sName, double dPrice,int iAmount, boolean bisFood )
    {
        inventoryArticle newArticle=new inventoryArticle(cBarcode, sName, iAmount,dPrice);
        newArticle.setFood(bisFood);

        inventory.add(newArticle);

        write_inventory() ;
        return true;
    }

    /**
     * Function: removeItem
     * @param cBarcode
     * @return successful if item was removed from the inventory, failed if item wasnt found
     *
     * remove a specific item from the inventory list
     */
    public boolean removeItem(char[] cBarcode)
    {
        inventoryArticle toBeRemoved;
        toBeRemoved = search_Article(cBarcode);
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

    /**
     * Function: displayArticle
     * @param cBarcode
     * @param ActualCart
     * @return no return value
     *
     * Display an specific article from  the cart
     * shows barcode, name, price, the price with discount, amount (picture ....later)
     *
     */
    public void displayArticle (char cBarcode[], cart ActualCart)
    {
        //searchArticle in Cart
        int iCurrentCart=0;

        iCurrentCart=searchArticleInCart(ActualCart,cBarcode);

        if(iCurrentCart==-1)
        {
            wrong_article(cBarcode);
        }
        else
        {
            int iDiscount=ActualCart.getiDiscount();
            int iAmount=ActualCart.getArticle().get(iCurrentCart).getAmount();
            double dNormalPrice=inventory.get(iCurrentCart).getPrice();
            String sName=inventory.get(iCurrentCart).getName();
            double dNewPrice=ActualCart.getArticle().get(iCurrentCart).getPrice();
            dNewPrice=(dNewPrice*iAmount)-(dNewPrice*iAmount*(iDiscount*0.01));
            //Output

            System.out.print(sName);
            System.out.printf(" (%1$.2f$)\n",dNormalPrice);
            System.out.print(new String(cBarcode)+"\t"+iAmount+"\tx\t");
            System.out.printf("%1$.2f$\n",dNewPrice);
        }
    }

    /**
     * Function: inventory
     * @return no return value
     *
     * Prints the whole inventory which is currently saved on the cash register
     */
    public void inventory()
    {
        char cBarcode[];
        String SName;
        int iAmount;
        double dPrice;
        System.out.println("Barcode\t\t\tArtikel\t\t\t\t\t\tMenge\tPreis");
        for(int iInventoryCounter=0; iInventoryCounter<inventory.size();iInventoryCounter++)
        {
            cBarcode= inventory.get(iInventoryCounter).getBarcode();
            SName = inventory.get(iInventoryCounter).getName();
            iAmount = inventory.get(iInventoryCounter).getAmount();
            dPrice =inventory.get(iInventoryCounter).getPrice();
            System.out.println(new String(cBarcode)+"\t"+SName+"\t\t"+iAmount+"\t\t"+dPrice);
        }
    }

    /**
     * Function: update
     * @return successful update process
     *
     * this function will check differences between inventory list and inventory.txt
     * so if you have added a new item it is currently saved on the cash register
     * after update the difference is spotted and the new item will be added to the inventory.txt
     *
     * the same will happen if you have changed the price on the cash register for some items
     * or the name
     * or the amount in the cash register is larger than the amount saved in the inventory.txt
     */
    public boolean update()
    {
        //False if any deviation
        boolean bCorrect=true;
        cashRegisterSystem invetoryData =new cashRegisterSystem();


        //Daten zum Testen
        //Normalerweiße: invetoryData.read_inventory();
        char[] barcode={'4','0','1','4','3','4','8','9','1','6','1','5','8'};
        invetoryData.newItem(barcode,"BASF GLYSANTIN G48 1,5L",13.99,4,false);
        cart KundeKarl =new cart();
        char [] barcode2={'5','0','0','0','1','1','2','5','6','3','7','3','3'};
        invetoryData.newItem(barcode2,"Relentless Energy Drink",1.99,3, true);
        char [] barcode3={'4','0','6','2','4','0','0','1','1','5','4','8','3'};
        invetoryData.newItem(barcode3,"SIERRA Tequila Silver",11.99,3, true);

        char cBarcode[];
        inventoryArticle TempArticle;
        boolean bAnythingChanged;
        //Go the each article
        for(int iCurrentInventoryData=0; iCurrentInventoryData<invetoryData.inventory.size(); iCurrentInventoryData++)
        {
            cBarcode=invetoryData.inventory.get(iCurrentInventoryData).getBarcode();
            //search the Barcode from the extern Data in the Inventory
            TempArticle=null;
            for(int iCurrentInventory=0;iCurrentInventory<inventory.size();iCurrentInventory++)
            {
                if (compare_Barcode(inventory.get(iCurrentInventory).getBarcode(), invetoryData.inventory.get(iCurrentInventoryData).getBarcode())) {
                    //Article found
                    TempArticle=inventory.get(iCurrentInventory);
                }
            }
            if(TempArticle==null) {
                //Article dont exist
                //Create new Article
                inventory.add(new inventoryArticle(cBarcode, invetoryData.inventory.get(iCurrentInventoryData).getName(),invetoryData.inventory.get(iCurrentInventoryData).getAmount(),invetoryData.inventory.get(iCurrentInventoryData).getPrice()));
                System.out.println("Artikel "+invetoryData.inventory.get(iCurrentInventoryData).getName()+" wurde hinzugefügt");

            }
            else
            {
                bAnythingChanged=false;
                if(!TempArticle.getName().equals(invetoryData.inventory.get(iCurrentInventoryData).getName()))
                {
                    System.out.println(TempArticle.getName()+" wurde zu "+invetoryData.inventory.get(iCurrentInventoryData).getName()+" geändert");
                    TempArticle.setName(invetoryData.inventory.get(iCurrentInventoryData).getName());
                    bAnythingChanged=true;
                }
                if(TempArticle.getAmount()!=invetoryData.inventory.get(iCurrentInventoryData).getAmount())
                {
                    System.out.println(TempArticle.getName()+"("+TempArticle.getAmount()+") sind nun "+invetoryData.inventory.get(iCurrentInventoryData).getAmount()+" auf Lager");
                    TempArticle.setAmount(invetoryData.inventory.get(iCurrentInventoryData).getAmount());
                    bAnythingChanged=true;
                }
                if(TempArticle.isFood()!=invetoryData.inventory.get(iCurrentInventoryData).isFood())
                {
                    if(invetoryData.inventory.get(iCurrentInventoryData).isFood())
                    {
                        System.out.println(TempArticle.getName()+" ist nun ein Lebensmittel");
                    }
                    else
                    {
                        System.out.println(TempArticle.getName()+" ist nun kein Lebensmittel");
                    }
                    TempArticle.setFood(invetoryData.inventory.get(iCurrentInventoryData).isFood());
                    bAnythingChanged=true;
                }
                if(TempArticle.getPrice()!=invetoryData.inventory.get(iCurrentInventoryData).getPrice())
                {
                    System.out.println(TempArticle.getName()+"("+TempArticle.getPrice()+") kostet nun "+invetoryData.inventory.get(iCurrentInventoryData).getPrice()+"€");
                    TempArticle.setPrice(invetoryData.inventory.get(iCurrentInventoryData).getPrice());
                    bAnythingChanged=true;
                }
                if(bAnythingChanged)
                {
                    System.out.println("---------------------------------");
                }

            }



        }
        System.out.println("Update komplett");
        System.out.println("---------------------------------");
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
        System.out.println(new String(barcode)+" nicht vorhanden!");
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
            // Error Handling exit with Error 1
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
    private inventoryArticle search_Article(char cBarcode[])
    {

        for(int iArticleCount=0;iArticleCount<inventory.size();iArticleCount++)
        {
            if(compare_Barcode(cBarcode, inventory.get(iArticleCount).getBarcode()))
            {
                return inventory.get(iArticleCount);
            }

        }
        //Return null if article dont exist
        return null;
    }
    private boolean compare_Barcode(char cBarcodeA[], char cBarcodeB[])
    {
        for(int iBarcodeCount=0; iBarcodeCount<MAX_BARCODE_LENGTH; iBarcodeCount++)
        {
            if(cBarcodeA[iBarcodeCount]!=cBarcodeB[iBarcodeCount])
            {
                return false;
            }
        }
        return true;

    }
    private int searchArticleInCart(cart ActualCart, char cBarcode[])
    {
        System.out.println(new String (cBarcode));
        int iCurrentCart=0;
        while(!compare_Barcode(ActualCart.getArticle().get(iCurrentCart).getBarcode(),cBarcode))
        {

            System.out.println(ActualCart.getArticle().get(iCurrentCart).getBarcode());
            if(iCurrentCart==ActualCart.getArticle().size()-1)
            {

                return iCurrentCart;
            }
            iCurrentCart++;
        }
        return -1;
    }
}

