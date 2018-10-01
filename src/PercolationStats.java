import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
	private final int		n;
	private final double	mean;
	private final double	stddev;
	private final double	confidenceLo;
	private final double	confidenceHi;
	// perform trials independent experiments on an n-by-n grid
	public PercolationStats(int n, int trials) {
		validate(n, trials);
		this.n							= n;
		Percolation grid;
		int			size 				= n * n;
		int[] 		closeSites;
		double[] 	thresholds 			= new double[trials];
		// Execute an experiment
		for (int trial = 0, numberOfOpenSites = 0; trial < trials; trial++, numberOfOpenSites = 0) {
			closeSites = new int[size];
			for (int i = 0; i < size; i++) {
				closeSites[i] = i + 1;
			}
			StdRandom.shuffle(closeSites);
			grid = new Percolation(n);
			for (int i = 0; i < size && !grid.percolates(); i++) {
				grid.open(getRow(closeSites[i]), getCol(closeSites[i]));
				numberOfOpenSites++;
			}
			thresholds[trial] = (double)numberOfOpenSites / size;
		}
		// Calculate results
		mean 			= StdStats.mean(thresholds);
		stddev 			= StdStats.stddev(thresholds);
		confidenceLo	= mean - (1.96 * stddev / Math.sqrt(trials));
		confidenceHi	= mean + (1.96 * stddev / Math.sqrt(trials));
	}
	private void validate(int n, int trials) {
		if (n <= 0 || trials <= 0) throw new IllegalArgumentException();
	}
	private int getCol(int i) {
		return i % n == 0 ? n : i % n;
	}
	private int getRow(int i) {
		return i % n == 0 ?  i / n : i / n + 1;
	}
	// sample mean of percolation threshold
	public double mean() {
		return mean;
	}
	// sample standard deviation of percolation threshold
	public double stddev() {
		return stddev;
	}
	// low  endpoint of 95% confidence interval
	public double confidenceLo() {
		return confidenceLo;
	}
	// high endpoint of 95% confidence interval
	public double confidenceHi() {
		return confidenceHi;
	}
	@Override
	public String toString() {
		return "mean = " + mean + "\nstddev = " + stddev + 
				"\n95% confidence interval = [" + confidenceLo
				+ ", " + confidenceHi + "]";
	}
	// test client (described below)
	public static void main(String[] args) {
		PercolationStats percolationStats = 
				new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		System.out.println(percolationStats);
		
	}
}