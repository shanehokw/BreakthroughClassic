import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class GameBoard extends JFrame implements ActionListener{
	
	JFrame rulesFrame;
	JTextArea privateChat;
	JTextField sendText;
	JButton send, rulesOK;
	JPanel north, center, south, innerSouth;
	JLabel participants, iconLabel;
	JScrollPane privateChatScroll;
	JMenuBar menuBar;
	JMenu file, help, game;
	JMenuItem rules, clear; 
	
	ImageIcon avatar1, avatar2;
	
	int count = 1;	
	static JButton [][] buttons = new JButton[8][8];	
	String player1, player2, username, boardUpdate, currentCoordinates, toCoordinates;;
	
	static int playerID, playerTurn;	
	
	Chat chat;
		
	public GameBoard(String username, String p1, String p2, int id, Chat _chat) {		
		chat = _chat;
		this.avatar1 = avatar1;
		this.avatar2 = avatar2;
		this.username = username;
		player1 = p1;
		player2 = p2;
		playerID = id;
		
		setAvatars(player1, player2);
		
		createNorth();
		createCenter();
		createSouth();
		createMenu();
		
		setTitle("You are playing as " + username);
		setVisible(true);
		sendText.requestFocusInWindow();
		pack();
		setResizable(false);
		setLocation(20+chat.getWidth(), 60);
		
		displayRules();	
	}
	
	public void displayRules() {
		JLabel header = new JLabel(" RULES:");
		
		header.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		JLabel rule1 = new JLabel(" 1.  You are able to move diagonally forward by one unit space.");
		JLabel rule2 = new JLabel(" 2.  You are able to move horizontally forward by one unit space.");
		JLabel rule3 = new JLabel(" 3.  You may only capture an enemy unit if it is located diagonally forward.");
		JLabel rule4 = new JLabel(" 4.  You cannot move vertically or backwards.");
		JLabel rule5 = new JLabel(" 5.  Player who reaches other side first or captures all enemy units wins.");
		
		rulesFrame = new JFrame("Rules");
		
		JPanel rulesPanel = new JPanel();
		rulesPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		rulesPanel.setLayout(new BoxLayout(rulesPanel, BoxLayout.Y_AXIS));		
		rulesPanel.add(rule1); rulesPanel.add(rule2); rulesPanel.add(rule3); rulesPanel.add(rule4); rulesPanel.add(rule5);
		
		JPanel south = new JPanel();
		rulesOK = new JButton("OK");
		rulesFrame.getRootPane().setDefaultButton(rulesOK);
		rulesOK.requestFocus();
		rulesOK.setPreferredSize(new Dimension(75, 25));
		south.add(rulesOK);
				
		rulesFrame.add(header, BorderLayout.NORTH);
		rulesFrame.add(rulesPanel, BorderLayout.CENTER);
		rulesFrame.add(south, BorderLayout.SOUTH);
		
		rulesFrame.setVisible(true);
		rulesFrame.pack();
		rulesFrame.setLocationRelativeTo(this);
		rulesFrame.setResizable(false);
		
		rulesOK.addActionListener(this);
	}
	
	public void setTurn(int turn) {
		playerTurn = turn;
	}
	
	public void setAvatars(String p1, String p2) {
		avatar1 = new ImageIcon(getClass().getResource("sprites/blackslot.png"));
		avatar2 = new ImageIcon(getClass().getResource("sprites/redslot.png"));
	}
	
	public void createMenu() {
		menuBar = new JMenuBar();
		
		help = new JMenu("Help");
		file = new JMenu("File");
		
		clear = new JMenuItem("Clear");
		rules = new JMenuItem("Rules");
		
		menuBar.add(file);
		menuBar.add(help);
		
		help.add(rules);
		file.add(clear);
		
		setJMenuBar(menuBar);
		
		rules.addActionListener(this);
	}
	
	public void createNorth() {
		north = new JPanel();
		
		participants = new JLabel(player1.toUpperCase() + " vs " + player2.toUpperCase());
		participants.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		participants.setForeground(new Color(1,80,150));
		
		iconLabel = new JLabel();
		
		north.add(participants);
		north.add(iconLabel);
		add(north, BorderLayout.NORTH);
	}
	
	public void createCenter() {
		center = new JPanel(new GridLayout(8,8));

		for(int row = 0; row < 8; row++) {
			for(int col = 0; col < 8; col++) { 
				//create an object of the inner Button class and add to 2D array
				buttons[row][col] = new Button(row, col);
				center.add(buttons[row][col]);   
				buttons[row][col].setPreferredSize(new Dimension(65,60)); 
				buttons[row][col].setOpaque(true);
				buttons[row][col].setBorderPainted(false);
				buttons[row][col].setBackground(new Color(238, 238, 238));
				setColor(row, col);				
			}
		}
		
		add(center, BorderLayout.CENTER);
	}
	
	public void createSouth() {
		south = new JPanel();
		innerSouth = new JPanel();
		
		south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
		
		privateChat = new JTextArea(5, 5);
		privateChat.setLineWrap(true);
		privateChat.setWrapStyleWord(true);
		privateChatScroll = new JScrollPane(privateChat);
		privateChat.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		privateChat.setForeground(new Color(1,80,125));
		
		sendText = new JTextField(15);
		send = new JButton("Send");
		
		innerSouth.add(sendText);
		innerSouth.add(send);
				
		south.add(privateChatScroll);
		south.add(innerSouth);
		add(south, BorderLayout.SOUTH);
		
		send.addActionListener(this);
		sendText.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				if (ke.getKeyCode() == KeyEvent.VK_ENTER)	{
					chat.sendPrivateMessage(sendText.getText());
				}						
			}
		});
	}

	public void updateBoard(String _boardUpdate) {
		boardUpdate = _boardUpdate;
		String [] boardLayout = boardUpdate.split(",");
		int counter = 0;
		
		int one = 0;
		int two = 0;
		
		for (int i = 0; i < boardLayout.length; i++) {
			if (boardLayout[i].equals("1")) {
				one++;
			}
			else if (boardLayout[i].equals("2")) {
				two++;
			}
		}
		
		if (playerID == playerTurn) {
			participants.setText("YOUR TURN");
			if (playerID == 1) {
				iconLabel.setIcon(new ImageIcon(getClass().getResource("sprites/blackslot.png")));
			}
			else {
				iconLabel.setIcon(new ImageIcon(getClass().getResource("sprites/redslot.png")));
			}
		}
		else {
			participants.setText("OPPONENT'S TURN");
			if (playerID == 1) {
				iconLabel.setIcon(new ImageIcon(getClass().getResource("sprites/redslot.png")));
			}
			else {
				iconLabel.setIcon(new ImageIcon(getClass().getResource("sprites/blackslot.png")));
			}
		}
		
			
		for(int row = 0; row < 8; row++) {
			for(int col = 0; col < 8; col++) {   
				if (boardLayout[counter].equals("1")) {
					buttons[row][col].setIcon(avatar1);					
					counter++;
				}
				else if (boardLayout[counter].equals("2")) {
					buttons[row][col].setIcon(avatar2);					
					counter++;
				}
				else if (boardLayout[counter].equals("0")) {
					buttons[row][col].setIcon(null);
					counter++;
				}
			}
		}	
	}
	
	public void setColor(int row, int col) {
		if ((row == 0 || row == 2 || row == 4 || row == 6) && (col == 0 || col == 2 || col == 4 || col == 6)) {
			buttons[row][col].setBackground(new Color(182,148,110));
			buttons[row][col].setOpaque(true);
			buttons[row][col].setBorderPainted(false);
		}//set dark blocks for all even numbered rows
		else if ((row == 1 || row == 3 || row == 5 || row == 7) && (col == 1 || col == 3 || col == 5 || col == 7)) {
			buttons[row][col].setBackground(new Color(182,148,110));
			buttons[row][col].setOpaque(true);
			buttons[row][col].setBorderPainted(false);
		}//set dark blocks for all odd numbered rows
		else if ((row == 0 || row == 2 || row == 4 || row == 6) && (col == 1 || col == 3 || col == 5 || col == 7)) {
			buttons[row][col].setBackground(new Color(255,241,220));
			buttons[row][col].setOpaque(true);
			buttons[row][col].setBorderPainted(false);
		}//set light blocks for all even numbered rows
		else if ((row == 1 || row == 3 || row == 5 || row == 7) && (col == 0 || col == 2 || col == 4 || col == 6)) {
			buttons[row][col].setBackground(new Color(255,241,220));
			buttons[row][col].setOpaque(true);
			buttons[row][col].setBorderPainted(false);
		}//set light blocks for all odd numbered rows
	}
	
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == send) {
			chat.sendPrivateMessage(sendText.getText());
		}
		else if (ae.getSource() == clear) {
			privateChat.setText("");
			sendText.requestFocusInWindow();
		}
		else if (ae.getSource() == rules) {
			displayRules();
		}
		
		else if (ae.getSource() == rulesOK) {
			rulesFrame.setVisible(false);
		}
	}
	
	class Button extends JButton implements ActionListener {

		int row, col;
		
		public Button(int row, int col) {
			this.row = row;
			this.col = col;
				
			setCoordinates();
			addActionListener(this);
		}
		
		public void setCoordinates()  {
			String message = "(" + col + ", " + row + ")";
			setToolTipText(message);
		}
		
		public String getCoordinates() {
			String coordinates = "(" + col + "," + row + ")";
			return coordinates;
		}
		
		public void sendCoordinates() {
			int currentX = Integer.parseInt(currentCoordinates.substring(1,2));
			int currentY = Integer.parseInt(currentCoordinates.substring(3,4));
			int toX = Integer.parseInt(toCoordinates.substring(1,2));
			int toY = Integer.parseInt(toCoordinates.substring(3,4));
			chat.sendMove(currentX, currentY, toX, toY);
		}
		
		public void actionPerformed(ActionEvent ae)  { 
			
			Toolkit toolkit = Toolkit.getDefaultToolkit();
		
			if ((playerID == 1 && playerTurn == 1) || (playerID == 2 && playerTurn == 2)) {
				// selects piece to move
				if (count == 1) {				
					for (int row = 0; row < 8; row++) {
						for (int col = 0; col < 8; col++) {
							GameBoard.buttons[col][row].setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						}
					}
					setBackground(new Color(1,80,125));
					currentCoordinates = getCoordinates();
					count = 2;
				}
				
				// selects location to move to
				else if(count == 2) {					
					for (int row = 0; row < 8; row++) {
						for (int col = 0; col < 8; col++) {
							GameBoard.buttons[col][row].setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
							buttons[row][col].setBackground(new Color(238, 238, 238));
							setColor(row, col);
						}
					} 					
					toCoordinates = getCoordinates();
					count = 1;
					sendCoordinates();
				}
			}
			else {
				JOptionPane.showMessageDialog(this, "It is not your turn");
			}
		}
	}//End of button inner class
}//End of GameBoard class