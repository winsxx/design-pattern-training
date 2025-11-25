package creational;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;


public class FactoryPattern {

  // Product: simple data source that returns next record or null if EOF
  interface DataSource {
    String read() throws Exception;
  }

  // Minimal concrete sources
  public static class FileDataSource implements DataSource {
    private final String[] lines = { "file-1", "file-2", "file-3" };
    private int i = 0;
    FileDataSource(Map<String, String> cfg) {}
    @Override
    public String read() {
      return (i < lines.length) ? lines[i++] : null;
    }
  }

  public static class KafkaDataSource implements DataSource {
    private int i = 0;
    KafkaDataSource(Map<String, String> cfg) {}
    @Override
    public String read() {
      return (++i <= 3) ? "kafka-" + i : null;
    }
  }

  public static class JdbcDataSource implements DataSource {
    private int i = 0;
    JdbcDataSource(Map<String, String> cfg) {}
    @Override
    public String read() {
      return (++i <= 2) ? "row-" + i : null;
    }
  }

  // Registry-style factory (type -> creator)
  public static class DataSourceFactory {
    private final Map<String, Function<Map<String,String>, DataSource>> registry = 
        new ConcurrentHashMap<>();

    DataSourceFactory() {
      // register built-in creators
      register("file", cfg -> new FileDataSource(cfg));
      register("kafka", cfg -> new KafkaDataSource(cfg));
      register("jdbc", cfg -> new JdbcDataSource(cfg));
    }

    public void register(String type, Function<Map<String,String>, DataSource> creator) {
      registry.put(type.toLowerCase(), creator);
    }

    public DataSource create(String type, Map<String,String> cfg) {
      Function<Map<String,String>, DataSource> c = registry.get(type.toLowerCase());
      if (c == null) throw new IllegalArgumentException("Unknown type: " + type);
      return c.apply(cfg);
    }
  }

  public static void main(String[] args) throws Exception {
    DataSourceFactory factory = new DataSourceFactory();

    // create and read a file source
    DataSource f = factory.create("file", Map.of("path","/tmp/x"));
    System.out.println("File source:");
    String r;
    while ((r = f.read()) != null) System.out.println("  " + r);

    // create and read kafka
    DataSource k = factory.create("kafka", Map.of("topic","t"));
    System.out.println("Kafka source:");
    while ((r = k.read()) != null) System.out.println("  " + r);

    // runtime registration: uppercase-file (wraps file and uppercases)
    factory.register("uppercase-file", cfg -> new DataSource() {
      private final DataSource base = new FileDataSource(cfg);
      @Override
      public String read() throws Exception {
        String v = base.read();
        return v == null ? null : v.toUpperCase();
      }
    });

    DataSource uc = factory.create("uppercase-file", Map.of());
    System.out.println("Uppercase-file source:");
    while ((r = uc.read()) != null) System.out.println("  " + r);
  }

}
