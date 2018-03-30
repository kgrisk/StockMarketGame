package socketWork;

import GameLogic.*;
import com.google.gson.Gson;
import org.glassfish.jersey.client.ClientConfig;
import webserver.GsonMessageBodyHandler;
import webserver.TomcatServer;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Kazimieras Griskaitis, 1503377 on 11/28/2017.
 */
public class GameService implements Runnable {
    /*steams for input and output while using serve*/
    private Scanner in;
    private PrintWriter out;

    private Game game;

    /*played id,
    amount of connected players including bots,
    how many players will be in game
     */
    private int id = 0;
    private static int connectedPlayers = 0;
    private static int playerNumber = 0;

    /*boolean for if bots is on,
    boolean then to start the game,
    boolean then to close the game
     */
    private static boolean botsOn = false;
    private static AtomicBoolean startGame = new AtomicBoolean(false);
    private static AtomicBoolean gameFinished = new AtomicBoolean(false);

    private int[][] playerShares;
    public WebTarget webTarget;

    /*
    constructor for new player it initialized the basic variables for it
    like input autput streams, his id and his connection to game mechanics through game object
     */
    public GameService(Game game, Socket socket){
        id = connectedPlayers;
        connectedPlayers++;
        this.game = game;
        try {
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
    constructor used by bot which connects bot to tomcat using webtarget
     */
    public GameService(Game game){
        id = connectedPlayers;
        connectedPlayers++;
        this.game = game;
        this.game.players[id].bot = true;

        ClientConfig config = new ClientConfig(GsonMessageBodyHandler.class);
        webTarget = ClientBuilder.newClient(config).target("http://localhost:8080/examples/rest/BotClasses");

    }
    /*
    Method there all players decisions takes place.
    at the start there is block of code dedicated to the first connected person
    which gives him right to adjust parameters of the game.
    On  the lines 107 - 131 used decided if he wants bots and if yes new instances of this class is created on new threads
    for bots usages.
    afterwards the game play starts.
     */
    @Override
    public void run() {
        if(!game.players[id].bot) {
            out.println("Welcome to Stock Market simulator!!");
            out.println("you are " + (id + 1) + "player!!");
        }
        boolean proceed = false;
        if(id == 0){
            while(!proceed){
                out.println("how many players do you want to be in game ?(1-4)");
                String text = in.nextLine();
                try{
                    playerNumber = Integer.parseInt(text);
                    if(playerNumber < 1 || playerNumber > 4){
                        out.println("wrong input try again!!");
                        continue;
                    }
                    proceed = true;
                } catch (Exception e) {
                    out.println("wrong input try again!!");
                }
            }proceed = false;
            while(!proceed){
                if(playerNumber - connectedPlayers == 0 && playerNumber == 4){
                    startGame.set(true);
                    break;
                }
                out.println("there is " + (playerNumber - connectedPlayers)+" unconnected players!");
                out.println("and " + (4 - playerNumber)+" left slots(until 4 players)!");
                out.println("don't forget to connect all players before proceeding");
                out.println("should Bots play in left slots?  (true false)");
                String text = in.nextLine();
                try{
                    botsOn = Boolean.parseBoolean(text);
                    out.println(botsOn);
                    if(botsOn){
                        for(int a = playerNumber;a <4;a++){
                            GameService service = new GameService(game);
                            new Thread(service).start();
                        }
                    }
                    proceed = true;
                    startGame.set(true);

                } catch (Exception e) {
                    out.println("wrong input try again!!");
                }
            }
        }
        if(!game.players[id].bot)
            out.println("Waiting for other players......");

        synchronized (this){
        while(!startGame.get()) {
            try {
                wait(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }}
        if(!game.players[id].bot)
            out.println("Game Started !!!");
        playerShares = new int[connectedPlayers][5];
        for(int a = 0; a < 5;a++) {
            if(!game.players[id].bot){
                Player.currentTurn = a;
                game.executeVotes();
                Player.ResetValues(game.players, connectedPlayers);
                out.println("turn " + (a+1));
                out.println("Stock prices:" + Arrays.toString(game.stockPrice));
                out.print("visible cards:");
                for(Card card : game.getCards())
                    out.print(card.toString());
                out.println();
                for(int i = 0; i < connectedPlayers; i++){
                    out.println("Player "+ (i+1) + Arrays.toString(game.getShares(i)));
                    out.println("cash " + game.getCash(i));

                }
            }
            for(int i = 0; i < connectedPlayers; i++)
                playerShares[i] = game.getShares(i);
            while (!Player.TurnsFinished(game.players, a, connectedPlayers)) {

                    while (!game.players[id].turnFinished[a]) {
                        try {
                            if(!game.players[id].bot) {
                                //sends the readed line to request which reads input and returns parameters for execution
                                Request request = Request.parse(in.nextLine());
                                String response = execute(game, request);
                                // note use of \r\n for CRLF
                                out.println(response + "\r\n");
                            }else{
                                //Wrapper wrapper = new Wrapper(id,game.stockPrice,game.getCards(),playerShares, game.getCash(id));
                                String nr = "";
                                for (int i =0;i < 5;i++){

                                    nr = nr + game.getCards()[i].effect;
                                }
                                /*
                                sends the request to webtarget and gets the answer which is string simulating user input
                                 */
                                String stringg = webTarget.request().post(Entity.entity(new Wrapper(id,game.stockPrice,
                                        game.getCards(),playerShares, game.getCash(id)), MediaType.APPLICATION_JSON))
                                        .readEntity(String.class);
                                String[] splited = stringg.split("[\n]");

                                for(String line : splited) {
                                    Request request = Request.parse(line);
                                    String response = execute(game, request);
                                }
                            }

                        } catch (NoSuchElementException e) {
                            if(!game.players[id].bot)
                                out.println("wrong operation try again");
                        }
                    }
            }
        }
        if(!game.players[id].bot){
            // sells all shares
            out.println(game.SellAll(connectedPlayers));

        }
        for(int i = 0; i < connectedPlayers; i++){
            out.println("Other results:");
            out.println("Player "+ (i+1) + " cash " + game.getCash(i));


        }

        while (!gameFinished.get()){
            if(!game.players[id].bot) {
                Request request = Request.parse(in.nextLine());
                String response = execute(game, request);
            }
        }


    }
    /*
    executes the got request on game using given parameters
     */
    public synchronized String execute(Game game, Request request) {
        try {
            switch (request.type) {
                case BUY:
                    return game.buy(id, Stock.parse(request.params[0]),Integer.parseInt(request.params[1]));
                case SELL:
                    return game.sell(id,Stock.parse(request.params[0]),Integer.parseInt(request.params[1]));
                case VOTE:
                    return game.vote( id, Stock.parse(request.params[0]), request.params[1].charAt(0));
                case FINISHED:

                    game.players[id].turnFinished[Player.currentTurn] = true;
                    return "Finished turn";
                case QUIT:
                    in.close();
                    out.close();
                    gameFinished.set(true);
                default:
            }
        } catch (Exception e) {
            out.println("wrong input try again!");
            out.println("input format: command company amount(or yes no), example:");
            out.println("buy dell 2");
        }

        return "";
    }
}
