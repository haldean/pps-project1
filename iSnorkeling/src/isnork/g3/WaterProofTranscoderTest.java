package isnork.g3;

import isnork.g3.WaterProofTranscoder;

import isnork.sim.SeaLife;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableSortedMap;

import static org.junit.Assert.*;

import org.junit.Test;

public class WaterProofTranscoderTest {
	@Test
	public void testEncode() {
		String name = "Mysterious";
		SortedMap<Integer, String> speciesRanking =
            ImmutableSortedMap.of(3, name, 2, "Herp", 1, "Derp", 0, "Durrrr");

		Transcoder xcoder = new WaterProofTranscoder(speciesRanking, 41);
		List<String> messages = null;
		
		messages = xcoder.encode(name, "29", new Point2D.Double(-6.0, 4.0));

		assertEquals("Should encode correctly.", "dbdip", Joiner.on("").join(messages));
	}

	@Test
	public void testDecode() {
		String name = "Lassie";
		SortedMap<Integer, String> speciesRanking =
            ImmutableSortedMap.of(0, name);

		Transcoder xcoder = new WaterProofTranscoder(speciesRanking, 41);
		xcoder.encode(name, "10", new Point2D.Double(9.0, -4.0));
		String message = "abdsk";
		SeaLife creature = xcoder.decode(message);
		//System.out.println("\n\n"+creature.toString()+" at "+creature.getLocation().toString()+"\n\n");
		assertEquals("Creature name should decode to Lassie.", "Lassie", creature.getName());
		assertEquals("Creature ID should decode to 29.", 29, creature.getId());
		assertEquals("Creature location should decode to 9.0,-4.0.", "Point2D.Double[9.0, -4.0]", creature.getLocation().toString());
	}
}
