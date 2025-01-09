package com.example.stack_routing.models

import com.example.stack_routing.BrowserHistory
import java.io.Serializable

data class BrowserTab(
    val id: Int,
    val history: BrowserHistory = BrowserHistory()
): Serializable
