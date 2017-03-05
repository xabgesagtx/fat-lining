var defaultDays = 30;

var getCharts = function(days) {
	$.get("/measurements/charts?days=" + days, function(data) {
		if (data.valid) {
			setDaysInCookie(days);
			setActiveButton(days);
			var markings = [];
			if (data.goal) {
				markings = [{ color: '#F00', lineWidth: 2, yaxis: { from: data.goal, to: data.goal } }];
			}
			var values = [];
			if (data.withTrend) {
				values = [data.measurements, data.trend];
			} else {
				values = [data.measurements];
			}
			$.plot($("#placeholder"), values, {
				xaxis: { 
					mode: "time" 
				},
				yaxis: {
					min: data.minValue,
					max: data.maxValue
				},
				grid: {
					markings: markings
				}
			});
		}
	});	
}

var getDaysFromCookie = function() {
	var days = Cookies.get("days");
	if (days) {
		return days;
	} else {
		return defaultDays;
	}
}

var setDaysInCookie = function(days) {
	Cookies.set("days", days, {expires : 90});
}

var setActiveButton = function(days) {
	$("#charts-navigation button").each(function() {
		var daysButton = $(this).data("days");
		if (daysButton == days) {
			$(this).addClass("active");
		} else {
			$(this).removeClass("active");
		}
	});
} 

$(document).ready(function(){
	$("#charts-navigation button").click(function() {
		var days = $(this).data("days");
		getCharts(days);
	});
	getCharts(getDaysFromCookie());
});