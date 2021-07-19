[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

# KTracer

This program is a rayTracer, written in [Kotlin](https://github.com/JetBrains/kotlin). It solves the rendering equation to generate a photorealistic image.  
This project is developed for the rayTracing course (a.y. 20-21) held by professor [Maurizio Tomasi](https://github.com/ziotom78) at the University of Milan, Physics Department.

The contributors to the project are [Matteo Martinelli](https://github.com/MatteoMartinelli97) and [Anna Pivetta](https://github.com/AnnaPivetta). 

## Table of Contents
* [Requirements](#requirements)
* [Distribution](#distribution)
* [Basic Usage](#basic-usage)

### Requirements
KTracers uses [Kotlin](https://github.com/JetBrains/kotlin) version 1.4.31. It is built with [Gradle](https://github.com/gradle/gradle).  
The only external library needed is [clikt](https://github.com/ajalt/clikt/), whose dependency can be added with:
   
    dependencies {
    implementation("com.github.ajalt.clikt:clikt:3.2.0")
    }

More information can be found at [clikt](https://github.com/ajalt/clikt/) repository.
### Distribution
In order to use and modify the code you can clone the repository with the command:
    
    git clone git@github.com:AnnaPivetta/KTracer.git

To check that everything work as expected run the command:

    ./gradlew test

The latest version is available at: 
https://github.com/AnnaPivetta/KTracer/tags  
Each version includes the source code and the built executable.

Distributions can be built from source code running the command:

    ./gradlew assembleDist

### Basic Usage
### Demo mode
<img align="right" width="300" src="./images/demo.png.png">
To do a quick check that everything works you can type the command:

    ./KTracer demo
and this will generate the image "demo.png" (if you like this image and want to modify it, you can use the render mode with input file [demo.txt](ricordati di mettere il link))

### Render mode     
Render mode is the most important one: you can use it to create your own images.


### Conversion mode :arrows_counterclockwise:
You can convert an existing .pfm file to a LDR file (format list available [here](https://github.com/AnnaPivetta/KTracer/blob/master/Format_List.txt)) running the command: 

    ./KTracer pfm2ldr --input <FILE IN> --output <FILE OUT> --format <FORMAT>
   
Running:
   
    ./KTracer pfm2ldr --help
   
you can find a list of all the available options



### Documentation
The documentation can be found on the [website](https://annapivetta.github.io/KTracer/).
