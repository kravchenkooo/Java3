package org.example.task1;
import java.util.Random;



public class ArrayGenerator {
    public static int[][] generateArray(int rows, int cols) {
        Random random = new Random();
        int firstElement = random.nextInt(100) + 1; // Генеруємо перший елемент
        int[][] array = new int[rows][cols];

        array[0][0] = firstElement;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == 0 && j == 0) continue;
                array[i][j] = firstElement * 2 + random.nextInt(100); // Вдвічі більше першого елементу
            }
        }
        return array;
    }
}



