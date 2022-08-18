import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class Utils {

    public static List<Integer> sortOddEven(List<Integer> numbers) {

        Comparator<Integer> odd = Integer::compareTo;
        Comparator<Integer> descending = (o1, o2) -> o2 - o1;
        numbers.sort(new OddComparator().reversed().thenComparing(descending));
        return numbers;
    }
}

class OddComparator implements Comparator<Integer> {

    final int BEFORE = -1;
    final int EQUAL = 0;
    final int AFTER = 1;

    @Override
    public int compare(Integer o1, Integer o2) {

        if (o1 % 2 == 0 && o2 % 2 != 0) {
            return BEFORE;
        } else if (o1 % 2 != 0 && o2 % 2 == 0) {
            return AFTER;
        } else if (o1 % 2 == 0 && o2 % 2 == 0) {
            return o1.compareTo(o2);
        } else if (o1 % 2 != 0 && o2 % 2 != 0) {
            return o2.compareTo(o1);
        }
        return EQUAL;
    }
}