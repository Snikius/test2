package org.example.test3;

import java.util.List;

public interface PrimeRecorder {
    void record(List<Integer> primesToRecord, int threadNumber, int periodStartValue, int batchSize);
    void init(int threadsCount);
    void finish();
}
