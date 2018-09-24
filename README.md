# Dynamic AABB tree

A very simple implementation of a dynamic AABB tree in Java, which can be used, for instance, for broadphase collision detection or finding objects in proximity. It was written with minimal object allocation in mind, but it could probably be optimized more in a few places, but its overall performance and memory usage should be acceptable (didn't test it, though.)

The only dependency the library has is [JOML](https://github.com/JOML-CI/JOML), which is a very nice 3D math library for Java, but if there's enough demand, I could try to remove the dependency.

The code has some unit tests written and I encourage you to fiddle with the source code. If you find any bugs/suggestions, feel free to create an issue. I'll do my best to work on them in my spare time. There's also some frustum culling code in the tree, but I didn't have time to test it.

### Usage
The tree was designed to be easily used with any code that you might have. The only requirement is to implement two interfaces - `Identifiable`, which should return a unique identifier for each object that you add to the tree, and `Boundable` which should return an axis-aligned box of the object that you add to the tree. Have a look at `pl.pateman.dynamicaabbtree.TestEntity` to see an example implementation.

A single instance of `AABBTree` should **not** be used across multiple threads.

### Credits and kudos
Portions of the code were inspired by [Dyn4J's](https://github.com/dyn4j/dyn4j) `DynamicAABBTree` class. Here are a few articles that helped me understand the whole structure:

http://www.randygaul.net/2013/08/06/dynamic-aabb-tree/

https://www.azurefromthetrenches.com/introductory-guide-to-aabb-tree-collision-detection/

http://allenchou.net/2014/02/game-physics-broadphase-dynamic-aabb-tree/