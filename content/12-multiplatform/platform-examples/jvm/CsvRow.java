package it.unibo.spe;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CsvRow implements Iterable<String> {
    private final List<String> values;

    public CsvRow(Collection<String> values) {
        this.values = List.copyOf(values);
    }

    public List<String> getValues() { // JavaBean getter
        return values;
    }

    @Override
    public Iterator<String> iterator() {
        return values.iterator();
    }

    public static void main(String... args) { // String[]
        var row = new CsvRow(List.of("Ada", "42"));
        for (String value : row) System.out.println(value);
        assert row.getValues().equals(List.of("Ada", "42"));
    }
}
