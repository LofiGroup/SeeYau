package com.lofigroup.seeyau

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lofigroup.data.navigator.local.UserDao
import com.lofigroup.data.navigator.local.model.UserEntity
import com.lofigroup.seeyau.data.AppDatabase
import com.lofigroup.seeyau.data.chat.local.ChatDao
import com.lofigroup.seeyau.data.chat.local.models.ChatAssembled
import com.lofigroup.seeyau.data.chat.local.models.ChatEntity
import com.lofigroup.seeyau.data.chat.local.models.MessageEntity
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okio.IOException
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

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
    val assembledChat = runBlocking{ chatDao.getChats().first() }
    val chat = assembledChat.find { it.chat.id == 1L } ?: throw Exception("Couldn't find chat")
    val user = runBlocking { userDao.getUser(chatEntity.partnerId) ?: throw Exception("Couldn't find partner") }
    assert(
      chat == ChatAssembled(
        chat = chatEntity,
        messages = messages,
        partner = user
      )
    )
  }

  private fun populateDatabase() = runBlocking {
    userDao.insert(UserEntity(name = "Ken", id = 1, imageUrl = "", lastConnection = 0))
    userDao.insert(UserEntity(name = "Tanaka", id = 2, imageUrl = "", lastConnection = 0))

    chatEntity = ChatEntity(id = 1, partnerId = 2)
    messages = listOf(
      MessageEntity(id = 1, author = 1, chatId = 1L, createdIn = 0, message = "Hello Tanaka"),
      MessageEntity(id = 2, author = 2, chatId = 1L, createdIn = 2, message = "Hello Ken")
    )
    chatDao.insertChat(chatEntity)
    chatDao.insertMessages(messages)
  }
}