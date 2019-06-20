package de.kah2.zodiac.nova4jmt;

/*
 * #%L
 * libnova for Java
 * %%
 * Copyright (C) 2014 novaforjava
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import java.util.Calendar;
import java.util.TimeZone;

import de.kah2.zodiac.nova4jmt.api.LnDate;

import org.junit.Assert;
import org.junit.Test;

public class OtherTests {
	@Test
	public void testVersion() {
		Assert.assertNotNull(Utility.ln_get_version());
	}

	@Test
	public void currentTime() {
		LnDate date = new LnDate();
		JulianDay.ln_get_date_from_sys(date);
		System.out.println(date);
	}

	@Test
	public void otherDate() {
		LnDate date = new LnDate();
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT+2"));
		cal.set(Calendar.YEAR, 1954);
		cal.set(Calendar.MONTH, 5);// june
		cal.set(Calendar.DAY_OF_MONTH, 30);
		cal.set(Calendar.HOUR_OF_DAY, 2);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		JulianDay.ln_get_date_from_cal(date, cal);
		Assert.assertEquals(1954, date.years);
		Assert.assertEquals(6, date.months);
		Assert.assertEquals(30, date.days);
		Assert.assertEquals(0, date.hours);
		Assert.assertEquals(0, date.minutes);
		Assert.assertEquals(0d, date.seconds, 0.000001);
	}

}
