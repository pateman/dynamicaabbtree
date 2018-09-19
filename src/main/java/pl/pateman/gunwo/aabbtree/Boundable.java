package pl.pateman.gunwo.aabbtree;


import org.joml.AABBf;

/**
 * Created by pateman.
 */
public interface Boundable {

  AABBf getAABB(AABBf dest);
}
