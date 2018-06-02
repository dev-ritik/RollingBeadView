package com.example.android.rollingbeadlibrary;

import android.graphics.Bitmap;
import android.util.Log;

public class RollingBead {

    private Bitmap changedBitmap;
    private Bitmap immutableBitmap;
    private int centerCircle_X = 0;
    private boolean generateCycle = false;
    private int movementInX = 15;
    private int radius = 35;
    private int numberOfTimes = 1;
//   private int numberOfTimes = radius / movementInX;

    public int getcenterCircle_X() {
        return centerCircle_X;
    }

    public int getUpdatedcenterCircle_X() {
//        Log.i("point rb22", "getUpdatedcenterCircle_X initial  " + centerCircle_X);
        centerCircle_X = (movementInX + centerCircle_X) % immutableBitmap.getWidth();
//        Log.i("point rb24", "getUpdatedcenterCircle_X final  " + centerCircle_X);

        return centerCircle_X;
    }

    public int getPreviouscenterCircle_X() {
//        Log.i("point rb30", "getPreviouscenterCircle_X initial  " + centerCircle_X);
        if (centerCircle_X > movementInX * numberOfTimes)
            return (centerCircle_X - movementInX * numberOfTimes);
        else
            return immutableBitmap.getWidth() + centerCircle_X - movementInX * numberOfTimes;
    }

    public void setcenterCircle_X(int centerCircle_X) {
        this.centerCircle_X = centerCircle_X;
    }

    public RollingBead() {
    }

    public RollingBead(Bitmap changedBitmap, Bitmap immutableBitmap, int centerCircle_X, int movementInX, int radius, int numberOfTimes) {
        this.changedBitmap = changedBitmap;
        this.immutableBitmap = immutableBitmap;
        this.centerCircle_X = centerCircle_X;
        this.movementInX = movementInX;
        this.radius = radius;
        this.numberOfTimes = numberOfTimes;
    }

    private Bitmap convert(Bitmap toChangeBitmap, Bitmap originalBitmap, int lens_center_x, int lens_center_y, int lens_radius) {
        Bitmap outputBitmap = toChangeBitmap;
//        Log.i("point ma34", "width  " + toChangeBitmap.getWidth() + "  height  " + toChangeBitmap.getHeight());
//        Log.i("point ma35", "lens_center_x  " + lens_center_x + "  lens_center_y  " + lens_center_y);
        double lens_factor = 1.0;
        for (int dx = lens_radius; dx >= 0; --dx) {
            //R.H.S
            if (lens_center_x + dx > toChangeBitmap.getWidth()) {
                dx = toChangeBitmap.getWidth() - lens_center_x;
                continue;
            }
            int terminalY = (int) Math.sqrt((lens_radius * lens_radius) - (dx * dx));
            for (int dy = -terminalY; dy <= terminalY; ++dy) {
                if (lens_center_y + dy < 0) {
                    dy = -lens_center_y;
                    continue;
                } else if (lens_center_y + dy >= toChangeBitmap.getHeight()) {
                    break;
                }
                double distance = Math.sqrt((dx * dx) + (dy * dy));
                double radius = distance / lens_radius;
                double distortion = Math.pow(radius, lens_factor);//radius^lens_factor  *  distance

                int sx = (int) (distortion * dx + lens_center_x);
                int sy = (int) (distortion * dy + lens_center_y);
                if ((sx >= 0) && (sy >= 0) && (sx < toChangeBitmap.getWidth()) && (sy < toChangeBitmap.getHeight())) {
//                    Log.i("point ma52", "dx  " + dx + "  dy  " + dy);

                    outputBitmap.setPixel(dx + lens_center_x, dy + lens_center_y, originalBitmap.getPixel(sx, sy));

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
                if (lens_center_y + dy < 0) {
                    dy = -lens_center_y;
                    continue;
                } else if (lens_center_y + dy >= toChangeBitmap.getHeight()) {
                    break;
                }
                double distance = Math.sqrt((dx * dx) + (dy * dy));

                double radius = distance / lens_radius;
                double distortion = Math.pow(radius, lens_factor);//radius^lens_factor  *  distance

                int sx = (int) (distortion * dx + lens_center_x);
                int sy = (int) (distortion * dy + lens_center_y);

                if ((sx >= 0) && (sy >= 0) && (sx < toChangeBitmap.getWidth()) && (sy < toChangeBitmap.getHeight())) {
//                    Log.i("point ma70", "dx  " + dx + "  dy  " + dy);
                    outputBitmap.setPixel(dx + (int) lens_center_x, dy + (int) lens_center_y, originalBitmap.getPixel(sx, sy));
                }
            }
        }
        return outputBitmap;
    }

    private Bitmap mixBitmap(Bitmap toChangeBitmap, Bitmap flavouringBitmap, int lens_center_x, int lens_center_y, int lens_radius) {
        Bitmap outputBitmap = toChangeBitmap;
        for (int dx = lens_radius; dx >= -lens_radius; --dx) {
            if (dx + lens_center_x >= toChangeBitmap.getWidth()) {
                dx = toChangeBitmap.getWidth() - lens_center_x;
                continue;
            } else if (dx + lens_center_x < 0) {
                break;
            }
            int terminalY = (int) Math.sqrt((lens_radius * lens_radius) - (dx * dx));
            for (int dy = -terminalY; dy <= terminalY; ++dy) {
                if (lens_center_y + dy < 0) {
                    dy = -lens_center_y;
                    continue;
                } else if (lens_center_y + dy >= toChangeBitmap.getHeight()) {
                    break;
                }
                outputBitmap.setPixel(dx + lens_center_x, dy + lens_center_y, flavouringBitmap.getPixel(dx + lens_center_x, dy + lens_center_y));
            }
        }
        return outputBitmap;
    }

    public Bitmap dissolveBitmap(Bitmap toChangeBitmap, Bitmap flavouringBitmap, int lens_center_x, int lens_center_y, int lens_radius, int movementInX) {
        Bitmap outputBitmap = toChangeBitmap;
//        Log.i("point ma239", "lens_center_x  " + lens_center_x + "  lens_center_y  " + lens_center_y + "  movementInX  " + movementInX);
//        Log.i("point ma156", "icon.getWidth(  " + icon.getWidth() + "  getCenterCirlce_X()  " + getCenterCirlce_X());
        for (int dx = -movementInX; dx <= 0; ++dx) {
            if (dx == 0 && lens_center_x == immutableBitmap.getWidth()) {
                break;
            } else if (dx + lens_center_x < 0) {
                break;
            }
            int terminalY = (int) Math.sqrt((lens_radius * lens_radius) - (dx * dx));
            for (int dy = -terminalY; dy <= terminalY; ++dy) {
                if (lens_center_y + dy < 0) {
                    dy = -lens_center_y;
                    continue;
                } else if (lens_center_y + dy >= toChangeBitmap.getHeight()) {
                    break;
                }
                outputBitmap.setPixel(dx + lens_center_x, dy + lens_center_y, flavouringBitmap.getPixel(dx + lens_center_x, dy + lens_center_y));
            }
        }
//        for (int dx = 0; dx <= getCenterCirlce_X() - movementInX; ++dx) {
//            if (dx + lens_center_x >= toChangeBitmap.getWidth()) {
//                dx = toChangeBitmap.getWidth() - lens_center_x;
//                continue;
//            } else if (dx + lens_center_x < 0) {
//                break;
//            }
//            int terminalY = (int) Math.sqrt((lens_radius * lens_radius) - (dx * dx));
//            for (int dy = -terminalY; dy <= terminalY; ++dy) {
//                if (lens_center_y + dy < 0) {
//                    dy = -lens_center_y;
//                    continue;
//                } else if (lens_center_y + dy >= toChangeBitmap.getHeight()) {
//                    break;
//                }
//                outputBitmap.setPixel(dx + lens_center_x, dy + lens_center_y, flavouringBitmap.getPixel(dx + lens_center_x, dy + lens_center_y));
//            }
//        }
        return outputBitmap;
    }

    public Bitmap generateBump(Bitmap toChangeBitmap, Bitmap originalBitmap, int lens_center_x, int lens_center_y, int lens_radius, int movementInX) {
        Bitmap outputBitmap = toChangeBitmap;
//        Log.i("point ma263", "lens_center_x  " + lens_center_x + "  lens_center_y  " + lens_center_y + "  movementInX  " + movementInX);
        double lens_factor = 1.0;
        for (int dx = lens_radius; dx >= 0; --dx) {
            //R.H.S
            if (lens_center_x + dx > toChangeBitmap.getWidth()) {
                dx = toChangeBitmap.getWidth() - lens_center_x;
                continue;
            }
            int terminalY = (int) Math.sqrt((lens_radius * lens_radius) - (dx * dx));
            for (int dy = -terminalY; dy <= terminalY; ++dy) {
                if (lens_center_y + dy < 0) {
                    dy = -lens_center_y;
                    continue;
                } else if (lens_center_y + dy >= toChangeBitmap.getHeight()) {
                    break;
                }
                double distance = Math.sqrt((dx * dx) + (dy * dy));
                double radius = distance / lens_radius;
                double distortion = Math.pow(radius, lens_factor);//radius^lens_factor  *  distance

                int sx = (int) (distortion * dx + lens_center_x);
                int sy = (int) (distortion * dy + lens_center_y);
                if ((sx >= 0) && (sy >= 0) && (sx < toChangeBitmap.getWidth()) && (sy < toChangeBitmap.getHeight())) {
//                    Log.i("point ma52", "dx  " + dx + "  dy  " + dy);

                    outputBitmap.setPixel(dx + lens_center_x, dy + lens_center_y, originalBitmap.getPixel(sx, sy));

                }
            }
        }
        for (int dx = -movementInX; dx <= 0; ++dx) {
            //L.H.S
            if (lens_center_x + dx < 0) {
                dx = -lens_center_x;
                continue;
            }
            int terminalY = (int) Math.sqrt((lens_radius * lens_radius) - (dx * dx));
            for (int dy = -terminalY; dy <= terminalY; ++dy) {
                if (lens_center_y + dy < 0) {
                    dy = -lens_center_y;
                    continue;
                } else if (lens_center_y + dy >= toChangeBitmap.getHeight()) {
                    break;
                }
                double distance = Math.sqrt((dx * dx) + (dy * dy));

                double radius = distance / lens_radius;
                double distortion = Math.pow(radius, lens_factor);//radius^lens_factor  *  distance

                int sx = (int) (distortion * dx + lens_center_x);
                int sy = (int) (distortion * dy + lens_center_y);

                if ((sx >= 0) && (sy >= 0) && (sx < toChangeBitmap.getWidth()) && (sy < toChangeBitmap.getHeight())) {
//                    Log.i("point ma70", "dx  " + dx + "  dy  " + dy);
                    outputBitmap.setPixel(dx + lens_center_x, dy + lens_center_y, originalBitmap.getPixel(sx, sy));
                }
            }
        }
        return outputBitmap;
    }

}
