import java.util.Scanner;

/**
 * JAVA PROGRAMMING - ICE TASK 2
 * Practical Assessment: Methods in Java
 *
 * This program prompts the user for two numbers, then uses methods
 * to calculate and display their sum and average.
 */
public class Practical2_Methods {

    /**
     * Question 1.1
     * Accepts two integers as parameters, calculates their sum,
     * and returns the result to the calling statement.
     *
     * @param num1 the first number
     * @param num2 the second number
     * @return the sum of num1 and num2
     */
    public static int calculateSum(int num1, int num2) {
        int sum = num1 + num2;
        return sum;
    }

    /**
     * Question 1.4
     * Accepts two integers as parameters, calculates their average,
     * and returns the result to the calling statement.
     *
     * @param num1 the first number
     * @param num2 the second number
     * @return the average of num1 and num2
     */
    public static double calculateAverage(int num1, int num2) {
        double average = (num1 + num2) / 2.0;
        return average;
    }

    public static void main(String[] args) {
        // Scanner object used to read user input
        Scanner input = new Scanner(System.in);

        // Question 1.2: Prompt the user to enter the first and second numbers
        System.out.print("Enter the first number: ");
        int firstNumber = input.nextInt();

        System.out.print("Enter the second number: ");
        int secondNumber = input.nextInt();

        // Question 1.3: Call the sum method and display the result
        int sumResult = calculateSum(firstNumber, secondNumber);
        System.out.println("The sum of the two numbers is: " + sumResult);

        // Question 1.4: Call the average method and display the result
        double averageResult = calculateAverage(firstNumber, secondNumber);
        System.out.println("The average of the two numbers is: " + averageResult);

        // Close the scanner to free up resources
        input.close();
    }
}