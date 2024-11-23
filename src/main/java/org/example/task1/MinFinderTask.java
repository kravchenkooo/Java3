package org.example.task1;
import java.util.concurrent.RecursiveTask;

public class MinFinderTask extends RecursiveTask<Integer> {
    private final int[][] array;
    private final int startRow, endRow;
    private final int firstElement;

    public MinFinderTask(int[][] array, int startRow, int endRow) {
        this.array = array;
        this.startRow = startRow;
        this.endRow = endRow;
        this.firstElement = array[0][0];
    }

    @Override
    protected Integer compute() {
        if (endRow - startRow <= 1) {
            int min = firstElement * 2;
            for (int i = startRow; i < endRow; i++) {
                for (int val : array[i]) {
                    if (firstElement * 2 <= val && min > val) {
                        min = val;
                    }
                }
            }
            return min;
        } else { // Розділяємо задачу
            int mid = (startRow + endRow) / 2;
            MinFinderTask task1 = new MinFinderTask(array, startRow, mid);
            MinFinderTask task2 = new MinFinderTask(array, mid, endRow);
            invokeAll(task1, task2);
            return Math.min(task1.join(), task2.join());
        }
    }
}
