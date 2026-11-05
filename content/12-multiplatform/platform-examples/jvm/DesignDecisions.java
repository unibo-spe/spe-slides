public class DesignDecisions {
    static class Counter {
        private int value;

        public synchronized void increment() {
            value++; // the receiver's monitor protects this update
        }

        @Override
        public String toString() {
            return "Counter(" + value + ")";
        }
    }

    public static void main(String[] args) {
        Counter counter = new Counter();
        counter.increment();
        Object object = counter; // every class extends Object
        System.out.println(object); // virtual call: Counter(1)
        int primitive = 1; // not an Object
        Object boxed = primitive; // autoboxed to Integer
        assert object.toString().equals("Counter(1)");
        assert boxed instanceof Integer;
    }
}
