package BotClasses;

import GameLogic.Card;
import GameLogic.Stock;
import GameLogic.Wrapper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by zelda on 11/29/2017.
 */
@Path("/BotClasses")
public class Bot {
    public Bot(){

    }


    /*
    the class is put on tomcat server and is asked from gameervice run method on each turn
    its basically returns string of lines which imitates contains all bot commands to imitade users turn
    */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public synchronized String transfer( Wrapper wrapper) {

        int stockEffectBuy = -40;

        int stockNumberBuy = 0;

        int oldStock = 0;
        Stock[] stocks = Stock.values();
        String info = "";

        for (int i =0;i < 5;i++){
            if( wrapper.cash >= wrapper.stockPrice[i] && wrapper.cards[i] >= stockEffectBuy){
                stockEffectBuy = wrapper.cards[i];
                stockNumberBuy = i;

            }
        }
        info = "buy " + stocks[stockNumberBuy] +" "+ (int)(wrapper.cash/wrapper.stockPrice[stockNumberBuy]) + "\n";

        info = info + "vote " + stocks[stockNumberBuy] +" "+ " yes" + "\n";
        oldStock = stockNumberBuy;
        for (int i =0 ;i < 5;i++){
            if( oldStock != i && wrapper.shares[wrapper.id][i] > 0 &&  wrapper.cards[i] <stockEffectBuy ){
                stockEffectBuy = wrapper.cards[i];
                stockNumberBuy = i;
            }

        }
        if(oldStock != stockNumberBuy){
        info = info + " sell " + stocks[stockNumberBuy] +" "+ wrapper.shares[wrapper.id][stockNumberBuy] + "\n";

        info = info + " vote " + stocks[stockNumberBuy] +" "+ " yes";
        }else{
            info = info + "finished";
        }


        return info;
    }
}