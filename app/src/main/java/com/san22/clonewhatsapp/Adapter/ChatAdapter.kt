package com.san22.clonewhatsapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.san22.clonewhatsapp.*
import kotlinx.android.synthetic.main.list_item_chat_recv_message.view.*
import kotlinx.android.synthetic.main.list_item_date_header.view.*

class ChatAdapter(private val list:MutableList<ChatEvent>,private val curruserid:String):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       val inflate={ layout:Int->
           LayoutInflater.from(parent.context).inflate(layout,parent,false)
       }
        return when(viewType)
        {
            TEXT_MESSAGE_RECEIVED->{
                MessageHolder(inflate(R.layout.list_item_chat_recv_message))
            }

            TEXT_MESSAGE_SENT->{
                MessageHolder(inflate(R.layout.list_item_chat_sent_message))
            }
            DATE_HEADER->{
                MessageHolder(inflate(R.layout.list_item_date_header))
            }
            else->{
                MessageHolder(inflate(R.layout.list_item_chat_recv_message))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        return when (val event = list[position]) {
            is Message -> {
                if (event.senderID ==curruserid) {
                    TEXT_MESSAGE_SENT
                } else {
                    TEXT_MESSAGE_RECEIVED
                }
            }
            is DateHeader -> DATE_HEADER
            else -> UNSUPPORTED
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is DateHeader -> {
                holder.itemView.textView.text = item.date
            }
            is Message -> {
                holder.itemView.content.text = item.msg
                holder.itemView.time.text = item.sentAt.formatAsTime()


                    }
                }
            }



    override fun getItemCount(): Int=list.size

    class MessageHolder(view: View) : RecyclerView.ViewHolder(view)

    companion object{
        private const val UNSUPPORTED = -1
        private const val TEXT_MESSAGE_RECEIVED = 0
        private const val TEXT_MESSAGE_SENT = 1
        private const val DATE_HEADER = 2
    }
}