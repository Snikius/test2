package org.example;

import org.example.test3.PrimeNumbers;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class PrimeNumbersTests {

    private final PrimeNumbers primeNumbers = new PrimeNumbers();

    @Test
    public void isPrimeTest() {
        assertTrue(primeNumbers.isPrime(2));
        assertTrue(primeNumbers.isPrime(3));
        assertTrue(primeNumbers.isPrime(5));
        assertTrue(primeNumbers.isPrime(7));
        assertTrue(primeNumbers.isPrime(11));
        assertTrue(primeNumbers.isPrime(89));
        assertTrue(primeNumbers.isPrime(881));
        assertTrue(primeNumbers.isPrime(7919));

        assertFalse(primeNumbers.isPrime(1));
        assertFalse(primeNumbers.isPrime(4));
        assertFalse(primeNumbers.isPrime(10));
        assertFalse(primeNumbers.isPrime(15));
        assertFalse(primeNumbers.isPrime(92));
        assertFalse(primeNumbers.isPrime(7871));
    }


    @Test
    public void primeBetweenTest() {
        assertEquals(
                List.of(2),
                primeNumbers.getPrimesIn(1, 3)
        );
        assertEquals(
                List.of(2),
                primeNumbers.getPrimesIn(2, 2)
        );
        assertEquals(
                List.of(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43,
                        47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97),
                primeNumbers.getPrimesIn(0, 100)
        );
        assertEquals(
                List.of(211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293),
                primeNumbers.getPrimesIn(200, 300)
        );
        assertEquals(
                List.of(7703, 7717, 7723, 7727, 7741, 7753, 7757, 7759, 7789, 7793, 7817,
                        7823, 7829, 7841, 7853, 7867, 7873, 7877, 7879, 7883),
                primeNumbers.getPrimesIn(7701, 7900)
        );
    }
}
