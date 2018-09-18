package pl.pateman.gunwo.aabbtree;

import org.joml.AABBf;

/**
 * Created by pateman.
 */
public class AABBTreeMain {

  public static void main(String[] args) {
    AABBTree<Rect> aabbTree = new AABBTree<>();

    aabbTree.add(new Rect(0.0f, 0.0f, 10.0f, 10.0f));
    aabbTree.add(new Rect(11.0f, 0.0f, 5.0f, 15.0f));
    aabbTree.add(new Rect(-3.0f, 1.0f, 10.0f, 4.0f));
    System.out.println("w");
  }

  private static class Rect implements Boundable {

    private final float x;
    private final float y;
    private final float width;
    private final float height;

    private Rect(float x, float y, float width, float height) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
    }

    @Override
    public AABBf getAABB() {
      return new AABBf(x, y, 0.0f, x + width, y + height, 0.0f);
    }
  }

}
