package com.san22.clonewhatsapp

import java.util.*

data class inbox (
    val msg:String,
    var from:String,
    var name:String,
var image:String,
val time:Date=Date(),
var count:Int=0){

    constructor():this("","","","",Date(),0)

}