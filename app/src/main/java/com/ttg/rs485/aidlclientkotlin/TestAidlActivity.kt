package com.ttg.rs485.aidlclientkotlin

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.aidlserver.IMyAidlInterface
import com.ttg.rs485.aidlclientkotlin.databinding.ActivityMainBinding

/**
 * test aidl
 */
class TestAidlActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "client-TestAidlActivity:xwg"
    }

    private var activityMainBinding: ActivityMainBinding? = null
    var myAidlInterface: IMyAidlInterface? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(activityMainBinding!!.root)
        Log.i(TAG,"running onCreate")

        val connection: ServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
                try {
                    Log.i(TAG, "onServiceConnected..")
                    myAidlInterface = IMyAidlInterface.Stub.asInterface(iBinder)
                } catch (e: Exception) {
                    Log.i(TAG,"onServiceConnected exception:$e")
                    e.printStackTrace()
                }
            }

            override fun onServiceDisconnected(componentName: ComponentName) {
                Log.i(TAG,"onServiceDisconnected!")
            }
        } //end connection


        val intent = Intent()
//        intent.setPackage("com.example.aidlserver")
//        intent.action = "com.example.service.action"
        intent.setClassName("com.example.aidlserver", "com.example.aidlserver.MyService")
        bindService(intent, connection, Context.BIND_AUTO_CREATE)

        activityMainBinding?.button?.setOnClickListener(View.OnClickListener {
            Log.i(TAG, "on click")
            val st = myAidlInterface?.get15RandomString()
            Log.i(TAG, "st: $st")
        })

    }//onCreate
}