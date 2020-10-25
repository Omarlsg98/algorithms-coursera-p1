import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {
    private LineSegment[] segments;
    private int numberSegments;
    private Point[][] segmios;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
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
                    throw new IllegalArgumentException("Duplicated points not allowed");
        }

        // Here begins the algorithm
        segmios = new Point[1][2];
        numberSegments = 0;

        Point[] ptsCopy = new Point[pts.length];
        for (int i = 0; i < pts.length; i++)
            ptsCopy[i] = pts[i];

        for (int i = 0; i < pts.length; i++) {
            Arrays.sort(ptsCopy, pts[i].slopeOrder());
            int count = 0;
            Point max = pts[i];
            Point min = pts[i];
            //StdOut.println("\n---------------------->" + pts[i]);
            for (int j = 2; j < ptsCopy.length; j++) {
                // StdOut.println(pts[i].slopeTo(ptsCopy[j]) + "-->" + ptsCopy[j]);
                if (pts[i].slopeTo(ptsCopy[j - 1]) == pts[i].slopeTo(ptsCopy[j])) {
                    count++;
                    if (min.compareTo(ptsCopy[j]) > 0) {
                        min = ptsCopy[j];
                    } else if (max.compareTo(ptsCopy[j]) < 0) {
                        max = ptsCopy[j];
                    }
                    if (count == 1) {
                        if (min.compareTo(ptsCopy[j - 1]) > 0) {
                            min = ptsCopy[j - 1];
                        } else if (max.compareTo(ptsCopy[j - 1]) < 0) {
                            max = ptsCopy[j - 1];
                        }
                    }
                } else {
                    if (count > 0) {
                        //StdOut.println("Count " + count);
                        if (count > 1) {
                            addSegment(min, max);
                            break;
                        }
                        count = 0;
                        min = pts[i];
                        max = pts[i];
                    }
                }
            }
            if (count > 1) {
                //StdOut.println("Count " + count);
                addSegment(min, max);
            }
        }
        createSegments();
    }

    private void createSegments() {
        segments = new LineSegment[numberSegments];
        for (int i = 0; i < numberSegments; i++) {
            segments[i] = new LineSegment(segmios[i][0], segmios[i][1]);
        }
    }

    private void addSegment(Point point, Point point1) {
        for (int i = 0; i < numberSegments; i++) {
            if (point.compareTo(segmios[i][0]) == 0 && point1.compareTo(segmios[i][1]) == 0)
                return;
        }
        segmios[numberSegments][0] = point;
        segmios[numberSegments++][1] = point1;
        if (numberSegments == segmios.length)
            resize(segmios.length * 2);
    }

    private void resize(int capacity) {
        Point[][] copymio = new Point[capacity][2];
        int to = segmios.length;
        for (int i = 0; i < to; i++) {
            copymio[i][0] = segmios[i][0];
            copymio[i][1] = segmios[i][1];
        }
        segmios = copymio;
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
        int[] xs = {1, 2, 3, 4, 5, 6, 3, 6, 9, 12, 7, 7, 7, 7, 1, 2, 3, 4};
        int[] ys = {1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 3, 4, 1, 9, 9, 9, 9};
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
        FastCollinearPoints fcp = new FastCollinearPoints(points);
        StdOut.println("\n" + fcp.numberSegments);
        LineSegment[] segments = fcp.segments();
        for (int i = 0; i < segments.length; i++) {
            segments[i].draw();
            StdOut.println(segments[i]);
        }
        StdDraw.show();
    }
}
