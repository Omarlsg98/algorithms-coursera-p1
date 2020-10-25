import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            String c = StdIn.readString();
            rq.enqueue(c);
        }
        for (Object c : rq) {
            if (k < 1) break;
            StdOut.println(c);
            k--;
        }
    }
}
