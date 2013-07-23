package com.infinitemule.hopperhack.poursquare.client

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable,
                                                RequestMapping}
import org.springframework.web.bind.annotation.RequestMethod._
import org.springframework.web.bind.annotation.{RequestBody, ResponseBody}
import org.springframework.web.servlet.ModelAndView

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.Model

import com.infinitemule.hopperhack.poursquare.service.PoursquareService
import com.infinitemule.hopperhack.poursquare.domain._

import scala.beans.BeanProperty
import scala.collection.JavaConversions._

import javax.servlet.http.HttpServletResponse

/*
 * Controllers for the client application
 * 
 * Notes:
 *   - All the Scala lists have to be converted to arrays
 *     so that the JSPs can iterate over them.
 */


/**
 * Controller for the client web app.
 */
@Controller
class AppController {

  
  @Autowired
  var service: PoursquareService = _
   
  /**
   * Shows the lasted checckins overall page
   */
  @RequestMapping(value=Array("latest"), method=Array(GET))
  def latestCheckins() = {    
    
    new ModelAndView("latestCheckins")
      .addObject("checkins", service.mostRecentCheckins(100).toArray)
    
  }
  
  
  /**
   * Shows the most recent checkins in the Boston area.
   */
  @RequestMapping(value=Array("boston"), method=Array(GET))
  def mostRecentBoston() = {    
    
    new ModelAndView("boston")
      .addObject("checkins", service.mostRecentInUsa(100, 0).toArray)
    
  }
  
  
  /**
   * Shows the number of checkins per country
   */
  @RequestMapping(value=Array("countries"), method=Array(GET))
  def checkinsByCountries() = {    
    
    new ModelAndView("byCountry")
      .addObject("checkins", service.checkinsByCountry().toArray)    

  }


  /**
   * Shows the real time map of checkins in the United States.
   */
  @RequestMapping(value=Array("map"), method=Array(GET))  
  def realtimeMap() = new ModelAndView("map")
  

  /**
   * Vote for Thunder Pants
   */
  @RequestMapping(value=Array("vote"), method=Array(GET))  
  def vote() = new ModelAndView("vote")

}


/**
 * Controller for the client API
 */
@Controller
class ApiController {
  
  @Autowired
  var service: PoursquareService = _
  

  /**
   * Retrieves a JSON post form the Storm cluster and stores it 
   * in MongoDB
   */
  @RequestMapping(value=Array("api/checkin/create"), 
                  method=Array(POST),
                  consumes=Array("application/json"),
                  produces=Array("application/json"))
  @ResponseBody                  
  def create(@RequestBody checkin: String): String = {    
            
    try {
      service.createCheckin(checkin)
      """{"status": "success"}"""
    }
    catch {
      case e: Exception => """{"status": "failure", "reason": "%s"} """.format(e.getMessage)    
    }
    
    
  }
  
  
  /**
   * Returns a list of JSON object for the most recent checkins in the country.
   * This is called every few seconds by the map page in order to get new checkins. 
   */
  @RequestMapping(value=Array("api/usa/{laterThan}"), 
                  method=Array(GET),
                  produces=Array("application/json"))
  @ResponseBody                      
  def mostRecentUsa(@PathVariable laterThan: Int, response: HttpServletResponse) = {    
    
    response.setHeader("content-encoding", "application/json; charset=UTF-8")
    
    val format = new java.text.SimpleDateFormat("HH:mm:ss dd MMM yyyy")
    
    val checkins = service.mostRecentInUsa(10, laterThan)
    	
    // Got a little sloppy here but was running out of time.  This should
    // probably be done with a Spray JSON protocol.
    
    val doc = """{"checkins": [%s]}"""
    
    val checkinsJson = checkins map { c =>
      """{"date": "%s", "time": %s, "name": "%s",  "city": "%s", "country": "%s - %s", "lat": %s, "lng": %s}""".format(
          format.format(c.time), c.time.getTime() / 1000, c.venue.name, 
          c.venue.getCityDisplay, c.venue.cc, c.venue.country,
          c.venue.lat, c.venue.lng)
    }
            
    doc.format(checkinsJson.mkString(","))
  }
  
}

