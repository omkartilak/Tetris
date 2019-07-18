import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class VelocityThread implements Runnable {

  private final KeyAdapter adapter;

  public VelocityThread(KeyAdapter adapter) {
    this.adapter = adapter;
  }

  @Override
  public void run() {
    while (true) {
      // Send a key press for down direction (keyCode 40 = down)
      adapter.keyPressed(new KeyEvent(new Button(), 0, 0, 0, 40, (char) 0));
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
