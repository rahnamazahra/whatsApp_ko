package com.rahnama.whatsapp

class ChatMessageModel() {
    var id:String?=null
    var sender:String?=null
    var reciver:String?=null
    var message:String?=null
    var date:String?=null
    var time:String?=null
    constructor(id:String,reciver:String,sender:String,message:String,date:String,time:String):this(){
        this.id=id
        this.sender=sender
        this.reciver=reciver
        this.message=message
        this.date=date
        this.time=time
    }
}