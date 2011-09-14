package isnork.g3.strategy;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LineStrategy
{
	private int d=-1, r=-1, n=-1;
	private int pathNum;
	private int interval;
	private LineStrategyPlayer[] player;
	private static final Logger log=Logger.getLogger(LineStrategy.class);

	public LineStrategy(int d, int r, int n)
	{
		this.d=d;
		this.r=r;
		this.n=n;
		player=new LineStrategyPlayer[n];
		int cornerOffset=upperRound((1-1/Math.sqrt(2))*r);
		getPathNum();
		int startBlank=((d*2+1)-(interval*(pathNum-1)+1))/2;
		for (int playerId=0; playerId<n;)
		{
			for (int pathId=0; pathId<pathNum && playerId<n; pathId++)
			{
				player[playerId]=new LineStrategyPlayer(playerId, pathId);
				int offset=r-startBlank;
				log.debug("offset="+offset+" interval="+interval+" pathNum="
					+pathNum);
				int x=startBlank+pathId*interval-d;
				if (x<0)
					x+=offset;
				else if (x>0)
					x-=offset;
				player[playerId].setX(x);
				if (playerId==0) // visit corner
				{
					player[playerId].addNode(new Point2D.Double(x, d-r));
					player[playerId].addNode(new Point2D.Double(x-1, d-r+cornerOffset));
					player[playerId].addNode(new Point2D.Double(x, d-r));
					player[playerId].addNode(new Point2D.Double(x, 0));
					player[playerId].addNode(new Point2D.Double(x, -d+r));
					player[playerId].addNode(new Point2D.Double(x-1, -d+r-cornerOffset));
					player[playerId].addNode(new Point2D.Double(x, -d+r));
					player[playerId].addNode(new Point2D.Double(x, 0));
				}
				else if (playerId==pathNum-1) // visit corner
				{
					player[playerId].addNode(new Point2D.Double(x, d-r));
					player[playerId].addNode(new Point2D.Double(x+1, d-r+cornerOffset));
					player[playerId].addNode(new Point2D.Double(x, d-r));
					player[playerId].addNode(new Point2D.Double(x, 0));
					player[playerId].addNode(new Point2D.Double(x, -d+r));
					player[playerId].addNode(new Point2D.Double(x+1, -d+r-cornerOffset));
					player[playerId].addNode(new Point2D.Double(x, -d+r));
					player[playerId].addNode(new Point2D.Double(x, 0));
				}
				else
				{
					player[playerId].addNode(new Point2D.Double(x, d-r));
					player[playerId].addNode(new Point2D.Double(x, 0));
					player[playerId].addNode(new Point2D.Double(x, -d+r));
					player[playerId].addNode(new Point2D.Double(x, 0));
				}				
				playerId++;
			}
		}
	}

	public void getPathNum()
	{
		int width=(d*2+1);
		interval=r*2+1;
		pathNum=upperRound(width/interval);

		for (; interval>r&&n>pathNum; interval--)
		{
			pathNum=upperRound(width/interval);
		}
		interval++;
		pathNum=upperRound(width/interval);
	}

	public int upperRound(double d)
	{
		return (int)(d+0.5);
	}

	public LineStrategyPlayer getPlayer(int i)
	{
		return player[i];
	}

	public static void main(String[] args)
	{
		PropertyConfigurator.configure("logger.properties");
		LineStrategy strategy=new LineStrategy(10, 3, 10);
		for (int playerId=0; playerId<strategy.player.length; playerId++)
		{
			LineStrategyPlayer player=strategy.getPlayer(playerId);
			ArrayList<Point2D> node=player.getNode();
			log.debug("playerId="+player.getPathId()+" pathId="
				+player.getPathId()+" x="+player.getX());
			for (Point2D p : node)
				log.debug(p);
		}
	}
}
