package com.android.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoachingSummaryJson(
    val activeTodo: Boolean,
    val activeChat: Boolean,
    val numberOfTodoItems: Int,
    val numberOfCompletedTodoItems: Int,
    val selected: Boolean
)
