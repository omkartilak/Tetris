import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main extends Frame {

  private final GameBox gameBox;

  private final TetrisShapesFactory tetrisShapesFactory;

  private static final int cellSize = 30;

  private static boolean gameOver = false;

  private static final Lock keyPressedLock = new ReentrantLock();

  public static void main(String[] args) {
    new Main();
  }

  private Main() {
    super("Tetris");

    setSize(1000, 1000);

    setVisible(true);

    addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            dispose();
            System.exit(0);
          }
        });

    gameBox =
        new GameBox(
            new Rectangle(new Point(100, 100), 780, 780, Color.WHITE),
            new Rectangle(
                new Point(0, 0),
                (int) getSize().getWidth(),
                (int) getSize().getHeight(),
                Color.WHITE),
            getGraphics(),
            cellSize);

    tetrisShapesFactory = new TetrisShapesFactory(gameBox, getGraphics(), cellSize);
    gameBox.setActiveShape(tetrisShapesFactory.getRandomShape());
    gameBox.draw();

    KeyAdapter adapter =
        new KeyAdapter() {
          @Override
          public void keyTyped(KeyEvent keyEvent) {
            super.keyTyped(keyEvent);
          }

          @Override
          public void keyPressed(KeyEvent keyEvent) {
            keyPressedLock.lock();
            try {
              if (gameOver) {
                return;
              }
              Direction direction;
              if (keyEvent.getKeyCode() == 37) {
                direction = Direction.LEFT;
              } else if (keyEvent.getKeyCode() == 38) {
                direction = Direction.UP;
              } else if (keyEvent.getKeyCode() == 39) {
                direction = Direction.RIGHT;
              } else if (keyEvent.getKeyCode() == 40) {
                direction = Direction.DOWN;
              } else if (keyEvent.getKeyCode() == 70) {
                direction = Direction.FLIP;
              } else {
                direction = Direction.DOWN;
              }
              if ((direction != Direction.FLIP)
                  && (gameBox.getActiveShape().moveOrigin(direction, 1))) {
                gameBox.draw();
              } else if (direction == Direction.FLIP) {
                gameBox.getActiveShape().flip();
                gameBox.draw();
              } else {
                gameBox.shapeHalted();
                TetrisShape shape = tetrisShapesFactory.getRandomShape();
                gameBox.setActiveShape(shape);
                gameBox.draw();
                if (gameBox.doesCollide(shape)) {
                  getGraphics()
                      .drawString(
                          "GAME OVER",
                          gameBox.getGameBox().getLeftBottomCorner().getX(),
                          gameBox.getGameBox().getLeftBottomCorner().getY());
                  gameOver = true;
                }
              }
            } finally {
              keyPressedLock.unlock();
            }
          }

          @Override
          public void keyReleased(KeyEvent keyEvent) {
            super.keyReleased(keyEvent);
          }
        };

    addKeyListener(adapter);
    Thread velocityThread = new Thread(new VelocityThread(adapter));
    velocityThread.start();
  }

  @Override
  public void paint(Graphics g) {}
}
