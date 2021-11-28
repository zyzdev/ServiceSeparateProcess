package com.example.serviceseparateprocess

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.MainThread
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import com.example.serviceseparateprocess.services.Service1
import com.example.serviceseparateprocess.services.Service2
import com.example.serviceseparateprocess.type.Type

const val KEY_TYPE = "type"
const val KEY_MESSAGE = "message"

class MainActivity : AppCompatActivity() {

    private val dTag = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.button).setOnClickListener {
            startActivity(Intent(this, ByIntentActivity::class.java))
        }
        findViewById<View>(R.id.button2).setOnClickListener {
            startActivity(Intent(this, ByBroadcastReceiverActivity::class.java))
        }
        findViewById<View>(R.id.button3).setOnClickListener {
            startActivity(Intent(this, ByAIDLActivity::class.java))
        }
    }
}

class ByIntentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_by_xxx)
        startService(Intent(this, Service1::class.java).apply {
            putExtra(KEY_TYPE, Type.BY_INTENT)
        })
        startService(Intent(this, Service2::class.java).apply {
            putExtra(KEY_TYPE, Type.BY_INTENT)
        })
    }
}

class ByBroadcastReceiverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_by_xxx)
        startService(Intent(this, Service1::class.java).apply {
            putExtra(KEY_TYPE, Type.BY_BROADCASTER)
        })
        startService(Intent(this, Service2::class.java).apply {
            putExtra(KEY_TYPE, Type.BY_BROADCASTER)
        })
    }
}

class ByAIDLActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_by_xxx)
        startService(Intent(this, Service1::class.java).apply {
            putExtra(KEY_TYPE, Type.BY_AIDL)
        })
    }
}