package com.example.kotlin.lesson9

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.kotlin.MyApp.Companion.getHistoryDao
import com.example.kotlin.domian.room.*

private const val URI_ALL = 1 // URI для всех записей
private const val URI_ID = 2 // URI для конкретной записи
private const val ENTITY_PATH =
    "HistoryEntity" // Часть пути (будем определять путь до HistoryEntity)

class HistoryContentProvider : ContentProvider() {

    private var authorities: String? = null // Адрес URI
    private lateinit var uriMatcher: UriMatcher // Помогает определить тип адреса URI

    // Типы данных
    private var entityContentType: String? = null // Набор строк
    private var entityContentItemType: String? = null // Одна строка
    private lateinit var contentUri: Uri // Адрес URI Provider

    override fun onCreate(): Boolean {
        // Прочитаем часть пути из ресурсов
        authorities = "authorities" //context?.resources?.getString(R.string.authorities)
        // Вспомогательный класс для определения типа запроса
        uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        uriMatcher.addURI(authorities, ENTITY_PATH, URI_ALL) // Если нас интересуют все объекты
        uriMatcher.addURI(
            authorities,
            "$ENTITY_PATH/#",
            URI_ID
        )// Если нас интересует только один объект
        // Тип содержимого — все объекты
        entityContentType =
            "vnd.android.cursor.dir/vnd.$authorities.$ENTITY_PATH"
        // Тип содержимого — один объект
        entityContentItemType =
            "vnd.android.cursor.item/vnd.$authorities.$ENTITY_PATH"
        // Строка для доступа к Provider
        contentUri = Uri.parse("content://$authorities/$ENTITY_PATH")
        return true

    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?

    ): Cursor? {
        // Получаем доступ к данным
        val historyDao: HistoryDao = getHistoryDao()
        // При помощи UriMatcher определяем, запрашиваются все элементы или один
        val cursor = when (uriMatcher.match(uri)) {
            URI_ALL -> historyDao.getHistoryCursor() // Запрос к базе данных для всех элементов
            URI_ID -> {
                // Определяем id из URI адреса. Класс ContentUris помогает это сделать
                val id = ContentUris.parseId(uri)
                // Запрос к базе данных для одного элемента
                historyDao.getHistoryCursor(id)
            }
            else -> throw IllegalArgumentException("Wrong URI: $uri")
        }
        // Устанавливаем нотификацию при изменении данных в content_uri
        cursor.setNotificationUri(context!!.contentResolver, contentUri)
        return cursor
    }

    // Provider требует переопределения этого метода, чтобы понимать тип запроса
    override fun getType(uri: Uri): String? {
        when (uriMatcher.match(uri)) {
            URI_ALL -> return entityContentType
            URI_ID -> return entityContentItemType
        }
        return null

    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        require(uriMatcher.match(uri) == URI_ALL) { "Wrong URI: $uri" }
// Получаем доступ к данным
        val historyDao = getHistoryDao()

        return mapper(values)?.let { it ->
            historyDao.insert(it)
            val resultUri = ContentUris.withAppendedId(contentUri, it.id)
// Уведомляем ContentResolver, что данные по адресу resultUri        изменились
            context?.contentResolver?.notifyChange(resultUri, null)
            resultUri
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        require(uriMatcher.match(uri) == URI_ID) { "Wrong URI: $uri" }
// Получаем доступ к данным
        val historyDao = getHistoryDao()
// Получаем идентификатор записи по адресу
        val id = ContentUris.parseId(uri)
// Удаляем запись по идентификатору
        historyDao.deleteById(id)
// Нотификация на изменение Cursor
        context?.contentResolver?.notifyChange(uri, null)
        return 1

    }

    // Обновляем данные в строке таблицы
    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        require(uriMatcher.match(uri) == URI_ID) { "Wrong URI: $uri" }
// Получаем доступ к данным
        val historyDao = getHistoryDao()
        mapper(values)?.let { historyDao.update(it) }
        context!!.contentResolver.notifyChange(uri, null)
        return 1

    }

    private fun mapper(values: ContentValues?): HistoryEntity? {
        return if (values == null) {
            null// HistoryEntity()
        } else {
            val id = if (values.containsKey(ID)) values[ID] as Long else 0
            val city = values[NAME] as String
            val temperature = values[TEMPERATURE] as Int
            HistoryEntity(id, city, temperature)
        }
    }


}


