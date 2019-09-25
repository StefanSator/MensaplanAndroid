package com.stefansator.mensaplan;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;

public class RoundedButton extends android.support.v7.widget.AppCompatButton {
    private double cornerRadius = 17.5;
    private int customBackgroundColor = Color.RED;
    private int customTextColor = Color.WHITE;
    private GradientDrawable shape;

    // Simple constructor to use when creating a button from code
    public RoundedButton(Context context) {
        super(context);
        sharedInit();
    }

    // LayoutInflater calls this constructor when inflating a Button from XML
    public RoundedButton(Context context, AttributeSet attr) {
        super(context, attr);
        sharedInit();
    }

    private void sharedInit() {
        shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        updateCorners(cornerRadius);
        updateColor(customBackgroundColor);
        updateTextColor(customTextColor);
    }

    private void updateTextColor(int color) {
        this.setTextColor(color);
    }

    private void updateColor(int color) {
        shape.setColor(customBackgroundColor);
        this.setBackground(shape);
    }

    private void updateCorners(double cornerRadius) {
        shape.setCornerRadius(200);
    }
}
