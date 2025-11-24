package tasks.notification.solution;

import java.time.LocalDateTime;

public class TextFormatter implements FormattingStrategy {

  @Override
  public String format(String message, String severity) {
    String timestamp = LocalDateTime.now().toString();
    return String.format("[%s] %s: %s", timestamp, severity.toUpperCase(), message);
  }

}
