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
    // immutableBitmap is the immutable bitmap to get pixels from
    private Bitmap changedBitmap, immutableBitmap;

    // movingCoordinate, constantCoordinate represents the two coordinate systems
    private int movingCoordinate = 0;
    private int constantCoordinate = 0;

    // other properties of bead
    // numberOfTimes represents number of beads visible before cleared,
    // minimum: 1 for positive an2 2 for negative direction
    private int movement, radius, numberOfTimes, height, width;
    //   private int numberOfTimes = radius / movementInX;

    private boolean orientationHorizontal, directionPositive;

//    private ImageView userImage;

    private int[] originalArray;

    public RollingBead(ImageView userImage) {
//        this.userImage = userImage;
//        Bitmap asd = ((BitmapDrawable) userImage.getDrawable()).getBitmap();
//        this(((BitmapDrawable) userImage.getDrawable()).getBitmap());
        getBitmapFromDrawable(userImage.getDrawable());
    }

    public RollingBead(Bitmap imageBitmap) {
        classifyBitmap(imageBitmap);
    }

    void getBitmapFromDrawable(Drawable drawable) {
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

    void classifyBitmap(Bitmap inputBitmap) {
        if (inputBitmap.isMutable()) {
            changedBitmap = inputBitmap;

        } else {
            changedBitmap = inputBitmap.copy(Bitmap.Config.ARGB_8888, true);
        }
    }

    //returns new moving coordinate for changing its position
    public int getUpdatedMovingCoordinate() {
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
    public int getPreviousMovingCoordinate() {
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

    public void setMovingCoordinate(int movingCoordinate) {
        this.movingCoordinate = movingCoordinate;
    }

    public int getConstantCoordinate() {
        return constantCoordinate;
    }

    public void setConstantCoordinate(int constantCoordinate) {
        this.constantCoordinate = constantCoordinate;
    }

    RollingBead(Bitmap changedBitmap, Bitmap immutableBitmap, int centerCircle_X, int centerCircle_Y, int movement, int radius, int numberOfTimes, boolean orientationHorizontal, boolean direction_Positive) {
//        Log.i("point rb65", "centerCircle_X  " + centerCircle_X + "  centerCircle_Y  " + centerCircle_Y);
        this.changedBitmap = changedBitmap;
        this.immutableBitmap = immutableBitmap;
        this.movement = movement;
        this.radius = radius;
        if (direction_Positive)
            this.numberOfTimes = numberOfTimes;
        else
            this.numberOfTimes = numberOfTimes + 1;
        if (numberOfTimes < 1)
            throw new IllegalArgumentException(String.format("number_Of_Times %s not supported.", numberOfTimes));

        this.orientationHorizontal = orientationHorizontal;
        this.directionPositive = direction_Positive;
        if (orientationHorizontal) {
            height = immutableBitmap.getHeight();
            width = immutableBitmap.getWidth();
            this.movingCoordinate = centerCircle_X;
            this.constantCoordinate = centerCircle_Y;
        } else {
            width = immutableBitmap.getHeight();
            height = immutableBitmap.getWidth();
            this.movingCoordinate = centerCircle_Y;
            this.constantCoordinate = centerCircle_X;
        }

        if (width <= centerCircle_X || height <= centerCircle_Y || centerCircle_X < 0 || centerCircle_Y < 0)
            throw new IllegalArgumentException("Co-ordinates out of range");

    }

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
        int width = changedBitmap.getWidth();
        int height = changedBitmap.getHeight();

        if (width <= centerCircle_X || height <= centerCircle_Y || centerCircle_X < 0 || centerCircle_Y < 0)
            throw new IllegalArgumentException("Co-ordinates out of range");

        //range of y for a given x
        int terminalY;

        //distance from current center coordinates
        //distortion is the amount of deflection to be made
        double distance, distortion;

        //calculated pixel to be copied to the present one
        int sx, sy;
        originalArray = new int[width * ((2 * radius) + 1)];
        Log.i("point rbi 253", centerCircle_X + " centerCircle_X  " + height + "  height");
        Log.i("point rbi 254", centerCircle_Y + "  centerCircle_Y  " + radius + "  radius");
        Log.i("point rbi 202", originalArray.length + "  length  " + width + "  width");
        if (centerCircle_Y + radius < height && centerCircle_Y - radius >= 0) {
            changedBitmap.getPixels(originalArray, 0, changedBitmap.getWidth(), 0, centerCircle_Y - radius, width, (2 * radius) + 1);
        } else {
            changedBitmap.getPixels(originalArray, 0, changedBitmap.getWidth(), 0, (centerCircle_Y - radius + height) % height, width, (radius + height - centerCircle_Y) % height);
            changedBitmap.getPixels(originalArray, width * ((radius + height - centerCircle_Y) % height), changedBitmap.getWidth(), 0, 0, width, (2 * radius + 1) - ((radius + height - centerCircle_Y) % height));

        }
        int[] changedArray = Arrays.copyOf(originalArray, originalArray.length);

        for (int dx = -radius; dx <= radius; ++dx) {
            //L.H.S
            if (roundX) {
                if (centerCircle_X + dx >= width) {
                    centerCircle_X -= width;
                    if (centerCircle_X + dx >= width) break;
                } else if (dx + centerCircle_X < 0) {
                    centerCircle_X += width;
                }
            } else {
                if (centerCircle_X + dx < 0) {
                    dx = -centerCircle_X;
                    continue;
                }
            }

            terminalY = (int) Math.sqrt((radius * radius) - (dx * dx));
            for (int dy = -terminalY; dy <= terminalY; ++dy) {
                if (roundY) {
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

                sx = (int) (distortion * dx);
                sy = (int) (distortion * dy);
//                if (sx + centerCircle_X >= width) sx -= width;
//                else if (sx + centerCircle_X < 0) sx += width;
//
//                if (sy + centerCircle_Y >= height) sy -= height;
//                else if (sy + centerCircle_Y < 0) sy += height;

//                changedBitmap.setPixel(dx + centerCircle_X, dy + centerCircle_Y, immutableBitmap.getPixel(sx, sy));


//                Log.i("point rb304", dx + "  dx  " + dy + "  dy  " + radius);
//                Log.i("point rb305", sx + "  sx  " + sy + "  sy  " + width);
//                Log.i("point rb254", (dx + centerCircle_X + ((dy + radius) * width)) + "  Lhs  " + ((sx + centerCircle_X) + ((sy + radius) * width)) + "  rhs ");
//                Log.i("point rb255", changedArray[dx + centerCircle_X + ((dy + radius) * width)] + "  changedArray  " + originalArray[(sx + centerCircle_X) + ((sy + radius) * width)] + "  originalArray  ");

                changedArray[dx + centerCircle_X + ((dy + radius) * width)] = originalArray[(sx + centerCircle_X) + ((sy + radius) * width)];
            }


        }
        centerCircle_Y = (centerCircle_Y + height) % height;
        if (centerCircle_Y + radius < height && centerCircle_Y - radius >= 0) {
            changedBitmap.setPixels(changedArray, 0, changedBitmap.getWidth(), 0, centerCircle_Y - radius, width, (2 * radius) + 1);
        } else {
            changedBitmap.setPixels(changedArray, 0, changedBitmap.getWidth(), 0, (centerCircle_Y - radius + height) % height, width, (radius + height - centerCircle_Y) % height);
            changedBitmap.setPixels(changedArray, width * ((radius + height - centerCircle_Y) % height), changedBitmap.getWidth(), 0, 0, width, (2 * radius + 1) - ((radius + height - centerCircle_Y) % height));

        }
//        Log.i("point rb320", "  end loop  ");

        return changedBitmap;
    }


    public Bitmap dissolveFixedBead(float centerCircle_XInDecimal, float centerCircle_YInDecimal, float radiusInDecimal, boolean roundX, boolean roundY) {
        return dissolveFixedBead((int) (centerCircle_XInDecimal * immutableBitmap.getHeight()),
                (int) (centerCircle_YInDecimal * immutableBitmap.getWidth()),
                (int) (radiusInDecimal * immutableBitmap.getHeight()),
                roundX,
                roundY);
    }

    // method to paste flavouringBitmap over toChangeBitmap at give centerCircle_X & centerCircle_Y with radius (to remove bead)
    public Bitmap dissolveFixedBead(int centerCircle_X, int centerCircle_Y, int radius, boolean roundX, boolean roundY) {

        int width = changedBitmap.getWidth();
        int height = changedBitmap.getHeight();

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
                changedBitmap.setPixel(dx + centerCircle_X, dy + centerCircle_Y, immutableBitmap.getPixel(dx + centerCircle_X, dy + centerCircle_Y));
            }

        }
        return changedBitmap;
    }

    // method to remove (rather put original pixels) over the supposed disturbed rectangular area
    void mixRectangleBitmap(int initialX,
                            int finalX, int constantCoordinate) {
//        Log.i("point rb159", "  constantCoordinate  " + constantCoordinate + "  initialX  " + initialX + "  finalX  " + finalX + "  width  " + width);

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

                // TODO: Rounding effect without effecting performance
                if (constantCoordinate + dy < 0) {
                    dy = -constantCoordinate;
                    continue;
                } else if (constantCoordinate + dy >= height) {
                    break;
                }

                // removing disturbance
                if (orientationHorizontal)
                    changedBitmap.setPixel(dx, dy + constantCoordinate, immutableBitmap.getPixel(dx, dy + constantCoordinate));
                else
                    changedBitmap.setPixel(dy + constantCoordinate, dx, immutableBitmap.getPixel(dy + constantCoordinate, dx));

            }
        }
//        Log.i("point rb214", "mixBitmap ends");
    }

    // dissolve trailing beads while moving
    void dissolveMovingBead() {

        //the previous ( after calculating number of times) center coordinate
        int centerCircle_X = getPreviousMovingCoordinate();
//        Log.i("point rb174", "centerCircle_X  " + centerCircle_X + "  constantCoordinate  " + constantCoordinate + "  movement  " + movement);

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
                    changedBitmap.setPixel(dx + centerCircle_X, dy + constantCoordinate, immutableBitmap.getPixel(dx + centerCircle_X, dy + constantCoordinate));
                else
                    changedBitmap.setPixel(dy + constantCoordinate, dx + centerCircle_X, immutableBitmap.getPixel(dy + constantCoordinate, dx + centerCircle_X));
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
//        double relativeRadius;
//        double distortion;

        int sx, sy;
        // actual left and right bounds of current bounds
        int terminalRight, terminalLeft;

        // taking boundaries as movement to reduce calculations
        // deciding according to direction of propagation
        if (directionPositive) {
            terminalRight = radius;
            terminalLeft = -movement;
        } else {
            terminalRight = movement;
            terminalLeft = -radius;
        }

//        Log.i("point rb234", "centerCircle_X  " + centerCircle_X + "  constantCoordinate  " + constantCoordinate + "  movement  " + movement);
//        double lens_factor = 1.0;

        for (int dx = terminalRight; dx >= terminalLeft; --dx) {
            //R.H.S

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

                sx = (int) ((distance / radius) * dx + centerCircle_X);
                sy = (int) ((distance / radius) * dy + constantCoordinate);

                if ((sx >= 0) && (sy >= 0) && (sx < width) && (sy < height)) {
                    if (orientationHorizontal)
                        changedBitmap.setPixel(dx + centerCircle_X, dy + constantCoordinate, immutableBitmap.getPixel(sx, sy));
                    else
                        changedBitmap.setPixel(dy + constantCoordinate, dx + centerCircle_X, immutableBitmap.getPixel(sy, sx));

                }
            }
        }

//        Log.i("point rb356", "generateBitmap ends");
    }

    // calling mixRectangleBitmap to dissolve supposed minimal effected rectangular area
    void dissolveAll() {
        if (directionPositive)
            mixRectangleBitmap(getPreviousMovingCoordinate() - movement, getPreviousMovingCoordinate() + (numberOfTimes) * movement + radius, constantCoordinate);
        else
            mixRectangleBitmap(getPreviousMovingCoordinate() - (numberOfTimes) * movement - radius, getPreviousMovingCoordinate(), constantCoordinate);
    }

}
