import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PokemonList extends JFrame{
	
	Background bg;
	SpringLayout layout;
	JLabel magikarp1, magikarp2;
	JLabel slot1, slot2;
	JLabel pressedSlot1, pressedSlot2;
	InterfaceContent content;
	ImageIcon playerSprite, playerSlot, pressedPlayerSlot;
	ImageIcon magikarpImg1, magikarpSlot1, magikarpPressedSlot1;
	ImageIcon magikarpImg2, magikarpSlot2, magikarpPressedSlot2;
	
	public PokemonList(InterfaceContent interfaceContent) {
		content = interfaceContent;
		
		bg = new Background("pokemonlist");
		layout = new SpringLayout();
		bg.setLayout(layout);
		
		createPokemon();
		createSlots();
		createPlayerStuff();
		setPositions();
				
		setTitle("My Pokemon");
		setContentPane(bg);
		setLocation(600, 400);
		setResizable(false);
		setSize(330, 275);		
	}
	
	public void createPokemon() {
		magikarpImg1 = new ImageIcon(getClass().getResource("avatar/Magikarp.gif"));		
		magikarpImg2 = new ImageIcon(getClass().getResource("battle/Magikarp.gif"));
		magikarp1 = new JLabel(magikarpImg1);
		magikarp2 = new JLabel(magikarpImg2);
		
		bg.add(magikarp1);
		bg.add(magikarp2);
	}
	
	public void createSlots() {
		magikarpSlot1 = new ImageIcon(getClass().getResource("interface/list/default1.png"));
		magikarpSlot2 = new ImageIcon(getClass().getResource("interface/list/default2.png"));
		slot1 = new JLabel(magikarpSlot1);
		slot2 = new JLabel(magikarpSlot2);
		
		magikarpPressedSlot1 = new ImageIcon(getClass().getResource("interface/list/pressed1.png"));
		magikarpPressedSlot2 = new ImageIcon(getClass().getResource("interface/list/pressed2.png"));
		pressedSlot1 = new JLabel(magikarpPressedSlot1); pressedSlot1.setVisible(false);
		pressedSlot2 = new JLabel(magikarpPressedSlot2); pressedSlot2.setVisible(false);
		
		bg.add(slot1); bg.add(pressedSlot1);
		bg.add(slot2); bg.add(pressedSlot2);
		
		slot1.addMouseListener(new MyMouseListener(slot1, pressedSlot1, "slot1", this));
		slot2.addMouseListener(new MyMouseListener(slot2, pressedSlot2, "slot2", this));
	}
	
	public void createPlayerStuff() {
		playerSprite = new ImageIcon(getClass().getResource("sprites/" + content.name + ".png"));
		playerSlot = new ImageIcon(getClass().getResource("interface/list/default3.png"));
		pressedPlayerSlot = new ImageIcon(getClass().getResource("interface/list/pressed3.png"));
	}
	
	public void setPositions() {
		//Magikarp slot 1
		layout.putConstraint(SpringLayout.WEST, magikarp1, 20, SpringLayout.WEST, bg);
		layout.putConstraint(SpringLayout.NORTH, magikarp1, 15, SpringLayout.NORTH, bg);
		
		//Slot 1
		layout.putConstraint(SpringLayout.WEST, slot1, 15, SpringLayout.WEST, bg);
		layout.putConstraint(SpringLayout.NORTH, slot1, 10, SpringLayout.NORTH, bg);
		
		//pressed 1
		layout.putConstraint(SpringLayout.WEST, pressedSlot1, 15, SpringLayout.WEST, bg);
		layout.putConstraint(SpringLayout.NORTH, pressedSlot1, 10, SpringLayout.NORTH, bg);
		
		//Magikarp slot 2
		layout.putConstraint(SpringLayout.WEST, magikarp2, 175, SpringLayout.WEST, bg);
		layout.putConstraint(SpringLayout.NORTH, magikarp2, 15, SpringLayout.NORTH, bg);
				
		//Slot 2
		layout.putConstraint(SpringLayout.WEST, slot2, 0, SpringLayout.EAST, slot1);
		layout.putConstraint(SpringLayout.NORTH, slot2, 10, SpringLayout.NORTH, bg);
		
		//pressed 2
		layout.putConstraint(SpringLayout.WEST, pressedSlot2, 0, SpringLayout.EAST, slot1);
		layout.putConstraint(SpringLayout.NORTH, pressedSlot2, 10, SpringLayout.NORTH, bg);			
	}	
}