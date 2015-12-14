<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <title>Google Maps Geocoding Demo</title> 
   <script src="http://maps.google.com/maps/api/js?sensor=false" type="text/javascript"></script>
   <script src="http://code.jquery.com/jquery-latest.js"></script>
   <link rel="stylesheet" href="./Css/jquery.mobile-1.4.4/jquery.mobile-1.4.4.min.css">
   <script src="./Css/jquery.mobile-1.4.4/demos/js/jquery.js"></script>
   <script src="./Css/jquery.mobile-1.4.4/jquery.mobile-1.4.4.min.js"></script>
   <script src="./Js/jquery.timer.js"></script>
</head>
<body>
<div id="map" style="width: 400px; height: 300px;"></div> 

   <script type="text/javascript"> 
   		var address = '4818 rue de gaspé, Sherbrooke, Canada';

	   	var map = new google.maps.Map(document.getElementById('map'), { 
	       mapTypeId: google.maps.MapTypeId.TERRAIN,
	       zoom: 12
	   	});
	   	
   		var geocoder = new google.maps.Geocoder();

	   geocoder.geocode({
	      'address': address
	   }, 
	   function(results, status) {
	      if(status == google.maps.GeocoderStatus.OK) {
	         new google.maps.Marker({
	            position: results[0].geometry.location,
	            map: map
	         });
	         map.setCenter(results[0].geometry.location);
	      }
	      else {
	         // Google couldn't geocode this request. Handle appropriately.
	      }
	   });
   </script> 
   <button id="refresh" href="#demo-links" class="ui-btn ui-shadow ui-corner-all ui-btn-a ui-icon-refresh ui-btn-icon-left ui-btn-inline">Rafraîchir les points</button>
   <script src="./Js/maps.js"></script>
</body>
</html>