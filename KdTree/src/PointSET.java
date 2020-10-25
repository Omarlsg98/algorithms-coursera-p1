import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    // construct an empty set of points
    private SET<Point2D> pointSET;

    public PointSET() {
        pointSET = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointSET.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointSET.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null not supported");
        pointSET.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null not supported");
        return pointSET.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : pointSET) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> query = new Queue<>();
        if (rect == null) throw new IllegalArgumentException("Null not supported");
        for (Point2D p : pointSET) {
            if (rect.contains(p)) {
                query.enqueue(p);
            }
        }
        if (query.isEmpty())
            return null;
        return query;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null not supported");
        if (isEmpty()) return null;
        double min = Double.MAX_VALUE;
        Point2D minP = null;
        for (Point2D pset : pointSET) {
            double distance = pset.distanceTo(p);
            if (distance < min) {
                min = distance;
                minP = pset;
            }
        }
        return minP;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        /*
        PointSET pset = new PointSET();
        while (!StdIn.isEmpty()) {
            double x = StdIn.readDouble();
            double y = StdIn.readDouble();
            pset.insert(new Point2D(x, y));
        }

        StdDraw.setScale(0, 1);

        StdDraw.setPenRadius(0.01);
        pset.draw();

        StdOut.println(pset.contains(new Point2D(0, 0)));
        StdOut.println(pset.contains(new Point2D(0.158530, 0.486901)));
        RectHV rect = new RectHV(0.3, 0.3, 0.7, 0.8);

        StdDraw.setPenRadius();
        rect.draw();

        StdDraw.setPenRadius(0.015);
        StdDraw.setPenColor(StdDraw.RED);
        for (Point2D p : pset.range(rect)) {
            p.draw();
        }

        StdDraw.setPenColor(StdDraw.BLUE);
        Point2D near = new Point2D(0.85, 0.85);
        near.draw();

        StdDraw.setPenColor(StdDraw.GREEN);
        pset.nearest(near).draw();

         */
    }
}
