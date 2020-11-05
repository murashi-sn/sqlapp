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
package com.sqlapp.data.converter;

import com.sqlapp.data.interval.IntervalYearToMonth;

/**
 * IntervalYearToMonth配列のコンバーター
 * @author satoh
 *
 */
public class IntervalYearToMonthArrayConverter extends AbstractArrayConverter<IntervalYearToMonth[],IntervalYearToMonth>{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4019990269964884424L;

	protected IntervalYearToMonthArrayConverter(Converter<IntervalYearToMonth> unitConverter) {
		super(unitConverter);
	}

	@Override
	protected IntervalYearToMonth[] newArrayInstance(int size) {
		return new IntervalYearToMonth[size];
	}

	@Override
	protected void setArray(IntervalYearToMonth[] array, int i, IntervalYearToMonth value) {
		array[i]=value;
	}

}
