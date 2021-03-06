package net.treelzebub.readerbility.api.model

import org.joda.time.LocalDate

/**
 * Created by Tre Murillo on 1/1/16
 */
data class User(
        val username: String,
        val firstName: String,
        val lastName: String,
        val dateJoined: LocalDate,
        val hasActiveSubscription: Boolean,
        val readingLimit: Int?,
        val emailIntoAddress: String?,
        val kindleEmailAddress: String?,
        val tags: List<Tag>
)
