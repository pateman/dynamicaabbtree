package pl.pateman.gunwo.aabbtree;

import org.joml.AABBf;
import pl.pateman.gunwo.aabbtree.AABBTreeHeuristicFunction.HeuristicResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pateman.
 */
public class AABBTree<T extends Boundable> {

  public static final float DEFAULT_FAT_AABB_MARGIN = 0.2f;
  private static final int MAX_NUM_OF_CHILDREN_PER_NODE = 2;
  private static final int INVALID_NODE_INDEX = -1;
  private static final int LEFT_CHILD = 0;

  private static final int RIGHT_CHILD = 1;

  private final List<Node<T>> nodes;
  private final AABBTreeHeuristicFunction<T> insertionHeuristicFunction;

  private int root;
  private float fatAABBMargin;

  public AABBTree() {
    this(new AreaAABBHeuristicFunction<>(), DEFAULT_FAT_AABB_MARGIN);
  }

  public AABBTree(AABBTreeHeuristicFunction<T> insertionHeuristicFunction, float fatAABBMargin) {
    nodes = new ArrayList<>();
    root = INVALID_NODE_INDEX;
    this.insertionHeuristicFunction = insertionHeuristicFunction;
    this.fatAABBMargin = fatAABBMargin;
  }

  protected int addNodeAndGetIndex(Node<T> node) {
    nodes.add(node);
    int idx = nodes.size() - 1;
    node.setIndex(idx);
    return idx;
  }

  protected Node<T> createLeafNode(T object) {
    Node<T> leafNode = new Node<>();
    leafNode.setData(object);
    leafNode.computeAABBWithMargin(fatAABBMargin);
    return leafNode;
  }

  protected Node<T> createBranchNode(Node<T> childA, Node<T> childB) {
    Node<T> branchNode = new Node<>();
    branchNode.assignChildren(childA.getIndex(), childB.getIndex());

    int branchNodeIndex = addNodeAndGetIndex(branchNode);
    childA.setParent(branchNodeIndex);
    childB.setParent(branchNodeIndex);

    return branchNode;
  }

  protected Node<T> getNodeAt(int index) {
    return nodes.get(index);
  }

  protected void insertNode(int node, int parent) {
    Node<T> nodeToAdd = getNodeAt(node);
    Node<T> parentNode = getNodeAt(parent);

    AABBf nodeToAddAABB = nodeToAdd.getAABB();

    while (!parentNode.isLeaf()) {
      int[] parentNodeChildren = parentNode.getChildren();
      AABBf aabbA = getNodeAt(parentNodeChildren[LEFT_CHILD]).getAABB();
      AABBf aabbB = getNodeAt(parentNodeChildren[RIGHT_CHILD]).getAABB();

      HeuristicResult heuristicResult = insertionHeuristicFunction
         .getInsertionHeuristic(aabbA, aabbB, nodeToAdd.getData(), nodeToAddAABB);

      parentNode = HeuristicResult.LEFT.equals(heuristicResult) ? getNodeAt(parentNodeChildren[LEFT_CHILD]) : getNodeAt(parentNodeChildren[RIGHT_CHILD]);
    }

    int oldParentIndex = parentNode.getParent();
    Node<T> newParent = createBranchNode(nodeToAdd, parentNode);
    newParent.setParent(oldParentIndex);
    newParent.setHeight(parentNode.getHeight() + 1);

    if (oldParentIndex == INVALID_NODE_INDEX || oldParentIndex == root) {
      root = newParent.index;
    }

    //  TODO: balance the tree
  }

  public void add(T object) {
    Node<T> leafNode = createLeafNode(object);

    if (root == INVALID_NODE_INDEX) {
      root = addNodeAndGetIndex(leafNode);
      return;
    }

    insertNode(addNodeAndGetIndex(leafNode), root);
  }

  private class Node<E extends Boundable> {
    private final int[] children;
    private final AABBf aabb;

    private int parent;
    private int index;
    private int height;
    private E data;

    private Node() {
      children = new int[MAX_NUM_OF_CHILDREN_PER_NODE];
      Arrays.fill(children, INVALID_NODE_INDEX);

      aabb = new AABBf();
      parent = INVALID_NODE_INDEX;
    }

    private boolean isLeaf() {
      return children[LEFT_CHILD] == INVALID_NODE_INDEX;
    }

    private void assignChildren(int childA, int childB) {
      children[LEFT_CHILD] = childA;
      children[RIGHT_CHILD] = childB;
    }

    private int[] getChildren() {
      return children;
    }

    private AABBf getAABB() {
      return aabb;
    }

    private int getParent() {
      return parent;
    }

    private void setParent(int parent) {
      this.parent = parent;
    }

    private int getIndex() {
      return index;
    }

    private void setIndex(int index) {
      this.index = index;
    }

    private int getHeight() {
      return height;
    }

    private void setHeight(int height) {
      this.height = height;
    }

    private E getData() {
      return data;
    }

    private void setData(E data) {
      this.data = data;
    }

    private void computeAABBWithMargin(float margin) {
      if (data == null)
      {
        return;
      }
      AABBf dataAABB = data.getAABB();
      this.aabb.setMin(dataAABB.minX - margin, dataAABB.minY - margin, dataAABB.minZ - margin);
      this.aabb.setMax(dataAABB.maxX + margin, dataAABB.maxY + margin, dataAABB.maxZ + margin);
    }
  }

}
