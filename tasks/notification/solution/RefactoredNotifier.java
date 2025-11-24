package tasks.notification.solution;

import java.util.ArrayList;
import java.util.List;

// ==========================================
// TESTING / ASSERTION SECTION
// ==========================================

public class RefactoredNotifier {
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