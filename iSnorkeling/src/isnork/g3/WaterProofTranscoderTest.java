package isnork.g3;

import isnork.g3.WaterProofTranscoder;

import isnork.sim.SeaLife;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.common.base.Joiner;

import static org.junit.Assert.*;

import org.junit.Test;

public class WaterProofTranscoderTest {
	@Test
	public void testEncode() {
		String name = "Mysterious";
		Map<Integer, String> speciesRanking = Maps.newHashMap();
		speciesRanking.put(3, name);

		Transcoder xcoder = new WaterProofTranscoder(speciesRanking, 41);
		List<String> messages = xcoder.encode(name, "42", new Point2D.Double(-6.0, 4.0));
		//System.out.println("\n\nEncoded message:\n" + Joiner.on(",").join(messages));
		assertEquals("Should encode correctly.", "da ip", Joiner.on("").join(messages));
	}

	@Test
	public void testDecode() {
		String name = "Lassie";
		Map<Integer, String> speciesRanking = Maps.newHashMap();
		speciesRanking.put(0, name);

		Transcoder xcoder = new WaterProofTranscoder(speciesRanking, 41);
		xcoder.encode(name, "10", new Point2D.Double(9.0, -4.0));
		String message = "aa sk";
		SeaLife creature = xcoder.decode(message);
		//System.out.println("\n\n"+creature.toString()+" at "+creature.getLocation().toString()+"\n\n");
		assertEquals("Creature name should decode to Lassie.", "Lassie", creature.getName());
		assertEquals("Creature ID should decode to 10.", 10, creature.getId());
		assertEquals("Creature location should decode to 9.0,-4.0.", "Point2D.Double[9.0, -4.0]", creature.getLocation().toString());
	}
}