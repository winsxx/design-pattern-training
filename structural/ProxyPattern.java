import java.util.concurrent.ConcurrentHashMap;

public class ProxyPattern {

  // Subject interface
  public interface DataFetcher {
    String fetch(String key);
  }

  // Real subject (expensive operation)
  public static class RealDataFetcher implements DataFetcher {
    @Override
    public String fetch(String key) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
      return "VALUE_FOR_" + key + "@" + System.currentTimeMillis();
    }
  }

  // Proxy that cache results
  public static class CachedDataFetcher implements DataFetcher {
    private final DataFetcher realSubject;
    private final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

    public CachedDataFetcher(DataFetcher realSubject) {
      this.realSubject = realSubject;
    }

    @Override
    public String fetch(String key) {
      // Fast path: valid entry exists
      String existing = cache.get(key);
      if (existing != null) {
        return existing;
      }

      // Compute/replace entry atomically
      String computed = cache.compute(key, (k, old)-> {
        String freshValue = realSubject.fetch(k);
        return freshValue;
      });

      return computed;
    }
  }

  public static void main(String[] args) {
    DataFetcher realSubject = new RealDataFetcher();
    DataFetcher cacheProxy = new CachedDataFetcher(realSubject);
    
    // First call -> cache miss and slow
    long t0 = System.nanoTime();
    System.out.println("Fetch A: " + cacheProxy.fetch("A"));
    System.out.printf("Took %.1f ms%n", (System.nanoTime() - t0) / 1_000_000.0);

    // Second call immediately -> cache hit and fast
    t0 = System.nanoTime();
    System.out.println("Fetch A again: " + cacheProxy.fetch("A"));
    System.out.printf("Took %.1f ms%n", (System.nanoTime() - t0) / 1_000_000.0);
  }
}


