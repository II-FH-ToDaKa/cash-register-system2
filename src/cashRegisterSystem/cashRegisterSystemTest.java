package cashRegisterSystem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Tobias on 26.02.2016.
 */
public class cashRegisterSystemTest {
    cashRegisterSystem CRS = new cashRegisterSystem();
    cart myCart = new cart();

    String barcode = "12345678901234";
    String articleName = "Apfel";
    double price = 0.99;
    int amountInventory = 4;
    boolean isFood = true;


    @Before
    public void setUp() throws Exception {

        CRS.newItem(barcode.toCharArray(), articleName, price, amountInventory, isFood);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testAddToPrice() throws Exception {
        double endPrice = 2.1186; //price*amountBuy*myCart.TAX_FOOD;

        int amountBuy = 2;

        CRS.addToPrice(myCart ,barcode.toCharArray(), amountBuy);

        assertEquals(endPrice, myCart.getdFullPrice(), 0.001);
    }

    @Test
    public void testRemoveFromPrice() throws Exception {
        double endPrice = 1.0593;//price * (amountBuy - amountRemove) * myCart.TAX_FOOD;

        int amountBuy = 2;
        int amountRemove = 1;

        CRS.addToPrice(myCart, barcode.toCharArray(), amountBuy);
        CRS.removeFromPrice(myCart, barcode.toCharArray(), amountRemove);

        assertEquals(endPrice, myCart.getdFullPrice(), 0.001);
    }

    @Test
    public void testStatistic() throws Exception {

    }
}