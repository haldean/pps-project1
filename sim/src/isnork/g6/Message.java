package isnork.g6;

import isnork.sim.GameObject.Direction;
import isnork.sim.GameEngine;
import isnork.sim.SeaLifePrototype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;

class QueueElement{
	String message;
	int time;
	int happiness;
	Direction creatureMovement;
}

public class Message {

	ArrayList<QueueElement> messageQueue = new ArrayList<QueueElement>();
	public static HashMap<String, String> charMap = new HashMap<String, String>();
	public static HashMap<String, Integer> creatureHappiness = new HashMap<String, Integer>();
	
	private static int tick = -1; //even ticks will have the creature names and odd will have their direction. 
	
	private Logger log = Logger.getLogger(this.getClass());

	public static String[] alphabet = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
	
	private int nullMessageCount = 0;
	
	/*constructs the message and adds it a good location in the message 
	 * queue 
	 * importance of message = happiness / age 
	 * more happiness = > more important.
	 * more age => less important
	 * 
	 */
	public void addMessageToQueue(String message, int happiness, Direction dir) {
		//construct the message
		QueueElement q = new QueueElement();
		String a = getLetter(message);
		if( a == null) //nothing to add. The simulator shouldnt be doing this, but am putting in a guard here. 
			return;
		//log.info("The creature we see want to add to queue" + message + " and message : " + a);
		q.message = a;
		q.time = InitialPlayer.getCurrentTime();
		q.happiness = happiness;
		int qimportance = q.happiness / 1; //age is 0 for now.
		q.creatureMovement = dir; //add direction to the message queue. (Send it out as two messages though.)
		int index = 0;
		int happy, age, importance, maxImportance = 0;
		for(int i = 0; i < messageQueue.size(); i++) {
			age = InitialPlayer.getCurrentTime() - messageQueue.get(i).time + 1;
			happy = messageQueue.get(i).happiness;
			importance = happy / age ;
			if(importance < qimportance) { //take first available position and get out.
				index = i; 
				break;
			}
		}
		messageQueue.add(index,q);
		//log.info(" message " + q.message + " added at position " + index);
	}
	
	/* removes element from queue and returns the value of what was there
	 * Sends two messages for each element in the queue. First is the creature itself and the other is the direction
	 * of its movement.
	 */
	public String getMessage(double happiness) {
		
		String message;
		tick++;
		if(messageQueue.isEmpty()) {
			message = "y"; //hopefully there will be no interference.
		}
		//if player has obtained max happiness, get send "" so that they can all get back on the boat.
		// After player sends it 3 times, it gets back to sending other messages. 
		else if( (int) happiness >= Utilities.getMaxHappiness() && nullMessageCount < 3) {
			nullMessageCount++;
			log.info(" saw maximum possible happiness : ");
			message = "z"; //hope for noninterference
		} else {
			QueueElement q = messageQueue.get(0);
			if(tick % 2 == 0) {
				message = q.message;
				log.info(" tick % 2 = 0, creature name message : " + message + " " +  tick);
			}else {
				log.info(" direction : " + q.creatureMovement +  " int for this direction " + Explorer.convertDirectionToInt(q.creatureMovement) + " tick = " + tick);
				message = Utilities.map1toa(Explorer.convertDirectionToInt(q.creatureMovement));
				messageQueue.remove(0);
			}
					
		}
		return message;
	}
	
	/*each name gets a different letter */
	public static void setVocabulary(Set<SeaLifePrototype> seaLifePossibilities) {
		int count = 0;
		for(SeaLifePrototype s : seaLifePossibilities) {
			if(count < 26 ) {
				charMap.put(s.getName(), alphabet[count]);
				creatureHappiness.put(alphabet[count],s.getHappiness());
				GameEngine.println(" creature Happiness " + alphabet[count] + " " + s.getHappiness());
				GameEngine.println("charMap entry " + s.getName() + " " + alphabet[count]);
				count++;
				
			}
		}
	}
	
	public static String getLetter(String creatureName) {
		return charMap.get(creatureName);
	}
	
	public static Integer getHappiness(String letter) {
		if( letter == null || letter.equals("")) return 0;
		if(creatureHappiness.containsKey(letter)) {
			return creatureHappiness.get(letter);
		}
		return 0;
	}
}
