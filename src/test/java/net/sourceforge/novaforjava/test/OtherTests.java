package net.sourceforge.novaforjava.test;

import net.sourceforge.novaforjava.JulianDay;
import net.sourceforge.novaforjava.Utility;
import net.sourceforge.novaforjava.api.LnDate;

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
}
