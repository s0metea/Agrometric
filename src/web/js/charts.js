var pubnub = new PubNub({
    publishKey: '',
    subscribeKey: 'sub-c-8101ca80-bf08-11e6-b38f-02ee2ddab7fe'
});

var eon_cols = ["temperature"];
var eon_labels = [];
var chart = eon.chart({
    pubnub: pubnub,
    channels: ["main-channel"],
    transform: function(message) {
        message = eon.c.flatten(message.eon);
        var o = {};
        for(index in message) {
            if(eon_cols.indexOf(index) > -1){
                o[eon_labels[index] || index] = message[index];
                $("#temperature").text(message[index] + "°C");
            }
        }
        return {
            eon: o
        };
    }
});

//Gauge example
var eon_cols2 = ["speed"];
var chart2 = eon.chart({
    pubnub: pubnub,
    channels: ["main-channel"],
    generate: {
        bindto: "#speed",
        data: {
            type: "gauge"
        },
        gauge: {
            min: 0,
            max: 100
        },
        color: {
            pattern: ['#F6C600', '#60B044', '#FF0000'],
            threshold: {
                values: [0, 40, 80]
            }
        }
    },
    transform: function(message) {
        message = eon.c.flatten(message.eon);
        var o = {};
        for(index in message) {
            if(eon_cols2.indexOf(index) > -1){
                o[eon_labels[index] || index] = message[index];
            }
        }
        return {
            eon: o
        };
    }
});

var eon_cols3 = ["moisture"];
var chart3 = eon.chart({
    pubnub: pubnub,
    channels: ["main-channel"],
    generate: {
        bindto: "#moisture",
        data: {
            type: "gauge"
        },
        gauge: {
            min: 0,
            max: 100
        },
        color: {
            pattern: ['#FF0000', '#F6C600', '#60B044'],
            threshold: {
                values: [30, 50, 90]
            }
        }
    },
    transform: function(message) {
        message = eon.c.flatten(message.eon);
        var o = {};
        for(index in message) {
            if(eon_cols3.indexOf(index) > -1){
                o[eon_labels[index] || index] = message[index];
                $(".moisture").text(message[index] + "%");
            }
        }
        return {
            eon: o
        };
    }
});

var eon_cols4 = ["waterLevel"];
chart = eon.chart({
    pubnub: pubnub,
    channels: ["main-channel"],
    flow: true,
    history: true,
    limit: 25,
    generate: {
        bindto: "#waterLevel",
        data: {
            type: "spline"
        },
        color: {
            pattern: ['#1c1c1c', '#ff0000']
        },

        tooltip: {
            show: false
        },
        point: {
            show: true
        }
    },
    transform: function(message) {
        message = eon.c.flatten(message.eon);
        var o = {};
        for(index in message) {
            if(eon_cols4.indexOf(index) > -1){
                o[eon_labels[index] || index] = message[index];
                $(".waterLevel").text(Math.round(message[index]) + "%");
            }
        }
        return {
            eon: o
        };
    }
});

var eon_cols5 = ["pressure"];
chart = eon.chart({
    pubnub: pubnub,
    channels: ["main-channel"],
    history: true,
	limit: 25,
    flow: true,
    generate: {
        bindto: "#pressure",
        data: {
            type: "spline"
        },
        color: {
            pattern: ['#1c1c1c'],
        },
        tooltip: {
            show: false
        },
        point: {
            show: true
        }
    },
    transform: function(message) {
        message = eon.c.flatten(message.eon);
        var o = {};
        for(index in message) {
            if(eon_cols5.indexOf(index) > -1){
                o[eon_labels[index] || index] = message[index];
                $(".pressure").text(message[index] + " мм рт.ст.");
            }
        }
        return {
            eon: o
        };
    }
});


var eon_cols7 = ["fuel"];
var fuelGauge = eon.chart({
    pubnub: pubnub,
    channels: ["main-channel"],
    generate: {
        bindto: "#fuel",
        data: {
            type: "gauge"
        },
        gauge: {
            min: 0,
            max: 100
        },
        color: {
            pattern: ['#FF0000', '#F6C600', '#60B044'],
            threshold: {
                values: [15, 45, 85]
            }
        }
    },
    transform: function(message) {
        message = eon.c.flatten(message.eon);
        var o = {};
        for(index in message) {
            if(eon_cols7.indexOf(index) > -1){
                o[eon_labels[index] || index] = message[index];
            }
        }
        return {
            eon: o
        };
    }
});

var eon_cols8 = ["ph"];
var phGauge = eon.chart({
    pubnub: pubnub,
    channels: ["main-channel"],
    generate: {
        bindto: "#ph",
        data: {
            type: "gauge"
        },
        gauge: {
            min: 0,
            max: 14
        },
        color: {
            pattern: ['#F6C600', '#60B044', '#FF0000'],
            threshold: {
                values: [0, 7, 14]
            }
        }
    },
    transform: function(message) {
        message = eon.c.flatten(message.eon);
        var o = {};
        for(index in message) {
            if(eon_cols8.indexOf(index) > -1){
                o[eon_labels[index] || index] = message[index];
                $(".ph").text(message[index]);
            }
        }
        return {
            eon: o
        };
    }
});



var startX = 30.545, startY = 59.854;
var currentX = startX, currentY = startY;

mapboxgl.accessToken = 'pk.eyJ1Ijoic29tZXRlYSIsImEiOiJjaXdtZ29zaG4wMDBtMnRxbjhkaGhid3RxIn0.WZQUJY8Fit-bAnjEZUy63A';
var map = new mapboxgl.Map({
    container: 'map',
    style: 'mapbox://styles/sometea/ciwmhdcn000312qpd7a554t9h',
    center: [currentX, currentY],
    zoom: 13
});

function movePoint() {
    return {
        "type": "Point",
        "coordinates": [
            currentX,
            currentY
        ]
    };
}

var eon_cols9 = ["latLng.lat", "latLng.lng"];
var eon_labels9 = [];
var latLng = eon.chart({
    pubnub: pubnub,
    channels: ["main-channel"],
    transform: function(message) {
        message = eon.c.flatten(message.eon);
        var o = {};
        for(index in message) {
            if(index == "latLng.lat")
                currentX = message[index];
            if(index == "latLng.lng")
                currentY = message[index];
        }
        return {
            eon: o
        };
    }
});


map.on('load', function () {
    // Add a source and layer displaying a point which will be animated in a circle.
    map.addSource('point', {
        "type": "geojson",
        "data": movePoint()
    });

    map.addLayer({
        "id": "point",
        "source": "point",
        "type": "circle",
        "paint": {
            "circle-radius": 5,
            "circle-color": "#007cbf"
        }
    });

    function animateMarker(timestamp) {
        // Update the data to a new position based on the animation timestamp. The
        // divisor in the expression `timestamp / 1000` controls the animation speed.
        map.getSource('point').setData(movePoint(timestamp));

        // Request the next frame of the animation.
        requestAnimationFrame(animateMarker);
    }

    // Start the animation.
    animateMarker(0);
});