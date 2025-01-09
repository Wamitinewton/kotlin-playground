package com.example.stack_routing

fun main() {
    val browser = Browser()

    println("===== Enhanced Browser History Application =====")

    // Demonstrate basic navigation
    with(browser.getCurrentTab().history) {
        navigateToPage("https://www.google.com", "Google")
        Thread.sleep(5000)
        navigateToPage("https://www.wikipedia.org", "Wikipedia")
        Thread.sleep(5000)
        navigateToPage("https://www.github.com", "Github")
    }

    // Demonstrate tab management
    val secondTabId = browser.createNewTab()
    Thread.sleep(3000)
    browser.switchTab(secondTabId)
    browser.getCurrentTab().history.navigateToPage("https://www.linkedin.com", "LinkedIn")

    // Demonstrate URL pattern matching
    val githubPages = browser.getCurrentTab().history
        .findVisitedPages(".*github\\.com")
    Thread.sleep(3000)
    println("\nGitHub pages visited: ${githubPages.map { it.url }}")

    //Demonstrate time-based operations
    val startTime = System.currentTimeMillis() - 10000
    val endTime = System.currentTimeMillis()
    val recentPages = browser.getCurrentTab().history
        .searchHistoryByTime(startTime, endTime)
    Thread.sleep(3000)
    println("\nRecent pages: ${recentPages.map { it.url }}")

    // Save and restore session
    browser.saveSession("browser_session.dat")
    browser.restoreSession("browser_session.dat")

    // show most visited domain
    val mostVisited = browser.getCurrentTab().history.getMostVisitedDomains()
    println("\nMost visited domain: $mostVisited")
}