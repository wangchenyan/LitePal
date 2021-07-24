package com.litepaltest.test.crud.query;

import android.database.Cursor;
import androidx.test.filters.SmallTest;

import com.litepaltest.model.Student;

import org.junit.Before;
import org.junit.Test;
import org.litepal.copy.LitePalCopy;
import org.litepal.copy.util.DBUtility;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;


@SmallTest
public class QueryMathTest {

    String studentTable;

    @Before
    public void setUp() {
        studentTable = DBUtility.getTableNameByClassName(Student.class.getName());
    }

    @Test
    public void testCount() {
		int result = LitePalCopy.count(Student.class);
		int realResult = -100;
		Cursor cursor = LitePalCopy.findBySQL("select count(1) from " + studentTable);
		if (cursor.moveToFirst()) {
			realResult = cursor.getInt(0);
		}
		cursor.close();
		assertEquals(realResult, result);
		result = LitePalCopy.where("id > ?", "99").count(studentTable);
		cursor = LitePalCopy.findBySQL("select count(1) from " + studentTable + " where id > ?", "99");
		if (cursor.moveToFirst()) {
			realResult = cursor.getInt(0);
		}
		cursor.close();
		assertEquals(realResult, result);
		try {
            LitePalCopy.count("nosuchtable");
			fail();
		} catch (Exception ignored) {
		}
	}

	@Test
	public void testAverage() {
		double result = LitePalCopy.average(Student.class, "age");
		double realResult = -100;
		Cursor cursor = LitePalCopy.findBySQL("select avg(age) from " + studentTable);
		if (cursor.moveToFirst()) {
			realResult = cursor.getDouble(0);
		}
		cursor.close();
		assertEquals(realResult, result);
		result = LitePalCopy.where("id > ?", "99").average(studentTable, "age");
		cursor = LitePalCopy.findBySQL("select avg(age) from " + studentTable + " where id > ?", "99");
		if (cursor.moveToFirst()) {
			realResult = cursor.getDouble(0);
		}
		cursor.close();
		assertEquals(realResult, result);
		try {
            LitePalCopy.average(Student.class, "nosuchcolumn");
			fail();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testMax() {
		int result = LitePalCopy.max(Student.class, "age", Integer.TYPE);
		int realResult = -100;
		Cursor cursor = LitePalCopy.findBySQL("select max(age) from " + studentTable);
		if (cursor.moveToFirst()) {
			realResult = cursor.getInt(0);
		}
		cursor.close();
		assertEquals(realResult, result);
		result = LitePalCopy.where("age < ?", "20").max(studentTable, "age", Integer.TYPE);
		cursor = LitePalCopy.findBySQL("select max(age) from " + studentTable + " where age < ?", "20");
		if (cursor.moveToFirst()) {
			realResult = cursor.getInt(0);
		}
		cursor.close();
		assertEquals(realResult, result);
	}

	@Test
	public void testMin() {
		int result = LitePalCopy.min(Student.class, "age", Integer.TYPE);
		int realResult = -100;
		Cursor cursor = LitePalCopy.findBySQL("select min(age) from " + studentTable);
		if (cursor.moveToFirst()) {
			realResult = cursor.getInt(0);
		}
		cursor.close();
		assertEquals(realResult, result);
		result = LitePalCopy.where("age > ?", "10").min(studentTable, "age", Integer.TYPE);
		cursor = LitePalCopy.findBySQL("select min(age) from " + studentTable + " where age > ?", "10");
		if (cursor.moveToFirst()) {
			realResult = cursor.getInt(0);
		}
		cursor.close();
		assertEquals(realResult, result);
	}

	@Test
	public void testSum() {
		int result = LitePalCopy.sum(Student.class, "age", Integer.TYPE);
		int realResult = -100;
		Cursor cursor = LitePalCopy.findBySQL("select sum(age) from " + studentTable);
		if (cursor.moveToFirst()) {
			realResult = cursor.getInt(0);
		}
		cursor.close();
		assertEquals(realResult, result);
		result = LitePalCopy.where("age > ?", "15").sum(studentTable, "age", Integer.TYPE);
		cursor = LitePalCopy.findBySQL("select sum(age) from " + studentTable + " where age > ?", "15");
		if (cursor.moveToFirst()) {
			realResult = cursor.getInt(0);
		}
		cursor.close();
		assertEquals(realResult, result);
	}

}