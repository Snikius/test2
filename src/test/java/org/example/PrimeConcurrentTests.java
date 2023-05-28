package org.example;

import java.io.IOException;
import java.nio.file.Path;

import org.example.test3.PrimeConcurrentSearch;
import org.example.test3.PrimeCycleRecorderImpl;
import org.example.test3.PrimeNumbers;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PrimeConcurrentTests {
    private PrimeConcurrentSearch primeConcurrentSearch;

    @Before
    public void initTest() {
        var primeNumbers = new PrimeNumbers();
        var primeRecorder = new PrimeCycleRecorderImpl();
        primeConcurrentSearch = new PrimeConcurrentSearch(primeNumbers, primeRecorder);
    }

    @Test
    public void concurrentСonsistencyTest() throws IOException {
        var primeTo100 = List.of(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41,
                43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97);

        primeConcurrentSearch.runProcess(100, 10, 1);
        var result1Thread = getValuesFromFile("Result.txt");
        assertEquals(primeTo100, result1Thread);

        primeConcurrentSearch.runProcess(100, 5, 2);
        var result2Threads = getValuesFromFile("Result.txt");
        assertEquals(primeTo100, result2Threads);

        primeConcurrentSearch.runProcess(100, 25, 3);
        var result3Threads = getValuesFromFile("Result.txt");
        assertEquals(primeTo100, result3Threads);

        primeConcurrentSearch.runProcess(100, 10, 5);
        var result5Thread = getValuesFromFile("Result.txt");
        assertEquals(primeTo100, result5Thread);

        primeConcurrentSearch.runProcess(100, 50, 6);
        var result6Threads = getValuesFromFile("Result.txt");
        assertEquals(primeTo100, result6Threads);
    }

    @Test
    public void concurrentСonsistencyLoopTest() throws IOException {
        var primeTo100 = List.of(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41,
                43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97);
        Random rand = new Random();

        for (int i = 0; i < 300; i++) {
            int randomThreadsNum = rand.nextInt((10 - 1) + 1) + 1;
            primeConcurrentSearch.runProcess(100, 10, randomThreadsNum);
            var resultThread = getValuesFromFile("Result.txt");
            assertEquals(primeTo100, resultThread);
        }
        for (int i = 0; i < 300; i++) {
            int randomThreadsNum = rand.nextInt((10 - 1) + 1) + 1;
            primeConcurrentSearch.runProcess(100, 5, randomThreadsNum);
            var resultThread = getValuesFromFile("Result.txt");
            assertEquals(primeTo100, resultThread);
        }
    }

    @Test
    public void concurrentSpeedDifferenceTest() {

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

    private List<Integer> getValuesFromFile(String file) throws IOException {
        var text = new String(Files.readAllBytes(Path.of(file)));
        return Arrays.stream(text.split(" "))
                .filter((v) -> !v.isBlank())
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }
}
