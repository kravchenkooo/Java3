package org.example.task1;

import java.util.concurrent.Callable;

public class MinFinderCallable implements Callable<Integer> {
    private final int[][] array;
    private final int startRow, endRow;
    private final int firstElement;

    public MinFinderCallable(int[][] array, int startRow, int endRow) {
        this.array = array;
        this.startRow = startRow;
        this.endRow = endRow;
        this.firstElement = array[0][0];
    }

    @Override
    public Integer call() {
        int min = Integer.MAX_VALUE;
        for (int i = startRow; i < endRow; i++) {
            for (int val : array[i]) {
                if (firstElement * 2 <= val && min > val) {
                    min = val;
                }
            }
        }
        return min;
    }
}
