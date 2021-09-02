# KTracer

### v1.1.0 <small>_ August 28, 2021</small>

- New [16](https://github.com/AnnaPivetta/KTracer/pull/16): New Pigments are available, thanks to Pocedural Texturing. Being more specific new
  Marble, Wood and Lava style pigment can be added to every shape.
- New [19](https://github.com/AnnaPivetta/KTracer/pull/19): Shapes variable can be defined in the input file. In this way complicated objects
  may be defined with a name, created wherever is convenient,  and then called back with the name and eventually transformed
  to be placed as desired.
  Follow tutorial 4 for more information


### v1.0.1 <small>_ July 21, 2021</small>
- Fixed: bug in box (u,v) mapping

### v1.0.0 <small>_ July 20, 2021</small>
- New [#17](https://github.com/AnnaPivetta/KTracer/pull/17): Render mode is able to read input files
- API: Angles must be specified in degrees


### v0.2.1 <small>_ June 10, 2021</small>
- New [#12](https://github.com/AnnaPivetta/KTracer/pull/12): CSG objects can be transformed after their _assembling_
- New [#14](https://github.com/AnnaPivetta/KTracer/pull/14): Box Texture has now changed the required format for Image Pigment. New shape Cylinder can be added to scenes


### v0.2.0 <small>_ May 28, 2021</small>
- New [#8](https://github.com/AnnaPivetta/KTracer/pull/8): Spheres can be added to the scene
- New [#9](https://github.com/AnnaPivetta/KTracer/pull/9): The program can be run in different mode by command line and a demo can be created
- New [#10](https://github.com/AnnaPivetta/KTracer/pull/10): Planes, boxes and CSG objects can be added to the scene
- New [#11](https://github.com/AnnaPivetta/KTracer/pull/11): Uniform and checkered textures can be used and the objects surface can be defined ad diffusive or specular
- New [#13](https://github.com/AnnaPivetta/KTracer/pull/13): Antialiasing algorithm can be applied in order to improve the image quality

### v0.1.1 <small>_ May 5, 2021</small>
- Fix [#6](https://github.com/AnnaPivetta/CNGIF/issues/6): Computation of pixel position onto the screen

### v0.1.0 <small>_ April 14, 2021</small>
- Reading PFM files
- Tone mapping
- Gamma correction
- Saving in LDR format. The list of available formats is [here](https://github.com/AnnaPivetta/CNGIF/files/6309554/Format_List.txt).
