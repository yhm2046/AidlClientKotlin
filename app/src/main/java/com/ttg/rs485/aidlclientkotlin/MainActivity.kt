package com.ttg.rs485.aidlclientkotlin

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.aidlserver.IDataService
import com.example.aidlserver.IDataServiceCallback
import com.example.aidlserver.IMyAidlInterface


/**
 * 客户端主动发消息
 */
class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "client-MainActivity:xwg"
    }
    private var mService: IDataService? = null
    private val mHandler = Handler()
    private val mRunnable = object : Runnable {
        override fun run() {
            mService?.let {
                try {
                    val message = "485 date-" + System.currentTimeMillis()
                    it.sendMessage(message)
                    Log.i(TAG, "client send to server:$message")
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }
            mHandler.postDelayed(this, 2 * 1000)
        }
    }
    private val mCallback = object : IDataServiceCallback.Stub() {
        override fun onMessageReceived(message: String?) {
            Log.d(TAG, "Received message from server: $message")
        }
    }
    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "onServiceConnected ")
            mService = IDataService.Stub.asInterface(service)
            try {
                mService?.registerCallback(mCallback)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }

        }
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected ")
            mService = null
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate.. ")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val intent = Intent().apply {
//            setClassName("com.example.aidlserver", "com.example.aidlserver.DataServiceNoPolling")
////            setClassName("com.example.aidlserver", "com.example.aidlserver.MyService")
//        }
//        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)

        val intent = Intent()
//        intent.setPackage("com.example.aidlserver")
//        intent.action = "com.example.service.action"
        intent.setClassName("com.example.aidlserver", "com.example.aidlserver.DataServiceNoPolling")
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
        mHandler.postDelayed(mRunnable, 2000)
    }
    override fun onDestroy() {
        super.onDestroy()
        mService?.let {
            try {
                it.unregisterCallback(mCallback)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
            unbindService(mConnection)
        }
        mHandler.removeCallbacks(mRunnable)
    }
}