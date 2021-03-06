package de.kah2.zodiac.nova4jmt.api;

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

/**
 ** Zone date \struct ln_zonedate \brief Human readable Date and time with
 * timezone information used by libnova
 *
 * This is the Human readable (easy printf) date with timezone format used by
 * libnova.
 */

public class LnZoneDate {

	/** Years. All values are valid */
	public int years;

	/** Months. Valid values : 1 (January) - 12 (December) */
	public int months;

	/** Days. Valid values 1 - 28,29,30,31 Depends on month. */
	public int days;

	/** Hours. Valid values 0 - 23. */
	public int hours;

	/** Minutes. Valid values 0 - 59. */
	public int minutes;

	/** Seconds. Valid values 0 - 59.99999.... */
	public double seconds;

	/** Timezone offset. Seconds east of UTC. Valid values 0..86400 */
	public long gmtoff;
}
