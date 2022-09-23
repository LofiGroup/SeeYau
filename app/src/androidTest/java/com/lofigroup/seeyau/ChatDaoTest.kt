package com.lofigroup.seeyau

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lofigroup.seeyau.data.profile.local.UserDao
import com.lofigroup.seeyau.data.AppDatabase
import com.lofigroup.seeyau.data.chat.local.ChatDao
import com.lofigroup.seeyau.data.chat.local.models.ChatAssembled
import com.lofigroup.seeyau.data.chat.local.models.ChatEntity
import com.lofigroup.seeyau.data.chat.local.models.MessageEntity
import com.lofigroup.seeyau.data.profile.local.model.UserEntity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class ChatDaoTest {

  private lateinit var chatDao: ChatDao
  private lateinit var userDao: UserDao
  private var db: AppDatabase? = null

  private lateinit var chatEntity: ChatEntity
  private lateinit var messages: List<MessageEntity>

  @Before
  fun createDb() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
    chatDao = db!!.chatDao
    userDao = db!!.userDao
    populateDatabase()
  }

  @After
  @Throws(IOException::class)
  fun closeDb() {
    db?.close()
  }

  @Test
  @Throws(Exception::class)
  fun readAssembledChat() {
    val assembledChat = runBlocking { chatDao.getChats().first() }
    val chat = assembledChat.find { it.chat.id == 1L } ?: throw Exception("Couldn't find chat")
    val user = runBlocking {
      userDao.getUser(chatEntity.partnerId) ?: throw Exception("Couldn't find partner")
    }
    assert(
      chat == ChatAssembled(
        chat = chatEntity,
        messages = messages,
        partner = user
      )
    )
  }

  @Test()
  @Throws(Exception::class)
  fun assembledChatFunction_messagesIsSorted() {
    println("Creating random messages")
    runBlocking {
      chatDao.insertMessages(listOf(
        createMessage(),
        createMessage(),
        createMessage(createdIn = 5000L)
      ))
    }
    println("Messages successfully have been created!")

    val assembledChat = runBlocking {
      chatDao.getAssembledChat(1).first()
    }

    println("Got assembledChat: $assembledChat")

    var lastCreatedIn = System.currentTimeMillis()
    for (message in assembledChat.messages) {
      println("    Message createdIn: ${message.createdIn}")
      assert(message.createdIn <= lastCreatedIn)
      lastCreatedIn = message.createdIn
    }
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test()
  @Throws(Exception::class)
  fun assembledChatFunction_isUpdatedWhenNewMessageIsInserted() = runTest {
    var result: ChatAssembled = chatDao.getAssembledChat(1L).first()

    val collectJob = launch(UnconfinedTestDispatcher()) {
      chatDao.getAssembledChat(1L).collect() {
        print("Update:\n    $it")
        result = it.copy()
      }
    }

    val newMessage = createMessage()
    val expectedResult = result.copy(
      messages = result.messages.plus(newMessage).sortedBy { it.createdIn }
    )
    chatDao.insertMessage(newMessage)
    assert(expectedResult == result)

    collectJob.cancel()
  }


  private fun populateDatabase() = runBlocking {
    userDao.insert(UserEntity(name = "Ken", id = 0, imageUrl = "", lastConnection = 0))
    userDao.insert(UserEntity(name = "Tanaka", id = 2, imageUrl = "", lastConnection = 0))

    chatEntity = ChatEntity(id = 1, partnerId = 2, lastVisited = 0L, partnerLastVisited = 0L)
    messages = listOf(
      createMessage(),
      createMessage(authorId = 2)
    )
    chatDao.insertChat(chatEntity)
    chatDao.insertMessages(messages)
  }

  private fun createMessage(
    authorId: Long = 0L,
    chatId: Long = 1L,
    createdIn: Long = System.currentTimeMillis(),
    message: String? = null
  ) = MessageEntity(
    id = getNextMessageId(),
    chatId = chatId,
    createdIn = createdIn,
    message = message ?: getRandomMessage(),
    author = authorId
  )

  companion object {
    private var messagesId = 1L
    private val messagesList = listOf("Hello", "How are you?", "Bye!")

    private fun getNextMessageId(): Long {
      return messagesId++
    }

    private fun getRandomMessage(): String {
      return messagesList[Random.nextInt(messagesList.size)]
    }
  }
}