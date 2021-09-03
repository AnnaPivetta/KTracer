Pigments are the objects that describe the color of the different shapes. They are used in 
[BRDF][1]s to specify both the diffuse color and the emitted one. 


[1]: ../input-file.md#brdf


### Uniform
The simplest pigment available is the `uniform` one. It is used to make the shapes appear
as homogeneously coloured objects.  
A color is defined by three floats (better if in the range [0, 1]), that describe the three components
[RBG][2], within `<>`.  
Some of the most common colors can be called also with its [HTML name][3], always included within `<>`. 
The list of the available names is updated [here][4].

    material sky ( 
            diffuse ( uniform(<skyblue>) ), 
            uniform(<skyblue>)
    )
    ...

<figure>
  <img src="https://github.com/AnnaPivetta/KTracer/blob/gh-pages/docs/assets/images/uniform.png?raw=true" width="480" align="center"/>
  <figcaption> Uniform shapes, ground and sky </figcaption>
</figure>

[2]: https://en.wikipedia.org/wiki/RGB_color_model
[3]: https://htmlcolorcodes.com/color-names/
[4]: https://github.com/AnnaPivetta/KTracer/blob/master/ColorList.txt

### Image
Not all the shapes, though, will be monochromatic. Sometimes something more complex is what you need to 
make you image appear realistic. For example, colouring a sphere with the map of the world
will make it resemble the earth.  
In this case an `image` pigment is what you need. You only need to have a _PFM_ file with the image you want to 
apply to the shape and define your material.

    material earth ( 
            diffuse ( image ( "earth.pfm" ) ), 
            uniform(<black>)
    )
    ...


<figure>
  <img src="https://github.com/AnnaPivetta/KTracer/blob/gh-pages/docs/assets/images/universe.png?raw=true" width="480" align="center"/>
  <figcaption> Earth is placed in the universe, both textured with image pigments </figcaption>
</figure>

## Procedural Pigments
Some kind of pigment can be generated with the help of mathematical functions, the evaluation of
which returns the color of each point of the surface of the object. Sometimes, with the help of a
pseudo-random noise you can generate very fancy effects!

### Checkered
This is the easiest procedural pigment and is often use in debug process. It produces a 
texture like the one of a chess board.  
Of course, you may set the 2 colors of tiles, and their number per side.
Usually this pigments makes great grounds, that let you understand better if your world is set
as you like.


    material ground ( 
            diffuse( 
                checkered( 
                    <black>,        #Color 1
                    <white>,        #Color 2
                    12              #n of tiles per side
                )
            ),
            uniform(<black>)
    )
    ...

<figure>
  <img src="https://github.com/AnnaPivetta/KTracer/raw/master/images/chair.png" width="480" align="center"/>
  <figcaption> The checkered ground makes the object more visible </figcaption>
</figure>

### Marble
Thanks to the [Perlin noise][5] it is possible to use some functions to evaluate the color 
of surface points as if the object was made of marble. Since this is an advanced pigment, 
it is provided also of a default setting that you can use by simply defining the pigment as
`marble()`.  
Otherwise, you can play with some parameters to custom your marble pigment:

- `c1` Background color of marble (recommended is white)
- `c2` Veining color of marble, you may change this to slightly modify the marble look toward this color
- `xPeriod` Number of vertical veining lines
- `yPeriod` Number of horizontal veining lines. Together with `xPeriod` defines the orientation of the stripes
- `turbPower` Intensity of the twists to apply to the lines to make veins more realistic
- `octaves` Number of octaves summed, more octaves mean blurrier texture (must be a power of 2)

=== "Default"

    ```
    material marbleMat (
        diffuse( marble() ),
        uniform(<black>)
    )
    ```

=== "Custom"

    ```
    material marbleMat (
        diffuse(
            marble(
                <white>,        #c1 
                <black>,        #c2 
                1.0,            #xPeriod 
                2.0,            #yPeriod
                3.0,            #turbPower 
                512             #octaves
            )
        ),
        uniform(<black>)
    )
    ```
[5]: https://mrl.cs.nyu.edu/~perlin/paper445.pdf

### Wood
With a different repetition of the lines, this time concentric, and with different colors,
[Perlin noise][5] based texture can be made into wood textures.
Once again default setting is provided, but you can customize your wood as you like.
Parameters are similar to those of [marble][6], but for the period parameter `xyPeriod` , that this time is unique, and is the
number of circles of wood's vein.

=== "Default"

    ```
    material woodMat (
        diffuse( wood() ),
        uniform(<black>)
    )
    ```

=== "Custom"
    ```
    material woodMat (
        diffuse(
            wood(
                <darkbrown>,    #c1 
                <black>,        #c2
                13.0,           #xyPeriod
                0.15,           #turbPower 
                512             #octaves
            )
        ),
        uniform(<black>)
    )
    ```

[6]: #marble

### Lava
Another available pigment is a lava-like one, with color from reddish brown to yellow. For this pigment
too there's the default one.
For lava pigment only two parameters are customizable:

- `scale` Sets the detail of the noise. Higher value reflects in more detailed noise and therefore
  into more fragmented lava style.
- `octaves` Number of octaves summed, more octaves mean blurrier texture (must be a power of 2)

=== "Default"

    ```
    material lavaMat (
        diffuse( lava() ),
        uniform(<black>)
    )
    ```

=== "Custom"
    
    ```
    material lavaMat (
        diffuse(
            lava(
                4.0,            #scale
                512             #octaves
            )
        ),
        uniform(<black>)
    )
    ```

<figure>
  <img src="https://github.com/AnnaPivetta/KTracer/blob/master/examples/ex4/image4.png?raw=true" width="480" align="center"/>
  <figcaption> Wood, Marble and Lava pigments </figcaption>
</figure>