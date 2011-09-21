package isnork.g3;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    PokedexTest.class,
    PrecomputationTest.class,
    SeaLifePrototypeBuilderTest.class,
    WaterProofSquareTest.class,
    WaterProofCartogramTest.class,
    WaterProofTranscoderTest.class,
    WaterProofMessengerTest.class
    })
public class AllTests {}
