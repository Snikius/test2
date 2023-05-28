package org.example.test3;

import java.security.InvalidParameterException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class PrimeConcurrentSearch {

    private final AtomicInteger currentProgress = new AtomicInteger(0);
    private final PrimeNumbers primeNumbers;
    private final PrimeRecorder primeRecorder;

    private final ReentrantLock processLock = new ReentrantLock();

    public PrimeConcurrentSearch(PrimeNumbers primeNumbers, PrimeRecorder primeRecorder) {
        this.primeNumbers = primeNumbers;
        this.primeRecorder = primeRecorder;
    }

    public void runProcess(int nLimit, int batchSize, int threadsCount) {
        if(!processLock.tryLock()) {
            throw new IllegalStateException("Concurrent prime process start");
        }
        if(nLimit % batchSize != 0) {
            throw new InvalidParameterException("Process with nLimit % batchSize != 0 not supported");
        }
        if(batchSize < 5) {
            throw new InvalidParameterException("Process with batchSize < 5 not supported");
        }
        try {
            var threadsCountDown = new CountDownLatch(threadsCount);
            currentProgress.set(0);
            primeRecorder.init(threadsCount);

            for (int i = 0; i < threadsCount; i++) {
                int finalI = i;
                Runnable runnable = () -> {
                    try {
                        processPrimesTask(nLimit, batchSize, finalI);
                        threadsCountDown.countDown();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                };
                new Thread(runnable).start();
            }
            threadsCountDown.await();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            processLock.unlock();
            primeRecorder.finish();
        }
    }
    private void processPrimesTask(int nLimit, int batchSize, int threadNumber) throws InterruptedException {
        var to = currentProgress.addAndGet(batchSize);
        var from = to - batchSize;

        while (to <= nLimit) {
            var primesInRange = primeNumbers.getPrimesIn(from, to);
            primeRecorder.record(primesInRange, threadNumber, from, batchSize);
            if(nLimit == to) {
                break;
            }
            to = currentProgress.addAndGet(batchSize);
            from = to - batchSize;
        }
    }
}
