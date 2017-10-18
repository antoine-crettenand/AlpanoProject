package ch.epfl.alpano.test;

import java.util.Random;

public interface TestRandomizer {
    // Fix random seed to guarantee reproducibility.
    long SEED = 2017;

    int RANDOM_ITERATIONS = 500;

    static Random newRandom() {
        return new Random(SEED);
    }
}
