public class AdapterPattern {

  // Target interface expected by client
  public interface FileStorage {
    void save(String file);  
  }

  // Adaptee: existing/third-party class we cannot change
  public static class GoogleCloudStorageLibrary {
    void uploadFile(String projectName, String file) {
      System.out.printf("File upload to GCS, project: %s, file content: %s", projectName, file);
    }
  }

  // Adapter: implements the Target and delegates to the Adaptee
  public static class GcsFileStorage implements FileStorage {
    private static final String BOOTCAMP_PROJECT = "bootcamp";
    private final GoogleCloudStorageLibrary storageLibrary;

    public GcsFileStorage(GoogleCloudStorageLibrary storageLibrary) {
      this.storageLibrary = storageLibrary;
    }

    @Override
    public void save(String file) {
      // Here we convert file format to match library requirement 
      storageLibrary.uploadFile(BOOTCAMP_PROJECT, file);
    }
  }

  public static void main(String[] args) {
    FileStorage fileStorage = new GcsFileStorage(new GoogleCloudStorageLibrary());
    // Client code
    fileStorage.save("Hello World");
  }
}





