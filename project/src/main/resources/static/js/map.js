const labels = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';

let map, infowindow, placeSearch;
let labelIndex = 0;

function initMap() {
    map = new google.maps.Map(document.getElementById('map'), {
        center: {
            lat: 50.431782,
            lng: 30.516382
        },
        zoom: 20
    });

    infoWindow = new google.maps.InfoWindow;

    autocomplete = new google.maps.places.Autocomplete(
        document.getElementById('autocomplete'), {
            types: ['geocode']
        });

    // This event listener calls addMarker() when the map is clicked.
    google.maps.event.addListener(map, 'click', function (event) {
        addMarker(event.latLng, map);
    });


    // // Try HTML5 geolocation.
    // if (navigator.geolocation) {
    //     navigator.geolocation.getCurrentPosition(function (position) {
    //         let pos = {
    //             lat: position.coords.latitude,
    //             lng: position.coords.longitude
    //         };

    //         infoWindow.setPosition(pos);
    //         infoWindow.setContent('Location found.');
    //         infoWindow.open(map);
    //         map.setCenter(pos);
    //     }, function () {
    //         handleLocationError(true, infoWindow, map.getCenter());
    //     });
    // } else {
    //     // Browser doesn't support Geolocation
    //     handleLocationError(false, infoWindow, map.getCenter());
    // }
}

function geolocate() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
            let geolocation = {
                lat: position.coords.latitude,
                lng: position.coords.longitude
            };
            let circle = new google.maps.Circle({
                center: geolocation,
                radius: position.coords.accuracy
            });
            autocomplete.setBounds(circle.getBounds());
        });
    }
}


// Adds a marker to the map.
function addMarker(location, map) {
    // Add the marker at the clicked location, and add the next-available label
    // from the array of alphabetical characters.
    let marker = new google.maps.Marker({
        position: location,
        label: labels[labelIndex++ % labels.length],
        map: map
    });
}


function handleLocationError(browserHasGeolocation, infoWindow, pos) {
    infoWindow.setPosition(pos);
    infoWindow.setContent(browserHasGeolocation ?
        'Error: The Geolocation service failed.' :
        'Error: Your browser doesn\'t support geolocation.');
    infoWindow.open(map);
}

$(document).ready(function () {
    $('#fullpage').fullpage({
        autoScrolling: true,
        scrollHorizontally: true
    });

    $('.ui.form .ui.selection.dropdown').dropdown({
        clearable: true
    });

    $('.clearable.example .ui.inline.dropdown').dropdown({
        clearable: true,
        placeholder: 'any'
    });


    $('select.dropdown').dropdown();
});