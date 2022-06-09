package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding
import okhttp3.*
import okio.ByteString
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var webSocket: WebSocket
    private lateinit var mAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        instantiateWebSocket()

        mAdapter = MessageAdapter()
        binding.messageList.layoutManager = LinearLayoutManager(this)
        binding.messageList.adapter = mAdapter

        binding.send.setOnClickListener {
            val message = binding.messageBox.text.toString()
            if (message.isNotEmpty()) {
                webSocket.send(message)
                val jsonObject = JSONObject()
                try {
                    jsonObject.put("message", message)
                    jsonObject.put("byServer", false)
                    mAdapter.addItem(jsonObject)
                    binding.messageBox.setText("")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }

    }

    private fun instantiateWebSocket() {
        val client = OkHttpClient()
        val request = Request.Builder().url("ws://192.168.43.105:8080").build()
        val socketListener = SocketListener(this, object: SocketListener.MessageReceived {
            override fun onMessageReceived(message: JSONObject) {
                mAdapter.addItem(message)
            }

        })
        webSocket = client.newWebSocket(request, socketListener)
    }

    class SocketListener(val activity: MainActivity, val messageListener: MessageReceived): WebSocketListener() {

        interface MessageReceived {
            fun onMessageReceived(message: JSONObject)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            activity.runOnUiThread(Runnable {
                val jsonObject = JSONObject()
                try {
                    jsonObject.put("message", text)
                    jsonObject.put("byServer", true)
                    messageListener.onMessageReceived(jsonObject)
//                    mAdapter.addItem(jsonObject)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            })
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            super.onMessage(webSocket, bytes)
        }

        override fun onOpen(webSocket: WebSocket, response: Response) {
            activity.runOnUiThread {
                Toast.makeText(activity, "Connection Established!", Toast.LENGTH_SHORT).show()
            }
        }

    }
}