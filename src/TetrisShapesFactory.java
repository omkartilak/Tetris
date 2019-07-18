import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class TetrisShapesFactory {

  private static final List<List<List<Integer>>> shapeConfigs =
      Arrays.asList(
          Arrays.asList(Arrays.asList(1, 1), Arrays.asList(1, 0), Arrays.asList(1, 0)),
          Arrays.asList(Arrays.asList(0, 1), Arrays.asList(1, 1), Arrays.asList(1, 0)),
          Arrays.asList(Arrays.asList(1, 1), Arrays.asList(0, 1), Arrays.asList(0, 1)),
          Arrays.asList(Arrays.asList(1, 0), Arrays.asList(1, 1), Arrays.asList(0, 1)),
          Arrays.asList(Arrays.asList(1, 1, 1, 1)),
          Arrays.asList(Arrays.asList(1, 1), Arrays.asList(1, 1)),
          Arrays.asList(Arrays.asList(0, 1, 0), Arrays.asList(1, 1, 1)));

  private static final Random rand = new Random();

  private final GameBox gameBox;

  private final Graphics g;

  private final int cellSize;

  private static final Color shapeColor = Color.BLUE;

  private static final List<Color> shapeColors =
      Arrays.asList(
          Color.BLUE,
          Color.BLACK,
          Color.CYAN,
          Color.DARK_GRAY,
          Color.GRAY,
          Color.GREEN,
          Color.LIGHT_GRAY,
          Color.MAGENTA,
          Color.ORANGE,
          Color.PINK,
          Color.YELLOW);

  private static final Color backgroundColor = Color.WHITE;

  private static final List<Supplier<List<List<Color>>>> colorConfigProducers =
      Arrays.asList(
          () ->
              Arrays.asList(
                  Arrays.asList(getRandomColor(), getRandomColor()),
                  Arrays.asList(getRandomColor(), backgroundColor),
                  Arrays.asList(getRandomColor(), backgroundColor)),
          () ->
              Arrays.asList(
                  Arrays.asList(backgroundColor, getRandomColor()),
                  Arrays.asList(getRandomColor(), getRandomColor()),
                  Arrays.asList(getRandomColor(), backgroundColor)),
          () ->
              Arrays.asList(
                  Arrays.asList(getRandomColor(), getRandomColor()),
                  Arrays.asList(backgroundColor, getRandomColor()),
                  Arrays.asList(backgroundColor, getRandomColor())),
          () ->
              Arrays.asList(
                  Arrays.asList(getRandomColor(), backgroundColor),
                  Arrays.asList(getRandomColor(), getRandomColor()),
                  Arrays.asList(backgroundColor, getRandomColor())),
          () ->
              Arrays.asList(
                  Arrays.asList(
                      getRandomColor(), getRandomColor(), getRandomColor(), getRandomColor())),
          () ->
              Arrays.asList(
                  Arrays.asList(getRandomColor(), getRandomColor()),
                  Arrays.asList(getRandomColor(), getRandomColor())),
          () ->
              Arrays.asList(
                  Arrays.asList(backgroundColor, getRandomColor(), backgroundColor),
                  Arrays.asList(getRandomColor(), getRandomColor(), getRandomColor())));

  private static Color getRandomColor() {
    return shapeColors.get(rand.nextInt(shapeColors.size()));
  }

  public TetrisShapesFactory(GameBox gameBox, Graphics g, int cellSize) {
    this.gameBox = gameBox;
    this.g = g;
    this.cellSize = cellSize;
  }

  public TetrisShape getRandomShape() {
    while (true) {
      int randomIndex = rand.nextInt(shapeConfigs.size());
      int minX = this.gameBox.getGameBox().getLeftUpCorner().getX();
      int maxX = this.gameBox.getGameBox().getRightUpCorner().getX();
      int yValue = this.gameBox.getGameBox().getLeftUpCorner().getY();
      int randomX = rand.nextInt((maxX - minX) + 1) + minX;
      if ((randomX - minX) % gameBox.getCellSize() != 0) {
        randomX = (((randomX - minX) / gameBox.getCellSize()) + 1) * gameBox.getCellSize() + minX;
      }
      Point randomOrigin = new Point(randomX, yValue);
      TetrisShape shape =
          new TetrisShape(
              shapeConfigs.get(randomIndex),
              colorConfigProducers.get(randomIndex).get(),
              randomOrigin,
              gameBox,
              g,
              cellSize);
      /*TetrisShape shape =
      new TetrisShape(
          Arrays.asList(Arrays.asList(1, 1), Arrays.asList(1, 1)),
          colorConfigProducers.get(5).get(),
          randomOrigin,
          gameBox,
          g,
          cellSize);*/
      if (gameBox.shapeFitsInside(shape)) {
        return shape;
      }
    }
  }
}
