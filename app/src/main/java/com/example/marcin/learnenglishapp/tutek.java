package com.example.marcin.learnenglishapp;

import android.graphics.Canvas;
import android.graphics.RectF;

public class tutek {


    boolean isNotLineless; // Set this in constructor or a setter
    boolean isNotThumbless; // Set this in constructor or a setter

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isNotLineless) {
            // draw seek bar background line
            final RectF rect = new RectF(padding, 0.5f * (getHeight() - lineHeight), getWidth() - padding, 0.5f * (getHeight() + lineHeight));
            paint.setStyle(Style.FILL);
            paint.setColor(Color.GRAY);
            paint.setAntiAlias(true);
            canvas.drawRect(rect, paint);

            // draw seek bar active range line
            rect.left = normalizedToScreen(normalizedMinValue);
            rect.right = normalizedToScreen(normalizedMaxValue);

            // orange color
            paint.setColor(DEFAULT_COLOR);
            canvas.drawRect(rect, paint);
        }

        if (isNotThumbless) {
            // draw minimum thumb
            drawThumb(normalizedToScreen(normalizedMinValue), Thumb.MIN.equals(pressedThumb), canvas);

            // draw maximum thumb
            drawThumb(normalizedToScreen(normalizedMaxValue), Thumb.MAX.equals(pressedThumb), canvas);
        }
    }
}
