package com.litepaltest.test.crud.query

import androidx.test.filters.SmallTest
import com.litepaltest.model.Student
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.fail
import org.junit.Before
import org.junit.Test
import org.litepal.copy.LitePalCopy
import org.litepal.copy.extension.*
import org.litepal.copy.util.DBUtility

@SmallTest
class QueryMathKotlinTest {

    private var studentTable: String? = null

    @Before
    fun setUp() {
        studentTable = DBUtility.getTableNameByClassName(Student::class.java.name)
    }

    @Test
    fun testCount() {
        var result = LitePalCopy.count<Student>()
        var realResult = -100
        var cursor = LitePalCopy.findBySQL("select count(1) from " + studentTable!!)
        if (cursor!!.moveToFirst()) {
            realResult = cursor.getInt(0)
        }
        cursor.close()
        assertEquals(realResult, result)
        result = LitePalCopy.where("id > ?", "99").count(studentTable)
        cursor = LitePalCopy.findBySQL("select count(1) from $studentTable where id > ?", "99")
        if (cursor!!.moveToFirst()) {
            realResult = cursor.getInt(0)
        }
        cursor.close()
        assertEquals(realResult, result)
        try {
            LitePalCopy.count("nosuchtable")
            fail()
        } catch (e: Exception) {
        }

    }

    @Test
    fun testAverage() {
        var result = LitePalCopy.average<Student>("age")
        var realResult = -100.0
        var cursor = LitePalCopy.findBySQL("select avg(age) from " + studentTable!!)
        if (cursor!!.moveToFirst()) {
            realResult = cursor.getDouble(0)
        }
        cursor.close()
        assertEquals(realResult, result)
        result = LitePalCopy.where("id > ?", "99").average(studentTable, "age")
        cursor = LitePalCopy.findBySQL("select avg(age) from $studentTable where id > ?", "99")
        if (cursor!!.moveToFirst()) {
            realResult = cursor.getDouble(0)
        }
        cursor.close()
        assertEquals(realResult, result)
        try {
            LitePalCopy.average<Student>("nosuchcolumn")
            fail()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @Test
    fun testMax() {
        var result = LitePalCopy.max<Student, Int>("age")
        var realResult = -100
        var cursor = LitePalCopy.findBySQL("select max(age) from " + studentTable!!)
        if (cursor!!.moveToFirst()) {
            realResult = cursor.getInt(0)
        }
        cursor.close()
        assertEquals(realResult, result)
        result = LitePalCopy.where("age < ?", "20").max(studentTable!!, "age")
        cursor = LitePalCopy.findBySQL("select max(age) from $studentTable where age < ?", "20")
        if (cursor!!.moveToFirst()) {
            realResult = cursor.getInt(0)
        }
        cursor.close()
        assertEquals(realResult, result)
    }

    @Test
    fun testMin() {
        var result = LitePalCopy.min<Student, Int>("age")
        var realResult = -100
        var cursor = LitePalCopy.findBySQL("select min(age) from " + studentTable!!)
        if (cursor!!.moveToFirst()) {
            realResult = cursor.getInt(0)
        }
        cursor.close()
        assertEquals(realResult, result)
        result = LitePalCopy.where("age > ?", "10").min(studentTable!!, "age")
        cursor = LitePalCopy.findBySQL("select min(age) from $studentTable where age > ?", "10")
        if (cursor!!.moveToFirst()) {
            realResult = cursor.getInt(0)
        }
        cursor.close()
        assertEquals(realResult, result)
    }

    @Test
    fun testSum() {
        var result = LitePalCopy.sum<Student, Int>("age")
        var realResult = -100
        var cursor = LitePalCopy.findBySQL("select sum(age) from " + studentTable!!)
        if (cursor!!.moveToFirst()) {
            realResult = cursor.getInt(0)
        }
        cursor.close()
        assertEquals(realResult, result)
        result = LitePalCopy.where("age > ?", "15").sum(studentTable!!, "age")
        cursor = LitePalCopy.findBySQL("select sum(age) from $studentTable where age > ?", "15")
        if (cursor!!.moveToFirst()) {
            realResult = cursor.getInt(0)
        }
        cursor.close()
        assertEquals(realResult, result)
    }

}