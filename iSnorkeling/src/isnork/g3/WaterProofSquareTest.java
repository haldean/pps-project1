package isnork.g3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import isnork.sim.SeaLife;
import isnork.sim.SeaLifePrototype;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class WaterProofSquareTest {
  private SeaLife getSeaLife(final boolean canMove) {
    SeaLifePrototype proto = new SeaLifePrototype();
    proto.setDangerous(false);
    proto.setHappiness(10);
    proto.setSpeed(canMove ? 1 : 0);
    proto.setName(canMove ? "Moving Creature" : "Stationary Creature");

    SeaLife seaLife = new SeaLife(proto);
    assertEquals(canMove, seaLife.getSpeed() > 0);
    return seaLife;
  }

  @Test public void testClearRemovesTransient() {
    List<SeaLife> creatures = Lists.newArrayList();
    for (int i=0; i<10; i++) {
      creatures.add(getSeaLife(i % 2 == 0));
    }
    
    Square testSquare = new WaterProofSquare(
    		new WaterProofPokedex(Sets.<SeaLifePrototype>newHashSet(creatures)));
    for (SeaLife creature : creatures) {
      testSquare.addCreature(creature, 1., 0);
    }

    assertEquals(
        "Should contain all creatures added",
        10, testSquare.getCreatures().size());

    testSquare.tick();
    
    assertEquals(
        "All moving creatures should be removed",
        5, testSquare.getCreatures().size());

    for (Square.SeaLifeExpectation expectedCreature : testSquare.getCreatures()) {
      assertFalse(
          "All remaining creatures should be stationary",
          expectedCreature.getSeaLife().getSpeed() > 0);
    }
  }
}
