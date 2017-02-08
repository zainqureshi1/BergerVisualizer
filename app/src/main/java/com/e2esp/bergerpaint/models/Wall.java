package com.e2esp.bergerpaint.models;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.e2esp.bergerpaint.interfaces.OnWallImageTouchListener;

/**
 * Created by Zain on 2/1/2017.
 */

public class Wall {
    private String name;
    private int imageRes;

    private ImageView wallImage;

    public Wall(String name, int imageRes) {
        this(name, imageRes, null);
    }

    public Wall(String name, int imageRes, ImageView wallImage) {
        this.name = name;
        this.imageRes = imageRes;
        this.wallImage = wallImage;
    }

    public String getName() {
        return name;
    }

    public int getImageRes() {
        return imageRes;
    }

    public ImageView getWallImage() {
        return wallImage;
    }

    public void setWallImage(final ImageView wallImage, final OnWallImageTouchListener onWallTouchListener) {
        this.wallImage = wallImage;
        this.wallImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                float eventX = event.getX();
                float eventY = event.getY();
                float[] eventXY = new float[] {eventX, eventY};

                Matrix invertMatrix = new Matrix();
                ((ImageView)view).getImageMatrix().invert(invertMatrix);

                invertMatrix.mapPoints(eventXY);
                int x = Integer.valueOf((int)eventXY[0]);
                int y = Integer.valueOf((int)eventXY[1]);

                Drawable drawable = ((ImageView)view).getDrawable();
                Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

                //Limit x, y range within bitmap
                if(x < 0) {
                    x = 0;
                } else if (x > bitmap.getWidth()-1) {
                    x = bitmap.getWidth()-1;
                }

                if (y < 0) {
                    y = 0;
                } else if (y > bitmap.getHeight()-1) {
                    y = bitmap.getHeight()-1;
                }

                int touchedRGB = bitmap.getPixel(x, y);

                /*Log.d("Touched", "touched position: "
                        + String.valueOf(eventX) + " / "
                        + String.valueOf(eventY));
                Log.d("Inverted", "touched position: "
                        + String.valueOf(x) + " / "
                        + String.valueOf(y));
                Log.d("Size", "drawable size: "
                        + String.valueOf(bitmap.getWidth()) + " / "
                        + String.valueOf(bitmap.getHeight()));
                Log.d("Color", "touched color: " + "#" + Integer.toHexString(touchedRGB));*/

                if (touchedRGB != 0) {
                    onWallTouchListener.onWallTouch(Wall.this);
                    return true;
                }

                return false;
            }
        });
    }

    public Wall clone() {
        return new Wall(getName(), getImageRes(), getWallImage());
    }

}