package org.n4j.api;

/*! \struct ln_hms
 ** \brief Hours, minutes and seconds.
 *
 * Human readable Angle in hours, minutes and seconds
 */

public class LnHms {

	public int hours; /* !< Hours. Valid 0 - 23 */
	public int minutes; /* !< Minutes. Valid 0 - 59 */
	public double seconds; /* !< Seconds. Valid 0 - 59.9999... */
}
