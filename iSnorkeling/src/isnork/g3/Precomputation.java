package isnork.g3;

public interface Precomputation {

	//ratio of the happiness from the highest-value creature to the median-value one
	public abstract double topToMedianHappinessRatio();

	//number of creatures per square
	public abstract double creatureDensity();

	//ratio of the number of dangerous creatures to the number of total creatures
	public abstract double dangerousToTotalRatio();

	//ratio of the # of moving dangerous creatures to the # of stationary dangerous creatures 
	public abstract double movingToStationaryDangerousRatio();

	//maximum score for a diver assuming a diver sees every single creature, with no danger penalties 
	public abstract int naiveHighScore();

}