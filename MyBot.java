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
	private List<Treasure> availableTreasures; // so we don't send 2 ships to the same treasure. every time 1 ship gets a target it is removed from this list.
	private List<Pirate> attackedPirates; // a list of pirates we are planning to attack. made to prevent multiple ships from attacking the same enemy.
	
	
    public void doTurn(PirateGame game) {
    	availableTreasures = game.treasures();
    	remainingActions = game.getActionsPerTurn();
    	availablePirates = game.mySoberPirates();
    	/*
    	 The basic plan is to prioritize bringing ships with treasures to their initial location,
    	 then have all ships that are able to attack, attack ships in their radius(prioritizing ships with
    	 treasures in case of multiple ships in range) and finally move all ships that don't have treasures
    	 or can't attack.
    	 */
    	for(Pirate p1: game.myPiratesWithTreasures())
    		movePirateHome(game, p1);//will move pirates with treasure to their initial location.
    	
    	for(Pirate p2: availablePirates){
    	if(!attack(game, p2))// a function that serves to attack with pirates that can.
    		getTreasure(game, p2);//moves with the pirates left that couldn't either attack or have treasures.
    	}
    	
    }


	private void getTreasure(PirateGame game,Pirate p) {
		// move the pirate to its closest treasure.
		
	}


	private boolean attack(PirateGame game,Pirate p) {
		//attack with the pirate.
		Pirate target = null;
		for(Pirate p1: game.allEnemyPirates()){ // p1 = enemy.
			if(game.inRange(p, p1)) // checks if enemy is in range.
					if(p1.hasTreasure()){ // checks if enemy has treasure.
						for(Pirate temp: attackedPirates){	
							if(p1.equals(temp)) // if enemy was already attacked, break from the loop AKA don't attack a drunk pirate.
								break;
							if(!p1.equals(temp) && temp.equals(attackedPirates.get(attackedPirates.size()-1))){ // if enemy wasn't attacked and is the last enemy in the list, attack him.
						game.attack(p, p1); // attack the enemy.
						remainingActions--; // amount of moves decreased by 1.
						availablePirates.remove(p); // this pirate isn't usable anymore.
						attackedPirates.add(p1); // the enemy is attacked and therefore shouldn't be attacked again.
						return true; // successfully attacked.
							}
						}
					}
				target = p1;
		}
		if(target != null){
			game.attack(p, target); // attack the enemy.
			remainingActions--; // amount of moves decreased by 1.
			availablePirates.remove(p); // this pirate isn't usable anymore.
			attackedPirates.add(target); // the enemy is attacked and therefore shouldn't be attacked again.
			return true; // successfully attacked.
		}
		return false;
	}


	private void movePirateHome(PirateGame game,Pirate p) {
		//move the pirate to its initial location.
		for(Location l: usedLocations)
			if(l.equals(game.getSailOptions(p, p.getInitialLocation(), 1).get(0)))
				return;
		game.setSail(p, game.getSailOptions(p, p.getInitialLocation(), 1).get(0));
		remainingActions--;
		availablePirates.remove(p);
		usedLocations.add(game.getSailOptions(p, p.getInitialLocation(), 1).get(0));
	}
	}