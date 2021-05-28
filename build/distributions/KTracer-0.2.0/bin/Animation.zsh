#!/bin/zsh

for angle in $(seq 0 359); do
    # Angle with three digits, e.g. angle="1" → angleNNN="001"
    angleNNN=$(printf "%03d" $angle)
    ./KTracer demo --width=640 --height=480 -n 13 --luminosity 0.1 --angle-deg $angle --ldr-o=Animation/img$angleNNN.png
done

# -r 25: Number of frames per second
ffmpeg -r 25 -f image2 -s 640x480 -i Animation/img%03d.png \
    -vcodec libx264 -pix_fmt yuv420p \
    demoworld-perspective.mp4