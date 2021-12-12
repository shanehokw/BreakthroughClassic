import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class InterfaceContent extends JPanel {

	JPanel bgPanel; 											
	JLabel attack1, hoverAttack1, selectedAttack1;
	JLabel attack2, hoverAttack2, selectedAttack2;	
	JLabel fight, fightPressed, fightHover;			
	JLabel pokemon, pokemonPressed, pokemonHover;	
	JLabel bag, bagPressed, bagHover;					
	JLabel run, runPressed, runHover;					
	JLabel avatar, pokeName, pokeNum, nature, text, text2;	//Trainer card labels
	JLabel battle, opponent;						
	ImageIcon avatarImage, battleImage, opponentImage;
	SpringLayout layout;
	String name, enemy;
	int id;
	Interface theInterface;
	
	String [] natureList = {"Hardy", "Lonely", "Brave", "Naughty", "Adamant", "Relaxed", "Impish", "Modest", "Jolly", "Quirky"};
	String [] quotes = {"All's fair in love, war, and Pokémon battles.", "A child like you would never understand what I hope to achieve!", 
	"Wow, it's a lot different to read about something... then really feel it!", "Is that supposed to be a Pokémon or a Christmas tree?",
	"Sometimes I look at that Psyduck's face and I get a headache.", "And I thought you were going to buy a stone to evolve yourself."};
	
	public InterfaceContent(JPanel _bg, String p1, String p2, int id, Interface _interface) {
		theInterface = _interface;
		
		if (id == 1) {
			name = p1;
			enemy = p2;
		}
		else {
			name = p2;
			enemy = p1;
		}
		
		bgPanel = _bg;		
		setOpaque(false);
		layout = new SpringLayout();				
		setLayout(layout);				     
		
		createTrainerCard();
		createBattleScene();
		createAttacks();
		createButtons();
		setPositions();		
	}
	
	public void createTrainerCard() {
		//Avatar
		avatarImage = new ImageIcon(getClass().getResource("avatar/" + name + ".gif"));
		avatar = new JLabel(avatarImage);			  
		
		//Quote on trainer card
		int randomNum = (int) (Math.random() * 6);
		String quote = quotes[randomNum];
		String [] editQuote = quote.split(" ");
		quote = "";
		String quote2 = "";
		for (int i = 0; i < editQuote.length; i++) {
			if (i < 7) {
				quote = quote + editQuote[i] + " ";
			}
			else {
				quote2 = quote2 + editQuote[i] + " ";
			}
		}		
		
		text = new JLabel(quote);
		text2 = new JLabel(quote2);
		text.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));		  
		text.setForeground(new Color(255, 255, 255));		
		text2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));		  
		text2.setForeground(new Color(255, 255, 255));		

		//Pokemon name on trainer card
		pokeName = new JLabel(name.toUpperCase());		
		pokeName.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));		  
		pokeName.setForeground(new Color(254, 205, 111));
		
		//pokemon number
		randomNum = 1 + (int) (Math.random() * 151);
		pokeNum = new JLabel(Integer.toString(randomNum));
		pokeNum.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));		  
		pokeNum.setForeground(new Color(254, 205, 111));
		
		//pokemon nature
		randomNum = (int) (Math.random() * 10);
		nature = new JLabel(natureList[randomNum].toUpperCase());
		nature.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));		  
		nature.setForeground(new Color(254, 205, 111));
						
		add(pokeName); add(pokeNum); add(nature);
		add(avatar); add(text); add(text2);	
	}
	
	public void createBattleScene() {
		//opponent pokemon
		opponentImage = new ImageIcon(getClass().getResource("avatar/" + enemy + ".gif"));
		opponent = new JLabel(opponentImage);
		
		//player pokemon
		battleImage = new ImageIcon(getClass().getResource("battle/" + name + ".png"));
		battle = new JLabel(battleImage);
		
		add(opponent); add(battle);
	}
	
	public void createButtons() {
		fight = new JLabel(new ImageIcon(getClass().getResource("buttons/fight.png")));
		fightPressed = new JLabel(new ImageIcon(getClass().getResource("buttons/fight-pressed.png")));
		fightHover = new JLabel(new ImageIcon(getClass().getResource("buttons/fight-hover.png")));	
		fightPressed.setVisible(false);
		fightHover.setVisible(false);
		
		pokemon = new JLabel(new ImageIcon(getClass().getResource("buttons/pokemon.png")));
		pokemonPressed = new JLabel(new ImageIcon(getClass().getResource("buttons/pokemon-pressed.png")));
		pokemonHover = new JLabel(new ImageIcon(getClass().getResource("buttons/pokemon-hover.png")));	
		pokemonPressed.setVisible(false);
		pokemonHover.setVisible(false);
		
		bag = new JLabel(new ImageIcon(getClass().getResource("buttons/bag.png")));
		bagPressed = new JLabel(new ImageIcon(getClass().getResource("buttons/bag-pressed.png")));
		bagHover = new JLabel(new ImageIcon(getClass().getResource("buttons/bag-hover.png")));	 
		bagPressed.setVisible(false);
		bagHover.setVisible(false);
			
		run = new JLabel(new ImageIcon(getClass().getResource("buttons/run.png")));
		runPressed = new JLabel(new ImageIcon(getClass().getResource("buttons/run-pressed.png")));
		runHover = new JLabel(new ImageIcon(getClass().getResource("buttons/run-hover.png")));				
		runPressed.setVisible(false);
		runHover.setVisible(false);
		
		add(fight);	add(fightPressed); add(fightHover);		
		add(pokemon); add(pokemonPressed); add(pokemonHover);		
		add(bag); add(bagPressed);	add(bagHover);		
		add(run); add(runPressed);	add(runHover);				
						
		fight.addMouseListener(new MyMouseListener(fight, fightPressed, fightHover, "fight", this, theInterface));				
		pokemon.addMouseListener(new MyMouseListener(pokemon, pokemonPressed, pokemonHover, "pokemon", this, theInterface));
		bag.addMouseListener(new MyMouseListener(bag, bagPressed, bagHover, "bag", this, theInterface));
		run.addMouseListener(new MyMouseListener(run, runPressed, runHover, "run", this, theInterface));
	}
	
	public void createAttacks() {
		attack1 = new JLabel(new ImageIcon(getClass().getResource("attacks/attack1.png")));
		hoverAttack1 = new JLabel(new ImageIcon(getClass().getResource("attacks/hoverAttack1.png")));
		selectedAttack1 = new JLabel(new ImageIcon(getClass().getResource("attacks/selectedAttack1.png")));
		hoverAttack1.setVisible(false);
		selectedAttack1.setVisible(false);
		
		attack2 = new JLabel(new ImageIcon(getClass().getResource("attacks/attack2.png")));
		hoverAttack2 = new JLabel(new ImageIcon(getClass().getResource("attacks/hoverAttack2.png")));
		selectedAttack2 = new JLabel(new ImageIcon(getClass().getResource("attacks/selectedAttack2.png")));				
		hoverAttack2.setVisible(false);
		selectedAttack2.setVisible(false);
		
		add(attack1); add(hoverAttack1);	add(selectedAttack1);	
		add(attack2); add(hoverAttack2);	add(selectedAttack2);		
		
		attack1.addMouseListener(new MyMouseListener(attack1, selectedAttack1, hoverAttack1, selectedAttack2, "attack1"));
		attack2.addMouseListener(new MyMouseListener(attack2, selectedAttack2, hoverAttack2, selectedAttack1, "attack2"));		
	}
	
	public void setPositions() {
		//battle - player
		layout.putConstraint(SpringLayout.WEST, battle, 50, SpringLayout.WEST, bgPanel);
		layout.putConstraint(SpringLayout.NORTH, battle, 301, SpringLayout.NORTH, bgPanel);
		
		//battle - opponent
		if (enemy.equals("Ash")) {
			JLabel magikarp = new JLabel(new ImageIcon(getClass().getResource("battle/Magikarp.gif")));
			add(magikarp);
			layout.putConstraint(SpringLayout.WEST, opponent, 195, SpringLayout.WEST, bgPanel);
			layout.putConstraint(SpringLayout.NORTH, opponent, 225, SpringLayout.NORTH, bgPanel);
			layout.putConstraint(SpringLayout.NORTH, magikarp, 240, SpringLayout.NORTH, bgPanel);
			layout.putConstraint(SpringLayout.EAST, magikarp, 15, SpringLayout.WEST, opponent);
		}
		else {
			layout.putConstraint(SpringLayout.WEST, opponent, 170, SpringLayout.WEST, bgPanel);
			layout.putConstraint(SpringLayout.NORTH, opponent, 235, SpringLayout.NORTH, bgPanel);
		}
		
		//Avatar
		layout.putConstraint(SpringLayout.WEST, avatar, 198, SpringLayout.WEST, bgPanel);
		layout.putConstraint(SpringLayout.NORTH, avatar, 65, SpringLayout.NORTH, bgPanel);
		
		//Text
		layout.putConstraint(SpringLayout.NORTH, text, 155, SpringLayout.NORTH, bgPanel);	
		layout.putConstraint(SpringLayout.WEST, text, 22, SpringLayout.WEST, bgPanel);	
		
		//Text2
		layout.putConstraint(SpringLayout.NORTH, text2, -2, SpringLayout.SOUTH, text);	
		layout.putConstraint(SpringLayout.WEST, text2, 22, SpringLayout.WEST, bgPanel);		
		
		//pokemon name on trainer card
		layout.putConstraint(SpringLayout.NORTH, pokeName, 55, SpringLayout.NORTH, bgPanel);
		layout.putConstraint(SpringLayout.WEST, pokeName, 75, SpringLayout.WEST, bgPanel);
		
		//pokedex number
		layout.putConstraint(SpringLayout.NORTH, pokeNum, 88, SpringLayout.NORTH, bgPanel);
		layout.putConstraint(SpringLayout.WEST, pokeNum, 125, SpringLayout.WEST, bgPanel);
		
		//nature
		layout.putConstraint(SpringLayout.NORTH, nature, 119, SpringLayout.NORTH, bgPanel);
		layout.putConstraint(SpringLayout.WEST, nature, 90, SpringLayout.WEST, bgPanel);
		
		//attack1
		layout.putConstraint(SpringLayout.NORTH, attack1, 250, SpringLayout.SOUTH, text);
		layout.putConstraint(SpringLayout.WEST, attack1, 30, SpringLayout.WEST, bgPanel);
		
		//selectedAttack1
		layout.putConstraint(SpringLayout.NORTH, selectedAttack1, 250, SpringLayout.SOUTH, text);
		layout.putConstraint(SpringLayout.WEST, selectedAttack1, 30, SpringLayout.WEST, bgPanel);
		
		//hoverAttack1
		layout.putConstraint(SpringLayout.NORTH, hoverAttack1, 250, SpringLayout.SOUTH, text);
		layout.putConstraint(SpringLayout.WEST, hoverAttack1, 30, SpringLayout.WEST, bgPanel);
		
		//attack2
		layout.putConstraint(SpringLayout.NORTH, attack2, 0, SpringLayout.SOUTH, attack1);
		layout.putConstraint(SpringLayout.WEST, attack2, 30, SpringLayout.WEST, bgPanel);
		
		//selectedAttack2
		layout.putConstraint(SpringLayout.NORTH, selectedAttack2, 0, SpringLayout.SOUTH, attack1);
		layout.putConstraint(SpringLayout.WEST, selectedAttack2, 30, SpringLayout.WEST, bgPanel);
		
		//hoverAttack2
		layout.putConstraint(SpringLayout.NORTH, hoverAttack2, 0, SpringLayout.SOUTH, attack1);
		layout.putConstraint(SpringLayout.WEST, hoverAttack2, 30, SpringLayout.WEST, bgPanel);
				
		//fight jlabel image
		layout.putConstraint(SpringLayout.NORTH, fight, 15, SpringLayout.SOUTH, attack2);				
		layout.putConstraint(SpringLayout.WEST, fight, -5, SpringLayout.WEST, bgPanel);	
		
		//fightPressed jlabel image
		layout.putConstraint(SpringLayout.NORTH, fightPressed, 15, SpringLayout.SOUTH, attack2);				
		layout.putConstraint(SpringLayout.WEST, fightPressed, -5, SpringLayout.WEST, bgPanel);	
		
		//fightHover jlabel image
		layout.putConstraint(SpringLayout.NORTH, fightHover, 15, SpringLayout.SOUTH, attack2);				
		layout.putConstraint(SpringLayout.WEST, fightHover, -5, SpringLayout.WEST, bgPanel);
		
		//pokemon jlabel image
		layout.putConstraint(SpringLayout.NORTH, pokemon, 15, SpringLayout.SOUTH, attack2);				
		layout.putConstraint(SpringLayout.EAST, pokemon, 15, SpringLayout.EAST, bgPanel);	
		
		//pokemonPressed jlabel image
		layout.putConstraint(SpringLayout.NORTH, pokemonPressed, 15, SpringLayout.SOUTH, attack2);				
		layout.putConstraint(SpringLayout.EAST, pokemonPressed, 15, SpringLayout.EAST, bgPanel);	
		
		//pokemonHover jlabel image
		layout.putConstraint(SpringLayout.NORTH, pokemonHover, 15, SpringLayout.SOUTH, attack2);				
		layout.putConstraint(SpringLayout.EAST, pokemonHover, 15, SpringLayout.EAST, bgPanel);
						
		//bag jlabel image
		layout.putConstraint(SpringLayout.NORTH, bag, -15, SpringLayout.SOUTH, fight);				
		layout.putConstraint(SpringLayout.WEST, bag, -5, SpringLayout.WEST, bgPanel);	
		
		//bagPressed jlabel image
		layout.putConstraint(SpringLayout.NORTH, bagPressed, -15, SpringLayout.SOUTH, fight);				
		layout.putConstraint(SpringLayout.WEST, bagPressed, -5, SpringLayout.WEST, bgPanel);	
		
		//bagHover jlabel image
		layout.putConstraint(SpringLayout.NORTH, bagHover, -15, SpringLayout.SOUTH, fight);				
		layout.putConstraint(SpringLayout.WEST, bagHover, -5, SpringLayout.WEST, bgPanel);	
			
		//run jlabel image
		layout.putConstraint(SpringLayout.NORTH, run, -15, SpringLayout.SOUTH, pokemon);				
		layout.putConstraint(SpringLayout.EAST, run, 15, SpringLayout.EAST, bgPanel);	
		
		//runPressed jlabel image
		layout.putConstraint(SpringLayout.NORTH, runPressed, -15, SpringLayout.SOUTH, pokemon);				
		layout.putConstraint(SpringLayout.EAST, runPressed, 15, SpringLayout.EAST, bgPanel);	
		
		//runHover jlabel image
		layout.putConstraint(SpringLayout.NORTH, runHover, -15, SpringLayout.SOUTH, pokemon);				
		layout.putConstraint(SpringLayout.EAST, runHover, 15, SpringLayout.EAST, bgPanel);
	}
}
