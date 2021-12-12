import java.awt.*;
import javax.swing.*;
import java.awt.Color.*;
import java.awt.event.*;

public class Interface extends JFrame {

	Background background;
	ImageIcon hp;
	JLabel hpBar;
	String name;
	GameBoard board;
	Chat chat;
	
	public Interface(GameBoard _board, Chat _chat) {
		board = _board;
		chat = _chat;
		
      background = new Background("interface");
      background.setLayout(new BorderLayout());		  
					
		hp = new ImageIcon(getClass().getResource("hp/16.png"));
		
		hpBar = new JLabel(hp);
		hpBar.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));		  
		background.add(hpBar, BorderLayout.NORTH);
		
		InterfaceContent content = new InterfaceContent(background, board.player1, board.player2, board.playerID, this);		  
		background.add(content, BorderLayout.CENTER);		  
				 
		setTitle("Pokemon Interface");      
		setContentPane(background);
		setSize(300, 702);
      setVisible(true);
		setLocation(30+board.getWidth()+chat.getWidth(), 60);
		setResizable(false);	
	}
	
	public void setHP(int _numPieces) {
		int numPieces = _numPieces;
		if (numPieces%2 == 0) {
			hp = new ImageIcon(getClass().getResource("hp/" + numPieces + ".png"));
			hpBar.setIcon((Icon)hp);
		}
		else {
			numPieces++;
			hp = new ImageIcon(getClass().getResource("hp/" + numPieces + ".png"));
			hpBar.setIcon((Icon)hp);
		}
	}
}