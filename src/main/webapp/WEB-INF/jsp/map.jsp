<%@ taglib prefix="app"  tagdir="/WEB-INF/tags/app"  %>
<%@ taglib prefix="c"    uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt"  uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ page   contentType="text/html;charset=UTF-8" %>
<app:view>
<app:head title="Realtime Map - Poursquare">

<style>
  
  table {
    font-size: 12px;
  }
      
  table.table td {
    padding: 5px;
  }    
  
  #map-canvas {
    margin: 0;
    padding: 0;
    height: 100%;
  }
  
</style>

<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>

<script>
  var map;
  var latestCheckin = 0;
  
  function initialize() {
    
	// Center the map over Wichita (not sure if that is the most central but it worked fine)
	  
	var mapOptions = {
        zoom: 4,
        center: new google.maps.LatLng(37.6922361,-97.3375448),
        
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    
    map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
    
    start();
  }
  

  google.maps.event.addDomListener(window, 'load', initialize);
 
  
  function addMarker(lat, lng, caption) {
	  
    var marker = new google.maps.Marker({
    	map: map,
    	draggable: false,
        animation: google.maps.Animation.DROP,
        position:  new google.maps.LatLng(lat, lng),
        title:     caption    	
    });	  
	  	  
  }
  
  function start() {
	// This will call the API every three seconds.
    window.setInterval(updateMap, 3000);    
  }
  
  function updateMap() {
	
	$.ajaxSetup({ scriptCharset: "utf-8" , contentType: "application/json; charset=utf-8"});
	
	$.ajax({
	    type: "GET",
	    url: "${appRoot}/api/usa/" + latestCheckin,
	    contentType: "application/json; charset=utf-8",
	    dataType: "json",	    
	    success: function(json) {
	      	
	      $.each(json.checkins, function(i, checkin) {
	            	             
	        addMarker(checkin.lat, checkin.lng, checkin.name + " at " + checkin.date);
	            
	        $('#checkinList').prepend(
	          $('<tr/>')
	              .append($('<td/>').html(checkin.date))
	              .append($('<td/>').html(checkin.name))
	              .append($('<td/>').html(checkin.city))
	              .append($('<td/>').html(checkin.country))
	              .append($('<td/>').html(checkin.lat + ',' + checkin.lng))
	          );	           	    		 
	    	});
	    	
	  	  // This is needed so that we get only the new checkins next time.
	  	  
	  	  if(json.checkins.length > 0) {
	        latestCheckin = json.checkins[0].time;
	  	  }      	           
	    },
	    
	    error: function (xhr, textStatus, errorThrown) {
	    	
	    }
	});
	
  }
</script>

</app:head>

<div class="container">
  <div>
    <h3>Realtime Map</h3>
  </div>

  <!-- Map goes here.  Google API does the rest -->
  <div id="map-canvas" style="width: 1170px; height: 600px;"></div>
          
  <div>
    <table class="table"> 
    <thead>
    <tr>
      <th>Time</th>
      <th>Place</th>
      <th>City</th>
      <th>Country</th>
      <th>Coords</th>      
    </tr>
    </thead>
    <tbody id="checkinList">
    </tbody>
    </table>
  </div>  
</div>        

</app:view>
