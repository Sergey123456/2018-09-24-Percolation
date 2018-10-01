import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	private final 	int 					n;					// 4
	private final 	int 					plusCoordinate1D;	// 4
	private final 	int 					minusCoordinate1D;	// 4
	private 		int 					numberOfOpenSites;	// 4
	private 		boolean 				isPercolates;		// 4
	private 		byte[] 					openGrid;			// 4 * n * n + 1
	private	final	WeightedQuickUnionUF 	wQUF;				// 4 * n * n + 2 + 4 * n * n + 2 + 4
	private	final	WeightedQuickUnionUF 	full_WQUF;			// 4 * n * n + 2 + 4 * n * n + 2 + 4
	
	// create n-by-n grid, with all sites blocked
	public Percolation(int n) {
		if (n < 1)
			throw new IllegalArgumentException();
		this.n				= n;
		openGrid 			= new byte[n * n + 1];
		wQUF				= new WeightedQuickUnionUF(n * n + 1);
		full_WQUF			= new WeightedQuickUnionUF(n * n + 2);
		plusCoordinate1D	= n * n;
		minusCoordinate1D	= n * n + 1;
		isPercolates		= false;
		for (int i = 0; i < n * n + 1; i++) {
			openGrid[i] = 0;
		}
		for (int i = 0; i < n; i++) {
			wQUF.union(plusCoordinate1D, i);
			full_WQUF.union(plusCoordinate1D, i);
		}
	}
   
	// open site (row, col) if it is not open already
	public void open(int row, int col) {
		validateIndexes(row, col);
		int coordinate1D = get1DCoordinate(row, col);
		if (openGrid[coordinate1D] == 1)
			return;
		openGrid[coordinate1D] = 1;
		if (row == n) {
			full_WQUF.union(coordinate1D, minusCoordinate1D);
		}
		unionWithOpenNeighbors(row, col);
		numberOfOpenSites++;
	}
	
	private void unionWithOpenNeighbors(int row, int col) {
		int currentSite1D = get1DCoordinate(row, col);
		unionNeighbors(currentSite1D, get1DCoordinate(row + 1, col));
		unionNeighbors(currentSite1D, get1DCoordinate(row - 1, col));
		unionNeighbors(currentSite1D, get1DCoordinate(row, col + 1));
		unionNeighbors(currentSite1D, get1DCoordinate(row, col - 1));
	}

	private void unionNeighbors(int currentSite1D, int newSite1D) {
		if ((newSite1D < 0) || openGrid[newSite1D] == 0 
				|| wQUF.connected(currentSite1D, newSite1D))
			return;
		wQUF.union(currentSite1D, newSite1D);
		full_WQUF.union(currentSite1D, newSite1D);
	}

	// is site (row, col) open?
	public boolean isOpen(int row, int col) {
		validateIndexes(row, col);
		return openGrid[get1DCoordinate(row, col)] == 1;
	}

   	// is site (row, col) full?
	public boolean isFull(int row, int col) {
		validateIndexes(row, col);
		return openGrid[get1DCoordinate(row, col)] == 1 && 
				wQUF.connected(plusCoordinate1D, get1DCoordinate(row, col));
	}
	
	// number of open sites
	public int numberOfOpenSites() {       
		return numberOfOpenSites;
	}
   
    // does the system percolate?
	public boolean percolates() {
		if (isPercolates) {
			return true;
		} else {
			isPercolates = full_WQUF.connected(plusCoordinate1D, minusCoordinate1D);
			return isPercolates;
		}
	}
	   
	private int get1DCoordinate(int row, int col) {
		return (row < 1 || row > n || col < 1 || col > n) ? -1 : (row - 1) * n + col - 1;
	}
	
	private void validateIndexes(int row, int col) {
		if (row < 1 || row > n || col < 1 || col > n)
			throw new IllegalArgumentException();
	}
}
