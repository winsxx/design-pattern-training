import java.util.ArrayList;
import java.util.List;

public class CompositePattern {

  // Component: interface declares common operations for both
  // simple and complex objects of a composition.
  interface FileSystemNode {
    void print(String indent);
  }

  // Leaf: simple object
  public static class FileLeaf implements FileSystemNode {
    private final String name;

    FileLeaf(String name) {
        this.name = name;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + name);
    }
  }

  // Composite: object that contains other objects
  public static class Folder implements FileSystemNode {
    private final String name;
    private final List<FileSystemNode> children = new ArrayList<>();

    Folder(String name) {
        this.name = name;
    }

    public void add(FileSystemNode node) {
        children.add(node);
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + name + "/");
        String childIndent = indent + "  ";
        for (FileSystemNode child : children) {
            child.print(childIndent);
        }
    }
  }

  public static void main(String[] args) {
      Folder root = new Folder("root");
      root.add(new FileLeaf("readme.txt"));
      root.add(new FileLeaf("notes.md"));

      Folder images = new Folder("images");
      images.add(new FileLeaf("logo.png"));
      images.add(new FileLeaf("banner.jpg"));

      root.add(images);

      root.print("");
  }
  
}

