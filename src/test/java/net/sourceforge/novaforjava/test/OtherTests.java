package net.sourceforge.novaforjava.test;

import net.sourceforge.novaforjava.Utility;

import org.junit.Assert;
import org.junit.Test;

public class OtherTests {
	@Test
	public void testVersion() {
		Assert.assertNotNull(Utility.ln_get_version());
	}
}
