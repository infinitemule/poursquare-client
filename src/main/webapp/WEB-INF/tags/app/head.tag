<%@attribute name="title" required="true" %>
<%@ taglib prefix="html"  tagdir="/WEB-INF/tags/html"  %>
<%@ taglib prefix="c"    uri="http://java.sun.com/jsp/jstl/core" %> 
<head>
  <title>Poursquare - ${title}</title>
    
  <html:favIcon href="/img/favicon.ico" />
  
  <html:importJs  path="/js/jquery-latest.js"/>
  <html:importJs  path="/bootstrap/js/bootstrap.js"/>
        
  <html:importCss path="/bootstrap/css/bootstrap.css"/>    
  <html:importCss path="/css/main"/>
  <html:importCss path="/bootstrap/css/bootstrap-responsive.css"/>
  <!--
  <html:importCss path="/css/bootstrap"/>
   -->
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>    
  
  <style type="text/css">

      html,
      body {
        height: 100%;
      }

      #wrap {
        min-height: 100%;
        height: auto !important;
        height: 100%;
        margin: 0 auto -60px;
      }

      #push,
      #footer {
        height: 60px;
      }
      #footer {
        background-color: #f5f5f5;
      }

      @media (max-width: 767px) {
        #footer {
          margin-left: -20px;
          margin-right: -20px;
          padding-left: 20px;
          padding-right: 20px;
        }
      }  
      
      .container .credit {
        margin: 20px 0;
      }  
      
    </style>  
  
     
  <jsp:doBody/>
</head>
