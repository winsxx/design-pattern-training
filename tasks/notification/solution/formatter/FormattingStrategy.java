package tasks.notification.solution;

// ==========================================
// PATTERN 1: STRATEGY
// Defines a family of algorithms (formatting)
// ==========================================

public interface FormattingStrategy {
  String format(String message, String severity);
}
