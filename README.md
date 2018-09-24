# Dynamic AABB tree

A very simple implementation of a dynamic AABB tree in Java, which can be used, for instance, for broadphase collision detection or finding objects in proximity. It was written with minimal object allocation in mind, but it could probably be optimized more in a few places, but its overall performance and memory usage should be acceptable (didn't test it, though.)

The only dependency the library has is [JOML](https://github.com/JOML-CI/JOML), which is a very nice 3D math library for Java, but if there's enough demand, I could try to remove the dependency.

The code has some unit tests written and I encourage you to fiddle with the source code. If you find any bugs/suggestions, feel free to create an issue. I'll do my best to work on them in my spare time. There's also some frustum culling code in the tree, but I didn't have time to test it.

### Credits and kudos
Portions of the code were inspired by [Dyn4J's](https://github.com/dyn4j/dyn4j) `DynamicAABBTree` class. Here are a few articles that helped me understand the whole structure:

http://www.randygaul.net/2013/08/06/dynamic-aabb-tree/
https://www.azurefromthetrenches.com/introductory-guide-to-aabb-tree-collision-detection/
http://allenchou.net/2014/02/game-physics-broadphase-dynamic-aabb-tree/