[KTracer][1] has three different algorithm that can be used for rendering images. Each one has
its pros and cons, let's get a deeper insight of it.

[1]: https://github.com/AnnaPivetta/KTracer

### On-off
This is the most basic (and fast!) algorithm of all. It is mostly use for debug purpose, indeed
it only can show 2 colors, distinguishing from pixels where a shape has been hit and point where
this did not happen.

<figure>
    <img src="https://github.com/AnnaPivetta/KTracer/blob/gh-pages/docs/assets/images/onOff.png?raw=true">
    <figcaption> On-off renderer shows only where shapes are </figcaption>
</figure>

### Flat
The flat renderer is more realistic than the [on-off](#on-off) one still being very fast.
It does not let reflections occur, so specular surfaces cannot be rendered. 
Without reflecting rays, it also does not show any kind of shades, making the image appear
a little plain, but it's always bright!  
Moreover, thanks to its velocity, it's an extremely important tool for having a glance of how
the image will be, before launching a lots-of-hour code run for a perfect ray-tracing.

<figure>
    <img src="https://github.com/AnnaPivetta/KTracer/blob/gh-pages/docs/assets/images/flat.png?raw=true">
    <figcaption> No shadows and mirrors are present, but the computation is really fast for Demo! </figcaption>
</figure>

### Path Tracer
It's the most sophisticated renderer of all. It uses a recursive algorithm to compute the 
radiance of each pixel following the [rendering equation][2]. For this reason, mirrors and shades
can be appreciated, but the price to pay is in terms of compute-time.  
Of course your better images will be performed by this algorithm. So prepare your input file
with the other 2 renderers and generate your perfect image with Path Tracer!

<figure>
    <img src="https://github.com/AnnaPivetta/KTracer/blob/gh-pages/docs/assets/images/pt.png?raw=true">
    <figcaption> The computation requires a lot of time and so the image is dark, but one can appreciate all the game
of shadows and lights</figcaption>
</figure>


[2]: https://en.wikipedia.org/wiki/Rendering_equation