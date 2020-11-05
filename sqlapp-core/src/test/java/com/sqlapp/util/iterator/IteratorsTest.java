/**
 * Copyright (C) 2007-2017 Tatsuo Satoh <multisqllib@gmail.com>
 *
 * This file is part of sqlapp-core.
 *
 * sqlapp-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sqlapp-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with sqlapp-core.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sqlapp.util.iterator;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;

import com.sqlapp.data.interval.IntervalMonth;
import com.sqlapp.util.DateUtils;

public class IteratorsTest {

	@Test
	public void testIntRange1() {
		int cnt=0;
		int sum=0;
		for(Integer val:Iterators.range(0,10,1)){
			cnt++;
			sum=sum+val.intValue();
		}
		assertEquals(10, cnt);
	}

	@Test
	public void testIntRange2() {
		int cnt=0;
		int sum=0;
		for(Integer val:Iterators.range(10)){
			cnt++;
			sum=sum+val.intValue();
		}
		assertEquals(10, cnt);
	}

	@Test
	public void testLongRange2() {
		int cnt=0;
		int sum=0;
		for(Long val:Iterators.range(10L)){
			cnt++;
			sum=sum+val.intValue();
		}
		assertEquals(10, cnt);
	}

	/**
	 * 日付型のrangeテスト(1日毎の加算)
	 * @throws ParseException
	 */
	@Test
	public void testDate1() throws ParseException {
		Date start=DateUtils.parse("20110101", "yyyyMMdd");
		Date end=DateUtils.parse("20120101", "yyyyMMdd");
		Date test=null;
		Date last=DateUtils.parse("20111231", "yyyyMMdd");
		int cnt=0;
		for(Date val:Iterators.range(start, end, 3600*24)){
			if (cnt==364){
				test=val;
			}
			cnt++;
		}
		assertEquals(365, cnt);
		assertEquals(last, test);
	}

	/**
	 * 日付型のrangeテスト(1日毎の加算)
	 * @throws ParseException
	 */
	@Test
	public void testCalendar1() throws ParseException {
		Calendar start=Calendar.getInstance();
		start.set(2011, 0, 1, 0, 0, 0);
		start.set(Calendar.MILLISECOND, 0);
		Calendar end=Calendar.getInstance();
		end.set(2012, 0, 1, 0, 0, 0);
		end.set(Calendar.MILLISECOND, 0);
		Calendar test=null;
		Calendar last=Calendar.getInstance();
		last.set(2011, 11, 31, 0, 0, 0);
		last.set(Calendar.MILLISECOND, 0);
		int cnt=0;
		for(Calendar val:Iterators.range(start, end, 3600*24)){
			if (cnt==364){
				test=val;
			}
			cnt++;
		}
		assertEquals(365, cnt);
		assertEquals(last, test);
	}

	/**
	 * 日付型のrangeテスト(1日毎の加算)
	 * @throws ParseException
	 */
	@Test
	public void testLocalDateTime1() throws ParseException {
		LocalDateTime start=LocalDateTime.of(2011,1,1, 0,0,0);
		LocalDateTime end=LocalDateTime.of(2012,1,1, 0,0,0);
		LocalDateTime test=null;
		LocalDateTime last=LocalDateTime.of(2011,12,31, 0,0,0);
		int cnt=0;
		for(LocalDateTime val:Iterators.range(start, end, 3600*24)){
			if (cnt==364){
				test=val;
			}
			cnt++;
		}
		assertEquals(365, cnt);
		assertEquals(last, test);
	}
	
	/**
	 * 日付型のrangeテスト(1日毎の加算)
	 * @throws ParseException
	 */
	@Test
	public void testInstant1() throws ParseException {
		Instant start=ZonedDateTime.of(2011,1,1, 0,0,0,0, ZoneId.systemDefault()).toInstant();
		Instant end=ZonedDateTime.of(2012,1,1, 0,0,0,0, ZoneId.systemDefault()).toInstant();
		Instant test=null;
		Instant last=ZonedDateTime.of(2011,12,31, 0,0,0,0, ZoneId.systemDefault()).toInstant();
		int cnt=0;
		for(Instant val:Iterators.range(start, end, 3600*24)){
			if (cnt==364){
				test=val;
			}
			cnt++;
		}
		assertEquals(365, cnt);
		assertEquals(last, test);
	}

	/**
	 * 日付型のrangeテスト(12時間毎)
	 * @throws ParseException
	 */
	@Test
	public void testDate2() throws ParseException {
		Date start=DateUtils.parse("20110101", "yyyyMMdd");
		Date end=DateUtils.parse("20120101", "yyyyMMdd");
		Date test=null;
		Date last=DateUtils.parse("20111231120000", "yyyyMMddHHmmss");
		int cnt=0;
		for(Date val:Iterators.range(start, end, 3600*12)){
			if (cnt==(365*2-1)){
				test=val;
			}
			cnt++;
		}
		assertEquals(365*2, cnt);
		assertEquals(last, test);
	}

	/**
	 * 日付型のrangeテスト(12時間毎)
	 * @throws ParseException
	 */
	@Test
	public void testCalendar2() throws ParseException {
		Calendar start=Calendar.getInstance();
		start.set(2011, 0, 1, 0, 0, 0);
		start.set(Calendar.MILLISECOND, 0);
		Calendar end=Calendar.getInstance();
		end.set(2012, 0, 1, 0, 0, 0);
		end.set(Calendar.MILLISECOND, 0);
		Calendar test=null;
		Calendar last=Calendar.getInstance();
		last.set(2011, 11, 31, 12, 0, 0);
		last.set(Calendar.MILLISECOND, 0);
		int cnt=0;
		for(Calendar val:Iterators.range(start, end, 3600*12)){
			if (cnt==(365*2-1)){
				test=val;
			}
			cnt++;
		}
		assertEquals(365*2, cnt);
		assertEquals(last, test);
	}

	/**
	 * 日付型のrangeテスト(12時間毎)
	 * @throws ParseException
	 */
	@Test
	public void testDateTime2() throws ParseException {
		LocalDateTime start=LocalDateTime.of(2011,1,1, 0,0,0);
		LocalDateTime end=LocalDateTime.of(2012,1,1, 0,0,0);
		LocalDateTime test=null;
		LocalDateTime last=LocalDateTime.of(2011,12,31, 12,0,0);
		int cnt=0;
		for(LocalDateTime val:Iterators.range(start, end, 3600*12)){
			if (cnt==(365*2-1)){
				test=val;
			}
			cnt++;
		}
		assertEquals(365*2, cnt);
		assertEquals(last, test);
	}
	
	/**
	 * 日付型のrangeテスト(うるう年の1日毎の加算)
	 * @throws ParseException
	 */
	@Test
	public void testDate3() throws ParseException {
		Date start=DateUtils.parse("20120101", "yyyyMMdd");
		Date end=DateUtils.parse("20130101", "yyyyMMdd");
		Date test=null;
		Date last=DateUtils.parse("20121231", "yyyyMMdd");
		int cnt=0;
		for(Date val:Iterators.range(start, end, 3600*24)){
			if (cnt==365){
				test=val;
			}
			cnt++;
		}
		assertEquals(366, cnt);
		assertEquals(last, test);
	}

	/**
	 * 日付型のrangeテスト(月初の1カ月)
	 * @throws ParseException
	 */
	@Test
	public void testDate4() throws ParseException {
		Date start=DateUtils.parse("20120101", "yyyyMMdd");
		Date end=DateUtils.parse("20130101", "yyyyMMdd");
		Date test=null;
		Date last=DateUtils.parse("20121201", "yyyyMMdd");
		int cnt=0;
		for(Date val:Iterators.range(start, end, new IntervalMonth(1))){
			if (cnt==11){
				test=val;
			}
			cnt++;
		}
		assertEquals(12, cnt);
		assertEquals(last, test);
	}

	/**
	 * 日付型のrangeテスト(月末の1カ月)
	 * @throws ParseException
	 */
	@Test
	public void testDate5() throws ParseException {
		Date start=DateUtils.parse("20120131", "yyyyMMdd");
		Date end=DateUtils.parse("20130101", "yyyyMMdd");
		Date test=null;
		Date last_1=DateUtils.parse("20121130", "yyyyMMdd");
		int cnt=0;
		for(Date val:Iterators.range(start, end, new IntervalMonth(1))){
			if (cnt==10){
				test=val;
			}
			cnt++;
		}
		assertEquals(12, cnt);
		assertEquals(last_1, test);
	}

	/**
	 * 日付型のrangeテスト(月末の1カ月)
	 * @throws ParseException
	 */
	@Test
	public void testCalendar5() throws ParseException {
		Calendar start=Calendar.getInstance();
		start.set(2011, 0, 31, 0, 0, 0);
		start.set(Calendar.MILLISECOND, 0);
		Calendar end=Calendar.getInstance();
		end.set(2012, 0, 1, 0, 0, 0);
		end.set(Calendar.MILLISECOND, 0);
		Calendar test=null;
		Calendar last_1=Calendar.getInstance();
		last_1.set(2011, 10, 30, 0, 0, 0);
		last_1.set(Calendar.MILLISECOND, 0);
		int cnt=0;
		for(Calendar val:Iterators.range(start, end, new IntervalMonth(1))){
			if (cnt==10){
				test=val;
			}
			cnt++;
		}
		assertEquals(12, cnt);
		assertEquals(last_1, test);
	}
	
	/**
	 * 日付型のrangeテスト(月末の1カ月)
	 * @throws ParseException
	 */
	@Test
	public void testDateTime5() throws ParseException {
		LocalDateTime start=LocalDateTime.of(2011,1,31, 0,0,0);
		LocalDateTime end=LocalDateTime.of(2012,1,1, 0,0,0);
		LocalDateTime test=null;
		LocalDateTime last_1=LocalDateTime.of(2011,11,30, 0,0,0);
		int cnt=0;
		for(LocalDateTime val:Iterators.range(start, end, new IntervalMonth(1))){
			if (cnt==10){
				test=val;
			}
			cnt++;
		}
		assertEquals(12, cnt);
		assertEquals(last_1, test);
	}
	
	
	@Test
	public void testSqlDate1() throws ParseException {
		java.sql.Date start=DateUtils.toSqlDate(DateUtils.parse("20110101", "yyyyMMdd"));
		java.sql.Date end=DateUtils.toSqlDate(DateUtils.parse("20120101", "yyyyMMdd"));
		java.sql.Date test=null;
		java.sql.Date last=DateUtils.toSqlDate(DateUtils.parse("20111231", "yyyyMMdd"));
		int cnt=0;
		for(java.sql.Date val:Iterators.range(start, end, 1)){
			if (cnt==364){
				test=val;
			}
			cnt++;
		}
		assertEquals(365, cnt);
		assertEquals(last, test);
	}

	@Test
	public void testSqlTime1() throws ParseException {
		java.sql.Time start=DateUtils.toTime(DateUtils.parse("000000", "HHmmss"));
		java.sql.Time end=DateUtils.toTime(DateUtils.parse("235959", "HHmmss"));
		java.sql.Time test=null;
		java.sql.Time last=DateUtils.toTime(DateUtils.parse("230000", "HHmmss"));
		int cnt=0;
		for(java.sql.Time val:Iterators.range(start, end, 60*60)){
			test=val;
			cnt++;
		}
		assertEquals(24, cnt);
		assertEquals(last, test);
	}

	@Test
	public void testStringArray() throws ParseException {
		int cnt=0;
		String test=null;
		for(String val:Iterators.range("a", "b", "c")){
			test=val;
			cnt++;
		}
		assertEquals(3, cnt);
		assertEquals("c", test);
	}
}
