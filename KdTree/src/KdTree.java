import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    // construct an empty set of points
    private int size;
    private Node root;

    private class Node {
        Node left;
        Node right;
        Point2D point;

        public Node(Point2D p) {
            this.point = p;
        }
    }

    public KdTree() {
        size = 0;
        root = null;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null not supported");
        root = put(root, p, 0);

    }

    private Node put(Node x, Point2D p, int level) {
        if (x == null) {
            size++;
            return new Node(p);
        }
        int cmp = getComparison(level, p, x.point);
        if (cmp < 0) x.left = put(x.left, p, level + 1);
        else if (cmp > 0) x.right = put(x.right, p, level + 1);
        else if (x.point.compareTo(p) != 0) x.right = put(x.right, p, level + 1);
        return x;
    }

    private int getComparison(int level, Point2D p1, Point2D p2) {
        if (level % 2 == 0) return Double.compare(p1.x(), p2.x());
        else return Double.compare(p1.y(), p2.y());
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null not supported");
        Node x = root;
        int level = 0;
        while (x != null) {
            int cmp = getComparison(level, p, x.point);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else if (p.compareTo(x.point) == 0) return true;
            else x = x.right;
            level++;
        }
        return false;
    }


    // draw all points to standard draw
    public void draw() {
        draw(root, 0, 1, 0, 1, 0);
    }

    private void draw(Node x, double x1, double x2, double y1, double y2, int level) {
        if (x == null) return;
        //Draw point and line
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        x.point.draw();
        StdDraw.setPenRadius();
        if (level % 2 == 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.point.x(), y1, x.point.x(), y2);
            //ir a izquierdo
            draw(x.left, x1, x.point.x(), y1, y2, level + 1);
            //ir a derecho
            draw(x.right, x.point.x(), x2, y1, y2, level + 1);
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x1, x.point.y(), x2, x.point.y());
            //ir a izquierdo
            draw(x.left, x1, x2, y1, x.point.y(), level + 1);
            //ir a derecho
            draw(x.right, x1, x2, x.point.y(), y2, level + 1);
        }

    }


    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> query = new Queue<>();
        if (rect == null) throw new IllegalArgumentException("Null not supported");
        range(query, root, 0, rect);
        return query;
    }

    private void range(Queue<Point2D> queue, Node x, int level, RectHV rect) {
        if (x == null) return;
        Point2D p = x.point;
        if (rect.contains(p)) queue.enqueue(p);
        if (level % 2 == 0) {
            if (p.x() <= rect.xmax())
                range(queue, x.right, level + 1, rect);
            if (p.x() > rect.xmin())
                range(queue, x.left, level + 1, rect);
        } else {
            if (p.y() <= rect.ymax())
                range(queue, x.right, level + 1, rect);
            if (p.y() > rect.ymin())
                range(queue, x.left, level + 1, rect);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null not supported");
        if (isEmpty()) return null;
        double[] limits = {0, 0, 1, 1};
        return nearest(root, 0, root.point, p, limits);
    }

    private Point2D nearest(Node x, int level, Point2D nearest, Point2D p, double[] limits) {
        if (x == null) return nearest;
        double min = p.distanceTo(nearest);
        double distance = p.distanceTo(x.point);
        if (distance < min) {
            nearest = x.point;
        }
        RectHV rectLeft;
        RectHV rectRight;
        int cmp = 0;
        if (level % 2 == 0) {
            rectLeft = new RectHV(limits[0], limits[1], x.point.x(), limits[3]);
            rectRight = new RectHV(x.point.x(), limits[1], limits[2], limits[3]);
            cmp = Double.compare(x.point.x(), p.x());
        } else {
            rectLeft = new RectHV(limits[0], limits[1], limits[2], x.point.y());
            rectRight = new RectHV(limits[0], x.point.y(), limits[2], limits[3]);
            cmp = Double.compare(x.point.y(), p.y());
        }

        if (cmp > 0) {
            nearest = nearest(x.left, level + 1, nearest, p, rectToDouble(rectLeft));
            min = p.distanceTo(nearest);
            if (rectRight.distanceTo(p) < min)
                nearest = nearest(x.right, level + 1, nearest, p, rectToDouble(rectRight));
        } else {
            nearest = nearest(x.right, level + 1, nearest, p, rectToDouble(rectRight));
            min = p.distanceTo(nearest);
            if (rectLeft.distanceTo(p) < min)
                nearest = nearest(x.left, level + 1, nearest, p, rectToDouble(rectLeft));
        }
        return nearest;
    }

    private double[] rectToDouble(RectHV rect) {
        double[] limits = new double[4];
        limits[0] = rect.xmin();
        limits[1] = rect.ymin();
        limits[2] = rect.xmax();
        limits[3] = rect.ymax();
        return limits;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree ktree = new KdTree();

        /*

        while (!StdIn.isEmpty()) {
            double x = StdIn.readDouble();
            double y = StdIn.readDouble();
            ktree.insert(new Point2D(x, y));
        }

        StdOut.println(ktree.size());
        StdOut.println(ktree.contains(new Point2D(0, 0)));
        StdOut.println(ktree.contains(new Point2D(0.158530, 0.486901)));

        ktree.draw();

        RectHV rect = new RectHV(0.3, 0.3, 0.6, 0.6);
        StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(StdDraw.MAGENTA);
        rect.draw();

        StdDraw.setPenRadius(0.015);
        StdDraw.setPenColor(StdDraw.RED);
        for (Point2D p : ktree.range(rect)) {
            p.draw();
        }
        */

        StdDraw.setPenRadius(0.015);
        StdDraw.setPenColor(StdDraw.BLUE);
        Point2D near = new Point2D(0.1, 0.1);
        near.draw();

        StdDraw.setPenColor(StdDraw.GREEN);
        ktree.nearest(near).draw();


    }
}
