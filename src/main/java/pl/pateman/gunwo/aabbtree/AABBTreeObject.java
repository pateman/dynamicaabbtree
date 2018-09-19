package pl.pateman.gunwo.aabbtree;

import java.util.Objects;

import static pl.pateman.gunwo.aabbtree.AABBTreeNode.INVALID_NODE_INDEX;

final class AABBTreeObject<E extends Identifiable>
{
   private final E data;

   private AABBTreeObject(E data)
   {
      this.data = data;
   }

   static <E extends Identifiable> AABBTreeObject<E> create(E data) {
      return new AABBTreeObject<>(data);
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
}
