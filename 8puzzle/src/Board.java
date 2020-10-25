import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

public final class Board {
    private final int N;
    private int[][] tiles;
    private final int[] twin;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        N = tiles.length;
        this.tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.tiles[i][j] = (byte) tiles[i][j];
            }
        }
        twin = getTwin();
    }


    // string representation of this board
    public String toString() {
        String str = N + "\n";
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                str += " " + tiles[i][j];
            }
            str += "\n";
        }
        return str;
    }

    // board dimension n
    public int dimension() {
        return N;
    }

    // number of tiles out of place
    public int hamming() {
        int number = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] != (N * i + j + 1) && tiles[i][j] != 0)
                    number++;
            }
        }
        return number;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int number = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] != 0) {
                    int goalRow = (tiles[i][j] - 1) / N;
                    int goalCol = (tiles[i][j] - 1) % N;
                    int diffRow = goalRow - i;
                    int diffCol = goalCol - j;
                    if (diffCol < 0)
                        diffCol *= -1;
                    if (diffRow < 0)
                        diffRow *= -1;
                    number += diffCol + diffRow;
                }
            }
        }
        return number;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null || getClass() != y.getClass()) {
            return false;
        }
        Board b = (Board) y;
        if (this.N != b.dimension())
            return false;

        return Arrays.deepEquals(this.tiles, b.tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> neighbors = new Queue<>();
        int[][] newTiles = new int[N][N];
        int[] newRows = {-1, 1, 0, 0};
        int[] newCols = {0, 0, -1, 1};
        int col0 = 0, row0 = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                newTiles[i][j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    row0 = i;
                    col0 = j;
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            newRows[i] += row0;
            newCols[i] += col0;
            if (newRows[i] >= 0 && newRows[i] < N && newCols[i] >= 0 && newCols[i] < N) {
                int temp = newTiles[newRows[i]][newCols[i]];
                newTiles[newRows[i]][newCols[i]] = 0;
                newTiles[row0][col0] = temp;
                neighbors.enqueue(new Board(newTiles));
                newTiles[newRows[i]][newCols[i]] = temp;
                newTiles[row0][col0] = 0;
            }
        }
        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int temp = tiles[twin[0]][twin[1]];
        tiles[twin[0]][twin[1]] = tiles[twin[2]][twin[3]];
        tiles[twin[2]][twin[3]] = temp;
        Board newBoard = new Board(tiles);
        tiles[twin[2]][twin[3]] = tiles[twin[0]][twin[1]];
        tiles[twin[0]][twin[1]] = temp;
        return newBoard;
    }

    private int[] getTwin() {
        int[] rows = new int[2];
        int[] cols = new int[2];
        while ((rows[1] == rows[0] && cols[1] == cols[0])
                || tiles[rows[0]][cols[0]] == 0
                || tiles[rows[1]][cols[1]] == 0) {
            rows[1] = StdRandom.uniform(N);
            rows[0] = StdRandom.uniform(N);
            cols[0] = StdRandom.uniform(N);
            cols[1] = StdRandom.uniform(N);
        }
        int[] celdas = {rows[0], cols[0], rows[1], cols[1]};
        return celdas;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};

        Board b = new Board(tiles);
        /*
        StdOut.println(b.toString());
        StdOut.println(b.hamming());
        StdOut.println(b.manhattan());
        StdOut.println(b.twin().toString());


         */

        for (Board n : b.neighbors()) {
            System.out.println(n.toString());
        }

    }
}
