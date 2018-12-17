package ch.epfl.sweng.runpharaa.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

public class RoundedCornersImageView extends android.support.v7.widget.AppCompatImageView {

    private static final float radius = 24.0f;
    private Path path;

    public RoundedCornersImageView(Context context) {
        super(context);
        init();
    }

    public RoundedCornersImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundedCornersImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        path = new Path();

    }

    private RectF getRect() {
        return new RectF(0, 0, this.getWidth(), this.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        RectF rect = getRect();
        path.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}
