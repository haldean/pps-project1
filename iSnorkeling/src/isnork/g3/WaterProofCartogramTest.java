package isnork.g3;

import isnork.sim.GameObject.Direction;
import isnork.sim.Observation;
import isnork.sim.SeaLife;
import isnork.sim.SeaLifePrototype;
import isnork.sim.iSnorkMessage;

import static org.junit.Assert.*;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Sets;
import org.easymock.EasyMock;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class WaterProofCartogramTest {
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

  private Pokedex getPokedex() {
    Pokedex dex = EasyMock.createMock(Pokedex.class);
    EasyMock.expect(dex.get(EasyMock.isA(String.class)))
      .andReturn(getSeaLife(true));
    EasyMock.expect(dex.getSpeciesRanking())
      .andReturn(ImmutableSortedMap.of(0, "Moving Creature"));
    dex.personallySawCreature("hello", 0);
    EasyMock.expectLastCall().anyTimes();
    EasyMock.expect(dex.getHappiness("hello")).andReturn(10);
    EasyMock.expect(dex.getPersonalSeenCount(EasyMock.isA(String.class)))
      .andReturn(0).anyTimes();
    EasyMock.expect(dex.getAllSpecies()).andReturn(
        new ArrayList<SeaLifePrototype>()).anyTimes();
    EasyMock.replay(dex);
    return dex;
  }

  @Test public void updateSucceedsOnEmptyMap() {
    Cartogram map = new WaterProofCartogram(20, 5, 20, getPokedex());
    map.update(
        new Point2D.Double(0., 0.), Sets.<Observation>newHashSet(),
        Sets.<Observation>newHashSet(), Sets.<iSnorkMessage>newHashSet());
  }

  @Test public void updateForSingleCreature() {
    WaterProofCartogram map = new WaterProofCartogram(11, 3, 10, getPokedex());   
    Observation obs = new MockObservation(
        new Point2D.Double(0., 1.), 1, "hello", 1., false, Direction.N);
    map.update(
        new Point2D.Double(0., 0.), ImmutableSet.of(obs),
        Sets.<Observation>newHashSet(), Sets.<iSnorkMessage>newHashSet());
  }
}
