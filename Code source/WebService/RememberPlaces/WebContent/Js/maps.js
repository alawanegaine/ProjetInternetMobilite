$(document).ready(function() {
    $("#refresh").click(function(event) {
    	$.mobile.loading('show', {
    	    theme: "b",
    	    text: "Loading point...",
    	    textonly: true,
    	    textVisible: true
    	});
    	getLocation(function(position){
    		if(position == null){
    			alert("requête non envoyée car pas de géolocalisation");
    		}
    		else {
            	// construct json to send
            	var Json = {
         			   latitude : position.latitude ,
        			   longitude : position.longitude,
        			   kmMax : $("#kmMax").val(),
        			   nbMoisMax : $("#nbMoisMax").val()
        	    }  
            	$.ajax({
         		   url: 'GetListPoints',
         		   type: 'POST',
         		   async : true,
          		   dataType: "json",
          		   contentType: 'application/json; charset=utf-8',
         		   data: JSON.stringify(Json),
         		   error: function() {
         		   },
         		   success: function(data) {
         			  $.mobile.loading('hide', {});
         			   GoogleMaps(data);
         		   }
        		});
    		}
    	});  	
  });    
  $("#refresh").trigger('click');
});
// Google Maps
function GoogleMaps(data) {

	// Setup the different icons and shadows
	var iconURLPrefix = 'http://maps.google.com/mapfiles/ms/icons/';
	var icons = [
	             iconURLPrefix + 'red-dot.png',
	             iconURLPrefix + 'green-dot.png',
	             iconURLPrefix + 'blue-dot.png',
	             iconURLPrefix + 'orange-dot.png',
	             iconURLPrefix + 'purple-dot.png',
	             iconURLPrefix + 'pink-dot.png',      
	             iconURLPrefix + 'yellow-dot.png'
	             ]
	var iconsLength = icons.length;

	var map = new google.maps.Map(document.getElementById('map'), {
		zoom: 10,
		center: new google.maps.LatLng(45.378697, -71.928295),
		mapTypeId: google.maps.MapTypeId.ROADMAP,
		mapTypeControl: false,
		streetViewControl: false,
		panControl: false,
		zoomControlOptions: {
			position: google.maps.ControlPosition.LEFT_BOTTOM
		}
	});

	var infowindow = new google.maps.InfoWindow({
		maxWidth: 160
	});

	var markers = new Array();

	var iconCounter = 0;

	// Add the markers and infowindows to the map
	for (var i in data.listPoints) {  
		var point = data.listPoints[i];
		
		var marker = new google.maps.Marker({
			position: new google.maps.LatLng(point.latitude, point.longitude),
			map: map,
			icon: icons[iconCounter]
		});

		markers.push(marker);

		google.maps.event.addListener(marker, 'click', (function(marker, i) {
			return function() {
				infowindow.setContent(""+data.listPoints[i].idPhoto);
				infowindow.open(map, marker);
			}
		})(marker, i));

		iconCounter++;
		// We only have a limited number of possible icon colors, so we may have to restart the counter
		if(iconCounter >= iconsLength) {
			iconCounter = 0;
		}
	}

	function autoCenter() {
		//  Create a new viewpoint bound
		var bounds = new google.maps.LatLngBounds();
		//  Go through each...
		for (var i = 0; i < markers.length; i++) {  
			bounds.extend(markers[i].position);
		}
		//  Fit these bounds to the map
		map.fitBounds(bounds);
	}
	autoCenter();
}
function getLocation(callback) {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position){
        	var coords = {
        	       	latitude : position.coords.latitude,
        	       	longitude : position.coords.longitude
    		} ;
        	callback(coords);
        });
    } else {
        alert("Geolocation is not supported by this browser.");
        callback(null);
    }
}