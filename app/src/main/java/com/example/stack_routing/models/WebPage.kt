package com.example.stack_routing.models

import java.io.Serializable

data class WebPage(
    val url: String,
    val title: String,
    val loadTime: Long = System.currentTimeMillis(),
    var leaveTime: Long? = null
): Serializable
