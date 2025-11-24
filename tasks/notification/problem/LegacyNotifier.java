package tasks.notification.problem;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class LegacyNotifier {

  /*
   * TASK: Refactor this class.
   * * Currently, this class violates the Single Responsibility Principle and the
   * Open/Closed Principle.
   * It handles formatting, routing, and sending all in one place.
   * * Use Design Patterns (Strategy, Observer) to refactor this.
   */

  // Used for testing verification
  public List<String> logHistory = new ArrayList<>();

  public List<String> processAlert(String message, String severity, String formatType, List<String> channels) {
    // 1. FORMATTING LOGIC (Violates Open/Closed: need to change code to add XML)
    String formattedMsg = "";
    String timestamp = LocalDateTime.now().toString();

    if (formatType.equals("text")) {
      formattedMsg = String.format("[%s] %s: %s", timestamp, severity.toUpperCase(), message);
    } else if (formatType.equals("json")) {
      // Manual JSON construction to avoid external library dependencies for this
      // exercise
      formattedMsg = String.format("{ \"timestamp\": \"%s\", \"level\": \"%s\", \"content\": \"%s\" }",
          timestamp, severity, message);
    } else {
      throw new IllegalArgumentException("Unknown format");
    }

    // 2. SENDING LOGIC (Violates Open/Closed: need to change code to add Slack)
    // This is also tightly coupled. The Notifier knows too much about
    // implementation details of Email/SMS.
    List<String> results = new ArrayList<>();

    if (channels.contains("email")) {
      // Simulation of email logic
      String output = "Email sent to admin@company.com: " + formattedMsg;
      System.out.println(output);
      results.add("EMAIL_SENT");
      this.logHistory.add(output);
    }

    if (channels.contains("sms")) {
      if (severity.equals("critical")) {
        // Simulation of SMS logic
        String output = "SMS sent to 555-0199: " + formattedMsg;
        System.out.println(output);
        results.add("SMS_SENT");
        this.logHistory.add(output);
      } else {
        System.out.println("SMS skipped (not critical)");
      }
    }

    if (channels.contains("log")) {
      // Simulation of file writing
      String output = "Writing to disk: " + formattedMsg;
      System.out.println(output);
      results.add("LOGGED");
      this.logHistory.add(output);
    }

    return results;
  }

  // ==========================================
  // TESTING / ASSERTION SECTION
  // ==========================================
  public static void main(String[] args) {
    System.out.println("--- Starting Legacy System Tests ---");

    LegacyNotifier system = new LegacyNotifier();

    // Test Case 1: Simple Text Alert via Email and Log
    System.out.println("\nTest 1: Warning (Text) -> Email, Log");
    List<String> channels1 = new ArrayList<>();
    channels1.add("email");
    channels1.add("log");

    List<String> res1 = system.processAlert("Disk space low", "warning", "text", channels1);

    verify(res1.contains("EMAIL_SENT"), "Test 1 Failed: Email not sent");
    verify(res1.contains("LOGGED"), "Test 1 Failed: Not logged");
    verify(!res1.contains("SMS_SENT"), "Test 1 Failed: SMS sent incorrectly");
    // Check formatting verification
    verify(system.logHistory.get(0).contains("WARNING: Disk space low"), "Test 1 Failed: Wrong format");

    // Test Case 2: Critical JSON Alert via SMS and Email
    System.out.println("\nTest 2: Critical (JSON) -> SMS, Email");
    List<String> channels2 = new ArrayList<>();
    channels2.add("sms");
    channels2.add("email");

    List<String> res2 = system.processAlert("Database Crash", "critical", "json", channels2);

    verify(res2.contains("SMS_SENT"), "Test 2 Failed: SMS not sent");
    verify(res2.contains("EMAIL_SENT"), "Test 2 Failed: Email not sent");

    // Check specific JSON formatting signature in the last few logs
    boolean jsonFound = false;
    for (String log : system.logHistory) {
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

    List<String> res3 = system.processAlert("System stable", "info", "text", channels3);
    verify(!res3.contains("SMS_SENT"), "Test 3 Failed: SMS should be skipped");

    System.out.println("\nALL TESTS PASSED. The legacy code works, but it is ugly.");
  }

  // Helper to verify conditions without needing -ea VM argument
  private static void verify(boolean condition, String errorMessage) {
    if (!condition) {
      throw new RuntimeException(errorMessage);
    }
  }
}