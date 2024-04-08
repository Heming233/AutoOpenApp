/*
 * Copyright (C) 2012 Yuriy Kulikov yuriy.kulikov.87@gmail.com
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
package com.better.alarm.receivers

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.accessibility.AccessibilityManager
import androidx.core.content.ContextCompat.startActivity
import com.better.alarm.bootstrap.globalInject
import com.better.alarm.bootstrap.globalLogger
import com.better.alarm.data.AlarmsRepository
import com.better.alarm.data.CalendarType
import com.better.alarm.domain.Alarms
import com.better.alarm.domain.AlarmsScheduler
import com.better.alarm.logger.Logger
import com.better.alarm.services.MyAccessibilityService
import java.lang.Thread.sleep

class AlarmsReceiver : BroadcastReceiver() {
  private val alarms: Alarms by globalInject()
  private val repository: AlarmsRepository by globalInject()
  private val log: Logger by globalLogger("AlarmsReceiver")

  override fun onReceive(context: Context, intent: Intent) {
    Log.d("IntentContent","$intent")
    when (intent.action) {
      AlarmsScheduler.ACTION_FIRED -> {

          /**
           * 初始化闹钟界面
           */
        val id = intent.getIntExtra(AlarmsScheduler.EXTRA_ID, -1)
        val calendarType =
            intent.extras?.getString(AlarmsScheduler.EXTRA_TYPE)?.let { CalendarType.valueOf(it) }
        log.debug { "Fired $id $calendarType" }

          /**
           * 弹出闹钟
           */
//        alarms.getAlarm(id)?.let { alarms.onAlarmFired(it) }

          /**
           * 关闭本次闹钟
           */
          alarms.getAlarm(id)?.dismiss()
//          Log.d("ClockStatus","I got it")


        wakeUpScreen(context)
        val intentUnlock = Intent("com.better.alarm.UNLOCK_BROADCAST")
        context.sendBroadcast(intentUnlock)
//        val isEnable = isAccessibilityServiceEnabled(context,MyAccessibilityService::class.java.name)
//        Log.d("MyAccessibility","$isEnable")
      }
      AlarmsScheduler.ACTION_INEXACT_FIRED -> {
        val id = intent.getIntExtra(AlarmsScheduler.EXTRA_ID, -1)
        log.debug { "Fired  ACTION_INEXACT_FIRED $id" }
        alarms.getAlarm(id)?.onInexactAlarmFired()
      }
      Intent.ACTION_BOOT_COMPLETED,
      AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED,
      Intent.ACTION_TIMEZONE_CHANGED,
      Intent.ACTION_LOCALE_CHANGED,
      Intent.ACTION_MY_PACKAGE_REPLACED -> {
        log.debug { "Refreshing alarms because of ${intent.action}" }
        alarms.refresh()
      }
        /**
         * 闹铃响后等待的操作
         */
      Intent.ACTION_TIME_CHANGED -> alarms.onTimeSet()
      PresentationToModelIntents.ACTION_REQUEST_SNOOZE -> {
        val id = intent.getIntExtra(AlarmsScheduler.EXTRA_ID, -1)
        log.debug { "Snooze $id" }
        alarms.getAlarm(id)?.snooze()
      }
      PresentationToModelIntents.ACTION_REQUEST_DISMISS -> {
        val id = intent.getIntExtra(AlarmsScheduler.EXTRA_ID, -1)
        log.debug { "Dismiss $id" }
        alarms.getAlarm(id)?.dismiss()
      }
      PresentationToModelIntents.ACTION_REQUEST_SKIP -> {
        val id = intent.getIntExtra(AlarmsScheduler.EXTRA_ID, -1)
        log.debug { "RequestSkip $id" }
        alarms.getAlarm(id)?.requestSkip()
      }
    }
    repository.awaitStored()
    intent.getStringExtra("CB")?.let { cbAction -> context.sendBroadcast(Intent(cbAction)) }
  }
    /**
     * 点亮屏幕
     */
    private fun wakeUpScreen(context: Context){
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or
            PowerManager.ACQUIRE_CAUSES_WAKEUP,            "AutoUnlockScreen:WakeLockTag")
        wakeLock.acquire(30*1000L)
        Log.d("wakeUpScreen","已点亮屏幕")
        wakeLock.release()
    }

    // 一个简单的函数来检查是否启用了无障碍服务
    private fun isAccessibilityServiceEnabled(context: Context, accessibilityService: String): Boolean {
        val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        return enabledServices?.contains(accessibilityService) == true
    }
}
