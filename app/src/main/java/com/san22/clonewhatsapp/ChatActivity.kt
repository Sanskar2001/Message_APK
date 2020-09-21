package com.san22.clonewhatsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.san22.clonewhatsapp.Adapter.ChatAdapter
import com.san22.clonewhatsapp.Fragments.NAME
import com.squareup.picasso.Picasso
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.google.GoogleEmojiProvider
import kotlinx.android.synthetic.main.activity_chat.*

val Name="NAME"
val PHOTO="PHOTO"
val UID="UID"
private val mutableItems: MutableList<ChatEvent> = mutableListOf()
class ChatActivity() : AppCompatActivity() {

    val db by lazy {
        FirebaseDatabase.getInstance()
    }
    val user by lazy {
        FirebaseAuth.getInstance()
    }
    val friendid by lazy {
        intent.getStringExtra(UID)
    }
    val photo by lazy{
        intent.getStringExtra(PHOTO)
    }
    val string by lazy{
        intent.getStringExtra(NAME)

    }
    val curruserid  by lazy{
        user.uid.toString()
    }
    val chatadapter=ChatAdapter(mutableItems,curruserid)
    override fun onCreate(savedInstanceState: Bundle?) {
        EmojiManager.install(GoogleEmojiProvider())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatrv.apply {
            layoutManager=LinearLayoutManager(this@ChatActivity)
            adapter=chatadapter
        }
        setupview(string,photo)
        sendbutton.setOnClickListener()
        {
            etchatbox.text?.let{
                if (!it.isNullOrEmpty())
                {
                    sendmessage(it.toString())
                    it.clear()
                }

            }
        }

    }

    private fun sendmessage(msg:String) {
        val id=getMessages().push().key
        checkNotNull(id){"Cannot be null"}
        val msgMap=Message(msg=msg,senderID = user.uid.toString(),msgID = id)
        getMessages().child(id).setValue(msgMap)
        updateLastMessage(msgMap)
        listentoMessages()
    }

    private fun listentoMessages()
    {
        getMessages().orderByKey().addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val msg=snapshot.getValue(Message::class.java)
                addMessage(msg!!)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun addMessage(msg: Message) {
       val eventbefore= mutableItems.lastOrNull()
        if (eventbefore!=null && !eventbefore.sentAt.isSameDayAs(msg.sentAt))
        {
             mutableItems.add(DateHeader(msg.sentAt,this))
        }
               mutableItems.add(msg)
        chatadapter.notifyItemInserted(mutableItems.size-1)
        chatrv.scrollToPosition( mutableItems.size-1)

    }

    private fun updateLastMessage(msgMap: Message) {
        val inboxmap=inbox(
            msg = msgMap.msg,
           from = curruserid!!,
           name =  string!!,
           image = photo!!,
            count = 1
        )
          getInbox(curruserid,friendid!!).setValue(inboxmap).addOnSuccessListener {
              getInbox(friendid!!,curruserid).addListenerForSingleValueEvent(object :ValueEventListener{
                  override fun onCancelled(error: DatabaseError) {}

                  override fun onDataChange(snapshot: DataSnapshot) {
                      val lastinbox=snapshot.getValue(inbox::class.java)
                      inboxmap.apply {
                          from=msgMap.senderID
                          name=string!!
                          image =photo!!
                          count=0   //11111
                      }
                     if (msgMap.senderID==lastinbox!!.from)
                     {
                         inboxmap.count=lastinbox.count+1
                     }
                      getInbox(friendid!!,curruserid).setValue(inboxmap)
                  }



              })


          }
       }

    private fun getInbox(friendid: String, curruserid: String) =db.reference.child("Chats/$curruserid/$friendid")




    private fun getMessages()=db.reference.child("messages/${getID(friendid!!)}")

    private fun getID(uid: String): String {
       return if(uid> user.uid.toString()){
           user.uid.toString()+uid
       }
        else{
           uid+user.uid.toString()
       }


    }

    private fun setupview(string:String?,photo:String?) {

        if (getMessages()!=null)
        {
            listentoMessages()
        }
        titletv.text=string
        Picasso.get().load(photo).centerCrop().fit().into(dpimgview)
    }


}




