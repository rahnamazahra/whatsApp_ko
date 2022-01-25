package com.rahnama.whatsapp

import io.reactivex.internal.operators.single.SingleDoAfterTerminate

class UserModel() {
    var Name:String?=null
    var About:String?=null
    var phoneNumber:String?=null
    var image:String?=null
    var thumb_image:String?=null
    var Time:String?=null
    var Date:String?=null
    var State:String?=null

constructor(Name:String,About:String,phoneNumber:String,thumb_image:String,image:String,Time:String,Date:String,State:String):this(){
    this.Name=Name
    this.About=About
    this.image=image
    this.thumb_image=thumb_image
    this.phoneNumber=phoneNumber
    this.Time=Time
    this.Date=Date
    this.State=State
}

}