# StockMarketGame
A simple stock market game

# Instructions
1. In webserver folder play tomcatserver class. 

2. In socketWork floder play gameService class.

3. Turn on Putty. Put localhost as host name. Use Raw server connection type. Put 8888 as port number. Now you can click open and the game will start open. 

# User interface 

The game involves five stocks: Apple, BP, Cisco, Dell and Ericsson

For each of the stocks, there is a deck with six influence cards which affect the share price of that stock.

The six different cards are: -20, -10, -5, +5, +10, +20. The number indicates the effect of that card on the stock price.

For example, the card "-5" in the Apple deck will decrease the Apple share price by 5, while the card "+10" in the Cisco deck will increase the Cisco share price by 10.
At the start of the game:

All stocks have an initial share price of £100

Each player is given £500

Each player is issued with 10 random shares.

The five decks with influence cards are shuffled. The top-most card of each deck is visible to all the players.

As the first player you will be given host rights. You can select how many players will be in the game (number 1-4) and should other slots be filled with bots(true or false). There is a maximum amount of 4 players per game (bots included). Trying to connect more players can lead to undefined situations. For a submitted version where always has to be one player. All players has to be connected before activating or deactivating bots (true or false) else the game will start and late connection will lead to error. If you select that you want to play with bots when where will be created as many bots as there is left until 4(if you have 4 players selected then you will have 0 bots). 
Game will have 5 rounds. On each round you can do two transaction operations and two voting. 
The input for transaction looks like: 
 
 ###### buy dell 2
 
 ###### sell dell 2
 
You always have to write operation word correctly but can make mistake on company name(that is important that first letter would be alright). If you ask to buy or sell too much stock operation will be declined.  
The voting looks like: 
 
 ###### vote apple yes
 
Again, you always have to write correct word at start but if you mistake company name program will still understand it. If you write yes then the card at top will be executed (if card has more yes votes) and its value will be added to stock price if you write any other word it will consider that you say no and will destroy the upper card but will not add its value. 
After two votes you will finish the turn and next turn will start. You can directly finish your turn by writing finished: 
 
 ###### finished
After the last round you will be show all the players results and who is the winner. By tapping quit you can close putty window and finish the game. 

###### quit
