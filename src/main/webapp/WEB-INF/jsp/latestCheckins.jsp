<%@ taglib prefix="app"  tagdir="/WEB-INF/tags/app"  %>
<%@ taglib prefix="c"    uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt"  uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ page   contentType="text/html;charset=UTF-8" %>
<app:view>
<app:head title="Latest Checkins">
<style>
  table {
    font-size: 12px;
  }
      
  table.table td {
    padding: 5px;
  }    
</style>
</app:head>

<div class="container">
  <div>
    <h3>Latest Checkins</h3>
  </div>
  <div>
    <table class="table"> 
      <tr>
        <th>Time</th>
        <th>Place</th>
        <th>City</th>
        <th>Country</th>
        <th>Coords</th>
        <th>Link</th>
      </tr>
      <c:forEach var="checkin" items="${checkins}">
        <tr>
          <td><fmt:formatDate pattern="HH:mm:ss dd MMM yyyy" value="${checkin.time}" />
          <td>${checkin.venue.name}</td>
          <td>${checkin.venue.cityDisplay}</td>
          <td>${checkin.venue.cc} - ${checkin.venue.country}</td>
          <td><a href="http://maps.google.com?q=${checkin.venue.lat},${checkin.venue.lng}" target="_blank">Map</a></td>
          <td><a href="${checkin.url}" target="_blank">Foursquare</a></td>
      </c:forEach>    
    </table>
  </div>  
</div>        

</app:view>