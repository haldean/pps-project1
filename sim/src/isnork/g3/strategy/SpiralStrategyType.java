package isnork.g3.strategy;

public class SpiralStrategyType
{
	private boolean isDangerous=false;
	private boolean isRepeat=true;
	private int radius=0;
	
	public SpiralStrategyType(boolean isDangerous)
	{
		this.isDangerous=isDangerous;
	}
	
	public SpiralStrategyType(boolean isDangerous, boolean isRepeat, int radius)
	{
		this.isDangerous=isDangerous;
		this.isRepeat=isRepeat;
		this.radius=radius;
	}
	
	public int getRadius()
	{
		return radius;
	}
	
	public void setRadius(int radius)
	{
		this.radius=radius;
	}
	
	public boolean isDangerous()
	{
		return isDangerous;
	}
}
