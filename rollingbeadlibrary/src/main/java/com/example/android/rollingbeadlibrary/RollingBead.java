package com.example.android.rollingbeadlibrary;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import java.util.Arrays;

// This class handles the all the background calculations involved

public class RollingBead {

    // changedBitmap is the resultant mutable bitmap to apply changes on
    // immutableBitmap is the immutable bitmap to get pixels from (used only in static bead)
    private Bitmap changedBitmap, immutableBitmap;

    // movingCoordinate, constantCoordinate represents the two coordinate systems
    private int movingCoordinate = 0;
    private int constantCoordinate = 0;

    // other properties of bead
    // numberOfTimes represents number of beads visible before cleared,
    // minimum: 1 for positive and 2 for negative direction
    private int movement, radius, numberOfTimes, height, width;
    //   private int numberOfTimes = radius / movementInX;

    private boolean orientationHorizontal, directionPositive;

    // originalArray contains unaltered pixel color from original bitmap from the required area (for moving bead)
    // changedArray contains changed pixel colors to be displayed (for moving bead)
    private int[] originalArray, changedArray;


    // constructor for static bead
    public RollingBead(ImageView userImage) {
        //g fet bitmap
        getBitmapFromDrawable(userImage.getDrawable());
        width = changedBitmap.getWidth();
        height = changedBitmap.getHeight();

    }

    // constructor for static bead
    public RollingBead(Bitmap imageBitmap) {
        // get required bitmaps
        classifyBitmap(imageBitmap);
        width = changedBitmap.getWidth();
        height = changedBitmap.getHeight();

    }

    private void getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return;
        }

        if (drawable instanceof BitmapDrawable) {
            classifyBitmap(((BitmapDrawable) drawable).getBitmap());
            return;
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                throw new IllegalArgumentException("can't affect ColorDrawable");
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
                classifyBitmap(bitmap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // get required bitmaps
    private void classifyBitmap(Bitmap inputBitmap) {
        if (inputBitmap.isMutable()) {
            changedBitmap = inputBitmap;
            immutableBitmap = inputBitmap.copy(inputBitmap.getConfig(), false);

        } else {
            immutableBitmap = inputBitmap;
            changedBitmap = immutableBitmap.copy(Bitmap.Config.ARGB_8888, true);
        }
    }

    //returns new moving coordinate for changing its position
    int getUpdatedMovingCoordinate() {
//        Log.i("point rb22", "getUpdatedMovingCoordinate initial  " + movingCoordinate);
        if (directionPositive) {
            if (movingCoordinate + movement < width) {
                movingCoordinate += movement;
            } else {
                movingCoordinate += movement - width;
            }
        } else {
            if (movingCoordinate > movement) {
                movingCoordinate -= movement;
            } else {
                movingCoordinate += width - movement;
            }
        }
//        Log.i("point rb24", "getUpdatedMovingCoordinate final  " + movingCoordinate);
        return movingCoordinate;
    }

    //returns old coordinate for clearing the bead
    int getPreviousMovingCoordinate() {
//        Log.i("point rb30", "getPreviouscenterCircle_X initial  " + movingCoordinate);
        if (directionPositive) {
            if (movingCoordinate > movement * numberOfTimes)
                return (movingCoordinate - movement * numberOfTimes);
            else
                return width + movingCoordinate - movement * numberOfTimes;
        } else {
            if (movingCoordinate + movement * numberOfTimes < width)
                return (movingCoordinate + movement * numberOfTimes);
            else
                return movingCoordinate + movement * numberOfTimes - width;
        }
    }

    public int getMovingCoordinate() {
        return movingCoordinate;
    }

    public int getConstantCoordinate() {
        return constantCoordinate;
    }

    // constructor for moving bead
    RollingBead(Bitmap changedBitmap, int centerCircle_X, int centerCircle_Y, int movement, int radius, int numberOfTimes, boolean orientationHorizontal, boolean direction_Positive) {
        this.changedBitmap = changedBitmap;
        this.movement = movement;
        this.radius = radius;

        if (numberOfTimes < 1)
            throw new IllegalArgumentException(String.format("number_Of_Times %s not supported.", numberOfTimes));
        if (direction_Positive)
            this.numberOfTimes = numberOfTimes;
        else
            this.numberOfTimes = numberOfTimes + 1;

        this.orientationHorizontal = orientationHorizontal;
        this.directionPositive = direction_Positive;
        if (orientationHorizontal) {
            height = changedBitmap.getHeight();
            width = changedBitmap.getWidth();
            this.movingCoordinate = centerCircle_X;
            this.constantCoordinate = centerCircle_Y;

            // only taking the horizontal strip of length 2*radius+1 and breadth as width
            originalArray = new int[width * ((2 * radius) + 1)];
            if (centerCircle_Y + radius < height && centerCircle_Y - radius >= 0) {
                changedBitmap.getPixels(originalArray, 0, changedBitmap.getWidth(), 0, centerCircle_Y - radius, width, (2 * radius) + 1);
            } else {
                // edge cases
                changedBitmap.getPixels(originalArray, 0, changedBitmap.getWidth(), 0, (centerCircle_Y - radius + height) % height, width, (radius + height - centerCircle_Y) % height);
                changedBitmap.getPixels(originalArray, width * ((radius + height - centerCircle_Y) % height), changedBitmap.getWidth(), 0, 0, width, (2 * radius + 1) - ((radius + height - centerCircle_Y) % height));

            }
        } else {
            width = changedBitmap.getHeight();
            height = changedBitmap.getWidth();
            this.movingCoordinate = centerCircle_Y;
            this.constantCoordinate = centerCircle_X;

            // taking almost the entire area of height * width - (width - ((2 * radius) + 1)) pixels using only a vertical strip as above
            originalArray = new int[height * width - (width - ((2 * radius) + 1))];
            if (centerCircle_X + radius < height && centerCircle_X - radius >= 0) {
                changedBitmap.getPixels(originalArray, 0, changedBitmap.getWidth(), centerCircle_X - radius, 0, (2 * radius) + 1, width);
            } else {
                // edge cases
                changedBitmap.getPixels(originalArray, 0, changedBitmap.getWidth(), (centerCircle_X - radius + height) % height, 0, (radius + height - centerCircle_X) % height, width);
                changedBitmap.getPixels(originalArray, ((radius + height - centerCircle_X) % height), changedBitmap.getWidth(), 0, 0, (2 * radius + 1) - ((radius + height - centerCircle_X) % height), width);

            }
        }
        changedArray = Arrays.copyOf(originalArray, originalArray.length);

        if (width <= movingCoordinate || height <= constantCoordinate || constantCoordinate < 0 || movingCoordinate < 0)
            throw new IllegalArgumentException("Co-ordinates out of range");

        if (2 * radius + 1 > height || 2 * radius + 1 > width)
            throw new IllegalArgumentException("can't change pixels twice!");


    }

    //method to receive parameters as float in [0,1) as pass to main method below
    public Bitmap generateFixedBead(float centerCircle_XInDecimal, float centerCircle_YInDecimal, float radiusInDecimal, double lens_factor, boolean roundX, boolean roundY) {
        return generateFixedBead((int) (centerCircle_XInDecimal * immutableBitmap.getHeight()),
                (int) (centerCircle_YInDecimal * immutableBitmap.getWidth()),
                (int) (radiusInDecimal * immutableBitmap.getHeight()),
                lens_factor,
                roundX,
                roundY);
    }

    // method to generate single bead at give centerCircle_X & centerCircle_Y, with provided lens_factor
    public Bitmap generateFixedBead(int centerCircle_X, int centerCircle_Y, int radius, double lens_factor, boolean roundX, boolean roundY) {
        if (width <= centerCircle_X || height <= centerCircle_Y || centerCircle_X < 0 || centerCircle_Y < 0)
            throw new IllegalArgumentException("Co-ordinates out of range");

        //range of y for a given x
        int terminalY;

        //distance from current center coordinates
        //distortion is the amount of deflection to be made
        double distance, distortion;

        //calculated pixel to be copied to the present one
        int sx, sy;

        for (int dx = radius; dx >= -radius; --dx) {
            if (roundX) {
                // rounding calculation
                if (centerCircle_X + dx >= width) {
                    centerCircle_X -= width;
                    if (centerCircle_X + dx >= width) break;
                } else if (dx + centerCircle_X < 0) {
                    centerCircle_X += width;
                }

            } else {
                if (centerCircle_X + dx > width) {
                    dx = changedBitmap.getWidth() - centerCircle_X;
                    continue;
                }
            }

            terminalY = (int) Math.sqrt((radius * radius) - (dx * dx));

            for (int dy = -terminalY; dy <= terminalY; ++dy) {
                if (roundY) {
                    // rounding calculation
                    if (centerCircle_Y + dy >= height) {
                        centerCircle_Y -= height;
                        if (centerCircle_Y + dy >= height) break;
                    } else if (dy + centerCircle_Y < 0) {
                        centerCircle_Y += height;
                    }

                } else {
                    if (centerCircle_Y + dy < 0) {
                        dy = -centerCircle_Y;
                        continue;
                    } else if (centerCircle_Y + dy >= height) {
                        break;
                    }
                }

                distance = Math.sqrt((dx * dx) + (dy * dy));
                distortion = Math.pow((distance / radius), lens_factor);

                sx = (int) (distortion * dx + centerCircle_X);
                sy = (int) (distortion * dy + centerCircle_Y);

                if (sx >= width) sx -= width;
                else if (sx < 0) sx += width;

                if (sy >= height) sy -= height;
                else if (sy < 0) sy += height;

                // pasting pixels
                changedBitmap.setPixel(dx + centerCircle_X, dy + centerCircle_Y, immutableBitmap.getPixel(sx, sy));

            }
        }
        return changedBitmap;// required changes incorporated
    }

    //method to receive parameters as float in [0,1) as pass to main method below
    public Bitmap dissolveFixedBead(float centerCircle_XInDecimal, float centerCircle_YInDecimal, float radiusInDecimal, boolean roundX, boolean roundY) {
        return dissolveFixedBead((int) (centerCircle_XInDecimal * immutableBitmap.getHeight()),
                (int) (centerCircle_YInDecimal * immutableBitmap.getWidth()),
                (int) (radiusInDecimal * immutableBitmap.getHeight()),
                roundX,
                roundY);
    }

    // method to dissolve changes at give centerCircle_X & centerCircle_Y with radius (to remove bead)
    public Bitmap dissolveFixedBead(int centerCircle_X, int centerCircle_Y, int radius, boolean roundX, boolean roundY) {

        if (width <= centerCircle_X || height <= centerCircle_Y || centerCircle_X < 0 || centerCircle_Y < 0)
            throw new IllegalArgumentException("Co-ordinates out of range");

        // entire domain
        for (int dx = radius; dx >= -radius; --dx) {
            if (roundX) {
                //considering rounding effect
                if (centerCircle_X + dx >= width) {
                    centerCircle_X -= width;
                    if (centerCircle_X + dx >= width) break;
                } else if (dx + centerCircle_X < 0) {
                    centerCircle_X += width;
                }
            } else {
                if (dx + centerCircle_X >= width) {
                    dx = width - centerCircle_X;
                    continue;
                } else if (dx + centerCircle_X < 0) {
                    break;
                }
            }

            // taking entire domain
            int terminalY = (int) Math.sqrt((radius * radius) - (dx * dx));
            for (int dy = -terminalY; dy <= terminalY; ++dy) {
                if (roundY) {
                    //considering rounding effect
                    if (centerCircle_Y + dy >= height) {
                        centerCircle_Y -= height;
                        if (centerCircle_Y + dy >= height) break;
                    } else if (dy + centerCircle_Y < 0) {
                        centerCircle_Y += height;
                    }

                } else {
                    if (centerCircle_Y + dy < 0) {
                        dy = -centerCircle_Y;
                        continue;
                    } else if (centerCircle_Y + dy >= height) {
                        break;
                    }
                }
                // pasting original pixels over changed
                changedBitmap.setPixel(dx + centerCircle_X, dy + centerCircle_Y, immutableBitmap.getPixel(dx + centerCircle_X, dy + centerCircle_Y));
            }

        }
        return changedBitmap;// required changes incorporated
    }

    //     method to remove (rather put original pixels) over the supposed disturbed rectangular area
    private void mixRectangleBitmap(int initialX,
                            int finalX, int constantCoordinate) {

        //taking finalX > initialX
        for (int dx = initialX; dx <= finalX; ++dx) {

            // rounding effect
            if (dx >= width) {
                finalX -= width;
                dx -= width;

                if (dx >= width) break;

            } else if (dx < 0) {
                finalX += width;
                dx += width;
            }

            //breadth == 2 * radius
            int terminalY = radius;

            for (int dy = -terminalY; dy <= terminalY; ++dy) {

                // TODO: Rounding effect without losing performance
                if (constantCoordinate + dy < 0) {
                    dy = -constantCoordinate;
                    continue;
                } else if (constantCoordinate + dy >= height) {
                    break;
                }

                // removing disturbance in array
                if (orientationHorizontal)
                    changedArray[dx + ((dy + radius) * width)] = originalArray[dx + ((dy + radius) * width)];
                else
                    changedArray[dy + radius + (dx * height)] = originalArray[dy + radius + (dx * height)];
            }
        }

        // providing changed array to bitmap
        if (orientationHorizontal) {
            if (constantCoordinate + radius < height && constantCoordinate - radius >= 0) {
                changedBitmap.setPixels(changedArray, 0, changedBitmap.getWidth(), 0, constantCoordinate - radius, width, (2 * radius) + 1);
            } else {
                changedBitmap.setPixels(changedArray, 0, changedBitmap.getWidth(), 0, (constantCoordinate - radius + height) % height, width, (radius + height - constantCoordinate) % height);
                changedBitmap.setPixels(changedArray, width * ((radius + height - constantCoordinate) % height), changedBitmap.getWidth(), 0, 0, width, (2 * radius + 1) - ((radius + height - constantCoordinate) % height));

            }
        } else {
            if (constantCoordinate + radius < height && constantCoordinate - radius >= 0) {
                changedBitmap.setPixels(changedArray, 0, changedBitmap.getWidth(), constantCoordinate - radius, 0, (2 * radius) + 1, width);
            } else {
                changedBitmap.setPixels(changedArray, 0, changedBitmap.getWidth(), (constantCoordinate - radius + height) % height, 0, (radius + height - constantCoordinate) % height, width);
                changedBitmap.setPixels(changedArray, ((radius + height - constantCoordinate) % height), changedBitmap.getWidth(), 0, 0, (2 * radius + 1) - ((radius + height - constantCoordinate) % height), width);

            }
        }
//        Log.i("point rb214", "mixBitmap ends");
    }

    // dissolve trailing beads while moving
    void dissolveMovingBead() {

        //the previous (after calculating number of times) center coordinate
        int centerCircle_X = getPreviousMovingCoordinate();

        // only from - movement to 0 to reduce calculations
        for (int dx = -movement; dx < 0; ++dx) {

            // rounding effect
            if (dx + centerCircle_X >= width) {
                centerCircle_X -= width;
                if (centerCircle_X + dx >= width) break;

            } else if (dx + centerCircle_X < 0) {
                centerCircle_X += width;
            }
            for (int dy = -radius; dy <= radius; ++dy) {
                if (constantCoordinate + dy < 0) {
                    dy = -constantCoordinate;
                    continue;
                } else if (constantCoordinate + dy >= height) {
                    break;
                }

                // actually dissolving them
                if (orientationHorizontal)
                    changedArray[dx + centerCircle_X + ((dy + radius) * width)] = originalArray[dx + centerCircle_X + ((dy + radius) * width)];
                else
                    changedArray[dy + radius + ((dx + centerCircle_X) * height)] = originalArray[dy + radius + ((dx + centerCircle_X) * height)];
                }
        }
        // providing changed array to bitmap
        if (orientationHorizontal)
            if (constantCoordinate + radius < height && constantCoordinate - radius >= 0) {
                changedBitmap.setPixels(changedArray, 0, changedBitmap.getWidth(), 0, constantCoordinate - radius, width, (2 * radius) + 1);
            } else {
                changedBitmap.setPixels(changedArray, 0, changedBitmap.getWidth(), 0, (constantCoordinate - radius + height) % height, width, (radius + height - constantCoordinate) % height);
                changedBitmap.setPixels(changedArray, width * ((radius + height - constantCoordinate) % height), changedBitmap.getWidth(), 0, 0, width, (2 * radius + 1) - ((radius + height - constantCoordinate) % height));

            }
        else {
            if (constantCoordinate + radius < height && constantCoordinate - radius >= 0) {
                changedBitmap.setPixels(changedArray, 0, changedBitmap.getWidth(), constantCoordinate - radius, 0, (2 * radius) + 1, width);
            } else {
                changedBitmap.setPixels(changedArray, 0, changedBitmap.getWidth(), (constantCoordinate - radius + height) % height, 0, (radius + height - constantCoordinate) % height, width);
                changedBitmap.setPixels(changedArray, ((radius + height - constantCoordinate) % height), changedBitmap.getWidth(), 0, 0, (2 * radius + 1) - ((radius + height - constantCoordinate) % height), width);

            }
        }
    }

    // method to generate bead while moving
    void generateMovingBead() {

        // get next center to move bead to
        int centerCircle_X = getUpdatedMovingCoordinate();

        int terminalY;

        // from center
        double distance;

        int sx, sy;
        // actual left and right bounds of current bounds
        int terminalRight, terminalLeft;

        // taking boundary as movement to reduce calculations
        // deciding according to direction of propagation
        if (directionPositive) {
            terminalRight = radius;
            terminalLeft = -movement;
        } else {
            terminalRight = movement;
            terminalLeft = -radius;
        }

        for (int dx = terminalRight; dx >= terminalLeft; --dx) {

            // rounding effect
            if (dx + centerCircle_X >= width) {
                centerCircle_X -= width;
                if (centerCircle_X + dx >= width) break;

            } else if (dx + centerCircle_X < 0) {
                centerCircle_X += width;
            }
            terminalY = (int) Math.sqrt((radius * radius) - (dx * dx));
            for (int dy = -terminalY; dy <= terminalY; ++dy) {

                // no rounding here
                if (constantCoordinate + dy < 0) {
                    dy = -constantCoordinate;
                    continue;
                } else if (constantCoordinate + dy >= height) {
                    break;
                }
                distance = Math.sqrt((dx * dx) + (dy * dy));

                sx = (int) ((distance / radius) * dx);
                sy = (int) ((distance / radius) * dy);

                if (sx + centerCircle_X >= width) sx -= width;
                else if (sx + centerCircle_X < 0) sx += width;

                if (sy + constantCoordinate >= height) sy -= height;
                else if (sy + constantCoordinate < 0) sy += height;

                // changing pixels at appropriate place
                if (orientationHorizontal)
                    changedArray[dx + centerCircle_X + ((dy + radius) * width)] = originalArray[(sx + centerCircle_X) + ((sy + radius) * width)];
                else {
                    changedArray[dy + radius + ((dx + centerCircle_X) * height)] = originalArray[(sy + radius) + ((sx + centerCircle_X) * height)];
                }
            }
        }
        // providing changed array to bitmap
        if (orientationHorizontal) {
            if (constantCoordinate + radius < height && constantCoordinate - radius >= 0) {
                changedBitmap.setPixels(changedArray, 0, changedBitmap.getWidth(), 0, constantCoordinate - radius, width, (2 * radius) + 1);
            } else {
                changedBitmap.setPixels(changedArray, 0, changedBitmap.getWidth(), 0, (constantCoordinate - radius + height) % height, width, (radius + height - constantCoordinate) % height);
                changedBitmap.setPixels(changedArray, width * ((radius + height - constantCoordinate) % height), changedBitmap.getWidth(), 0, 0, width, (2 * radius + 1) - ((radius + height - constantCoordinate) % height));

            }
        } else {
            if (constantCoordinate + radius < height && constantCoordinate - radius >= 0) {
                changedBitmap.setPixels(changedArray, 0, changedBitmap.getWidth(), constantCoordinate - radius, 0, (2 * radius) + 1, width);
            } else {
                changedBitmap.setPixels(changedArray, 0, changedBitmap.getWidth(), (constantCoordinate - radius + height) % height, 0, (radius + height - constantCoordinate) % height, width);
                changedBitmap.setPixels(changedArray, ((radius + height - constantCoordinate) % height), changedBitmap.getWidth(), 0, 0, (2 * radius + 1) - ((radius + height - constantCoordinate) % height), width);

            }
        }
//        Log.i("point rb356", "generateBitmap ends");
    }

    // calling mixRectangleBitmap to dissolve supposed maximal effected rectangular area
    void dissolveAll() {
        if (directionPositive)
            mixRectangleBitmap(getPreviousMovingCoordinate() - movement, getPreviousMovingCoordinate() + (numberOfTimes) * movement + radius, constantCoordinate);
        else
            mixRectangleBitmap(getPreviousMovingCoordinate() - (numberOfTimes) * movement - radius, getPreviousMovingCoordinate(), constantCoordinate);
    }

}
