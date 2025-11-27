package tasks.notification.solution.subscriber;

// ==========================================
// PATTERN 2: OBSERVER
// Defines a one-to-many dependency 
// ==========================================

public interface Subscriber {
  String update(String formattedMessage, String severity);
}
