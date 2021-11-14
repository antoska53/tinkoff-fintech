package ru.myacademyhomework.tinkoffmessenger.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Subscription (
    @SerialName("name")
    val name: String,

    @SerialName("stream_id")
    val streamID: Long,

    @SerialName("description")
    val description: String,

    @SerialName("rendered_description")
    val renderedDescription: String,

    @SerialName("invite_only")
    val inviteOnly: Boolean,

    @SerialName("is_web_public")
    val isWebPublic: Boolean,

    @SerialName("stream_post_policy")
    val streamPostPolicy: Int,

    @SerialName("history_public_to_subscribers")
    val historyPublicToSubscribers: Boolean,

    @SerialName("first_message_id")
    val firstMessageID: Int,

    @SerialName("message_retention_days")
    val messageRetentionDays: Int?,

    @SerialName("date_created")
    val dateCreated: Int,

    val color: String,

    @SerialName("is_muted")
    val isMuted: Boolean,

    @SerialName("pin_to_top")
    val pinToTop: Boolean,

    @SerialName("audible_notifications")
    val audibleNotifications: Boolean?,

    @SerialName("desktop_notifications")
    val desktopNotifications: Boolean?,

    @SerialName("email_notifications")
    val emailNotifications: Boolean?,

    @SerialName("push_notifications")
    val pushNotifications: Boolean?,

    @SerialName("wildcard_mentions_notify")
    val wildcardMentionsNotify: Boolean?,

    @SerialName("role")
    val role: Int,

    @SerialName("in_home_view")
    val inHomeView: Boolean,

    @SerialName("is_announcement_only")
    val isAnnouncementOnly: Boolean,

    @SerialName("stream_weekly_traffic")
    val streamWeeklyTraffic: Int?,

    @SerialName("email_address")
    val emailAddress: String,

)
