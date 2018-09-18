package pl.pateman.gunwo.aabbtree;

import org.joml.AABBf;
import pl.pateman.gunwo.aabbtree.AABBTreeHeuristicFunction.HeuristicResult;

import java.util.ArrayList;
import java.util.List;

import static pl.pateman.gunwo.aabbtree.AABBTreeNode.*;

/**
 * Created by pateman.
 */
public class AABBTree<T extends Boundable> {

    public static final float DEFAULT_FAT_AABB_MARGIN = 0.2f;

    private final List<AABBTreeNode<T>> nodes;
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
        if (this.insertionHeuristicFunction == null) {
            throw new IllegalArgumentException("A valid insertion heuristic function is required");
        }
        this.fatAABBMargin = fatAABBMargin;
    }

    protected int addNodeAndGetIndex(AABBTreeNode<T> node) {
        nodes.add(node);
        int idx = nodes.size() - 1;
        node.setIndex(idx);
        return idx;
    }

    protected AABBTreeNode<T> createLeafNode(T object) {
        AABBTreeNode<T> leafNode = new AABBTreeNode<>();
        leafNode.setData(object);
        leafNode.computeAABBWithMargin(fatAABBMargin);
        return leafNode;
    }

    protected void moveNodeToParent(AABBTreeNode<T> node, int newParentIndex) {
        int oldParentIndex = node.getParent();
        if (oldParentIndex != INVALID_NODE_INDEX) {
            getNodeAt(oldParentIndex).replaceChild(node.getIndex(), newParentIndex);
        }
        node.setParent(newParentIndex);
    }

    protected AABBTreeNode<T> createBranchNode(AABBTreeNode<T> childA, AABBTreeNode<T> childB) {
        AABBTreeNode<T> branchNode = new AABBTreeNode<>();
        branchNode.assignChildren(childA.getIndex(), childB.getIndex());

        int branchNodeIndex = addNodeAndGetIndex(branchNode);
        moveNodeToParent(childA, branchNodeIndex);
        moveNodeToParent(childB, branchNodeIndex);

        return branchNode;
    }

    protected AABBTreeNode<T> getNodeAt(int index) {
        return nodes.get(index);
    }

    protected void insertNode(int node) {
        AABBTreeNode<T> nodeToAdd = getNodeAt(node);
        AABBTreeNode<T> parentNode = getNodeAt(root);

        AABBf nodeToAddAABB = nodeToAdd.getAABB();

        while (!parentNode.isLeaf()) {
            int[] parentNodeChildren = parentNode.getChildren();
            AABBf aabbA = getNodeAt(parentNodeChildren[LEFT_CHILD]).getAABB();
            AABBf aabbB = getNodeAt(parentNodeChildren[RIGHT_CHILD]).getAABB();

            HeuristicResult heuristicResult = insertionHeuristicFunction
                    .getInsertionHeuristic(aabbA, aabbB, nodeToAdd.getData(), nodeToAddAABB);

            parentNode = HeuristicResult.LEFT.equals(heuristicResult) ? getNodeAt(parentNodeChildren[LEFT_CHILD]) :
                    getNodeAt(parentNodeChildren[RIGHT_CHILD]);
        }

        int oldParentIndex = parentNode.getParent();
        AABBTreeNode<T> newParent = createBranchNode(nodeToAdd, parentNode);
        newParent.setParent(oldParentIndex);
        newParent.setHeight(parentNode.getHeight() + 1);

        if (oldParentIndex == INVALID_NODE_INDEX) {
            root = newParent.getIndex();
            newParent.setParent(INVALID_NODE_INDEX);
        }

        //  TODO: balance the tree
    }

    public void add(T object) {
        AABBTreeNode<T> leafNode = createLeafNode(object);

        if (root == INVALID_NODE_INDEX) {
            root = addNodeAndGetIndex(leafNode);
            return;
        }

        insertNode(addNodeAndGetIndex(leafNode));
    }

}
