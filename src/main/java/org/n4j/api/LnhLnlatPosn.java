package org.n4j.api;

/*! \struct lnh_lnlat_posn
 ** \brief Ecliptical (or celestial) Latitude and Longitude.
 * 
 * Human readable Ecliptical (or celestial) Longitude and Latitude.
 */

public class LnhLnlatPosn {
	/**
	 * longitude. Object longitude.
	 */
	public LnDms lng = new LnDms();
	/**
	 * latitude. Object latitude
	 */
	public LnDms lat = new LnDms();

}
