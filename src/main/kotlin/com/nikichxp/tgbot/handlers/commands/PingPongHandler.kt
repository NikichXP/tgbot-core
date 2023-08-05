package com.nikichxp.tgbot.handlers.commands

import com.nikichxp.tgbot.dto.Update
import com.nikichxp.tgbot.service.menu.CommandHandler
import com.nikichxp.tgbot.service.tgapi.TgOperations
import org.springframework.stereotype.Component

@Component
class PingPongHandler(
    private val tgOperations: TgOperations
) : CommandHandler {

    override fun isCommandSupported(command: String): Boolean = command == "/ping"

    override fun processCommand(args: List<String>, update: Update): Boolean {
        tgOperations.replyToCurrentMessage("pong!", update)
        return true
    }
}