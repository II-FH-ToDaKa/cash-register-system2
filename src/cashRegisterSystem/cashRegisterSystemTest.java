package cashRegisterSystem;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Tobias on 18.01.2016.
 */
public class cashRegisterSystemTest {

    @Test
    public void testAddArticle() throws Exception {
        boolean returnValue = true;
        String barcode = "12345678901234";
        String articleName = "Apfel";
        double price = 0.99;
        int amount = 1;
        boolean isFood = true;

        cashRegisterSystem CRS = new cashRegisterSystem();
        cart myCart = new cart();


        CRS.newItem(barcode.toCharArray(), articleName, price, 4, isFood);

        assertTrue(CRS.addArticle(barcode.toCharArray(),amount,myCart));

        assertEquals(barcode, new String(myCart.getArticle().get(0).getBarcode()));
        assertEquals(articleName, myCart.getArticle().get(0).getName());
        assertEquals(price, myCart.getArticle().get(0).getPrice(), 0.001);
        assertEquals(amount, myCart.getArticle().get(0).getAmount());

    }

    @Test
    public void testDelArticle() throws Exception {
        String barcode = "12345678901234";
        String articleName = "Apfel";
        double price = 0.99;
        int amount = 2;
        boolean isFood = true;

        cashRegisterSystem CRS = new cashRegisterSystem();
        cart myCart = new cart();


        CRS.newItem(barcode.toCharArray(), articleName, price, 4, isFood);

        assertTrue(CRS.addArticle(barcode.toCharArray(), amount, myCart));
        assertEquals(1, myCart.getArticle().size()); //should be 1

        assertTrue(CRS.delArticle(barcode.toCharArray(), amount, myCart));
        assertEquals(0, myCart.getArticle().size()); //should be 0
    }

    @Test
    public void testDiscount() throws Exception {

    }

    @Test
    public void testOtherPrice() throws Exception {

    }

    @Test
    public void testAddToPrice() throws Exception {
        String barcode1 = "12345678901234";
        String barcode2 = "98765432109876";
        String articleName1 = "Apfel";
        String articleName2 = "Stift";
        double price = 0.99;
        int amount = 1;


        cashRegisterSystem CRS = new cashRegisterSystem();
        cart myCart1 = new cart();
        cart myCart2 = new cart();

        double priceForFood = price*amount*myCart1.TAX_FOOD;
        double priceNotFood = price*amount*myCart2.TAX_NORMAL;

        CRS.newItem(barcode1.toCharArray(), articleName1, price, 4, true);
        CRS.newItem(barcode2.toCharArray(), articleName2, price, 4, false);

        CRS.addToPrice(myCart1, barcode1.toCharArray(), amount);
        CRS.addToPrice(myCart2, barcode2.toCharArray(), amount);

        assertEquals(priceForFood, myCart1.getdFullPrice(), 0.001);
        assertEquals(priceNotFood, myCart2.getdFullPrice(), 0.001);
    }

    @Test
    public void testRemoveFromPrice() throws Exception {
        String barcode1 = "12345678901234";
        String barcode2 = "98765432109876";
        String articleName1 = "Apfel";
        String articleName2 = "Stift";
        double price = 0.99;
        int amount = 1;


        cashRegisterSystem CRS = new cashRegisterSystem();
        cart myCart1 = new cart();
        cart myCart2 = new cart();

        double endPrice = 0.0;

        CRS.newItem(barcode1.toCharArray(), articleName1, price, 4, true);
        CRS.newItem(barcode2.toCharArray(), articleName2, price, 4, false);

        CRS.addToPrice(myCart1, barcode1.toCharArray(), amount);
        CRS.addToPrice(myCart2, barcode2.toCharArray(), amount);

        CRS.removeFromPrice(myCart1, barcode1.toCharArray(), amount);
        CRS.removeFromPrice(myCart2, barcode2.toCharArray(), amount);

        assertEquals(endPrice, myCart1.getdFullPrice(), 0.001);
        assertEquals(endPrice, myCart2.getdFullPrice(), 0.001);
    }

    @Test
    public void testNewItem() throws Exception {
        String barcode = "12345678901234";
        String articleName = "Apfel";
        double price = 0.99;
        int amount = 1;
        boolean isFood = true;

        cashRegisterSystem CRS = new cashRegisterSystem();

        CRS.newItem(barcode.toCharArray(), articleName, price, amount, isFood);
        CRS.inventory();
        //kann nur manuel überprüft werden da die Inventory Liste private ist
    }

    @Test
    public void testRemoveItem() throws Exception {
        String barcode = "12345678901234";
        String articleName = "Apfel";
        double price = 0.99;
        int amount = 1;
        boolean isFood = true;

        cashRegisterSystem CRS = new cashRegisterSystem();

        CRS.newItem(barcode.toCharArray(), articleName, price, amount, isFood);
        CRS.inventory();

        CRS.removeItem(barcode.toCharArray());
        CRS.inventory();
        //kann nur manuel überprüft werden da die Inventory Liste private ist
    }

    @Test
    public void testDisplayArticle() throws Exception {
        String barcode = "12345678901234";
        String articleName = "Apfel";
        double price = 0.99;
        int amount = 1;
        boolean isFood = true;

        cashRegisterSystem CRS = new cashRegisterSystem();
        cart myCart = new cart();

        CRS.newItem(barcode.toCharArray(), articleName, price, amount, isFood);
        CRS.displayArticle(barcode.toCharArray(), myCart);

        //kann nur manuel überprüft werden
    }

    @Test
    public void testInventory() throws Exception {

    }

    @Test
    public void testUpdate() throws Exception {

    }

    @Test
    public void testStatistic() throws Exception {

    }

    @Test
    public void testOutputInventory() throws Exception {

    }

    @Test
    public void testWrongArticle() throws Exception {

    }

}