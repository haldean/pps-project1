package isnork.g3;

import isnork.sim.SeaLife;
import isnork.sim.SeaLifePrototype;
import isnork.sim.iSnorkMessage;
import isnork.sim.Observation;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.base.Joiner;

import static org.junit.Assert.*;
import org.easymock.EasyMock;
import org.junit.Test;

public class WaterProofMessengerTest {
    private WaterProofMessenger msgr;

    public WaterProofMessengerTest() {
        msgr = new WaterProofMessenger(getPokedex(), 20, 10);
    }

    private Pokedex getPokedex() {
        Pokedex dex = EasyMock.createMock(Pokedex.class);
        EasyMock.expect(dex.get(EasyMock.isA(String.class)))
            .andReturn(new SeaLife(new SeaLifePrototypeBuilder("charmander").create()));
        EasyMock.expect(dex.getSpeciesRanking()).andReturn(ImmutableMap.of(0, "charmander"));
        EasyMock.replay(dex);
        return dex;
    }

    @Test
    public void testAddReceived() {
        iSnorkMessage ism = EasyMock.createMock(iSnorkMessage.class);
        EasyMock.expect(ism.getSender()).andReturn(1);
        EasyMock.expect(ism.getLocation()).andReturn(new Point2D.Double(2.0,2.0));
        EasyMock.expect(ism.getMsg()).andReturn("");
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