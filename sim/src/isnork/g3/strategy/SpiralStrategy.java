package isnork.g3.strategy;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class SpiralStrategy
{
	private int d=-1, r=-1, n=-1;
	private SpiralStrategyType spiralStrategyType;
	private int pathNum;
	private int interval;
	private SpiralStrategyPlayer[] player;
	double dangerousLimit=12/Math.pow(20*2+1, 2);
	
	private static final Logger log=Logger.getLogger(SpiralStrategy.class);

	public SpiralStrategy(int d, int r, int n)
	{
		this.d=d;
		this.r=r;
		this.n=n;
		this.spiralStrategyType=new SpiralStrategyType(false);
		player=new SpiralStrategyPlayer[n];

		initParam();
		buildPath();
	}
	
	public SpiralStrategy(int d, int r, int n, SpiralStrategyType spiralStrategyType)
	{
		this.d=d;
		this.r=r;
		this.n=n;
		this.spiralStrategyType=spiralStrategyType;
		player=new SpiralStrategyPlayer[n];

		initParam();
		buildPath();
	}

	public void initParam()
	{
		if (spiralStrategyType.isDangerous())
		{
			pathNum=1;
			interval=0;
			return;
		}
		double width=d;
		int minInterval=r*2+1;
		int maxInterval=r*2+1;
		interval=maxInterval;
		pathNum=upperRound(width/interval);

		for (; interval>=minInterval && n>pathNum; interval--)
		{
			pathNum=upperRound(width/interval);
		}
		interval++;
		pathNum=upperRound(width/interval);
	}
	
	public void buildPath()
	{
		if (spiralStrategyType.isDangerous())
		{
			for (int i=0; i<n; i++)
			{
				int playerId=i;
				int pathId=0;
				int pos=Math.min(spiralStrategyType.getRadius(), r+1);
				player[playerId]=new SpiralStrategyPlayer(playerId, pathId);
				player[playerId].setPos(pos);
				player[playerId].setInitNode(buildInitNode(pathId, i));
				player[playerId].setNode(buildOneLevelNode(pos));
				player[playerId].buildNodeBasedOnInitAndStandard();
			}
			return;
		}
		int currentPlayerId=0;
		for (int pathId=0; pathId<pathNum; pathId++)
		{
			int playerNumAtPath=getPlayerNumByPath(pathId);
			for (int i=0; i<playerNumAtPath; i++)
			{
				int playerId=i+currentPlayerId;
				int pos=getPosByPath(pathId);
				player[playerId]=new SpiralStrategyPlayer(playerId, pathId);
				player[playerId].setPos(pos);
				player[playerId].setInitNode(buildInitNode(pathId, i));
				
				if (pathId==pathNum-1 && i==0 && r<Math.sqrt(2)*(d-pos))
				{
					int cornerOffset=upperRound((1-1/Math.sqrt(2))*(d-pos));
					player[playerId].setNode(buildCornerNode(cornerOffset));
					player[playerId].buildNodeBasedOnInitAndCorner();
				}
				else
				{
					player[playerId].setNode(buildStandardNode());
					player[playerId].buildNodeBasedOnInitAndStandard();
				}
			}
			currentPlayerId+=playerNumAtPath;
		}
	}
	
	public int getPosByPath(int path)
	{
		if (spiralStrategyType.isDangerous())
			return spiralStrategyType.getRadius();
		int gap=d-r-interval*(pathNum-1);
		int offset=(interval-gap)/2;
		if (path==0 && r<=d/2)
			return r+1;
		else if (path==0 && r>d/2)
			return d/2+1;
		else if (path==pathNum-1)
			return d-r;
		else
			return path*interval+getPosByPath(0)-offset;
	}
	
	public int getPlayerNumByPath(int path)
	{
		if (spiralStrategyType.isDangerous())
			return n;
		
		float sum=0;
		for (int i=0; i<pathNum; i++)
			sum+=getPosByPath(i);
		
		int playerLeft=n;
		for (int i=0; i<path; i++)
			playerLeft-=getPlayerNumByPath(i);		
		if (playerLeft==0)
			return 0;
		
		if (path!=pathNum-1)
			return Math.min(playerLeft, Math.max(1, Math.round(getPosByPath(path)/sum*n)));
		else
		{
			int sumExceptLast=0;
			for (int i=0; i<pathNum-1; i++)
				sumExceptLast+=getPlayerNumByPath(i);
			return (int)n-sumExceptLast;
		}
	}
	
	double getAngle(int path, int index)
	{
		int playerNum=getPlayerNumByPath(path);
		return 2*Math.PI*index/playerNum;
	}
	
	public Point2D buildInitNode(int path, int index)
	{
		int pos=getPosByPath(path);
		double angle=getAngle(path, index);
		//log.debug("init: pos="+pos);
		//log.debug("init: angle="+angle);
		if ((angle>=0 && angle<Math.PI/4) || (angle>=Math.PI*7/4 && angle<Math.PI*2))
			return new Point2D.Double(pos, -Math.round(Math.tan(angle)*pos));
		else if (angle>=Math.PI/4 && angle<Math.PI*3/4)
			return new Point2D.Double(Math.round((1.0/Math.tan(angle)*pos)), pos);
		else if (angle>=Math.PI*3/4 && angle<Math.PI*5/4)
			return new Point2D.Double(-pos, -Math.round(Math.tan(angle)*pos));
		else if (angle>=Math.PI*5/4 && angle<Math.PI*7/4)
			return new Point2D.Double(-Math.round(1.0/Math.tan(angle)*pos), -pos);
		return null;
	}
	
	public ArrayList<Point2D> buildStandardNode(ArrayList<Point2D> node, int pos)
	{
		node.add(new Point2D.Double(pos, 0));
		node.add(new Point2D.Double(pos, -pos));
		node.add(new Point2D.Double(0, -pos));
		node.add(new Point2D.Double(-pos, -pos));
		node.add(new Point2D.Double(-pos, 0));
		node.add(new Point2D.Double(-pos, pos));
		node.add(new Point2D.Double(0, pos));
		node.add(new Point2D.Double(pos, pos));
		
		node.add(new Point2D.Double(pos, 0));
		return node;
	}
	
	public ArrayList<Point2D> buildOneLevelNode(int pos)
	{
		ArrayList<Point2D> node=new ArrayList<Point2D>();
		node.add(new Point2D.Double(pos, 0));
		node.add(new Point2D.Double(pos, -pos));
		node.add(new Point2D.Double(0, -pos));
		node.add(new Point2D.Double(-pos, -pos));
		node.add(new Point2D.Double(-pos, 0));
		node.add(new Point2D.Double(-pos, pos));
		node.add(new Point2D.Double(0, pos));
		node.add(new Point2D.Double(pos, pos));
		return node;
	}
	
	public ArrayList<Point2D> buildStandardNode()
	{
		ArrayList<Point2D> node=new ArrayList<Point2D>();
		for (int pathId=0; pathId<pathNum; pathId++)
			node=buildStandardNode(node, getPosByPath(pathId));
		for (int pathId=pathNum-2; pathId>0; pathId--)
			node=buildStandardNode(node, getPosByPath(pathId));
		return node;
	}
	
	public ArrayList<Point2D> buildCornerNode(int offset)
	{
		ArrayList<Point2D> node=new ArrayList<Point2D>();
		for (int pathId=0; pathId<pathNum-1; pathId++)
			node=buildStandardNode(node, getPosByPath(pathId));
		// corner case
		int pos=getPosByPath(pathNum-1);
		node.add(new Point2D.Double(pos, 0));
		node.add(new Point2D.Double(pos, -pos));
		node.add(new Point2D.Double(pos+offset, -pos-offset));
		node.add(new Point2D.Double(pos, -pos));
		node.add(new Point2D.Double(0, -pos));
		node.add(new Point2D.Double(-pos, -pos));
		node.add(new Point2D.Double(-pos-offset, -pos-offset));
		node.add(new Point2D.Double(-pos, -pos));
		node.add(new Point2D.Double(-pos, 0));
		node.add(new Point2D.Double(-pos, pos));
		node.add(new Point2D.Double(-pos-offset, pos+offset));
		node.add(new Point2D.Double(-pos, pos));
		node.add(new Point2D.Double(0, pos));
		node.add(new Point2D.Double(pos, pos));
		node.add(new Point2D.Double(pos+offset, pos+offset));
		node.add(new Point2D.Double(pos, pos));
		
		node.add(new Point2D.Double(pos, 0));
		
		for (int pathId=pathNum-2; pathId>0; pathId--)
			node=buildStandardNode(node, getPosByPath(pathId));
		return node;
	}
	
	public int upperRound(double d)
	{
		if (d>(int)d)
			return (int)(d+1);
		return (int)d;
	}

	public SpiralStrategyPlayer getPlayer(int i)
	{
		return player[i];
	}

	public static void main(String[] args)
	{
		PropertyConfigurator.configure("logger.properties");
		SpiralStrategy strategy=new SpiralStrategy(40, 5, 3, new SpiralStrategyType(true, true, 5));
		log.debug(strategy.player.length);
		for (int playerId=0; playerId<strategy.player.length; playerId++)
		{
			SpiralStrategyPlayer player=strategy.getPlayer(playerId);
			ArrayList<Point2D> node=player.getNode();
			log.debug("playerId="+player.getPlayerId()+" pathId="
				+player.getPathId()+" x="+player.getX()
				+" initNode="+player.getInitNode());
			for (Point2D p : node)
				log.debug(p);
		}
	}
}
