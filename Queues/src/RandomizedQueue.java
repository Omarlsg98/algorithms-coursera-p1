import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;


public class RandomizedQueue<Item> implements Iterable<Item> {
    private int tail;
    private Item[] q;
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        q = (Item[]) new Object[1];
        tail = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Not null");
        q[tail] = item;
        tail++;
        size++;
        if (size == q.length || tail == q.length) {
            resize(2 * size);
        }
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        int current = 0;
        for (Item item : q) {
            if (item != null) {
                copy[current] = item;
                current++;
            }
        }
        q = copy;
        tail = size;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty())
            throw new java.util.NoSuchElementException("The queue is empty");
        return getItem(true);
    }

    private Item getItem(boolean borrar) {
        Item item = null;
        boolean continuar = true;
        while (continuar) {
            int index = StdRandom.uniform(tail);
            item = q[index];
            if (item != null) {
                continuar = false;
                if (borrar) {
                    q[index] = null;
                    size--;
                    if (size > 0 && size == (q.length / 4)) {
                        resize(q.length / 2);
                    }
                }
            }
        }
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty())
            throw new java.util.NoSuchElementException("The queue is empty");
        return getItem(false);
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedIterator();
    }

    private class RandomizedIterator implements Iterator<Item> {
        int current = 0;
        int[] indicesAleatorios;

        public RandomizedIterator() {
            //Solo los indices que si tienen elemento
            indicesAleatorios = new int[size];
            int actual = 0;
            for (int i = 0; i < q.length; i++) {
                if (q[i] != null) {
                    indicesAleatorios[actual] = i;
                    actual++;
                }
            }

            //desordenando los elementos

            for (int i = indicesAleatorios.length; i > 0; i--) {
                int posicion = StdRandom.uniform(0, i);
                int tmp = indicesAleatorios[i - 1];
                indicesAleatorios[i - 1] = indicesAleatorios[posicion];
                indicesAleatorios[posicion] = tmp;
            }
        }

        // Checks if the next element exists
        public boolean hasNext() {
            return current < size;
        }

        // moves the cursor/iterator to next element
        public Item next() {
            if (!hasNext())
                throw new java.util.NoSuchElementException("There are no more items to return");
            Item item = q[indicesAleatorios[current]];
            current++;
            return item;
        }

        // Used to remove an element. Implement only if needed
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        for (int i = 0; i < 10; i++) {
            rq.enqueue(i);
        }
        for (int i = 0; i < 3; i++) {
            StdOut.println("--" + rq.dequeue());
        }
        for (Integer i : rq) {
            StdOut.println("##" + i);
        }
    }

}
