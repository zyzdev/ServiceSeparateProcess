package com.example.serviceseparateprocess.services

import android.app.Service
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.annotation.WorkerThread
import com.example.serviceseparateprocess.KEY_MESSAGE
import com.example.serviceseparateprocess.KEY_TYPE
import com.example.serviceseparateprocess.MessageAidlInterface
import com.example.serviceseparateprocess.Service2AidlInterface
import com.example.serviceseparateprocess.services.Service2.Companion.RECEIVE_KEY_SERVICE2
import com.example.serviceseparateprocess.type.Type
import kotlinx.coroutines.*

class Service1 : Service() {

    companion object {
        const val RECEIVE_KEY_SERVICE1 = "RECEIVE_KEY_SERVICE1"
    }

    private val dTag = Service1::class.java.simpleName

    private lateinit var demoType: Type
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            printReceivedMessage(p1?.extras)
            sendMessage()
        }

    }

    private lateinit var service2AidlInterface: Service2AidlInterface

    private val messageReceiver :MessageAidlInterface = object : MessageAidlInterface.Stub(){

        override fun messageReceiver(code:String, data:Bundle) {
            printReceivedMessage(data)
            sendMessage()
        }
    }
    override fun onCreate() {
        super.onCreate()
        Log.d(dTag, "onCreate")
    }

    private val scope = CoroutineScope(Job() + Dispatchers.Default)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.apply {
            if (hasExtra(KEY_TYPE)) {
                demoType = intent.extras!!.getSerializable(KEY_TYPE) as Type
                when (demoType) {
                    Type.BY_INTENT -> {
                        sendMessage()
                    }
                    Type.BY_BROADCASTER -> {
                        registerReceiver(receiver, IntentFilter(RECEIVE_KEY_SERVICE1))
                        sendMessage()
                    }
                    Type.BY_AIDL -> {
                        Log.d(dTag, "demoType:$demoType")
                        bindService(Intent(this@Service1, Service2::class.java).apply {
                            putExtra(
                                KEY_TYPE, demoType
                            )
                        }, object : ServiceConnection {
                            override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
                                service2AidlInterface = Service2AidlInterface.Stub.asInterface(p1)
                                service2AidlInterface.setUpMessageSender(messageReceiver)
                                sendMessage()
                            }

                            override fun onServiceDisconnected(p0: ComponentName?) {

                            }

                        }, Context.BIND_AUTO_CREATE)
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
        return null
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
                    Log.d(dTag, service2AidlInterface.sayHelloAndReturnString())
                }
            }

        }
    }

    private fun printReceivedMessage(bundle: Bundle?) {
        Log.d(dTag, "${bundle?.get(KEY_MESSAGE)}")
    }

    private fun sendMessageByIntent() {
        startService(Intent(this, Service2::class.java).apply {
            putExtra(KEY_MESSAGE, "sendMessageByIntent from $dTag")
        })
    }

    private fun sendMassageByBroadcast() {
        sendBroadcast(Intent(RECEIVE_KEY_SERVICE2).apply {
            putExtra(KEY_MESSAGE, "sendMassageByBroadcast from $dTag")
        })
    }
}