/**
 * Represents a single Player connected to the server.
 * Tracks their name and whether or not they're currently playing a game.
 */

public class Player
{
	private boolean inGame = false;
	private Game game = null;
	private int playerNum;
	public String userName;
	
	public Player() {
		
	}
	
	public Player(int playerNum) {
		this.playerNum = playerNum;
		this.userName = "";
	}
	
	public boolean isInGame() {
		return inGame;
	}
	
	public Game getGame() {
		return game;
	}
	
	public int getPlayerNum() {
		return playerNum;
	}
	
	// Have this player join a game
	public void joinGame(Game newGame, int playerNum) {
		game = newGame;
		this.playerNum = playerNum;
		setInGame(true);
	}
	
	// Removes this player from any games they may be in
	public void clearInGame() {
		game = null;
		inGame = false;
	}
	
	public void setInGame(boolean value) {
		inGame = value;
	}
}
