package com.example.stack_routing.models

import java.io.Serializable

data class BrowserSession(
    val tabs: Map<Int, BrowserTab>,
    val activeTabId: Int
): Serializable
