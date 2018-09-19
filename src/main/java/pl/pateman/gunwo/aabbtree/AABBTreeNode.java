package pl.pateman.gunwo.aabbtree;

import org.joml.AABBf;

import java.util.Arrays;

class AABBTreeNode<E extends Boundable> {
    static final int MAX_NUM_OF_CHILDREN_PER_NODE = 2;
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
        Arrays.fill(children, INVALID_NODE_INDEX);

        aabb = new AABBf();
        parent = INVALID_NODE_INDEX;
    }

    boolean isLeaf() {
        return children[LEFT_CHILD] == INVALID_NODE_INDEX;
    }

    void replaceChild(int childIndexToReplace, int replacement) {
        if (children[LEFT_CHILD] == childIndexToReplace) {
            assignChild(LEFT_CHILD, replacement);
        } else if (children[RIGHT_CHILD] == childIndexToReplace) {
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
        AABBf dataAABB = data.getAABB();
        this.aabb.setMin(dataAABB.minX - margin, dataAABB.minY - margin, dataAABB.minZ - margin);
        this.aabb.setMax(dataAABB.maxX + margin, dataAABB.maxY + margin, dataAABB.maxZ + margin);
    }

    int[] getChildren() {
        return children;
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
