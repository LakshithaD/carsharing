import java.math.BigInteger;
import java.util.Scanner;
import java.util.stream.*;

class PrimeNumbers {

    /**
     * Checking if a number is prime
     *
     * @param number to test >= 2
     * @return true if number is prime else false
     */
    private static boolean isPrime(long number) {
        // write your code here
        //LongStream.of(number).filter(value -> value > 1).filter()
        return Stream.iterate(2, n ->  {
                    BigInteger integer = BigInteger.valueOf(n);
                    return integer.nextProbablePrime().intValue();
                })
                .takeWhile(bigInteger -> bigInteger <= number)
                .filter(integer -> integer == number)
                .count() == 1;

    }

    public static void main(String[] args) {



        Scanner scanner = new Scanner(System.in);

        String line = scanner.nextLine().trim();

        int n = Integer.parseInt(line);

        System.out.println(isPrime(n) ? "True" : "False");
    }
}