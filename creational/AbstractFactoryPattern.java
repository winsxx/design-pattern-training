package creational;

public class AbstractFactoryPattern {
  // Product interfaces
  public interface Button {
    void paint();
  }
  public interface Checkbox {
    void paint();
  }

  // Concrete products for Windows
  public static class WindowsButton implements Button {
    @Override
    public void paint() {
      System.out.println("Rendering a Windows button.");
    }
  }
  public static class WindowsCheckbox implements Checkbox {
    @Override
    public void paint() {
      System.out.println("Rendering a Windows checkbox.");
    }
  }

  // Concrete products for MacOS
  public static class MacButton implements Button {
    @Override
    public void paint() {
      System.out.println("Rendering a MacOS button.");
    }
  }
  public static class MacCheckbox implements Checkbox {
    @Override
    public void paint() {
      System.out.println("Rendering a MacOS checkbox.");
    }
  }

  // Abstract factory : create each product in the family
  public interface UiFactory {
    Button createButton();
    Checkbox createCheckbox();
  }

  // Concrete factory for each family
  public static class WindowsFactory implements UiFactory {
    @Override
    public Button createButton() {
      return new WindowsButton();
    }
    @Override
    public Checkbox createCheckbox() {
      return new WindowsCheckbox();
    }
  }

  public static class MacFactory implements UiFactory {
    @Override
    public Button createButton() {
      return new MacButton();
    }
    @Override
    public Checkbox createCheckbox() {
      return new MacCheckbox();
    }
  }

  // Cross-platform renderer
  public static class Renderer {
    private final UiFactory factory;

    public Renderer(UiFactory factory) {
      this.factory = factory;
    }

    public void render() {
      Button button = factory.createButton();
      Checkbox checkbox = factory.createCheckbox();
      button.paint();
      checkbox.paint();
    }
  }

  public static void main(String[] args) {
    System.out.println("Using Windows family:");
    Renderer windowsRenderer = new Renderer(new WindowsFactory());
    windowsRenderer.render();

    System.out.println("\nUsing MacOS family:");
    Renderer macRenderer = new Renderer(new MacFactory());
    macRenderer.render();
  }

}

