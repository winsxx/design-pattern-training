package behaviour;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class IteratorPattern {
  
  // Concreate collection 
  public static class Range implements Iterable<Integer> {
    private final int start, end;

    public Range(int start, int end) {
      this.start = start;
      this.end = end;
    }

    @Override
    public Iterator<Integer> iterator() {
      // Return concreate iterator
      return new Iterator<Integer>() {
        private int current = start;

        @Override
        public boolean hasNext() {
          return current < end;
        }

        @Override
        public Integer next() {
          if (!hasNext()) throw new NoSuchElementException();
          return current++;
        }
      };
    }
  }

  public static void main(String[] args) {
    for (int i : new Range(5, 11)) {
      System.out.println(i);
    }
  }
}

