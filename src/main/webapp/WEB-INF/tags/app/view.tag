<%@ taglib prefix="c"    uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="app"  tagdir="/WEB-INF/tags/app"  %>
<c:set var="appRoot" value="${pageContext.request.contextPath}" scope="request"/>

<!DOCTYPE html>

<html lang="en">
<div id="wrap">
<div id="header" style="background-color: #f5f5f5; height: 60px; padding: 5px;">
<h2 style="float: left; color: #000066; font-variant: small-caps;">Poursquare</h2>
<h4 style="float: right; margin-top:20px;">Team Thunder Pants</h4>
</div>

<jsp:doBody/>
  <div id="push"></div>
</div>
<app:footer/>
</html>