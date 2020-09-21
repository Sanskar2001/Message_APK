package com.san22.clonewhatsapp.Fragments

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.san22.clonewhatsapp.R
import com.san22.clonewhatsapp.user
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.listitem.view.*

class userViewholder( itemView: View):RecyclerView.ViewHolder(itemView)
{
  fun bind(user: user,clickfun:(name:String,photo:String,id:String)->Unit)=

      with(itemView)
      {

          title.text=user.name
          subtitle.text=user.status
          counttv.alpha=0F
          timetv.alpha=0F
          Picasso.get().load(user.thumbnail).centerCrop().fit().placeholder(R.drawable.ic_baseline_account_circle_24).into(imgview)
           setOnClickListener(){
               clickfun.invoke(user.name,user.thumbnail,user.uid)
           }

      }
  }

class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view)
