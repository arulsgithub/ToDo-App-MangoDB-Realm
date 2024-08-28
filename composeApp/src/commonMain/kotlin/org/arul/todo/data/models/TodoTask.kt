package org.arul.todo.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class TodoTask: RealmObject {

    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var title: String = ""
    var desc: String = ""
    var completed: Boolean = false
    var fav: Boolean = false
}