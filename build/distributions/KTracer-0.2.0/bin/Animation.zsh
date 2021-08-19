#!/bin/zsh

for angle in $(seq 0 359); do
    #Angle with three digits, e.g. angle="1" → angleNNN="001"
    angleNNN=$(printf "%03d" $angle)
    ./KTracer render --inputfile animation_input.txt --declare-float angle=$angle --ldr-o=Animation/img$angleNNN.png --hdr-o=Animation/pfm/img$angleNNN.pfm
done

# -r 25: Number of frames per second
ffmpeg -r 25 -f image2 -s 640x480 -i Animation/img%03d.png \
    -vcodec libx264 -pix_fmt yuv420p \
    animation.mp4