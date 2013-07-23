package com.infinitemule.hopperhack.poursquare.data


import com.infinitemule.hopperhack.poursquare.domain._

import com.mongodb.util.JSON

import com.mongodb.{DBObject,
                    DBCursor,
                    DBCollection,
                    BasicDBObject}

import java.util.Date

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.stereotype.Repository


import scala.collection.JavaConversions._


object Direction {  
  val ASC  =  1;
  val DESC = -1;  
}

/**
 * Mongo DB DAO.  There is a Scala Mongo library called Casbah,
 * but I didn't have much experience with it so I used the pure
 * Java one instead.  
 * 
 * Notes: 
 *   - The code is a little verbose in Scala, but
 *     easy enough to understand.  
 *   - Some implicits may have helped herebut I didn't have 
 *     time to write them.  
 *   - There is also some repeated code that could have 
 *     been extracted into common methods.
 *   - I just cut and pasted the queries from the mongo shell, it might
 *     be better and faster to use the library methods instead.
 *         
 */
@Repository
class PoursquareDaoMongo {

  @Autowired  
  var mongo: MongoOperations = _
  
 
  /**
   * Saves a checkin sent over from the storm cluster
   */
  def createCheckin(json: String) = {
     
    mongo.save(JSON.parse(json).asInstanceOf[DBObject], "checkins")
        
  }

    
  /**
   * Returns the most recent checkins in the US since a specified date.  Used
   * in the map page.
   */
  def mostRecentInUsa(limit: Int, laterThan: Int): List[Checkin] = {

    val coll = mongo.getCollection("checkins")

    val query = JSON.parse("{$and: [{'venue.location.cc': 'US'}, {'created_at': {'$gt': %s}}] }"
                    .format(laterThan))
                    .asInstanceOf[DBObject]
    
    val checkins = coll.find(query).limit(limit).sort(sortBy("created_at", Direction.DESC))

                
    asList(checkins).map { obj => 
      
      val location = obj.get("venue").asInstanceOf[DBObject]
                        .get("location").asInstanceOf[DBObject]
      
      Checkin(
           obj.get("_id").asInstanceOf[String],
           new Date(obj.get("created_at").asInstanceOf[Int].toLong * 1000), 
           Venue(
             obj.get("venue").asInstanceOf[DBObject]
                .get("name").asInstanceOf[String],
             location.get("address").asInstanceOf[String],
             location.get("city").asInstanceOf[String],
             location.get("state").asInstanceOf[String],
             location.get("country").asInstanceOf[String],
             location.get("cc").asInstanceOf[String],
             location.get("lat").asInstanceOf[Double],
             location.get("lng").asInstanceOf[Double]
           ),
           obj.get("url").asInstanceOf[String]
       )
           
    }
    
  }
  
  /**
   * Retrieves the most recent checkins overall
   */
  def mostRecentCheckins(limit: Int): List[Checkin] = {
	
    val coll = mongo.getCollection("checkins")
	
    
    val checkins = coll.find().limit(limit).sort(sortBy("created_at", Direction.DESC))

    
    asList(checkins).map { obj => 
      
      val location = obj.get("venue").asInstanceOf[DBObject]
                        .get("location").asInstanceOf[DBObject]
      
      Checkin(
           obj.get("_id").asInstanceOf[String],
           new Date(obj.get("created_at").asInstanceOf[Int].toLong * 1000), 
           Venue(
             obj.get("venue").asInstanceOf[DBObject]
                .get("name").asInstanceOf[String],
             location.get("address").asInstanceOf[String],
             location.get("city").asInstanceOf[String],
             location.get("state").asInstanceOf[String],
             location.get("country").asInstanceOf[String],
             location.get("cc").asInstanceOf[String],
             location.get("lat").asInstanceOf[Double],
             location.get("lng").asInstanceOf[Double]
           ),
           obj.get("url").asInstanceOf[String]
       )
           
    }
      
  }
  
  
  /**
   * Sums up the total number of checkins by country
   */
  def checkinsByCountry(): List[CheckinCountryCount] = {
    
    val coll = mongo.getCollection("checkins")
	
    val query = JSON.parse("{ $group: { _id: { cc: '$venue.location.cc', country: '$venue.location.country'}, count: { $sum: 1 } } }}")
                    .asInstanceOf[DBObject]
    
    val counts = coll.aggregate(query, JSON.parse("{$sort: {count: -1}}").asInstanceOf[DBObject])
    
    counts.results.toList map { r =>
            
      CheckinCountryCount(
       r.get("_id").asInstanceOf[DBObject]
         .get("country").asInstanceOf[String],
       r.get("_id").asInstanceOf[DBObject]
         .get("cc").asInstanceOf[String],
       r.get("count").asInstanceOf[Int]
      )
    }    
        
  }
  
  /**
   * Iterates over a cursor and returns a list of DBObjects.  I think
   * there is probably a better way of doing this since this will
   * force you to interate over the list twice.
   */
  private def asList(cursor: DBCursor): List[DBObject] = {
    
    val list = new scala.collection.mutable.ListBuffer[DBObject]()
    
    try {
      while(cursor.hasNext()) {
        list += cursor.next()
      }
    }
    finally {
      cursor.close
    }
    
    list.toList
  }
  
  
  private def sortBy(field: String, dir: Int) = {
    new BasicDBObject(field, dir)
  }
}