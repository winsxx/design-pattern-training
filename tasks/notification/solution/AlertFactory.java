package tasks.notification.solution;

import java.util.List;

import tasks.notification.solution.formatter.JsonFormatter;
import tasks.notification.solution.formatter.TextFormatter;
import tasks.notification.solution.subscriber.DiskLogSubscriber;
import tasks.notification.solution.subscriber.EmailSubscriber;
import tasks.notification.solution.subscriber.SmsSubscriber;

// ==========================================
// FACTORY (Helper to wire it up easily)
// ==========================================

public class AlertFactory {
  public static NotificationService createService(String formatType, List<String> channels, List<String> historyRef) {
    NotificationService service = new NotificationService();

    // 1. Set Strategy
    if (formatType.equals("text")) {
      service.setStrategy(new TextFormatter());
    } else if (formatType.equals("json")) {
      service.setStrategy(new JsonFormatter());
    }

    // 2. Attach Observers
    if (channels.contains("email")) {
      service.attach(new EmailSubscriber(historyRef));
    }
    if (channels.contains("sms")) {
      service.attach(new SmsSubscriber(historyRef));
    }
    if (channels.contains("log")) {
      service.attach(new DiskLogSubscriber(historyRef));
    }

    return service;
  }
}
