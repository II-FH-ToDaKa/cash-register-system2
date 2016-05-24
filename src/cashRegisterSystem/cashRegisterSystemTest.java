package cashRegisterSystem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class cashRegisterSystemTest {
    cashRegisterSystem CRS = new cashRegisterSystem();
    cart myCart = new cart();

    String sBarcode = "12345678901234";
    String sArticleName = "Apfel";
    double dPrice = 0.99;
    int iAmountInventory = 4;
    boolean isFood = true;

    List<cashRegisterSystem.inventoryArticle> tempInventory = new ArrayList<cashRegisterSystem.inventoryArticle>();


    @Before
    public void setUp() throws Exception {
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

                tempInventory.add(new cashRegisterSystem.inventoryArticle(barcode, name, amount, price));
                row = br.readLine();
            }

            br.close();
        }
        catch (IOException e)
        {
            System.out.println("Error" + e.getMessage());
            System.out.println("Error reading File");
        }

        CRS.newItem(sBarcode.toCharArray(), sArticleName, dPrice, iAmountInventory, isFood);
        CRS.writeInventory();
    }

    @After
    public void tearDown() throws Exception {
        try
        {
            FileWriter writer = new FileWriter("inventory.txt");


            for(int counter = 0; counter<tempInventory.size(); counter++)
            {
                writer.write(tempInventory.get(counter).getBarcode());
                writer.append(":"
                        +tempInventory.get(counter).getName()+":"
                        +String.valueOf(tempInventory.get(counter).getAmount())+":"
                        +String.valueOf(tempInventory.get(counter).getPrice())+":"
                        +String.valueOf(tempInventory.get(counter).isFood())+'\n');
            }

            writer.close();
        }
        catch(IOException e)
        {
            System.out.println("Error" + e.getMessage());
            System.out.println("Error writing File");
        }
    }

    @Test
    public void testAddToPrice() throws Exception {
        double dEndPrice = 2.1186; //price*amountBuy*myCart.TAX_FOOD;

        int iAmountBuy = 2;

        CRS.addToPrice(myCart ,sBarcode.toCharArray(), iAmountBuy);

        assertEquals(dEndPrice, myCart.getdFullPrice(), 0.001);
    }

    @Test
    public void testRemoveFromPrice() throws Exception {
        double dEndPrice = 1.0593;//price * (amountBuy - amountRemove) * myCart.TAX_FOOD;

        int iAmountBuy = 2;
        int iAmountRemove = 1;

        CRS.addToPrice(myCart, sBarcode.toCharArray(), iAmountBuy);
        CRS.removeFromPrice(myCart, sBarcode.toCharArray(), iAmountRemove);

        assertEquals(dEndPrice, myCart.getdFullPrice(), 0.001);
    }

    @Test
    public void testStatistic() throws Exception {
        double dEndEarning = 0.99;

        int iAmountBuy = 2;
        int iAmountRemove = 1;

        CRS.delArticle(sBarcode.toCharArray(), iAmountRemove, myCart);

        assertEquals(dEndEarning, CRS.statistic(), 0.001);

    }
}