package pl.pateman.gunwo.aabbtree;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AABBTreeTest
{
   @Test
   public void shouldAddAnObjectToTheTree() {
      // Given
      AABBTree<TestEntity> tree = givenTree();
      TestEntity entity = new TestEntity(1, 0.0f, 0.0f, 10.0f, 10.0f);

      // When
      tree.add(entity);

      // Then
      assertTrue(tree.contains(entity));
   }

   @Test
   public void shouldNotAddTheSameObjectTwice() {
      // Given
      AABBTree<TestEntity> tree = givenTree();
      TestEntity entity = new TestEntity(1, 0.0f, 0.0f, 10.0f, 10.0f);

      // When
      tree.add(entity);
      tree.add(entity);

      // Then
      assertEquals(1, tree.size());
      assertEquals(1, tree.getNodes().size());
      assertEquals(0, tree.getRoot());
      assertTrue(tree.contains(entity));
   }

   @Test
   public void shouldAddTwoObjectsToTheTreeByCreatingABranch() {
      // Given
      AABBTree<TestEntity> tree = givenTree();
      TestEntity entity1 = new TestEntity(1, 0.0f, 0.0f, 10.0f, 10.0f);
      TestEntity entity2 = new TestEntity(2, -5.0f, 0.0f, 10.0f, 10.0f);

      // When
      tree.add(entity1);
      tree.add(entity2);

      // Then
      assertEquals(3, tree.getNodes().size());
      assertEquals(2, tree.getRoot());
      assertFalse(tree.getNodes().get(2).isLeaf());
      assertTrue(tree.getNodes().get(1).isLeaf());
      assertTrue(tree.getNodes().get(0).isLeaf());
   }

   @Test
   public void shouldCorrectlyCalculateHeights() {
      // Given
      AABBTree<TestEntity> tree = givenTree();
      TestEntity entity1 = new TestEntity(1, 0.0f, 0.0f, 10.0f, 10.0f);
      TestEntity entity2 = new TestEntity(2, -5.0f, 0.0f, 10.0f, 10.0f);

      // When
      tree.add(entity1);
      tree.add(entity2);

      // Then
      List<AABBTreeNode<TestEntity>> nodes = tree.getNodes();
      assertEquals(1, nodes.get(2).getHeight());
      assertEquals(0, nodes.get(1).getHeight());
      assertEquals(0, nodes.get(0).getHeight());
   }

   @Test
   public void shouldCorrectlyCalculateHeightsInAMoreComplexCase() {
      // Given
      AABBTree<TestEntity> tree = givenTree();
      TestEntity entity1 = new TestEntity(1, 0.0f, 0.0f, 10.0f, 10.0f);
      TestEntity entity2 = new TestEntity(2, -5.0f, 0.0f, 10.0f, 10.0f);
      TestEntity entity3 = new TestEntity(3, 5.0f, 0.0f, 10.0f, 10.0f);

      // When
      tree.add(entity1);
      tree.add(entity2);
      tree.add(entity3);

      // Then
      List<AABBTreeNode<TestEntity>> nodes = tree.getNodes();
      AABBTreeNode<TestEntity> root = nodes.get(tree.getRoot());

      AABBTreeNode<TestEntity> rootRight = nodes.get(root.getRightChild());
      assertEquals(2, root.getHeight());
      assertEquals(0, nodes.get(root.getLeftChild()).getHeight());
      assertEquals(1, rootRight.getHeight());
      assertEquals(0, nodes.get(rootRight.getLeftChild()).getHeight());
      assertEquals(0, nodes.get(rootRight.getRightChild()).getHeight());
   }

   @Test
   public void shouldCorrectlyCalculateHeightsInAnEvenMoreComplexCase() {
      // Given
      AABBTree<TestEntity> tree = givenTree();
      TestEntity entity1 = new TestEntity(1, 0.0f, 0.0f, 10.0f, 10.0f);
      TestEntity entity2 = new TestEntity(2, -5.0f, 0.0f, 10.0f, 10.0f);
      TestEntity entity3 = new TestEntity(3, 5.0f, 0.0f, 10.0f, 10.0f);
      TestEntity entity4 = new TestEntity(4, 15.0f, 0.0f, 10.0f, 10.0f);
      TestEntity entity5 = new TestEntity(5, -25.0f, 10.0f, 10.0f, 10.0f);

      // When
      tree.add(entity1);
      tree.add(entity2);
      tree.add(entity3);
      tree.add(entity4);
      tree.add(entity5);

      // Then
      List<AABBTreeNode<TestEntity>> nodes = tree.getNodes();
      AABBTreeNode<TestEntity> root = nodes.get(tree.getRoot());
      AABBTreeNode<TestEntity> rootLeft = nodes.get(root.getLeftChild());
      AABBTreeNode<TestEntity> rootRight = nodes.get(root.getRightChild());

      // TODO Verify that everything is fine
      assertEquals(3, root.getHeight());
      assertEquals(2, rootLeft.getHeight());
      assertEquals(1, nodes.get(rootLeft.getLeftChild()).getHeight());
      assertEquals(0, nodes.get(rootLeft.getRightChild()).getHeight());
      assertEquals(1, rootRight.getHeight());
      assertEquals(0, nodes.get(rootRight.getLeftChild()).getHeight());
      assertEquals(0, nodes.get(rootRight.getRightChild()).getHeight());
   }

   @Test
   public void shouldCorrectlyCalculateHeightsAfterRemovingAnObject() {
      // Given
      AABBTree<TestEntity> tree = givenTree();
      TestEntity entity1 = new TestEntity(1, 0.0f, 0.0f, 10.0f, 10.0f);
      TestEntity entity2 = new TestEntity(2, -5.0f, 0.0f, 10.0f, 10.0f);
      TestEntity entity3 = new TestEntity(3, 5.0f, 0.0f, 10.0f, 10.0f);

      // When
      tree.add(entity1);
      tree.add(entity2);
      tree.add(entity3);
      tree.remove(entity2);

      // Then
      List<AABBTreeNode<TestEntity>> nodes = tree.getNodes();
      AABBTreeNode<TestEntity> root = nodes.get(tree.getRoot());

      assertEquals(1, root.getHeight());
      assertEquals(0, nodes.get(root.getLeftChild()).getHeight());
      assertEquals(0, nodes.get(root.getRightChild()).getHeight());
   }

   @Test
   public void shouldRemoveAnObjectFromTheTree() {
      // Given
      AABBTree<TestEntity> tree = givenTree();
      TestEntity entity = new TestEntity(1, 0.0f, 0.0f, 10.0f, 10.0f);

      // When
      tree.add(entity);
      tree.remove(entity);

      // Then
      assertFalse(tree.contains(entity));
   }

   @Test
   public void shouldRemovingAnObjectMarkANodeFree() {
      // Given
      AABBTree<TestEntity> tree = givenTree();
      TestEntity entity = new TestEntity(1, 0.0f, 0.0f, 10.0f, 10.0f);

      // When
      tree.add(entity);
      tree.remove(entity);

      // Then
      assertFalse(tree.getFreeNodes().isEmpty());
      assertEquals(0, (int) tree.getFreeNodes().peek());
   }

   @Test
   public void shouldRemovingAnObjectInAMoreComplexTreeMarkNodesFree() {
      // Given
      AABBTree<TestEntity> tree = givenTree();
      TestEntity entity1 = new TestEntity(1, 0.0f, 0.0f, 10.0f, 10.0f);
      TestEntity entity2 = new TestEntity(2, -5.0f, 0.0f, 10.0f, 10.0f);

      // When
      tree.add(entity1);
      tree.add(entity2);
      tree.remove(entity1);

      // Then
      assertFalse(tree.getFreeNodes().isEmpty());
      assertEquals(2, tree.getFreeNodes().size());
      assertEquals(1, tree.getRoot());
   }

   @Test
   public void shouldContainsReturnTrueIfAnObjectIsAddedToTheTree() {
      // Given
      AABBTree<TestEntity> tree = givenTree();
      TestEntity entity1 = new TestEntity(1, 0.0f, 0.0f, 10.0f, 10.0f);
      TestEntity entity2 = new TestEntity(2, -5.0f, 0.0f, 10.0f, 10.0f);

      // When
      tree.add(entity1);

      // Then
      assertTrue(tree.contains(entity1));
      assertFalse(tree.contains(entity2));
   }

   @Test
   public void shouldClearTheTree() {
      // Given
      AABBTree<TestEntity> tree = givenTree();
      TestEntity entity1 = new TestEntity(1, 0.0f, 0.0f, 10.0f, 10.0f);
      TestEntity entity2 = new TestEntity(2, -5.0f, 0.0f, 10.0f, 10.0f);

      // When
      tree.add(entity1);
      tree.clear();

      // Then
      assertFalse(tree.contains(entity1));
      assertFalse(tree.contains(entity2));
      assertTrue(tree.getFreeNodes().isEmpty());
      assertTrue(tree.getNodes().isEmpty());
      assertEquals(AABBTreeNode.INVALID_NODE_INDEX, tree.getRoot());
   }

   private AABBTree<TestEntity> givenTree() {
      return new AABBTree<>();
   }
}
