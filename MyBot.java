package bots;
import java.util.List;
import pirates.game.Direction;
import pirates.game.Location;
import pirates.game.Treasure;
import pirates.game.Pirate;
import pirates.game.PirateBot;
import pirates.game.PirateGame;

public class MyBot implements PirateBot {
    
	private List<Pirate> availablePirates; // the pirates that haven't done their turn yet.
	private List<Location>usedLocations; // places on the map where our ships are going to be in after the turn(this is in order to prevent bumping 2 ships).
	private int remainingActions; // so we can know how many more moves we have.
	private List<Treasure> availableTreasures; // so we don't send 2 ships to the same treasure. everytime 1 ship gets a target it is removed from this list.
	private List<Pirate> attackedPirates; // a list of pirates we are planning to attack. made to prevent multiple ships from attacking the same enemy.
	
	
    public void doTurn(PirateGame game) {
    	availableTreasures = game.treasures();
    	remainingActions = game.getActionsPerTurn();
    	
    	/*
    	 The basic plan is to prioritize bringing ships with treasures to their initial location,
    	 then have all ships that are able to attack, attack ships in their radius(prioritizing ships with
    	 treasures in case of multiple ships in range) and finally move all ships that don't have treasures
    	 or can't attack.
    	 */
    	for(Pirate p: game.mySoberPirates()){
    	movePirateHome(p);//will move pirates with treasure to their initial location
    	if(!attack(p))// a function that serves to attack with pirates that can
    		getTreasure(p);//moves with the pirates left that couldn't either attack or have treasures
    	}
    }


	private void getTreasure(Pirate p) {
		// move the pirate to its closest treasure
		
	}


	private boolean attack(Pirate p) {
		//attack with the pirate
		return false;
	}


	private void movePirateHome(Pirate p) {
		//move the pirate to its initial location
		
	}
	}
