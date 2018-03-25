package com.sample.app2

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.GONE
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

    open class BaseMsgViewHolder(myView: View) : RecyclerView.ViewHolder(myView) {
        private val msgTextTv: TextView = myView.findViewById(R.id.msgTextTv)

        open fun bindMessage(msg: Message,isSameUserAsBefore: Boolean) {
            msgTextTv.text = msg.msg
        }
    }

    class InMsgViewHolder(myView: View) : BaseMsgViewHolder(myView){
        private val senderEmailTextTv:TextView = myView.findViewById(R.id.senderEmailTextTv)

        override fun bindMessage(msg: Message,isSameUserAsBefore:Boolean) {
            senderEmailTextTv.visibility = View.GONE
            if(!isSameUserAsBefore){
                senderEmailTextTv.visibility = View.VISIBLE
                senderEmailTextTv.text = msg.uname
            }
            super.bindMessage(msg,isSameUserAsBefore)
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMsg = messages[position]
        if(holder is BaseMsgViewHolder){
            var isSameUserAsBefore = false
            if(position > 0 && messages[position-1].uid == currentMsg.uid){
                isSameUserAsBefore = true
            }

            holder.bindMessage(currentMsg,isSameUserAsBefore)
            holder.itemView.setOnClickListener {
                onItemClick(position,holder.itemView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inMSG = InMsgViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.in_msg_layout, parent, false))
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