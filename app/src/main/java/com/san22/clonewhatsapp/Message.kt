package com.san22.clonewhatsapp
import com.san22.clonewhatsapp.formatAsHeader
import android.content.Context
import java.util.*
interface ChatEvent{
    val sentAt:Date
}
data class Message(val msg:String,
 val senderID:String,
val msgID:String,
val type:String="TEXT",
val status:Int=1,
val linked:Boolean=false,
override val sentAt:Date=Date()):ChatEvent{
constructor():this("","","","",1,false, Date())
}
data class DateHeader(override val sentAt: Date, val context: Context) : ChatEvent {
    val date: String = sentAt.formatAsHeader(context)
}
//data class DataHeader(override val sentAt: Date= Date(), val context: Context):ChatEvent{
//    val date:String=sentAt.for
//}