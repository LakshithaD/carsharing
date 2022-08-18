import java.util.OptionalLong;
import java.util.Scanner;
import java.util.stream.*;

class Main {

    /**
     * Calculates the factorial of the given number n
     *
     * @param n >= 0
     *
     * @return factorial value
     */
    public static long factorial(long n) {
        // write your code here
        if (n == 0) {
            return 1;
        }
        OptionalLong optionalLong = LongStream.rangeClosed(1, n).reduce((left, right) -> left * right);
        return optionalLong.getAsLong();
    }

    // Don't change the code below
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        long n = Integer.parseInt(scanner.nextLine().trim());

        System.out.println(factorial(n));
    }
}