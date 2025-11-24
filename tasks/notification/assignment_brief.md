# Design Pattern Challenge: The Smart Notification System (Java)

## Scenario

You have been hired to maintain a legacy notification system for a server monitoring application. Currently, the system is a "God Class" that handles everything: logic for formatting messages, logic for filtering severity, and logic for sending to different channels (Email, SMS, Disk Log).

The Product Owner wants to add new features (Slack integration, HTML formatting), but the current code is too brittle to touch without breaking existing functionality.

## The Task

Refactor the provided `LegacyNotifier.java` code. You must transition the code from a monolithic procedural style to an Object-Oriented design using Design Patterns.

**Requirements**
1. **Functionality**: The logic of the main execution block must remain exactly the same. The assertions at the bottom must still pass.
2. **Decoupling**: Separation of concerns is key. The code that formats the message should not know about the code that sends the message.
3. **Extensibility**: It should be easy to add a new formatter (e.g., XML) or a new listener (e.g., Slack) without changing the core logic.

## Required Patterns

You must use at least two of the following patterns (ideally all three):
1. **Strategy Pattern**: To handle the different ways a message can be formatted (JSON vs. Plain Text).
2. **Observer Pattern**: To handle the distribution of messages to multiple recipients (Email, SMS, Log) simultaneously.
3. **Factory Method** (Optional but recommended): To create the specific formatter or subscribers based on configuration.

## Hints

**Hint 1: The Formatter (Strategy)**
Look at the processAlert method in the LegacyNotifier. It uses if/else to decide how the string looks.
- *Tip*: Create an interface called `FormattingStrategy` with a method `String format(String message, String severity)`. Create concrete classes `TextFormatter` and `JsonFormatter` that implement this interface.

**Hint 2: The Subscribers (Observer)**
Look at the sending logic. It manually checks `if (channels.contains("email"))`.
- *Tip*: The Notification Manager should be the "Subject." The Emailer, SMSSender, and FileLogger should be "Observers" implementing a Subscriber interface. The Manager shouldn't care who is listening.

**Hint 3: Testing**
Do not delete the `verify` calls in the `main` method. The external behavior must appear unchanged to the client code