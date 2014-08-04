package org.n4j.api;

/*!
* \struct ln_rst_time
* \brief Rise, Set and Transit times. 
*
* Contains the Rise, Set and transit times for a body.
*  
* Angles are expressed in degrees.
*/
public class LnRstTime {

    double rise;        /*!< Rise time in JD */
    double set;         /*!< Set time in JD */
    double transit;     /*!< Transit time in JD */
}
