
The scenes to render are described in an `input file`, which is the only mandatory parameter
for [Render mode][1].  
Input file is provided also with comments, identified by the key symbol `#`
The three main ingredients for a scene are:

 - [materials](#materials)
 - [shapes](#shapes)
 - [camera](#camera)

[1]: basic-usage.md#render-mode

## Materials
Each object in the scene must be made of a material, which is defined by its [BRDF][2] and its emitted radiance.
Moreover, to each material you must assign a name with which you'll be able to call it
all through the file.  

### BRDF
The [BRDF][2] describes the way the light interacts with the shape, once a ray hits it. The light
can be scattered or reflected, and this behaviour affects the way we see the object.
There are 2 different [BRDF][2]s available:

#### Diffusive (Lambertian)
Represents a completely diffusive material. When the light hits, it is scattered in random directions, 
thus making the object totally opaque.


#### Specular
As the name suggests, this [BRDF][2] describes a total reflecting surface, such as the one of 
an ideal mirror. When the light hits, the ray follows the [law of reflection][3].

Both [BRDF][2]s needs also a parameter: the color of the object, which is represented by a [Pigment][4]

### Emitted Radiance
Emitted radiance too is defined by a [Pigment][4]. This value is the color in which the objects,
eventually, radiate.  
For diffusive objects, it is set as `uniform(<black>)`, but a different choice leads to
creating a source of lights in the scene.
It is always necessary to have at least one source of lights in order to see the scene.

Here are two examples of different material definitions:


=== "Diffusive (radiating)"

    ```
    material sky ( 
            diffusive ( uniform(<skyblue>) ), 
            uniform(<skyblue>)
        )
    ...
    ```

=== "Specular (not radiating)"

    ```
    material mirror (
            specular ( uniform(<silver>) ),
            uniform(<black>)
        )
    ...
    ```


<figure>
  <img src="https://raw.githubusercontent.com/AnnaPivetta/KTracer/master/images/demo.png" width="480" align="center"/>
  <figcaption>Here I must put an image with both brdfs </figcaption>
</figure>

[2]: https://en.wikipedia.org/wiki/Bidirectional_reflectance_distribution_function
[3]: https://en.wikipedia.org/wiki/Specular_reflection
[4]: Pigments/pigments.md

## Shapes
After having defined the materials, it's now time to build the scene you want to render.
This is made by placing the available [shapes][5] wherever you like, assigning them the material
they're made of.  
Not only the material is important, but also transformations play great role in the setting of the stage.
By default, each shape is placed with its center in the point (0.0, 0.0, 0.0) of the world and has a default dimension.
Both position and dimension, along with also orientation in the space, can be modified by the means of a transformation.

### Transformations
If no transformation need to be applied you can simply use the keyword `identity`, such as in the following example
```
...
sphere ( sky, identity )
...
```
#### Translation
To apply a solid translation to the selected shape the keyword is `translation([vec])`, where the vector generating the
transformation is `[vec] = [x, y, z]`, specified giving its three components between squared brackets
```
...
sphere ( sky, translation([1.0, 2.0, 0.0]) )
...
```
#### Rotations
Rotations are always with respect to one of the 3 principal axis. The angle of rotation is expressed in degrees,
and the keyword is `rotation_^`, where `^` is one of `x`, `y` or `z`, and is the axis along which the rotation occurs.  
Different basic transformation may be combined into a more complex one with the help of `*` operator. Since they are implemented
as matrices, the order in which transformations are applied is from right to left.  
In the following example a cube is rotated with respect to the z axis and translated along the x axis by 1.0:
```
...
box ( (-0.5, -0.5, -0.5),
      (0.5, 0.5, 0.5),
      minecraft,
      translation([1.0, 0.0, 0.0]) * rotation_z(30) 
    )
...
```
<figure>
  <img src="https://raw.githubusercontent.com/AnnaPivetta/KTracer/master/images/demo.png" width="480" align="center"/>
  <figcaption>Here is the result of the code above (left) vs what will result by the swapping of the 2 transformations (right) </figcaption>
</figure>


#### Scaling
The third possible transformation is the one that changes the dimension of the object. The scaling does
not have to be homogeneous and may vary along different directions. The keyword is
`scaling([vec])`, where `[vec]` components are the scale factor along each direction.
```
...
sphere ( mirror, scaling([1.0, 3.0, 1.0]) )
...
```
<figure>
  <img src="https://raw.githubusercontent.com/AnnaPivetta/KTracer/master/images/demo.png" width="480" align="center"/>
  <figcaption> Inhomogeneous scaling of a sphere leads to an ellipsoid </figcaption>
</figure>


One may also want to define a variable that represents a shape, so that this can be
called multiple times in the input file with the use of a simple name. This is extremely useful
in the creation of complex [CSG][6] shapes.
The syntax is similar to the one used for defining materials, and to call the object 
in the file it is sufficient to write the name, and the transformation to apply:
```
...
shape minecraftCube box (                    #Defining the shape
                          (-0.5, -0.5, -0.5),
                          (0.5, 0.5, 0.5),
                          minecraft,
                          identity 
                    )
                 
mincraft ( rotation_z(45) )                  #Placing the shape into the world
...
```

[5]: Shapes/shapes.md
[6]: Shapes/shapes.md#csg

## Camera
The last ingredient, before rendering your first image, is a camera from which the world is observed.
Cameras accept a transformation parameter that sets its position and orientation. It also need to know the
aspect ratio of the image, and the distance from the camera and the scree onto which the scene is projected (recommended is 1.0).  
There are two different projections available: `orthogonal` and `perspective`:

=== "Orthogonal"

    ```
    ...
    camera(
        perspective,			
        translation([-4,0,1]),
        1.0,				#Aspect Ratio
        1.0			        #Distance from screen
    )
    ```
<figure>
  <img src="https://raw.githubusercontent.com/AnnaPivetta/KTracer/master/images/demo.png" width="480" align="center"/>
  <figcaption> Orthogonal projection </figcaption>
</figure>
=== "Perspective"

    ```
    ...
    camera(
        perspective,			
        translation([-4,0,1]),
        1.0,				#Aspect Ratio
        1.0			        #Distance from screen
    )
    ```
<figure>
  <img src="https://raw.githubusercontent.com/AnnaPivetta/KTracer/master/images/demo.png" width="480" align="center"/>
  <figcaption> Perspective projection </figcaption>
</figure>