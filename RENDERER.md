Starting tools:
- Canvas (awt.Canvas): It provides us with a rect on the screen to paint to. This rect has width and height defined in pixels.
- Vectors (Main.Vec): The Vec class allows us to store information about 3D points (x,y,z)

We start by creating some points with the vector class. We need to envision those points as real 3D points living in a 3D space, and out mission is to stamp them in our canvas somehow (The oficcial name of this is projection).

In order for us to accomplish this, we need need to insert our canvas into that 3D space. Now, the canvas is a 2D plane if you think about it, because it has two axis that we can use (X and Y). So, if the canvas is a 2D plane, it only makes sense that the easiest way to add our canvas into that 3D space is by invoking it on one of the 3 planes our 3D world has.

We will take the XY plane of the 3D world, meaning that if a point has a value of (5,12,7), we will translate that to our canvas as (5,12). Now we will do one modification to that, in reality we will inverse the Y, meaning that if the point is (2,5,7), we will translate it as (2,-5) to our canvas. This also means that by default we will be interpreting the 3D space units as pixel units, because our canvas works with pixels. We will want to change this later but for now that's where it stands.

Now that we know the plane that our canvas will be living in, we need to stablish three things; direction, position and projection range.

Direction:
With direction I mean from wich side of the plane we the users will be looking at it. You can picture this by imagining that you have two balls in front of you, you have one in front of you to your right and the onter one in fron of you as well but to your left. The one on the right is blue and the one on the left is red. Now if you walk pass them and turn around, then once you look at your right what you will see is the red ball instead of the blue one, and vice-versa. That is what I mean with direction, and the way we decide that in this case is by how we interpret the axis values of the plane we have selected, the XY plane. By leaving the X axis interpretation as-is from the 3D world's XY plane to the 2D canvas, we decided that our direction would be Z+.

This however, does not mean that we will only render from wathever point forward, we still have not decided on WHERE in the Z axis our plane will be, because the truth of the matter is that when we have indeed already selected what plane to use, we haven't really positioned that plane in the Z axis.

Positioning:
Right now, our canvas is set to project from the XY plane of the 3D world, but if we think about it, a plane doesn't usually has limits, its an infinite plane. However our canvas has limits; it has a width and a height. So just deciding on the plane is not enough, we need to position that canvas somewhere in the world's XY plane.

In order to do that we need to understand units of measurement. We know that our Canvas's units are pixels, but what are our 3D world's units?? Well, for now they will also be pixels, but we might want to come back to this later. It is enough for now to understand that in order for us to properly place our finite 2D canvas into the 3D world plane we need to figure out what units our canvas works with and what units our 3D world works with. So for now, to make it simple we will have them both use the same.

Now, another thing that we need to figure out is origin points. As it is right now, our canvas's origin point is located where the 3D world's origin point is. But we will change that to make it look like our canvas is directly looking at the world's origin point (remember our canvas' origin point is its top left corner). To do this we just need a method that offsets each point values. So, each time we are working with a 3D point of that world we run it trough that method and obtain the new value, like this:

    static public void WorldToCanvas(Vec p, int w, int h) {
        // w and h are the width and height of the canvas
        p.x = (w/2) + p.x;
        p.y = (h/2) + p.y;
    }

Projection range (Field of view):

To explain projection range, lets first talk about the problem that it solves:

As it is right now, our plane is a finite plane centered at the origin of the 3D world. If we were to start projecting all the points we find, we would paste into our canvas every single point whos X and Y value where within the limits of the plane. But that is not what we want, we want to use that direction we set so that we only paint the points that are in front of us, not the ones behind us. However there is no front and back right now because we as a canvas dont have a notion of depth. The solution to this is obviously to give our canvas a range; to tell it: Hey, only include points that are from this Z point forward, or only from this Z point backward, or only from this to this Z points. That's exactly what a field of view is; two Z values that define a range in the Z axis that we want to work with.

The closest Z value of the two is what is called the near plane or near clip in the FOV, and the further one is called the the far plane. It stands to reason that if your direction is Z+, the nearest value will be lower than the furthest one.


So far we have managed to put our 2D canvas inside the 3D world; we have given it a location, a direction, a size, a range. All of this is good but the way we are rendering things right now is very dependant on the canvas properties. We have no way of achieving similar results with a different canvas. The main reason for this right now are the aspect ratio and pixel density.

If we have two canvases that in your screen measure 5*4 inches, but one of them has a much denser pixel density, then that one will be able to render a much zoomed out view of our 3D world, things will look much smaller and we will be able to see so much more. We want to fix this somehow, we want to make it to work in a way similar to playing a movie in the TV and playing it in the phone, both of them render the same images scaled out of their particular canvases.

The solution to this problem is something called normalizing. We need to normalize the canvas. This means that this pixel units we have been depending on will have to be changed. We will have to come up with a measurement unit for our world, our 3D space. We will call it world unit. So a point at (2,0,0) is two world units to the right of the world's origin.

Now, because our canvas needs to live in the 3D space and that space now has its own units, we need to come up with a size for it. We can no longer use pixels. It's no longer going to be "Hey this (2,2,0) point just paint it two pixels to the right and top pixels up on the canvas".

We will assign its own width and height to our finite 2D plane that lives inside the 3D world, at its units will be world units. If our finite 2D canvas (we will start calling this finite 2D canvas that lives inside the 3D world space a camera) has a size of (5,5) it means that our real canvas that we paint on will have to somehow accomodate that 5,5 image into the canvas.

What this will do is that we will now have two canvas, each with its own size. The camera will be one of those canvases and then the actual canvas where we paint on will be the second one (lets call it the application canvas). Our new mission will be to make sure that no matter how our application's canvas is, we always render the camera's canvas the best we can, using as much space as we can of it.

There are three ways of going about this:

The first way is what I call an inner resizing (Image 50). You can see there that we scale the Camera canvas down to fit the application canvas, but try to scale it as little as we can.

The second solution is an outer resizing (Image 50B), which means that we take an axis we want to have control over (horizontal or vertical one) and we scale the Camera down or up to match that axis, and then for the second axis we allow the camera to go while and we render as much as allowed.

Lastly, the third way involves syncing the aspect ratio of the camera's canvas to that of the application canvas. This means that the camera's size is not defined by its width and height, but by a scaling factor applied to the width and height, because its W and H would only be used to define the aspect ratio which is locked to be that of the application's canvas, so the size would be (aspect ratio x scaling factor); two decimal numbers.

The third approach seems to be the most practical, the first one has its uses too. The second one I think its unusable, there's no way you would want to have that, is the approach that gives you the less control over what you end up seeing because it leaves all to the application's canvas.

A side note, have you played old games in modern hardware? If so you might have seen how some of them have some permanent black vertical stripes on the side. I am inclined to guess that has something to do with approach one. Modern games seem to be using approach three, and nobody seems to be using approach two, but is good to know about it because it helps us see how things work in our mind.

Now we have touched on aspect ratio a bit but we havent really explained it that much, so lets talk about it:

Aspect ratio is a decimal number that records the size relation between the width and the height. The formula for the aspect ratio is w/h.

There are three possible values that we can get from that formula: A number between 0 and 1 (not included), an exact 1, a number superior than 1.

If we get a 1 it means the width and the height are the same, which means our screen's shape is that of a square. If we however, get a number that is less than 1 it means that our screen's height is more than our screen's width, which means our screen has porltrait-ish dimensions, and the further away from the one it gets, the more vertical our screen grows. Reaching 0 is impossible if you think about it, so any number less than 1 stays between 0 and 1. Lastly if our number is superior than one it means our screen's width is superior than our height's, which means the shape of the screen is more inline with a landscape form. Simirarly, the furthest from the one the more it grows horizontally. This time you might get numbers supperior than 2.

Exercise: If you think about it the counterpart of 0 on a portrait aspect ratio is infinite for the landscape aspect ratio. Try to picture it, can you see infinite ending in zero and inverting the whole thing? Infinite is what happens when you go outside of the world, it means to come from below 0, you invert one of the axis of the screen. Also, the "speed" at which you go increments at not a steady rate, but faster and faster as you get further away from 1.

The aspect ratio of a screen is usually not written down as the decimal number, people think it more elegant to write it down as a fraction (6:20, 8:16, aka Six by twenty, Eight by Sixteen), but since we are computing stuff with that number we need to use its decimal representation. Also the norm when talking about aspect ratio is to divide the Width by the Height, if we do the opposite of that then the 0 to 1 would be landscape and 0+ would be portrait.


Implementing Solution 3 (Image 50C):

Right now we have our camera in the 3D world (Remember, that finite canvas that we spawned on the 3D world is now called the camera and the actual canvas in our application window is the application canvas now). We need the camera to have the same aspect ratio as the application canvas, but we also need to give it a size in the 3D world, in world units. The way we do that is with the already mentioned (aspect ratio * scale factor) formula. We would have a field in our camera class called "size" and that woud be our scale factor. This would "spawn" a camera in our beautiful 3D world with the correct aspect ratio and we would be able to scale it up or down as we see fit wit the size property.

We are missing something tho, and that is, in broad terms: "connecting the camera with the application canvas so taht the app canvas renders what's to be rendered", lets dive into that now:
What we have right now with our camera is basically a rect that we can consult to know what needs to be rendered and what doesn't. We have that because this rect we have has world units and a near and far planes that allow us to consult each point with this perimeter and check if the point should be rendered or not. But how do we render it? We can no longer just go "Well the point says (3,2,5) so we just need to do the same on the canvas (3,2)", we cannot do that because that (3,2,5) is in world units, but our canvas works with pixel units, so we need a way of translating those world units into pixel ones.

We know that our camera is centered, as we specified earlier, looking at the origin at some point in the (0,0,Z), and we know that with the value we have given to the near and far planes, that Z position is the value of the near plane, and we want to render as far as the value of the far plane dictates. This means that (w/2,h/2) of the application canvas equals to (0,0,0) in the 3D world, which also qeuals (w/2, h/2) of the camera's canvas. With that in mind, all we have to do is something called normalizing. We need to normalize the point's location in reference to the camera's canvas.

What does it mean to normalize? It means that if we have a camera with a width of 10 world units, and a point at (2.5,0,0) world units, that points normalized value will be 0.5 where 0 means the origin point and 1 means the right edge of the camera's canvas, which also directly applies to the application's canvas, so once we get the 0.5 normalized value we can convert that back to pixel units on the application canvas. However let me go back and re-explain that but step by step now.

As you see in the image 86, to normalize the point's position you take the camera's width and height (which you can calculate by doing: app canvas width / App canvas height * scale factor for width and app canvas height / app canvas width * scale factor for height) and do: [x/(w/2), y/(h/2), z].

Then you convert that normalized value to pixel units for your application canvas by doing: [x(w/2), y(h/2), z] where w and h are the canva's width and height in pixels this time.

State of affairs:
Now this is great, we have come from a vector with three floats all the way up to a 3D world space with their own units, a camera and a proper way to render what the camera has into our own application canvas.

We will soon start to think about how movement and rotation could work, and also about how to connect those dots (render some wireframe), but first we need to talk about something else, and that is isometric and perspective cameras.

What we have right now is an isometric camera, that is; a camera where Z doesn't mean anything at all, depth doesnt exists.

Lets imagine for a second that instead of dots we are rendering cubes; we have this 3D world were we have three cubes, one on the left, one at the center and one at the right (from where we are looking at them, that is). Now lets also imagine that they are not at the same distant from us, the one on our left is far away, 30 units in depth, the one in the center is close to us and the one in the right is in between the left and center one when it comes to how far away it is (all of this difference in the Z axis of course), if we were to render those cubes, the three of them would look like they are at the same Z distance. What should look like small little cube at our left will look like a cube thats just as big as the one in the center, same with the right one.

We have gotten far enough









































