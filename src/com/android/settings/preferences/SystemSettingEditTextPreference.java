/*
 * Copyright (C) 2017 AICP
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
import androidx.preference.EditTextPreference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.provider.Settings;

import com.android.settings.R;

public class SystemSettingEditTextPreference extends EditTextPreference {
    private boolean mAutoSummary = false;

    public SystemSettingEditTextPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setPreferenceDataStore(new SystemSettingsStore(context.getContentResolver()));
        init(context, attrs);
    }

    public SystemSettingEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPreferenceDataStore(new SystemSettingsStore(context.getContentResolver()));
        init(context, attrs);
    }

    public SystemSettingEditTextPreference(Context context) {
        super(context);
        setPreferenceDataStore(new SystemSettingsStore(context.getContentResolver()));
    }

    private boolean isPersisted() {
        return Settings.System.getString(getContext().getContentResolver(), getKey()) != null;
    }

    private String getString(String key, String defaultValue) {
        String result = Settings.System.getString(getContext().getContentResolver(), key);
        return result == null ? defaultValue : result;
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
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        final String value;
        if (!restorePersistedValue || !isPersisted()) {
            if (defaultValue == null) {
                return;
            }
            value = (String) defaultValue;
            if (shouldPersist()) {
                persistString(value);
            }
        } else {
            // Note: the default is not used because to have got here
            // isPersisted() must be true.
            value = getString(getKey(), null /* not used */);
        }
        setText(value);
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        if (mAutoSummary || TextUtils.isEmpty(getSummary())) {
            setSummary(text, true);
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
}

