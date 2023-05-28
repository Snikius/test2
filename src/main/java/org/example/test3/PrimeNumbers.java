package org.example.test3;

import java.util.ArrayList;
import java.util.List;

public class PrimeNumbers {
    public List<Integer> getPrimesIn(int from, int to) {
        List<Integer> primesResult = new ArrayList<>((int) ((to - from) * 0.5));
        int start = from;
        if (from <= 2 && to >= 2) {
            primesResult.add(2);
        }
        if (start % 2 == 0) {
            start++;
        }

        for (int i = start; i < to; i = i + 2) {
            if (isPrime(i)) {
                primesResult.add(i);
            }
        }

        return primesResult;
    }

    public boolean isPrime(int n) {
        if (n < 2) {
            return false;
        }
        if (n % 2 == 0) {
            return n == 2;
        }
        for (int i = 3; i * i <= n; i = i + 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
