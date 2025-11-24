package tasks.notification.solution;

import java.util.List;

public class EmailSubscriber implements Subscriber {
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
