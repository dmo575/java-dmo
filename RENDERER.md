# Rendering step by step

## Intro
This text aims to record the process of how we can project a 3D scene into a 2D canvas from scratch. We tackle the problems that we encounter one by one and keep on evolving our solution by iterating on it and modifying as we get closer and closer to our objective.

## Index

- Prerequisites
- Understanding the tools
    - Canvas
    - Vector
- Intruducing the 3D and 2D spaces
    - Application Canvas
    - 3D World
- Entering the 3D World (The concept of a camera)
    - Selecting a plane
    - Resolving axis interpretation
    - The idea of a measurement unit
    - Direction
    - Position
    - Projection range (FOV)
- Moving the camera
    - Cartesian quadrants
    - One formula to represent them all
- Aspect ratio
    - The problem
    - First possible solution: Inner boxing
    - Second possible solution: Outer boxing
    - Third possible solution: Dynamic camera
    - Solving the aspect ratio problem with the dynamic aspect ratio camera
- The units roadblock
    - Why units
    - Normalizing
- Dealing with the Z

## Prerequisites

You don't need a lot to read trough it. Just basic knowledge about Cartesian maps, OOP, how to get a canvas to paint on in your computer and some imagination to picture the scenarios I will describe. We will be using pseudo-code as the place where we will apply our knowledge in a practical way using OOP, meanwhile our imagination will be the place where we will see all those objects working together.

I will also be using Java, wip.

### Understanding our tools

The first thing we need to make sure we understand is our starting point.

We have two tools to begin with:
- Canvas (awt.Canvas): It provides us with a rect on the screen to paint to. This rect has width and height defined in pixels.
- Vectors (Main.Vec): The Vec class allows us to store information about 3D points (x,y,z)

The canvas is a place where you can paint anything you want (lines, dots, rects, wathever it is). It provides functions where you specify the X and Y coordinates (pixel units) and the dimensions of the thing that you want to start painting there.

The vectors are just three floats together grouped under the same object. Each float has its own name: X, Y, Z. A vector represents a point in a 3D space

### The 3D and the 2D worlds

We will start by creating some instances of the vector class. We need to imagine those vectors that we made as real 3D points living in a 3D world, that would be our 3D space.

Our objective is to paste those 3D points into our application's canvas (this is called projecting), and to do so we need to first understand the two worlds we are trying to connect: The 3D world where the points live, and the 2D canvas where we can paint pixels. We need to somehow insert our canvas into that imaginary 3D space where our points live.

If you think about it, the application canvas that we have has its own plane (the XY plane), in fact, you can think of the entire 2D canvas as a plane. This means that we can insert the 2D canvas into the 3D world by selecting a plane in the 3D world where we would like to channel it.

Since our 2D canvas has the X and Y axis, it makes sense to select the XY plane in the 3D world as the plane to instantiate our canvas in. 

So, the way things are at the moment we have a 2D canvas we can paint on, an imaginary 3D world and an imaginary 2D canvas that we are trying to channel into that 3D world, but so far we have nothing spawned there, we just have a selected 3D plane (XY) in a 3D world that we are interested in, thats not a 2D plane, let alone a canvas. If we want to have a 2D canvas in this 3D world, we need to position this thing somewhere, give it a direction and finally some boundaries.

~~So, the way things are at the moment we have a 2D canvas we can paint on, an imaginary 3D world and an imaginary 2D canvas that we are trying to spawn into that 3D world, but just selecting a plane we want to spawn it in doesn't really places it in the map yet. We still need to position it somewhere, give it a direction and finally some boundaries.~~

**Positioning:**
Lets start with positioning. Picture yourself trying to select a **2D plane** in the 3D world: You have the axis of the plane (XY), you know a plane is infinite so you don't really need to select a position in the Xs or in the Ys for this XY plane, what else is missing? The Z value. Remember your plane is 2D, is flat, if you want to place it in a 3D world you will have to give it a Z value. Lets take 0 for the Z value, now you can see what seems to be a thin infinite paper at Z=0, expanding infinitely in the X and Y directions.

Now, our imaginary 2D canvas that we want to spawn in this 3D world needs to be a representation of our actual application canvas, and right now that is not true because our application canvas is a finite plane if you will, it has a width and a height, but our representation here is an infinite plane in the XY at Z=0. To fix that we need to talk about a couple of things that both the 2D and 3D world have in common: Origin points and units of measurement.

If we decide that we will align the origin point of the imaginary canvas we are trying to spawn to the origin point of the 3D world, we are basically saying that (0,0,0) in the 3D world equals (0,0) in the 2D world of that imaginary canvas. Thats is actually a great first step on syncing these two entities, but we still cannot really know how wide or tall our 2D canvas is in this 3D world because, if you think about it, we have no idea how much 1 unit means in this 3D space, we haven't defined it yet!

Luckily for us, we can solve this problem really fast. We know that we want this imaginary 2D canvas (I will give it a name soon, dw) to be a representation of our actual application canvas in this 3D world, so it makes sense that we would adopt the measurement units from the application canvas (pixels) onto the imaginary 2D one, and while we are at it, we could just do the exact same for the 3D world, lets adopt pixels as our measurement units there too. That's it, Solved.

You notice what that means right? We have a 3D world whos units are in pixels, we have an imaginary 2D plane spawn on that world, using the X and Y axis as its plane axis, located at Z=0, with its origin point at (0,0,0), and with a width and height that we can define in pixels, just like our application canvas' dimensions are defined in.

Lets then give it some width and height, lets give it the same width and height as our application canvas. At this point our "2D imaginary plane in the 3D space" is not really a plane anymore, it has two things that planes do not have: origin point and dimensions. We could very well call this a 2D canvas in a 3D space, and indeed we should. However, beware this is not its final name, haha. We sill have a lot of work to do on this 2D canvas in a 3D space. We are done with positioning for now tho, we succesfully spawned our 2D plane and magnificently pushed away the problem of measurement units for the time being.

**Direction:**
With direction I mean from which side of the 2D canvas we will be looking trough. You might not have noticed but there is something very important that we haven't discussed yet about axis.

Take a look at this image, it depicts our 2D canvas inside the 3D world. Can you tell me which one correctly depicts it?

Right now, option A seems like it. And in reality, if this were a simple object you would be right, but, this is not a simple object, this is not a 3D point in the 3D world this is a window to another dimension we are talking about. Therefore the way this representation shows itself in the 3D world depends entirely on how this 2D canvas entity decides to interpret the X and Y axis.

The 2D canvas that we are spawning here will be interpreting the X axis the same way the 3D world is, so by knowing that you should be able to cross 2 of the possible ways the 2D canvas will channel itself as (answer is, options B and C are no more, only A or D).

The Y axis however, will be interpreted differently in the 2D canvas world: It will go down where the 3D world goes up, and up where the 3D world goes down. Meaning negative values will be interpreted as its positive counterparts and vice-versa. This means option D is exactly how the 2D canvas is channeled into this 3D world.

The reason why we are chosing to invert the Y axis with this 2D canvas is because our application canvas that we can draw on uses this same axis set up, and remember the 2D canvas we have been building all this time is meant to be a representation of this actual application canvas, so we must go with this set up.

With that settled, we now need to figure out which face of the 2D canvas we are supposed to use. If you think about it, its been settled already by deciding on how to treat the axis, but I haven't told you which one it is yet because I want to first explain what direction means, so lets do that.

Imagine that in front of you are two spheres. Both are in front of you but one is to your right side a bit, and the other one towards your left side. The one on the right is blue while the one on the leftmost side is red. Now, imagine you walk past them, and then you turn back 180 degrees. At that moment, the sphere at your right is no longer the blue one, it's the red one now. Same with the other sphere. That is what I mean with direction.

Another example: Imagine you have a paper, it has two sides. You can look at one side or the other, not both. Our imaginary 2D canvas also has two sides because it lives in a 3D world. Our actual application canvas however, only has one side because its just a 2D world in there. The side our application canvas uses is the one where for us, the viewer, the X goes positive to the right. This means that the side of that 2D canvas in our 3D world that we will be using will be the one where X increments to the right, also known as the side where the Z increments go forward and Z decrements go backwards. So the Z+ side.

Why was that important? Because it will help us identify which of the two planes is our near one, which is the thing we will introduce next.

**Stablishing the boundaries: FOV**

Here is where our 2D canvas in a 3D world evolves into something else. Let me however start by explaining a little problem we have right now.

Lets say you have several 3D points scattered across your beautifull 3D world, and you decide to project those into your 2D canvas. Well, your canvas has an origin, a width and a height in pixels, so you can start to look for those points whos X and Y values lie within the canvas range and you project them, paste them, on your 2D canvas. Do you see the problem? In a 3D world with infinite depth, you would be pasting into your canvas absolutely every point whos X and Y value felt between your 2D canvas' range, whether that point is on one side of your 2D canvas of the other, you would paste them all. The final result would look like an X-ray render with no real beginning or end.

for each point
    is the point within the XY range of my 2D canvas in a 3D world?
        Y: give me the X and Y
            paint a dot in my app canvas at (X,Y)
        N: next

That's not how it should be, we shouldn't be painting absolutely everything, we need a range, a field of view as they call it.

Now, the way things are we do have boundaries in the X and Y axis, but not on the Z axis. The X and Y axis have their boundaries defined by the origin point and the width and height respectively. We can do something similar.

Lets add two values to our 2D canvas... oh, whats that? 2D-canvaskemon-in-a-3D-world is evolving ! Its growing into a more complex object with more properties than that of a simple 2D canvas !... is... a 3D-Camerakemon !. That's right, you saw it comming, I saw it comming, grandma saw it comming. It's a 3D camera... or, the foundations of it anyways. So our new camera is lacking some Z boundaries. We will add into this entity two new concepts, these are the near plane and the far plane.

These planes are just going to be two numbers that define a Z value for now. Lets imagine that one of our planes' value is 5 and the other one is -10. Can you guess which one is the near plane and which one is the far plane? If you think about the direction of the camera (+Z) then it becomes easy to know: since the camera's direction is +Z, the lowest value is the near plane while the highest value is the far plane.

All that is left to do is to make sure we only render those points that fall within the boundaries, which you should now be able to picture as a 3D box. Sure, this might not completely solve the X-ray problem, but it will surely mitigate it. At least the points being rendered are those that are within the field of view. We will leave this topic as-is for now.



## Camera size & scale






By now you should know that wathever falls within the field of view of your camera can be projected onto your application canvas. You should be able to see how that happens.

Try to imagine some points projected ont your canvas, now increase your canvas by twice its size and render the same scene. Do you see what happened? You increasing the size of the canvas means your camera's width and height automatically increased. The place of the points you projected stayed the same however. That is a problem, can you imagine leaving the cinema after watching a movie, trying to watch it again on your phone and your phone only rendering a tiny portion of what you saw on the big screen? You see, the way things are right now, the size of the camera is bound by the size of the canvas, and not only that, but there is no way for us to zoom in or out either. We need to fix that, and the best way to do so is to come up with out own units for our 3D world.




## Aspect ratio


Since we no longer will use pixels for our 3D world, we need to come up with a name for our new units. We will call them "world units". This means our camera's width and height will now be measured in world units. So now we have a camera with its own dimensions in world units and an application canvas that needs to fit wathever the camera has captured into its own space somehow. Basicaly we have two unique rectangles and we have to fit one inside the other.

There are three ways we see this problem solved:

- **Inner boxing:** One solution is to make sure that wathever final image is produced by the camera is scaled up or down as much as possible within the canvas space to fit the best it can, but without cropping.

- **Outter boxing:** Another solution would be the opposite, scale the cameras image as necessary to ensure we cover the whole application canvas space, even if it means cutting down some of the camera's image.

- **Dynamic resizing:** The last solution is what we will try to implement. This involves letting the application canvas control the camera's aspect ratio, which would make it possible for the cameras image to scale itself perfectly without cutting, or shrinking or anything like that.






- SCALING
    - Normalizing
- ASPECT RATIO
    - Inner boxing
    - Outer boxing
    - Dynamic sizing
- MOVEMENT
    - Dealing with quadrants
- ROTATION
- ISOMETRIC CAMERAS
- PERSPECTIVE CAMERA
    - Using the Z axis








-------------------------------------------------------------
-------------------------------------------------------------
-------------------------------------------------------------
-------------------------------------------------------------











































