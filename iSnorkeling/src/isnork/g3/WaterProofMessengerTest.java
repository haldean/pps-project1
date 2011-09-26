package isnork.g3;

import static org.junit.Assert.assertEquals;
import isnork.sim.Observation;
import isnork.sim.SeaLife;
import isnork.sim.iSnorkMessage;

import java.awt.geom.Point2D;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Test;

import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Sets;

public class WaterProofMessengerTest {
    private WaterProofMessenger msgr;

    public WaterProofMessengerTest() {
        msgr = new WaterProofMessenger(getPokedex(), 20, 10);
    }

    private Pokedex getPokedex() {
        Pokedex dex = EasyMock.createMock(Pokedex.class);
        EasyMock.expect(dex.get(EasyMock.isA(String.class)))
            .andReturn(new SeaLife(new SeaLifePrototypeBuilder("charmander").create()));
        EasyMock.expect(dex.getSpeciesRanking()).andReturn(ImmutableSortedMap.of(0, "charmander"));
        EasyMock.expect(dex.getHappiness("charmander")).andReturn(0);
        EasyMock.replay(dex);
        return dex;
    }

    @Test
    public void testAddReceived() {
        iSnorkMessage ism = EasyMock.createMock(iSnorkMessage.class);
        EasyMock.expect(ism.getSender()).andReturn(1).anyTimes();
        EasyMock.expect(ism.getLocation())
            .andReturn(new Point2D.Double(2.0,2.0)).anyTimes();
        EasyMock.expect(ism.getMsg()).andReturn("").anyTimes();
        EasyMock.replay(ism);

        Set<iSnorkMessage> msgs = Sets.newHashSet(ism);
        msgr.addReceivedMessages(msgs);
    }

    @Test
    public void testAddOutboundMessage() {
        Observation bestSeen = EasyMock.createMock(Observation.class);
        EasyMock.expect(bestSeen.getName()).andReturn("charmander");
        EasyMock.expect(bestSeen.getId()).andReturn(10);
        EasyMock.expect(bestSeen.getLocation()).andReturn(new Point2D.Double(2.0,2.0));
        EasyMock.replay(bestSeen);

        msgr.addOutboundMessage(bestSeen);
    }

    @Test
    public void testSendNext() {
        testAddOutboundMessage();
        assertEquals("Next message should be \'a\'.", "a", msgr.sendNext());
    }
}
