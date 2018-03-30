package GameLogic;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by Kazimieras Griskaitis, 1503377 on 11/29/2017.
 *
 * Wrapper class used in wrapping information and putting it to json to send to method on tomcat server.
 */
public class Wrapper {
    public int id;
    public int[] stockPrice;
    public int[] cards = new int[5];
    public int[][] shares;
    public int cash;

    public Wrapper(int id, int[] stockPrice, Card[] cards, int[][] shares, int cash) {
        this.id = id;
        this.shares = new int [shares.length][shares[0].length];
        this.stockPrice = stockPrice.clone();
        for(int i = 0; i < 5; i ++){
            this.cards[i] = cards[i].effect;
        }

        for (int i = 0; i < shares.length;i++)
            for (int a = 0; a < shares.length;a++){
                this.shares[i][a] = shares[i][a];

        }

        this.cash = cash;
    }

    public Wrapper()
    {

    }
}
