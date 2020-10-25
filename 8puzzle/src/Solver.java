import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private SearchNode solution;
    private Board initial;
    private boolean isSolvable;

    private final class SearchNode implements Comparable<SearchNode> {
        final SearchNode previousNode;
        final Board board;
        final int movements;
        final int priority;
        final int manhattan;

        SearchNode(int movements, Board board, SearchNode previousNode) {
            this.movements = movements;
            this.board = board;
            this.previousNode = previousNode;
            this.manhattan = board.manhattan();
            priority = manhattan + this.movements;
        }

        @Override
        public int compareTo(SearchNode o) {
            return Integer.compare(this.priority, o.priority);
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException("Not null");
        this.initial = initial;
        int count = 0;
        MinPQ<SearchNode> pq = new MinPQ<>();
        SearchNode actual = new SearchNode(0, initial, null);
        pq.insert(actual);

        MinPQ<SearchNode> pqMirror = new MinPQ<>();
        SearchNode actualMirror = new SearchNode(0, initial.twin(), null);
        pqMirror.insert(actualMirror);
        while (true) {
            actual = pq.delMin();
            actualMirror = pqMirror.delMin();
            count++;
            if (actual.board.isGoal()) {
                solution = actual;
                isSolvable = true;
                break;
            }
            if (actualMirror.board.isGoal()) {
                isSolvable = false;
                break;
            }
            Board previous;
            Board previousMirror;
            previous = actual.previousNode == null ? actual.board : actual.previousNode.board;
            previousMirror = actualMirror.previousNode == null ? actualMirror.board : actualMirror.previousNode.board;

            for (Board n : actual.board.neighbors()) {
                if (!n.equals(previous))
                    pq.insert(new SearchNode(actual.movements + 1, n, actual));
            }
            for (Board n : actualMirror.board.neighbors()) {
                if (!n.equals(previousMirror))
                    pqMirror.insert(new SearchNode(actualMirror.movements + 1, n, actualMirror));
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        if (isSolvable)
            return solution.movements;
        return -1;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (isSolvable) {
            Stack<Board> steps = new Stack<>();
            SearchNode actual = solution;
            for (int i = 0; i <= solution.movements; i++) {
                steps.push(actual.board);
                actual = actual.previousNode;
            }
            return steps;
        }
        return null;
    }

    // test client (see below)
    public static void main(String[] args) {
        int[][] tiles = {{0, 8, 6}, {1, 4, 2}, {5, 7, 3}};
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);
        StdOut.println(solver.moves());
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
