package com.example.android.rollingbeadlibrary;

import android.graphics.Bitmap;
import android.util.Log;

public class RollingBead {

    private Bitmap changedBitmap;
    private Bitmap immutableBitmap;
    private int centerCircle_X = 0;
    private int centerCircle_Y = 350;
    private boolean generateCycle = true;
    private int movement;
    private int radius;
    private int numberOfTimes;
    private final int height;
    private final int width;
    //   private int numberOfTimes = radius / movementInX;
    private boolean orientationHorizontal;
    private boolean directionPositive;

    public int getcenterCircle_X() {
        return centerCircle_X;
    }

    public int getUpdatedcenterCircle_X() {
//        Log.i("point rb22", "getUpdatedcenterCircle_X initial  " + centerCircle_X);
//        centerCircle_X = (movementInX + centerCircle_X) % width;
        if (directionPositive) {
            if (centerCircle_X + movement < width) {
                centerCircle_X += movement;
            } else {
                centerCircle_X += movement - width;
            }
        } else {
            if (centerCircle_X > movement) {
                centerCircle_X -= movement;
            } else {
                centerCircle_X += width - movement;
            }
        }
//        Log.i("point rb24", "getUpdatedcenterCircle_X final  " + centerCircle_X);
        return centerCircle_X;
    }

    public int getPreviouscenterCircle_X() {
//        Log.i("point rb30", "getPreviouscenterCircle_X initial  " + centerCircle_X);
        if (directionPositive) {
            if (centerCircle_X > movement * numberOfTimes)
                return (centerCircle_X - movement * numberOfTimes);
            else
                return width + centerCircle_X - movement * numberOfTimes;
        } else {
            if (centerCircle_X + movement * numberOfTimes < width)
                return (centerCircle_X + movement * numberOfTimes);
            else
                return centerCircle_X + movement * numberOfTimes - width;
        }
    }

    public void setcenterCircle_X(int centerCircle_X) {
        this.centerCircle_X = centerCircle_X;
    }

    public RollingBead(Bitmap changedBitmap, Bitmap immutableBitmap, int centerCircle_X, int centerCircle_Y, int movement, int radius, int numberOfTimes, boolean orientationHorizontal, boolean direction_Positive) {
        this.changedBitmap = changedBitmap;
        this.immutableBitmap = immutableBitmap;
        this.movement = movement;
        this.radius = radius;
        if (direction_Positive)
            this.numberOfTimes = numberOfTimes;
        else
            this.numberOfTimes = numberOfTimes + 1;

        this.orientationHorizontal = orientationHorizontal;
        this.directionPositive = direction_Positive;
        if (orientationHorizontal) {
            height = immutableBitmap.getHeight();
            width = immutableBitmap.getWidth();
            this.centerCircle_X = centerCircle_X;
            this.centerCircle_Y = centerCircle_Y;
        } else {
            width = immutableBitmap.getHeight();
            height = immutableBitmap.getWidth();
            this.centerCircle_X = centerCircle_Y;
            this.centerCircle_Y = centerCircle_X;
        }

        if (height <= centerCircle_X || width <= centerCircle_Y || centerCircle_X < 0 || centerCircle_Y < 0)
            throw new IllegalArgumentException("center exceeds bitmap size!!");

    }

    private Bitmap convert(Bitmap toChangeBitmap, Bitmap originalBitmap, int lens_center_x, int centerCircle_Y, int lens_radius) {
        Bitmap outputBitmap = toChangeBitmap;
//        Log.i("point rb86", "width  " + toChangeBitmap.getWidth() + "  height  " + toChangeBitmap.getHeight());
//        Log.i("point rb87", "lens_center_x  " + lens_center_x + "  centerCircle_Y  " + centerCircle_Y);
        double lens_factor = 1.0;
        for (int dx = lens_radius; dx >= 0; --dx) {
            //R.H.S
            if (lens_center_x + dx > toChangeBitmap.getWidth()) {
                dx = toChangeBitmap.getWidth() - lens_center_x;
                continue;
            }
            int terminalY = (int) Math.sqrt((lens_radius * lens_radius) - (dx * dx));
            for (int dy = -terminalY; dy <= terminalY; ++dy) {
                if (centerCircle_Y + dy < 0) {
                    dy = -centerCircle_Y;
                    continue;
                } else if (centerCircle_Y + dy >= toChangeBitmap.getHeight()) {
                    break;
                }
                double distance = Math.sqrt((dx * dx) + (dy * dy));
                double relativeRadius = distance / lens_radius;
                double distortion = Math.pow(relativeRadius, lens_factor);//radius^lens_factor  *  distance

                int sx = (int) (distortion * dx + lens_center_x);
                int sy = (int) (distortion * dy + centerCircle_Y);
                if ((sx >= 0) && (sy >= 0) && (sx < toChangeBitmap.getWidth()) && (sy < toChangeBitmap.getHeight())) {
//                    Log.i("point rb110", "dx  " + dx + "  dy  " + dy);

                    outputBitmap.setPixel(dx + lens_center_x, dy + centerCircle_Y, originalBitmap.getPixel(sx, sy));

                }
            }
        }
        for (int dx = -lens_radius; dx <= 0; ++dx) {
            //L.H.S
            if (lens_center_x + dx < 0) {
                dx = -lens_center_x;
                continue;
            }
            int terminalY = (int) Math.sqrt((lens_radius * lens_radius) - (dx * dx));
            for (int dy = -terminalY; dy <= terminalY; ++dy) {
                if (centerCircle_Y + dy < 0) {
                    dy = -centerCircle_Y;
                    continue;
                } else if (centerCircle_Y + dy >= toChangeBitmap.getHeight()) {
                    break;
                }
                double distance = Math.sqrt((dx * dx) + (dy * dy));

                double radius = distance / lens_radius;
                double distortion = Math.pow(radius, lens_factor);//radius^lens_factor  *  distance

                int sx = (int) (distortion * dx + lens_center_x);
                int sy = (int) (distortion * dy + centerCircle_Y);

                if ((sx >= 0) && (sy >= 0) && (sx < toChangeBitmap.getWidth()) && (sy < toChangeBitmap.getHeight())) {
//                    Log.i("point ma70", "dx  " + dx + "  dy  " + dy);
                    outputBitmap.setPixel(dx + (int) lens_center_x, dy + (int) centerCircle_Y, originalBitmap.getPixel(sx, sy));
                }
            }
        }
        return outputBitmap;
    }

    private Bitmap mixBitmapCircle(Bitmap toChangeBitmap, Bitmap flavouringBitmap, int lens_center_x, int lens_center_y, int lens_radius) {
        Bitmap outputBitmap = toChangeBitmap;
        for (int dx = lens_radius; dx >= -lens_radius; --dx) {
            if (dx + lens_center_x >= width) {
                dx = width - lens_center_x;
                continue;
            } else if (dx + lens_center_x < 0) {
                break;
            }
            int terminalY = (int) Math.sqrt((lens_radius * lens_radius) - (dx * dx));
            for (int dy = -terminalY; dy <= terminalY; ++dy) {
                if (lens_center_y + dy < 0) {
                    dy = -lens_center_y;
                    continue;
                } else if (lens_center_y + dy >= height) {
                    break;
                }
                outputBitmap.setPixel(dx + lens_center_x, dy + lens_center_y, flavouringBitmap.getPixel(dx + lens_center_x, dy + lens_center_y));
            }
        }
        return outputBitmap;
    }

    private void mixBitmapRectangle(Bitmap toChangeBitmap, Bitmap flavouringBitmap, int initialX, int finalX, int centerCircle_Y) {
        Log.i("point rb159", "  centerCircle_Y  " + centerCircle_Y + "  initialX  " + initialX + "  finalX  " + finalX + "  width  " + width);

        //taking finalX>initialX
        for (int dx = initialX; dx <= finalX; ++dx) {
            if (dx >= width) {
                finalX -= width;
                dx -= width;
                Log.i("point rb163", "dx  " + dx);

                if (dx >= width) break;

            } else if (dx < 0) {
                Log.i("point rb168", "dx  " + dx);
                finalX += width;
                dx += width;
            }
            int terminalY = radius;
            for (int dy = -terminalY; dy <= terminalY; ++dy) {
                if (centerCircle_Y + dy < 0) {
                    dy = -centerCircle_Y;
                    continue;
                } else if (centerCircle_Y + dy >= height) {
                    break;
                }
                if (orientationHorizontal)
                    toChangeBitmap.setPixel(dx, dy + centerCircle_Y, flavouringBitmap.getPixel(dx, dy + centerCircle_Y));
//                    toChangeBitmap.setPixel(dx, dy + centerCircle_Y, flavouringBitmap.getPixel(dx, dy + centerCircle_Y));
                else
                    toChangeBitmap.setPixel(dy + centerCircle_Y, dx, flavouringBitmap.getPixel(dy + centerCircle_Y, dx));

            }
        }
        Log.i("point rb214", "mixBitmap ends");

    }

    public void dissolveBitmap(Bitmap toChangeBitmap, Bitmap flavouringBitmap) {
        int centerCircle_X = getPreviouscenterCircle_X();
//        Bitmap outputBitmap = toChangeBitmap;
        Log.i("point rb174", "centerCircle_X  " + centerCircle_X + "  centerCircle_Y  " + centerCircle_Y + "  movement  " + movement);
//        Log.i("point rb156", "icon.getWidth(  " + width + "  getCenterCirlce_X()  " + centerCircle_X+"  height  "+height);
        for (int dx = -movement; dx < 0; ++dx) {
            if (dx + centerCircle_X >= width) {
                centerCircle_X -= width;
                if (centerCircle_X + dx >= width) break;

            } else if (dx + centerCircle_X < 0) {
//                Log.i("point rb180","dx  "+dx+"  break");
                centerCircle_X += width;
            }
            for (int dy = -radius; dy <= radius; ++dy) {
                if (centerCircle_Y + dy < 0) {
                    dy = -centerCircle_Y;
                    continue;
                } else if (centerCircle_Y + dy >= height) {
                    break;
                }
                if (orientationHorizontal)
                    toChangeBitmap.setPixel(dx + centerCircle_X, dy + centerCircle_Y, flavouringBitmap.getPixel(dx + centerCircle_X, dy + centerCircle_Y));
                else
                    toChangeBitmap.setPixel(dy + centerCircle_Y, dx + centerCircle_X, flavouringBitmap.getPixel(dy + centerCircle_Y, dx + centerCircle_X));
            }
        }
//        for (int dx = 0; dx <= getCenterCirlce_X() - movement; ++dx) {
//            if (dx + centerCircle_X >= toChangeBitmap.getWidth()) {
//                dx = toChangeBitmap.getWidth() - centerCircle_X;
//                continue;
//            } else if (dx + centerCircle_X < 0) {
//                break;
//            }
//            int terminalY = (int) Math.sqrt((lens_radius * lens_radius) - (dx * dx));
//            for (int dy = -terminalY; dy <= terminalY; ++dy) {
//                if (centerCircle_Y + dy < 0) {
//                    dy = -centerCircle_Y;
//                    continue;
//                } else if (centerCircle_Y + dy >= toChangeBitmap.getHeight()) {
//                    break;
//                }
//                outputBitmap.setPixel(dx + centerCircle_X, dy + centerCircle_Y, flavouringBitmap.getPixel(dx + centerCircle_X, dy + centerCircle_Y));
//            }
//        }
//        return toChangeBitmap;

        Log.i("point rb263", "dissolveBitmap ends");
    }

    public void generateBump(Bitmap toChangeBitmap, Bitmap originalBitmap) {

        int centerCircle_X = getUpdatedcenterCircle_X();
        int terminalY;

        double distance;
        double relativeRadius;
        double distortion;
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
        Log.i("point rb234", "centerCircle_X  " + centerCircle_X + "  centerCircle_Y  " + centerCircle_Y + "  movement  " + movement);
        double lens_factor = 1.0;
        for (int dx = terminalRight; dx >= 0; --dx) {
            //R.H.S
//            if (centerCircle_X + dx >= width) {
//                dx = width - centerCircle_X;
//                continue;
//            }
            if (dx + centerCircle_X >= width) {
                centerCircle_X -= width;
                if (centerCircle_X + dx >= width) break;

            } else if (dx + centerCircle_X < 0) {
//                Log.i("point rb180","dx  "+dx+"  break");
                centerCircle_X += width;
            }
            terminalY = (int) Math.sqrt((radius * radius) - (dx * dx));
            for (int dy = -terminalY; dy <= terminalY; ++dy) {
                if (centerCircle_Y + dy < 0) {
                    dy = -centerCircle_Y;
                    continue;
                } else if (centerCircle_Y + dy >= height) {
                    break;
                }
                distance = Math.sqrt((dx * dx) + (dy * dy));
                relativeRadius = distance / radius;
                distortion = Math.pow(relativeRadius, lens_factor);//radius^lens_factor  *  distance

                sx = (int) (distortion * dx + centerCircle_X);
                sy = (int) (distortion * dy + centerCircle_Y);
                if ((sx >= 0) && (sy >= 0) && (sx < width) && (sy < height)) {
//                    Log.i("point rb257", "dx  " + dx + "  dy  " + dy);
                    if (orientationHorizontal)
                        toChangeBitmap.setPixel(dx + centerCircle_X, dy + centerCircle_Y, originalBitmap.getPixel(sx, sy));
                    else
                        toChangeBitmap.setPixel(dy + centerCircle_Y, dx + centerCircle_X, originalBitmap.getPixel(sy, sx));

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
                if (centerCircle_Y + dy < 0) {
                    dy = -centerCircle_Y;
                    continue;
                } else if (centerCircle_Y + dy >= height) {
                    break;
                }
                distance = Math.sqrt((dx * dx) + (dy * dy));

                relativeRadius = distance / radius;
                distortion = Math.pow(relativeRadius, lens_factor);//radius^lens_factor  *  distance

                sx = (int) (distortion * dx + centerCircle_X);
                sy = (int) (distortion * dy + centerCircle_Y);

                if ((sx >= 0) && (sy >= 0) && (sx < width) && (sy < width)) {
//                    Log.i("point rb289", "dx  " + dx + "  dy  " + dy);

                    if (orientationHorizontal)
                        toChangeBitmap.setPixel(dx + centerCircle_X, dy + centerCircle_Y, originalBitmap.getPixel(sx, sy));
                    else
                        toChangeBitmap.setPixel(dy + centerCircle_Y, dx + centerCircle_X, originalBitmap.getPixel(sy, sx));
                }
            }
        }
        Log.i("point rb356", "generateBitmap ends");
    }


    public void dissolveAll(Bitmap toChangeBitmap, Bitmap flavouringBitmap) {
        Log.i("point rbi363", "dissolve all start");
        if (directionPositive)
            mixBitmapRectangle(toChangeBitmap, flavouringBitmap, getPreviouscenterCircle_X() - movement, getPreviouscenterCircle_X() + (numberOfTimes) * movement + radius, centerCircle_Y);
        else
            mixBitmapRectangle(toChangeBitmap, flavouringBitmap, getPreviouscenterCircle_X() - (numberOfTimes) * movement - radius, getPreviouscenterCircle_X(), centerCircle_Y);
    }

}
