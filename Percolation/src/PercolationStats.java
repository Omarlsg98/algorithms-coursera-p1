import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final double[] results;
    private final int T;
    private final float confidence=1.96f;
    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials){
        results=new double[trials];
        T=trials;
        for (int i =0;i<trials;i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                p.open(StdRandom.uniform(n)+1,StdRandom.uniform(n)+1);
            }
            results[i]=(double)(p.numberOfOpenSites())/(n*n);
        }
    }

    // sample mean of percolation threshold
    public double mean(){
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev(){
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo(){
        return mean()-confidence*stddev()/T;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi(){
        return mean()+confidence*stddev()/T;
    }

    // test client
    public static void main(String[] args){
        int[] iArgs={Integer.parseInt(args[0]),Integer.parseInt(args[1])};
        if(iArgs[0]<=0 || iArgs[1]<=0)
            throw new IllegalArgumentException("invalid parameters, must be greater than 0");
        PercolationStats ps= new PercolationStats(iArgs[0],iArgs[1]);
        System.out.println("Mean\t= "+ps.mean());
        System.out.println("StdDeviation\t= "+ps.stddev());
        System.out.println("95% confidence interval\t= ["+ps.confidenceLo()+","+ps.confidenceHi()+"]");
    }

}
