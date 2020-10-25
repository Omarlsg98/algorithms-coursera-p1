import edu.princeton.cs.algs4.WeightedQuickUnionUF;
public class Percolation {

    private boolean[][] grid;
    private final int size;
    private final WeightedQuickUnionUF uf;
    private int openSites;
    private final int []imaginaryNodes;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n){
        if(n<=0)
            throw new IllegalArgumentException("size can not be less or equal to 0");
        uf=new WeightedQuickUnionUF(n*n+2);
        size=n;
        imaginaryNodes=new int[]{n*n,n*n+1};
        grid=new boolean[n][n];
        openSites=0;
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                grid[i][j]=false;
            }
        }
    }
    private int getID(int row, int col){
        return col+row*size;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col){
        row--;
        col--;
        if(!existCoordinate(row,col))
            throw new IllegalArgumentException("row and column must be between 1 and "+ size);
        if(!isOpen(row+1, col+1)) {
            grid[row][col] = true;
            openSites++;
            connectNearSites(row, col);
        }
    }

    private boolean existCoordinate(int row, int col) {
        return row>=0 && col>=0  && row<size && col<size;
    }
    private void connectNearSites(int row, int col) {
        int[][] coords = {{row+1,col},{row-1,col},{row,col+1},{row,col-1}};
        for (int i =0;i<4;i++) {
            if (existCoordinate(coords[i][0], coords[i][1])) {
                if(isOpen(coords[i][0]+1, coords[i][1]+1)){
                    uf.union(getID(row, col), getID(coords[i][0], coords[i][1]));
                }
            }
        }
        if(row==0) {
            //creates an imaginary node (0) for the upper ones
            uf.union(imaginaryNodes[0],getID(row,col));
        }
        if(row==(size-1)){
            //creates an imaginary node (1) for the lower ones
            uf.union(imaginaryNodes[1],getID(row,col));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col){
        row--;
        col--;
        if(!existCoordinate(row,col))
            throw new IllegalArgumentException("row and column must be between 1 and "+ size);
        return grid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col){
        row--;
        col--;
        if(!existCoordinate(row,col))
            throw new IllegalArgumentException("row and column must be between 1 and "+ size);
        return uf.find(getID(row,col))== uf.find(imaginaryNodes[0]) && isOpen(row+1,col+1);
    }

    // returns the number of open sites
    public int numberOfOpenSites(){
        return openSites;
    }

    // does the system percolate?
    public boolean percolates(){
        //size+2 is the lower imaginary node if the parent of this node is (n+1) (the upper imaginay node)
        //then the system percolates
        return uf.find(imaginaryNodes[1])==uf.find(imaginaryNodes[0]);
    }

    // test client (optional)
    public static void main(String[] args){
        Percolation p=new Percolation(100);
        p.open(10,0);
        System.out.println(p.percolates());


    }
}
