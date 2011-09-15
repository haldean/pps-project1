package isnork.g3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import isnork.sim.SeaLifePrototype;


public class SeaLifePrototypeBuilderTest {

	@Test
	public void testProperties(){
		boolean dangerous = true;
		int happiness = 10;
		int max = 20;
		int min = 3;
		SeaLifePrototype builderCharmander = new SeaLifePrototypeBuilder("charmander")
			.dangerous(dangerous)
			.happiness(happiness)
			.maxCount(max)
			.minCount(min)
			.create();
		
		SeaLifePrototype nonBuilderCharmander = new SeaLifePrototype();
		nonBuilderCharmander.setDangerous(dangerous);
		nonBuilderCharmander.setHappiness(happiness);
		nonBuilderCharmander.setMaxCount(max);
		nonBuilderCharmander.setMinCount(min);
		nonBuilderCharmander.setName("charmander");
		
		assertEquals(nonBuilderCharmander.isDangerous(), builderCharmander.isDangerous());
		assertEquals(nonBuilderCharmander.getHappiness(), builderCharmander.getHappiness());
		assertEquals(nonBuilderCharmander.getMaxCount(), builderCharmander.getMaxCount());
		assertEquals(nonBuilderCharmander.getMinCount(), builderCharmander.getMinCount());
		assertEquals(nonBuilderCharmander.getName(), builderCharmander.getName());
	}


	@Test
	public void testDoubleCreation(){
		SeaLifePrototypeBuilder builder = new SeaLifePrototypeBuilder("charmander");
		SeaLifePrototype firstCharmander = builder.create();
		SeaLifePrototype secondCharmander = builder.create();
		
		assertFalse(firstCharmander == secondCharmander);		
	}
	
	@Test
	public void testDoubleCreationModification(){
		SeaLifePrototypeBuilder builder = new SeaLifePrototypeBuilder("charmander");
		SeaLifePrototype charmander = builder.name("charmander").create();
		SeaLifePrototype squirtle = builder.name("squirtle").create();
		
		assertEquals("charmander", charmander.getName());		
		assertEquals("squirtle", squirtle.getName());		
	}
}
