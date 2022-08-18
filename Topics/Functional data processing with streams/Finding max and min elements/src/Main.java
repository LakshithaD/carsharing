import java.util.*;
import java.util.function.*;
import java.util.stream.*;


class MinMax {

    public static <T> void findMinMax(
            Stream<? extends T> stream,
            Comparator<? super T> order,
            BiConsumer<? super T, ? super T> minMaxConsumer) {

        List<T> values = stream.collect(Collectors.toList());
        if (values.isEmpty()) {
            minMaxConsumer.accept(null, null);
        } else {
            values.sort(order);
            minMaxConsumer.accept(values.get(0), values.get(values.size() - 1));
        }

    }
}