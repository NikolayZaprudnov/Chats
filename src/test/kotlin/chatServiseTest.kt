import org.junit.Test

import org.junit.Assert.*
import java.time.Instant

class chatServiseTest {

    @Test
    fun addChat() {
        chatServise.addChat(5)
        assert(chatServise.AllChat.isNotEmpty())
    }

    @Test
    fun addMessage() {
        chatServise.addChat(5)
        chatServise.addMessage(Chat.Message(
            0, "Hello", null, Instant.now(), true), 5)
        assert(chatServise.findChatById(5)!!.message.size  === 2)
    }

    @Test
    fun deleteMessages() {
        chatServise.addChat(5)
        chatServise.addMessage(Chat.Message(
            0, "Hello", null, Instant.now(), true), 5)
        chatServise.deleteMessages(5, 2)
        assert(chatServise.findChatById(5)!!.message.size  === 1)
    }

    @Test(expected = ChatNotFoundException::class)
    fun ChatNotFoundException() {
        chatServise.findChatById(5)
    }
}