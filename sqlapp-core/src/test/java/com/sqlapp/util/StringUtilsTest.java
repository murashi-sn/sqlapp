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
/**
 * 
 */
package com.sqlapp.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.sqlapp.util.StringUtils;
/**
 * @author satoh
 *
 */
public class StringUtilsTest {

	/**
	 * Test method for {@link com.sqlapp.util.StringUtils#camelToSnakeCase(java.lang.String)}.
	 */
	@Test
	public void camelToSnakeCase() {
		assertEquals("AAAA_BBBB_CCCC", StringUtils.camelToSnakeCase("aaaaBbbbCccc"));
	}

	/**
	 * Test method for {@link com.sqlapp.util.StringUtils#snakeToPascal(java.lang.String)}.
	 */
	@Test
	public void testUnderscoreToPascal() {
		assertEquals("AaaaBbbbCccc", StringUtils.snakeToPascal("AAAA_BBBB_CCCC"));
	}

	/**
	 * Test method for {@link com.sqlapp.util.StringUtils#camelToSnakeCase(java.lang.String)}.
	 */
	@Test
	public void testCamelToSnakeCase() {
		assertEquals("AAAA_BBBB_CCCC", StringUtils.camelToSnakeCase("aaaaBbbbCccc"));
	}

	/**
	 * Test method for {@link com.sqlapp.util.StringUtils#containsUpperCase(java.lang.String)}.
	 */
	@Test
	public void testContainsUpperCase(){
		assertTrue(StringUtils.containsUpperCase("AaaaBbbb"));
		assertTrue(StringUtils.containsUpperCase("AAABBB"));
		assertFalse(StringUtils.containsUpperCase("aaabbbb"));
	}

	/**
	 * Test method for {@link com.sqlapp.util.StringUtils#containsUpperCase(java.lang.String)}.
	 */
	@Test
	public void testContainsLowerCase(){
		assertTrue(StringUtils.containsLowerCase("AaaaBbbb"));
		assertFalse(StringUtils.containsLowerCase("AAABBB"));
		assertTrue(StringUtils.containsLowerCase("aaabbbb"));
	}

	@Test
	public void testCapitalize(){
		assertEquals("AaaaBbbb", StringUtils.capitalize("aaaaBbbb"));
		assertEquals("aaaaBbbb", StringUtils.uncapitalize("AaaaBbbb"));
	}

	@Test
	public void testTransposition(){
		assertEquals("a\na\na", StringUtils.transposition("aaa", " "));
		assertEquals("aB\nab\nab\n b", StringUtils.transposition("aaa\nBbbb", " "));
	}
}
