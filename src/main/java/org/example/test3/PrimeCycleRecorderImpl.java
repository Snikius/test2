package org.example.test3;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PrimeCycleRecorderImpl implements PrimeRecorder {

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private volatile int expectedNextValue = 0;
    private BufferedWriter mainFile;
    private Map<Integer, BufferedWriter> threadFiles;

    @Override
    public void record(List<Integer> primesToRecord, int threadNumber, int periodStartValue, int batchSize) {
        try {
            waitForThreadTurn(periodStartValue);
            for (Integer i: primesToRecord) {
                writeIntoFile(threadNumber, i);
            }
            updateNextExpectedThread(batchSize);
        } catch (IOException | InterruptedException exception) {
            throw new RuntimeException("Record exception", exception);
        }
    }

    @Override
    public void init(int threadsCount) {
        try {
            mainFile = Files.newBufferedWriter(Path.of("Result.txt"));
            threadFiles = new HashMap<>();
            expectedNextValue = 0;
            for (int i = 0; i < threadsCount; i++) {
                threadFiles.put(
                        i, Files.newBufferedWriter(Path.of("Thread" + (i + 1) + ".txt"))
                );
            }
        } catch (IOException exception) {
            throw new RuntimeException("File close error", exception);
        }
    }

    @Override
    public void finish() {
        try {
            mainFile.close();
            for (BufferedWriter file: threadFiles.values()) {
                file.close();
            }
        } catch (IOException exception) {
            throw new RuntimeException("File close error", exception);
        }
    }


    private void writeIntoFile(int thread, int num) throws IOException {
        var toWrite =  " " + num;
        mainFile.write(toWrite);
        threadFiles.get(thread).write(toWrite);
    }

    private void waitForThreadTurn(int periodStart) throws InterruptedException  {
        lock.lock();
        while(expectedNextValue != periodStart) {
            condition.await();
        }
    }

    private void updateNextExpectedThread(int batchSize) {
        expectedNextValue += batchSize;
        condition.signalAll();
        lock.unlock();
    }
}
