package com.mycompany.selectionsort;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorServiceSelection {

    private final int[] numbers;
    private double execTime;

    public ExecutorServiceSelection(int[] numbers) {
        this.numbers = numbers;
    }

    public void sort() {
        long startTime = System.nanoTime();
        ExecutorService executor = Executors.newFixedThreadPool(6);

        try {
            int taskSize = numbers.length / 6;
            Future<int[]>[] futures = new Future[6];
            
            for(int i = 0; i < 6; i++) {
                int start = i * taskSize;
                int end = (i == 5) ? numbers.length : (i + 1) * taskSize;
                futures[i] = executor.submit(new SortTask(Arrays.copyOfRange(numbers, start, end)));
            }
            
            for(int i = 0; i < 6; i++) {
                int start = i * taskSize;
                int end = (i == 5) ? numbers.length : (i + 1) * taskSize;
                System.arraycopy(futures[i].get(), 0, numbers, start, end - start);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        long endTime = System.nanoTime();
        execTime = (endTime - startTime) / 1_000_000;
    }

    private static class SortTask implements Callable<int[]> {

        private final int[] numbers;

        public SortTask(int[] numbers) {
            this.numbers = numbers;
        }

        @Override
        public int[] call() {
            selectionSort(numbers);
            return numbers;
        }
    }

    private static void selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                int temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;
            }
        }
    }

    public int[] getArr() {
        return numbers;
    }

    public double getExecTime() {
        return execTime;
    }

}
