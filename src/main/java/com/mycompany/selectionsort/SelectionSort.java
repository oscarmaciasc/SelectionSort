
package com.mycompany.selectionsort;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class SelectionSort extends JFrame {
    
    /* General Approach:
    
       The list is divided into two sublists, sorted and unsorted,
       wich are divided by an imaginary wall.
        
       We find the smallest element from the unsorted sublist and swap
       it with the element at the beginning of the unsorted data.
    
       After each selection adn swapping, the imaginary wall between
       the two sublists move one element ahead, increasing the number
       of sorted elements and decreasing the number of unsorted ones.
    
       Each time we move one element from the unsorted sublist to the
       sorted sublist, we say that we have completed a sort pass.
    
       A list of n elements requires n-1 passes to completely rearrange
       the data.
    */
    
    private final JLabel unsortedLabel, input;
    private final JTextArea unsortedTextArea, sortedTextArea, inputArea;
    private final JLabel sortedLabel;
    private final JButton linealButton;
    private final JButton forkJoinButton;
    private final JButton executorServiceButton;
    private final JButton clearButton;
    private final JLabel timeLineal, timeFork, timeExecutor;
    
    public SelectionSort() {
        // Set up JFrame
        setTitle("Selection Sort Comparison");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(840, 450)); // Increased height for the execution time label

        // Customize look and feel of buttons
        UIManager.put("Button.background", Color.WHITE);
        UIManager.put("Button.foreground", Color.BLACK);
        UIManager.put("Button.font", new Font("Arial", Font.PLAIN, 14));
        UIManager.put("Button.border", BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Create Input Box
        JPanel inputBox = new JPanel(new BorderLayout());
        inputBox.setPreferredSize(new Dimension(400, 200)); // Reduced height to make space for execution time label
        inputBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        JPanel unsortedPanel = new JPanel(new BorderLayout());
        unsortedLabel = new JLabel("Unsorted:");
        unsortedLabel.setPreferredSize(new Dimension(200, 20)); // Set width to 50%
        unsortedPanel.add(unsortedLabel, BorderLayout.NORTH);

        unsortedTextArea = new JTextArea();
        unsortedTextArea.setLineWrap(true);
        unsortedTextArea.setWrapStyleWord(true);

        JScrollPane scrollPaneSort = new JScrollPane(unsortedTextArea);

        unsortedPanel.add(scrollPaneSort, BorderLayout.CENTER);

        JPanel sortedPanel = new JPanel(new BorderLayout());
        sortedLabel = new JLabel("Sorted:");
        sortedLabel.setPreferredSize(new Dimension(200, 20)); // Set width to 50%
        sortedPanel.add(sortedLabel, BorderLayout.NORTH);

        sortedTextArea = new JTextArea();
        sortedTextArea.setLineWrap(true);
        sortedTextArea.setWrapStyleWord(true);
        sortedTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(sortedTextArea);
        sortedPanel.add(scrollPane, BorderLayout.CENTER);

        inputBox.add(unsortedPanel, BorderLayout.WEST);
        inputBox.add(sortedPanel, BorderLayout.CENTER);

        // Add borders
        inputBox.setBorder(BorderFactory.createTitledBorder("Input Box"));

        add(inputBox, BorderLayout.WEST);

        // Create Button Box
        JPanel buttonBox = new JPanel(new GridLayout(2, 2, 10, 10)); // 2 rows, 2 columns, gap of 10 between components
        buttonBox.setPreferredSize(new Dimension(400, 200)); // Reduced height to make space for execution time label
        buttonBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        linealButton = new JButton("lineal");
        forkJoinButton = new JButton("forkjoin");
        executorServiceButton = new JButton("executorservice");
        clearButton = new JButton("clear");

        buttonBox.add(linealButton);
        buttonBox.add(forkJoinButton);
        buttonBox.add(executorServiceButton);
        buttonBox.add(clearButton);

        // Add borders
        buttonBox.setBorder(BorderFactory.createTitledBorder("Button Box"));

        add(buttonBox, BorderLayout.EAST);

        // Create Execution Time Label
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS)); // Set layout to vertical BoxLayout

        timeLineal = new JLabel("Time Lineal: ");
        timeLineal.setAlignmentX(Component.LEFT_ALIGNMENT); // Align to the left
        timeLineal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        timeFork = new JLabel("Time Fork: ");
        timeFork.setAlignmentX(Component.LEFT_ALIGNMENT); // Align to the left
        timeFork.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        timeExecutor = new JLabel("Time Executor: ");
        timeExecutor.setAlignmentX(Component.LEFT_ALIGNMENT); // Align to the left
        timeExecutor.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        inputArea = new JTextArea();
        input = new JLabel("num Elementos: ");

        statusPanel.add(timeLineal);
        statusPanel.add(timeFork);
        statusPanel.add(timeExecutor);
        statusPanel.add(input);
        statusPanel.add(inputArea);

        add(statusPanel, BorderLayout.SOUTH);

        linealButton.addActionListener(e -> {
            int[] numbers = getInput();
            setUnsortedNumbers(numbers);
            LinealSelectionSort linealSelectionSort = new LinealSelectionSort(numbers);
            linealSelectionSort.sort();
            int[] sorted = linealSelectionSort.getArr();
            setSortedNumbers(sorted);
            timeLineal.setText("Time Lineal: " + linealSelectionSort.getExecTime() + " ms");
        });

        forkJoinButton.addActionListener(e -> {
            int[] numbers = getInput();
            setUnsortedNumbers(numbers);
            
            long startTime = System.nanoTime();
            ForkJoinPool pool = new ForkJoinPool(6);
            ForkJoinSelection forkJoinSelection = new ForkJoinSelection(numbers, 0, numbers.length);
            pool.invoke(forkJoinSelection);
            int[] sorted = forkJoinSelection.getArr();
            long endTime = System.nanoTime();
            double executionTime = (double) (endTime - startTime) / 1_000_000;
            setSortedNumbers(sorted);

            timeFork.setText("Time Fork: " + executionTime + " ms");
        });

        executorServiceButton.addActionListener(e -> {
            int[] numbers = getInput();
            setUnsortedNumbers(numbers);

            ExecutorServiceSelection executorSelection = new ExecutorServiceSelection(numbers);
            executorSelection.sort();

            int[] sorted = executorSelection.getArr();
            setSortedNumbers(sorted);

            timeExecutor.setText("Time Executor: " + executorSelection.getExecTime() + " ms");
        });

        clearButton.addActionListener(e -> {
            // clear button action
            System.out.println("Clear button clicked");
            sortedTextArea.setText("");
            timeLineal.setText("Time Lineal: ");
            timeFork.setText("Time Fork: ");
            timeExecutor.setText("Time Executor: ");
        });

        pack();
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SelectionSort::new);
    }
    
    public int[] getNumbersArray() {
        // Get the text from unsortedTextArea
        String unsortedNumbersString = unsortedTextArea.getText();

        // Split the text into individual numbers using spaces and commas as delimiters
        String[] numbersStringArray = unsortedNumbersString.split("\\s*,\\s*|\\s+");

        // Convert the string array to an integer array
        int[] numbers = new int[numbersStringArray.length];
        for (int i = 0; i < numbersStringArray.length; i++) {
            numbers[i] = Integer.parseInt(numbersStringArray[i]);
        }

        return numbers;
    }
    
    public int[] getInput() {
        String inputText = inputArea.getText().trim();
        String unsortedPanel = unsortedTextArea.getText().trim(); // Get text from input area and remove leading/trailing whitespace

        if (unsortedPanel.isEmpty() && !inputText.isEmpty()) { // Check if input text is not empty
            int nums = Integer.parseInt(inputText); // Convert input text to integer
            int[] numbers = generateNumbers(0, nums, 1); // Pass integer to generateNumbers method
            return numbers;
        } else if (!unsortedPanel.isEmpty() && !inputText.isEmpty()) {
            int currentNums = Integer.parseInt(inputText);
            if(currentNums != getNumbersArray().length) {
                int[] numbers = generateNumbers(0, currentNums, 1);
                return numbers;
            } else {
                return getNumbersArray();
            }
        } else {
            return getNumbersArray();
        }
    }
    
    public static int[] generateNumbers(int start, int end, int gap) {
        if (start >= end || gap <= 0) {
            System.out.println("Invalid input parameters!");
            return new int[0]; // Returning an empty array
        }

        int size = (end - start) / gap;
        int[] numbers = new int[size];

        Random rand = new Random(); // Create a single Random object outside the loop

        int range = (500 - start) / gap + 1; // Calculate the range once

        for (int i = 0; i < size; i++) {
            numbers[i] = start + rand.nextInt(range) * gap;
        }

        return numbers;
    }
    
    public void setSortedNumbers(int[] sorted) {
        // Convert the sorted array to a string
        StringBuilder sortedString = new StringBuilder();
        for (int i = 0; i < sorted.length; i++) {
            sortedString.append(sorted[i]);
            if (i < sorted.length - 1) {
                sortedString.append(", "); // Add comma and space between numbers
            }
        }

        // Set the sorted string to the sortedTextArea
        sortedTextArea.setText(sortedString.toString());
    }

    public void setUnsortedNumbers(int[] unsorted) {
        // Convert the sorted array to a string
        StringBuilder unsortedString = new StringBuilder();
        for (int i = 0; i < unsorted.length; i++) {
            unsortedString.append(unsorted[i]);
            if (i < unsorted.length - 1) {
                unsortedString.append(", "); // Add comma and space between numbers
            }
        }

        // Set the sorted string to the sortedTextArea
        unsortedTextArea.setText(unsortedString.toString());
    }
}
