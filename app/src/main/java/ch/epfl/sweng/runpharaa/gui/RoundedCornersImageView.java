package ch.epfl.sweng.runpharaa.gui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import ch.epfl.sweng.runpharaa.R;

public class RoundedCornersImageView extends android.support.v7.widget.AppCompatImageView {

    private float radius;
    private Path path;

    public RoundedCornersImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RoundedCornersImageView);
        radius = a.getFloat(0, 24.f);
        a.recycle();
        init();
    }

    /**
     * Init the ImageView Path
     */
    private void init() {
        path = new Path();
    }

    /**
     * Get a RectF with the the RoundedCornersImageView width and height
     *
     * @return
     */
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
