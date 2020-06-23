package com.roda.paqueue.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class Queue (
    @PrimaryKey
    var id: String,
    @Required
    var status: String = "IDLE",
    var court_number: Int = 0,
    var created_at: Date = Date(),
    var players: RealmList<Player> = RealmList()
): RealmObject()