$(document).ready(function() {
	menuNavigation();
});

function menuNavigation() {
	$("li").hide();
	$("#leftSection div").on("click", function(event) {
		$("li").on("click", function(e) {
			e.stopPropagation()
		});
		$(this).find("ul li").toggle();
	});
};