# RollingBeadView
**version 1.0**

RollingBead is an android library that can make bead (lens) moving effect in Views like ImageViews(as of now). This is available in two 
flavours, i.e, MovingBead and StaticBead. Both can be used as and when required to produce circular bead(lens) effect.

# Features
- <b>Moving Bead</b> : The library provides enough methods to control movement of moving bead.
- <b>single(Static) Bead</b> : Method to form and dissolve single bead has been provided.
- <b>Reduction in calculation</b> : Effort has been made to reduce large amount of calculations involved.
- <b>Unharmed image</b> : Care has been taken not to harm the original image provided.
- <b>Consistency</b> : It provides required attributes and parameters to make it consistent accross screen densities.
- <b>Async Calculations</b> : The library does all operations(Moving Bead) asynchronously to avoid blocking the UI thread.

# Usage
Add the following dependency to your app's build.gradle file:

```groovy
allprojects {
	repositories {
		maven { url 'https://www.jitpack.io' }
	}
}
```
```groovy
dependencies {
	        implementation 'com.github.ritik1991998:RollingBeadView:v1.0'
	}
```

Currently the library supports two types of usage:

## static Bead:
* Get the ImageView object and pass in to RollingBead as parameter
```java
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        RollingBead rb = new RollingBead(imageView);
        // or directly pass the bitmap
```

* use the `generateFixedBead` method to generate a static bead

* use the `dissolveFixedBead` method to dissolve a static bead

**Example 1**
<img src="/sample/colors.gif" align="right" height="220" width="250"><br>

```java
imageView.setImageBitmap(rb.generateFixedBead(0.025f, 0.025f, 0.45f, 2.0, true, true));
```
```java
imageView.setImageBitmap(rb.dissolveFixedBead(0.025f, 0.025f, 0.45f, true, true));
```

**Example 2**

<img src="/sample/hope_static.gif" align="right" height="220" width="250">
<br>

```java
imageView.setImageBitmap(rb.generateFixedBead(0.93f, 0.46f, 0.2f, 2.0, true, true));
```
```java
imageView.setImageBitmap(rb.dissolveFixedBead(0.93f, 0.46f, 0.2f, true, true));
```

## moving bead:
**Example 1**
<img src="/sample/life_1.gif" align="right" width="320" height="330">

```xml
 <com.example.android.rollingbeadlibrary.RollingBeadImageView
        android:id="@+id/mimage"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerVertical="true"
        android:src="@drawable/life"
        app:center_X="37%"
        app:center_Y="5%"
        app:direction="positive"
        app:movement="3.7%"
        app:number_Of_Times="1"
        app:radius="14.8%"
        app:orientation="vertical"
        app:repetition_Times="220" />
```

**Example 2**

<img src="/sample/hope.gif" align="right" width="350" height="400">

```xml
    <com.example.android.rollingbeadlibrary.RollingBeadImageView
        android:id="@+id/mimage"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerVertical="true"
        android:src="@drawable/hope_1"
        app:center_X="245dp"
        app:center_Y="203dp"
        app:direction="positive"
        app:movement="23dp"
        app:number_Of_Times="1"
        app:radius="35dp"
        app:orientation="horizontal"
        app:repetition_Times="92" />
```
<br>

# Documentation

## static Bead:
* Parameters for moving bead: `RollingBead`

|Java methods**       |Parameters      | Description                                       |Range                 |
|-------------------|----------------|---------------------------------------------------|----------------------|
|generateFixedBead  | centerCircle_X | X coordinate of the bead                          |int(Px) or Float[0,1) |
|                   | centerCircle_Y | Y coordinate of the bead                          |int(Px) or Float[0,1) |
|                   | radius         | radius of the bead                                |int(Px) or Float[0,1) |
|                   | lens_factor    | intensity of deviation of the bead                |double[0,∞) and -ve(may cause error)|
|                   | roundX         | should the effect be pronounced round X axis edges|boolean               |
|                   | roundY         | should the effect be pronounced round Y axis edges|boolean               |
|dissolveFixedBead* | centerCircle_X | X coordinate of the bead                          |int(Px) or Float[0,1) |
|                   | centerCircle_Y | Y coordinate of the bead                          |int(Px) or Float[0,1) |
|                   | radius         | radius of the bead                                |int(Px) or Float[0,1) |
|                   | roundX         | should the effect be pronounced round X axis edges|boolean               |
|                   | roundY         | should the effect be pronounced round Y axis edges|boolean               |

*Or use generateFixedBead with `0.0` as `lens_factor`<br>
**returns Bitmap object

----

## moving bead:
* Attributes for moving bead: `RollingBeadImageView`

|XML attribute       |Java methods               |Description                                   |Default Value   |
|--------------------|---------------------------|----------------------------------------------|----------------|
|center_X*#          |setCenterCircle_X##        | set the new current absolute X coordinate    |0               |
|                    |getOriginalCenterCircle_X  | get originally set absolute X coordinate     |0               |
|                    |getCurrentCenterCircle_X   | get the current absolute X coordinate        |0               |
|center_Y*           |setCenterCircle_Y#         | set the new current absolute Y coordinate    |0               | 
|                    |getOriginalCenterCircle_Y  | get originally set absolute Y coordinate     |0               |
|                    |getCurrentCenterCircle_Y   | get the current absolute Y coordinate        |0               | 
|radius*             |setRadius#                 | set the new radius                           |30              |
|                    |getRadius                  | get the current radius (in Px)               |30              |
|movement*           |setMovement#               | set the new movement                         |15              |
|                    |getMovement#               | set the current movement (in Px)             |15              |
|repetition_Times**  |setRepetitionTime          | set the new half-period for bead movement    |50              |
|                    |getRepetitionTime          | get the current half-period for bead movement|50              |
|number_Of_Times**   |setNumberOfTimes           | set the new number of bead tails visible     |1               |
|                    |getNumberOfTimes           | get the number of bead tails visible         |1               |
|orientation***      |setOrientationHorizontal   | set the new absolute orientation             |true(horizontal)|
|                    |isOrientationHorizontal    | get the absolute orientation                 |true(horizontal)|
|direction***        |setDirection_Positive      | set the new absolute direction of motion     |true(positive)  |
|                    |isDirection_Positive       | get the new absolute direction of motion     |true(positive)  |

*attribute: `dimension` or `fraction` [0%,100%)(w.r.t height) <br>
*#attribute: `dimension` or `fraction` [0%,100%)(w.r.t width)<br>
**attribute: `integer` <br>
***attribute: `enum` <br>
*#* parameters: `int`(Pixels) or `float` [0,1)(w.r.t height) <br>
*##* parameters: `int`(Pixels) or `float` [0,1)(w.r.t width)

----

# Limitations:

The methods used in the library intensively uses system's resources. Thus encountering some of the vital limitations!!. Do
go through these before using the library:

## static Bead:
* Static bead function is currently handled on the **UI** thread. It will be improved in the future. Currently you
  can make separate thread yourself or make a PR for the same.
  
* making `lens_factor` negative squeezes the image, (instead of bulging it), but this might produce error!!.

## moving bead:
* The entire movingBeadImageView is highly vulnerable to frequent GC event, thus affectingly reducing performance and
 disturbing the UI thread notoriously. To counter this, **reduce the radius, movement and increase repetition_Times**.
 At the same time smaller image and horizontal orientation will be quiet helpful.
 
* When moving forward, moving bead creates a major segment instead of complete circle to reduce calculation.

* Though moving bead does't disturbs a single pixel after passing, please use `stopRender` when switching the current activity.

* Following the point above, don't put `set` request consecutively within `repetition_Times` interval. This might severely
  damage the input image.

# Contributions!

All contributions are welcome and appreciated. Please make a Pull Request or open an issue, if necessary.
This may also include any form of feature enhancement. Every constructive criticism is welcome.

# Source:
The initial algorithm for generating bead effect was taken from [this repo](https://github.com/ArashPartow/bitmap#simple-example-5---magnifying-lens-distortion)
