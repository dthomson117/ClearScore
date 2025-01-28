package com.android.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoachingSummaryJson(
    val activeTodo: Boolean,
    val activeChat: Boolean,
    val numberOfTodoItems: Int,
    val numberOfCompletedTodoItems: Int,
    val selected: Boolean,
) {
    companion object {
        fun default() = CoachingSummaryJson(
            activeTodo = false,
            activeChat = false,
            numberOfTodoItems = 0,
            numberOfCompletedTodoItems = 0,
            selected = false
        )
    }
}
