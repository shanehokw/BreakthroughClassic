import java.net.Socket;

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
	
	public void setInGame(boolean value) {
		inGame = value;
	}
}
