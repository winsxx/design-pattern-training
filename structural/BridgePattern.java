import java.util.Map;

public class BridgePattern {
  
  // Implementor: gateway interface (the "implementation" side of the bridge)
  public interface PaymentGateway {
    // common primitives used by Payment abstractions
    boolean charge(String account, double amount, Map<String,String> metadata);
  }

  // Concrete implementors
  public static class StripeGateway implements PaymentGateway {
    @Override
    public boolean charge(String accountId, double amount, Map<String,String> metadata) {
        System.out.println("Stripe: charging " + amount + " " + " for " + accountId);
        // call Stripe SDK here...
        return true;
    }
  }
  public static class PayPalGateway implements PaymentGateway {
    @Override
    public boolean charge(String accountId, double amount, Map<String,String> metadata) {
        System.out.println("PayPal: charging " + amount + " " + " for " + accountId);
        // call PayPal SDK here...
        return true;
    }
  }

  // Abstraction: base Payment (uses a PaymentGateway)
  public static abstract class Payment {
    protected final PaymentGateway gateway;
    
    protected Payment(PaymentGateway gateway) {
      this.gateway = gateway;
    }
    
    public abstract boolean pay(String accountId, double amount);
  }

  // Refined Abstractions (concrete payment types)
  public static class OneTimePayment extends Payment {

    public OneTimePayment(PaymentGateway gateway) {
      super(gateway);
    }

    @Override
    public boolean pay(String accountId, double amount) {
      Map<String,String> meta = Map.of("type","one-time");
      return gateway.charge(accountId, amount, meta);
    }
  }

  public static class SubscriptionPayment extends Payment {

    public SubscriptionPayment(PaymentGateway gateway) {
      super(gateway);
    }

    @Override
    public boolean pay(String accountId, double amount) {
      Map<String,String> meta = Map.of("type","subscription");
      return gateway.charge(accountId, amount, meta);
    }
  }


  public static void main(String[] args) {
    PaymentGateway stripe = new StripeGateway();
    PaymentGateway paypal = new PayPalGateway();

    Payment oneTimeViaStripe = new OneTimePayment(stripe);
    Payment subViaPayPal = new SubscriptionPayment(paypal);

    oneTimeViaStripe.pay("acct-100", 49.99); // uses Stripe implementation
    subViaPayPal.pay("acct-200", 9.99); // uses PayPal implementation
  }
}
