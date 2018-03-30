package socketWork;


import GameLogic.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Kazimieras Griskaitis, 1503377 on 11/28/2017.
 */
public class GameServer {
    public static final int PORT = 8888;

    /* starts game server and waits player to connect*/
    public static void main(String[] args) throws IOException {
        Game game = new Game();
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("Started GameServer at port " + PORT);
        System.out.println("Waiting for Players to connect...");

        while (true) {
            Socket socket = server.accept();
            System.out.println("Player connected.");
            GameService service = new GameService(game, socket);
            new Thread(service).start();
        }
    }
}
