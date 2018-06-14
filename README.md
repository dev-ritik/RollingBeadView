# RollingBeadView
RollingBead is an android library that can make bead (lens) moving effect in views like imageviews.

# Features
- <b>Bead movement</b> : The library provides enough methods to control movement of moving bead.
- <b>single bead</b> : Method to form and dissolve single bead has been provided.
- <b>Reduction in calculation</b> : Effort has been made to reduce large amount of calculations involved.
- <b>Unharmed image</b> : Care has been taken not to harm the original image provided
- <b>Async Calculations</b> : The library does all operations asynchronously to avoid blocking the UI thread.

# Usage
Currently the library supports two types of usage:

## static Bead:
<img src="/sample/hope_static.gif" align="right" height="450" width="480">
<img src="/sample/colors.gif" align="left" height="450" width="360">
<br>

## moving bead:
<img src="/sample/life_1.gif" align="right" width="350" height="450">

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
<img src="/sample/hope.gif" align="left" width="400" height="450">

```xml
<com.example.android.rollingbeadlibrary.RollingBeadImageView
        android:id="@+id/mimage"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerVertical="true"
        android:src="@drawable/hope_1"
        app:center_X="39%"
        app:center_Y="43%"
        app:direction="positive"
        app:movement="5%"
        app:number_Of_Times="1"
        app:radius="7.5%"
        app:orientation="horizontal"
        app:repetition_Times="92" />
```
<br>

# Documentation

Attributes for moving bead: `RollingBeadImageView`

|XML attribute       |Java set methods*          |Description                                   |Default Value |
|--------------------|---------------------------|----------------------------------------------|--------------|
|center_X*           |setCenterCircle_X##        | set the new current absolute X coordinate    |0                 |
|                    |getOriginalCenterCircle_X  | get originally set absolute X coordinate     |0|
|                    |getCurrentCenterCircle_X   | get the current absolute X coordinate        |0|
|center_Y*           |setCenterCircle_Y#         | set the new current absolute Y coordinate    |0|
|                    |getOriginalCenterCircle_Y  | get originally set absolute Y coordinate     |0|
|                    |getCurrentCenterCircle_Y   | get the current absolute Y coordinate        |0|
|radius*             |setRadius#                 | set the new radius                           |30           |
|                    |getRadius                  | get the current radius (in Px)               |30           |
|movement*           |setMovement#               | set the new movement                         |15           |
|                    |getMovement#               | set the current movement (in Px)             |15           |
|repetition_Times**  |setRepetitionTime          | set the new half-period for bead movement    |50           |
|                    |getRepetitionTime          | get the current half-period for bead movement|50           |
|number_Of_Times**   |setNumberOfTimes           | set the new number of bead tails visible     |1           |
|                    |getNumberOfTimes           | get the number of bead tails visible         |1           |
|orientation***      |setOrientationHorizontal   | set the new absolute orientation             |true(horizontal)           |
|                    |isOrientationHorizontal    | get the absolute orientation                 |true(horizontal)           |
|direction***        |setDirection_Positive      | set the new absolute direction of motion     |true(positive)          |
|                    |isDirection_Positive       | get the new absolute direction of motion     |true(positive)           |


*attribute: `dimension` or `fraction` <br>
**attribute: `integer` <br>
***attribute: `enum` <br>
*#* parameters: `int`(Pixels) or `float` [0,1)(w.r.t height) <br>
*##* parameters: `int`(Pixels) or `float` [0,1)(w.r.t width)
# Source:
The initial algorithm for generating bead effect was taken from [this repo](https://github.com/ArashPartow/bitmap#simple-example-5---magnifying-lens-distortion)
