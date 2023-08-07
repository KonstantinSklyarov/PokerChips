package com.example.pokerchips;

import android.content.Context;

public class MyImageButton extends androidx.appcompat.widget.AppCompatImageButton {
    public MyImageButton(Context context) {
        super(context);
    }
    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }
}
