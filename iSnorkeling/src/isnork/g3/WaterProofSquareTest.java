package isnork.g3;

import com.google.common.collect.Lists;

import isnork.sim.SeaLife;
import isnork.sim.SeaLifePrototype;

import java.util.List;

import org.junit.Test;

public class WaterProofSquareTest {
  @Test public void testClearRemovesTransient() {
    Square testSquare = new WaterProofSquare();

    List<SeaLife> creatures = Lists.newArrayList();
    for (int i=0; i<10; i++) {
      final boolean isDangerous = i < 5;
      SeaLifePrototype proto = new SeaLifePrototype() {
        protected boolean dangerous = isDangerous;
        protected double happiness = 10.;
      };
      creatures.add(new SeaLife(proto));
    }
      
    for (SeaLife creature : creatures) {
      testSquare.addCreature(creature, 1.);
    }
  }
}
