import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class BruteCollinearPoints {
    private LineSegment[] segments;
    private int numberSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException("Not null argument");
        Point[] pts = points.clone();
        for (int i = 0; i < pts.length; i++)
            if (pts[i] == null)
                throw new IllegalArgumentException("Not allowed nulls in array");
        Arrays.sort(pts);
        for (int i = 0; i < pts.length; i++) {
            if (i < pts.length - 1)
                if (pts[i].compareTo(pts[i + 1]) == 0)
                    throw new IllegalArgumentException("Duplicated pts not allowed");
        }


        segments = new LineSegment[1];
        numberSegments = 0;
        for (int i = 0; i < pts.length - 3; i++) {
            for (int j = i + 1; j < pts.length - 2; j++) {
                for (int k = j + 1; k < pts.length - 1; k++) {
                    for (int l = k + 1; l < pts.length; l++) {
                        Comparator<Point> comp = pts[i].slopeOrder();
                        if (comp.compare(pts[j], pts[k]) == 0 && comp.compare(pts[k], pts[l]) == 0) {
                            //StdOut.println(pts[i] + " " + pts[j] + " " + pts[k] + " " + pts[l]);
                            //StdOut.println(pts[i].slopeTo(pts[j]) + "_" + pts[i].slopeTo(pts[k]) + "_" + pts[i].slopeTo(pts[l]));
                            segments[numberSegments++] = new LineSegment(pts[i], pts[l]);
                            if (numberSegments == segments.length)
                                resize(segments.length * 2);
                        }
                    }
                }
            }
        }
        resize(numberSegments);
    }

    private void resize(int capacity) {
        LineSegment[] copy = new LineSegment[capacity];
        int to = Math.min(segments.length, capacity);
        for (int i = 0; i < to; i++) {
            copy[i] = segments[i];
        }
        segments = copy;
    }

    // the number of line segments
    public int numberOfSegments() {
        return numberSegments;
    }

    // the line segments
    public LineSegment[] segments() {
        return segments;
    }

    public static void main(String[] args) {
        int[] xs = {1, 2, 3, 4, 3, 6, 9, 12, 7, 7, 7, 7, 1, 2, 3, 4};
        int[] ys = {1, 2, 3, 4, 1, 2, 3, 4, 5, 3, 4, 1, 9, 9, 9, 9};
        Point[] points = new Point[xs.length];

        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 10);
        StdDraw.setYscale(0, 10);
        StdDraw.setPenRadius(0.01);
        for (int i = 0; i < xs.length; i++) {
            points[i] = new Point(xs[i], ys[i]);
            points[i].draw();
            StdOut.print(points[i] + " ");
        }
        StdDraw.show();

        StdDraw.setPenRadius();
        BruteCollinearPoints bcp = new BruteCollinearPoints(points);
        StdOut.println("\n" + bcp.numberSegments);
        LineSegment[] segments = bcp.segments();
        for (int i = 0; i < segments.length; i++) {
            segments[i].draw();
            StdOut.println(segments[i]);
        }
        StdDraw.show();
    }
}
