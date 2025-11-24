package tasks.notification.solution;

import java.time.LocalDateTime;

public class JsonFormatter implements FormattingStrategy {
  @Override
  public String format(String message, String severity) {
    String timestamp = LocalDateTime.now().toString();
    return String.format("{ \"timestamp\": \"%s\", \"level\": \"%s\", \"content\": \"%s\" }",
        timestamp, severity, message);
  }
}
