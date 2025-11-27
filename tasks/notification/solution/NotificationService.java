package tasks.notification.solution;

import java.util.ArrayList;
import java.util.List;

import tasks.notification.solution.formatter.FormattingStrategy;
import tasks.notification.solution.subscriber.Subscriber;

// ==========================================
// CONTEXT (The Publisher)
// ==========================================

public class NotificationService {
  private List<Subscriber> subscribers = new ArrayList<>();
  private FormattingStrategy formatter;

  public void setStrategy(FormattingStrategy formatter) {
    this.formatter = formatter;
  }

  public void attach(Subscriber subscriber) {
    this.subscribers.add(subscriber);
  }

  public List<String> notify(String message, String severity) {
    if (formatter == null) {
      throw new IllegalStateException("No formatting strategy set");
    }

    String formattedMsg = formatter.format(message, severity);
    List<String> results = new ArrayList<>();

    for (Subscriber sub : subscribers) {
      String res = sub.update(formattedMsg, severity);
      if (res != null) {
        results.add(res);
      }
    }
    return results;
  }
}
