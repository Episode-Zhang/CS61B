package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        final int MAX_N = 128000;
        final int MAX_M = 10000;
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> op_count = new AList<>();

        for (int N = 1000; N <= MAX_N; N *= 2) {
            SLList<Integer> lst = new SLList<>();
            for (int i = 0; i < N; i++) {
                lst.addLast(i);
            }
            Stopwatch watcher = new Stopwatch();
            for (int M = 0; M < MAX_M; M++) {
                lst.getLast();
            }
            times.addLast(watcher.elapsedTime());
            Ns.addLast(N);
            op_count.addLast(MAX_M);
        }
        printTimingTable(Ns, times, op_count);
    }

}
