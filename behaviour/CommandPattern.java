package behaviour;

import java.util.ArrayDeque;
import java.util.Deque;

public class CommandPattern {

  // Command interface
  public interface Command {
    void execute();
    void undo();
  }

  // Receiver: holds the mutable state that commands operate on
  public static class TextEditor {
    private final StringBuilder text = new StringBuilder();

    public void insert(int pos, String s) {
      text.insert(pos, s);
    }

    public void delete(int pos, int len) {
      text.delete(pos, pos + len);
    }

    public String getText() {
      return text.toString();
    }
  }

  // Concrete command: insert text at a position
  public static class InsertCommand implements Command {
    private final TextEditor editor;
    private final int pos;
    private final String text;

    public InsertCommand(TextEditor editor, int pos, String text) {
      this.editor = editor;
      this.pos = pos;
      this.text = text;
    }

    @Override
    public void execute() {
      editor.insert(pos, text);
    }

    @Override
    public void undo() {
      editor.delete(pos, text.length());
    }
  }

  // Concrete command: delete a range of text
  public static class DeleteCommand implements Command {
    private final TextEditor editor;
    private final int pos;
    private final int len;
    private String deleted; // saved on execute() so undo() can restore it

    public DeleteCommand(TextEditor editor, int pos, int len) {
      this.editor = editor;
      this.pos = pos;
      this.len = len;
    }

    @Override
    public void execute() {
      deleted = editor.getText().substring(pos, pos + len);
      editor.delete(pos, len);
    }

    @Override
    public void undo() {
      editor.insert(pos, deleted);
    }
  }

  // Invoker: manages undo/redo history
  public static class EditorHistory {
    private final Deque<Command> undoStack = new ArrayDeque<>();
    private final Deque<Command> redoStack = new ArrayDeque<>();

    public void execute(Command cmd) {
      cmd.execute();
      undoStack.push(cmd);
      redoStack.clear(); // new action invalidates redo history
    }

    public void undo() {
      if (undoStack.isEmpty()) {
        System.out.println("Nothing to undo.");
        return;
      }
      Command cmd = undoStack.pop();
      cmd.undo();
      redoStack.push(cmd);
    }

    public void redo() {
      if (redoStack.isEmpty()) {
        System.out.println("Nothing to redo.");
        return;
      }
      Command cmd = redoStack.pop();
      cmd.execute();
      undoStack.push(cmd);
    }
  }

  public static void main(String[] args) {
    TextEditor editor = new TextEditor();
    EditorHistory history = new EditorHistory();

    history.execute(new InsertCommand(editor, 0, "Hello"));
    System.out.println(editor.getText()); // Hello

    history.execute(new InsertCommand(editor, 5, ", World"));
    System.out.println(editor.getText()); // Hello, World

    history.execute(new DeleteCommand(editor, 5, 7)); // delete ", World"
    System.out.println(editor.getText()); // Hello

    history.undo(); // restore ", World"
    System.out.println(editor.getText()); // Hello, World

    history.undo(); // undo second insert
    System.out.println(editor.getText()); // Hello

    history.redo(); // redo second insert
    System.out.println(editor.getText()); // Hello, World

    history.undo(); // undo again
    history.undo(); // undo first insert
    System.out.println(editor.getText()); // (empty)

    history.undo(); // nothing left
  }
}
