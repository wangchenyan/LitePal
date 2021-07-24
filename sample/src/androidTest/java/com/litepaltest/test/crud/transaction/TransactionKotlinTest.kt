package com.litepaltest.test.crud.transaction

import android.content.ContentValues
import androidx.test.filters.SmallTest
import com.litepaltest.model.*
import com.litepaltest.test.LitePalTestCase
import junit.framework.TestCase
import org.junit.Assert
import org.junit.Test
import org.litepal.copy.LitePalCopy
import org.litepal.copy.extension.find
import org.litepal.copy.extension.runInTransaction
import java.lang.NullPointerException
import java.util.*

@SmallTest
class TransactionKotlinTest : LitePalTestCase() {

    @Test
    fun testTransactionForSave() {
        val book = Book()
        LitePalCopy.runInTransaction {
            book.bookName = "First Line of Android"
            book.pages = 700
            Assert.assertTrue(book.save())
            val bookFromDb = LitePalCopy.find(Book::class.java, book.id)
            Assert.assertEquals("First Line of Android", bookFromDb.bookName)
            Assert.assertEquals(700L, bookFromDb.pages.toInt().toLong())
            false
        }
        Assert.assertTrue(book.isSaved)
        val bookFromDb = LitePalCopy.find(Book::class.java, book.id)
        Assert.assertNull(bookFromDb)
    }

    @Test
    fun testTransactionForSaveAll() {
        val serial = UUID.randomUUID().toString()
        val weiboMessage = WeiboMessage()
        LitePalCopy.runInTransaction {
            weiboMessage.follower = "nobody"
            val saveResult = weiboMessage.save()
            val cellphones: MutableList<Cellphone> = ArrayList()
            for (i in 0..19) {
                val cellphone = Cellphone()
                cellphone.setBrand("Apple")
                cellphone.serial = serial + i % 10 // serial is unique, so this should save failed
                cellphone.messages.add(weiboMessage)
                cellphones.add(cellphone)
            }
            val saveAllResult = LitePalCopy.saveAll(cellphones)
            saveResult && saveAllResult
        }
        Assert.assertTrue(weiboMessage.isSaved)
        val messageFromDb = LitePalCopy.find(WeiboMessage::class.java, weiboMessage.id.toLong())
        Assert.assertNull(messageFromDb)
        val list = LitePalCopy.where("serial like ?", "$serial%").find(Cellphone::class.java)
        TestCase.assertTrue(list.isEmpty())
    }

    @Test
    fun testTransactionForUpdate() {
        val teacher = Teacher()
        teacher.teacherName = "Tony"
        teacher.teachYears = 3
        teacher.age = 23
        teacher.isSex = false
        Assert.assertTrue(teacher.save())
        LitePalCopy.runInTransaction {
            val values = ContentValues()
            values.put("TeachYears", 13)
            val rows = LitePalCopy.update(Teacher::class.java, values, teacher.id.toLong())
            Assert.assertEquals(1, rows.toLong())
            val teacherFromDb = LitePalCopy.find(Teacher::class.java, teacher.id.toLong())
            Assert.assertEquals(13, teacherFromDb.teachYears.toLong())
            // not set transaction successful
            false
        }
        val teacherFromDb = LitePalCopy.find(Teacher::class.java, teacher.id.toLong())
        Assert.assertEquals(3, teacherFromDb.teachYears.toLong())
    }

    @Test
    fun testTransactionForDelete() {
        val tony = Student()
        tony.name = "Tony"
        tony.age = 23
        tony.save()
        val studentId = tony.id
        LitePalCopy.runInTransaction {
            val rowsAffected = tony.delete()
            Assert.assertEquals(1, rowsAffected.toLong())
            val studentFromDb = LitePalCopy.find<Student>(studentId.toLong())
            Assert.assertNull(studentFromDb)
            // not set transaction successful
            false
        }
        val studentFromDb = LitePalCopy.find<Student>(studentId.toLong())
        Assert.assertNotNull(studentFromDb)
        Assert.assertEquals("Tony", studentFromDb!!.name)
        Assert.assertEquals(23, studentFromDb.age.toLong())
    }

    @Test
    fun testTransactionForCRUD() {
        var lastId = -1
        LitePalCopy.runInTransaction {
            val tony = Student()
            tony.name = "Tony"
            tony.age = 23
            tony.save()
            val studentId = tony.id
            var studentFromDb = LitePalCopy.find<Student>(studentId.toLong())
            Assert.assertNotNull(studentFromDb)
            Assert.assertEquals("Tony", studentFromDb!!.name)
            Assert.assertEquals(23, studentFromDb.age.toLong())
            val updateModel = Student()
            updateModel.age = 25
            var rowsAffected = updateModel.update(studentId.toLong())
            Assert.assertEquals(1, rowsAffected.toLong())
            studentFromDb = LitePalCopy.find(Student::class.java, studentId.toLong())
            Assert.assertEquals(25, studentFromDb.age.toLong())
            rowsAffected = tony.delete()
            Assert.assertEquals(1, rowsAffected.toLong())
            studentFromDb = LitePalCopy.find(Student::class.java, studentId.toLong())
            Assert.assertNull(studentFromDb)
            Assert.assertTrue(tony.save())
            studentFromDb = LitePalCopy.find(Student::class.java, tony.id.toLong())
            Assert.assertNotNull(studentFromDb)
            lastId = tony.id
            // not set transaction successful
            false
        }
        val studentFromDb = LitePalCopy.find<Student>(lastId.toLong())
        Assert.assertNull(studentFromDb)
    }

    @Test
    fun testTransactionSuccessfulForCRUD() {
        var lastId = -1
        LitePalCopy.runInTransaction {
            val tony = Student()
            tony.name = "Tony"
            tony.age = 23
            tony.save()
            val studentId = tony.id
            var studentFromDb = LitePalCopy.find<Student>(studentId.toLong())
            Assert.assertNotNull(studentFromDb)
            Assert.assertEquals("Tony", studentFromDb!!.name)
            Assert.assertEquals(23, studentFromDb.age.toLong())
            val updateModel = Student()
            updateModel.age = 25
            var rowsAffected = updateModel.update(studentId.toLong())
            Assert.assertEquals(1, rowsAffected.toLong())
            studentFromDb = LitePalCopy.find(Student::class.java, studentId.toLong())
            Assert.assertEquals(25, studentFromDb.age.toLong())
            rowsAffected = tony.delete()
            Assert.assertEquals(1, rowsAffected.toLong())
            studentFromDb = LitePalCopy.find(Student::class.java, studentId.toLong())
            Assert.assertNull(studentFromDb)
            Assert.assertTrue(tony.save())
            studentFromDb = LitePalCopy.find(Student::class.java, tony.id.toLong())
            Assert.assertNotNull(studentFromDb)
            lastId = tony.id
            // not set transaction successful
            true
        }
        val studentFromDb = LitePalCopy.find<Student>(lastId.toLong())
        Assert.assertNotNull(studentFromDb)
        Assert.assertEquals("Tony", studentFromDb!!.name)
        Assert.assertEquals(23, studentFromDb.age.toLong())
    }

    @Test
    fun testTransactionWithException() {
        val book = Book()
        LitePalCopy.runInTransaction {
            book.bookName = "First Line of Android"
            book.pages = 700
            Assert.assertTrue(book.save())
            val bookFromDb = LitePalCopy.find(Book::class.java, book.id)
            Assert.assertEquals("First Line of Android", bookFromDb.bookName)
            Assert.assertEquals(700L, bookFromDb.pages.toInt().toLong())
            if (true) throw NullPointerException("just throw to fail the transaction")
            true
        }
        Assert.assertTrue(book.isSaved)
        val bookFromDb = LitePalCopy.find(Book::class.java, book.id)
        Assert.assertNull(bookFromDb)
    }

}