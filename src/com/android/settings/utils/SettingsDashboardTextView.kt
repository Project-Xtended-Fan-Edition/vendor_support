/*
 * Copyright (C) 2023-2024 The risingOS Android Project
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
package com.android.settings.utils

import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.os.UserHandle
import android.os.UserManager
import android.provider.Settings
import android.util.AttributeSet
import android.widget.TextView
import java.util.Calendar
import java.util.Random

import com.android.settings.R
import com.android.settings.Utils

class SettingsDashboardTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    private val handler = Handler(Looper.getMainLooper())
    private var updateInterval = 15000L
    private var homepageTitle: TextView? = null

    private val updateTextRunnable = object : Runnable {
        override fun run() {
            updateMessageBasedOnTime()
            handler.postDelayed(this, updateInterval)
        }
    }

    private val settingsObserver = object : ContentObserver(handler) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            updateMessageBasedOnTime()
        }
    }

    fun setAssociatedViews(homepageTitle: TextView) {
        this.homepageTitle = homepageTitle
        updateMessageBasedOnTime()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        context.contentResolver.registerContentObserver(
            Settings.System.getUriFor("dashboard_greetings"),
            false,
            settingsObserver
        )
        startUpdatingText()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        context.contentResolver.unregisterContentObserver(settingsObserver)
        stopUpdatingText()
    }

    private fun startUpdatingText() {
        handler.post(updateTextRunnable)
    }

    private fun stopUpdatingText() {
        handler.removeCallbacks(updateTextRunnable)
    }

    private fun getUserName(): String {
        val userManager = context.getSystemService(Context.USER_SERVICE) as UserManager
        val userInfo = Utils.getExistingUser(userManager, UserHandle.of(UserHandle.myUserId()))
        val fullName = userInfo.name ?: context.getString(R.string.default_user)
        val nameParts = fullName.split("\\s+".toRegex())
        return if (nameParts.isNotEmpty()) {
            nameParts[0]
        } else {
            context.getString(R.string.default_user)
        }
    }

    private fun getMessagesBasedOnTime(): Array<String> {
        return when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 5..11 -> context.resources.getStringArray(R.array.dashboard_morning)
            in 12..17 -> context.resources.getStringArray(R.array.dashboard_daytime)
            in 18..21 -> context.resources.getStringArray(R.array.dashboard_evening)
            else -> context.resources.getStringArray(R.array.dashboard_night)
        }
    }

    private fun getGreetingBasedOnTime(): String {
        val greetings = context.resources.getStringArray(R.array.dashboard_greetings)
        
        if (Random().nextFloat() < 0.4) {
            return greetings[4] // Namaste
        }

        return when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 5..11 -> greetings[0]  // Good morning
            in 12..17 -> greetings[1] // Hello
            in 18..21 -> greetings[2] // Good evening
            else -> greetings[3]      // Good night
        }
    }

    private fun updateMessageBasedOnTime() {
        val dashboardGreetings = Settings.System.getInt(context.contentResolver, "dashboard_greetings", 0)
        if (dashboardGreetings != 1) {
            // Use DefaultHomepageTitleText when greetings are disabled
            text = context.getString(R.string.dashboard_title)
            homepageTitle?.text = ""
            setTextAppearance(R.style.DefaultHomepageTitleText)
            return
        }

        val username = getUserName()
        val greeting = "${getGreetingBasedOnTime()} $username,"
        
        // Set text appearance for greeting line
        setTextAppearance(R.style.HomepageTitleText)
        text = greeting

        // Set the random message with its style
        val messages = getMessagesBasedOnTime()
        val randomMessage = messages.random()
        
        // Update homepageTitle with the random message and its style
        homepageTitle?.let {
            it.setTextAppearance(R.style.HomepageSubTitleText)
            it.text = randomMessage
        }
    }
}
