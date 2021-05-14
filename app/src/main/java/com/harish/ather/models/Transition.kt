package com.harish.ather.models

import com.harish.ather.enums.TileMoveType
import java.io.Serializable

class Transition : Serializable {
    var action: TileMoveType
    var value: Int
    var location: Int
    var oldLocation: Int

    constructor(action: TileMoveType, value: Int, location: Int, oldLocation: Int = -1) {
        this.action = action
        this.value = value
        this.location = location
        this.oldLocation = oldLocation
    }
}