import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private int size;
    private Node first;
    private Node last;

    private class Node {
        Node next;
        Node before;
        Item item;

        Node(Item item, Node next, Node before) {
            this.next = next;
            this.before = before;
            this.item = item;
        }
    }

    // construct an empty deque
    public Deque() {
        size = 0;
        first = null;
        last = null;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Not null");
        if (size == 0) {
            first = new Node(item, null, null);
            last = first;
        } else {
            Node oldFirst = first;
            first = new Node(item, oldFirst, null);
            oldFirst.before = first;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Not null");
        if (size == 0) {
            last = new Node(item, null, null);
            first = last;
        } else {
            Node newLast = new Node(item, null, last);
            last.next = newLast;
            last = newLast;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty())
            throw new java.util.NoSuchElementException("The queue is empty");
        Item item = first.item;
        if (size == 1) {
            first = last = null;
        } else {
            first = first.next;
            first.before = null;
        }
        size--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty())
            throw new java.util.NoSuchElementException("The queue is empty");
        Item item = last.item;
        if (size == 1) {
            last = first = null;
        } else {
            last = last.before;
            last.next = null;
        }
        size--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeueIterator();
    }

    private class DequeueIterator implements Iterator<Item> {

        Node current = first;

        // Checks if the next element exists
        public boolean hasNext() {
            return current != null;
        }

        // moves the cursor/iterator to next element
        public Item next() {
            if (!hasNext())
                throw new java.util.NoSuchElementException("There are no more items to return");
            Item item = current.item;
            current = current.next;
            return item;
        }

        // Used to remove an element. Implement only if needed
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> myDeq = new Deque<>();
        StdOut.println(myDeq.isEmpty());
        myDeq.addFirst(1);
        myDeq.addFirst(0);
        myDeq.addLast(2);
        StdOut.println(myDeq.size());
        for (Integer i : myDeq) {
            StdOut.println("-- " + i);
        }
        StdOut.println(myDeq.removeFirst());
        StdOut.println(myDeq.removeLast());
        StdOut.println(myDeq.removeLast());
        StdOut.println(myDeq.size());
        for (Integer i : myDeq) {
            StdOut.println("-- " + i);
        }
        myDeq.addLast(5);
        myDeq.addLast(6);
        myDeq.addLast(7);
        StdOut.println(myDeq.removeLast());
    }
}
