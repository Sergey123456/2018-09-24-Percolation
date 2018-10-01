public class PercolationWithoutQuickUnionFind {
	private final 	int 					n;
	private final 	int 					plusCoordinate1D;
	private 		int[] 					openGrid;
	private 		int[] 					weightGrid;
	private 		int[]					roots;
	private 		int 					numberOfOpenSites;
	private 		boolean 				isPercolates;

	
	// create n-by-n grid, with all sites blocked
	public PercolationWithoutQuickUnionFind(int n) {
		if (n < 1)
			throw new IllegalArgumentException();
		this.n				= n;
		numberOfOpenSites	= 0;
		openGrid 			= new int[n * n + 2];
		weightGrid			= new int[n * n + 2];
		roots				= new int[n * n + 2];
		plusCoordinate1D	= n * n;
		isPercolates		= false;
		for (int i = 0; i < weightGrid.length; i++) {
			openGrid[i] 	= 0;
			weightGrid[i] 	= 1;
			roots[i] 		= i;
		}
		for (int i = 0; i < n; i++) {
			roots[i] = plusCoordinate1D;
		}
		weightGrid[plusCoordinate1D] 	= 0;
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
	}
	
	private void mergeSites(int row, int col) {
		int currentSite1D = get1DCoordinate(row, col);
		mergeSitesAndReindex(currentSite1D, get1DCoordinate(row + 1, col));
		mergeSitesAndReindex(currentSite1D, get1DCoordinate(row - 1, col));
		mergeSitesAndReindex(currentSite1D, get1DCoordinate(row, col + 1));
		mergeSitesAndReindex(currentSite1D, get1DCoordinate(row, col - 1));
	}

	private void mergeSitesAndReindex(int currentSite1D, int newSite1D) {
		if ((newSite1D == -1) || openGrid[newSite1D] == 0
				|| roots[currentSite1D] == roots[newSite1D])
			return;
		int curWeight 	= weightGrid[roots[currentSite1D]];
		int newWeight 	= weightGrid[roots[newSite1D]];
		int curRoot 	= roots[currentSite1D];
		int newRoot 	= roots[newSite1D];
		int loseRoot	= -1;
		int winRoot;
		
		if (curRoot == plusCoordinate1D) {
			loseRoot 	= newRoot;
			winRoot		= curRoot;
		} else if (newRoot == plusCoordinate1D) {
			loseRoot 	= curRoot;
			winRoot		= newRoot;
		} else if (curWeight > newWeight) {
			loseRoot 	= newRoot;
			winRoot		= curRoot;
		} else {
			loseRoot 	= curRoot;
			winRoot		= newRoot;
		}
		int i = 0;
		int j = 0;
		int k = weightGrid[loseRoot];
		while (j < k || i < roots.length - 2) {
			if (roots[i] == loseRoot) {
				roots[i] = winRoot;
				j++;
			}
			i++;
		}
		weightGrid[winRoot] = curWeight + newWeight;
	}

	// is site (row, col) open?
	public boolean isOpen(int row, int col) {
		validateIndexes(row, col);
		return openGrid[get1DCoordinate(row, col)] == 1;
	}

   	// is site (row, col) full?
	public boolean isFull(int row, int col) {
		validateIndexes(row, col);
		return roots[get1DCoordinate(row, col)] == plusCoordinate1D 
				&& openGrid[get1DCoordinate(row, col)] == 1;
	}
   
	// number of open sites
	public int numberOfOpenSites() {       
		return numberOfOpenSites;
	}
   
    // does the system percolate?
	public boolean percolates() {
		if (isPercolates)
			return true;
		for (int i = n * (n - 1); i < n * n; i++) {
			if (isFull(i / n + 1, i % n + 1)) {
				isPercolates = true;
				return true;
			}
		}
		return false;
	}
	   
	private int get1DCoordinate(int row, int col) {
		return (row < 1 || row > n || col < 1 || col > n) ? -1 : (row - 1) * n + col - 1;
	}
	
	private void validateIndexes(int row, int col) {
		if (row < 1 || row > n || col < 1 || col > n)
			throw new IllegalArgumentException();
	}
	
	
}
