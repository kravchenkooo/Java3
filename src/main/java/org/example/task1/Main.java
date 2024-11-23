package org.example.task1;

import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Введення розмірів масиву
        System.out.print("Введіть кількість рядків масиву: ");
        int rows = scanner.nextInt();

        System.out.print("Введіть кількість стовпців масиву: ");
        int cols = scanner.nextInt();

        int[][] array = ArrayGenerator.generateArray(rows, cols);
        printArray(array);

        long startTime, endTime;

        // Work Stealing
        startTime = System.nanoTime();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        int minStealing = forkJoinPool.invoke(new MinFinderTask(array, 0, rows));
        endTime = System.nanoTime();
        System.out.println("Work Stealing мінімальний елемент: " + minStealing);
        System.out.println("Час виконання (Work Stealing): " + (endTime - startTime) / 1_000_000 + " мс");

        // Work Dealing
        startTime = System.nanoTime();
        int minDealing = ExecutorServiceMinFinder.findMin(array, 4); // 4 потоки
        endTime = System.nanoTime();
        System.out.println("Work Dealing мінімальний елемент: " + minDealing);
        System.out.println("Час виконання (Work Dealing): " + (endTime - startTime) / 1_000_000 + " мс");
    }

    public static void printArray(int[][] array) {
        for (int[] row : array) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }
}


