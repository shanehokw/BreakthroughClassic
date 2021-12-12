import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Represents a single game instance of Breakthrough.
 * Tracks which players are currently playing the game,
 * whether it has been won yet or not, the game tiles and their owners,
 * and handles all the rule enforcement for moves.
 */
public class Game
{
	protected Player player1 = null;
	protected Player player2 = null;
	protected Player unownedPlayer = new Player(0);
	protected int player1Pieces = 0;
	protected int player2Pieces = 0;
	protected Player currentPlayer = null; // Player whose turn it is right now
	protected ConcurrentSkipListMap<Coordinate, GameTile> gameTiles = new ConcurrentSkipListMap<Coordinate, GameTile>();
	protected String lastErrorMessage = null; // Holds the last error message encountered (if any)
	boolean gameOver = false;
	Player winner = null;
	
	public Game(Player player1, Player player2) {
		
		this.player1 = player1;
		this.player2 = player2;
		
		// Add players to game
		player1.joinGame(this, 1);
		player2.joinGame(this, 2);
		
		// Configure unowned player
		unownedPlayer.joinGame(this, 0);
		
		// Set player 1 to be current player (randomize this later)
		currentPlayer = this.player1;
		
		// Create the game!
		//int tileIndex = 0;
		for (int y=0; y<8; y++) {
			for (int x=0; x<8; x++) {
				try{
				Coordinate newTileCoordinate = new Coordinate(x, y);
				GameTile newTile = new GameTile(newTileCoordinate);
				
				if(gameTiles.put(newTileCoordinate, newTile) == null) {
					
					System.out.println("It was null");
				}
			
				System.out.println(gameTiles.size());
				
				}
				catch(ClassCastException e) {
					e.printStackTrace();
				}
				
				//tileIndex++;
			}
		}
	}
	
	public synchronized Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	// Receive coordinates of a move and evaluate its validity
	public synchronized boolean evaluateMove(Player initiatingPlayer, Coordinate currentCoord, Coordinate destinationCoord) {
		
		System.out.println("Evaluate move started");
		
		if (isMoveValid(initiatingPlayer, currentCoord, destinationCoord)) {
			
			System.out.println("Move is valid!");
			
			System.out.println("Start " + currentCoord + " was owned by: " + gameTiles.get(currentCoord).player.userName);
			System.out.println("Destination " + destinationCoord + " was owned by: " + gameTiles.get(destinationCoord).player.userName);
			
			// Do the move!
			gameTiles.get(currentCoord).player = unownedPlayer;
			gameTiles.get(destinationCoord).player = initiatingPlayer;
			
			System.out.println("Start " + currentCoord + " is NOW owned by: " + gameTiles.get(currentCoord).player.userName);
			System.out.println("Destination " + destinationCoord + " is NOW owned by: " + gameTiles.get(destinationCoord).player.userName);
			
			// Check to see if anybody has won
			if(
				// If player 1 makes it to the rightmost side, they win
				(initiatingPlayer.equals(player1) && destinationCoord.getCoordinate('x') == 7) ||
				// If player 2 makes it to the leftmost side, they win
				(initiatingPlayer.equals(player2) && destinationCoord.getCoordinate('x') == 0)
			)
			{
				setWin(initiatingPlayer);
			}
			
			// If player 1 has no pieces left, player 2 wins
			if(player1Pieces == 0) {
				setWin(getPlayer(2));
			}
			
			// If player 2 has no pieces left, player 1 wins
			if(player2Pieces == 0) {
				setWin(getPlayer(1));
			}

			// Switch the player and advance the turn
			toggleCurrentPlayer();
			
			return true;
		}
		else {
			System.out.println("Move is NOT valid! :(");
			return false;
		}
	}
	
	// A player has won!
	private void setWin(Player winPlayer) {
		int winningPlayer = getPlayerNum(winPlayer);
		
		if (winningPlayer == 1) {
			winner = player1;
		}
		else if(winningPlayer == 2) {
			winner = player2;
		}
		
		gameOver = true;
		
		// Also delete the unowned player to avoid memory leaks
		unownedPlayer = null;
	}
	
	private void toggleCurrentPlayer() {
		if(currentPlayer.equals(player1)) {
			currentPlayer = player2;
		}
		else {
			currentPlayer = player1;
		}
	}
	
	// Serialize the game state to a string	
	public synchronized String getGameState() {
		
		StringBuffer output = new StringBuffer();
		for(GameTile tile: gameTiles.values()) {
			output.append(tile.player.getPlayerNum() + ",");
		}
		output.deleteCharAt(output.length()-1); // Delete the trailing comma
		
		System.out.println("\n\nSTART COORDINATES OF GAMESTATE TILES\n\n");
	
		for(GameTile tile: gameTiles.values()) {
			System.out.println(tile.coordinates);
		}
		
		// Print all the coordinate keys in oprder
		System.out.println("This should be all the coordinates in order");
		for(Coordinate c: gameTiles.keySet()) {
			System.out.println(c);
		}
		
		return output.toString();
	}

	public synchronized String getLastError() {
		String lastErr = lastErrorMessage;
		lastErrorMessage = null;
		return lastErr;
	}
	
	
	// Returns the Player that is NOT whatever player the method receives.
	public synchronized Player getOpponentPlayer(Player currentPlayer) {
		if(player1.equals(currentPlayer)) {
			return player2;
		}
		else {
			return player1;
		}
	}
	
	// Returns the integer number of a player, provided the Player object
	public synchronized int getPlayerNum(Player p) {
		if(p.equals(player1)) {
			return 1;
		}
		else if(p.equals(player2)) {
			return 2;
		}
		else if(p.equals(unownedPlayer)) {
			return 0;
		}
		else return -1;
	}
	
	public synchronized Player getPlayer(int playerNum) {
		if(playerNum == 1) {
			return player1;
		}
		else if(playerNum == 2) {
			return player2;
		}
		else if(playerNum == 0) {
			return unownedPlayer;
		}
		else {
			return null;
		}
	}
	
	// Return an array of the 2 Players in this game
	public synchronized Player[] getPlayers() {
		Player[] players = {player1, player2};
		return players;
	}
	
	/**
	 * Determines whether a proposed move for a certain player is valid. 
	 * @param initiatingPlayer
	 * @param currentCoord
	 * @param destinationCoord
	 * @return Returns TRUE if the move is valid. Returns FALSE otherwise.
	 */
	private synchronized boolean isMoveValid(Player initiatingPlayer, Coordinate currentCoord, Coordinate destinationCoord) {
		
		System.out.println("Evaluate move started");
		
		// Make sure this player actually owns the tile they tried to move
		Player currentTileOwner = gameTiles.get(currentCoord).player;
		if(!initiatingPlayer.equals(currentTileOwner)) {
			lastErrorMessage = "You do not own this tile.";
			return false;
		}
		
		// Player can only move a maximum distance of 1
		if(currentCoord.isTooFarAway(destinationCoord)) {
			lastErrorMessage = "You cannot move more than 1 space.";
			return false;
		}
		
		// Player 1 may only move right
		if(initiatingPlayer.equals(player1)) {
			if(!destinationCoord.isRightOf(currentCoord)) {
				lastErrorMessage = "You must move to the right.";
				return false;
			}
		}
		// Player 2 may only move left
		else if(initiatingPlayer.equals(player2)) {
			if(!destinationCoord.isLeftOf(currentCoord)) {
				lastErrorMessage = "You must move to the left.";
				return false;
			}
		}
		else {
			lastErrorMessage = "You aren't even a valid player- how did you even get here? Go away!";
			return false;
		}
		
		// Get the owning Player of the destination tile for the next rules
		Player destinationTileOwner = gameTiles.get(destinationCoord).player;
		
		// Player cannot move to a tile they already own
		if(destinationTileOwner.equals(initiatingPlayer)) {
			lastErrorMessage = "You already own this tile.";
			return false;
		}
		
		// !! THIS RULE MUST BE EVALUATED LAST! It affects the player piece count
		// If this move is an attack, it must be diagonal and adjacent
		if(destinationTileOwner.equals(getOpponentPlayer(initiatingPlayer))) { // This is an attack move
			if(!destinationCoord.isDiagonalAndAdjacentTo(currentCoord)) {
				lastErrorMessage = "Attacks must be diagonal.";
				return false;
			}
			else {
				if(destinationTileOwner.equals(player1)) {
					player1Pieces--;
				}
				else if(destinationTileOwner.equals(player2)) {
					player2Pieces--;
				}
			}
		}
		
		return true;
	}
	
	// Receive the serialized game state (comma delimited string) and update the game status
	public synchronized void updateGameState(String boardData) {
		
		String gameTiles[] = boardData.split(",");
		
		int tileValue = 0;
		int tileIndex = 0;
		for(int y=0; y<8; y++) {
			for (int x=0; x<8; x++) {
				tileValue = Integer.parseInt(gameTiles[tileIndex]);
				tileIndex++;
			}
		}
	}
	
	protected class GameTile
	{
		private Player player;
		private Coordinate coordinates;
		
		public GameTile(Coordinate coordinates) {
			
			System.out.println("A new coordinate is getting created: " + coordinates);
			this.coordinates = coordinates;
			
			// Assign a player to the tiles on the first and last 2 columns
			switch(coordinates.getCoordinate('x')) {
				case 0:
				case 1:
					this.player = player1; 
					player1Pieces++;
					break;
				case 6:
				case 7: 
					this.player = player2;
					player2Pieces++;
					break;
				default: this.player = unownedPlayer;
			}
			
			//System.out.println("New tile coordinates are: " + coordinates);
		}
	}
}
