package GameLogic;

import java.util.Random;
/*
class with all game mechanics
 */
public class Game {
	public static Deck[] decks = new Deck[5];
	public static int[] stockPrice = new int[]{100,100,100,100,100};
	public Player [] players;
	public static int[][] votes = new int[2][5];
	// skeleton class with dummy constructors and methods 

	// create a random game 
	public Game() {
		int i = 0;
		players = new Player[4];
		for(Stock t : Stock.values()){
			decks[i] = new Deck(t);
			i++;
		}
		for(int a = 0; a < 4;a++){
			players[a] = new Player(CountRandomShares());
		}

	}

	public int[] CountRandomShares(){
		Random rand = new Random();
		int[] companies = new int[5];
		int sum = 10;
		while (sum > 0)
		{
			companies[rand.nextInt(5)]++;
			sum--;
		}
		return companies;
	}

	// create a game with specific initial decks and share holdings
	// used for unit testing 
	public Game(Deck[] decks, int[][] shares) {
		this.decks = decks;
		players = new Player[shares.length];
		for(int i = 0; i< shares.length;i++){
			players[i] = new Player(shares[i]);
		}
	}

	public int getCash(int playerId) {
		return players[playerId].cash;
	}

	public int[] getShares(int playerId) {
		return players[playerId].shares;
	}

	public int[] getPrices() {
		return stockPrice;
	}
	//returns five upper cards
	public Card[] getCards() {
		Card[] cards = new Card[5];
		for(int i =0;i<5;i++){

				cards[i] = decks[i].cards.get(0);
		}
		return cards;
	}
	// executes all the votes after turn
	public synchronized void executeVotes() {
		for(int i =0;i<5;i++){
			if(votes[0][i] > votes[1][i]){
				stockPrice[i] = stockPrice[i] + decks[i].cards.get(0).effect;
				System.out.println(stockPrice[i]);
				decks[i].cards.remove(0);
			}else if(votes[0][i] < votes[1][i])
				decks[i].cards.remove(0);
		}
		votes = new int[2][5];
	}
	//buy operation has restriction to buy just amount that player is capable to buy and can be called just twice per turn
	public synchronized String buy(int id, Stock s, int amount) {
		int nr = s.ordinal();
		if(players[id].cash - (amount *  stockPrice[nr] + amount * 3) >= 0 && players[id].operationsDone < 2){
			players[id].operationsDone++;
			players[id].shares[nr] = players[id].shares[nr] + amount;
			players[id].cash = players[id].cash - (amount *  stockPrice[nr] + amount * 3);
			return "brought";
		}
		return "player doesn't have enough shares or already done 2 operations";
	}
	//buy operation has restriction to sell just amount that player is capable to sell and can be called just twice per turn
	public synchronized String sell(int id, Stock s, int amount) {
		int nr = s.ordinal();
		if(players[id].shares[nr] - amount >= 0 && players[id].operationsDone < 2){
			players[id].operationsDone++;
			players[id].shares[nr] = players[id].shares[nr] - amount;
			players[id].cash = players[id].cash + amount *  stockPrice[nr];
			return "sold";
		}
		return "player doesnt have enough cash or already done 2 operations";
	}
	//method called after fife turns to sell all shares and show the winner
	public synchronized String SellAll( int playersCount) {
		int mostCash = 0;
		int playerId = 0;
		for(int i = 0;i < playersCount; i ++){
			for(int a = 0;a < 5; a ++){
				players[i].cash = players[i].cash + players[i].shares[a] *  stockPrice[a];
				players[i].shares[a] = 0;
				if(players[i].cash >= mostCash){
					mostCash = players[i].cash;
					playerId = i;
				}
			}
		}
		return "Winner is Player " + (playerId + 1) + " with amount of " + players[playerId].cash + " cash";
	}
	// method for adding votes. can be called just twice per turn and on different stocks
	public synchronized String vote(int id, Stock s, char yes) {
		if(players[id].votedCard == null)
			players[id].votedCard = s;
		else if(players[id].votedCard == s)
			return "you already voted on this stock";
		players[id].votesDone++;
		int nr = s.ordinal();
		if(Character.toUpperCase(yes) == 'Y')
			votes[0][nr]++;
		else
			votes[1][nr]++;

		if(players[id].votesDone == 2){
			players[id].turnFinished[Player.currentTurn] = true;
			return "your turn is finished";
		}
		return "okay";
	}
}
