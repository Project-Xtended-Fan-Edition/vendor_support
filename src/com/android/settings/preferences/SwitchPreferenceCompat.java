/*
 * Copyright (C) 2020 Havoc-OS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.preferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.View;

import com.android.settings.R;

public class SwitchPreferenceCompat extends androidx.preference.SwitchPreferenceCompat {

    private final Vibrator mVibrator;

    public SwitchPreferenceCompat(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public SwitchPreferenceCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
        init(context, attrs);
    }

    public SwitchPreferenceCompat(Context context, AttributeSet attrs) {
        this(context, attrs, androidx.preference.R.attr.switchPreferenceCompatStyle);
        init(context, attrs);
    }

    public SwitchPreferenceCompat(Context context) {
        this(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray b = context.obtainStyledAttributes(attrs, R.styleable.PreferenceLayout);
        int position = b.getInt(R.styleable.PreferenceLayout_position, 4);
        b.recycle();

        switch (position) {
            case 0: // Top
                setLayoutResource(R.layout.everest_preference_top);
                break;
            case 1: // Middle
                setLayoutResource(R.layout.everest_preference_middle);
                break;
            case 2: // Bottom
                setLayoutResource(R.layout.everest_preference_bottom);
                break;
            case 3: // Full
                setLayoutResource(R.layout.everest_preference);
                break;
            case 4: // None
                return;
        }
    }

    @Override
    protected void performClick(View view) {
        super.performClick(view);
        mVibrator.vibrate(VibrationEffect.get(VibrationEffect.EFFECT_CLICK));
    }
}
