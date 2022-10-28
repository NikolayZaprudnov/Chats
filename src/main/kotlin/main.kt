import java.time.Duration
import java.time.Instant

fun main() {
    chatServise.addChat( 5)
    chatServise.addChat( 15)
    chatServise.addMessage(Chat.Message(0, "Расскажи, как у тебя дела?", null, Instant.now(),
        true), 15)
    chatServise.addMessage(Chat.Message(0, "Тестовое Сообщение", null, Instant.now(),
        true), 5)
    chatServise.getMessages(15)
    chatServise.getMessages(5)
    chatServise.addMessage(Chat.Message(0, "Тестовое Сообщение2", null, Instant.now(),
        true), 5)
    chatServise.getUnreadMessage(5)
    chatServise.deleteMessages(5, 2)
    chatServise.AllChat.forEach(::println)
    chatServise.deleteChat(15)
    chatServise.AllChat.forEach(::println)

}

data class Chat(var CompanionId: Int?, var message: MutableList<Message>, var LastTime: Instant) {
    data class Message(
        var MessageId: Int,
        var Text: String,
        val attachment: Array<Attachment>?,
        val Time: Instant,
        var UnreadMessages: Boolean,
    ) {
        open class Attachment(type: String)
        class Video(val id: Int, val name: String, val duration: Duration, val title: String)
        class VideoAttachment(val video: Video) : Attachment("video")
        class Audio(val id: Int, val name: String, val duration: Duration, val title: String)
        class AudioAttachment(val audio: Audio) : Attachment("audio")
        class Image(val id: Int, val name: String, val URL: String, val title: String)
        class ImageAttachment(val image: Image) : Attachment("image")
        class File(val id: Int, val name: String, val formate: String, val title: String)
        class FileAttachment(val file: File) : Attachment("file")
        class Sticker(val id: Int, val imageURL: String)
        class StickerAttachment(val sticker: Sticker) : Attachment("sticker")
    }
}

object chatServise {

    var AllChat = mutableListOf<Chat>()
    fun addChat( companionId: Int): Chat {
        val chat = Chat(null, mutableListOf(Chat.Message(
            1, "Привет", null, Instant.now(), true
        )), Instant.now())
        chat.CompanionId = companionId
        AllChat += chat
        return AllChat.last()
    }

    fun addMessage(message: Chat.Message, CompanionId: Int): Chat.Message {
        message.MessageId = findChatById(CompanionId)?.message!!.lastIndex + 2
        message.UnreadMessages = true
        findChatById(CompanionId)?.message?.plusAssign(message)
        findChatById(CompanionId)?.LastTime = Instant.now()
        return findChatById(CompanionId)?.message!!.last()
    }

    fun getMessages(CompanionId: Int) {
        findChatById(CompanionId)?.message
            ?.onEach { it.UnreadMessages = false }
            ?.forEach(::println)
    }

    fun getUnreadMessage(CompanionId: Int) {
        var UnreadMessages = findChatById(CompanionId)?.message
            ?.filter(fun(message: Chat.Message) = message.UnreadMessages === true)
            ?.onEach { it.UnreadMessages = false }
        println(UnreadMessages)
    }

    fun deleteMessages(CompanionId: Int, MessageId: Int) {
        findChatById(CompanionId)?.message?.remove(findMessageById(MessageId, CompanionId))
    }

    fun deleteChat(CompanionId: Int) {
        AllChat.remove(findChatById(CompanionId))
    }

    fun findChatById(id: Int): Chat? {
        var result = false
        for (Chat in AllChat) {
            if (Chat.CompanionId === id) {
                result = true
                return Chat
            }
        }
        if (result === false) {
            throw ChatNotFoundException("Не найден Элемент с номером ID $id")
        }
        return null
    }

    fun findMessageById(MessageId: Int, CompanionId: Int): Chat.Message? {
        var result = false
        for (message in findChatById(CompanionId)?.message!!) {
            if (message.MessageId === MessageId) {
                result = true
                return message
            }
        }
        if (result === false) {
            throw ChatNotFoundException("Не найден Элемент с номером ID $MessageId")
        }
        return null
    }
}


class ChatNotFoundException(message: String) : RuntimeException(message)