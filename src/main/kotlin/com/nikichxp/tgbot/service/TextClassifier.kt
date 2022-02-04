package com.nikichxp.tgbot.service

import org.springframework.stereotype.Service
import org.yaml.snakeyaml.Yaml
import java.io.File

@Service
class TextClassifier {

    private val positive: List<String>
    private val negative: List<String>

    init {
        val content: String = try {
            File(System.getProperty("user.dir") + "/src/main/resources/dictionary.yaml").readText()
        } catch (e: Exception) {
            ""
        }

        val yaml = Yaml()
        val obj = yaml.load<Map<String, List<String>>>(content)
        positive = obj["positive"] ?: listOf()
        negative = obj["negative"] ?: listOf()
    }

    // TODO когда-нибудь я доберусь сюда и будет збс определение силы эмоции человека
    //      может даже с машинным обучением (нет)
    fun getReaction(text: String): Double {
        val trimText = text.trim()
        positive.forEach {
            if (trimText.startsWith(it)) {
                return 1.0
            }
        }
        negative.forEach {
            if (trimText.startsWith(it)) {
                return -1.0
            }
        }
        return when {
            trimText.startsWith('+') -> 1.0
            trimText.startsWith('-') -> -1.0
            else -> 0.0
        }
    }
}