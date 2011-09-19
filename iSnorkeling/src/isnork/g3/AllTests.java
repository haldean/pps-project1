package isnork.g3;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    PokedexTest.class,
    SeaLifePrototypeBuilderTest.class,
    WaterProofSquareTest.class,
    WaterProofTranscoderTest.class
    })
public class AllTests {}
