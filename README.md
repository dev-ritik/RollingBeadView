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
<img src="/sample/hope_static.gif" align="right" height="450" width="470">
<img src="/sample/colors.gif" align="left" height="450" width="350">
<br>

## moving bead:

<img src="/sample/life_1.gif" align="right" width="430" height="450">
<img src="/sample/hope.gif" align="left" width="400" height="450">
<br>

# Documentation

Attributes for moving bead: `RollingBeadImageView`

|XML attribute   |Java set methods*          |Description                                  |attribute & parameter|Default Value |
|----------------|--------------------------|----------------------------------------------|-------------------|--------------|
|center_X        |setCenterCircle_X         | set the new current absolute X coordinate    |dimension            |0                 |
|                |                          |                                              |fraction                  |
|                |                          |                                              |float                   |
|                |                          |                                              |int                   |
|center_Y        |setCenterCircle_Y         | set the new current absolute Y coordinate    |dimension|0|
|                |                          |                                              |fraction                  |
|                |                          |                                              |float                   |
|                |                          |                                              |int                   |
|direction       |setDirection_Positive     | set the new absolute direction of motion     |positive           |
|movement        |setMovement               | set the new movement (in Pixels)             |15           |
|number_Of_Times |setNumberOfTimes          | set the new number of bead tails visible     |1           |
|orientation     |setOrientationHorizontal  | set the new absolute orientation             |positive           |
|radius          |setRadius                 | set the new radius                           |35           |
|repetition_Times|setRepetitionTime         | set the new half-period for bead movement    |70           |

# Source:
The initial algorithm for generating bead effect was taken from [this repo](https://github.com/ArashPartow/bitmap#simple-example-5---magnifying-lens-distortion)
