package behaviour;

public class StrategyPattern {

  // Strategy interface
  public interface DiscountStrategy {
    double apply(double price);
  }

  // Concrete strategies
  public static class NoDiscount implements DiscountStrategy {
    @Override
    public double apply(double price) {
      return price;
    }
  }

  public static class PercentageDiscount implements DiscountStrategy {
    private final double percent; // e.g., 0.2 for 20%

    public PercentageDiscount(double percent) {
      this.percent = percent;
    }

    @Override
    public double apply(double price) {
      return price * (1 - percent);
    }
  }

  public static class FixedAmountDiscount implements DiscountStrategy {
    private final double amount;

    public FixedAmountDiscount(double amount) {
      this.amount = amount;
    }

    @Override
    public double apply(double price) {
      return Math.max(0, price - amount);
    }
  }

  // Context (uses a strategy)
  public static class ShoppingCart {
    private DiscountStrategy discount;

    public ShoppingCart(DiscountStrategy discount) {
      this.discount = discount;
    }

    public void setDiscount(DiscountStrategy discount) {
      this.discount = discount;
    }

    public double checkout(double totalPrice) {
      return discount.apply(totalPrice);
    }
  }

  public static void main(String[] args) {
    ShoppingCart cart = new ShoppingCart(new NoDiscount());
    System.out.println(cart.checkout(100)); // 100.0

    cart.setDiscount(new PercentageDiscount(0.2)); // 20% off
    System.out.println(cart.checkout(100)); // 80.0

    cart.setDiscount(new FixedAmountDiscount(15)); // $15 off
    System.out.println(cart.checkout(100)); // 85.0
  }
}


