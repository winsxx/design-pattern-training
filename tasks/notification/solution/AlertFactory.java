package tasks.notification.solution;

import java.util.List;

public class AlertFactory {
  public static NotificationService createService(String formatType, List<String> channels, List<String> historyRef) {
    NotificationService service = new NotificationService();
    // Implement
    return service;
  }
}
