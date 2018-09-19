package pl.pateman.gunwo.aabbtree;

import org.joml.AABBf;
import pl.pateman.gunwo.aabbtree.AABBTreeHeuristicFunction.HeuristicResult;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;
import static pl.pateman.gunwo.aabbtree.AABBTreeNode.INVALID_NODE_INDEX;
import static pl.pateman.gunwo.aabbtree.AABBTreeNode.LEFT_CHILD;
import static pl.pateman.gunwo.aabbtree.AABBTreeNode.RIGHT_CHILD;

/**
 * Created by pateman.
 */
public final class AABBTree<T extends Boundable> {

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

    private int addNodeAndGetIndex(AABBTreeNode<T> node) {
        nodes.add(node);
        int idx = nodes.size() - 1;
        node.setIndex(idx);
        return idx;
    }

    private AABBTreeNode<T> createLeafNode(T object) {
        AABBTreeNode<T> leafNode = new AABBTreeNode<>();
        leafNode.setData(object);
        leafNode.computeAABBWithMargin(fatAABBMargin);
        return leafNode;
    }

    private void moveNodeToParent(AABBTreeNode<T> node, int newParentIndex) {
        int oldParentIndex = node.getParent();
        if (oldParentIndex != INVALID_NODE_INDEX) {
            getNodeAt(oldParentIndex).replaceChild(node.getIndex(), newParentIndex);
        }
        node.setParent(newParentIndex);
    }

    private AABBTreeNode<T> createBranchNode(AABBTreeNode<T> childA, AABBTreeNode<T> childB) {
        AABBTreeNode<T> branchNode = new AABBTreeNode<>();
        branchNode.assignChildren(childA.getIndex(), childB.getIndex());

        int branchNodeIndex = addNodeAndGetIndex(branchNode);
        moveNodeToParent(childA, branchNodeIndex);
        moveNodeToParent(childB, branchNodeIndex);

        return branchNode;
    }

    private AABBTreeNode<T> getNodeAt(int index) {
        return nodes.get(index);
    }

    private AABBTreeNode<T> balanceRight(AABBTreeNode<T> node, AABBTreeNode<T> left, AABBTreeNode<T> right) {
        AABBTreeNode<T> rightLeftChild = getNodeAt(right.getLeftChild());
        AABBTreeNode<T> rightRightChild = getNodeAt(right.getRightChild());

        right.assignChild(LEFT_CHILD, node.getIndex());
        right.setParent(node.getParent());
        node.setParent(right.getIndex());

        if (right.getParent() != INVALID_NODE_INDEX) {
            AABBTreeNode<T> rightParent = getNodeAt(right.getParent());
            if (rightParent.getLeftChild() == node.getIndex())
            {
                rightParent.assignChild(LEFT_CHILD, right.getIndex());
            } else {
                rightParent.assignChild(RIGHT_CHILD, right.getIndex());
            }
        } else {
            root = right.getIndex();
        }

        if (rightLeftChild.getHeight() > rightRightChild.getHeight()) {
            right.assignChild(RIGHT_CHILD, rightLeftChild.getIndex());
            node.assignChild(RIGHT_CHILD, rightRightChild.getIndex());
            rightRightChild.setParent(node.getIndex());
            left.getAABB().union(rightRightChild.getAABB(), node.getAABB());
            node.getAABB().union(rightLeftChild.getAABB(), right.getAABB());
            node.setHeight(1 + max(left.getHeight(), rightRightChild.getHeight()));
            right.setHeight(1 + max(node.getHeight(), rightLeftChild.getHeight()));
        } else {
            right.assignChild(RIGHT_CHILD, rightRightChild.getIndex());
            node.assignChild(RIGHT_CHILD, rightLeftChild.getIndex());
            rightLeftChild.setParent(node.getIndex());
            left.getAABB().union(rightLeftChild.getAABB(), node.getAABB());
            node.getAABB().union(rightRightChild.getAABB(), right.getAABB());
            node.setHeight(1 + max(left.getHeight(), rightLeftChild.getHeight()));
            right.setHeight(1 + max(node.getHeight(), rightRightChild.getHeight()));
        }

        return right;
    }

    private AABBTreeNode<T> balanceLeft(AABBTreeNode<T> node, AABBTreeNode<T> left, AABBTreeNode<T> right) {
        AABBTreeNode<T> leftLeftChild = getNodeAt(left.getLeftChild());
        AABBTreeNode<T> leftRightChild = getNodeAt(left.getRightChild());

        left.assignChild(LEFT_CHILD, node.getIndex());
        left.setParent(node.getParent());
        node.setParent(left.getIndex());

        if (left.getParent() != INVALID_NODE_INDEX) {
            AABBTreeNode<T> leftParent = getNodeAt(left.getParent());
            if (leftParent.getLeftChild() == node.getIndex()) {
                leftParent.assignChild(LEFT_CHILD, left.getIndex());
            } else {
                leftParent.assignChild(RIGHT_CHILD, left.getIndex());
            }
        } else {
            root = left.getIndex();
        }

        if (leftLeftChild.getHeight() > leftRightChild.getHeight()) {
            left.assignChild(RIGHT_CHILD, leftLeftChild.getIndex());
            node.assignChild(LEFT_CHILD, leftRightChild.getIndex());
            leftRightChild.setParent(node.getIndex());
            right.getAABB().union(leftRightChild.getAABB(), node.getAABB());
            node.getAABB().union(leftLeftChild.getAABB(), left.getAABB());
            node.setHeight(1 + max(right.getHeight(), leftRightChild.getHeight()));
            left.setHeight(1 + max(node.getHeight(), leftLeftChild.getHeight()));
        } else {
            left.assignChild(RIGHT_CHILD, leftRightChild.getIndex());
            node.assignChild(LEFT_CHILD, leftLeftChild.getIndex());
            leftLeftChild.setParent(node.getIndex());
            right.getAABB().union(leftLeftChild.getAABB(), node.getAABB());
            node.getAABB().union(leftRightChild.getAABB(), left.getAABB());
            node.setHeight(1 + max(right.getHeight(), leftLeftChild.getHeight()));
            left.setHeight(1 + max(node.getHeight(), leftRightChild.getHeight()));
        }

        return left;
    }

    private AABBTreeNode<T> balanceNode(AABBTreeNode<T> node) {
        if (node.isLeaf() || node.getHeight() < 2)
        {
            return node;
        }

        AABBTreeNode<T> left = getNodeAt(node.getLeftChild());
        AABBTreeNode<T> right = getNodeAt(node.getRightChild());

        int balance = right.getHeight() - left.getHeight();

        if (balance > 1) {
            return balanceRight(node, left, right);
        }
        if (balance < -1) {
            return balanceLeft(node, left, right);
        }
        return node;
    }

    private void syncUpHierarchy(AABBTreeNode<T> startingPoint) {
        AABBTreeNode<T> node = getNodeAt(startingPoint.getParent());
        while (node != null && node.getIndex() != INVALID_NODE_INDEX) {
            node = balanceNode(node);

            AABBTreeNode<T> left = getNodeAt(node.getLeftChild());
            AABBTreeNode<T> right = getNodeAt(node.getRightChild());

            node.setHeight(1 + max(left.getHeight(), right.getHeight()));
            left.getAABB().union(right.getAABB(), node.getAABB());

            node = node.getParent() == INVALID_NODE_INDEX ? null : getNodeAt(node.getParent());
        }
    }

    private void insertNode(int node) {
        AABBTreeNode<T> nodeToAdd = getNodeAt(node);
        AABBTreeNode<T> parentNode = getNodeAt(root);

        AABBf nodeToAddAABB = nodeToAdd.getAABB();

        while (!parentNode.isLeaf()) {
            AABBTreeNode<T> leftChild = getNodeAt(parentNode.getLeftChild());
            AABBTreeNode<T> rightChild = getNodeAt(parentNode.getRightChild());
            AABBf aabbA = leftChild.getAABB();
            AABBf aabbB = rightChild.getAABB();

            HeuristicResult heuristicResult = insertionHeuristicFunction
                    .getInsertionHeuristic(aabbA, aabbB, nodeToAdd.getData(), nodeToAddAABB);
            parentNode = HeuristicResult.LEFT.equals(heuristicResult) ? leftChild : rightChild;
        }

        int oldParentIndex = parentNode.getParent();
        AABBTreeNode<T> newParent = createBranchNode(nodeToAdd, parentNode);
        newParent.setParent(oldParentIndex);
        newParent.setHeight(parentNode.getHeight() + 1);

        if (oldParentIndex == INVALID_NODE_INDEX) {
            root = newParent.getIndex();
            newParent.setParent(INVALID_NODE_INDEX);
        }

        syncUpHierarchy(nodeToAdd);
    }

    public void add(T object) {
        AABBTreeNode<T> leafNode = createLeafNode(object);

        if (root == INVALID_NODE_INDEX) {
            root = addNodeAndGetIndex(leafNode);
            return;
        }

        insertNode(addNodeAndGetIndex(leafNode));
    }

    public void clear() {
        nodes.clear();
        root = INVALID_NODE_INDEX;
    }
}
