package com.infinitemule.hopperhack.poursquare.domain

import java.util.Date
import scala.beans.BeanProperty

/*
 * Case classes for Checkins.  The BeanProperty annotation
 * is unfortunately necessary in order for JSPs to work properly.
 */

case class Venue(
  @BeanProperty name:    String,
  @BeanProperty address: String,
  @BeanProperty city:    String,
  @BeanProperty state:   String,
  @BeanProperty country: String,
  @BeanProperty cc:      String,
  @BeanProperty lat:     Double,
  @BeanProperty lng:     Double
) {
  
  def getCityDisplay(): String = {
    (city, state) match {
      case(null, null) => ""
      case(_, null)    => city
      case(null,_)     => state
      case _           => "%s, %s".format(city, state)
    }
  }
  
}
  

case class Checkin(
  @BeanProperty id:    String,
  @BeanProperty time:  Date, 
  @BeanProperty venue: Venue,
  @BeanProperty url:   String
)


case class CheckinCountryCount(
  @BeanProperty country:     String, 
  @BeanProperty countryCode: String,
  @BeanProperty count:       Int
) {  
  def getCountryDisplay(): String = "%s - %s".format(countryCode, country)
  def getFlagName(): String  = countryCode.toLowerCase()
}


