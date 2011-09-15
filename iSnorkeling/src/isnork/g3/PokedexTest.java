package isnork.g3;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;

import isnork.sim.SeaLifePrototype;


public class PokedexTest {
	
	@Test
	public void testPopulateOneIdentity(){
		SeaLifePrototype charmander = new SeaLifePrototypeBuilder("charmander").create();
		
		WaterProofPokedex dex = new WaterProofPokedex(ImmutableSet.of(charmander));
		
		assertTrue((charmander == dex.get("charmander")));
	}
	
	@Test
	public void testPopulateTwoIdentity(){
		SeaLifePrototype charmander = new SeaLifePrototypeBuilder("charmander").create();
		SeaLifePrototype squirtle = new SeaLifePrototypeBuilder("squirtle").create();
		
		WaterProofPokedex dex = new WaterProofPokedex(ImmutableSet.of(charmander, squirtle));
		
		assertTrue((charmander == dex.get("charmander")));
		assertTrue((squirtle == dex.get("squirtle")));
	}
	
	@Test
	public void testPopulateOneProperties(){
		boolean danger = true;
		int happiness = 7;
		int max = 3;
		int min = 2;
		SeaLifePrototype charmander = new SeaLifePrototypeBuilder("charmander")
			.dangerous(danger)
			.happiness(happiness)
			.minCount(min)
			.maxCount(max)
			.create();
		
		WaterProofPokedex dex = new WaterProofPokedex(ImmutableSet.of(charmander));

		assertTrue((max == dex.getMaxCount("charmander")));
		assertTrue((min == dex.getMinCount("charmander")));
		assertTrue((happiness == dex.getHappiness("charmander")));
		assertTrue((danger == dex.isDangerous("charmander")));
	}

	
	@Test
	public void testPopulateTwoProperties(){
		boolean danger = true;
		int happiness = 7;
		int max = 3;
		int min = 2;
		SeaLifePrototype charmander = new SeaLifePrototypeBuilder("charmander")
			.dangerous(danger)
			.happiness(happiness)
			.minCount(min)
			.maxCount(max)
			.create();
		
		SeaLifePrototype squirtle = new SeaLifePrototypeBuilder("squirtle")
			.dangerous(!danger)
			.happiness(happiness * 2)
			.minCount(min * 2)
			.maxCount(max * 2)
			.create();
		
		WaterProofPokedex dex = new WaterProofPokedex(ImmutableSet.of(charmander, squirtle));

		assertTrue((max == dex.getMaxCount("charmander")));
		assertTrue((min == dex.getMinCount("charmander")));
		assertTrue((happiness == dex.getHappiness("charmander")));
		assertTrue((danger == dex.isDangerous("charmander")));

	
		assertTrue((max * 2 == dex.getMaxCount("squirtle")));
		assertTrue((min * 2 == dex.getMinCount("squirtle")));
		assertTrue((happiness * 2 == dex.getHappiness("squirtle")));
		assertTrue((!danger == dex.isDangerous("squirtle")));
	}
	
	@Test
	public void testPopulateOneCount(){
		int num = 20;
		int captures = 3;
		
		SeaLifePrototype charmander = new SeaLifePrototypeBuilder("charmander").create();
		
		WaterProofPokedex dex = new WaterProofPokedex(ImmutableSet.of(charmander));
		
		for (int index = 0; index < num; index++){
			dex.someoneSawCreature("charmander");
		}
		
		for (int index = 0; index < captures; index++){
			dex.personallySawCreature("charmander");
		}
		
		assertTrue((num + captures == dex.getSeenCount("charmander")));
		assertTrue((captures == dex.getPersonalSeenCount("charmander")));
	}

	
	@Test
	public void testPopulateTwoCount(){
		int num = 20;
		int captures = 3;
		
		SeaLifePrototype charmander = new SeaLifePrototypeBuilder("charmander").create();
		SeaLifePrototype squirtle = new SeaLifePrototypeBuilder("squirtle").create();
		
		WaterProofPokedex dex = new WaterProofPokedex(ImmutableSet.of(charmander, squirtle));
		
		for (int index = 0; index < num; index++){
			dex.someoneSawCreature("charmander");
			dex.someoneSawCreature("squirtle");
			dex.someoneSawCreature("squirtle");
		}
		
		for (int index = 0; index < captures; index++){
			dex.personallySawCreature("charmander");
		}
		
		for (int index = 0; index < captures / 2; index++){
			dex.personallySawCreature("squirtle");
		}
		
		assertTrue((num + captures == dex.getSeenCount("charmander")));
		assertTrue((captures == dex.getPersonalSeenCount("charmander")));

		assertTrue((num * 2 + captures / 2 == dex.getSeenCount("squirtle")));
		assertTrue((captures / 2== dex.getPersonalSeenCount("squirtle")));
	}


}
