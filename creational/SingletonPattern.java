package creational;

public class SingletonPattern {

  public static final class LazySingleton {
    private static volatile LazySingleton instance;
    
    private LazySingleton() {
      // private constructor
    }

    public static LazySingleton getInstance() {
      if (instance == null) {
        synchronized (LazySingleton.class) {
          if (instance == null) {
            instance = new LazySingleton();
          }
        }
      }
      return instance;
    }
  }

  public static void main(String[] args) throws InterruptedException {
    System.out.println("\nMulti-thread test:");
    Runnable task = () -> {
      LazySingleton inst = LazySingleton.getInstance();
      System.out.println(Thread.currentThread().getName() +
          " -> instance hash: " + inst.hashCode());
    };

    Thread t1 = new Thread(task, "T1");
    Thread t2 = new Thread(task, "T2");
    Thread t3 = new Thread(task, "T3");

    t1.start();
    t2.start();
    t3.start();

    t1.join();
    t2.join();
    t3.join();

    System.out.println("If all hash codes are the same → singleton works correctly.");
  }
}

