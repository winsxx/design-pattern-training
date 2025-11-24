package tasks.notification.solution;

import java.util.List;

public class DiskLogSubscriber implements Subscriber {
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
