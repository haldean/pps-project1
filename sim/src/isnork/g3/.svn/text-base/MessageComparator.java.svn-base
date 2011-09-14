package isnork.g3;

import isnork.sim.SeaLifePrototype;
import isnork.sim.iSnorkMessage;

import java.util.Comparator;
import java.util.Set;

public class MessageComparator implements Comparator<iSnorkMessage> {

	private Set<SeaLifePrototype> seaLifePossibilities=null;
	public MessageComparator(Set<SeaLifePrototype> seaLifePossibilities){
		this.seaLifePossibilities=seaLifePossibilities;
	}
	
	public int getScore(String creatName) {
		for(SeaLifePrototype slp: seaLifePossibilities) {
			if(creatName != null)
			{
				if(slp.getName() != null)
				{
					if(slp.getName().substring(0, 1).equals(creatName))
					{
						return slp.getHappiness();
					}
				}
			}
		}
		return -1;
	}
	
    public int compare(iSnorkMessage slp1, iSnorkMessage slp2) {

        if (this.getScore(slp1.getMsg())> this.getScore(slp2.getMsg()))
            return -1;
        else  if (this.getScore(slp1.getMsg())< this.getScore(slp2.getMsg()))
            return 1;
        else
            return 0;
    }

}
