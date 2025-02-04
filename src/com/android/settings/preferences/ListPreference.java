/*
 * Copyright (C) 2017-2018 AICP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
import android.text.TextUtils;
import android.util.AttributeSet;

import com.android.settings.R;

public class ListPreference extends androidx.preference.ListPreference {
    private boolean mAutoSummary = false;

    public ListPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public ListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ListPreference(Context context) {
        super(context);
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
        if (mAutoSummary || TextUtils.isEmpty(getSummary())) {
            setSummary(getEntry(), true);
        }
    }

    @Override
    public void setSummary(CharSequence summary) {
        setSummary(summary, false);
    }

    private void setSummary(CharSequence summary, boolean autoSummary) {
        mAutoSummary = autoSummary;
        super.setSummary(summary);
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
}
