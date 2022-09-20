package com.riteshmaagadh.quotesapp.data.notifications

import java.util.*

object NotificationQuotes {

    fun getNotificationQuote() : String {
        val list: List<String> = listOf(
            "You can get everything in life you want if you will just help enough other people get what they want.",
            "Inspiration does exist, but it must find you working.",
            "Don't settle for average. Bring your best to the moment. Then, whether it fails or succeeds, at least you know you gave all you had.",
            "Show up, show up, show up, and after a while the muse shows up, too.",
            "Don't bunt. Aim out of the ballpark. Aim for the company of immortals.",
            "I have stood on a mountain of no’s for one yes.",
            "If you believe something needs to exist, if it's something you want to use yourself, don't let anyone ever stop you from doing it.",
            "First forget inspiration. Habit is more dependable. Habit will sustain you whether you're inspired or not. Habit will help you finish and polish your stories. Inspiration won't. Habit is persistence in practice.",
            "The best way out is always through.",
            "The battles that count aren't the ones for gold medals. The struggles within yourself—the invisible, inevitable battles inside all of us—that's where it's at.",
            "If there is no struggle, there is no progress.",
            "Someone will declare, “I am the leader!” and expect everyone to get in line and follow him or her to the gates of heaven or hell. My experience is that it doesn’t happen that way. Others follow you based on the quality of your actions rather than the magnitude of your declarations.",
            "Courage is like a muscle. We strengthen it by use.",
            "Develop success from failures. Discouragement and failure are two of the surest stepping stones to success.",
            "Relentlessly prune bullshit, don't wait to do things that matter, and savor the time you have. That's what you do when life is short.",
            "More is lost by indecision than wrong decision."
        )
        return list[Random().nextInt(list.size)]
    }

}