package com.example.mathchallenge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

public class CurvedTextView extends AppCompatTextView {

    private Path textPath;
    private Paint textPaint;
    private RectF arcBounds;

    public CurvedTextView(Context context) {
        super(context);
        init();
    }

    public CurvedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CurvedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        textPath = new Path();
        arcBounds = new RectF();

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(getCurrentTextColor());
        textPaint.setTextSize(getTextSize());
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER); // Ensure center alignment
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        // Define the arc bounds (adjust the values to control the curvature)
        arcBounds.set(0, height / 2f, width, height *0.79f);

        // Create an arc path
        textPath.reset();
        textPath.addArc(arcBounds, -160, 140);

        // Draw the text along the path
        canvas.drawTextOnPath(getText().toString(), textPath, 0, 0, textPaint);
    }
}
