
package com.mycompany.selectionsort;

public class LinealSelectionSort {
    
    private int[] numbers;
    private double executionTime;
    
    public LinealSelectionSort(int[] numbers) {
        this.numbers = numbers;
    }
    
    public void sort() {
        long startTime = System.nanoTime();
        sort(numbers);
        long endTime = System.nanoTime();
        executionTime = (double) (endTime - startTime) / 1_000_000;
    }
    
    private void sort(int[] nums) {
        int size = nums.length;
        
        for(int i = 0; i < size; i++) {
            int min = i;
            
            for(int j = i + 1; j < size; j++) {
                if(numbers[j] < numbers[min]) {
                    min = j;
                }
            }
            
            int temp = numbers[i];
            numbers[i] = numbers[min];
            numbers[min] = temp;
        }
    }
    
    public int[] getArr() {
        return numbers;
    }
    
     public double getExecTime() {
        return executionTime;
    }
}
