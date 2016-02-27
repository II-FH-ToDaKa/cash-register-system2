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

    String sBarcode = "12345678901234";
    String sArticleName = "Apfel";
    double dPrice = 0.99;
    int iAmountInventory = 4;
    boolean isFood = true;


    @Before
    public void setUp() throws Exception {

        CRS.update();
    }

    @After
    public void tearDown() throws Exception {

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

        CRS.addArticle(sBarcode.toCharArray(), iAmountBuy, myCart);
        CRS.delArticle(sBarcode.toCharArray(), iAmountRemove, myCart);

        assertEquals(dEndEarning, CRS.statistic(), 0.001);

    }
}