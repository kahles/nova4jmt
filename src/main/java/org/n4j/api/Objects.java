package org.n4j.api;


public class Objects {




/*! \struct ln_equ_posn
** \brief Equatorial Coordinates.
*
* The Right Ascension and Declination of an object.
*
* Angles are expressed in degrees.
*/

struct ln_equ_posn {
    double ra;  /*!< RA. Object right ascension in degrees.*/
    double dec; /*!< DEC. Object declination */
};

/*! \struct ln_hrz_posn
** \brief Horizontal Coordinates.
*
* The Azimuth and Altitude of an object.
*
* Angles are expressed in degrees.
*/

struct ln_hrz_posn {
    double az;  /*!< AZ. Object azimuth. <p>
          0 deg = South, 90 deg = West, 180 deg = Nord, 270 deg = East */
    double alt; /*!< ALT. Object altitude. <p> 0 deg = horizon, 90 deg = zenit, -90 deg = nadir */
};


/*! \struct ln_lnlat_posn
** \brief Ecliptical (or celestial) Longitude and Latitude.
*
* The Ecliptical (or celestial) Latitude and Longitude of and object.
*
* Angles are expressed in degrees. East is positive, West negative.
*/

struct ln_lnlat_posn {
    double lng; /*!< longitude. Object longitude. */
    double lat; /*!< latitude. Object latitude */
};


/*! \struct ln_helio_posn
* \brief Heliocentric position 
*
* A heliocentric position is an objects position relative to the
* centre of the Sun. 
*
* Angles are expressed in degrees.
* Radius vector is in AU.
*/
struct ln_helio_posn {
    double L;   /*!< Heliocentric longitude */
    double B;   /*!< Heliocentric latitude */
    double R;   /*!< Heliocentric radius vector */
};

/*! \struct ln_rect_posn
* \brief Rectangular coordinates
*
* Rectangular Coordinates of a body. These coordinates can be either
* geocentric or heliocentric.
*
* A heliocentric position is an objects position relative to the
* centre of the Sun. 
* A geocentric position is an objects position relative to the centre
* of the Earth.
*
* Position is in units of AU for planets and in units of km
* for the Moon.
*/
struct ln_rect_posn {
    double X;   /*!< Rectangular X coordinate */
    double Y;   /*!< Rectangular Y coordinate */
    double Z;   /*!< Rectangular Z coordinate */
};

/*!
* \struct ln_gal_posn
* \brief Galactic coordinates
*
* The Galactic Latitude and Longitude of and object.
*
* Angles are expressed in degrees.
*/
struct ln_gal_posn {
    double l;   /*!< Galactic longitude (degrees) */
    double b;   /*!< Galactic latitude (degrees) */
};

/*!
* \struct ln_ell_orbit
* \brief Elliptic Orbital elements
*
*  TODO.
* Angles are expressed in degrees.
*/
struct ln_ell_orbit {
    double a;   /*!< Semi major axis, in AU */
    double e;   /*!< Eccentricity */
    double i;   /*!< Inclination in degrees */
    double w;   /*!< Argument of perihelion in degrees */
    double omega;   /*!< Longitude of ascending node in degrees*/
    double n;   /*!< Mean motion, in degrees/day */
    double JD;  /*!< Time of last passage in Perihelion, in julian day*/
};

/*!
* \struct ln_par_orbit
* \brief Parabolic Orbital elements
*
*  TODO.
* Angles are expressed in degrees.
*/
struct ln_par_orbit {
    double q;   /*!< Perihelion distance in AU */
    double i;   /*!< Inclination in degrees */
    double w;   /*!< Argument of perihelion in degrees */
    double omega;   /*!< Longitude of ascending node in degrees*/
    double JD;  /*!< Time of last passage in Perihelion, in julian day */
};

/*!
* \struct ln_hyp_orbit
* \brief Hyperbolic Orbital elements
*
*  TODO.
* Angles are expressed in degrees.
*/
struct ln_hyp_orbit {
    double q;   /*!< Perihelion distance in AU */
    double e;   /*!< Eccentricity */
    double i;   /*!< Inclination in degrees */
    double w;   /*!< Argument of perihelion in degrees */
    double omega;   /*!< Longitude of ascending node in degrees*/
    double JD;  /*!< Time of last passage in Perihelion, in julian day*/
};

/*!
* \struct ln_rst_time
* \brief Rise, Set and Transit times. 
*
* Contains the Rise, Set and transit times for a body.
*  
* Angles are expressed in degrees.
*/
struct ln_rst_time {
    double rise;        /*!< Rise time in JD */
    double set;         /*!< Set time in JD */
    double transit;     /*!< Transit time in JD */
};

/*!
* \struct ln_nutation
* \brief Nutation in longitude, ecliptic and obliquity. 
*
* Contains Nutation in longitude, obliquity and ecliptic obliquity. 
*
* Angles are expressed in degrees.
*/
struct ln_nutation {
    double longitude;   /*!< Nutation in longitude, in degrees */
    double obliquity;   /*!< Nutation in obliquity, in degrees */
    double ecliptic;    /*!< Mean obliquity of the ecliptic, in degrees */
};

#if defined(__WIN32__) && !defined(__MINGW__)

#include <time.h>

struct timeval
{
    time_t  tv_sec;         /* count of seconds since Jan. 1, 1970 */
    long    tv_usec;        /* and microseconds */
};

struct timezone
{
    int     tz_minuteswest; /* Minutes west of GMT */
    int     tz_dsttime;     /* DST correction offset */
};

#endif /* __WIN32__ */

#ifdef __cplusplus
};
#endif

#endif

}
