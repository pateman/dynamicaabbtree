package pl.pateman.gunwo.aabbtree;

import java.util.Objects;

import static pl.pateman.gunwo.aabbtree.AABBTreeNode.INVALID_NODE_INDEX;

final class AABBTreeObject<E extends Identifiable>
{
   private final E data;
   private final int nodeIndex;

   private AABBTreeObject(E data, int nodeIndex)
   {
      this.data = data;
      this.nodeIndex = nodeIndex;
   }

   static <E extends Identifiable> AABBTreeObject<E> create(E data) {
      return new AABBTreeObject<>(data, INVALID_NODE_INDEX);
   }

   static <E extends Identifiable> AABBTreeObject<E> create(E data, int nodeIndex) {
      return new AABBTreeObject<>(data, nodeIndex);
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o)
      {
         return true;
      }
      if (o == null || getClass() != o.getClass())
      {
         return false;
      }
      AABBTreeObject<?> that = (AABBTreeObject<?>) o;
      return Objects.equals(data.getID(), that.data.getID());
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(data.getID());
   }

   E getData()
   {
      return data;
   }

   int getNodeIndex()
   {
      return nodeIndex;
   }
}
