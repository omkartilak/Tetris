import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class TetrisShape {

  private List<List<Integer>> shapeConfig;

  private List<List<Color>> colorConfig;

  private final Graphics g;

  private final GameBox gameBox;

  private Point origin;

  private final int cellSize;

  TetrisShape(
      List<List<Integer>> shapeConfig,
      List<List<Color>> colorConfig,
      Point origin,
      GameBox gameBox,
      Graphics g,
      int cellSize) {
    this.shapeConfig = shapeConfig;
    this.colorConfig = colorConfig;
    this.g = g;
    this.gameBox = gameBox;
    this.origin = origin;
    this.cellSize = cellSize;
  }

  public void draw() {
    Graphics2D g2 = (Graphics2D) g;
    getAllRectangles()
        .forEach(
            rectangle -> {
              GradientPaint colorToWhite =
                  new GradientPaint(
                      rectangle.getLeftUpCorner().getX(),
                      rectangle.getLeftUpCorner().getY(),
                      rectangle.getColor(),
                      rectangle.getRightBottomCorner().getX(),
                      rectangle.getRightBottomCorner().getY(),
                      Color.WHITE);
              Paint originalPaint = g2.getPaint();
              g2.setPaint(colorToWhite);
              g2.fill(
                  new Rectangle2D.Double(
                      rectangle.getLeftUpCorner().getX(),
                      rectangle.getLeftUpCorner().getY(),
                      rectangle.getHeight(),
                      rectangle.getWidth()));
              g2.setPaint(originalPaint);
            });
  }

  public List<Rectangle> getAllRectangles() {
    return getAllRectangles(shapeConfig, colorConfig, origin);
  }

  private List<Rectangle> getAllRectangles(
      List<List<Integer>> shapeConfig, List<List<Color>> colorConfig, Point origin) {
    List<Rectangle> result = new ArrayList<>();
    for (int row = 0; row < shapeConfig.size(); row++) {
      int rowY = origin.getY() + row * cellSize;
      for (int column = 0; column < shapeConfig.get(row).size(); column++) {
        int columnX = origin.getX() + column * cellSize;
        if (shapeConfig.get(row).get(column) == 1) {
          result.add(
              new Rectangle(
                  new Point(columnX, rowY), cellSize, cellSize, colorConfig.get(row).get(column)));
        }
      }
    }
    return result;
  }

  private boolean canDraw(
      List<List<Integer>> shapeConfig, List<List<Color>> colorConfig, Point origin) {
    boolean canDraw = true;
    for (Rectangle rectangle : getAllRectangles(shapeConfig, colorConfig, origin)) {
      canDraw =
          canDraw && gameBox.getGameBox().contains(rectangle) && !gameBox.doesCollide(rectangle);
    }
    return canDraw;
  }

  private boolean canDraw() {
    return canDraw(shapeConfig, colorConfig, origin);
  }

  public boolean setOrigin(Point coordinate) {
    Point oldOrigin = origin;
    origin = coordinate;
    if (canDraw()) {
      return true;
    } else {
      origin = oldOrigin;
      return false;
    }
  }

  public boolean moveOrigin(Direction direction, int units) {
    boolean result = true;
    switch (direction) {
      case UP:
        setOrigin(new Point(origin.getX(), origin.getY() - units * cellSize));
        break;
      case DOWN:
        result = setOrigin(new Point(origin.getX(), origin.getY() + units * cellSize));
        break;
      case LEFT:
        setOrigin(new Point(origin.getX() - units * cellSize, origin.getY()));
        break;
      case RIGHT:
        setOrigin(new Point(origin.getX() + units * cellSize, origin.getY()));
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + direction);
    }
    return result;
  }

  public void flip() {
    List<List<Integer>> transposedShapeConfig = transpose(shapeConfig, () -> 0);
    List<List<Color>> transposedColorConfig = transpose(colorConfig, () -> Color.WHITE);
    Collections.reverse(transposedShapeConfig);
    Collections.reverse(transposedColorConfig);
    if (canDraw(transposedShapeConfig, transposedColorConfig, origin)) {
      shapeConfig = transposedShapeConfig;
      colorConfig = transposedColorConfig;
    }
  }

  <T> List<List<T>> transpose(List<List<T>> array, Supplier<T> defaultValueSupplier) {
    List<List<T>> result = new ArrayList<>();
    int rowCount = array.size();
    if (rowCount == 0) {
      return result;
    }
    int columnCount = array.get(0).size();
    for (int row = 0; row < columnCount; row++) {
      List<T> transposedRow = new ArrayList<>();
      for (int column = 0; column < rowCount; column++) {
        transposedRow.add(defaultValueSupplier.get());
      }
      result.add(transposedRow);
    }
    for (int row = 0; row < columnCount; row++) {
      for (int column = 0; column < rowCount; column++) {
        result.get(row).set(column, array.get(column).get(row));
      }
    }
    return result;
  }

  @Override
  public String toString() {
    return "TetrisShape{"
        + "shapeConfig="
        + shapeConfig
        + ", colorConfig="
        + colorConfig
        + ", g="
        + g
        + ", gameBox="
        + gameBox
        + ", origin="
        + origin
        + ", cellSize="
        + cellSize
        + '}';
  }
}
