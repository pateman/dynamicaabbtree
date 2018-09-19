package pl.pateman.gunwo.aabbtree;

import org.joml.AABBf;

import java.util.Arrays;

final class AABBTreeNode<E extends Boundable> {
    private static final int MAX_NUM_OF_CHILDREN_PER_NODE = 2;
    static final int INVALID_NODE_INDEX = -1;
    static final int LEFT_CHILD = 0;
    static final int RIGHT_CHILD = 1;

    private final int[] children;
    private final AABBf aabb;

    private int parent;
    private int index;
    private int height;
    private E data;

    AABBTreeNode() {
        children = new int[MAX_NUM_OF_CHILDREN_PER_NODE];
        assignChildren(INVALID_NODE_INDEX, INVALID_NODE_INDEX);

        aabb = new AABBf();
        parent = INVALID_NODE_INDEX;
    }

    boolean isLeaf() {
        return getLeftChild() == INVALID_NODE_INDEX;
    }

    void replaceChild(int childIndexToReplace, int replacement) {
        if (getLeftChild() == childIndexToReplace) {
            assignChild(LEFT_CHILD, replacement);
        } else if (getRightChild() == childIndexToReplace) {
            assignChild(RIGHT_CHILD, replacement);
        }
    }

    void assignChild(int whichChild, int childIndex) {
        children[whichChild] = childIndex;
    }

    void assignChildren(int childA, int childB) {
        assignChild(LEFT_CHILD, childA);
        assignChild(RIGHT_CHILD, childB);
    }

    void computeAABBWithMargin(float margin) {
        if (data == null) {
            return;
        }
        AABBf dataAABB = data.getAABB(aabb);
        aabb.setMin(dataAABB.minX - margin, dataAABB.minY - margin, dataAABB.minZ - margin);
        aabb.setMax(dataAABB.maxX + margin, dataAABB.maxY + margin, dataAABB.maxZ + margin);
    }

    int getLeftChild() {
        return children[LEFT_CHILD];
    }

    int getRightChild() {
        return children[RIGHT_CHILD];
    }

    AABBf getAABB() {
        return aabb;
    }

    int getParent() {
        return parent;
    }

    void setParent(int parent) {
        this.parent = parent;
    }

    int getIndex() {
        return index;
    }

    void setIndex(int index) {
        this.index = index;
    }

    int getHeight() {
        return height;
    }

    void setHeight(int height) {
        this.height = height;
    }

    E getData() {
        return data;
    }

    void setData(E data) {
        this.data = data;
    }
}
