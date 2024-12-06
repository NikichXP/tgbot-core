package com.nikichxp.tgbot.childcarebot

import com.nikichxp.tgbot.core.dto.Update
import com.nikichxp.tgbot.core.entity.TgBot
import com.nikichxp.tgbot.core.entity.UpdateMarker
import com.nikichxp.tgbot.core.handlers.UpdateHandler
import com.nikichxp.tgbot.core.handlers.commands.CommandHandler
import com.nikichxp.tgbot.core.handlers.commands.HandleCommand
import com.nikichxp.tgbot.core.service.tgapi.TgOperations
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import org.springframework.data.mongodb.core.findAll


@Service
class ChildCareCommandHandler(
    private val tgOperations: TgOperations
) : CommandHandler, UpdateHandler {
    override fun supportedBots(): Set<TgBot> = setOf(TgBot.NIKICHBOT)

    @HandleCommand("/status")
    suspend fun status() {
        tgOperations.sendMessage {
            replyToCurrentMessage()
            text = "I'm alive!"
            withKeyboard(listOf(listOf("Уснула", "Ест")))
        }
    }

    override fun botSupported(bot: TgBot): Boolean = bot == TgBot.NIKICHBOT

    override fun getMarkers() = setOf(UpdateMarker.MESSAGE_IN_CHAT)

    override suspend fun handleUpdate(update: Update) {
        tgOperations.sendMessage {
            replyToCurrentMessage()
            text = "handling update!"
        }
    }
}


enum class ChildActivity {
    SLEEP, WAKE_UP, EATING
}

data class ChildActivityEvent(
    val activity: ChildActivity,
    val date: LocalDateTime
) {
    lateinit var id: ObjectId
}

@Service
class ChildActivityService(
    private val mongoTemplate: MongoTemplate
) {

    fun addActivity(activity: ChildActivity) {
        val event = ChildActivityEvent(activity, LocalDateTime.now())
        mongoTemplate.save(event)
    }

    fun getActivities(): List<ChildActivityEvent> {
        return mongoTemplate.findAll()
    }

}