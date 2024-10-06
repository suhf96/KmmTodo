package com.gyadam.kmmtodo.domain

import io.realm.kotlin.types.RealmObject
import org.mongodb.kbson.ObjectId
import io.realm.kotlin.types.annotations.PrimaryKey

class ToDoTask : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var title: String = ""
    var description: String = ""
    var favourite: Boolean = false
    var isDone: Boolean = false

}