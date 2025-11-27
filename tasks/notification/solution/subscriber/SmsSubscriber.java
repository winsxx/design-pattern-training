package tasks.notification.solution.subscriber;

import java.util.List;

public class SmsSubscriber implements Subscriber {
  private List<String> historyLog;

  public SmsSubscriber(List<String> historyLog) {
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
