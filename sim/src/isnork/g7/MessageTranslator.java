package isnork.g7;

import isnork.sim.SeaLifePrototype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class MessageTranslator {

	public static HashMap<String, SeaLifePrototype> hm = null;
	private static ArrayList<SeaLifePrototype> sl;
	public static void initializeMap(Set<SeaLifePrototype> slp)
	{
		sl = new ArrayList<SeaLifePrototype>(slp);
		hm = new HashMap<String, SeaLifePrototype>();
		Collections.sort(sl, new SeaLifeComparator());
		for(int i = 0;i<sl.size();i++)
		{
			hm.put(new Character((char)(i+97)).toString(),sl.get(i));
		}
		Iterator<String> it = hm.keySet().iterator();
		while(it.hasNext())
		{
			String a = it.next();
		}
	}	
	
	public static String getMessage(String name)
	{
		for(int i =0; i<sl.size();i++)
		{
			if(sl.get(i).getName().equals(name))
				return new Character((char)(i+97)).toString();
		}
		return "";
	}
}
