package com.better.alarm.services

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.text.TextUtils
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi
import com.better.alarm.domain.AlarmsScheduler
import kotlinx.coroutines.Runnable
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.thread


class MyAccessibilityService: AccessibilityService() {
    private val SWIPE_ACTION= "com.better.alarm.UNLOCK_BROADCAST"
    private var log = LogOutput()

    /**
     *启动无障碍服务，注册监听器
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d("Connection","Service connected")
        val filter = IntentFilter(SWIPE_ACTION)
        registerReceiver(swipeReceiver,filter, RECEIVER_NOT_EXPORTED)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

    }

    override fun onInterrupt() {

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(swipeReceiver)
    }

    /**
     * 监控签到结果
     */
    private fun monitorSignInResult(){
        val checkInterval = 500L
        val timeout = 5000L
        val startTime = System.currentTimeMillis()

        val runnable = object : Runnable{
            override fun run(){
                val elapsed = System.currentTimeMillis() - startTime
                if(elapsed > timeout){
                    Log.w(TAG, "签到超时，准备重试")
//                    handleSignInResult(false)
                    return
                }

                //判断签到结果
                val result = checkSignInResultFromUI()
                if(result != null){
//                    handleSignInResult(result)
                }
                else{
//                    mainHandler.postDelayed(this,checkInterval)
                }
            }
        }

//        mainHandler.post(runnable)
    }

    /**
     * 判断签到结果
     * 该部分代码调用了rootInActiveWindow安卓辅助功能API
     * 这个必须在主线程+辅助功能服务自身的线程中调用
     * 现在为了程序不被杀死，我已经把solutionOfNote10()放入子线程
     * 而系统禁止子线程获取当前窗口信息
     * 所以函数不被执行
     */
//    private fun checkSignInResultFromUI() : Boolean?{
////        val root = rootInActiveWindow?: return null
//        var result: Boolean? = null
//
//        Handler(Looper.getMainLooper()).post{
//            val root = rootInActiveWindow ?: run{
//                Log.w("SignInStatus","rootInActiveWindow = null")
//                result = null
//                return@post
//            }
//
//            if(root.findAccessibilityNodeInfosByText("上班打卡成功").isNotEmpty() ||
//                root.findAccessibilityNodeInfosByText("下班打卡成功").isNotEmpty()){
//                Log.d("SignInStatus","打卡成功")
//                result = true
//            }
//            else if(root.findAccessibilityNodeInfosByText("超出管理员指定的打卡范围").isNotEmpty() ||
//                root.findAccessibilityNodeInfosByText("管理员已关闭外勤打卡").isNotEmpty()){
//                Log.w("SignInStatus","打卡失败")
//                result = false
//            }
//            else{
//                Log.w("FindTextStatus","未找到对话框")
//                result = null
//            }
//        }
//        Thread.sleep(100)
//        return result
//    }
    private fun checkSignInResultFromUI() : Boolean?{
        val root = rootInActiveWindow ?: return null

        if (root.findAccessibilityNodeInfosByText("上班打卡成功").isNotEmpty() ||
            root.findAccessibilityNodeInfosByText("下班打卡成功").isNotEmpty()
        ) {
            Log.d("SignInStatus", "打卡成功")
            return true
        }

        if (root.findAccessibilityNodeInfosByText("超出管理员指定的打卡范围").isNotEmpty() ||
            root.findAccessibilityNodeInfosByText("管理员已关闭外勤打卡").isNotEmpty() ||
            root.findAccessibilityNodeInfosByText("无法打卡").isNotEmpty()
        ) {
            Log.w("SignInStatus", "打卡失败")
            return false
        }

        Log.w("FindTextStatus", "未找到对话框")
        return null
    }


    /**
     * 初始化监听器，调用上划屏幕方法
     */
    private val swipeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
//            Log.d("Broadcast",intent?.action.toString())
//            Log.d("SWIPE_ACTION", SWIPE_ACTION)
            if (intent?.action == SWIPE_ACTION) {
                Log.d("SwipeReceiver","Caught broadcast :${intent.action.toString()}")
//                solutionOfP9(context)
//                solutionOfXiaomi(context)
//                solutionOfHuaweiCX70(context)
//                solutionOfHuawei_XiaoHei(context)
//                solutionOfV20(context)
//                solutionOfHonor9(context)
                /**
                 * 我写的这个解决方案函数里让线程sleep太长了
                 * 动不动就Thread.sleep()
                 * 阻塞时间太长导致系统判定为程序无响应从而杀死程序
                 */
//                thread(start = true){
//                    solutionOfNote10(context)
//                }
                solutionOfNote10(context)
            }
        }
    }

    /**
     *
     */

    fun solutionOfHuaweiCX70(context: Context){
        try{
            Thread.sleep(2000L)
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }
        /**
         * 划动屏幕调出输入密码界面
         */
        performSwipeGesture(350F,1108F,415F,696F,500L)
        try{
            Thread.sleep(1500L)
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }

        Log.d("KeyStatus","Unlocking the screen")
        //135792的坐标
//        performClick(152F,1014F)
//        performClick(571F,1014F)
//        performClick(359F,1159F)
//        performClick(152F,1300F)
//        performClick(571F,1300F)
//        performClick(360F,1014F)

        //高级密码坐标
        performClick(577F,1160F)
        performClick(360F,1440F)
        performClick(360F,1014F)
        performClick(150F,1014F)
        performClick(360F,1014F)
        performClick(577F,1300F)

        Log.d("KeyStatus","Unlocked the screen")

        returnHome()
        try{
            Thread.sleep(1000L)
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }
        performSwipeGesture(590F,627F,163F,627F,500L)

        try{
            Thread.sleep(1000L)
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }

        /**
         * 点击桌面打卡按钮
         */
        performClick(160F,1172F)

        /**
         * 畅想70点击钉钉内部打卡按钮
         */
        try{
            Thread.sleep(7000L)
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }
        performClick(360F,960F)

        /**
         * 畅想70等待按钮反应，返回桌面
         */
        try{
            Thread.sleep(3000L)
            performSwipeGesture(400F,1570F,400F,810F,200L)
            returnHome()
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }

    }

    fun solutionOfP9(context: Context){
        try{
            Thread.sleep(2000L)
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }
        performSwipeGesture(544F,1799F,578F,777F,500L)
        try{
            Thread.sleep(1500L)
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }

        Log.d("KeyStatus","Unlocking the screen")
        performClick(219F,1005F)
        performClick(865F,1005F)
        performClick(541F,1253F)
        performClick(219F,1490F)
        performClick(865F,1490F)
        performClick(541F,1005F)
        Log.d("KeyStatus","Unlocked the screen")

        /**
         * 点击桌面打卡按钮
         */
        performClick(534F,1230F)

        /**
         * p9点击钉钉内部打卡按钮
         */
        try{
            Thread.sleep(7000L)
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }
        performClick(534F,1095F)

        /**
         * p9等待按钮反应，返回桌面
         */
        try{
            Thread.sleep(3000L)
            returnHome()
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }
//        log.writeLog(context,0x2,"钉钉打开完毕，已返回桌面")
        //p9返回桌面曾经采用这个方案
//        Thread.sleep(10000)
//        Log.d("DurationStatus","Waiting 10 seconds......")
//        try{
//            val timer = Timer()
//            timer.schedule(object : TimerTask(){
//                override fun run() {
//                    returnHome()
//                } },10000L)
//        }
//        catch(e: Exception){
//            e.printStackTrace()
//        }
    }

    fun solutionOfOppo(context: Context){
        try{
            Thread.sleep(2000L)
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }

        performSwipeGesture(518F,1393F,518F,777F,500L)
        try{
            Thread.sleep(1500L)
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }

        Log.d("KeyStatus","Unlocking the screen")
        performClick(220F,780F)
        performClick(855F,780F)
        performClick(540F,1020F)
        performClick(220F,1236F)
        performClick(855F,1236F)
        performClick(540F,780F)
        Log.d("KeyStatus","Unlocked the screen")

        returnHome()
        Thread.sleep(1000L)

        /**
         * 点击桌面打卡按钮
         */
        performClick(800F,1033F)

        /**
         * 点击钉钉内部打卡按钮
         */
        try{
            Thread.sleep(7000L)
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }
        performClick(526F,1072F)

        /**
         * 等待按钮反应，返回桌面
         */
        try{
            Thread.sleep(3000L)
            returnHome()
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }
//        try{
//            Thread.sleep(1000L)
//        }
//        catch(e: InterruptedException){
//            e.printStackTrace()
//        }
//        startDing(context)
//        Thread.sleep(8000L)
//        Handler(Looper.getMainLooper()).postDelayed({
//            performClick(543F,2275F)
//        },10000)
    }

    fun solutionOfV20(context: Context){
        try{
            Thread.sleep(2000L)
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }
        /**
         * 划动屏幕调出输入密码界面
         */
        performSwipeGesture(350F,1108F,415F,696F,500L)
        try{
            Thread.sleep(1500L)
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }

        Log.d("KeyStatus","Unlocking the screen")
        performClick(224F,1432F)
        performClick(859F,1432F)
        performClick(540F,1647F)
        performClick(224F,1863F)
        performClick(859F,1863F)
        performClick(540F,1432F)

        Log.d("KeyStatus","Unlocked the screen")

        returnHome()
        try{
            Thread.sleep(1000L)
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }
        performSwipeGesture(863F,931F,220F,931F,500L)

        try{
            Thread.sleep(1000L)
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }

        /**
         * 点击桌面打卡按钮
         */
        performClick(227F,1714F)

        /**
         * 荣耀V20点击钉钉内部打卡按钮
         */
        try{
            Thread.sleep(7000L)
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }
        performClick(540F,1315F)

        /**
         * 荣耀V20等待按钮反应，返回桌面
         */
        try{
            Thread.sleep(3000L)

            /**
             * returnHome()方法执行一次仅返回桌面
             * 连续执行两次才是返回主桌面
             */
            returnHome()
            Thread.sleep(1000L)
            performSwipeGesture(550F,2253F,550F,970F,200L)
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }

    }

    fun solutionOfXiaomi(context: Context){
        //开屏：上划屏幕，点击时间调出密码界面
        Thread.sleep(1500L)
        performSwipeGesture(345F,747F,345F,1157F,500L)
        Thread.sleep(500L)
        performClick(118F,135F)
        Thread.sleep(2000L)

        //点击按钮解锁屏幕
        Log.d("KeyStatus","Unlocking the screen")
        performClick(141F,772F)
        performClick(578F,772F)
        performClick(366F,911F)
        performClick(141F,1046F)
        performClick(578F,1046F)
        performClick(366F,772F)
        Log.d("KeyStatus","Unlocked the screen")

        try{
            Thread.sleep(1000L)
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }

        //初始化流程，定位手机主界面，小米手机特例，定位主界面需要连续执行两遍才能确保成功
        returnHome()
        Thread.sleep(1000L)
        returnHome()
        Thread.sleep(3000L)

        //点击桌面打卡
        performClick(526F,943F)

        Thread.sleep(8000L)

        //点击钉钉打卡
        performClick(357F,810F)

        Thread.sleep(5000L)

        returnHome()

//        startDing(context)
//        Log.d("DurationStatus","Waiting 10 seconds......")
//        try{
//            val timer = Timer()
//            timer.schedule(object : TimerTask(){
//                override fun run() {
//                    returnHome()
//                } },10000L)
//        }
//        catch(e: Exception){
//            e.printStackTrace()
//        }
    }

    fun solutionOfHonor9(context: Context){
        //honor9
        try{
            Thread.sleep(2000L)
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }
        /**
         * 划动屏幕调出输入密码界面
         */
        performSwipeGesture(551F,1435F,551F,512F,500L)
        try{
            Thread.sleep(1500L)
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }

        Log.d("KeyStatus","Unlocking the screen")
        performClick(226F,1107F)
        performClick(860F,1107F)
        performClick(544F,1322F)
        performClick(226F,1535F)
        performClick(860F,1535F)
        performClick(544F,1094F)

        Log.d("KeyStatus","Unlocked the screen")

        returnHome()
//        try{
//            Thread.sleep(1000L)
//        }
//        catch(e: InterruptedException){
//            e.printStackTrace()
//        }
//        performSwipeGesture(863F,931F,220F,931F,500L)

        try{
            Thread.sleep(1000L)
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }

        /**
         * 点击桌面打卡按钮
         */
        performClick(533F,1052F)



        /**
         * 荣耀9点击钉钉内部打卡按钮
         */
        try{
            Thread.sleep(7000L)
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }
        performClick(534F,1125F)

        /**
         * 荣耀9等待按钮反应，返回桌面
         */
        try{
            Thread.sleep(3000L)

            /**
             * returnHome()方法执行一次仅返回桌面
             * 连续执行两次才是返回主桌面
             */
            returnHome()
//            Thread.sleep(1000L)
//            performSwipeGesture(550F,2253F,550F,970F,200L)
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }

    }

    fun solutionOfHuawei_XiaoHei(context: Context){
        try{
            Thread.sleep(2000L)
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }

        //划动屏幕打开输入密码的界面
        performSwipeGesture(385F,859F,385F,328F,500L)
        try{
            Thread.sleep(1500L)
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }

        Log.d("KeyStatus","Unlocking the screen")
        performClick(142F,686F)
        performClick(580F,686F)
        performClick(361F,840F)
        performClick(142F,1000F)
        performClick(580F,1000F)
        performClick(361F,680F)
        Log.d("KeyStatus","Unlocked the screen")

        returnHome()
        Thread.sleep(1000L)

        /**
         * 点击桌面打卡按钮
         */
        performClick(361F,881F)

        /**
         * 点击钉钉内部打卡按钮
         */
        try{
            Thread.sleep(7000L)
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }
        performClick(360F,682F)

        /**
         * 等待按钮反应，返回桌面
         */
        try{
            Thread.sleep(3000L)
            returnHome()
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }
    }

    fun solutionOfNote10(context: Context){
        //Huawei Note10

        var flag = false
        try{
            Thread.sleep(500L)
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }
////        var flag = false;
//
////        try{
////            Thread.sleep(1000L)
////        }
////        catch(e: InterruptedException){
////            e.printStackTrace()
////        }
        /**
         * 划动屏幕调出输入密码界面
         */
        performSwipeGesture(551F,1762F,551F,512F,500L)
        try{
            Thread.sleep(1000L)
        }
        catch(e: InterruptedException){
            e.printStackTrace()
        }
        Log.d("KeyStatus","Unlocking the screen")
        performClick(837F,1692F)
        performClick(540F,2083F)
        performClick(540F,1500F)
        performClick(244F,1500F)
        performClick(540F,1500F)
        performClick(837F,1885F)

        Log.d("KeyStatus","Unlocked the screen")
        returnHome()

        try {
            Thread.sleep(500L)
        }
        catch (e: InterruptedException){
            e.printStackTrace()
        }
        performClick(545F,1063F)
        try{
            Thread.sleep(1000L)
        }
        catch (e: InterruptedException){
            e.printStackTrace()
        }

        flag = checkSignInResultFromUI() ?: false
        for(i in 0..4){
            if(flag == true){

            }
        }
//        /**
//         * 使用Handler+postDelayed分布延时
//         */
//        Handler(Looper.getMainLooper()).postDelayed({

//
//            Handler(Looper.getMainLooper()).postDelayed({
//                //点击桌面钉钉图标
//                performClick(560F,1045F)
//
////                Handler(Looper.getMainLooper()).postDelayed({
////                    //点击工作台
////                    performClick(534F,2117F)
////
////                    Handler(Looper.getMainLooper()).postDelayed({
////                        //点击考勤打卡
////                        performClick(144F,1006F)
////
////                        Handler(Looper.getMainLooper()).postDelayed({
////                            checkLoop(0)
////                        },7000)
////                    },1500)
////                },1500)
//                thread{
//                    Thread.sleep(7000)
//                    checkLoopInChildThread(0)
//                }
////                Handler(Looper.getMainLooper()).postDelayed({
////                    checkLoop(0)
////                },7000)
//            },1000)
//        },1500)

//        try{
//            Thread.sleep(1000L)
//        }
//        catch(e: InterruptedException){
//            e.printStackTrace()
//        }
//        performSwipeGesture(863F,931F,220F,931F,500L)

//        try{
//            Thread.sleep(1000L)
//        }
//        catch(e: InterruptedException){
//            e.printStackTrace()
//        }

//
//
//
//
//        /**
//         * 华为Note10点击钉钉内部打卡按钮
//         */
//        try{
//            Thread.sleep(7000L)
//        }
//        catch(e: InterruptedException){
//            e.printStackTrace()
//        }
//
//        for(i in 0 .. 9 ){
//            flag = checkSignInResultFromUI() ?: false
//            if(!flag){
//                //点击“我知道了”关闭对话框
//                performClick(805F,1273F)
//                Thread.sleep(1000L)
//
//                //点击打卡按钮
//                performClick(540F,1280F)
//            }
//            else{
//                break
//            }
//            try{
//                Thread.sleep(2000L)
//            }
//            catch (e: InterruptedException){
//                e.printStackTrace()
//            }
//        }
//
//
//        /**
//         * Note10等待按钮反应，返回桌面
//         */
//        try{
//            Thread.sleep(3000L)
//
//            /**
//             * returnHome()方法执行一次仅返回桌面
//             * 连续执行两次才是返回主桌面
//             */
//            returnHome()
////            Thread.sleep(1000L)
////            performSwipeGesture(550F,2253F,550F,970F,200L)
//        }
//        catch(e: InterruptedException){
//            e.printStackTrace()
//        }

    }

    private fun checkLoop(times: Int){
        var times = 0
        while (times < 10) {
            try {
                var result: Boolean? = null

                // 无障碍检查 → 用 Handler，无红线
                Handler(Looper.getMainLooper()).post {
                    result = checkSignInResultFromUI()
                }
                Thread.sleep(150)

                Log.d("LoopTest", "第 ${times+1} 次检查：$result")

                // 成功 → 返回桌面
                if (result == true) {
                    Thread.sleep(3000)
                    Handler(Looper.getMainLooper()).post {
                        returnHome()
                    }
                    return
                }

                // 点击“我知道了”
                Handler(Looper.getMainLooper()).post {
                    performClick(805F, 1273F)
                }
                Thread.sleep(1000)

                // 点击打卡按钮
                Handler(Looper.getMainLooper()).post {
                    performClick(540F, 1280F)
                }
                Thread.sleep(2000)

                times++

            } catch (e: Exception) {
                e.printStackTrace()
                break
            }
        }
//        if(times >= 10){
//            Log.d("LoopTest","已达最大次数，退出打卡循环")
//            return
//        }
//
//
//        val flag = checkSignInResultFromUI()
//        Log.d("LoopTest","第 $times 次检查，结果为$flag")
//
//        if(flag == true){
//            Log.d("LoopTest","打卡成功")
//            Handler(Looper.getMainLooper()).postDelayed({
//                returnHome()
//            },3000)
//            return
//        }
//
//        Log.d("LoopTest","打卡未成功")
//        performClick(805F,1273F)
//
//        Handler(Looper.getMainLooper()).postDelayed({
//            performClick(540F,1280F)
//
//            Handler(Looper.getMainLooper()).postDelayed({
//                checkLoop(times + 1)
//            },2000)
//        },1000)
    }

    /**
     * 上划屏幕
     */
    fun performSwipeGesture(startX: Float, startY: Float, endX: Float, endY: Float, duration: Long) {
        Log.d("SwipeStatus","Swiping......")
        val path = android.graphics.Path().apply {
            moveTo(startX, startY)
            lineTo(endX, endY)
        }
        val gestureBuilder = GestureDescription.Builder()
        val gestureDescription = gestureBuilder.addStroke(GestureDescription.StrokeDescription(path, 0, duration)).build()
        dispatchGesture(gestureDescription, null, null)
        Log.d("SwipeStatus","Swipe completed")
    }

    /**
     * 点击屏幕指定坐标
     */
    fun performClick(x: Float, y: Float) {
        Thread.sleep(500)
        val path = android.graphics.Path().apply{
            moveTo(x, y)
        }
        val gestureBuilder = GestureDescription.Builder()
        val gestureDescription = gestureBuilder.addStroke(GestureDescription.StrokeDescription(path, 0, 100)).build()

        dispatchGesture(gestureDescription, null, null)

    }

    /**
     * 在界面停留指定时间后返回桌面
     */
    fun returnHome() {
        /**
         * 设置返回桌面的intent
         */
        Log.d("HomeStatus","Returning home")
        val homeIntent = Intent(Intent.ACTION_MAIN)
//        Log.d("HomeStatus","Returning home x2")
//        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        Log.d("HomeStatus","Returning home x3")
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        Log.d("HomeStatus","Returning home x4")
        homeIntent.addCategory(Intent.CATEGORY_HOME)
//        Log.d("HomeStatus","Returning home x5")

        /**
         * 设置在界面停留10秒后返回桌面
         */
//        Thread.sleep(5000L)
//        Log.d("HomeStatus","Returning home x6")
        startActivity(homeIntent)
        Log.d("HomeStatus","Returned home")
    }

    /**
     * 启动钉钉
     */
    fun startDing(context: Context) {

        Log.d("DingStatus","Launching Ding")
        //获取钉钉包名
        val packageName = "com.alibaba.android.rimet"

        /**
         * 设置启动钉钉的intent
         */
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.flags = Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED or Intent.FLAG_ACTIVITY_NEW_TASK

        /**
         * 以下启动方式可以检测指定app是否处于后台状态，若是，则从后台调用；若不是，则启动app
         */
        startLaunchAPK(context, packageName)
        Log.d("DingStatus","Launched Ding")
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun startLaunchAPK(context: Context, packageName: String) {
        var mainAct: String? = null
        val pkgMag = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.flags = Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED or Intent.FLAG_ACTIVITY_NEW_TASK
        //如果已经启动apk，则直接将apk从后台调到前台运行（类似home键之后再点击apk图标启动），如果未启动apk，则重新启动
        @SuppressLint("WrongConstant")
        val list = pkgMag.queryIntentActivities(
            intent,
            PackageManager.GET_ACTIVITIES
        )
        for (i in list.indices) {
            val info = list[i]
            if (info.activityInfo.packageName == packageName) {
                mainAct = info.activityInfo.name
                break
            }
        }
        if (TextUtils.isEmpty(mainAct)) {
            return
        }
        // 启动指定的activity页面
        //intent.component = ComponentName(packageName,activityName)
        //启动到app的主页或启动到原来留下的位置
        intent.component = ComponentName(packageName, mainAct!!)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        //启动app
        context.startActivity(intent)
    }
}
