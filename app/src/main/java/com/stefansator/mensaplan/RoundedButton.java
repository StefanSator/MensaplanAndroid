package com.stefansator.mensaplan;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;

/**
 * Class which defines a Custom Button.
 */
public class RoundedButton extends androidx.appcompat.widget.AppCompatButton {
    /** The radius of the corners of the button. */
    private double cornerRadius = 17.5;
    /** The background color of the button. */
    private int customBackgroundColor = Color.RED;
    /** The text color of the button. */
    private int customTextColor = Color.WHITE;
    /** The shape of the button. */
    private GradientDrawable shape;

    /**
     * Simple constructor to use when creating a button from code.
     * @param context Current application context.
     */
    public RoundedButton(Context context) {
        super(context);
        sharedInit();
    }

    /**
     * LayoutInflater calls this constructor when inflating a Button from XML.
     * @param context Current application context.
     * @param attr The AttributeSet of the Button.
     */
    public RoundedButton(Context context, AttributeSet attr) {
        super(context, attr);
        sharedInit();
    }

    /**
     * Shared Initializer which is called by both constructors for shared initialization of
     * the Custom Button.
     */
    private void sharedInit() {
        shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        updateCorners(cornerRadius);
        updateColor(customBackgroundColor);
        updateTextColor(customTextColor);
    }

    /**
     * Update Text Color of the button.
     * @param color New text color.
     */
    private void updateTextColor(int color) {
        this.setTextColor(color);
    }

    /**
     * Update Background Color of the button.
     * @param color New background color.
     */
    private void updateColor(int color) {
        shape.setColor(customBackgroundColor);
        this.setBackground(shape);
    }

    /**
     * Update corner radius of the button.
     * @param cornerRadius New corner radius.
     */
    private void updateCorners(double cornerRadius) {
        shape.setCornerRadius(200);
    }
}
