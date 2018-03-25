package com.sample.app2

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.sample.app2.ChatActivity.Message

/**
 * Created by Ishaq Hassan on 3/21/2018.
 */
class ChatMessagesAdapter(val messages: ArrayList<Message>, public val onItemClick: (Int, View) -> Unit) : Adapter<RecyclerView.ViewHolder>() {

    private val IN_MSG = 1
    private val OUT_MSG = 2

    class BaseMsgViewHolder(aa: View) : RecyclerView.ViewHolder(aa) {
        private val msgTextTv: TextView = aa.findViewById(R.id.msgTextTv)

        fun bindMessage(msg: Message) {
            msgTextTv.text = msg.msg
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is BaseMsgViewHolder){
            holder.bindMessage(messages[position])
            holder.itemView.setOnClickListener {
                onItemClick(position,holder.itemView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inMSG = BaseMsgViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.in_msg_layout, parent, false))
        val outMSG = BaseMsgViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.out_msg_layout, parent, false))
        return when (viewType) {
            IN_MSG -> inMSG
            OUT_MSG -> outMSG
            else -> inMSG
        }
    }

    override fun getItemViewType(position: Int): Int {
        val msg  = messages[position]
        return when(msg.uid){
            FirebaseAuth.getInstance().currentUser?.uid -> OUT_MSG
            else -> IN_MSG
        }
    }

}