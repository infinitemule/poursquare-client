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
    <h3>Checkins by Country</h3>
  </div>
  <div>
    <table class="table" style="max-width: 600px;"> 
      <tr>
        <th>Country</th>
        <th>Total Checkins</th>
      </tr>      
      <c:forEach var="checkin" items="${checkins}">
        <tr>
          <td><img src="${appRoot}/img/flags/${checkin.flagName}.png"/>&nbsp;&nbsp;${checkin.countryDisplay}</td>
          <td>${checkin.count}</td>
      </c:forEach>    
    </table>
  </div>  
</div>        

</app:view>
