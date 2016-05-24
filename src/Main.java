import cashRegisterSystem.*;
import com.sqlconnector.ConnectionConfiguration;
import sun.nio.ch.Net;
import java.util.*;
import java.sql.*;


public class Main {

    public static void main(String[] args) {

        //menue
        cashRegisterSystem crsREWE=new cashRegisterSystem();

        cart caTempCustomer = null;
        Scanner sMenue = new Scanner(System.in);
        Scanner sArticle = new Scanner(System.in);
        int iMenue=-1;
        long iBarcode;
        char[] cBarcode=new char[13];
        int iAmount;
        String sName;
        double dPrice;
        boolean bIsFood;

        while (iMenue!=9) {
            System.out.println("Cash Register System - II-FH-ToDaKa");
            System.out.println("---------------------------------");
            if (caTempCustomer == null) {
                System.out.println("1 neuer Kunde");
                System.out.println("3 Artikel hinzufügen");
                System.out.println("4 Artikel löschen");
                System.out.println("5 Inventar anzeigen");
                System.out.println("6 Statisitk");
                System.out.println("7 Update");
                System.out.println("8 Speichern");
                System.out.println("9 Program verlassen");
                System.out.println("10 datenbank");


            } else {
                System.out.println("1 Artikel zum Einkaufswagen hinzufügen");
                System.out.println("2 Artikel Preis reduzieren");
                System.out.println("3 Rabatt auf alles");
                System.out.println("4 Artikel stonieren");
                System.out.println("0 Checkout");


            }


            iMenue=Integer.parseInt(sMenue.next());

            if(caTempCustomer==null)
            {
                if(iMenue==1)
                {
                    caTempCustomer=new cart();

                }
                else if(iMenue==3)
                {
                    System.out.println("Barcode:");
                    cBarcode=sArticle.next().toCharArray();
                    System.out.println("Name:");
                    sName=sArticle.next();
                    System.out.println("Preis:");
                    dPrice=Double.parseDouble(sArticle.next());
                    System.out.println("Anzahl:");
                    iAmount=Integer.parseInt(sArticle.next());
                    System.out.println("Lebensmittel, ja=1, nein=0");
                    bIsFood=Boolean.parseBoolean(sArticle.next());
                    crsREWE.newItem(cBarcode, sName, dPrice, iAmount, bIsFood);
                }
                else if(iMenue==4)
                {
                    System.out.println("Barcode:");
                    cBarcode=sArticle.next().toCharArray();
                    if(crsREWE.removeItem(cBarcode))
                    {
                        System.out.println(new String(cBarcode)+ " wurde aus dem Inventar entfernt");
                    }
                    else
                    {
                        crsREWE.wrongArticle(cBarcode);
                    }
                }
                else if(iMenue==5)
                {
                    crsREWE.outputInventory();
                }
                else if(iMenue==6)
                {
                    crsREWE.statistic();
                }
                else if(iMenue==7)
                {
                    crsREWE.update();
                }
                else if(iMenue==8)
                {
                    if(crsREWE.writeInventory())
                    {
                        System.out.println("Erfolgreich gespeichert");
                    }
                    else
                    {
                        System.out.println("Konnte nicht gespeichert werden");
                    }
                }
                else if(iMenue==9)
                {
                    System.out.println("Programm wird beendet...");
                }
                else if(iMenue==10)
                {
                }
                else
                {
                    System.out.println("Fehlerhafte eingabe");
                }
            }
            else {
                if(iMenue==1)
                {

                    System.out.println("Barcode:");
                    iBarcode=Long.parseLong(sArticle.next());
                    System.out.println("Menge:");
                    iAmount=Integer.parseInt(sArticle.next());

                    crsREWE.addArticle(iBarcode, iAmount, 1, 0);
                    //addtoprice
                    crsREWE.displayArticle(cBarcode,caTempCustomer);
                }
                else if(iMenue==2)
                {
                    System.out.println("Barcode:");
                    cBarcode=sArticle.next().toCharArray();
                    System.out.println("Neuer Preis:");
                    dPrice=Double.parseDouble(sArticle.next());
                    crsREWE.otherPrice(caTempCustomer,cBarcode,dPrice);
                }
                else if(iMenue==3)
                {
                    System.out.println("Rabatt, in Prozent:");
                    iAmount=Integer.parseInt(sArticle.next());
                    crsREWE.discount(caTempCustomer,iAmount);
                }
                else if(iMenue==0)
                {
                    crsREWE.displayAll(caTempCustomer);
                    caTempCustomer=null;
                }

                else
                {
                    System.out.println("Fehlerhafte eingabe");
                }
            }

        }
    }
}


