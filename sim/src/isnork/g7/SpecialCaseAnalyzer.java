package isnork.g7;

import isnork.sim.SeaLifePrototype;

import java.util.Set;

import org.apache.log4j.Logger;


public class SpecialCaseAnalyzer {
	
	public static final String HIGH_DANGER_STRING = "Extreme danger";
	public static final String MED_DANGER_STRING = "Medium danger";
	public static final String LOW_DANGER_STRING = "Low danger";
	public static final String VERY_LOW_DANGER_STRING = "Very low danger";
	
	private static final int HIGH_DANGER_THRESHOLD = 9000;
	private static final int MED_DANGER_THRESHOLD = 2000;
	private static final int LOW_DANGER_THRESHOLD = 500;
	
	private static final int NUM_DANGEROUS_CREATURES_THRESH = 20;

//	
//	private static final int HIGH_DANGER_THRESHOLD = 17;
//	private static final int MED_DANGER_THRESHOLD = 10;
//	private static final int LOW_DANGER_THRESHOLD = 5;


	private static final Logger logger = Logger.getLogger(SpecialCaseAnalyzer.class);
	
	public String detectDangerousMap(Set<SeaLifePrototype> seaLifePossibilities){
		int boardDangerTotal = 0;
		int numDangerousCreatures = 0;
		
		for (SeaLifePrototype life : seaLifePossibilities) {
			int avgCount = (int) Math.ceil(( life.getMinCount() + life.getMaxCount() ) / 2.0);
			
			if (life.isDangerous() && life.getSpeed() > 0){
				boardDangerTotal += avgCount * (life.getHappiness()*2);
				numDangerousCreatures += avgCount;
				//boardDangerTotal += avgCount;
			}
		}
		
		if (boardDangerTotal > HIGH_DANGER_THRESHOLD || numDangerousCreatures >= NUM_DANGEROUS_CREATURES_THRESH){
			return HIGH_DANGER_STRING;
		}
		else if (boardDangerTotal > MED_DANGER_THRESHOLD){
			return MED_DANGER_STRING;
		}
		else if (boardDangerTotal > LOW_DANGER_THRESHOLD){
			return LOW_DANGER_STRING;
		}
		else{
			return VERY_LOW_DANGER_STRING;
		}
	}
	

	
}
