package com.github.aakira.compoundicontextview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.Dimension;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.text.TextUtilsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import java.util.Locale;

/**
 * You can use a vector drawable in TextView instead of drawableLeft, drawableTop, drawableRight and
 * drawableBottom.
 * <p>
 * You must set this line in your gradle.
 * <p>
 * ```
 * default config { "vectorDrawables.useSupportLibrary = true"  }
 * ```
 */
public class CompoundIconTextView extends AppCompatTextView {

    public static final int UNDEFINED_RESOURCE = -0x001;

    private static final int INDEX_LEFT = 0;
    private static final int INDEX_TOP = 1;
    private static final int INDEX_RIGHT = 2;
    private static final int INDEX_BOTTOM = 3;

    /**
     * @see android.support.v7.widget.DrawableUtils#VECTOR_DRAWABLE_CLAZZ_NAME
     */
    private static final String VECTOR_DRAWABLE_CLAZZ_NAME = "android.graphics.drawable.VectorDrawable";

    /**
     * @see android.support.v7.widget.ThemeUtils#CHECKED_STATE_SET
     */
    private static final int[] CHECKED_STATE_SET = new int[]{android.R.attr.state_checked};

    /**
     * @see android.support.v7.widget.ThemeUtils#EMPTY_STATE_SET
     */
    private static final int[] EMPTY_STATE_SET = new int[0];

    private int iconWidth = UNDEFINED_RESOURCE;
    private int iconHeight = UNDEFINED_RESOURCE;
    private int iconColor = UNDEFINED_RESOURCE;
    private Drawable[] drawables = new Drawable[4];
    private int[] drawableResIds = new int[4]; // cache drawable resource ids

    public CompoundIconTextView(Context context) {
        this(context, null);
    }

    public CompoundIconTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CompoundIconTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = null;
        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.CompoundIconTextView, defStyleAttr, 0);
            drawableResIds[INDEX_LEFT] = a.getResourceId(R.styleable.CompoundIconTextView_cit_drawableLeft, UNDEFINED_RESOURCE);
            drawableResIds[INDEX_TOP] = a.getResourceId(R.styleable.CompoundIconTextView_cit_drawableTop, UNDEFINED_RESOURCE);
            drawableResIds[INDEX_RIGHT] = a.getResourceId(R.styleable.CompoundIconTextView_cit_drawableRight, UNDEFINED_RESOURCE);
            drawableResIds[INDEX_BOTTOM] = a.getResourceId(R.styleable.CompoundIconTextView_cit_drawableBottom, UNDEFINED_RESOURCE);
            drawableResIds[isRtl() ? INDEX_RIGHT : INDEX_LEFT] = a.getResourceId(R.styleable.CompoundIconTextView_cit_drawableStart, UNDEFINED_RESOURCE);
            drawableResIds[isRtl() ? INDEX_LEFT : INDEX_RIGHT] = a.getResourceId(R.styleable.CompoundIconTextView_cit_drawableEnd, UNDEFINED_RESOURCE);
            iconWidth = a.getDimensionPixelSize(R.styleable.CompoundIconTextView_cit_iconWidth, UNDEFINED_RESOURCE);
            iconHeight = a.getDimensionPixelSize(R.styleable.CompoundIconTextView_cit_iconHeight, UNDEFINED_RESOURCE);
            iconColor = a.getColor(R.styleable.CompoundIconTextView_cit_iconColor, UNDEFINED_RESOURCE);
        } finally {
            if (a != null) {
                a.recycle();
            }
        }

        if (drawableResIds[INDEX_LEFT] == UNDEFINED_RESOURCE && drawableResIds[INDEX_TOP] == UNDEFINED_RESOURCE
                && drawableResIds[INDEX_RIGHT] == UNDEFINED_RESOURCE && drawableResIds[INDEX_BOTTOM] == UNDEFINED_RESOURCE) {
            return;
        }

        checkHasIconSize();
        makeDrawableIcons();
        updateIcons();
    }

    /**
     * @param resourceId Set the {@link CompoundIconTextView#UNDEFINED_RESOURCE} if you want clear icon.
     */
    public void setVectorDrawableLeft(@DrawableRes final int resourceId) {
        setVectorDrawable(INDEX_LEFT, resourceId);
    }

    /**
     * @param resourceId Set the {@link CompoundIconTextView#UNDEFINED_RESOURCE} if you want clear icon.
     */
    public void setVectorDrawableTop(@DrawableRes final int resourceId) {
        setVectorDrawable(INDEX_TOP, resourceId);
    }

    /**
     * @param resourceId Set the {@link CompoundIconTextView#UNDEFINED_RESOURCE} if you want clear icon.
     */
    public void setVectorDrawableRight(@DrawableRes final int resourceId) {
        setVectorDrawable(INDEX_RIGHT, resourceId);
    }

    /**
     * @param resourceId Set the {@link CompoundIconTextView#UNDEFINED_RESOURCE} if you want clear icon.
     */
    public void setVectorDrawableBottom(@DrawableRes final int resourceId) {
        setVectorDrawable(INDEX_BOTTOM, resourceId);
    }

    /**
     * @param resourceId Set the {@link CompoundIconTextView#UNDEFINED_RESOURCE} if you want clear icon.
     */
    public void setVectorDrawableStart(@DrawableRes final int resourceId) {
        setVectorDrawable(isRtl() ? INDEX_RIGHT : INDEX_LEFT, resourceId);
    }

    /**
     * @param resourceId Set the {@link CompoundIconTextView#UNDEFINED_RESOURCE} if you want clear icon.
     */
    public void setVectorDrawableEnd(@DrawableRes final int resourceId) {
        setVectorDrawable(isRtl() ? INDEX_LEFT : INDEX_RIGHT, resourceId);
    }

    /**
     * Change drawable icon color
     *
     * @param resId Set color resource id
     */
    public void setIconColorResource(@ColorRes final int resId) {
        setIconColor(ContextCompat.getColor(getContext(), resId));
    }

    /**
     * Change drawable icon color
     *
     * @param color Set color integer
     */
    public void setIconColor(@ColorInt final int color) {
        for (int i = 0; i < drawables.length; i++) {
            setColorFilter(i, color);
        }
        iconColor = color;
        updateIcons();
    }

    /**
     * Change drawable icon size
     *
     * @param widthRes  Set width resource id
     * @param heightRes Set height resource id
     */
    public void setIconSizeResource(@DimenRes final int widthRes, @DimenRes final int heightRes) {
        setIconSize(getContext().getResources().getDimensionPixelSize(widthRes),
                getContext().getResources().getDimensionPixelSize(heightRes));
    }

    /**
     * Change drawable icon size
     *
     * @param width  Set width size
     * @param height Set height size
     */
    public void setIconSize(@Dimension final int width, @Dimension final int height) {
        iconWidth = width;
        iconHeight = height;
        makeDrawableIcons();
        updateIcons();
    }

    private void checkHasIconSize() {
        if (iconWidth == UNDEFINED_RESOURCE || iconHeight == UNDEFINED_RESOURCE) {
            throw new IllegalStateException("You must set the 'iconSize'");
        }
    }

    private void makeDrawableIcons() {
        for (int i = 0; i < drawables.length; i++) {
            if (drawableResIds[i] != UNDEFINED_RESOURCE) {
                setDrawable(i, drawableResIds[i]);
            }
        }
    }

    private void setColorFilter(final int index, @ColorInt final int color) {
        if (drawables[index] != null) {
            drawables[index].setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }

    private void setVectorDrawable(final int index, @DrawableRes final int resourceId) {
        if (resourceId == UNDEFINED_RESOURCE) {
            drawables[index] = null;
            drawableResIds[index] = UNDEFINED_RESOURCE;
            updateIcons();
        } else {
            checkHasIconSize();
            setDrawable(index, resourceId);
            drawableResIds[index] = resourceId;
            updateIcons();
        }
    }

    private void updateIcons() {
        setCompoundDrawablesWithIntrinsicBounds(drawables[INDEX_LEFT], drawables[INDEX_TOP],
                drawables[INDEX_RIGHT], drawables[INDEX_BOTTOM]);
    }

    private void setDrawable(final int index, @DrawableRes final int resourceId) {
        drawables[index] = resource2VectorDrawable(resourceId, iconColor, iconWidth, iconHeight);
    }

    /**
     * @param resourceId drawable resourceId
     * @param iconColor  color integer
     * @param iconWidth  pixel size
     * @param iconHeight pixel size
     * @return Resized bitmap
     */
    private Drawable resource2VectorDrawable(@DrawableRes final int resourceId, @ColorInt final int iconColor,
                                             final int iconWidth, final int iconHeight) {
        final Context context = getContext();
        final Drawable drawable = AppCompatResources.getDrawable(context, resourceId);

        if (drawable == null) {
            throw new Resources.NotFoundException("Resource not found : %s." + resourceId);
        }

        // See if we need to 'fix' the drawableLeft
        fixDrawable(drawable);
        // Set color
        DrawableCompat.setTint(drawable, iconColor);
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
        // Resize Bitmap
        return new BitmapDrawable(context.getResources(),
                Bitmap.createScaledBitmap(drawable2Bitmap(drawable, iconWidth, iconHeight), iconWidth, iconHeight, true));
    }

    /**
     * Convert to bitmap from drawable
     */
    private static Bitmap drawable2Bitmap(final Drawable drawable, final int iconWidth, final int iconHeight) {
        final Bitmap bitmap = Bitmap.createBitmap(iconWidth, iconHeight, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * @see android.support.v7.widget.DrawableUtils#fixDrawable(Drawable)
     * <p>
     * Attempt the fix any issues in the given drawable, usually caused by platform bugs in the
     * implementation. This method should be call after retrieval from
     * {@link android.content.res.Resources} or a {@link android.content.res.TypedArray}.
     */
    private static void fixDrawable(@NonNull final Drawable drawable) {
        if (Build.VERSION.SDK_INT == 21
                && VECTOR_DRAWABLE_CLAZZ_NAME.equals(drawable.getClass().getName())) {
            fixVectorDrawableTinting(drawable);
        }
    }

    /**
     * @see android.support.v7.widget.DrawableUtils#fixVectorDrawableTinting(Drawable)
     * <p>
     * VectorDrawable has an issue on API 21 where it sometimes doesn't create its tint filter.
     * Fixed by toggling it's state to force a filter creation.
     */
    private static void fixVectorDrawableTinting(final Drawable drawable) {
        final int[] originalState = drawable.getState();
        if (originalState.length == 0) {
            // The drawable doesn't have a state, so set it to be checked
            drawable.setState(CHECKED_STATE_SET);
        } else {
            // Else the drawable does have a state, so clear it
            drawable.setState(EMPTY_STATE_SET);
        }
        // Now set the original state
        drawable.setState(originalState);
    }

    private boolean isRtl() {
        Resources resources = getContext().getResources();
        Locale locale = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                ? resources.getConfiguration().getLocales().getFirstMatch(resources.getAssets().getLocales())
                : resources.getConfiguration().locale;
        return TextUtilsCompat.getLayoutDirectionFromLocale(locale) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }
}