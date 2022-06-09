package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemMessageBinding
import org.json.JSONObject

class MessageAdapter: RecyclerView.Adapter<ItemViewHolder>() {

    val messageList = arrayListOf<JSONObject>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        if (messageList[position].getBoolean("byServer")) {
            holder.binding.receivedMessage.visibility = View.VISIBLE
            holder.binding.receivedMessage.text = messageList[position].getString("message")
            holder.binding.sentMessage.visibility = View.INVISIBLE
        } else {
            holder.binding.sentMessage.visibility = View.VISIBLE
            holder.binding.sentMessage.text = messageList[position].getString("message")
            holder.binding.receivedMessage.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    fun addItem(item: JSONObject) {
       messageList.add(item)
        notifyDataSetChanged()
    }
}

class ItemViewHolder(val binding: ItemMessageBinding): RecyclerView.ViewHolder(binding.root)