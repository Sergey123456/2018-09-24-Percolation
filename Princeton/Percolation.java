import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	private final 	int 					n;
	private final 	int 					plusCoordinate1D;
	private 		int[] 					openGrid;
	private 		int[] 					fullGrid;
	private 		int 					numberOfOpenSites;
	private 		boolean 				isPercolates;
	private			WeightedQuickUnionUF 	wQUF;
	
	// create n-by-n grid, with all sites blocked
	public Percolation(int n) {
		if (n < 1)
			throw new IllegalArgumentException();
		this.n				= n;
		numberOfOpenSites	= 0;
		openGrid 			= new int[n * n + 2];
		fullGrid 			= new int[n * n + 2];
		wQUF				= new WeightedQuickUnionUF(n * n + 2);
		plusCoordinate1D	= n * n;
		isPercolates		= false;
		for (int i = 0; i < n * n + 2; i++) {
			openGrid[i] 	= 0;
			fullGrid[i] 	= 0;
		}
		for (int i = 0; i < n; i++) {
			wQUF.union(plusCoordinate1D, i);
			fullGrid[i] = 1;
		}
	}
   
	// open site (row, col) if it is not open already
	public void open(int row, int col) {
		validateIndexes(row, col);
		int coordinate1D = get1DCoordinate(row, col);
		if (openGrid[coordinate1D] == 1)
			return;
		openGrid[coordinate1D] = 1;
		mergeSites(row, col);
		numberOfOpenSites++;
		if (!isPercolates) {
			for (int i = n * (n - 1); i < n * n; i++) {
				if (isFullWithoutValidate(i)) {
					isPercolates = true;
				}
			}
		}
	}
	
	private void mergeSites(int row, int col) {
		int currentSite1D = get1DCoordinate(row, col);
		mergeSitesAndReindex(currentSite1D, get1DCoordinate(row + 1, col));
		mergeSitesAndReindex(currentSite1D, get1DCoordinate(row - 1, col));
		mergeSitesAndReindex(currentSite1D, get1DCoordinate(row, col + 1));
		mergeSitesAndReindex(currentSite1D, get1DCoordinate(row, col - 1));
	}

	private void mergeSitesAndReindex(int currentSite1D, int newSite1D) {
		if ((newSite1D < 0) || openGrid[newSite1D] == 0 
				|| wQUF.connected(currentSite1D, newSite1D))
			return;
		if (isFullWithoutValidate(currentSite1D) && !isFullWithoutValidate(newSite1D)) {
			int root = wQUF.find(newSite1D);
			for (int i = 0; i < n * n; i++) {
				if (wQUF.find(i) == root)
					fullGrid[i] = 1;
			}
		} else if (!isFullWithoutValidate(currentSite1D) && isFullWithoutValidate(newSite1D)) {
			int root = wQUF.find(currentSite1D);
			for (int i = 0; i < n * n; i++) {
				if (wQUF.find(i) == root)
					fullGrid[i] = 1;
			}
		}
		wQUF.union(currentSite1D, newSite1D);
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
				fullGrid[get1DCoordinate(row, col)] == 1;
	}
	
	// is site (row, col) full?
	private boolean isFullWithoutValidate(int index) {
		return openGrid[index] == 1 && fullGrid[index] == 1;
	}
   
	// number of open sites
	public int numberOfOpenSites() {       
		return numberOfOpenSites;
	}
   
    // does the system percolate?
	public boolean percolates() {
		return isPercolates;
	}
	   
	private int get1DCoordinate(int row, int col) {
		return (row < 1 || row > n || col < 1 || col > n) ? -1 : (row - 1) * n + col - 1;
	}
	
	private void validateIndexes(int row, int col) {
		if (row < 1 || row > n || col < 1 || col > n)
			throw new IllegalArgumentException();
	}
}
