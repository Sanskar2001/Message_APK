package com.san22.clonewhatsapp.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.san22.clonewhatsapp.ChatActivity
import com.san22.clonewhatsapp.R
import com.san22.clonewhatsapp.user
import id.zelory.compressor.overWrite
import kotlinx.android.synthetic.main.fragment_people.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
val NAME="NAME"
val PHOTO="PHOTO"
val UID="UID"

private const val DELETED_VIEW_TYPE = 1
private const val NORMAL_VIEW_TYPE = 2

class PeopleFragment() : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    lateinit var mpagerAdapter:FirestorePagingAdapter<user,RecyclerView.ViewHolder>

     val database by lazy{
        FirebaseFirestore.getInstance().collection("users").orderBy("name",Query.Direction.DESCENDING)
    }
    val  auth by lazy {
        FirebaseAuth.getInstance()
    }
    val curruser by lazy {
        auth.currentUser!!.uid.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        setupAdapter()
        return inflater.inflate(R.layout.fragment_people, container, false)
    }

    private fun setupAdapter() {
        val config=PagedList.Config.Builder().setPrefetchDistance(2).setEnablePlaceholders(false).setPageSize(10).build()
        val mQuery=database
       val optionss=FirestorePagingOptions.Builder<user>().setLifecycleOwner(viewLifecycleOwner).setQuery(mQuery,config,user::class.java).build()
       mpagerAdapter=object:FirestorePagingAdapter<user,RecyclerView.ViewHolder>(optionss){

           override fun onCreateViewHolder(
               parent: ViewGroup,
               viewType: Int,
           ): RecyclerView.ViewHolder {
               Log.i("ViewType:","$viewType")
               return  userViewholder(layoutInflater.inflate(R.layout.listitem,parent,false))
              return when(viewType)
               {

                   NORMAL_VIEW_TYPE->{
                     userViewholder(layoutInflater.inflate(R.layout.listitem,parent,false))
                   }

                   else->{
                       EmptyViewHolder(layoutInflater.inflate(R.layout.empty_view,parent,false))
                   }


               }

           }

           override fun onBindViewHolder(
               holder: RecyclerView.ViewHolder,
               position: Int,
               model: user,
           ) {


              if(holder is userViewholder) {


                             holder.bind(model) { name, photo, id ->
                                 val myintent = Intent(requireContext(), ChatActivity::class.java)
                                 myintent.putExtra(NAME, name)
                                 myintent.putExtra(PHOTO, photo)
                                 myintent.putExtra(UID, id)
                                 startActivity(myintent)
                             }
                         }




              }




           override fun getItemId(position: Int): Long {
               val item = getItem(position)?.toObject(user::class.java)
                 Log.i("ids","item=${item!!} and curruser=$curruser")


               if (curruser==item?.uid) {
                   return DELETED_VIEW_TYPE.toLong()
               } else {
                  return NORMAL_VIEW_TYPE.toLong()
               }
           }


       }


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv.apply {
            layoutManager=LinearLayoutManager(requireContext())
            adapter=mpagerAdapter
        }
    }




}