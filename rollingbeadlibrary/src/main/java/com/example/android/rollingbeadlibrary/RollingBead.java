package com.example.android.rollingbeadlibrary;

import android.graphics.Bitmap;
import android.util.Log;

public class RollingBead {

    private Bitmap changedBitmap, immutableBitmap;
    private int movingCoordinate = 0;
    private int constantCoordinate = 0;
    private int movement, radius, numberOfTimes, height, width;
    //   private int numberOfTimes = radius / movementInX;
    private boolean orientationHorizontal, directionPositive;

    public RollingBead() {
    }

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
        if (radius > 150)
            throw new IllegalArgumentException(String.format("radius %s not supported.", radius));
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

//        Log.i("point rb67", "height  " + height + "  width  " + width);

        if (width <= centerCircle_X || height <= centerCircle_Y || centerCircle_X < 0 || centerCircle_Y < 0)
            throw new IllegalArgumentException("Co-ordinates out of range");

    }

    public Bitmap generateBead(Bitmap toChangeBitmap, Bitmap originalBitmap, int centerCircle_X, int centerCircle_Y, int radius, double lens_factor, boolean roundX, boolean roundY) {
        int width = toChangeBitmap.getWidth();
        int height = toChangeBitmap.getHeight();

        if (width <= centerCircle_X || height <= centerCircle_Y || centerCircle_X < 0 || centerCircle_Y < 0)
            throw new IllegalArgumentException("Co-ordinates out of range");

        int terminalY;
        double distance, distortion;

        int sx, sy;

        for (int dx = radius; dx >= 0; --dx) {
            //R.H.S
            if (roundX) {
                if (centerCircle_X + dx >= width) {
                    centerCircle_X -= width;
                    if (centerCircle_X + dx >= width) break;
                } else if (dx + centerCircle_X < 0) {
                    centerCircle_X += width;
                }
            } else {
                if (centerCircle_X + dx > width) {
                    dx = toChangeBitmap.getWidth() - centerCircle_X;
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
                distortion = Math.pow((distance / radius), lens_factor);//radius^lens_factor  *  distance

                sx = (int) (distortion * dx + centerCircle_X);
                sy = (int) (distortion * dy + centerCircle_Y);
                if ((sx >= 0) && (sy >= 0) && (sx < width) && (sy < height)) {
                    toChangeBitmap.setPixel(dx + centerCircle_X, dy + centerCircle_Y, originalBitmap.getPixel(sx, sy));

                }
            }
        }

        for (int dx = -radius; dx <= 0; ++dx) {
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
                distortion = Math.pow((distance / radius), lens_factor);//radius ^ lens_factor  *  distance

                sx = (int) (distortion * dx + centerCircle_X);
                sy = (int) (distortion * dy + centerCircle_Y);

                if ((sx >= 0) && (sy >= 0) && (sx < width) && (sy < height)) {
                    toChangeBitmap.setPixel(dx + centerCircle_X, dy + centerCircle_Y, originalBitmap.getPixel(sx, sy));
                }
            }
        }
        return toChangeBitmap;
    }

    public Bitmap mixCircleBitmap(Bitmap toChangeBitmap, Bitmap flavouringBitmap, int centerCircle_X, int centerCircle_Y, int radius, double lens_factor, boolean roundX, boolean roundY) {

        int width = toChangeBitmap.getWidth();
        int height = toChangeBitmap.getHeight();

        if (width <= centerCircle_X || height <= centerCircle_Y || centerCircle_X < 0 || centerCircle_Y < 0)
            throw new IllegalArgumentException("Co-ordinates out of range");

        for (int dx = radius; dx >= -radius; --dx) {
            if (roundX) {
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

            int terminalY = (int) Math.sqrt((radius * radius) - (dx * dx));
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
                toChangeBitmap.setPixel(dx + centerCircle_X, dy + centerCircle_Y, flavouringBitmap.getPixel(dx + centerCircle_X, dy + centerCircle_Y));
            }

        }
        return toChangeBitmap;
    }

    void mixRectangleBitmap(Bitmap toChangeBitmap, Bitmap flavouringBitmap, int initialX,
                            int finalX, int constantCoordinate) {
//        Log.i("point rb159", "  constantCoordinate  " + constantCoordinate + "  initialX  " + initialX + "  finalX  " + finalX + "  width  " + width);

        //taking finalX>initialX
        for (int dx = initialX; dx <= finalX; ++dx) {
            if (dx >= width) {
                finalX -= width;
                dx -= width;
//                Log.i("point rb163", "dx  " + dx);

                if (dx >= width) break;

            } else if (dx < 0) {
//                Log.i("point rb168", "dx  " + dx);
                finalX += width;
                dx += width;
            }
            int terminalY = radius;
            for (int dy = -terminalY; dy <= terminalY; ++dy) {
                if (constantCoordinate + dy < 0) {
                    dy = -constantCoordinate;
                    continue;
                } else if (constantCoordinate + dy >= height) {
                    break;
                }
                if (orientationHorizontal)
                    toChangeBitmap.setPixel(dx, dy + constantCoordinate, flavouringBitmap.getPixel(dx, dy + constantCoordinate));
                else
                    toChangeBitmap.setPixel(dy + constantCoordinate, dx, flavouringBitmap.getPixel(dy + constantCoordinate, dx));

            }
        }
//        Log.i("point rb214", "mixBitmap ends");

    }

    void dissolveMovingBead(Bitmap toChangeBitmap, Bitmap flavouringBitmap) {
        int centerCircle_X = getPreviousMovingCoordinate();
//        Log.i("point rb174", "centerCircle_X  " + centerCircle_X + "  constantCoordinate  " + constantCoordinate + "  movement  " + movement);
//        Log.i("point rb156", "icon.getWidth(  " + width + "  getCenterCirlce_X()  " + centerCircle_X+"  height  "+height);
        for (int dx = -movement; dx < 0; ++dx) {
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
                if (orientationHorizontal)
                    toChangeBitmap.setPixel(dx + centerCircle_X, dy + constantCoordinate, flavouringBitmap.getPixel(dx + centerCircle_X, dy + constantCoordinate));
                else
                    toChangeBitmap.setPixel(dy + constantCoordinate, dx + centerCircle_X, flavouringBitmap.getPixel(dy + constantCoordinate, dx + centerCircle_X));
            }
        }
    }

    void generateMovingBead(Bitmap toChangeBitmap, Bitmap originalBitmap) {

        int centerCircle_X = getUpdatedMovingCoordinate();
        int terminalY;

        double distance;
//        double relativeRadius;
//        double distortion;
        int sx, sy;
        int terminalRight;
        int terminalLeft;

        if (directionPositive) {
            terminalRight = radius;
            terminalLeft = -movement;
        } else {
            terminalRight = movement;
            terminalLeft = -radius;
        }

//        Log.i("point rb234", "centerCircle_X  " + centerCircle_X + "  constantCoordinate  " + constantCoordinate + "  movement  " + movement);
//        double lens_factor = 1.0;

        for (int dx = terminalRight; dx >= 0; --dx) {
            //R.H.S
            if (dx + centerCircle_X >= width) {
                centerCircle_X -= width;
                if (centerCircle_X + dx >= width) break;

            } else if (dx + centerCircle_X < 0) {
                centerCircle_X += width;
            }
            terminalY = (int) Math.sqrt((radius * radius) - (dx * dx));
            for (int dy = -terminalY; dy <= terminalY; ++dy) {
                if (constantCoordinate + dy < 0) {
                    dy = -constantCoordinate;
                    continue;
                } else if (constantCoordinate + dy >= height) {
                    break;
                }
                distance = Math.sqrt((dx * dx) + (dy * dy));
//                relativeRadius = distance / radius;
//                distortion = Math.pow(relativeRadius, lens_factor);//radius^lens_factor  *  distance

//                sx = (int) (distortion * dx + centerCircle_X);
//                sy = (int) (distortion * dy + constantCoordinate);
                sx = (int) ((distance / radius) * dx + centerCircle_X);
                sy = (int) ((distance / radius) * dy + constantCoordinate);
                if ((sx >= 0) && (sy >= 0) && (sx < width) && (sy < height)) {
                    if (orientationHorizontal)
                        toChangeBitmap.setPixel(dx + centerCircle_X, dy + constantCoordinate, originalBitmap.getPixel(sx, sy));
                    else
                        toChangeBitmap.setPixel(dy + constantCoordinate, dx + centerCircle_X, originalBitmap.getPixel(sy, sx));

                }
            }
        }

        for (int dx = terminalLeft; dx <= 0; ++dx) {
            //L.H.S
            if (centerCircle_X + dx < 0) {
                dx = -centerCircle_X;
                continue;
            }
            terminalY = (int) Math.sqrt((radius * radius) - (dx * dx));
            for (int dy = -terminalY; dy <= terminalY; ++dy) {
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
                        toChangeBitmap.setPixel(dx + centerCircle_X, dy + constantCoordinate, originalBitmap.getPixel(sx, sy));
                    else
                        toChangeBitmap.setPixel(dy + constantCoordinate, dx + centerCircle_X, originalBitmap.getPixel(sy, sx));
                }
            }
        }
//        Log.i("point rb356", "generateBitmap ends");
    }


    protected void dissolveAll(Bitmap toChangeBitmap, Bitmap flavouringBitmap) {
//        Log.i("point rbi363", "dissolve all start");
        if (directionPositive)
            mixRectangleBitmap(toChangeBitmap, flavouringBitmap, getPreviousMovingCoordinate() - movement, getPreviousMovingCoordinate() + (numberOfTimes) * movement + radius, constantCoordinate);
        else
            mixRectangleBitmap(toChangeBitmap, flavouringBitmap, getPreviousMovingCoordinate() - (numberOfTimes) * movement - radius, getPreviousMovingCoordinate(), constantCoordinate);
    }

}
