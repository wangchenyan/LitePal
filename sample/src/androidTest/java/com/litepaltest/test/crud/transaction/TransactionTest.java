package com.litepaltest.test.crud.transaction;

import android.content.ContentValues;
import androidx.test.filters.SmallTest;

import com.litepaltest.model.Book;
import com.litepaltest.model.Cellphone;
import com.litepaltest.model.Student;
import com.litepaltest.model.Teacher;
import com.litepaltest.model.WeiboMessage;
import com.litepaltest.test.LitePalTestCase;

import org.junit.Assert;
import org.junit.Test;
import org.litepal.copy.LitePalCopy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertTrue;

@SmallTest
public class TransactionTest extends LitePalTestCase {

    @Test
    public void testTransactionForSave() {
        LitePalCopy.beginTransaction();
        Book book = new Book();
        try {
            book.setBookName("First Line of Android");
            book.setPages(700);
            Assert.assertTrue(book.save());
            Book bookFromDb = LitePalCopy.find(Book.class, book.getId());
            Assert.assertEquals("First Line of Android", bookFromDb.getBookName());
            Assert.assertEquals(700L, bookFromDb.getPages().intValue());
            if (true) {
                throw new NullPointerException("Throw a exception to fail the transaction.");
            }
            LitePalCopy.setTransactionSuccessful();
        } catch (Exception e) {
            // do nothing
        } finally {
            LitePalCopy.endTransaction();
        }
        Assert.assertTrue(book.isSaved());
        Book bookFromDb = LitePalCopy.find(Book.class, book.getId());
        Assert.assertNull(bookFromDb);
    }

    @Test
    public void testTransactionForSaveAll() {
        LitePalCopy.beginTransaction();
        String serial = UUID.randomUUID().toString();
        WeiboMessage weiboMessage = new WeiboMessage();
        try {
            weiboMessage.setFollower("nobody");
            boolean saveResult = weiboMessage.save();
            List<Cellphone> cellphones = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                Cellphone cellphone = new Cellphone();
                cellphone.setBrand("Apple");
                cellphone.setSerial(serial + (i % 10)); // serial is unique, so this should save failed
                cellphone.getMessages().add(weiboMessage);
                cellphones.add(cellphone);
            }
            boolean saveAllResult = LitePalCopy.saveAll(cellphones);
            if (saveResult && saveAllResult) {
                LitePalCopy.setTransactionSuccessful();
            }
        } finally {
            LitePalCopy.endTransaction();
        }
        Assert.assertTrue(weiboMessage.isSaved());
        WeiboMessage messageFromDb = LitePalCopy.find(WeiboMessage.class, weiboMessage.getId());
        Assert.assertNull(messageFromDb);
        List<Cellphone> list = LitePalCopy.where("serial like ?", serial + "%").find(Cellphone.class);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testTransactionForUpdate() {
        Teacher teacher = new Teacher();
        teacher.setTeacherName("Tony");
        teacher.setTeachYears(3);
        teacher.setAge(23);
        teacher.setSex(false);
        Assert.assertTrue(teacher.save());
        LitePalCopy.beginTransaction();
        ContentValues values = new ContentValues();
        values.put("TeachYears", 13);
        int rows = LitePalCopy.update(Teacher.class, values, teacher.getId());
        Assert.assertEquals(1, rows);
        Teacher teacherFromDb = LitePalCopy.find(Teacher.class, teacher.getId());
        Assert.assertEquals(13, teacherFromDb.getTeachYears());
        // not set transaction successful
        LitePalCopy.endTransaction();
        teacherFromDb = LitePalCopy.find(Teacher.class, teacher.getId());
        Assert.assertEquals(3, teacherFromDb.getTeachYears());
    }

    @Test
    public void testTransactionForDelete() {
        Student tony = new Student();
        tony.setName("Tony");
        tony.setAge(23);
        tony.save();
        int studentId = tony.getId();
        LitePalCopy.beginTransaction();
        int rowsAffected = tony.delete();
        Assert.assertEquals(1, rowsAffected);
        Student studentFromDb = LitePalCopy.find(Student.class, studentId);
        Assert.assertNull(studentFromDb);
        // not set transaction successful
        LitePalCopy.endTransaction();
        studentFromDb = LitePalCopy.find(Student.class, studentId);
        Assert.assertNotNull(studentFromDb);
        Assert.assertEquals("Tony", studentFromDb.getName());
        Assert.assertEquals(23, studentFromDb.getAge());
    }

    @Test
    public void testTransactionForCRUD() {
        LitePalCopy.beginTransaction();
        Student tony = new Student();
        tony.setName("Tony");
        tony.setAge(23);
        tony.save();
        int studentId = tony.getId();
        Student studentFromDb = LitePalCopy.find(Student.class, studentId);
        Assert.assertNotNull(studentFromDb);
        Assert.assertEquals("Tony", studentFromDb.getName());
        Assert.assertEquals(23, studentFromDb.getAge());
        Student updateModel = new Student();
        updateModel.setAge(25);
        int rowsAffected = updateModel.update(studentId);
        Assert.assertEquals(1, rowsAffected);
        studentFromDb = LitePalCopy.find(Student.class, studentId);
        Assert.assertEquals(25, studentFromDb.getAge());
        rowsAffected = tony.delete();
        Assert.assertEquals(1, rowsAffected);
        studentFromDb = LitePalCopy.find(Student.class, studentId);
        Assert.assertNull(studentFromDb);
        Assert.assertTrue(tony.save());
        studentFromDb = LitePalCopy.find(Student.class, tony.getId());
        Assert.assertNotNull(studentFromDb);
        // not set transaction successful
        LitePalCopy.endTransaction();
        studentFromDb = LitePalCopy.find(Student.class, tony.getId());
        Assert.assertNull(studentFromDb);
    }

    @Test
    public void testTransactionSuccessfulForCRUD() {
        LitePalCopy.beginTransaction();
        Student tony = new Student();
        tony.setName("Tony");
        tony.setAge(23);
        tony.save();
        int studentId = tony.getId();
        Student studentFromDb = LitePalCopy.find(Student.class, studentId);
        Assert.assertNotNull(studentFromDb);
        Assert.assertEquals("Tony", studentFromDb.getName());
        Assert.assertEquals(23, studentFromDb.getAge());
        Student updateModel = new Student();
        updateModel.setAge(25);
        int rowsAffected = updateModel.update(studentId);
        Assert.assertEquals(1, rowsAffected);
        studentFromDb = LitePalCopy.find(Student.class, studentId);
        Assert.assertEquals(25, studentFromDb.getAge());
        rowsAffected = tony.delete();
        Assert.assertEquals(1, rowsAffected);
        studentFromDb = LitePalCopy.find(Student.class, studentId);
        Assert.assertNull(studentFromDb);
        Assert.assertTrue(tony.save());
        studentFromDb = LitePalCopy.find(Student.class, tony.getId());
        Assert.assertNotNull(studentFromDb);
        LitePalCopy.setTransactionSuccessful();
        LitePalCopy.endTransaction();
        studentFromDb = LitePalCopy.find(Student.class, tony.getId());
        Assert.assertNotNull(studentFromDb);
        Assert.assertEquals("Tony", studentFromDb.getName());
        Assert.assertEquals(23, studentFromDb.getAge());
    }

}
