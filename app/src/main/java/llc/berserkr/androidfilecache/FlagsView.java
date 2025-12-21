package llc.berserkr.androidfilecache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

public class FlagsView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    //#################### SurfaceView mechanics ####################
    private final SurfaceHolder holder;
    private boolean running = false;

    //######################
    private int w;
    private int h;

    /**
     * these constructors are defined to use this view in the xml file
     * @param context
     */
    public FlagsView(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);

        init(context);

    }

    /**
     * these constructors are defined to use this view in the xml file
     *
     * @param context
     * @param attrs
     */
    public FlagsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        holder = getHolder();
        holder.addCallback(this);

        init(context);
    }

    /**
     * these constructors are defined to use this view in the xml file
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public FlagsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        holder = getHolder();
        holder.addCallback(this);

        init(context);
    }

    /**
     * these constructors are defined to use this view in the xml file
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    public FlagsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        holder = getHolder();
        holder.addCallback(this);

        init(context);

    }

    private void init(Context context) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        running = true;
        Thread renderThread = new Thread(this);
        renderThread.start();

        takeMeasurements(holder.getSurfaceFrame().width(), holder.getSurfaceFrame().height());

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        // Handle surface changes (e.g., orientation changes)
        takeMeasurements(width, height);

    }
    private void takeMeasurements(int width, int height) {

        this.w = width;
        this.h = height;

        //1280x896

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        running = false;
    }

    @Override
    public void run() {
        while (running) {

            if (!holder.getSurface().isValid()) {
                continue;
            }

            final Canvas canvas = holder.lockCanvas();
            if (canvas != null) {

                // Clear the canvas (optional, but often necessary for full redraw)
                canvas.drawColor(Color.BLACK); // Or any background color

                // Perform drawing operations on the canvas
                holder.unlockCanvasAndPost(canvas);

            }
        }
    }


    /**
     * This returns the pixels for the dp value
     *
     * @param dp
     * @param context
     * @return
     */
    public static int dpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    /**
     * This method converts a vector drawable to a bitmap.
     *
     * @param context
     * @param drawableId
     * @return
     */
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

}
