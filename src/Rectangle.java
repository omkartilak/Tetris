import java.awt.*;

public class Rectangle {

  private final Point leftUpCorner;

  private final Point leftBottomCorner;

  private final Point rightUpCorner;

  private final Point rightBottomCorner;

  private final int width;

  private final int height;

  private final Color color;

  Rectangle(Point leftUpCorner, int width, int height, Color color) {
    this.leftUpCorner = leftUpCorner;
    this.color = color;
    this.leftBottomCorner = new Point(leftUpCorner.getX(), leftUpCorner.getY() + height);
    this.rightUpCorner = new Point(leftUpCorner.getX() + width, leftUpCorner.getY());
    this.rightBottomCorner = new Point(leftUpCorner.getX() + width, leftUpCorner.getY() + height);
    this.width = width;
    this.height = height;
  }

  private boolean contains(Point point) {
    if ((leftUpCorner.getX() > point.getX()) || (point.getX() > leftUpCorner.getX() + width)) {
      return false;
    }
    if ((leftUpCorner.getY() > point.getY()) || (point.getY() > leftUpCorner.getY() + height)) {
      return false;
    }
    return true;
  }

  boolean contains(Rectangle rectangle) {
    if (contains(rectangle.leftUpCorner)
        && contains(rectangle.leftBottomCorner)
        && contains(rectangle.rightBottomCorner)
        && contains(rectangle.rightUpCorner)) {
      return true;
    }

    return false;
  }

  public Point getLeftUpCorner() {
    return leftUpCorner;
  }

  public Point getLeftBottomCorner() {
    return leftBottomCorner;
  }

  public Point getRightUpCorner() {
    return rightUpCorner;
  }

  public Point getRightBottomCorner() {
    return rightBottomCorner;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public Color getColor() {
    return color;
  }

  @Override
  public String toString() {
    return "Rectangle{"
        + "leftUpCorner="
        + leftUpCorner
        + ", leftBottomCorner="
        + leftBottomCorner
        + ", rightUpCorner="
        + rightUpCorner
        + ", rightBottomCorner="
        + rightBottomCorner
        + ", width="
        + width
        + ", height="
        + height
        + ", color="
        + color
        + '}';
  }
}
