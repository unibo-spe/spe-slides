
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.util.FastMath;

class HelloMath {
    public static void main(String... args) {
        double[] xs = {1, 2, 3, 4, 5};
        double[] ys = {2, 4.1, 5.9, 8.2, 10.1};
        final DescriptiveStatistics stats = new DescriptiveStatistics();
        for (double x : xs) { stats.addValue(x); }
        System.out.println("avg=" + stats.getMean() + ", std.dev=" + stats.getStandardDeviation());
        final SimpleRegression lr = new SimpleRegression(true);
        for (int i = 0; i < xs.length; i++) { lr.addData(xs[i], ys[i]); }
        System.out.println("regression: y = " + lr.getSlope() + " * x + " + lr.getIntercept());
        System.out.println("R²=" + lr.getRSquare());
    }
}
