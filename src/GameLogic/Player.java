package GameLogic;

/**
 * Created by Kazimieras Griskaitis, 1503377 on 11/27/2017.
 */
public class Player {
    // variables containing players information
    public int[] shares;
    public int cash;
    public int operationsDone = 0;
    public int votesDone = 0;
    public boolean [] turnFinished = new boolean[]{false,false,false,false,false};
    public boolean bot = false;
    public  static int currentTurn = 0;

    public Stock votedCard = null;
    public Player (int[] shares){
        this.shares = shares;
        this.cash = 500;
    }
    //static method to check if all players finished their turn
    public synchronized static Boolean TurnsFinished(Player[] players, int turn, int playerCount){
        for(int i =0;i <playerCount;i++){
            if(!players[i].turnFinished[turn])
                return  false;
        }
        return true;
    }
    //reset player values
    public static void ResetValues(Player[] players, int playerCount){
        for(int i =0;i <playerCount;i++){
            players[i].votesDone =0;
            players[i].operationsDone = 0;
            players[i].votedCard = null;
        }
    }

}
