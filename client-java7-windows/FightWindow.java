import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.util.*;

public class FightWindow extends JFrame implements ActionListener {
	
	Interface theInterface;
	JPanel center, south;
	JComboBox currentCoordinates, toCoordinates;
	JButton sendMove, errorOK;
	JLabel curr, to;
	JFrame  error;
	String boardLayout, selection, toCoords, playerID;
	int type;
	
	public FightWindow(Interface _interface) {
		theInterface = _interface;
		
		createCenter();
		createSouth();
		displayError();
		
		setTitle("What Do You Want To Do?");
		setSize(400, 120);
		setResizable(false);
		setLocation(525, 400);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				resetColors();
			}
		});
	}
	
	public void displayError() {
		error = new JFrame("!!");
		
		JPanel errorCenter = new JPanel();
		JPanel errorSouth = new JPanel();
		
		JLabel errorLabel = new JLabel("SELECT AN ATTACK FIRST!");
		errorLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		errorLabel.setForeground(new Color(1,80,125));
		errorCenter.add(errorLabel);
		
		errorOK = new JButton("OK");
		errorOK.setPreferredSize(new Dimension(75, 25));
		error.getRootPane().setDefaultButton(errorOK);
		errorOK.requestFocus();
		
		errorSouth.add(errorOK);
		
		error.add(errorCenter, BorderLayout.CENTER);
		error.add(errorSouth, BorderLayout.SOUTH);	
		
		error.pack();
		error.setLocationRelativeTo(error);	
		
		errorOK.addActionListener(this);
	}
		
	public void createCenter() {
		center = new JPanel();
		
		curr = new JLabel("MOVE PIECE FROM ");
		to = new JLabel("TO ");
		
		curr.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		to.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		
		curr.setForeground(new Color(1,80,125));
		to.setForeground(new Color(1,80,125));
		
		currentCoordinates = new JComboBox();
		currentCoordinates.setPreferredSize(new Dimension(90, 25));
		((JLabel)currentCoordinates.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		
		toCoordinates = new JComboBox();
		toCoordinates.setPreferredSize(new Dimension(90, 25));
		toCoordinates.setEnabled(false);
		((JLabel)toCoordinates.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
				
		center.add(curr);
		center.add(currentCoordinates);
		center.add(to);
		center.add(toCoordinates);
		
		add(center, BorderLayout.CENTER);
		currentCoordinates.addActionListener(this);
		toCoordinates.addActionListener(this);
	}
	
	public void createSouth() {
		south = new JPanel();
		sendMove = new JButton("Send Move");
		south.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		south.add(sendMove);
		add(south, BorderLayout.SOUTH);
		sendMove.addActionListener(this);
	}
	
	public void updateChoices(String _boardLayout) {
		boardLayout = _boardLayout;
		String [] boardList = boardLayout.split(",");
		currentCoordinates.removeAllItems();
		currentCoordinates.addItem("");
		
		playerID = "" + theInterface.board.playerID;				
		int i = 0;
		
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				if (boardList[i].equals(playerID)) {
					currentCoordinates.addItem("(" + x + "," + y + ")");
				}
				i++;
			}
		}
	}
	
	public void updateSecondChoices() {
		toCoordinates.removeAllItems();
		
		if (selection.equals("") || selection == null) {
			return;
		}
		else{
			int x = Integer.parseInt(selection.substring(1,2));
			int y = Integer.parseInt(selection.substring(3,4));
			
			theInterface.board.buttons[y][x].setBackground(new Color(1,80,125));
		
			if (type == 1) {
				if (playerID.equals("1")) {
					if (y == 0) {
						x++;
						toCoordinates.addItem("(" + x + "," + y + ")");
						theInterface.board.buttons[y][x].setBackground(new Color(175, 198, 211));
						y++;
						toCoordinates.addItem("(" + x + "," + y + ")");
						theInterface.board.buttons[y][x].setBackground(new Color(175, 198, 211));
					}
					else if (y == 7) {
						x++;
						toCoordinates.addItem("(" + x + "," + y + ")");
						theInterface.board.buttons[y][x].setBackground(new Color(175, 198, 211));
						y--;
						toCoordinates.addItem("(" + x + "," + y + ")");
						theInterface.board.buttons[y][x].setBackground(new Color(175, 198, 211));
					}
					else {
						x++;
						int y1 = y-1;
						toCoordinates.addItem("(" + x + "," + y1 + ")");
						theInterface.board.buttons[y1][x].setBackground(new Color(175, 198, 211));
						toCoordinates.addItem("(" + x + "," + y + ")");
						theInterface.board.buttons[y][x].setBackground(new Color(175, 198, 211));
						int y2 = y+1;
						toCoordinates.addItem("(" + x + "," + y2 + ")");
						theInterface.board.buttons[y2][x].setBackground(new Color(175, 198, 211));
					}
				}
				else if (playerID.equals("2")) {
					if (y == 0) {
						x--;
						toCoordinates.addItem("(" + x + "," + y + ")");
						theInterface.board.buttons[y][x].setBackground(new Color(175, 198, 211));
						y++;
						toCoordinates.addItem("(" + x + "," + y + ")");
						theInterface.board.buttons[y][x].setBackground(new Color(175, 198, 211));
					}
					else if (y == 7) {
						x--;
						toCoordinates.addItem("(" + x + "," + y + ")");
						theInterface.board.buttons[y][x].setBackground(new Color(175, 198, 211));
						y--;
						toCoordinates.addItem("(" + x + "," + y + ")");
						theInterface.board.buttons[y][x].setBackground(new Color(175, 198, 211));
					}
					else {
						x--;
						int y1 = y-1;
						toCoordinates.addItem("(" + x + "," + y1 + ")");
						theInterface.board.buttons[y1][x].setBackground(new Color(175, 198, 211));
						toCoordinates.addItem("(" + x + "," + y + ")");						
						theInterface.board.buttons[y][x].setBackground(new Color(175, 198, 211));
						int y2 = y+1;
						toCoordinates.addItem("(" + x + "," + y2 + ")");
						theInterface.board.buttons[y2][x].setBackground(new Color(175, 198, 211));
					}
				}
			}
			else if (type == 2) {
				if (playerID.equals("1")) {
					if (y == 0) {
						x++;
						y++;
						toCoordinates.addItem("(" + x + "," + y + ")");
						theInterface.board.buttons[y][x].setBackground(new Color(175, 198, 211));
					}
					else if (y == 7) {
						x++;
						y--;
						toCoordinates.addItem("(" + x + "," + y + ")");
						theInterface.board.buttons[y][x].setBackground(new Color(175, 198, 211));
					}
					else {
						x++;
						int y1 = y-1;
						toCoordinates.addItem("(" + x + "," + y1 + ")");
						theInterface.board.buttons[y1][x].setBackground(new Color(175, 198, 211));
						int y2 = y+1;
						toCoordinates.addItem("(" + x + "," + y2 + ")");
						theInterface.board.buttons[y2][x].setBackground(new Color(175, 198, 211));
					}
				}
				else if (playerID.equals("2")) {
					if (y == 0) {
						x--;
						y++;
						toCoordinates.addItem("(" + x + "," + y + ")");
						theInterface.board.buttons[y][x].setBackground(new Color(175, 198, 211));
					}
					else if (y == 7) {
						x--;
						y--;
						toCoordinates.addItem("(" + x + "," + y + ")");
						theInterface.board.buttons[y][x].setBackground(new Color(175, 198, 211));
					}
					else {
						x--;
						int y1 = y-1;
						toCoordinates.addItem("(" + x + "," + y1 + ")");
						theInterface.board.buttons[y1][x].setBackground(new Color(175, 198, 211));
						int y2 = y+1;
						toCoordinates.addItem("(" + x + "," + y2 + ")");
						theInterface.board.buttons[y2][x].setBackground(new Color(175, 198, 211));
					}
				}			
			}	
		}	
	}
	
	public void resetColors() {
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				theInterface.board.buttons[row][col].setBackground(new Color(238, 238, 238));
				theInterface.board.setColor(row,col);
			}
		}	
	}
	
	public void setType(int _type) {
		type = _type;
	}
		
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == sendMove) {
			if (selection.equals("") || selection == null) {
				return;
			}
			else if (theInterface.board.participants.getText().indexOf("OPPONENT") != -1) {
				JOptionPane.showMessageDialog(null, "It is not your turn");
				return;
			}
			resetColors();
			int currentX = Integer.parseInt(selection.substring(1,2));
			int currentY = Integer.parseInt(selection.substring(3,4));
			toCoords = (String) toCoordinates.getSelectedItem();
			int toX = Integer.parseInt(toCoords.substring(1,2));
			int toY = Integer.parseInt(toCoords.substring(3,4));
			
			theInterface.chat.sendMove(currentX, currentY, toX, toY);
		}
		else if (ae.getSource() == currentCoordinates) {
			selection = (String) currentCoordinates.getSelectedItem();
			resetColors();

			try {
				if (selection.equals("") || selection == null) {
					toCoordinates.setEnabled(false);
					return;
				}
				else {
					updateSecondChoices();
					toCoordinates.setEnabled(true);
					toCoords = (String) toCoordinates.getSelectedItem();
					int x = Integer.parseInt(toCoords.substring(1,2));
					int y = Integer.parseInt(toCoords.substring(3,4));
					
					theInterface.board.buttons[y][x].setBackground(new Color(103, 155, 185));
				}
			}
			catch(NullPointerException npe) {}
		}
		else if (ae.getSource() == errorOK) {
			error.setVisible(false);
		}
		else if (ae.getSource() == toCoordinates) {
			for (int i = 0; i < toCoordinates.getItemCount(); i++) {
				if (toCoordinates.getItemAt(i) == toCoordinates.getSelectedItem()) {
					toCoords = (String) toCoordinates.getItemAt(i);
					int x = Integer.parseInt(toCoords.substring(1,2));
					int y = Integer.parseInt(toCoords.substring(3,4));
					
					theInterface.board.buttons[y][x].setBackground(new Color(103, 155, 185));
				}
				else {
					toCoords = (String) toCoordinates.getItemAt(i);
					int x = Integer.parseInt(toCoords.substring(1,2));
					int y = Integer.parseInt(toCoords.substring(3,4));
					
					theInterface.board.buttons[y][x].setBackground(new Color(175, 198, 211));
				}
			}
		}
	}
}
