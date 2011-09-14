package isnork.g4;

import isnork.sim.SeaLife;
import isnork.sim.SeaLifePrototype;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class SpeciesChecklist {
    protected static final int WISH_TO_SEE = 3;
    
    protected HashMap<String, ArrayList<Integer>> checklist;
    
    public SpeciesChecklist(Collection<SeaLifePrototype> allLife) {
        checklist = new HashMap<String, ArrayList<Integer>>();
        for(SeaLifePrototype proto : allLife) {
            checklist.put(proto.getName(), new ArrayList<Integer>());
        }
    }
    
    public void checkOff(SeaLife specimen) {
        checklist.get(specimen.getName()).add(specimen.getId());
    }
    
    public int countRemaining(SeaLifePrototype prototype) {
        return Math.max(0, WISH_TO_SEE - checklist.get(prototype.getName()).size());
    }
    
    public boolean hasSeenIndividual(SeaLife specimen) {
        return checklist.get(specimen.getName()).contains(specimen.getId());
    }
}
