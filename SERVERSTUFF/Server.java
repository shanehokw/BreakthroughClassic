import java.awt.BorderLayout;
import java.awt.Font;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 *	Manages how users/clients connect to the server.
 *  Accepts data from clients and sends it to the appropriate methods and objects.
 *  Manages a player game queue, and creates new games when appropriate. 
 */
public class Server extends JFrame{

	private ServerSocket serverSocket;
	private ConcurrentHashMap<Player, ConnectedClient> allClients = new ConcurrentHashMap<Player, ConnectedClient>(10);
	private HashSet<String> takenUsernames = new HashSet<String>(10);
	private LinkedBlockingQueue<ConnectedClient> waitingQueue = new LinkedBlockingQueue<ConnectedClient>();
	JTextArea textArea = new JTextArea(20,40);

	public Server() {
		
		makeGUI();
		
		// Test stuff
		
		/*
		Player p1 = new Player(1);
		Player p2 = new Player(2);
		Game g1 = new Game(p1, p2);
		
		System.out.println(g1.getGameState());
		
		
		// simulate move
		g1.evaluateMove(p1, new Coordinate(1,0), new Coordinate(2,0));
		
		System.out.println(g1.getGameState());
		*/
		
		try {
			
			serverSocket = new ServerSocket(16238);
			Socket socket;

			while(true) {
				socket = serverSocket.accept();
				ConnectedClient clientThread = new ConnectedClient(socket);
				System.out.println("Somebody is connecting");
				clientThread.start();		
			}
		}
		catch (BindException be) {
			JOptionPane.showMessageDialog(null, "The port specified (16238) is already in use. Please free up the port and try again.");
			System.exit(0);
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void makeGUI() {
		
		textArea.setEditable(false);

		/*
		for (String name : clientNames) {
			textArea.append(name + "\n");
		}
		*/
		
		JLabel online = new JLabel("Online:");
		online.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		add(online, BorderLayout.NORTH);
		add(textArea, BorderLayout.CENTER);

		setVisible(true);
		pack();
		setLocation(1300, 350);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	class ConnectedClient extends Thread implements Observer {
		
		private Player player = null;
		Socket socket;
		DataOutputStream out;
		DataInputStream in;
		public static final int INITIAL_TIMEOUT = 20;
		private boolean keepAlive = true;
		// My heartbeat has the same timeout period (10 seconds),
		// but is non-blocking and sends every 2.5 seconds
		private Heartbeat heartbeat = new Heartbeat(4, 2500);
	

		public ConnectedClient(Socket socket) {
			this.socket = socket;
			player = new Player();
			
			// Add this thread to the list of threads
			allClients.put(player, this);
		}

		public void run() {
			try {
				out = new DataOutputStream(socket.getOutputStream());
				in = new DataInputStream(socket.getInputStream());

				// Validate the username. Abort connection if it isn't
				if(validateUsername(in.readUTF())) {
					System.out.println("Username is valid");
					new Packet(0).send(getOut()); // Send valid packet to client
					sendListOfPlayers();
				}
				else {
					System.out.println("Username is invalid");
					new Packet(-1).send(getOut()); // Send error packet to client
				}
				
				heartbeat.addObserver(this);
				
				while(keepAlive) {

					// Check if there's data to be read
					if(in.available() > 0) {
						int packetType = in.read();

						// Do the action indicated by this packet type
						switch(packetType) {
						case 0: System.out.println("Heartbeat received"); break;
						case 1: sendLobbyChat(in.readUTF()); break;
						case 2: sendGameChat(in.readUTF()); break;
						case 3: joinGameQueue(); break;
						case 4: System.out.println("Packet 4 received");
								receiveMove(in.readInt(), in.readInt(), in.readInt(), in.readInt()); break;
						case 5: disconnect(); break;
						}

						heartbeat.resetTimeout(); // Reset the heartbeat timeout
					}
					else {
						Thread.sleep(20); // Save a little CPU time (but not nearly as bad as the half second lag with the original heartbeat)
					}
				}
			}
			catch(IOException e) {
				// Not able to write; disconnect
				disconnect();
			}
			catch(InterruptedException e) {
				// Shouldn't happen
			}
		}

		@Override
		/**
		 * Used by to notify the thread about the connection heartbeat
		 */
		public void update(Observable arg0, Object arg1)
		{
			if( arg0 instanceof Heartbeat) {
				Heartbeat hb = (Heartbeat) arg0;
				int timeOut = (Integer) arg1;
				
				// Disconnect if the heartbeat is dead
				if(timeOut == 0 || hb.isDead()) {
					// The heartbeat has expired, remove the user and disconnect
					System.out.println("Disconnecting due to timeout");
					disconnect();
				}
				// Otherwise, send a heartbeat packet to the client
				else {
					try
					{
						System.out.println("Sending a heartbeat to the client (last value was " + timeOut + ")");
						// Try to send the heartbeat packet
						new Packet(0).send(getOut());
					}
					catch (IOException e)
					{
						// If we can't send a heartbeat, they are disconnected
						System.out.println("Disconnecting user, connection is broken");
						disconnect();
					}
				}
			}
		}
		
		public synchronized Player getPlayer() {
			return player;
		}
		
		public synchronized DataOutputStream getOut() {
			return out;
		}
		
		private boolean validateUsername(String username) {
			if(username.length() > 0) {
				
				// Make sure that the username is not already taken
				synchronized(takenUsernames){
					if(takenUsernames.contains(username)) {
						textArea.append("New Player Failed to Join  (" + username + " was taken)\n"); // Print to server log
						return false;
					}
					else {
						takenUsernames.add(username);
					}
				}
				
				// Name is > 0 and is not already taken
				player.userName = username;
				synchronized(textArea) {
					textArea.append("\nNew Player " + username + " joined the server successfully!" +
							" (There are " + allClients.size() + "users connected.)"); // Print to server log
				}
				return true;
			}
			textArea.append("New Player Failed to Join  (" + username + " was empty)\n"); // Print to server log
			return false;
		}

		private void disconnect() {
			
			// Tell the client thread it can die
			keepAlive = false;
			
			// Remove the client thread from the thread list
			allClients.remove(this);
			
			// Remove the players username from the taken usernames list
			synchronized(takenUsernames){
				takenUsernames.remove(this.getPlayer().userName);
			}
			
			// Remove the heartbeat (so it can die in peace)
			heartbeat.deleteObservers();
			
			// Update every connect clients' player list
			try {
				sendListOfPlayers();
			}
			catch (IOException e){
				// The heartbeat will catch any weird disconnects.
				// Don't need to worry about it here
			}
			
			// Close the IO stuff
			try {
				in.close();
				out.close();
				socket.close();
			}
			catch(Exception e) {
				
			}
		}
		
		private synchronized void sendLobbyChat(String message) throws IOException {
			
			synchronized(textArea) {
				textArea.append("\nLobby Chat: Player " + player.userName + " says: " + message);
			}
			
			// Send lobby message to every client
			for (ConnectedClient client : allClients.values()) {
				
				// Assemble the packet properties
				ArrayList<Object> packetProperties = new ArrayList<Object>(); // ArrayList of packet items
				packetProperties.add(player.userName);
				packetProperties.add(message);
							
				new Packet(1, packetProperties).send(client.getOut()); // Create and send the packet
			}
		}
		
		// Send message to the two players in the current game
		private synchronized void sendGameChat(String message) throws IOException {
			
			synchronized(textArea) {
				textArea.append("\nGame Chat: Player " + player.userName + " says: " + message);
			}
			
			Player opponent;
			ConnectedClient opponentServerThread = null;
			
			synchronized(player) {
				// Get the other player
				opponent = player.getGame().getOpponentPlayer(player);
				
				// Get the server thread of the opponent player
				opponentServerThread = allClients.get(opponent);	
			}
			
			// Send the message to just the current player and the opponent player in the current game
			ArrayList<Object> packetProperties = new ArrayList<Object>(); // ArrayList of packet items
			packetProperties.add(player.userName);
			packetProperties.add(message);
			
			DataOutputStream[] recipients = {out, opponentServerThread.getOut()};
			new Packet(2, packetProperties).send(recipients); // Create and send the packet to both players
		}
		
		// Send a list of all the players to each player connected, anytime somebody connects or disconnects
		public void sendListOfPlayers() throws IOException {

			synchronized(allClients) {
				for(ConnectedClient client : allClients.values()) {
					client.out.writeInt(7);
					client.out.writeInt(allClients.size());
					for(Player curPlayer : allClients.keySet()) {
						client.out.writeUTF(curPlayer.userName);
					}
					System.out.println(client.getPlayer().userName);
				}
			}
		}
		
		private void joinGameQueue() throws IOException {
			synchronized(waitingQueue) {
				try
				{
					synchronized(textArea) {
						textArea.append("\nPlayer " + player.userName + " has joined a queue");
					}
					
					waitingQueue.put(this);

					// If there are 2 people or more in the queue, create a new Game with first 2 players
					if(waitingQueue.size() >= 2) {
					
						Game newGame = new Game(waitingQueue.poll().getPlayer(), waitingQueue.poll().getPlayer());
						
						// Get the Client threads for both players
						ConnectedClient player1Thread = allClients.get(newGame.getPlayer(1));
						ConnectedClient player2Thread = allClients.get(newGame.getPlayer(2));
						ConnectedClient[] newGamePlayerThreads = {player1Thread, player2Thread};
						
						synchronized(textArea) {
							textArea.append("\nPlayers " + player1Thread.player.userName + " and " +
									player2Thread.player.userName + " have joined a new game");
						}
						
						// Send game-start packets to both players in the new game
						for(ConnectedClient client: newGamePlayerThreads) {

							// Assemble packet (signals to player they are in a game now)
							ArrayList<Object> packetProperties = new ArrayList<Object>(); // ArrayList of packet items
							packetProperties.add(client.getPlayer().getPlayerNum());
							packetProperties.add(newGame.getPlayer(1).userName);
							packetProperties.add(newGame.getPlayer(2).userName);
							new Packet(3, packetProperties).send(client.getOut()); // Create and send the packet to original player

							// Send the initial board state and pieces packet
							packetProperties = new ArrayList<Object>(); // ArrayList of packet items
							packetProperties.add(1);
							packetProperties.add(newGame.getGameState());
							new Packet(4, packetProperties).send(client.getOut()); // Create and send the packet to original player
						}		
					}
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		//5 = error 4 = update
		// Receive a potential move from the client and evaluate its validity. 
		private void receiveMove(int currentX, int currentY, int destinationX, int destinationY) throws IOException {
			
			System.out.println("Move Received");
			
			System.out.println("INCOMING START X: " + currentX);
			System.out.println("INCOMING START Y: " + currentY);
			System.out.println("INCOMING END X: " + destinationX);
			System.out.println("INCOMING END Y: " + destinationY);
			
			synchronized(player) {
				Coordinate currentCoord = new Coordinate(currentX, currentY);
				Coordinate destinationCoord = new Coordinate(destinationX, destinationY);
				Game curGame = player.getGame();
				
				// If the move is valid, update the board state for both players
				if(curGame.evaluateMove(player, currentCoord, destinationCoord)) {
					
					// Send the updated state of the board to both players
					ConnectedClient opponentServerThread = allClients.get(curGame.getOpponentPlayer(player));
					
					System.out.println("Current player username is: " + player.userName);
					System.out.println("Opponent player username is: " + opponentServerThread.player.userName);
					
					DataOutputStream[] recipients = {getOut(), opponentServerThread.getOut()};
					
					// Assemble packet
					ArrayList<Object> packetProperties = new ArrayList<Object>(); // ArrayList of packet items
					packetProperties.add(curGame.getCurrentPlayer().getPlayerNum());
					packetProperties.add(curGame.getGameState());
					new Packet(4, packetProperties).send(recipients); // Create and send the packet to original player
					
					
					// Now check if the game was won / is over
					if(curGame.gameOver) {
						// Notify the players the game is over
						new Packet(8, "The game is over! Player " + 
								curGame.winner.getPlayerNum() + " has won!").send(recipients);
						
						synchronized(textArea) {
							textArea.append("\n" + curGame.winner.userName + " has won their game against " + 
									curGame.getOpponentPlayer(curGame.winner).userName + "!");
						}
						
						// Clear the game stuff from both players
						for(Player curPlayer : curGame.getPlayers()) {
							curPlayer.clearInGame();
						}
					}
					
				}
				// If the move is not valid, send an error message to the current player
				else {
					new Packet(5, curGame.getLastError()).send(getOut()); // Create and send the packet to original player
				}
			}
		}
		
	}//End of ConnectedClient class
	
	public static void main(String [] args) {
		new Server();
	}
	
}//End of Server class