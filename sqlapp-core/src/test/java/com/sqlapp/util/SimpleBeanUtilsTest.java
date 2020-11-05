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
package com.sqlapp.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.sqlapp.data.converter.EnumConvertable;

/**
 * Beanユーティリティのテスト
 * 
 */
public class SimpleBeanUtilsTest {

	/**
	 * パブリックフィールドのテストを実施します
	 */
	@Test
	public void testPublicfield1() {
		Dummy1 dummy1 = new Dummy1();
		dummy1.id = 1;
		dummy1.name = "aaa";
		Map<String, Object> map = SimpleBeanUtils.toMap(dummy1);
		assertEquals(7, map.size());
		assertEquals("aaa", map.get("name"));
	}

	/**
	 * セッター、ゲッターのテストを行います
	 */
	@Test
	public void testSetterGetter2() {
		Dummy2 dummy2 = new Dummy2();
		dummy2.setId(1);
		dummy2.setName("aaa");
		Map<String, Object> map = SimpleBeanUtils.toMap(dummy2);
		assertEquals(8, map.size());
		assertEquals("aaa", map.get("name"));
		assertEquals(Boolean.FALSE, map.get("enable"));
	}

	/**
	 * プロパティコピーのテストを行います
	 */
	@Test
	public void testCopy1() {
		Dummy1 dummy1 = new Dummy1();
		dummy1.id = 1;
		dummy1.name = "aaa";
		dummy1.fiscalYear = "201103";
		Dummy2 dummy2 = SimpleBeanUtils.convert(dummy1, Dummy2.class);
		assertEquals("aaa", dummy2.getName());
		assertEquals(null, dummy2.getFiscal_year());
	}

	/**
	 * プロパティコピー(大文字、小文字無視)のテストを行います
	 */
	@Test
	public void testCopyCI1() {
		Dummy1 dummy1 = new Dummy1();
		dummy1.id = 1;
		dummy1.name = "aaa";
		dummy1.fiscalYear = "201103";
		dummy1.type = 1;
		dummy1.typeVal = Type.A;
		Dummy2 dummy2 = SimpleBeanUtils.convertCI(dummy1, Dummy2.class);
		assertEquals("aaa", dummy2.getName());
		assertEquals(dummy1.fiscalYear, dummy2.getFiscal_year());
		assertEquals(Type.A, dummy2.type);
		assertEquals(Type.A.getValue(), dummy2.typeVal);
	}

	/**
	 * リストのプロパティコピー(大文字、小文字無視)のテストを行います
	 */
	@Test
	public void testCopyListCI1() {
		Dummy1 dummy1 = new Dummy1();
		dummy1.id = 1;
		dummy1.name = "aaa";
		dummy1.fiscalYear = "201103";
		List<Dummy1> list = new ArrayList<Dummy1>();
		list.add(dummy1);
		List<Dummy2> list2 = SimpleBeanUtils.convertListCI(list, Dummy2.class);
		assertEquals(list.size(), list2.size());
		Dummy2 dummy2 = list2.get(0);
		assertEquals("aaa", dummy2.getName());
		assertEquals(dummy1.fiscalYear, dummy2.getFiscal_year());
	}

	/**
	 * ネストしたプロパティコピーのテストを行います
	 */
	@Test
	public void testNestCopy1() {
		Parent1 parent1 = new Parent1();
		parent1.id = 1;
		parent1.name = "aaa";
		parent1.childProperty = new Child1();
		parent1.childProperty.id = 3;
		Parent2 parent2 = SimpleBeanUtils.convert(parent1, Parent2.class);
		assertEquals("aaa", parent2.getName());
		assertEquals(null, parent2.getChild_property());
	}

	/**
	 * ネストしたプロパティコピーのテストを行います
	 */
	@Test
	public void testNestCopyCI1() {
		Parent1 parent1 = new Parent1();
		parent1.id = 1;
		parent1.name = "aaa";
		parent1.childProperty = new Child1();
		parent1.childProperty.id = 3;
		Parent2 parent2 = SimpleBeanUtils.convertCI(parent1, Parent2.class);
		assertEquals("aaa", parent2.getName());
		assertTrue(parent2.getChild_property() != null);
		assertEquals(parent1.childProperty.id, parent2.getChild_property()
				.getId());
	}

	/**
	 * コレクションをマップに変換するテストを実施します
	 */
	@Test
	public void testToMap() {
		List<Dummy1> list = new ArrayList<Dummy1>();
		Dummy1 dummy1 = new Dummy1();
		dummy1.id = 1;
		dummy1.name = "aaa";
		list.add(dummy1);
		//
		dummy1 = new Dummy1();
		dummy1.id = 2;
		dummy1.name = "bbb";
		list.add(dummy1);
		Map<Integer, Dummy1> map = SimpleBeanUtils.convertMap(list, "id");
		assertEquals(2, map.size());
		assertEquals("aaa", map.get(1).name);
		assertEquals("bbb", map.get(2).name);
	}

	/**
	 * コレクションをマップに変換するテストを実施します
	 */
	@Test
	public void testToMapCI() {
		List<Dummy1> list = new ArrayList<Dummy1>();
		Dummy1 dummy1 = new Dummy1();
		dummy1.id = 1;
		dummy1.name = "aaa";
		dummy1.type = 1;
		list.add(dummy1);
		//
		dummy1 = new Dummy1();
		dummy1.id = 2;
		dummy1.name = "bbb";
		list.add(dummy1);
		Map<Integer, Dummy1> map = SimpleBeanUtils.convertMapCI(list, "ID");
		assertEquals(2, map.size());
		assertEquals("aaa", map.get(1).name);
		assertEquals("bbb", map.get(2).name);
	}
	
	
	@Test
	public void testSetField() {
		Dummy2 dummy = new Dummy2();
		boolean bool= SimpleBeanUtils.setField(dummy, "id", -10);
		assertEquals(true, bool);
		assertEquals(-10, dummy.getId());
		bool= SimpleBeanUtils.setField(dummy, "baseId", -20);
		assertEquals(true, bool);
		assertEquals(-20, dummy.getBaseId());
	}

	static class Base {
		private int baseId;

		/**
		 * @return the baseId
		 */
		public int getBaseId() {
			return baseId;
		}

		/**
		 * @param baseId the baseId to set
		 */
		public void setBaseId(int baseId) {
			this.baseId = baseId;
		}
		
	}

	
	static class Dummy1 {
		public int id;
		public String name;
		public boolean enable;
		public String fiscalYear;
		public Long val;
		public Integer type;
		public Type typeVal;
	}

	static class Dummy2 extends Base{
		private int id;
		private String name;
		private boolean enable = false;
		public String fiscal_year;
		public long val;
		public Type type;
		public Integer typeVal;

		/**
		 * @return the fiscal_year
		 */
		public String getFiscal_year() {
			return fiscal_year;
		}

		/**
		 * @param fiscal_year
		 *            the fiscal_year to set
		 */
		public void setFiscal_year(String fiscal_year) {
			this.fiscal_year = fiscal_year;
		}

		/**
		 * @return the enable
		 */
		public boolean isEnable() {
			return enable;
		}

		/**
		 * @param enable
		 *            the enable to set
		 */
		public void setEnable(boolean enable) {
			this.enable = enable;
		}

		/**
		 * @return the id
		 */
		public int getId() {
			return id;
		}

		/**
		 * @param id
		 *            the id to set
		 */
		public void setId(int id) {
			this.id = id;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name
		 *            the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

	}

	static class Parent1 {
		public int id;
		public String name;
		public Child1 childProperty;
	}

	static class Child1 {
		public int id;
	}

	static class Parent2 {
		private int id;
		private String name;

		/**
		 * @return the id
		 */
		public int getId() {
			return id;
		}

		/**
		 * @param id
		 *            the id to set
		 */
		public void setId(int id) {
			this.id = id;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name
		 *            the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the child_property
		 */
		public Child2 getChild_property() {
			return child_property;
		}

		/**
		 * @param child_property
		 *            the child_property to set
		 */
		public void setChild_property(Child2 child_property) {
			this.child_property = child_property;
		}

		private Child2 child_property;
	}

	static class Child2 {
		private int id;

		/**
		 * @return the id
		 */
		public int getId() {
			return id;
		}

		/**
		 * @param id
		 *            the id to set
		 */
		public void setId(int id) {
			this.id = id;
		}
	}

	public static enum Type implements EnumConvertable<Integer> {
		A(1), B(2);
		Integer value;

		Type(Integer value) {
			this.value = value;
		}

		@Override
		public Integer getValue() {
			return value;
		}

		public static Type parse(Integer value) {
			for (Type t : values()) {
				if (t.getValue().equals(value)) {
					return t;
				}
			}
			return null;
		}
	}

}
