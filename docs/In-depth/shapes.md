Shapes are the elements that interact with light, building the scene. Everything, from sky
to ground to other objects are shapes, that have been eventually transformed and combined 
to give rise to images.
There are some basic shapes available, and more complex can be obtained by [CSG][1] constructions.

[1]:#csg

## Basic
### Sphere
Sphere is by default the canonical one, with radius=1 and the center in the origin of the axes.
Any deformation must be set with a `scaling` transformation.  
The only two parameters are the `material` and the `transformation`.
    
    ...
    sphere ( sky, scaling([20.0, 20.0, 20.0]) )
    ...

### Plane
The plane by default is the _xy_ plane with _z=0_. Just like the sphere it accepts a `material` and a `transformation` parameter.
    
    ...
    plane ( ground, scaling([20.0, 20.0, 20.0]) )
    ...

### Cylinder
Default cylinders are the one with unit radius and unit height, from _z=-0.5_ to _z=0.5_. As for the previous shapes
they accept a `material` and a `transformation`.

    ...
    cylinder ( woodMat, translation([2.0, 0.0, 1.0]) )
    ...

### Box
Boxes are a little more complicated. Their dimension are given by the point of minimal 
value of vertices' coordinates and the maximum one (which are the 2 opposite vertices along one specific diagonal), even though a `scaling` transformation is always
possible. Then the usual `material` and `transformation` parameter are accepted.

    box (           
        (-0.5, -0.5, -0.5), #Min point
        (0.5, 0.5, 0.5),    #Max point
        minecraft,
        identity
    )

### Hyperboloid
[Hyperboloids][5] are shapes defined by the quadratic equation: x<sup>2</sup> + y<sup>2</sup> - z<sup>2</sup> = 1.  
The fragment of hyperboloid to be considered, is defined giving the minimum and the maximum values
of _z_ accepted. Then the usual `material` and `transformation` parameter are accepted.

    hyperboloid (           
        -0.5,   #Min Z
        0.5,    #Max Z
        marble,
        identity
    )

[5]: https://en.wikipedia.org/wiki/Hyperboloid
## CSG
Of course the infinite complexity of shapes cannot be captured by only basic shapes. [Constructive Solid Geometry][2] (CSG), does 
not provide a definitive answer to this problem, but offers a powerful tool for 
combining different basic shapes together, that can be exploited to reach exceptional results.  
The combination of shapes is based on three logical operations:

- [difference](#difference)
- [union](#union)
- [intersection](#intersection)

Remember that shape definitions can be of great help when trying to build a complex
CSG object, we recommend to give a look to its documentation in the [input file][3] section.

[2]: https://en.wikipedia.org/wiki/Constructive_solid_geometry
[3]: ../input-file.md#definitions

### Difference
Difference is the operation that, given 2 shapes, builds the object where all the point belonging
to the first shape and not to the second are considered.  
In the following example from a box, two other boxes are removed, each with a different rotation, giving 
birth to a triangular shape.

    shape baseBox box (
        (-0.05, 0.0 , 0.0),
        (0.05, 1.0 , 1.0),
        marble,
        identity
    )

    shape cutBox box (
        (-1.0, -3.0 , 0.0),
        (1.0, 3.0 , 1.0),
        marble,
        identity
    )

    shape baseCrossUnit difference(
        baseBox(identity),
        cutBox(rotation_x(63),
        identity
    )

    shape crossUnit difference (
        baseCrossUnit(identity),
        cutBox( translation([0, 1.0, 0]) * rotation_x(-63) ),
        translation([0, -0.5, -0.5)
    )

    crossUnit(identity)

<figure>
  <img src="https://github.com/AnnaPivetta/KTracer/blob/gh-pages/docs/assets/images/diffKing.png?raw=true" width="480" align="center"/>
  <figcaption> With the difference it is possible to obtain a triangular shape, which will be 
    useful for the top of the final figure </figcaption>
</figure>

### Union
Union is the operation that combines two shapes into an object resulting from taking all the points of both shapes.
It is useful to build together shapes into a unique structure, as in the example.

    shape base cylinder (
        marble,
        scaling([1.15, 1.15, 0.2]) * translation([0, 0, 0.5])
    )

    shape base2 union (
        base,
        base ( scaling([0.85, 0.85, 1.5]) * translation([0, 0, 0.2]) )
    )

    shape body union (
        base2,
        hyperboloid (
            0.5, 
            0.5,
            marble,
            translation([0, 0, 2.0]) * scaling([0.55, 0.55, 3.0])
    )

    body (identity)

<figure>
  <img src="https://github.com/AnnaPivetta/KTracer/blob/gh-pages/docs/assets/images/baseKing.png?raw=true" width="480" align="center"/>
  <figcaption> With the union some cylinders and an hyperboloid can be built up together </figcaption>
</figure>


### Intersection
Intersection is the last [CSG][2] operation available, and builds the object considering
all the points that belong to both the shapes. For example, intersecting a Cylinder and a Sphere will result in: 
    
    intersection(
        sphere( marble, scaling([3.0, 3.0, 3.0]) ),
        cylinder( marble, translation([0, 0, 2.9]) * scaling([0.5, 0.5, 0.2]) ),
        identity
    )

<figure>
  <img src="https://github.com/AnnaPivetta/KTracer/blob/gh-pages/docs/assets/images/interKing.png?raw=true" width="480" align="center"/>
  <figcaption> Intersection is what we need for the top of our figure </figcaption>
</figure>

And here is the result of a complex [CSG][2] object, build up of several combinations, and available in [KTracer][4]
as a variable of name `KTKing`.

<figure>
  <img src="https://github.com/AnnaPivetta/KTracer/blob/gh-pages/docs/assets/images/kingChess.png?raw=true" width="480" align="center"/>
  <figcaption> The King of a chess set, with marble procedural texture </figcaption>
</figure>

[4]: https://github.com/AnnaPivetta/KTracer

### Advanced CSG
Some fascinating shapes have been made available to programmers for their images:

- [`KTArmchair`][6] A nice armchair for your living room
- [`KTKing`][7] The most important piece in the chess board

If you have some interesting creations, let us know via [issues][4] via [e-mail](teo.martinelli97@gmail.com). 
If you like to share it with the community, we will provide to extend the available shapes.


[6]: https://github.com/AnnaPivetta/KTracer/raw/master/images/chair.png
[7]: https://github.com/AnnaPivetta/KTracer/blob/gh-pages/docs/assets/images/kingChess.png?raw=true