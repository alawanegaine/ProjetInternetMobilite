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
	<div class="ui-grid-a">
		<div style="width:55%" class="ui-block-a">
			<div id="map" style="width: 780px; height: 600px;"></div> 
		</div>
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
			<div style="width:45%" class="ui-block-b">
			   <form>
			    	<label for="slider-1">Km max pour la recherche des points :</label>
			    	<input type="range" name="slider-1" id="kmMax" min="0" max="100" value="20">
					<div class="ui-field-contain">
					    <label for="select-native-1">Date maximum de recherche :</label>
					    <select name="select-native-1" id="nbMoisMax">
					        <option value="1">1 mois</option>
					        <option value="3">3 mois</option>
					        <option value="6">6 mois</option>
					        <option value="12">1 an</option>
					 		<option value="24">2 an</option>
					    </select>
					</div>
				</form>
		   		<button id="refresh" href="#demo-links" class="ui-btn ui-shadow ui-corner-all ui-btn-a ui-icon-refresh ui-btn-icon-left ui-btn-inline">Rafraîchir les points</button>
				<script src="./Js/maps.js"></script>
		   </div>
  	</div>
</body>
</html>