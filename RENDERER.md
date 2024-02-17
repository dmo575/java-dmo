# Rendering step by step

## Intro
This text aims to record the process of how we can project a 3D scene into a 2D canvas from scratch. We tackle the problems that we encounter one by one and keep on evolving our solution by iterating on it and modifying as we get closer and closer to our objective. By doing this in an iterative and incremental manner we get to see all the problems that we face and what every transformation does to solve it.

## Index

(BOLD marks the section as completed)

- **Prerequisites**
- Cartesian maps
- **Understanding the tools**
    - **Canvas**
    - **Vector**
- **The situation room**
    - **Pseudo**
    - **Formulas**
- **Defining the 2D and 3D worlds**
    - **The application canvas**
    - **The 3D world**
- **Channeling a world into another** p89
    - **Positioning**
    - **Direction**
    - **Stablishing the boundaries (FOV)**
- **Situation room: Update**
- [Freeing the camera](#cam)

-   sds

- Further separating the camera from the application canvas
- ... WIP


## Prerequisites

You don't need a lot to read trough it. Just basic knowledge about OOP, how to get a canvas in your computer that you can paint on and some imagination to picture the scenarios I will describe. We will be using pseudo-code as the place to structure things out using OOP and our imagination will serve as the place where we will build the scenarios and add concepts one by one.

## Cartesian maps

TODO: Explain Cartesian maps: Axes, Planes, Origin points, dimensions. How one dimension can be represented in another one (from high to low and low to high).
Axis == 1D == direction == movement
Plane == 2D == expansion == 
Space == 3D == volume

## Understanding the tools

The first thing we need to make sure we understand is our starting point.

We have two tools to begin with:
- Canvas (awt.Canvas): It provides us with a rect on the screen to paint to. This rect has width and height defined in pixels.
- Vectors (Main.Vec): The Vec class allows us to store information about 3D points (x,y,z)

The canvas is a place where you can paint anything you want (lines, dots, rects, wathever it is). It provides functions where you specify the X and Y coordinates (pixel units) and the dimensions of the thing that you want to start painting there.

The vectors are just three floats together grouped under the same object. Each float has its own name: X, Y, Z. A vector represents a point in a 3D space

## The situation room

Given our tools, we can already write some pseudo-code to illustrate what we would be dealing with. We will update this code as we move forward on our understanding of what's going on and what needs to happen. We will also update the formulas we extract out of our journey.

We will display the latest on the many iterations of these two sections I will present now, under the title: "Situation room: Update":

**PSEUDO**
```
// a method on our canvas class where we can just paint to our application canvas. If you are using java.awt then this fits right in with you, if not then you should be able to translate this scenario to your environment withouth much trouble since its just defining a place where we can tell the canvas what and how to paint.
Canvas.paint()
    // we create our 3D world here by creating some 3D points
    vectors[] points = { ... }

    // we then paint here, so far we have nothing
```
**FORMULAS**
```
None so far
```

## Defining the 2D and 3D worlds

### The application canvas

The canvas that we are provided with is something that we will refer to as the **application canvas**. I really encourage you to look at it as something that lives in a 2D space as shown here.

**IMAGE 00**

As you can see, we have a Cartesian map with two axes, X and Y. These two form a 2D plane, and inside it we have a rect. The rect's top left corner is located at the origin point of this 2D space (0,0), the rect's bottom right corner is located at (canvas width, canvas height).

The measurement units of this 2D space is pixels.

### The 3D world

This is the 3D world that we will be using:

**IMAGE 01**

All of our 3D points that we create by using the Vector class will live there. As you can see, we got three axes there, we also got three planes.

Right now our 3D world is empty, so use the Vector class and create some points. That group of points is our 3D world in our code. Make sure you can access those when its time to paint.

## Channeling a world into another

Our objective is to paste our 3D world's points into our application's canvas (this is called projecting), and to do so we need to first understand the two worlds we are trying to connect: The 3D world where the points live, and the 2D canvas where we can paint pixels. We need to somehow insert our canvas into that imaginary 3D space where our points live.

If you think about it, the application canvas that we are given lives in its own plane (the XY plane), in fact, that plane encapsulates the whole space of that 2D world. This means that we can insert the 2D canvas (its imaginary counter part, that is) into the 3D world by selecting a plane in the 3D world where we would like to channel it. Now, a plane in a 3D space is not exactly the same as a plane in a 2D space because 3D planes have volume and 2D ones do not. But that is why I used the word "chanel", because its not like we can pop up the 2D canvas in the 3D world just like that, its a process and the first step lies in **choosing** one of the 3 planes that our 3D world has to offer (XY, XZ, YZ).

Since our 2D canvas has a couple of axes named X and Y, it makes sense to select the XY plane in the 3D world as the plane to instantiate our canvas in. It makes things easier to visualize, and its also the convention.

So, the way things are at the moment we have a 2D canvas we can paint on (the application canvas), an imaginary 3D world that we can visualize in our minds and whos represetation in code are the Vectors we made and an imaginary copy of the application canvas that we are trying to channel into that 3D world, but so far we have nothing spawned there, we just have a selected 3D plane (XY) in a 3D world that we are interested in, thats not a 2D plane, let alone a canvas. If we want to have a 2D canvas in this 3D world, we need to position this thing somewhere, give it a direction and finally some boundaries.


### **Positioning:**
Lets start with positioning. Picture yourself trying to select a **2D plane** in the 3D world: You have the axes of the plane (XY), you know a plane is infinite so you don't really need to select a position in the Xs or in the Ys for this XY plane, what else is missing? The Z value. Remember your plane is 2D, is flat, if you want to place it in a 3D world you will have to give it a Z value. Lets take 0 for the Z value, now you can see what seems to be a thin infinite paper at Z=0, expanding infinitely in the X and Y directions.

**IMG 02**

Now, our imaginary 2D canvas that we want to spawn in this 3D world needs to be a representation of our actual application canvas, and right now that is not true because our application canvas is a finite plane if you will, it has a width and a height, but our representation here is an infinite plane in the XY at Z=0. To fix that we need to talk about a couple of things that both the 2D and 3D world have in common: Origin points and units of measurement.

If we decide that we will align the origin point of the imaginary canvas that we are trying to spawn to the origin point of the 3D world, we are basically saying that (0,0,0) in the 3D world equals (0,0) in the 2D world of that imaginary canvas. Thats is actually a great first step on syncing these two entities, but we still cannot really know how wide or tall our 2D canvas is in this 3D world because, if you think about it, we have no idea how much 1 unit means in this 3D space, we haven't defined it yet!

Luckily for us, we can solve this problem really fast. We know that we want this imaginary 2D canvas to be a representation of our actual application canvas in this 3D world, so it makes sense that we would adopt the measurement units from the application canvas (pixels) into the imaginary 2D one, and while we are at it, we could just do the exact same for the 3D world, lets adopt pixels as our measurement units there too.

You notice what that means right? We have a 3D world whos units are in pixels, we have an imaginary 2D plane spawn on that world, using the X and Y axis as its plane axes, located at Z=0, with its origin point at (0,0,0), and with a width and height that we can define in pixels, just like our application canvas' dimensions are defined in.

Lets then give it some width and height, lets give it the same width and height as our application canvas. At this point our "2D imaginary plane in a 3D world" is not really a plane anymore, it has two things that planes do not have: origin point and dimensions. We could very well call this a 2D canvas in a 3D space, and indeed we should. However, beware this is not its final name. We sill have a lot of work to do on this 2D canvas in a 3D space. We are done with positioning for now tho, we succesfully spawned our 2D plane and magnificently pushed away the problem of measurement units for the time being.

**IMG 03**

### **Direction:**
You might not have noticed but there is something very important that we haven't discussed yet about axes.

Take a look at this image, it depicts our 2D canvas inside the 3D world. Can you tell me which one correctly depicts it?

**IMG MD83**

Right now, option A seems like it. And in reality, if this were a simple object you would be right, but, this is not a simple object, this is not a 3D point in the 3D world this is a window to another dimension we are talking about. Therefore the way this representation shows itself in the 3D world depends entirely on how this 2D canvas entity decides to interpret the X and Y axis.

The 2D canvas that we are spawning here will be interpreting the X axis the same way the 3D world is, so by knowing that you should be able to cross 2 of the possible ways the 2D canvas could possibly show itself as:

**IMG MD84**
(answer is, options B and C are no more, only A or D).


The Y axis however, will be interpreted differently in the 2D canvas world: It will go down where the 3D world goes up, and up where the 3D world goes down. Meaning negative values will be interpreted as its positive counterparts and vice-versa. This means option D is exactly how the 2D canvas is channeled into this 3D world.

The reason why we are chosing to invert the Y axis with this 2D canvas is because our application canvas that we can draw on uses this same axis set up, and remember the 2D canvas we have been building all this time is meant to be a representation of this actual application canvas, so we must go with this set up.

With that settled, we now need to figure out which face of the 2D canvas we are supposed to use, and by that I mean from which side of the 2D canvas we will be looking trough.

If you think about it, its been settled already by deciding on how to treat the axes, but I haven't told you which one it is yet because I want to first explain what direction means, so lets do that.

Imagine that in front of you there are two spheres. While both are in front of you, one is to your right side a bit, and the other one towards your left side. The one on the right is blue while the one on the leftmost side is red. Now, imagine you walk past them, and then you turn back 180 degrees. At that moment, the sphere at your right is no longer the blue one, it's the red one now. Same with the other sphere. That is what I mean with direction.

Another example: Imagine you have a paper, it has two sides. You can look at one side or the other, not both. Our imaginary 2D canvas also has two sides because it lives in a 3D world. Our actual application canvas however, only has one side because its just a 2D world in there. The side our application canvas uses is the one where for us, the viewer, the X goes positive to the right. This means that the side of that 2D canvas in our 3D world that we will be using will be the one where X increments to the right, also known as the side where the Z increments go forward and Z decrements go backwards. So the Z+ side.

Why was that important? Because it will help us identify which of the two planes is our near one, which is the thing we will introduce next.

#### Situation room: Update
**PSEUDO**
```
Canvas.paint()
    vectors[] points = { ... }

    // this is how we are projecting our 3D points right now
    for each point P in points:
        draw a point on the canvas at (P.X, -P.Y)
```
**FORMULAS**
```
None so far
```

### **Stablishing the boundaries: FOV**

Here is where our 2D canvas in a 3D world evolves into something else. Let me however start by explaining a little problem we have right now.

Lets say you have several 3D points scattered across your beautiful 3D world, and you decide to project those into your 2D canvas. Well, your canvas has an origin, a width and a height in pixels, so you can start to look for those points whos X and Y values lie within the canvas range and you project them (paste them) on your 2D canvas. Do you see the problem? In a 3D world with infinite depth, you would be pasting into your canvas absolutely every point whos X and Y value felt between your 2D canvas' range, whether that point is on one side of your 2D canvas or the other, you would paste them all. The final result would look like an X-ray render with no real beginning or end for its depth.

```
    for each point P in points:
        Is point within the canvas XY range?
            Yes: draw a point on the canvas at (P.X, -P.Y)
            No: continue
```

That's not how it should be, we shouldn't be painting absolutely everything, we need a range for our depth, a field of view as they call it.

Now, the way things are we do have boundaries in the X and Y axis, but not on the Z axis. The X and Y axis have their boundaries defined by the origin point and the width and height respectively. We can do something similar.

Lets add two values to our 2D canvas... oh, whats that? 2D-canvaskemon-in-a-3D-world is evolving ! Its growing into a more complex object with more properties than that of a simple 2D canvas !... is... a 3D-Camerakemon ! ! !. That's right, and my apologies for that joke by the way, it must have hurt to read. Anyways, by adding the two values we will add we will have oficially evolved that imaginary 2D canvas into a camera, so lets call it that from now on.

The two values will be named near plane and far plane. These two new planes are just going to be two numbers that define a Z value for now. They will help us in defining some boundaries for our camera in the Z axis.

Lets imagine that one of our planes' value is 5 and the other one is -10. Can you guess which one is the near plane and which one is the far plane? If you think about the direction of the camera (+Z, as we established earlier) then it becomes easy to know: since the camera's direction is +Z, the lowest value is the near plane while the highest value is the far plane.

All that is left to do is to make sure we only project those points that fall within the boundaries, which you should now be able to visualize in your 3D world as a 3D box. Sure, this might not completely solve the X-ray problem, but it will mitigate it. At least the points being rendered are those that are within the field of view. We will leave this topic as-is for now.

**IMG 37 from OLD**

#### Situation room: Update

**PSEUDO**
```
Canvas.paint()
    // 3D world
    vectors[] points = { ... }

    // projection into canvas
    for each point P in points:
        Is point within the canvas XY range AND also within the camera's Z range ?
            Yes: draw a point on the canvas at (P.X, -P.Y)
            No: continue

// general idea of whats inside the camera class atm
Camera class:
    float near_plane, far_plane;
```
**FORMULAS**
```
None so far
```
<a id="cam"></a>

## Freeing the camera


Our goal of spawning an embassador for the application canvas in the 3D world is complete. Now we just need to make it better and better. For that we will have to start breaking a lot of the connections that we have been making between our now named camera and our application canvas.

### Zooming in/out: The problem.

When we think about what we would want to do with a camera, one of the first things that might come to mind is zomming in and out. But the way things are at the moment makes this impossible for us. The reason is because in order for us to zoom in and out we need to have control of the camera's XY size. Now, we can try giving the camera its own width and height, but doing that right now wouldnt yield any proper results.

Lets say that we add a couple of floats to the camera, one for width and another one for height. Now lets update our paint function to accomodate for that:

```
Canvas.paint()
    vectors[] points = { ... }

    for each point P in points:
        Is point within the CAMERA'S XY range AND also within the camera's Z range ?
            Yes: draw a point on the canvas at (P.X, -P.Y)
            No: continue
```

We changed the pseudo-code so it now checks the camera's XY values (aka its width and height). Do you see what would that do? It effectively gives the camera its own width and height, so you should be able to picture a camera with its own XY dimensions now, independent of the application canvas' ones.

With this new set up, we have our application canvas with its own dimensions in pixels and our camera in the 3D world with its own XY dimensions in pixels too, since that is the units of our 3D world so far. This presents to us three posible scenarios. Lets take a look at them:

- Projecting an image when the camera's XY size is the same as the application canvas':
    - This is basically what we have been doing so far. No changes.
- Projecting an image when the camera's XY size is smaller than the application canvas':
    - Here you need to imagine the camera's XY as a window you can partially close. What happens when you partially close a window? You see less of the outside, that's all that happens.
    So if you recude the camera's XY dimensions, you are NOT shrinking what you see, you are just resizing the window you use to look outside. This means that when you go to the canvas you will have extra space that you wont use. You can think about it this way: the camera is a 4x4 paper, and you want to exactly copy (meaning same size as well) wathever drawing is in that paper into a 6x6 paper (this would be the canvas). You will start copying from the top left corner and by the time you finish, you will see that you have an unpainted horizontal stripe at the bottom of the paper and another unpainted vertical stripe at the right end of the paper.
    **IMG 04**
- Projecting an image when the camera's XY size is bigger than the application canvas':
    - No visual changes will happen here because as already mentioned, resizing the camera this way just creates a bigger window to look from, but when it is time to paste that image, is like copying an image from a 6x6 paper onto a 4x4 paper without changing the scale, starting from the top left corner. Wathever extra was on the 6x6 will never be drawn because there is no space. This means that the end result of this is the same result you would get if you shrink the camera's XY dimensions to those of the application canvas' ones and paint again.
    **IMG 05**

However not all is bad about this. At least we now have a camera with its own dimensions in that 3D world. Kind of useless when it comes to projecting to the canvas but still, we can use this. This is a first good step, and the reason for that is this:

Zooming in and out means that our camera captures more of the 3D world (if we want to zoom out) or less of the 3D world (if we want to zoom in) and then we get that view and project it into our canvas, using presumably all of its space. This is what creates that zoom in/out illusion; the resizing of the camera's XY plane captured in the same immutable application canvas.

Now that we understand the problem, understand what we want.

### Zooming in/out: What would the solution look like

In order for us to be able to zoom in and out, what we need to do is to have the camera be able to increase or decrease its XY size, and then have wathever the camera captures be projected onto the same application canvas. This creates the effect of zooming in and out. Check out the image:

**IMG 06**

Imagine that instead of some points we can project a circle for a moment. You can see how increasing the camera's XY size will give the effect of zooming out right? Sure it's not a very centered zoom out, because we haven't tackeld that yet, but centered or not, it is definitely zoomed out in the canvas, and that is what we are looking for.

### What it means to break the connection between the camera and the canvas

Before continuing, is important to mention a couple of thins about what happens when we give the camera its own width and height.

When do that, we are essentially breaking the connection between the camera and the application canvas in a couple of places. This means that wathever solution we are looking for must address this issue, it must reconnect these places. This is what we are breaking:

- **Sizes (width and height):** The most obvious thing we are breaking is the actual width and height connnection that the camera and the application canvas shared. We built this connection a while a go when we decided to assign the pixel as the 3D world's measurement unit and give the then "2D imaginary canvas in a  3D world" the X and Y values that the application canvas had. We will have to reconnect the size somehow.

- **Aspect ratio:** We indirectly broke this too, because aspect ratio depends on the width and height, so by having its own values for those, we are also obtaining our own independent aspect ratio.

**About the aspect ratio:**

Aspect ratio is a decimal number that records the size relation between the width and the height. The formula for the aspect ratio is w/h.

There are three possible values that we can get from that formula: A number between 0 and 1 (not included), an exact 1 and finally a number superior than 1.

- **Between zero and one:** Getting this means that our screen's shape is akin to a portrait. The lower the number the more vertical it grows.

- **An exact 1:** Getting this means that our screen's shape is exactly a square.

- **Between one and infinite+:** Getting this means that our screen's shape is akin to a landscape. The lower the number the more horizontal it grows.

If you invert the formula from w/h to h/w you also get an aspect ratio but instead of getting the aspect ratio relative to the width, you get it relative to the height. Same thing, but when people talk about aspect ratio (like buying a monitor) they usually go with w/h.

The aspect ratio of a screen is usually not written down as the decimal number, people think it more elegant to write it down as a fraction (6:20, 8:16, aka Six by twenty, Eight by Sixteen), but since we are computing stuff with that number we need to use its decimal representation. 


### Cheking out more approaches to better understand the idea

We know the problem and we know the solution we are looking for. But before implementing it, lets take a look at what that solution's pros and cons are as well as two other possible solutions that we could implement.

We need to somehow, during projection, resize the resulting image to fit on the canvas. Whether the canvas is smaller or bigger shouldn't matter, it should work both ways. We also know that by giving the camera its own dimensions, we broke both the width and height height connection and the aspect ratio connection we had with the application canvas. Keeping those two in mind, lets see what we can do:

- **Inner boxing:**: This solution would resize the projection down to make sure that the canvas can display the full image while conserving the camera's aspect ratio.
    - Pros: You get to keep control of the camera's aspect ratio.
    - Cons: While you would be able to always show what the camera captures, you will not always make use of the whole canvas.
    - How its made: We will never fix the connection between the camera's and application canvas' aspect ratios. We will only reconnect either the width or height by constraining it to the application canvas' value while letting the other of the two sizes adjust proportionally.
**IMG 50OLD**
- **Outer boxing:** We would prioritize using the whole application canvas while mantaining the proportions of the camera in the projection.
    - Pros: You get to use the whole canvas.
    - Cons: You will have to do some cropping; you wont always capture all the camera sees in your canvas.
    - How its made: Similar to Inner boxing, we leave the aspect ratio connection broken and only constrain either the width or height and leave the un-constrained to adjust itself. The difference here is that we would constrain the smallest of the two while with Inner boxing we would constrain the bigger of the two values.
**IMG 50BOLD**

- **Dynamic aspect ratio:** We would let the canvas control the camera's aspect ratio, but we would keep the camera in control of its overall size by adding a scale factor.
    - Pros: You get to use the whole canvas and always paint everything the camera has to show. You still can resize the camera via a scale factor variable.
    - Cons: You wont have control over the aspect ratio from within the camera.
    - How its done: We reconnect both the aspect ratio and the dimensions.
**IMG 50COLD**


I have seen solutions 1 and 3. Solution 1 you can see it in old applications playing in newer hardware. Solution 3 is what most applications have today by default. I don't think anyone in their right mind would use solution 2 but its a good thing to imagine it. We will implement solution 3.





- How scaling works
    - Scaling vector
        - The idea of a scaling vector, what it is composed of and what does every part means for our scaling
    - Origin canvas
    - Target canvas
    - Scaling factor
        - This determines the magnitude of the scaling vector
    - Target point
        - This will be the origin point of the scaling vector
    - Scaling pivot
        - This will give us the direction of the scaling vector









## BELOW IS OLD VERSION



### Implementing solution 3 (Dynamic aspect ratio):

Lets go with solution 3. We will give the camera a "dynamic aspect ratio", meaning we will constrain it to the application canvas' aspect ratio while allowing it to have a way of scaling up and down via a scale factor.

In order to constrain the aspect ratio while mantaining the option of resizing, we need to take these into consideration:

- Initial scale: we will give it 1.0f as the initial scale.
- Initial dimensions: we will assign it the canvas' dimensions as the default one. This means that a scale of 1.0f will bring the camera's size to be its initial one, which is the canvas size.

```
Camera class:
    float near_plane, far_plane, width, height;
    float scale = 1;

    Camera()
        width = canvas width;
        height = canvas height;

```

With that, all the variables are in place. Now all that's left is to use them. First we need to make sure we "capture all the space we need to capture for the projection", meaning make sure that we let any point that lies within the range of the camera to pass the "is within range" check. For that we need to know the camera's dimensions which are these:

camera.x = canvas.x * Camera.scale;
camera.y = canvas.y * Camera.scale;

We will use those from now on when checking this part of the pseudo-code: `Is point within the CAMERA'S XY range`.

Now we need to make sure we draw the points at the correct place
Lets change our `draw a point on the canvas at (...)` to use these:
(P.X / Camera.scale, P.Y / Camera.scale)

We will then change how we project the 3D world into the canvas:

```
Canvas.paint()
    vectors[] points = { ... }

    for each point P in points:
        Is point within the CAMERA'S XY range AND also within the camera's Z range ?
            Yes: draw a point on the canvas at (P.X / Camera.scale, -(P.Y / Camera.scale))
            No: continue
```
Formulas:
```
Camera's origin: (0,0)
Camera's XY dimensions: (Canvas.x * Camera.scale, Canvas.y * Camera.scale)
```

Look at that, we can now zoom in and out... more or less that is... It's obvious that our zoom in/out is taking the top left corner as some sort of pivot for the scaling. Lets break down what is going on.

 The reason is simple: We need a point of reference that we can use as an origin to scale things up and down from it. That point must be located at the same position in both the camera and the canvas, and we are using the origin point for that right now. Let me explain:

#### What goes into scale-less projecting:
If we want to project from one canvas to another (in this case from the camera's XY to the application canvas) even when those canvases do not have the same scale (remember they got the same aspect ratio, is just the scale that is off), then we need two things: the scale factor, a point of reference and the point

Using the scenario we have right now where the point of reference for the scaling is the 0,0

Lets try again but this time lets use another point for the scaling, lets use the points that are in the center of both the camera and the canvas.













- DO NORMALIZING
- THEN PRESENT SCALE PROBLEM
    - Because the units we have in 3D are pixels, we cannot zoom in or out. Think about it, our application canvas' units are pixels too. We cannot zoom in a pixel can we? Its a pixel! Think about it this way: You have your eyes that look into the world, and then the brain that processes what your eyes see, right? Good, now, try to zoom in with your eyes (you cannot move). Can you do that? Of course not!
- THEN DO UNITS
    - Now that the camera's dimensions are no longer attached to the canvas, we dont need the 3D world units to be pixels. 

- THEN MOVEMENT
    - REMEMBER QUADRANTS PROBLEM
- THEN TRY TO FINISH ISOMETRIC CAMERA?? FOR THAT WE NEED ROTATION IN PLACE... MAYBE Z FIRST


You probably have noticed that so far we haven't really done a lot of movement with the camera (which is, remember, an idea). The reason is that our camera is still way too conected to our actual application canvas.



- The camera doesn't have a position of its own, its dependant on the application canvas' position, which cannot move (Remember I told you to picture the app canvas as a rect in its own 2D world. Imagine that and realize we don't have control over the rect's position in that space). This however is not true about the camera's Z position, we do have control of the Z position via the near and far plane values.
- The camera doesn't have its own XY size. It's completely dependant on the application canvas' size. Which means how much we render depends on the canvas' size.

Can you see why is the camera so linked to the application canvas? The reason is because we have been using the application canvas' values because we haven't defined those for the camera yet. These values have served us well in understanding how we get the application canvas inside the 3D space, but now that we know that, we must evolve this into its own thing.






















## Further separating the Camera from the Application canvas

We have a size problem right now. Picture this: You decide to project some points on your app canvas whos dimensions are (300, 200) pixels. The point you get comes with the 3D world coords of (200, -150, 50). Now, since you know you have to invert the Ys and we are not using the Zs yet, you call in the paint operation at (200,150). That gives you a point that from your canvas's center point of view, it looks like its located on the fourth quadrant. Now, render the same point with a canvas of (1000, 1000). As you can see, the point is now located at the second quadrant.

You can imagine this in our 3D world as if we are manually expanding and contracting our camera's size: We know the origin point of our camera is the 0,0,0 in the 3D world, and we know the width and height is defined in pixels in that world, we also know that we have decided that our camera's width and height will be that of the canvas. So incrementing the width just moves the X limit further away, same with the Y. This means that with how things are right now, the canvas has total control over the camera's size.

We need our camera to have its own independent size that we can manuipulate. We also know that this camera lives in our 3D world, and our 3D world measurement units are pixels. We could try to solve this problem in one of two ways:

- Assigning my own values to the camera's width and height.

- PROBLEM: CANVAS CONTROLS CAMERA'S SIZE, MEANING HOW MUCH WE SEE OF THAT WORLD DEPENDS ON THE CANVAS
    - WE WANT TO BE ABLE TO CONTROL THAT, THIS WOULD ALLOWS US TO ZOOM IN AND OUT
- SOLUTION:
    - ONE: WE COULD JUST SET OUR OWN PIXEL VALUES FOR THE CAMERA
        - BUT then that means we would include the points into the paint phase but if the camera is bigger we would not be able to see them, only if its smaller
        - We would have to then start thinking about what to do with the final image and any potential wasted space on the canvas, or cropping on the image.
    - TWO: SET WORLD UNITS AND NORMALIZE
- **FREE WILLIAM! (Allowing the camera to move free in the 3D WORLD)







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











































