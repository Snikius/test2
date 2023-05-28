package org.example;

import org.example.test2.Grouper;
import org.example.test2.Person;
import org.example.test3.PrimeConcurrentSearch;
import org.example.test3.PrimeCycleRecorderImpl;
import org.example.test3.PrimeNumbers;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Main {
    /**
     * Тестовые классы
     */
    public static void main(String[] args) {
        // TEST 2

        var grouper = new Grouper();
        var grouped = grouper.groupByName(List.of(
                new Person("p2"),
                new Person("p4"),
                new Person("p1"),
                new Person("p1"),
                new Person("p2"),
                new Person("p1"),
                new Person("p3")
        ));
        var expected = Map.of(
                "p1", List.of(new Person("p1"), new Person("p1"), new Person("p1")),
                "p2", List.of(new Person("p2"), new Person("p2")),
                "p3", List.of(new Person("p3")),
                "p4", List.of(new Person("p4"))
        );
        assertEquals(expected, grouped);



        // TEST 3
        var primeNumbers = new PrimeNumbers();
        var primeRecorder = new PrimeCycleRecorderImpl();
        var primeConcurrentSearch = new PrimeConcurrentSearch(primeNumbers, primeRecorder);
        long start = System.currentTimeMillis();
        primeConcurrentSearch.runProcess(1000000, 10000, 1);
        long elapsedTimeOneThread = System.currentTimeMillis() - start;

        start = System.currentTimeMillis();
        primeConcurrentSearch.runProcess(1000000, 10000, 2);
        long elapsedTimeTwoThread = System.currentTimeMillis() - start;

        start = System.currentTimeMillis();
        primeConcurrentSearch.runProcess(1000000, 10000, 4);
        long elapsedTimeFourThread = System.currentTimeMillis() - start;

        System.out.printf("\n1 Thread: " + elapsedTimeOneThread);
        System.out.printf("\n2 Threads: " + elapsedTimeTwoThread);
        System.out.printf("\n4 Threads: " + elapsedTimeFourThread);
        assertTrue(elapsedTimeTwoThread < elapsedTimeOneThread);
        assertTrue(elapsedTimeFourThread < elapsedTimeOneThread);
    }
}