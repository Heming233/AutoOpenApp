package com.better.alarm.services

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar

/**
 * 打印并输出日志文件至./Android/data/com.example.autodingtest/files/AutoDingTest/AutoDingTestLog.txt
 */
class LogOutput {

    private  val LEVEL_FILE = 0x2

    fun writeLog(context: Context,logLevel : Int, msg : String){
        if(LEVEL_FILE == logLevel){

            //该获取路径的方法已被弃用
//            val filePath = Environment.getExternalStorageDirectory().absolutePath

            /**
             * 获取项目在手机存储中的路径
             */
            val filePath = context.getExternalFilesDir(null)?.absolutePath
//            if (filePath != null) {
//                Log.d("the file path is ", filePath)
//            }
            val fileWriter: FileWriter
            var bufferedWriter: BufferedWriter? = null

            try{
                val direction = File(filePath, "SimpleClock")
//                if(!direction.exists()){
//                    if(direction.mkdir()){
//                        Log.d("mkdirInfo","mkdir successfully")
//                    }
//                    else{
//                        Log.d("mkdirInfo","mkdir failed")
//                    }
//                }
                if(!direction.exists()){
                    direction.mkdir()
                }

                val file = File(direction, "SimpleClockLog.txt")
                if(!file.exists()){
                    file.createNewFile()
                }

                fileWriter = FileWriter(file, true)
                bufferedWriter = BufferedWriter(fileWriter)
                bufferedWriter.write("$msg=======时间：${getCurrentTime()}\n")
                bufferedWriter.close()
            }
            catch(e:Exception){
                e.printStackTrace()
            }
            finally {
                if(bufferedWriter != null){
                    try{
                        bufferedWriter.close()
                    }
                    catch(e: IOException){
                        e.printStackTrace()
                    }
                }
            }

        }
        else{
            Log.d("SimpleClockLogStatus", msg)
        }
    }

    private fun getCurrentTime(): String{
        val calendar = Calendar.getInstance()
        @SuppressLint("SimpleDateFormat")
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return sdf.format(calendar.time)
    }
}
