package com.infinitemule.hopperhack.poursquare.service


import com.infinitemule.hopperhack.poursquare.data.PoursquareDaoMongo

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired

import com.infinitemule.hopperhack.poursquare.domain._

/*
 * A service that in this case just forwards calls to a DAO. 
 */

trait PoursquareService {

  def createCheckin(json: String)
  def mostRecentCheckins(limit: Int): List[Checkin]
  def mostRecentInUsa(limit: Int, laterThan: Int): List[Checkin]
  def checkinsByCountry(): List[CheckinCountryCount]
  
}


@Component
class PoursquareDataService extends PoursquareService {
  
  @Autowired
  var dao: PoursquareDaoMongo = _
  
  override def createCheckin(json: String) = {      
    dao.createCheckin(json)
  }

  override def mostRecentInUsa(limit: Int, laterThan: Int): List[Checkin] = {
    dao.mostRecentInUsa(limit, laterThan)
  }  

  override def mostRecentCheckins(limit: Int): List[Checkin] = {
    dao.mostRecentCheckins(limit)
  }  
  
  override def checkinsByCountry(): List[CheckinCountryCount] = {
    dao.checkinsByCountry()
  }
}