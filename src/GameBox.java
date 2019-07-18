import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameBox {

  private final Rectangle gameBox;

  private final Rectangle outerWindow;

  private final List<TetrisShape> haltedShapes;

  private final Graphics g;

  private TetrisShape activeShape;

  private final int cellSize;

  private final int cellRowCount;

  private final int cellColumnCount;

  private final TetrisShape gameBoxTetrisShape;

  private final List<List<Integer>> shapeConfig;

  private final List<List<Color>> colorConfig;

  public GameBox(Rectangle gameBox, Rectangle outerWindow, Graphics g, int cellSize) {
    this.gameBox = gameBox;
    this.outerWindow = outerWindow;
    this.g = g;
    this.cellSize = cellSize;
    if ((gameBox.getHeight() % cellSize != 0) || (gameBox.getWidth() % cellSize != 0)) {
      throw new IllegalArgumentException("cell size is not correct");
    }
    this.cellRowCount = gameBox.getHeight() / cellSize;
    this.cellColumnCount = gameBox.getWidth() / cellSize;

    haltedShapes = new ArrayList<>();
    shapeConfig = new ArrayList<>();
    colorConfig = new ArrayList<>();

    for (int i = 0; i < cellRowCount; i++) {
      List<Integer> shapeRow = new ArrayList<>();
      List<Color> colorRow = new ArrayList<>();
      for (int j = 0; j < cellColumnCount; j++) {
        shapeRow.add(0);
        colorRow.add(Color.WHITE);
      }
      shapeConfig.add(shapeRow);
      colorConfig.add(colorRow);
    }

    gameBoxTetrisShape =
        new TetrisShape(
            shapeConfig,
            colorConfig,
            new Point(gameBox.getLeftUpCorner().getX(), gameBox.getLeftUpCorner().getY()),
            this,
            g,
            cellSize);
  }

  public void setActiveShape(TetrisShape activeShape) {
    this.activeShape = activeShape;
  }

  public TetrisShape getActiveShape() {
    return activeShape;
  }

  public void shapeHalted() {
    haltedShapes.add(activeShape);
    activeShape
        .getAllRectangles()
        .forEach(
            rectangle -> {
              RowColumn rowCol = getCellRowAndColumn(rectangle);
              shapeConfig.get(rowCol.getRowNumber()).set(rowCol.getColumnNumber(), 1);
              colorConfig
                  .get(rowCol.getRowNumber())
                  .set(rowCol.getColumnNumber(), rectangle.getColor());
            });
    activeShape = null;
    try {
      reduceShape();
    } catch (Exception e) {
    }
  }

  public Rectangle getGameBox() {
    return gameBox;
  }

  public int getCellSize() {
    return cellSize;
  }

  public boolean shapeFitsInside(TetrisShape shape) {
    for (Rectangle rectangle : shape.getAllRectangles()) {
      if (!gameBox.contains(rectangle)) {
        return false;
      }
    }
    return true;
  }

  public boolean doesCollide(TetrisShape shape) {
    for (Rectangle rectangle : shape.getAllRectangles()) {
      if (doesCollide(rectangle)) {
        return true;
      }
    }
    return false;
  }

  public boolean doesCollide(Rectangle rectangle) {
    RowColumn rowColumn = getCellRowAndColumn(rectangle);
    if (shapeConfig.get(rowColumn.getRowNumber()).get(rowColumn.getColumnNumber()) == 1) {
      return true;
    }
    return false;
    /*
    for (TetrisShape shape : haltedShapes) {
      for (Rectangle shapeRectangle : shape.getAllRectangles()) {
        if (shapeRectangle.contains(rectangle)) {
          return true;
        }
      }
    }
    return false;
     */
  }

  private void reduceShape() throws InterruptedException {
    while (true) {
      boolean isReducable = false;
      int reducableRow = -1;
      for (int row = 0; row < shapeConfig.size(); row++) {
        boolean allOnes = true;
        for (int column = 0; column < shapeConfig.get(row).size(); column++) {
          if (shapeConfig.get(row).get(column) != 1) {
            allOnes = false;
          }
        }
        if (allOnes) {
          reducableRow = row;
          isReducable = true;
          break;
        }
      }

      if (isReducable) {
        List<Color> allReds = new ArrayList<>();
        for (int column = 0; column < cellColumnCount; column++) {
          allReds.add(Color.RED);
        }
        List<Color> allWhites = new ArrayList<>();
        for (int column = 0; column < cellColumnCount; column++) {
          allWhites.add(Color.WHITE);
        }
        List<Integer> allZeros = new ArrayList<>();
        for (int column = 0; column < cellColumnCount; column++) {
          allZeros.add(0);
        }

        colorConfig.set(reducableRow, allReds);
        draw();
        Thread.sleep(1000);

        shapeConfig.remove(reducableRow);
        colorConfig.remove(reducableRow);
        shapeConfig.add(0, allZeros);
        colorConfig.add(0, allWhites);
        draw();
        Thread.sleep(1000);
      } else {
        return;
      }
    }
  }

  public void draw() {
    g.clearRect(
        outerWindow.getLeftUpCorner().getX(),
        outerWindow.getLeftUpCorner().getY(),
        (int) outerWindow.getWidth(),
        outerWindow.getHeight());
    g.drawRect(
        gameBox.getLeftUpCorner().getX(),
        gameBox.getLeftUpCorner().getY(),
        gameBox.getWidth(),
        gameBox.getHeight());
    if (activeShape != null) {
      activeShape.draw();
    }
    /*haltedShapes.forEach(
    shape -> {
      shape.draw();
    });*/
    gameBoxTetrisShape.draw();
  }

  private RowColumn getCellRowAndColumn(Rectangle rectangle) {
    int rowNumber =
        (rectangle.getLeftUpCorner().getY() - gameBox.getLeftUpCorner().getY()) / cellSize;
    int columnNumber =
        (rectangle.getLeftUpCorner().getX() - gameBox.getLeftUpCorner().getX()) / cellSize;
    return new RowColumn(rowNumber, columnNumber);
  }

  private static class RowColumn {
    private final int rowNumber;

    private final int columnNumber;

    public RowColumn(int rowNumber, int columnNumber) {
      this.rowNumber = rowNumber;
      this.columnNumber = columnNumber;
    }

    public int getRowNumber() {
      return rowNumber;
    }

    public int getColumnNumber() {
      return columnNumber;
    }
  }
}
