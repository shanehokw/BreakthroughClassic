import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.net.*;
import java.io.*;
import java.applet.*;
import javax.sound.sampled.*;

public class Chat extends JFrame implements ActionListener{
	
	GameBoard game;
	Background bg;
	
	Calendar cal = Calendar.getInstance();
  	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	
	AudioClip bump, song;
	
	//Username GUI
	JFrame pokemonFrame;
	JButton ok;
	JComboBox pkmn;
	
	//Main Chat GUI
	JMenuBar menuBar;
	JMenu file, settings, gameWindow;
	JMenuItem queue, disconnect, connect, editIP, viewGame, clear;	
	JTextField sendText;	
	JButton send;	
	JPanel mainPanel;	
	JTextArea chatArea, users;
	JScrollPane chatScroll, usersScroll;
	JLabel userHeader;
		
	ArrayList<String> pokemon = new ArrayList<String>(Arrays.asList("Arcanine", "Articuno", 
	"Blastoise", "Charizard", "Cubone", "Diglett", "Ditto", "Dragonair", "Dragonite", 
	"Eevee", "Flareon", "Gengar", "Growlithe", "Gyarados", "Haunter", "Jolteon", "Lapras", 
	"Magikarp", "Magmar", "Mew", "Mewtwo", "Moltres", "Ninetales", "Onix", "Pidgeot", "Pikachu", 
	"Ponyta", "Rapid", "Scyther", "Seadra", "Seal", "Snorlax", "Starmie", "Vaporeon", "Venusaur", 
	"Vulpix", "Zapdos"));
	
	//Client thread variables
	Socket socket;
	DataInputStream in;
	DataOutputStream out;
	
	String player1, player2, username;
	String ipAddress = "localhost";
	
	int numPlayers, playerID, playerTurn;	
	static final int INITIAL_TIMEOUT = 20;
		
	public Chat() {		
		int random = 1 + (int) (Math.random() * 4);	
		bg = new Background("bg" + random);
		bg.setLayout(new BorderLayout());
		
		createMenu();
		createChatArea();
		createUserList();
		createSendText();
		createMainPanel();
		importMedia();
				
		setJMenuBar(menuBar);
		
		setTitle("Breakthrough Lobby");
		setVisible(true);
		setResizable(false);
		setLocation(25, 60);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(bg);
		setSize(450, 300);
	}
	
	public void importMedia() {
		try {
			bump = Applet.newAudioClip(getClass().getResource("sounds/bump.wav"));
			song = Applet.newAudioClip(getClass().getResource("sounds/song.wav"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setOpaque(false);
		SpringLayout layout = new SpringLayout();
		mainPanel.setLayout(layout);
		
		mainPanel.add(chatScroll);
		mainPanel.add(userHeader);
		mainPanel.add(usersScroll);
		mainPanel.add(sendText);
		mainPanel.add(send);
		
		//Chat area
		layout.putConstraint(SpringLayout.NORTH, chatScroll, 10, SpringLayout.NORTH, bg);
		layout.putConstraint(SpringLayout.WEST, chatScroll, 10, SpringLayout.WEST, bg);
		
		//Users list
		layout.putConstraint(SpringLayout.NORTH, usersScroll, 0, SpringLayout.SOUTH, userHeader);
		layout.putConstraint(SpringLayout.WEST, usersScroll, 15, SpringLayout.EAST, chatScroll);
		
		//Users label
		layout.putConstraint(SpringLayout.NORTH, userHeader, 5, SpringLayout.NORTH, bg);
		layout.putConstraint(SpringLayout.WEST, userHeader, 15, SpringLayout.EAST, chatScroll);
		
		//Send textfield
		layout.putConstraint(SpringLayout.NORTH, sendText, 15, SpringLayout.SOUTH, chatScroll);
		layout.putConstraint(SpringLayout.WEST, sendText, 20, SpringLayout.WEST, bg);
		
		//Send button
		layout.putConstraint(SpringLayout.NORTH, send, 10, SpringLayout.SOUTH, chatScroll);
		layout.putConstraint(SpringLayout.WEST, send, 10, SpringLayout.EAST, sendText);
		
		bg.add(mainPanel, BorderLayout.CENTER);
	}
	
	public void createChatArea() {
		chatArea = new JTextArea(11, 26);
		chatArea.setLineWrap(true);
		chatArea.setWrapStyleWord(true);
		chatArea.setEditable(false);
		chatArea.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		chatArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
				
		chatScroll = new JScrollPane(chatArea);
		
		chatArea.setOpaque(false);
		chatScroll.setOpaque(false);
		chatScroll.getViewport().setOpaque(false);
	}
	
	public void createSendText() {
		send = new JButton("Send");
		sendText = new JTextField(20);
		
		send.addActionListener(this);
		sendText.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
					sendPublicMessage(sendText.getText());		
				}
			}
		});
	}
	
	public void createUserList() {				
		users = new JTextArea(11, 7);
		users.setLineWrap(true);
		users.setWrapStyleWord(true);
		users.setEditable(false);
		users.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		
		usersScroll = new JScrollPane(users);
		
		userHeader = new JLabel("ONLINE:");
		userHeader.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		
		users.setOpaque(false);
		usersScroll.setOpaque(false);
		usersScroll.getViewport().setOpaque(false);
	}
	
	public void createMenu() {
		menuBar = new JMenuBar();
		menuBar.setOpaque(false);
		file = new JMenu("File");
		settings = new JMenu("Settings");
		gameWindow = new JMenu("Game");
		
		editIP = new JMenuItem("Edit IP Address");
		queue = new JMenuItem("Join Queue");
		disconnect = new JMenuItem("Disconnect");
		connect = new JMenuItem("Connect");
		viewGame = new JMenuItem("View Game"); viewGame.setEnabled(false);
		clear = new JMenuItem("Clear");
		
		clear.addActionListener(this);
		queue.addActionListener(this);
		disconnect.addActionListener(this);
		connect.addActionListener(this);
		editIP.addActionListener(this);
		viewGame.addActionListener(this);
						
		//Menu bar
		menuBar.add(file);
		menuBar.add(settings);
		menuBar.add(gameWindow);
		settings.add(editIP);
		file.add(connect);
		file.add(disconnect);
		file.add(queue);
		file.add(clear);
		gameWindow.add(viewGame);
	}
	
	public void generateName() {
	
		pkmn = new JComboBox();
				
		for (String name : pokemon) {
			pkmn.addItem(name);
		}
		pkmn.setEditable(false);
		
		pokemonFrame = new JFrame("Choose your Pokemon");
		JPanel bottom = new JPanel();
		
		ok = new JButton("OK");
		
		ok.addActionListener(this);
		
		bottom.add(ok);
		
		pokemonFrame.add(pkmn, BorderLayout.CENTER);
		pokemonFrame.add(bottom, BorderLayout.SOUTH);
				
		pokemonFrame.setLocationRelativeTo(this);
		pokemonFrame.setVisible(true);
		pokemonFrame.setSize(300, 100);
	}
	

	
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == connect) {
			generateName();
		}
		else if (ae.getSource() == disconnect) {
			try {
				disconnect();
			}
			catch (NullPointerException npe) {
				chatArea.append("You are not connected to a server.\n");
				chatArea.setCaretPosition(chatArea.getDocument().getLength());
			}
		}
		else if (ae.getSource() == ok) {
			username = (String) pkmn.getSelectedItem();
			pokemonFrame.setVisible(false);
			connect();
		}
		else if (ae.getSource() == editIP) {
			ipAddress = JOptionPane.showInputDialog(this, "IP Address: " , ipAddress);
		}
		else if (ae.getSource() == queue) {
			joinQueue();
		}
		else if (ae.getSource () == viewGame) {
			game.setVisible(true);
		}
		else if (ae.getSource() == send) {
			sendPublicMessage(sendText.getText());
		}
		else if (ae.getSource() == clear) {
			chatArea.setText("");
			sendText.requestFocusInWindow();
		}
	}//End of actionPerformed
	
	public void disconnect() {
		try {
			out.write(5);
			out.flush();
			in.close();
			out.close();
			socket.close();
			users.setText("");
			game.privateChat.append("Disconnected from server.\n");
			game.privateChat.setCaretPosition(game.privateChat.getDocument().getLength());
			connect.setEnabled(true);
			queue.setEnabled(true);
		}
		catch (IOException ioe) {
			users.setText("");
			chatArea.append("Disconnected from server.\n");
			chatArea.setCaretPosition(chatArea.getDocument().getLength());
			connect.setEnabled(true);
			queue.setEnabled(true);
		}
	}
		
	public void connect() {
		try {
			socket = new Socket(ipAddress, 16238);
			Client client = new Client();
			client.start();
			connect.setEnabled(false);
		}
		catch (UnknownHostException uhe) {
			chatArea.append("Cannot connect to server. Check your host or port number\n");
			chatArea.setCaretPosition(chatArea.getDocument().getLength());
			connect.setEnabled(true);
		}
		catch (IOException ioe) {
			chatArea.append("Cannot connect to server. Check your host or port number.\n");
			chatArea.setCaretPosition(chatArea.getDocument().getLength());
			connect.setEnabled(true);
		}
	}
		
	public void sendPrivateMessage(String msg) {
		try {
			if (msg.equals("")) {
				game.sendText.requestFocusInWindow();
				return;
			}
			out.write(2);
			out.writeUTF(msg);
			out.flush();
			game.sendText.setText("");
			game.sendText.requestFocusInWindow();
		}
		catch (IOException ioe) {
			game.privateChat.append("Cannot send message. Check server connection\n");
			game.privateChat.setCaretPosition(game.privateChat.getDocument().getLength());
			connect.setEnabled(true);
			queue.setEnabled(true);
		}
		catch (NullPointerException npe) {
			game.privateChat.append("Cannot send message. Check server connection\n");
			game.privateChat.setCaretPosition(game.privateChat.getDocument().getLength());
			connect.setEnabled(true);
			queue.setEnabled(true);
		}
	}
	
	public void sendPublicMessage(String msg) {
		try {
			if (msg.equals("")) {
				sendText.requestFocusInWindow();
				return;
			}
			out.write(1);
			out.writeUTF(msg);
			out.flush();
							
			sendText.setText("");
			sendText.requestFocusInWindow();
		}
		catch (IOException ioe) {
			chatArea.append("Not connected to a server\n");
			chatArea.setCaretPosition(chatArea.getDocument().getLength());
			connect.setEnabled(true);
			queue.setEnabled(true);
		}		
		catch (NullPointerException npe) {
			chatArea.append("Not connected to a server\n");
			chatArea.setCaretPosition(chatArea.getDocument().getLength());
			connect.setEnabled(true);
			queue.setEnabled(true);
		}
	}
	
	public void sendMove(int currentX, int currentY, int toX, int toY) {
		try {
			out.write(4);			
			out.writeInt(currentX);
			out.writeInt(currentY);
			out.writeInt(toX);
			out.writeInt(toY);
			out.flush();
		}
		catch (IOException ioe) {
			game.privateChat.append("Cannot send move. Check server connection.\n");
			game.privateChat.setCaretPosition(game.privateChat.getDocument().getLength());
			connect.setEnabled(true);
			queue.setEnabled(true);
		}		
		catch (NullPointerException npe) {
			game.privateChat.append("Cannot send move. Check server connection.\n");
			game.privateChat.setCaretPosition(game.privateChat.getDocument().getLength());
			connect.setEnabled(true);
			queue.setEnabled(true);
		}
	}
	
	public void joinQueue() {
		try {			
			out.write(3);
			out.flush();
			chatArea.append("[" + sdf.format(cal.getTime()) + "] " + "You have joined a queue. Please wait while you are being matched.\n");
			chatArea.setCaretPosition(chatArea.getDocument().getLength());
			queue.setEnabled(false);
		}
		catch (IOException ioe) {
			chatArea.append("You are not connected to a server.\n");
			chatArea.setCaretPosition(chatArea.getDocument().getLength());
			queue.setEnabled(true);
			connect.setEnabled(true);
		}
		catch (NullPointerException npe) {
			chatArea.append("Cannot join queue. Not connected to a server\n");
			chatArea.setCaretPosition(chatArea.getDocument().getLength());
		}
	}
			
	public static void main(String [] args) {
		new Chat();
	}
	
	class Client extends Thread {
		
		public Client() {
		}
		
		public void run() {
			try {
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
				
				out.writeUTF(username);
				out.flush();
				
				int timeOut = INITIAL_TIMEOUT;
				int returnCode = in.readInt();
				
				if (returnCode == 0) {	
					chatArea.append("Welcome, " + username + "!\n");
					chatArea.setCaretPosition(chatArea.getDocument().getLength());		
							
					while(true) {
						try {
							sleep(500);
							
							if (timeOut == 0) {
								disconnect();
								return;
							}
							
							if (in.available() > 0) {
								timeOut = INITIAL_TIMEOUT;
								
								int packetType = in.read();
								
								switch(packetType) {
									case 0: 
										//Recv heartbeat
										out.write(0);
										out.flush();
										break;
									case 1:
										//Recv lobby chat
										chatArea.append(in.readUTF() + " says: " + in.readUTF() + "\n");
										chatArea.setCaretPosition(chatArea.getDocument().getLength());
										break;
									case 2:
										//Recv game chat
										game.privateChat.append(in.readUTF() + " says: " + in.readUTF() + "\n");
										game.privateChat.setCaretPosition(game.privateChat.getDocument().getLength());
										break;
									case 3:
										//Joined a game
										playerID = in.readInt();
										player1 = in.readUTF();
										player2 = in.readUTF();
										game = new GameBoard(pokemon, username, player1, player2, playerID, Chat.this); 
										chatArea.append("[" + sdf.format(cal.getTime()) + "] " + "Game Created: " + player1 + " vs " + player2 + "\n");
										chatArea.setCaretPosition(chatArea.getDocument().getLength());
										game.privateChat.append(player1 + " joined the game\n");
										game.privateChat.append(player2 + " joined the game\n");
										game.privateChat.setCaretPosition(game.privateChat.getDocument().getLength());
										song.play();
										viewGame.setEnabled(true);
										break;
									case 4:
										//Game update
										playerTurn = in.readInt();
										if (playerTurn == 1) {
											game.privateChat.append(player1 + "'s turn\n");
											game.privateChat.setCaretPosition(game.privateChat.getDocument().getLength());
										}
										else {
											game.privateChat.append(player2 + "'s turn\n");
											game.privateChat.setCaretPosition(game.privateChat.getDocument().getLength());
										}
										String boardUpdate = in.readUTF();
										game.setTurn(playerTurn);
										game.updateBoard(boardUpdate);
										break;
									case 5:
										//Game Error Msg
										String errorMsg = in.readUTF();
										game.privateChat.append(errorMsg + "\n");
										game.privateChat.setCaretPosition(game.privateChat.getDocument().getLength());
										bump.play();
										JOptionPane.showMessageDialog(game, errorMsg);
										break;
									case 6:
										//Game Message
										String gameMsg = in.readUTF();
										game.privateChat.append(gameMsg + "\n");
										game.privateChat.setCaretPosition(game.privateChat.getDocument().getLength());
										bump.play();
										JOptionPane.showMessageDialog(game, gameMsg);
										break;
									case 7:
										//List of players
										numPlayers = in.readInt();
										users.setText("");
										for (int i = 0; i < numPlayers; i++) {
											users.append(in.readUTF() + "\n");
										}
										break;
									case 8:
										//Game Over
										String winnerMsg = in.readUTF();
										game.privateChat.append(winnerMsg + "\n");
										game.privateChat.setCaretPosition(game.privateChat.getDocument().getLength());
										chatArea.append("[" + sdf.format(cal.getTime()) + "] " + "Game Ended: " + player1 + " vs " + player2 + "\n" + winnerMsg + "\n");
										chatArea.setCaretPosition(chatArea.getDocument().getLength());
										for(int row = 0; row < 8; row++) {
											for(int col = 0; col < 8; col++) { 								
												game.buttons[row][col].setEnabled(false);
											}
										}
										game.participants.setText(winnerMsg);
										game.iconLabel.setIcon(null);
										song.stop();
										viewGame.setEnabled(false);
										queue.setEnabled(true);										
										break;
								}//End of switch
							}
							else {
								timeOut--;
							}
						}
						catch (InterruptedException ie) {
							System.out.println("Interrupted exception at packet processing");
						}
					}//End of while loop;
				}
				else if (returnCode == -1) {
					chatArea.append("Username taken. Choose another.\n");
					chatArea.setCaretPosition(chatArea.getDocument().getLength());
					disconnect();
					return;
				}
				
				disconnect();
			}
			catch (IOException ioe) {
				disconnect();
			}
		}//End of run		
	}//End of Client class
}//End of Chat class (the gui)