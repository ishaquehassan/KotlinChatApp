package com.sample.app2

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    var user:FirebaseUser? = null
    var messagesRef:DatabaseReference? = null
    var msgEt : EditText? = null
    var messagesList:RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        user = FirebaseAuth.getInstance().currentUser
        messagesRef = FirebaseDatabase.getInstance().getReference("messages")

        msgEt = findViewById(R.id.msgEt)

        findViewById<Button>(R.id.btnSend).setOnClickListener {
            if(msgEt?.text?.isNotEmpty() == true){
                sendMessage(msgEt?.text.toString())
            }
        }


        val messages:ArrayList<Message> = arrayListOf()

        messagesList = findViewById(R.id.chatMessagesList)
        messagesList?.layoutManager = LinearLayoutManager(this)
        messagesList?.adapter = ChatMessagesAdapter(messages,{position,view ->
            Toast.makeText(this, "tapped at position $position", Toast.LENGTH_SHORT).show();
        })

        messagesRef?.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val msg = p0?.getValue(Message::class.java)
                if(msg != null){
                    messages.add(msg)
                    val lastPos = messages.size-1
                    messagesList?.adapter?.notifyItemInserted(lastPos)
                    messagesList?.scrollToPosition(lastPos)
                }
            }

            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    override fun onResume() {
        super.onResume()
        if(user == null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }

    private fun sendMessage(msg:String){
        messagesRef?.push()?.setValue(Message(msg = msg,uid = user?.uid!!,uname = user?.email!!,timestamp = System.currentTimeMillis()))
                ?.addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        msgEt?.text?.clear()
                    }else{
                        Toast.makeText(this,"Error ${task.exception?.message}",Toast.LENGTH_LONG).show()
                    }
                }
    }

    data class Message(val msg:String,val uid:String,val uname:String,val timestamp: Long){
        constructor() : this("","","",0)
    }
}
