package com.example.stack_routing

import com.example.stack_routing.models.WebPage
import java.io.Serializable
import java.net.URI

class BrowserHistory : Serializable {
    /**
     * @param
     * A ‘deque’ as  is a double-ended queue that allows you to add or remove elements from both ends (rear and front).
     */

    private val backStack = ArrayDeque<WebPage>()
    private val forwardStack = ArrayDeque<WebPage>()
    private var currentPage: WebPage? = null
    private val maxHistorySize = 50
    private val visitsCount = HashMap<String, Int>()
    private val currentTimeInMillis = System.currentTimeMillis()

    companion object {
        private const val serialVersionUID: Long = 1L
    }

    fun navigateToPage(url: String, title: String) {
        currentPage?.let {
            it.leaveTime = currentTimeInMillis
            // call the push back stack
            pushToBackStack(it)
        }

        val newPage = WebPage(url, title)
        forwardStack.clear()
        currentPage = newPage

        // update visits count for domain statistics

        val domain = URI(url).host
        visitsCount[domain] = (visitsCount[domain] ?: 0) + 1

        println("Navigated to: $url")
    }

    /**
     * Function to go back in the history
     */
    fun goBack(): Boolean {
        if (backStack.isEmpty()) {
            println("Cannot go back - No previous pages")
            return false
        }
        currentPage?.let {
            it.leaveTime = currentTimeInMillis
            pushToForwardStack(it)
        }

        currentPage = backStack.removeLastOrNull()
        println("Going back to: ${currentPage?.url}")
        return true
    }

    /**
     * Function to go forward in the history
     */

    fun goForward(): Boolean {
        if (forwardStack.isEmpty()) {
            println("Cannot got ahead - No forward pages")
            return false
        }
        currentPage?.let {
            it.leaveTime = currentTimeInMillis
            pushToBackStack(it)
        }
        currentPage = forwardStack.removeLastOrNull()
        println("Going forward to: ${currentPage?.url}")
        return true
    }

    /**
     * Search history for a specific URL with time range
     */

    fun searchHistoryByTime(startTime: Long, endTime: Long): List<WebPage> {
        return (backStack + listOfNotNull(currentPage) + forwardStack)
            .filter { it.loadTime in startTime..endTime }

        /**
         * listOfNotNull ensures that currentPage is not null and added to the list
         */
    }

    /**
     * Calculate the total time spent on a specific URL
     */

    fun getSpentTimeOnPage(url: String): Long {
        return (backStack + listOfNotNull(currentPage))
            .filter { it.url == url && it.leaveTime != null }
            .sumOf { it.leaveTime!! - it.loadTime }
    }

    /**
     * Find the visited pages matching the search query
     */

    fun findVisitedPages(pattern: String): List<WebPage> {
        val regex = pattern.toRegex()
        return (backStack + listOfNotNull(currentPage) + forwardStack)
            .filter { it.url.matches(regex) }
    }

    /**
     * Get the most visited URLs within a time range
     */

    fun getMostVisitedDomains(): String? {
        return visitsCount.maxByOrNull { it.value }?.key
    }

    private fun pushToBackStack(page: WebPage) {
        if (backStack.size >= maxHistorySize) {
            backStack.removeFirstOrNull()
        }
        backStack.addLast(page)
    }


    private fun pushToForwardStack(page: WebPage) {
        if (forwardStack.size >= maxHistorySize) {
            forwardStack.removeFirstOrNull()
        }
        forwardStack.addLast(page)
    }

    fun getCurrentState(): Triple<WebPage?, List<WebPage>, List<WebPage>> {
        return Triple(currentPage, backStack.toList(), forwardStack.toList())
    }
}