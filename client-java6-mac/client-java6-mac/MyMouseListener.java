import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MyMouseListener implements MouseListener {
	
	JLabel original, selected, hover, selected2;
	int option;
	String name;
	JFrame bag, escape;
	FightWindow fight;
	PokemonList pkmnList;
	Interface theInterface;
	InterfaceContent content;
	ImageIcon magikarp = new ImageIcon(getClass().getResource("battle/Magikarp.png"));
	ImageIcon smagikarp = new ImageIcon(getClass().getResource("battle/SMagikarp.png"));
	ImageIcon player;
		
	public MyMouseListener(JLabel original, JLabel selected, JLabel hover, String name, InterfaceContent interfaceContent, Interface theInterface) {
		this.original = original;
		this.selected = selected;
		this.hover = hover;
		this.name = name;
		this.theInterface = theInterface;
		content = interfaceContent;
		option = 1;
		pkmnList = new PokemonList(interfaceContent);
		fight = new FightWindow(theInterface);
		createBag();
		createRun();		
	}
	
	public MyMouseListener(JLabel original, JLabel selected, JLabel hover, JLabel selected2, String name) {		
		this.original = original;
		this.selected = selected;
		this.hover = hover;
		this.selected2 = selected2;
		this.name = name;
		option = 2;
		createBag();
		createRun();
	}
	
	public MyMouseListener(JLabel original, JLabel selected, String name, PokemonList pkmnList) {
		this.pkmnList = pkmnList;
		this.original = original;
		this.selected = selected;
		this.name = name;
		player = new ImageIcon(getClass().getResource("battle/" + pkmnList.content.name + ".png"));
		option = 3;
		createBag();
		createRun();
	}
	
	//Reset to default buttons
	public void mouseExited(MouseEvent me) {
		if (option == 1) {
			original.setVisible(true);
			selected.setVisible(false);
			hover.setVisible(false);
		}
		else if (option == 2){
			hover.setVisible(false);
		}
		else if (option == 3) {
			original.setVisible(true);
			selected.setVisible(false);
		}
	}
	
	//Hover effects
	public void mouseEntered(MouseEvent me) {
		if (option == 1 || option == 2) {
			hover.setVisible(true);
		}
		else if (option == 3) {
			selected.setVisible(true);
		}
	}
	
	public void mouseReleased(MouseEvent me) {
	}
	
	//Invoked when mouse is held down
	public void mousePressed(MouseEvent me) {
		if (option == 1 || option == 3) {
			original.setVisible(false);
			selected.setVisible(true);
		}
		else if (option == 2) {
			if (selected.isVisible() == false) {
				selected.setVisible(true);
				selected2.setVisible(false);
				}
			else {
				selected.setVisible(false);
			}
		}
	}
	
	public void mouseClicked(MouseEvent me) {
		if (name.equals("fight")) {
			fight.updateChoices(theInterface.board.boardUpdate);
			
			if (content.selectedAttack1.isVisible()) {
				fight.setVisible(true);
				fight.setType(1);
			}
			else if (content.selectedAttack2.isVisible()) {
				fight.setVisible(true);
				fight.setType(2);
			}
			else {
				fight.error.setVisible(true);
			}
		}
		else if (name.equals("pokemon")) {
			pkmnList.setVisible(true);			
		}
		else if (name.equals("bag")) {
			bag.setVisible(true);
		}
		else if (name.equals("run")) {
			escape.setVisible(true);
		}
		else if (name.equals("slot1")) {								
			Icon selectedSlot = pkmnList.magikarp1.getIcon();
			Icon otherSlot = pkmnList.magikarp2.getIcon();
			
			if ((selectedSlot == pkmnList.magikarpImg1) && (otherSlot == pkmnList.magikarpImg2)) {
				pkmnList.content.battle.setIcon(magikarp);
				
				pkmnList.magikarp1.setIcon(pkmnList.playerSprite);
				pkmnList.slot1.setIcon(pkmnList.playerSlot);
				pkmnList.pressedSlot1.setIcon(pkmnList.pressedPlayerSlot);
				
				pkmnList.layout.putConstraint(SpringLayout.WEST, pkmnList.magikarp1, 30, SpringLayout.WEST, pkmnList.bg);
				pkmnList.layout.putConstraint(SpringLayout.NORTH, pkmnList.magikarp1, 30, SpringLayout.NORTH, pkmnList.bg);
			}
			else if ((selectedSlot == pkmnList.magikarpImg2) && (otherSlot == pkmnList.magikarpImg1)) {
				pkmnList.content.battle.setIcon(smagikarp);
				
				pkmnList.magikarp1.setIcon(pkmnList.playerSprite);
				pkmnList.slot1.setIcon(pkmnList.playerSlot);
				pkmnList.pressedSlot1.setIcon(pkmnList.pressedPlayerSlot);
				
				pkmnList.layout.putConstraint(SpringLayout.WEST, pkmnList.magikarp1, 30, SpringLayout.WEST, pkmnList.bg);
				pkmnList.layout.putConstraint(SpringLayout.NORTH, pkmnList.magikarp1, 30, SpringLayout.NORTH, pkmnList.bg);
			}
			else if ((selectedSlot == pkmnList.playerSprite) && (otherSlot == pkmnList.magikarpImg1)) {
				pkmnList.content.battle.setIcon(player);
				
				pkmnList.magikarp1.setIcon(pkmnList.magikarpImg2);
				pkmnList.slot1.setIcon(pkmnList.magikarpSlot2);
				pkmnList.pressedSlot1.setIcon(pkmnList.magikarpPressedSlot2);
				
				pkmnList.layout.putConstraint(SpringLayout.WEST, pkmnList.magikarp1, 27, SpringLayout.WEST, pkmnList.bg);
				pkmnList.layout.putConstraint(SpringLayout.NORTH, pkmnList.magikarp1, 15, SpringLayout.NORTH, pkmnList.bg);
			}	
			else if ((selectedSlot == pkmnList.playerSprite) && (otherSlot == pkmnList.magikarpImg2)) {
				pkmnList.content.battle.setIcon(player);
				
				pkmnList.magikarp1.setIcon(pkmnList.magikarpImg1);
				pkmnList.slot1.setIcon(pkmnList.magikarpSlot1);
				pkmnList.pressedSlot1.setIcon(pkmnList.magikarpPressedSlot1);
				
				pkmnList.layout.putConstraint(SpringLayout.WEST, pkmnList.magikarp1, 20, SpringLayout.WEST, pkmnList.bg);
				pkmnList.layout.putConstraint(SpringLayout.NORTH, pkmnList.magikarp1, 15, SpringLayout.NORTH, pkmnList.bg);
			}	
			else if ((selectedSlot == pkmnList.magikarpImg1) && (otherSlot == pkmnList.playerSprite)) {
				pkmnList.content.battle.setIcon(magikarp);
				
				pkmnList.magikarp1.setIcon(pkmnList.magikarpImg2);
				pkmnList.slot1.setIcon(pkmnList.magikarpSlot2);
				pkmnList.pressedSlot1.setIcon(pkmnList.magikarpPressedSlot2);
				
				pkmnList.layout.putConstraint(SpringLayout.WEST, pkmnList.magikarp1, 27, SpringLayout.WEST, pkmnList.bg);
				pkmnList.layout.putConstraint(SpringLayout.NORTH, pkmnList.magikarp1, 15, SpringLayout.NORTH, pkmnList.bg);
			}	
			else if ((selectedSlot == pkmnList.magikarpImg2) && (otherSlot == pkmnList.playerSprite)) {
				pkmnList.content.battle.setIcon(smagikarp);
				
				pkmnList.magikarp1.setIcon(pkmnList.magikarpImg1);
				pkmnList.slot1.setIcon(pkmnList.magikarpSlot1);
				pkmnList.pressedSlot1.setIcon(pkmnList.magikarpPressedSlot1);
				
				pkmnList.layout.putConstraint(SpringLayout.WEST, pkmnList.magikarp1, 20, SpringLayout.WEST, pkmnList.bg);
				pkmnList.layout.putConstraint(SpringLayout.NORTH, pkmnList.magikarp1, 15, SpringLayout.NORTH, pkmnList.bg);
			}			
		}
		else if (name.equals("slot2")) {	
			Icon selectedSlot = pkmnList.magikarp2.getIcon();
			Icon otherSlot = pkmnList.magikarp1.getIcon();
			
			if ((selectedSlot == pkmnList.magikarpImg1) && (otherSlot == pkmnList.magikarpImg2)) {
				pkmnList.content.battle.setIcon(magikarp);
				
				pkmnList.magikarp2.setIcon(pkmnList.playerSprite);
				pkmnList.slot2.setIcon(pkmnList.playerSlot);
				pkmnList.pressedSlot2.setIcon(pkmnList.pressedPlayerSlot);
				
				pkmnList.layout.putConstraint(SpringLayout.WEST, pkmnList.magikarp2, 175, SpringLayout.WEST, pkmnList.bg);
				pkmnList.layout.putConstraint(SpringLayout.NORTH, pkmnList.magikarp2, 30, SpringLayout.NORTH, pkmnList.bg);
			}
			else if ((selectedSlot == pkmnList.magikarpImg2) && (otherSlot == pkmnList.magikarpImg1)) {
				pkmnList.content.battle.setIcon(smagikarp);
				
				pkmnList.magikarp2.setIcon(pkmnList.playerSprite);
				pkmnList.slot2.setIcon(pkmnList.playerSlot);
				pkmnList.pressedSlot2.setIcon(pkmnList.pressedPlayerSlot);
				
				pkmnList.layout.putConstraint(SpringLayout.WEST, pkmnList.magikarp2, 175, SpringLayout.WEST, pkmnList.bg);
				pkmnList.layout.putConstraint(SpringLayout.NORTH, pkmnList.magikarp2, 30, SpringLayout.NORTH, pkmnList.bg);
			}
			else if ((selectedSlot == pkmnList.playerSprite) && (otherSlot == pkmnList.magikarpImg1)) {
				pkmnList.content.battle.setIcon(player);
				
				pkmnList.magikarp2.setIcon(pkmnList.magikarpImg2);
				pkmnList.slot2.setIcon(pkmnList.magikarpSlot2);
				pkmnList.pressedSlot2.setIcon(pkmnList.magikarpPressedSlot2);
				
				pkmnList.layout.putConstraint(SpringLayout.WEST, pkmnList.magikarp2, 175, SpringLayout.WEST, pkmnList.bg);
				pkmnList.layout.putConstraint(SpringLayout.NORTH, pkmnList.magikarp2, 15, SpringLayout.NORTH, pkmnList.bg);
			}	
			else if ((selectedSlot == pkmnList.playerSprite) && (otherSlot == pkmnList.magikarpImg2)) {
				pkmnList.content.battle.setIcon(player);
				
				pkmnList.magikarp2.setIcon(pkmnList.magikarpImg1);
				pkmnList.slot2.setIcon(pkmnList.magikarpSlot1);
				pkmnList.pressedSlot2.setIcon(pkmnList.magikarpPressedSlot1);
				
				pkmnList.layout.putConstraint(SpringLayout.WEST, pkmnList.magikarp2, 165, SpringLayout.WEST, pkmnList.bg);
				pkmnList.layout.putConstraint(SpringLayout.NORTH, pkmnList.magikarp2, 15, SpringLayout.NORTH, pkmnList.bg);
			}	
			else if ((selectedSlot == pkmnList.magikarpImg1) && (otherSlot == pkmnList.playerSprite)) {
				pkmnList.content.battle.setIcon(magikarp);
				
				pkmnList.magikarp2.setIcon(pkmnList.magikarpImg2);
				pkmnList.slot2.setIcon(pkmnList.magikarpSlot2);
				pkmnList.pressedSlot2.setIcon(pkmnList.magikarpPressedSlot2);
				
				pkmnList.layout.putConstraint(SpringLayout.WEST, pkmnList.magikarp2, 175, SpringLayout.WEST, pkmnList.bg);
				pkmnList.layout.putConstraint(SpringLayout.NORTH, pkmnList.magikarp2, 15, SpringLayout.NORTH, pkmnList.bg);
			}	
			else if ((selectedSlot == pkmnList.magikarpImg2) && (otherSlot == pkmnList.playerSprite)) {
				pkmnList.content.battle.setIcon(smagikarp);
				
				pkmnList.magikarp2.setIcon(pkmnList.magikarpImg1);
				pkmnList.slot2.setIcon(pkmnList.magikarpSlot1);
				pkmnList.pressedSlot2.setIcon(pkmnList.magikarpPressedSlot1);
				
				pkmnList.layout.putConstraint(SpringLayout.WEST, pkmnList.magikarp2, 165, SpringLayout.WEST, pkmnList.bg);
				pkmnList.layout.putConstraint(SpringLayout.NORTH, pkmnList.magikarp2, 15, SpringLayout.NORTH, pkmnList.bg);
			}		
		}
	}
		
	public void createBag() {
		bag = new JFrame("Bag - doesn't do anything!");
		Background bagBackground = new Background("bag");
		
		bag.setContentPane(bagBackground);
		bag.setLocation(500, 400);
		bag.setResizable(false);
		bag.setSize(500, 225);
	}
	
	public void createRun() {
		escape = new JFrame("Oh nooo");
						
		JLabel escapeMsg = new JLabel("CAN'T ESCAPE!");			
		escapeMsg.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		escapeMsg.setHorizontalAlignment(JLabel.CENTER);
		escapeMsg.setForeground(new Color(255,255,255));
		
		escape.add(escapeMsg, BorderLayout.CENTER);
		escape.getContentPane().setBackground(new Color(1,80,96));
					
		escape.setLocation(650, 400);
		escape.setResizable(false);
		escape.setSize(200, 100);
	}
}