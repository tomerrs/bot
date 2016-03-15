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
    	/* this is horrible practice, we couldnt get initializing an empty array list to work @ptzafrir */
    	
	attackedPirates = game.allEnemyPirates();
        attackedPirates.clear();
        
        /* this has to be initiallized empty at the start (line 16) */
        
    	availableTreasures = game.treasures();
    	remainingActions = game.getActionsPerTurn();
    	availablePirates = game.mySoberPirates();
    	/*
    	 The basic plan is to prioritize bringing ships with treasures to their initial location,
    	 then have all ships that are able to attack, attack ships in their radius(prioritizing ships with
    	 treasures in case of multiple ships in range) and finally move all ships that don't have treasures
    	 or can't attack.
    	 */
    	for(Pirate p1: game.myPiratesWithTreasures()){
    		if(remainingActions < p1.getCarryTreasureSpeed())
    			return;
    		movePirateHome(game, p1);//will move pirates with treasure to their initial location.
    	}
    	for(Pirate p2: availablePirates){
    		if(remainingActions < 1)
    			return;
    	if(!attack(game, p2))// a function that serves to attack with pirates that can.
    		moveToClosestTreasure(game, p2);//moves with the pirates left that couldn't either attack or have treasures.
    	}
    	
    }


	private void moveToClosestTreasure(PirateGame game,Pirate p) {
		// move the pirate to its closest treasure.
		
	}


	private boolean attack(PirateGame game,Pirate p) {
		//attack with the pirate.
		Pirate target = null;
		for(Pirate p1: game.allEnemyPirates()){ // p1 = enemy.
			if(attackedPirates.contains(p1)) // checking if current enemy was attacked or not
				continue;
			if(game.inRange(p, p1) && p1.getTurnsToSober() < 5){ // checks if enemy is in range.
					if(p1.hasTreasure()){ // checks if enemy has treasure.
						game.attack(p, p1); // attack the enemy.
						remainingActions--; // amount of moves decreased by 1.
						availablePirates.remove(p); // this pirate isn't usable anymore.
						attackedPirates.add(p1); // the enemy is attacked and therefore shouldn't be attacked again.
						return true; // successfully attacked.	
					}
					target = p1;
			}
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
		int maxSpeed = p.getCarryTreasureSpeed();
		Location home = p.getInitialLocation();
		for(Location l: usedLocations)
			if(l.equals(game.getSailOptions(p, home, maxSpeed).get(0)))
				return;
		game.setSail(p, game.getSailOptions(p, home, maxSpeed).get(0));
		remainingActions -= p.getCarryTreasureSpeed();
		availablePirates.remove(p);
		usedLocations.add(game.getSailOptions(p, home, maxSpeed).get(0));
	}
	}
