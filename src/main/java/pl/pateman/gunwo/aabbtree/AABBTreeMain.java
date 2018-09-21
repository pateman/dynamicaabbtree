package pl.pateman.gunwo.aabbtree;

import org.joml.AABBf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pateman.
 */
public class AABBTreeMain {

  public static void main(String[] args) {
    AABBTree<Rect> aabbTree = new AABBTree<>();

    aabbTree.add(new Rect(0, 0.0f, 0.0f, 10.0f, 10.0f));
    Rect object = new Rect(1, 11.0f, 0.0f, 5.0f, 15.0f);
    aabbTree.add(object);
//    aabbTree.add(new Rect(2, -3.0f, 1.0f, 10.0f, 4.0f));
//    aabbTree.add(new Rect(3, -8.0f, 5.0f, 25.0f, 138.0f));
//    aabbTree.add(new Rect(4, -18.0f, 5.0f, 5.0f, 8.0f));
//    aabbTree.add(new Rect(5, -20.0f, 2.0f, 5.0f, 8.0f));
    aabbTree.remove(object);
    aabbTree.add(object);

    List<CollisionPair<Rect>> list = new ArrayList<>();
    aabbTree.detectCollisionPairs(list);

    List<Rect> rects = new ArrayList<>();
    aabbTree.detectOverlaps(new AABBf(-5.0f, 0.0f, 0.0f, -2.0f, 3.0f, 0.0f), rects);

    System.out.println("w");
  }

  private static class Rect implements Boundable, Identifiable {

    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final int id;

    private Rect(int id, float x, float y, float width, float height) {
      this.id = id;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
    }

    @Override
    public AABBf getAABB(AABBf dest) {
      if (dest == null)
      {
        dest = new AABBf();
      }
      dest.setMin(x, y, 0.0f);
      dest.setMax(x + width, y + height, 0.0f);
      return dest;
    }

    @Override
    public long getID()
    {
      return id;
    }
  }

}
