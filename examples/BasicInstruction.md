In this file we provide you some basic and very simple instructions you can follow to write a suitable input file.
We suggest you not to skip the tutorials!
In input files, all the line starting with '#' are considered comments.
White spaces and carriage return are always allowed between words, symbols and numbers.
We'll now go through all the elements you need to construct a scene

## Point
This is how you can specify a point (brackets are required):

    (coordinate x, coordinate y, coordinate z)
for example:
    
    (1, 2.3, 4)
    (5,3, 6.7)
    (5,
        3.2,90)
## Vector
This is how you can create a vector (square brackets are required):

    [component x, component y, component z]

for example:

    [8.7,3, 4]

## Color
This is how you can create a color (angular brackets are required):

    <r component, g component, b component>
If the color you want to specify is one of our already implemented colors you can specify it writing its name between angular brackets:

    <green>
The list of the available colors is [here](https://github.com/AnnaPivetta/KTracer/blob/master/ColorList.txt).

## Pigment
You can choose between uniform, checkered or image pigment:
### Uniform
to create a uniform pigment you can use:

    uniform(color)
and you have to specify a color between brackets, for example:

    uniform(<0.8, 0.3, 0.4>)
    uniform(<red>)
### Checkered 
If you want a chekered pigment you can use:
    
    checkered(color1, color2, number or repetitions)
exemple:
    
    checkered(<black>, <white>, 4)
### Image
and if you want an image pigment:
    
    image(file)
The file must be in pfm format and if it isn't in the folder you are working in, you have to specify its path. A simple example is:

    image("my_image.pfm")

## BRDF
### Diffusive
To create a diffusive brdf:

    diffuse(pigment)

where you have to specify a pigment between brackets

### Specular
To create a specular brdf:

    specular(pigment)
where you have to specify a pigment between brackets

### Material
The syntax you need to use to create a material is:
    
    material material_name (brdf, pigment)

The pigment you need to specify as second argument is the pigment associated to the material emission (if you use a uniform black pigment it means no emission).
Once you have created a material in the input file, you can refer to it using its name.
An exemple is:

    material beautiful_material(diffuse(uniform(<navy>)), uniform(<black>))

## Transformations
### Translation
This is how you can operate a translation:

    translation(vector)
you have to specify between brackets what is the vector you want to describe the translation, for example:

    translation([7.3, 4, 9.0])
### Rotation
This is how you can operate a rotation around x, y or z axis:

    rotation_x(angle)
    rotation_y(angle)
    rotation_z(angle)
where the angle is specified in degrees.
### Scaling
This is how you can operate a scaling transformation by a vector:

    scaling(vector)
for example:

    scaling([1, 1, 2])
will double your object's height. Remember that if you want your object to maintain its dimensions along, for example, the x direction the x component of the vector you specify must be 1 and not 0 ! 
You can combine different transformations in this way:

    transformation1*transformation2*...
For example:

    scaling([2.3, 4, 5])*translation([9,5,7]) * rotation_y(30)
If you use:
        
    identity
it means no transformation

## Plane
This is how you can add a plane to the scene:

    plane(plane_material, plane_transformation)
The default plane is the x,y plane passing through the origin.
For example:

    material plane_material(
        diffuse(checkered(<black>, <white>, 10)), uniform(<black>)
    )
    plane(plane_material, rotation_y(90))
this will create a y,z plane passing through the origin made of the material *plane_material*

## Sphere
You can add a sphere to the scene in this way:

    sphere(sphere_material, sphere_transformation)
The default sphere is centered in the origin and has radius 1. Using the transformation you can move and deform the sphere

## Box
This is how you can create a box:

    box(lower_point, upper_point, box_material, box_transformation)
where lower_point is a vertex and upper_point is the opposite one along the diagonal of the box.
Pay attention: every coordinate of the lower point must be smaller than the corresponding one in the upper point.
If this rule is not satisfied your box will be replaced with the default box:

    box((-0.5,-0.5,-0.5), (0.5,0.5,0.5), material, transformation)

## Cylinder
You can create a cylinder in this way:

    cylinder(cylinder_material, transformation)
the default cylinder has height 1 (from z=-0.5 to z=0.5), radius 1 and is centered in the origin, but you can move and deform it applying a suitable transformation.

## Union
You can add the union of two different shapes in the scene in this way:

    union(first_shape, second_shape, transformation)
the transformation specified as last argument is the transformation you want to apply to the new shape made up from the union of the two shapes specified in the first and second argument (obviously the first and second shape could have been already transformed by their constructors)
If you prefer, you can use `Union(..)` with capital letter.

## Intersection
This is how you can add the intersection of two shapes to the scene:

    intersection(first_shape, second_shape, transformation)
the transformation specified as last argument is the transformation you want to apply to the new shape made up from the union of the two shapes specified in the first and second argument (obviously the first and second shape could have been already transformed by their constructors)
If you prefer, you can use `Intersection(..)` with capital letter.

## Difference
You can add the difference between *first_shape* and *second_shape* to the scene in this way:

    difference(first_shape, second_shape, transformation)
and the transformation applies to the new shape as in the previous cases.
If you prefer, you can use `Difference(..)` with capital letter.  
  
You can also combine different CSG constructions: the shape resulting from a difference, union or intersection is all effects a shape, so you can use it as an argument in `union(..)`, `difference(..)` or `intersection(..)`.

## Camera
You need to specify a camera observing the scene. There are two type of Camera:
### Perspective camera
You can define a perspective camera looking at the scene like this:

    camera(perspective, transformation, aspect_ratio, distance)
where the second argument is the transformation you want to apply in order to move the camera from its default position, which is (0,0,0) looking in the positive direction of the x axis.
The third argument is the aspect ratio, and the last argument is the distance between the camera and the screen.
Example:

    camera(perspective, translation([-1,0,2]), 1.0, 1.0)
### Orthogonal camera
You can define an orthogonal camera looking at the scene like this:

    camera(orthogonal, transformation, aspect_ratio, distance)

