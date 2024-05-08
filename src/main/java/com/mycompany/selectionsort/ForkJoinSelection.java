
package com.mycompany.selectionsort;

import java.util.concurrent.RecursiveAction;

public class ForkJoinSelection extends RecursiveAction {
    private int[] numbers;
    private int start;
    private int end;
    
    public ForkJoinSelection(int[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }
    
    @Override
    protected void compute() {
        if(end - start > 1) {
            int mid = start + (end - start) / 2;
            ForkJoinSelection leftTask = new ForkJoinSelection(numbers, start, mid);
            ForkJoinSelection rightTask = new ForkJoinSelection(numbers, mid, end);
            
            leftTask.fork();
            rightTask.fork();
            
            leftTask.join();
            rightTask.join();
            
            merge(start, mid, end);
        }
    }
    
    private void merge(int start, int mid, int end) {
        int minIndex;
        for(int i = start; i < end - 1; i++) {
            minIndex = i;
            for(int j = i + 1; j < end; j++) {
                if(numbers[j] < numbers[minIndex]) {
                    minIndex = j;
                }
            }
            swap(i, minIndex);
        }
    }
    
    private void swap(int i, int j) {
        if(i != j) {
            int temp = numbers[i];
            numbers[i] = numbers[j];
            numbers[j] = temp;
        }
    }
    
    public int[] getArr() {
        return numbers;
    }
   
}
