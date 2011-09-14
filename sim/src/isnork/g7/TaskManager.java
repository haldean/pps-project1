package isnork.g7;

import isnork.sim.Observation;
import isnork.sim.SeaLifePrototype;
import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;

import org.apache.log4j.Logger;


/*TODO: STILL NEED TO MARK TASKS INVALID AFTER GETTING MESSAGE FROM FOLLOWING PLAYER THAT STOPS FOLLOWING THE CREATURE*/

public class TaskManager {
	private PriorityQueue<Task> taskList; 
	private HashMap<Task, Boolean> seenObjects; 
	//Hash of the SeaLife creature name Strings mapping to the number of times it was seen
	//by the diver using this class
	public HashMap<String, HashSet<Integer>> seenCreatures;
	public HashMap<String, Integer> chasedCreatures;
	private OurBoard ourBoard;
	private Set<SeaLifePrototype> seaLifePossibilities;
	private Set<Observation> playerLocations;
	
	private static final Logger logger = Logger.getLogger(TaskManager.class);

	public TaskManager(Set<SeaLifePrototype> seaLifePossibilities, OurBoard ourBoard){
		taskList = new PriorityQueue<Task>(10, new TaskComparator());
		seenObjects = new HashMap<Task, Boolean>();
		this.seaLifePossibilities = seaLifePossibilities;
		this.ourBoard = ourBoard;
		
		seenCreatures = new HashMap<String, HashSet<Integer>>();
		chasedCreatures = new HashMap<String, Integer>();
		
		for(SeaLifePrototype s: seaLifePossibilities){
			seenCreatures.put(s.getName(), new HashSet<Integer>());
		}
	}
	
	public void setPlayerLocations(Set<Observation> playerLocations){
		this.playerLocations = playerLocations;
	}

	
	public static class TaskComparator implements Comparator<Task>
    {
        @SuppressWarnings("unchecked")
		public int compare (Task o1, Task o2)
        {
            return ((Comparable<Task>) o1).compareTo(o2);
        }
    }
	
	/*To add tasks with a player associated (a player traveling a moving creature)*/
	public void addTask(String creatureName, int playerID){
		if ((seenCreatures.get(creatureName)).size() > 0 || numTasks(creatureName) > 1)
			return;
		
		Task task = new Task(creatureName, playerID, ourBoard, seaLifePossibilities, playerLocations);

		chasedCreatures.put(creatureName,1);
		taskList.add(task);
		seenObjects.put(task, false);
	}
	
	public int numTasks(String s)
	{
		int count = 0;
		for(Task t: taskList)
		{
			if(t.toString().equalsIgnoreCase(s))
				count++;
		}
		return count;
	}
	
	/*To add tasks with static creature and fixed position associated*/
	public void addTask(String creatureName, Point2D coordinate){
		if ((seenCreatures.get(creatureName)).size() > 0 || numTasks(creatureName) > 1)
		{
			logger.debug("num creatures with name " + creatureName + " seen: " + seenCreatures.get(creatureName).size());
			return;
		}
		
		Task task = new Task(creatureName, coordinate, ourBoard, seaLifePossibilities, playerLocations);
		//task.getObservation().setLocation(coordinate);
		chasedCreatures.put(creatureName,1);
		taskList.add(task);
		seenObjects.put(task, false);
	}
	
	
	/*Will return null if there are no valid remaining tasks in the queue*/
	public Task getNextTask(Point2D myCurrentLocation){
		updatePriorityScores(myCurrentLocation);
		
		Iterator<Task> taskIterator = taskList.iterator();
		
		Task nextTask;

		if(taskIterator.hasNext()){
			nextTask = taskIterator.next();
		
			while(!nextTask.getObservation().isValid() || !taskUnseen(nextTask)){
				
				if(taskIterator.hasNext()){
					nextTask = taskIterator.next();
				}
				else 
					break;
			}
			
			if(nextTask != null){
//				markTaskComplete(nextTask);
				return nextTask;
			}
		}
		
		return null;
	}
	
	private boolean taskUnseen(Task task){
		return seenObjects.get(task);
	}
	
	
	/*This isn't getting used yet*/
	public void markTaskInvalid(Task task){
		task.getObservation().setInvalid();
	}
	
	public void markTaskComplete(Task task){
		task.getObservation().setInvalid();
		markCreatureSeen(task.getObservation().getCreatureName(), task.getObservation().getId());
		Iterator<Task> it = taskList.iterator();
		while(it.hasNext())
		{
			Task t = it.next();
			if(t.toString().equalsIgnoreCase(task.toString()))
				it.remove();
		}
	}
	
	private void markCreatureSeen(String creatureName, int id){
		if (!seenCreatures.containsKey(creatureName)) {
			seenCreatures.put(creatureName, new HashSet<Integer>());
		}
		seenCreatures.get(creatureName).add(id);
	}
	
	private void updatePriorityScores(Point2D myCurrentLocation){
		Iterator<Task> taskIterator = taskList.iterator();
		
		Task nextTask;
		boolean hasNextTask = taskIterator.hasNext();
		
		while (hasNextTask){
			nextTask = taskIterator.next();
			nextTask.updatePriorityScore(myCurrentLocation, seenCreatures);
			hasNextTask = taskIterator.hasNext();	
		}
	}
	
	public void updatePlayerLocations(Set<Observation> playerLocations){
		for(Task task : taskList){
			task.updatePlayerLocations(playerLocations);
		}
	}
	
	public void updateSeenCreatures(Set<Observation> creatures)
	{
		for(Observation o: creatures)
		{
			if(!o.getName().equalsIgnoreCase("bp consultant"))
				seenCreatures.get(o.getName()).add(new Integer(o.getId()));
		}
	}
	
	public void printTaskList(){
		Iterator<Task> taskIterator = taskList.iterator();
		int counter = 0;
		
		while(taskIterator.hasNext()){
			Task nextTask = taskIterator.next();
			//if(nextTask.getObservation().happiness() > 0)
			logger.debug(counter++ + " " + nextTask);
		}
	}

	public void cleanTasks(Task task) 
	{
		Iterator<Task> it = taskList.iterator();
		while(it.hasNext())
		{
			Task t = it.next();
			if(t.toString().equalsIgnoreCase(task.toString()))
				it.remove();
		}
	}

	public void cleanTasks(String name, int sender) 
	{
		Iterator<Task> it = taskList.iterator();
		while(it.hasNext())
		{
			Task t = it.next();
			//logger.debug("CLEANING TASK");
			//logger.debug(t.getObservation().getTheLocation().getPlayerID());
			//logger.debug(sender);
			//logger.debug(name);
			//logger.debug(t.toString());
			if(t.toString().equalsIgnoreCase(name) && sender == t.getObservation().getTheLocation().getPlayerID())
				it.remove();
		}
	}
}
