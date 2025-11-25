public class DecoratorPattern {
  // Component
  public interface Beverage {
    String getDescription();
    double cost();
  }

  // Concrete Component
  public static class SimpleCoffee implements Beverage {
    @Override public String getDescription() { return "Simple Coffee"; }
    @Override public double cost() { return 2.0; }
  }

  // Base decorator (holds reference to wrapped Beverage)
  public static abstract class BeverageDecorator implements Beverage {
    protected final Beverage inner;
    protected BeverageDecorator(Beverage inner) { this.inner = inner; }
    @Override public String getDescription() { return inner.getDescription(); }
    @Override public double cost() { return inner.cost(); }
  }

  // Concrete decorators
  public static class MilkDecorator extends BeverageDecorator {
    public MilkDecorator(Beverage inner) { 
      super(inner); 
    }

    @Override 
    public String getDescription() { 
      return inner.getDescription() + ", Milk";
    }

    @Override
    public double cost() {
      return inner.cost() + 0.5;
    }
  }

  public static class SugarDecorator extends BeverageDecorator {
    public SugarDecorator(Beverage inner) {
      super(inner);
    }

    @Override
    public String getDescription() {
      return inner.getDescription() + ", Sugar";
    }

    @Override
    public double cost() {
      return inner.cost() + 0.2;
    }
  }

  // Demo
  public static void main(String[] args) {
    Beverage basic = new SimpleCoffee();
    System.out.println(basic.getDescription() + " -> $" + basic.cost());

    // Add milk dynamically
    Beverage withMilk = new MilkDecorator(basic);
    System.out.println(withMilk.getDescription() + " -> $" + withMilk.cost());

    // Stack sugar on top
    Beverage withMilkAndSugar = new SugarDecorator(withMilk);
    System.out.println(withMilkAndSugar.getDescription() + " -> $" + withMilkAndSugar.cost());

    // Or compose inline
    Beverage fancy = new SugarDecorator(new MilkDecorator(new SimpleCoffee()));
    System.out.println(fancy.getDescription() + " -> $" + fancy.cost());
  }
}
