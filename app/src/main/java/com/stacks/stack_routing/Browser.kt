package com.example.stack_routing

import com.example.stack_routing.customExceptions.BrowserHistoryException
import com.example.stack_routing.customExceptions.TabManagementException
import com.example.stack_routing.models.BrowserSession
import com.example.stack_routing.models.BrowserTab
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
 * Browser class managing multiple tabs and sessions for a web browser
 */

class Browser : Serializable {
    private val tabs = HashMap<Int, BrowserTab>()
    private var activeTabId: Int = 0
    private var nextTabId: Int = 0

    companion object {
        // Required for serialization
        private const val serialVersionUID: Long = 1L
    }

    init {
        createNewTab()
    }

    /**
     * Function to create a new web tab
     */
     fun createNewTab(): Int {
        val newTabId = nextTabId++
        tabs[newTabId] = BrowserTab(newTabId)
        activeTabId = newTabId
        println("Create new tab with ID: $newTabId")
        return newTabId
    }

    /**
     * Switch to a new web tab
     */
    fun switchTab(tabId: Int) {
        if (!tabs.containsKey(tabId)) {
            throw TabManagementException("Tab $tabId does not exist")
        }
        activeTabId = tabId
        println("Switched to tab: $tabId")
    }

    /**
     * Save browser session
     */

    fun saveSession(fileName: String) {
        try {
            ObjectOutputStream(FileOutputStream(fileName)).use { out ->
                val session = BrowserSession(tabs.toMap(), activeTabId)
                out.writeObject(session)
            }
            println("Session saved successfully")
        } catch (e: IOException) {
            println(e.message)
            throw BrowserHistoryException("Failed to save session: ${e.message}")
        }
    }

    /**
     * Restore  browser session
     */

    fun restoreSession(fileName: String) {
        try {
            ObjectInputStream(FileInputStream(fileName)).use { input ->
                val session = input.readObject() as BrowserSession
                tabs.clear()
                tabs.putAll(session.tabs)
                activeTabId = session.activeTabId
            }
            println("Session restored successfully")
        } catch (e: IOException) {
            println(e.message)
            throw BrowserHistoryException("Failed to restore session: ${e.message}")
        }
    }

    /**
     * Delegate current tab operations
     */
    fun getCurrentTab(): BrowserTab = tabs[activeTabId]
        ?: throw TabManagementException("No active tab")
}