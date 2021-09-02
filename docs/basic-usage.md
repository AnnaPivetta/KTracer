#  Basic usage

## Demo mode

Once installed your [KTracer][1], the first and very simple image you may generate is 
a demo.
Running the command:
    
    ./KTracer demo

will render the image described in the file `demo.txt`, which by default is the following:

<figure>
  <img src="https://raw.githubusercontent.com/AnnaPivetta/KTracer/master/images/demo.png" width="480" align="center"/>
  <figcaption>Demo image shows some of KTracer capability</figcaption>
</figure>

[1]: https://github.com/AnnaPivetta/KTracer

## Converter mode

If you have run the _demo_, you may have notice that you have obtained 2 different files.
A _ldr_ image, the one showed above, and an _hdr_ one. This second one is useful, as it contains the raw
value of pixel color, without any _tone mapping_ or _gamma correction_ applied.
If the luminosity of the image does not satisfy you and you want to correct it, with the `pfm` file
it will be a child's play.  
Just run the command:

    ./KTracer pfm2ldr --input <FILE IN> --output <FILE OUT> --format <FORMAT>

and set the parameters:
- `--luminosity <LUMINOSITY>` The required average luminosity
- `--gamma <GAMMA>` The gamma factor of the monitor
- `--factor <FACTOR>` a rescaling factor for colors (if interested, you can find more details in [Shirley&Morley][2])

[2]: https://books.google.it/books/about/Realistic_Ray_Tracing_Second_Edition.html?id=ywOtPMpCcY8C&redir_esc=y

## Render mode
If you are here for realistic image generation, this is by far the most interesting and fun mode you can choose.
Once you'll learn how to produce the input file for setting the scene, this command will 
perform the magic:

    ./KTracer render --inputfile <FILE IN>

Even though the `inputfile` is the only mandatory flag, there are quite a lot of other interesting
features that may be activated while rendering an image.
Some of the most useful parameters are:

- `--width | -w <WIDTH>` The width of the image
- `--height | -h <HEIGTH>` The height of the image
- `--algorithm | -a <ALGORITHM>` The algorithm used to render the image. See [later](In-depth/renderers.md) for further information
- `--ldr-o | --ldroutput <FILE OUT>` The name of the LDR output file
- `--nr | -n <NUMBER OF RAYS>` Number of rays generated at each surface-ray interaction
- `--maxDepth | -Md <NUMBER OF REFLECTIONS>` Maximum number of reflections per ray
- `--rrTrigger | -rr <TRIGGER>` Depth value after which Russian Roulette algorithm activates.
- `--AAgrid | --AA | --aa | -A <NUMBER>` Number of divisions in each pixel's side to perform antialiasing (more simply, <NUMBER\>*<NUMBER\> is the total
  amount of samples per pixel)
  
## Animation
With the help of [ffmpeg][3] also beautiful animations can be rendered with [KTracer][1].
On our [repository][4] there is a script to create animations. You must have a variable in the input file
that is associated to the rotation of the camera, and using it you can create your own `GIF`.
<figure>
<img src="https://github.com/AnnaPivetta/KTracer/blob/master/images/demoworld-perspective.gif?raw=true" width="480" align="center"/>
</figure>

[3]: https://github.com/FFmpeg/FFmpeg
[4]: https://github.com/AnnaPivetta/KTracer/blob/master/build/distributions/KTracer-0.2.0/bin/Animation.zsh