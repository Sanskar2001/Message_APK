package com.san22.clonewhatsapp

import com.google.firebase.auth.PhoneAuthProvider

data class user(
   var name:String,
    var imageurl:String,
   var thumbnail:String,
   var deviceToken:String,
   var status:String,
   var online:Boolean,
   var uid:String
) {
    /** Empty [Constructor] for Firebase */
    constructor() : this("","","","","Hello",false,"")

   constructor(Name: String,imageurl: String,thumbnail: String,uid: String) : this() {
       this.name=Name
       this.imageurl=imageurl
       this.thumbnail=thumbnail
       this.uid=uid
       this.online=false
   }


}