package creational;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ObjectPoolPattern {

  // Reuseable dummy expensive resource
  public static class SimpleConnection {
    private final int id;
    // Pretend the object creation is expensive
    public SimpleConnection(int id) { this.id = id; }
    String query(String sql) { return "conn-" + id + " -> " + sql; }
    @Override
    public String toString() { return "conn-" + id; }
  }

  // Object Pool
  public static class ConnectionPool {
    private final int maxSize;
    private final BlockingQueue<SimpleConnection> available = new LinkedBlockingQueue<>();
    private final AtomicInteger created = new AtomicInteger(0);

    public ConnectionPool(int maxSize) {
      this.maxSize = maxSize;
    }

    // Borrow a connection (blocks if pool is at capacity)
    public SimpleConnection acquire() throws InterruptedException {
      // fast-path: if available, return immediately
      SimpleConnection c = available.poll();
      if (c != null)
        return c;
      // can we create a new one?
      synchronized (this) {
        if (created.get() < maxSize) {
          int id = created.incrementAndGet();
          return new SimpleConnection(id);
        }
      }
      // otherwise wait until someone releases one
      return available.take();
    }

    // Return it to the pool
    void release(SimpleConnection conn) {
      if (conn == null)
        return;
      available.offer(conn);
    }
  }

  public static void main(String[] args) throws InterruptedException {
    ConnectionPool pool = new ConnectionPool(2); // at most 2 connections

    Runnable job = () -> {
      try {
        SimpleConnection c = pool.acquire();
        try {
          System.out.println(Thread.currentThread().getName() + " got " + c);
          Thread.sleep(2000); // simulate work
          System.out.println(Thread.currentThread().getName() + " used " + c + 
              " -> " + c.query("SELECT 1"));
        } finally {
          pool.release(c);
          System.out.println(Thread.currentThread().getName() + " released " + c);
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    };

    ExecutorService executor = Executors.newFixedThreadPool(4);
    for (int i = 0; i < 4; i++) {
      executor.submit(job);
    }
    executor.shutdown();
  }
}
