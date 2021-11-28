package com.example.serviceseparateprocess.services

import android.app.Service
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.example.serviceseparateprocess.KEY_MESSAGE
import com.example.serviceseparateprocess.KEY_TYPE
import com.example.serviceseparateprocess.MessageAidlInterface
import com.example.serviceseparateprocess.Service2AidlInterface
import com.example.serviceseparateprocess.services.Service1.Companion.RECEIVE_KEY_SERVICE1
import com.example.serviceseparateprocess.type.Type
import kotlinx.coroutines.*


class Service2 : Service() {

    companion object {
        const val RECEIVE_KEY_SERVICE2 = "RECEIVE_KEY_SERVICE2"
    }
    private val dTag = Service2::class.java.simpleName

    private lateinit var demoType: Type

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            printReceivedMessage(p1?.extras)
            sendMessage()
        }

    }

    private val aldi = object: Service2AidlInterface.Stub(){
        override fun sayHelloAndReturnString() : String{
            val value = "\"How are you?\""
            Log.d(dTag, "Service2 call sayHelloAndReturnString, return $value")
            sendMessage()
            return value
        }

        override fun setUpMessageSender(messageReceiver: MessageAidlInterface) {
            Log.d(dTag, "setUpMessageSender")
            messageSender = messageReceiver
        }
    }

    private lateinit var messageSender: MessageAidlInterface

    override fun onCreate() {
        super.onCreate()
        Log.d(dTag, "onCreate")
    }

    private val scope = CoroutineScope(Job() + Dispatchers.Default)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.apply {
            if(hasExtra(KEY_TYPE)) {
                demoType = intent.extras!!.getSerializable(KEY_TYPE) as Type
                when(demoType) {
                    Type.BY_INTENT -> {
                        sendMessage()
                    }
                    Type.BY_BROADCASTER -> {
                        registerReceiver(receiver, IntentFilter(RECEIVE_KEY_SERVICE2))
                    }
                    Type.BY_AIDL -> {

                    }
                }
            } else if (hasExtra(KEY_MESSAGE)) {
                printReceivedMessage(intent.extras)
                sendMessage()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        if(intent.hasExtra(KEY_TYPE)) {
            demoType = intent.extras!!.getSerializable(KEY_TYPE) as Type
        }
        return if(demoType == Type.BY_AIDL) aldi else null
    }

    private fun sendMessage() {
        scope.launch {
            delay(2000)
            when (demoType) {
                Type.BY_INTENT -> {
                    sendMessageByIntent()
                }
                Type.BY_BROADCASTER -> {
                    sendMassageByBroadcast()
                }
                Type.BY_AIDL -> {
                    sendMassageByAIDL()
                }
            }

        }
    }

    private fun printReceivedMessage(bundle: Bundle?) {
        Log.d(dTag, "${bundle?.get(KEY_MESSAGE)}")
    }

    private fun sendMessageByIntent() {
        startService(Intent(this, Service1::class.java).apply {
            putExtra(KEY_MESSAGE, "sendMessageByIntent from $dTag")
        })
    }

    private fun sendMassageByBroadcast() {
        sendBroadcast(Intent(RECEIVE_KEY_SERVICE1).apply {
            putExtra(KEY_MESSAGE, "sendMassageByBroadcast from $dTag")
        })
    }

    private fun sendMassageByAIDL() {
        messageSender.messageReceiver(KEY_MESSAGE, Bundle().also {
            it.putString(KEY_MESSAGE, "sendMassageByAIDL from $dTag")
        })
    }
}