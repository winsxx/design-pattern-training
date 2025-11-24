package behaviour;

import java.util.ArrayList;
import java.util.List;

public class ObserverPattern {

  // Observer
  public interface ClickListener {
    void onClick(String info);
  }

  // Concrete observers
  public static class TriggerApiClickListener implements ClickListener {
    @Override
    public void onClick(String info) {
      System.out.println("Clicked! Trigger API with info: " + info);
    }
  }

  public static class TrackerClickListener implements ClickListener {
    @Override
    public void onClick(String info) {
      System.out.println("Clicked! Track user activity with info: " + info);
    }
  }

  // Subject
  public static class Button {
    private final List<ClickListener> clickListeners = new ArrayList<>();

    public void addClickListener(ClickListener l) {
      clickListeners.add(l);
    }

    public void removeClickListener(ClickListener l) {
      clickListeners.remove(l);
    }

    // Simulate a click: notify all registered listeners (observers)
    public void click(String info) {
      for (ClickListener l : clickListeners) {
        l.onClick(info);
      }
    }
  }

  public static void main(String[] args) {
    Button button = new Button();

    // Add listeners
    ClickListener triggerApiClickListener = new TriggerApiClickListener();
    ClickListener trackerClickListener = new TrackerClickListener();
    button.addClickListener(triggerApiClickListener);
    button.addClickListener(trackerClickListener);

    System.out.println("First click (both listeners):");
    button.click("user1"); // both listeners invoked

    // remove listener A
    button.removeClickListener(trackerClickListener);

    System.out.println("Second click (after removing tracker):");
    button.click("user2"); // only trigger API listener invoked
  }
}

