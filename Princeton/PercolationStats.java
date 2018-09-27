/*----------------------------------------------------------------
 *  Author:        Sergey Stol
 *  Written:       25/02/2018
 *  Last updated:  25/02/2018
 *
 *  Compilation:   javac PercolationStats.java
 *  Execution:     java PercolationStats 200, 100
 *  
 *  Monte Carlo simulation experiment (to estimate the percolation threshold):
 *	1) Initialize all sites to be blocked.
 *
 *	2) Repeat the following until the system percolates:
 *	   a)Choose a site uniformly at random among all blocked sites.
 *	   b)Open the site. 
 *
 *	3) The fraction of sites that are opened when the system percolates 
 *	   provides an estimate of the percolation threshold. 
 *
 *  % java-algs4 PercolationStats 200 100
 *  mean                    = 0.5929934999999997
 *  stddev                  = 0.00876990421552567
 *  95% confidence interval = [0.5912745987737567, 0.5947124012262428]
 *
 *----------------------------------------------------------------*/

import java.lang.IllegalArgumentException;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 *  The {@code PercolationStats} class represents a <b>Monte Carlo simulation.</b>
 *  <p>
 *  <b>Monte Carlo simulation.</b> To estimate the percolation threshold, 
 *  consider the following computational experiment:
 *  <ul>
 *	<li>Initialize all sites to be blocked.
 *	<li>Repeat the following until the system percolates:
 *	   <ul>
 *		<li>Choose a site uniformly at random among all blocked sites.
 *		<li>Open the site. 
 *	   </ul>
 *	<li>The fraction of sites that are opened when the system percolates 
 *	provides an estimate of the percolation threshold. 
 *  </ul>
 *  <p>
 *  For example, if sites are opened in a 20-by-20 lattice according 
 *  to the snapshots below, then our estimate of the percolation 
 *  threshold is 204/400 = 0.51 because the system percolates 
 *  when the 204th site is opened. 
 *
 *  @author Sergey Stol
 */ 
public class PercolationStats {

   private final double arr_Thresholds[]; // array of threshold
   private final double d_mean, d_dev, d_coLo, d_hiLo;

   //***************************************************************************
   // API (public) functions

   // perform trials independent experiments on an n-by-n grid
   public PercolationStats(int n, int trials) {
	validate(n, trials);
	arr_Thresholds = new double[trials];
	for (int i = 1; i <= trials; i++) {
	   Percolation perc = new Percolation(n);
	   int numberOfOpenSites = 0;
	   
	   while (!perc.percolates()) {
		int k = StdRandom.uniform(1, n + 1);
		int m = StdRandom.uniform(1, n + 1);
		if (perc.isOpen(k, m)) { continue; }
		perc.open(k, m);
		numberOfOpenSites++;
	   }
	   arr_Thresholds[i - 1] = ((double)numberOfOpenSites / (n * n));
	}
	d_mean   = StdStats.mean(arr_Thresholds);
	d_dev	   = StdStats.stddev(arr_Thresholds);
	d_coLo   = d_mean - 1.96 * d_dev / Math.sqrt(arr_Thresholds.length);
	d_hiLo   = d_mean + 1.96 * d_dev / Math.sqrt(arr_Thresholds.length);
   }
   
   // sample mean of percolation threshold
   public double mean() {
	return d_mean;
   }  
   
   // sample standard deviation of percolation threshold
   public double stddev() {
	return d_dev;
   }
  
   // low  endpoint of 95% confidence interval
   public double confidenceLo() {
	return d_coLo;
   }
   // high endpoint of 95% confidence interval
   public double confidenceHi() {
	return d_hiLo;
   }

   public static void main(String[] args) {
	int N = Integer.parseInt(args[0]);
	int T = Integer.parseInt(args[1]);
	
	PercolationStats percStat = new PercolationStats(N, T);
	System.out.println("mean\t" + percStat.mean());
	System.out.println("stddev\t " + percStat.stddev());
	System.out.println("95 % confidence interval = ["
				   + percStat.confidenceLo()
				   + " "
				   + percStat.confidenceHi()
				   + "]");
   }
   
   //***************************************************************************
   // Internal (private) functions
   
   private void validate(int n, int trials) {
	if ((n <= 0) || (trials <= 0)) {throw new IllegalArgumentException();}
   }
}
   
