
package cashRegisterSystem;


import com.sqlconnector.ConnectionConfiguration;
import com.sun.corba.se.spi.orbutil.fsm.Guard;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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
     *
     * @author Tobias,Daniel
     *
     * this is the class to define the stock which is currently in the store
     * will be the datatype for a list
     * the stock is listed in a txt file asnd will have the structure like the variables in this class
     */
    static public class inventoryArticle
    {

        long iBarcode;
        private char barcode[];
        private String name;
        private int amount;
        private double price;
        private boolean isFood;
        private int BonID;

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
     *
     * this is the stocklist which every function works with
     * it will be filled with information from the inventory.txt
     * and at the end it will be copied again into the txt for the difference what was selled
     */
    private List <inventoryArticle> inventory = new ArrayList<inventoryArticle>();

    public boolean addArticle(long iBarcode, int iAmount,int iBonID, int iCustomerID)
    {
            ConnectionConfiguration Connection =new ConnectionConfiguration();
            boolean ReturnValue=false;
            int ActualAmount=AmountArticle(iBarcode);
            if(ActualAmount>=iAmount)
            {
                ReturnValue =Connection.Update("INSERT INTO `boninventory` (`BonInventoryID`, `barcode`, `amount`, `DateTime`, `BonID`) VALUES ('"+iCustomerID+"', '"+iBarcode+"', '"+iAmount+"', CURRENT_TIMESTAMP, '"+iBonID+"')");
                Connection.Update("UPDATE `inventory` SET `amount` = '"+(ActualAmount-iAmount)+"' WHERE `inventory`.`barcode` ="+iBarcode);
            }
            else
            {

                ErrorMessage("Artikel nicht im Bestand");
            }
            return ReturnValue;

    }

    public int AmountArticle(long iBarcode)
    {
        ConnectionConfiguration Connection =new ConnectionConfiguration();
        return Integer.parseInt (Connection.OnResult("SELECT amount FROM inventory WHERE barcode="+iBarcode));

    }
    /**Function: delArticle
     * @author Karl
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

        inventoryArticle tempart;
        tempart= searchArticle(cBarcode);


        for (int i = 0; i < isizeInventory; i++)
        {
            if (compareBarcode(cBarcode, alList.get(i).getBarcode())) //find the article number in the cart
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

            tempart.setAmount(tempart.getAmount()+ iAmount);

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

            tempart.setAmount(tempart.getAmount()+ iAmount);
            return true;
        }


    }

    /**
     * Function: discount
     * @author Daniel
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
     * @author Daniel
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
        if(dprice<0)
        {
            return false;
        }
        iCurrentCart=searchArticleInCart(ActualCart,cBarcode);

        if(iCurrentCart==-1)
        {
            wrongArticle(cBarcode);
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
     * @author Tobias
     * @param ccart
     * @param cBarcode
     * @param iAmount
     * @return no return value
     *
     * this function will be called if an article is added to the cart
     * it adds the old price + the new article
     * combination with add article
     */
    public void addToPrice(cart ccart, char[] cBarcode, int iAmount)
    {
        inventoryArticle currentArticle;
        currentArticle = searchArticle(cBarcode);
        if(currentArticle.isFood())
        {
            ccart.setdFullPrice(ccart.getdFullPrice() + (currentArticle.getPrice() * iAmount) * ccart.TAX_FOOD);
        }
        else
        {
            ccart.setdFullPrice(ccart.getdFullPrice() + (currentArticle.getPrice() * iAmount) * ccart.TAX_NORMAL);
        }
    }

    /**
     * Function: removeFromPrice
     * @author Tobias
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
        currentArticle = searchArticle(barcode);
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
     * @author Karl
     * @param cBarcode
     * @param sName
     * @param dPrice
     * @param iAmount
     * @param bisFood
     * @return if a article was created successfully
     *
     * adds a new article into the inventory list
     */
    public boolean newItem(char[] cBarcode, String sName, double dPrice,int iAmount, boolean bisFood )
    {
        inventoryArticle newArticle=new inventoryArticle(cBarcode, sName, iAmount,dPrice);
        newArticle.setFood(bisFood);

        inventory.add(newArticle);
        return true;
    }

    /**
     * Function: removeItem
     * @author Tobias
     * @param cBarcode
     * @return successful if item was removed from the inventory, failed if item wasn't found
     *
     * remove a specific item from the inventory list
     */
    public boolean removeItem(char[] cBarcode)
    {
        inventoryArticle toBeRemoved;
        toBeRemoved = searchArticle(cBarcode);
        if(toBeRemoved != null)
        {
            inventory.remove(toBeRemoved);
            writeInventory();
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
     * @author Daniel
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
            wrongArticle(cBarcode);
        }
        else
        {
            int iDiscount=ActualCart.getiDiscount();
            int iAmount=ActualCart.getArticle().get(iCurrentCart).getAmount();
            double dNormalPrice=ActualCart.getArticle().get(iCurrentCart).getPrice();
            String sName=ActualCart.getArticle().get(iCurrentCart).getName();
            double dNewPrice=(dNormalPrice*iAmount)-(dNormalPrice*iAmount*(iDiscount*0.01));
            //Output

            System.out.print(sName);
            System.out.printf(" (%1$.2f$)\n",dNormalPrice);
            System.out.print(new String(cBarcode)+"\t"+iAmount+"\tx\t");
            System.out.printf("%1$.2f$\n",dNewPrice);
        }
    }
    /**
     * Function: displayAll
     * @param ActualCart
     * @return no return value
     *
     * Displays all articles from  the cart
     * shows barcode, name, price, the price with discount, amount (picture ....later)
     *
     */
    public void displayAll(cart ActualCart)
    {

        System.out.println("--------------------------------");
        double dNewPrice;
        double dFullPrice=0;
        double dTax=0;
        inventoryArticle TempArticle;
        for(int iCounter = 0; iCounter<ActualCart.getArticle().size(); iCounter++)
        {
            displayArticle(ActualCart.getArticle().get(iCounter).getBarcode(),ActualCart);

            dFullPrice+=ActualCart.getArticle().get(iCounter).getPrice()*ActualCart.getArticle().get(iCounter).getAmount()-(ActualCart.getArticle().get(iCounter).getPrice()*ActualCart.getArticle().get(iCounter).getAmount()*ActualCart.getiDiscount()*0.01);

            TempArticle=searchArticle(ActualCart.getArticle().get(iCounter).getBarcode());
            if(TempArticle.isFood())
            {
                dTax+=ActualCart.TAX_FOOD*0.01*(ActualCart.getArticle().get(iCounter).getPrice()*ActualCart.getArticle().get(iCounter).getAmount()-(ActualCart.getArticle().get(iCounter).getPrice()*ActualCart.getArticle().get(iCounter).getAmount()*ActualCart.getiDiscount()*0.01));
            }
            else
            {
                dTax+=ActualCart.TAX_NORMAL*0.01*(ActualCart.getArticle().get(iCounter).getPrice()*ActualCart.getArticle().get(iCounter).getAmount()-(ActualCart.getArticle().get(iCounter).getPrice()*ActualCart.getArticle().get(iCounter).getAmount()*ActualCart.getiDiscount()*0.01));
            }
        }

        ActualCart.setdPricewoTax(dTax);
        ActualCart.setdFullPrice(dFullPrice);
        System.out.println("--------------------------------");
        System.out.printf("Summe:\t\t\t\t\t\t%1$.2f€\n",ActualCart.getdFullPrice());
        if(ActualCart.getiDiscount()!=0)
        {
            System.out.println("Rabatt:\t\t\t\t\t\t"+ActualCart.getiDiscount()+"%");
        }
        System.out.printf("Steuern:\t\t\t\t\t%1$.2f€\n",ActualCart.getdPricewoTax());
        System.out.println("--------------------------------");
        System.out.println("--------------------------------");
    }


    /**
     * Function: update
     * @author Daniel
     * @return successful update process
     *
     * this function will check differences between inventory list and inventory.txt
     * so if you have added a new item it is currently saved on the cash register
     * after update the difference is spotted and the new item will be added to the inventory.txt
     *
     * the same will happen if you have changed the price on the cash register for some items
     * or the name
     * or the amount in7 the cash register is larger than the amount saved in the inventory.txt
     * or the specification if an article is a food or not
     */
    public boolean update()
    {
        cashRegisterSystem invetoryData =new cashRegisterSystem();

        //False if any deviation
        boolean bCorrect=invetoryData.readInventory();

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
                if (compareBarcode(inventory.get(iCurrentInventory).getBarcode(), invetoryData.inventory.get(iCurrentInventoryData).getBarcode())) {
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
    /**
     * Funtion: statistic
     * @author Daniel
     * @return no return value
     *
     * this function operates similar to updates but instead of only checking differences which item wa added
     * this will check the amount difference between the inventory and the inventory.txt
     * to calculate the turnover with the sold products
     */
    public double statistic() {
        int iNewArticle = 0;
        cashRegisterSystem invetoryData = new cashRegisterSystem();
        invetoryData.readInventory();

        boolean bTableHeader = false;
        int iAmountDifference;
        double dEarning = 0;

        for (int iCurrentInventory = 0; inventory.size() > iCurrentInventory; iCurrentInventory++) {
            //search in extern Data
            for (int iCurrrentInventoryData = 0; invetoryData.inventory.size() > iCurrrentInventoryData; iCurrrentInventoryData++) {
                if (compareBarcode(invetoryData.inventory.get(iCurrrentInventoryData).getBarcode(), inventory.get(iCurrentInventory).getBarcode())) {
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
        return dEarning;
    }
    /**
     * Funtion: outputInventory
     * @author Daniel
     * @return no return value
     *
     * this will disply the current stock which is registerd in the inventory list
     *
     */

    public void outputInventory()
    {
        int iTotalProducts=0;
        for(int iCurrentInventory=0; iCurrentInventory<inventory.size();iCurrentInventory++)
        {
            if(iTotalProducts==0)
            {
                System.out.println("Barcode\t Artikel\t\t\t\tMenge\tPreis");
            }
            iTotalProducts++;

            System.out.println(new String(inventory.get(iCurrentInventory).getBarcode())+"\t"+inventory.get(iCurrentInventory).getName()+"\t"+inventory.get(iCurrentInventory).getAmount()+"\t\t"+inventory.get(iCurrentInventory).getPrice());

        }
        if(iTotalProducts==0)
        {
            System.out.println("Es befindet sich kein Artikel in Bestand");
        }
        else
        {
            System.out.println("------------------");
            System.out.println("Es befinden sich "+iTotalProducts+ " Artikel im Bestand");
        }


    }
    /**
     * Function wrongArticle
     * @author Daniel
     * @param barcode
     *
     * simple output to display that you tried a wrong
     */
    public void wrongArticle(char[] barcode)
    {
        System.out.println(new String(barcode)+" nicht vorhanden!");
    }



    /**
     * Function: readInventory
     * @author Tobias
     * @return no return value
     *
     * this function reads the inventory.txr and copies the content into the inventory list
     * mainly used at the start of the cash register system
     */
    private boolean readInventory()
    {
        try
        {
            FileReader reader = new FileReader("inventory.txt");
            BufferedReader br = new BufferedReader(reader);

            String row = br.readLine();
            while (row != null)
            {
                String articleinfo[] = row.split(":");

                char barcode[] = articleinfo[0].toCharArray();
                String name = articleinfo[1];
                int amount = Integer.parseInt(articleinfo[2]);
                double price = Double.parseDouble(articleinfo[3]);
                boolean food = Boolean.parseBoolean(articleinfo[4]);

                inventory.add(new inventoryArticle(barcode, name, amount, price));
                row = br.readLine();
            }

            br.close();
            return true;
        }
        catch (IOException e)
        {
            System.out.println("Error" + e.getMessage());
            System.out.println("Error reading File");
            return false;
        }
    }



    /**
     * Function: writeInventory
     * @author Tobias
     * @return no return value
     *
     * this function is the opposite of read_invetory
     * it will put the altered stock from the inventory list into the inventory.txt
     */
    public boolean writeInventory()
    {
        try
        {
            FileWriter writer = new FileWriter("inventory.txt");


            for(int counter = 0; counter<inventory.size(); counter++)
            {
                writer.write(inventory.get(counter).getBarcode());
                writer.append(":"
                        +inventory.get(counter).getName()+":"
                        +String.valueOf(inventory.get(counter).getAmount())+":"
                        +String.valueOf(inventory.get(counter).getPrice())+":"
                        +String.valueOf(inventory.get(counter).isFood())+'\n');
            }

            writer.close();
            return true;
        }
        catch(IOException e)
        {
            System.out.println("Error" + e.getMessage());
            System.out.println("Error writing File");
            return false;
        }


    }

    /**
     * Function: searchArticle
     * @author Daniel
     * @param cBarcode
     * @return a specific article from the inventory list with the help of the barcode
     * will return a NULL if it didnt work/ didnt found the article
     *
     */
    private inventoryArticle searchArticle(char cBarcode[])
    {

        for(int iArticleCount=0;iArticleCount<inventory.size();iArticleCount++)
        {
            if(compareBarcode(cBarcode, inventory.get(iArticleCount).getBarcode()))
            {
                return inventory.get(iArticleCount);
            }

        }
        //Return null if article dont exist
        return null;
    }

    /**
     * FUnction: compareBarcode
     * @author Daniel
     * @param cBarcodeA
     * @param cBarcodeB
     * @return successful if both barcodes are similar
     *
     * at some point you need to compare to barcodes this function will allow it
     * pretty needed in some functions
     *
     */
    private boolean compareBarcode(char cBarcodeA[], char cBarcodeB[])
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

    /**
     * Function: searchArticleInCart
     * @author Daniel
     * @param ActualCart
     * @param cBarcode
     * @return the postion in the cart at which the searched item is
     *
     *
     */
    private int searchArticleInCart(cart ActualCart, char cBarcode[])
    {
        //System.out.println(new String (cBarcode));
        /*
        int iCurrentCart=0;
        while(!compareBarcode(ActualCart.getArticle().get(iCurrentCart).getBarcode(),cBarcode))
        {

            //System.out.println(ActualCart.getArticle().get(iCurrentCart).getBarcode());
            if(iCurrentCart==ActualCart.getArticle().size()-1)
            {

                return iCurrentCart;
            }
            iCurrentCart++;
        }
        return -1;
        */

        for(int iCounter = 0; iCounter<ActualCart.getArticle().size(); iCounter++)
        {
            if(compareBarcode(ActualCart.getArticle().get(iCounter).getBarcode(), cBarcode))
            {
                return iCounter;
            }
        }
        return -1;

    }
    private void ErrorMessage(String Statement)
    {
        System.out.println(Statement);
    }
}

