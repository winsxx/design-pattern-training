package tasks.notification.solution;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class RefactoredNotifierOneFile {

  // ==========================================
  // PATTERN 1: STRATEGY
  // Defines a family of algorithms (formatting)
  // ==========================================

  interface FormattingStrategy {
    String format(String message, String severity);
  }

  static class TextFormatter implements FormattingStrategy {
    @Override
    public String format(String message, String severity) {
      String timestamp = LocalDateTime.now().toString();
      return String.format("[%s] %s: %s", timestamp, severity.toUpperCase(), message);
    }
  }

  static class JsonFormatter implements FormattingStrategy {
    @Override
    public String format(String message, String severity) {
      String timestamp = LocalDateTime.now().toString();
      return String.format("{ \"timestamp\": \"%s\", \"level\": \"%s\", \"content\": \"%s\" }",
          timestamp, severity, message);
    }
  }

  // ==========================================
  // PATTERN 2: OBSERVER
  // Defines a one-to-many dependency
  // ==========================================

  interface Subscriber {
    String update(String formattedMessage, String severity);
  }

  static class EmailSubscriber implements Subscriber {
    private List<String> historyLog;

    public EmailSubscriber(List<String> historyLog) {
      this.historyLog = historyLog;
    }

    @Override
    public String update(String formattedMessage, String severity) {
      String output = "Email sent to admin@company.com: " + formattedMessage;
      System.out.println(output);
      this.historyLog.add(output);
      return "EMAIL_SENT";
    }
  }

  static class SMSSubscriber implements Subscriber {
    private List<String> historyLog;

    public SMSSubscriber(List<String> historyLog) {
      this.historyLog = historyLog;
    }

    @Override
    public String update(String formattedMessage, String severity) {
      if ("critical".equals(severity)) {
        String output = "SMS sent to 555-0199: " + formattedMessage;
        System.out.println(output);
        this.historyLog.add(output);
        return "SMS_SENT";
      } else {
        System.out.println("SMS skipped (not critical)");
        return null;
      }
    }
  }

  static class DiskLogSubscriber implements Subscriber {
    private List<String> historyLog;

    public DiskLogSubscriber(List<String> historyLog) {
      this.historyLog = historyLog;
    }

    @Override
    public String update(String formattedMessage, String severity) {
      String output = "Writing to disk: " + formattedMessage;
      System.out.println(output);
      this.historyLog.add(output);
      return "LOGGED";
    }
  }

  // ==========================================
  // CONTEXT (The Publisher)
  // ==========================================

  static class NotificationService {
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

  // ==========================================
  // FACTORY (Helper to wire it up easily)
  // ==========================================

  static class AlertFactory {
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
        service.attach(new SMSSubscriber(historyRef));
      }
      if (channels.contains("log")) {
        service.attach(new DiskLogSubscriber(historyRef));
      }

      return service;
    }
  }

  // ==========================================
  // TESTING / ASSERTION SECTION
  // ==========================================
  public static void main(String[] args) {
    System.out.println("--- Starting Refactored System Tests ---");

    // Global history log to mimic the legacy system's verification method
    List<String> globalHistory = new ArrayList<>();

    // Test Case 1: Warning (Text) -> Email, Log
    System.out.println("\nTest 1: Warning (Text) -> Email, Log");
    List<String> channels1 = new ArrayList<>();
    channels1.add("email");
    channels1.add("log");

    NotificationService service = AlertFactory.createService("text", channels1, globalHistory);
    List<String> res1 = service.notify("Disk space low", "warning");

    verify(res1.contains("EMAIL_SENT"), "Test 1 Failed: Email not sent");
    verify(res1.contains("LOGGED"), "Test 1 Failed: Not logged");
    verify(!res1.contains("SMS_SENT"), "Test 1 Failed: SMS sent incorrectly");
    verify(globalHistory.get(0).contains("WARNING: Disk space low"), "Test 1 Failed: Wrong format");

    // Clear history for next test
    globalHistory.clear();

    // Test Case 2: Critical (JSON) -> SMS, Email
    System.out.println("\nTest 2: Critical (JSON) -> SMS, Email");
    List<String> channels2 = new ArrayList<>();
    channels2.add("sms");
    channels2.add("email");

    service = AlertFactory.createService("json", channels2, globalHistory);
    List<String> res2 = service.notify("Database Crash", "critical");

    verify(res2.contains("SMS_SENT"), "Test 2 Failed: SMS not sent");
    verify(res2.contains("EMAIL_SENT"), "Test 2 Failed: Email not sent");

    boolean jsonFound = false;
    for (String log : globalHistory) {
      if (log.contains("{") && log.contains("Database Crash")) {
        jsonFound = true;
        break;
      }
    }
    verify(jsonFound, "Test 2 Failed: JSON format not found");

    // Test Case 3: SMS skipped because not critical
    System.out.println("\nTest 3: Info (Text) -> SMS (Should skip)");
    List<String> channels3 = new ArrayList<>();
    channels3.add("sms");

    service = AlertFactory.createService("text", channels3, globalHistory);
    List<String> res3 = service.notify("System stable", "info");
    verify(!res3.contains("SMS_SENT"), "Test 3 Failed: SMS should be skipped");

    System.out.println("\nALL TESTS PASSED. The Refactored Code is Clean, Extensible, and SOLID.");
  }

  private static void verify(boolean condition, String errorMessage) {
    if (!condition) {
      throw new RuntimeException(errorMessage);
    }
  }
}
