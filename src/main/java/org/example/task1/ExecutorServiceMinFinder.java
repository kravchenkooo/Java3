package org.example.task1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorServiceMinFinder {
    public static int findMin(int[][] array, int threadCount) throws Exception {
        int rows = array.length;
        int chunkSize = (int) Math.ceil((double) rows / threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Future<Integer>> results = new ArrayList<>();

        for (int i = 0; i < rows; i += chunkSize) {
            int endRow = Math.min(i + chunkSize, rows);
            results.add(executor.submit(new MinFinderCallable(array, i, endRow)));
        }

        int globalMin = Integer.MAX_VALUE;
        for (Future<Integer> result : results) {
            globalMin = Math.min(globalMin, result.get());
        }

        executor.shutdown();
        return globalMin;
    }
}
