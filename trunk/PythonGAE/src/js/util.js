/**
 * Returns an XMLHttp instance to use for asynchronous downloading. This method
 * will never throw an exception, but will return NULL if the browser does not
 * support XmlHttp for any reason.
 * 
 * @return {XMLHttpRequest|Null}
 */
function createXmlHttpRequest() {
	try {
		if (typeof ActiveXObject != 'undefined') {
			return new ActiveXObject('Microsoft.XMLHTTP');
		} else if (window["XMLHttpRequest"]) {
			return new XMLHttpRequest();
		}
	} catch (e) {
		changeStatus(e);
	}
	return null;
};

/**
 * This functions wraps XMLHttpRequest open/send function. It lets you specify a
 * URL and will call the callback if it gets a status code of 200.
 * 
 * @param {String}
 *            url The URL to retrieve
 * @param {Function}
 *            callback The function to call once retrieved.
 */
function downloadUrl(url, type, data, callback) {
	var status = -1;
	var request = createXmlHttpRequest();
	if (!request) {
		return false;
	}

	request.onreadystatechange = function() {
		if (request.readyState == 4) {
			try {
				status = request.status;
			} catch (e) {
				// Usually indicates request timed out in FF.
			}
			if (status == 200) {
				callback(request.responseText);
				request.onreadystatechange = function() {
				};
			}
		}
	}
	request.open(type, url, true);
	if (type == "POST") {
		request.setRequestHeader("Content-type",
				"application/x-www-form-urlencoded");
		request.setRequestHeader("Content-length", data.length);
		request.setRequestHeader("Connection", "close");
	}

	try {
		request.send(data);
	} catch (e) {
		changeStatus(e);
	}
};

function downloadScript(url) {
	var script = document.createElement('script');
	script.src = url;
	document.body.appendChild(script);
}

function Request(function_name, opt_argv) {

	if (!opt_argv)
		opt_argv = new Array();

	// Find if the last arg is a callback function; save it
	var callback = null;
	var len = opt_argv.length;
	if (len > 0 && typeof opt_argv[len - 1] == 'function') {
		callback = opt_argv[len - 1];
		opt_argv.length--;
	}
	var async = (callback != null);

	// Build an Array of parameters, w/ function_name being the first parameter
	var params = new Array(function_name);
	for ( var i = 0; i < opt_argv.length; i++) {
		params.push(opt_argv[i]);
	}
	var body = JSON.stringify(params);

	// Create an XMLHttpRequest 'POST' request w/ an optional callback handler
	var req = new XMLHttpRequest();
	req.open('POST', '/rpc', async);

	req.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	req.setRequestHeader("Content-length", body.length);
	req.setRequestHeader("Connection", "close");

	if (async) {
		req.onreadystatechange = function() {
			if (req.readyState == 4 && req.status == 200) {
				var response = null;
				try {
					response = JSON.parse(req.responseText);
				} catch (e) {
					response = req.responseText;
				}
				callback(response);
			}
		}
	}

	// Make the actual request
	req.send(body);
}

// Adds a stub function that will pass the arguments to the AJAX call
function InstallFunction(obj, functionName) {
	obj[functionName] = function() {
		Request(functionName, arguments);
	}
}

// starts here

// Server object that will contain the callable methods
var server = {};

// Insert 'Add' as the name of a callable method
InstallFunction(server, 'ajaxAddSerie');
InstallFunction(server, 'ajaxRemoveSerie');
InstallFunction(server, 'ajaxEditEpisodes');
InstallFunction(server, 'ajaxDropSerie');
InstallFunction(server, 'ajaxViewSerie');

// Client function that calls a server rpc and provides a callback
function doAdd(keys) {
	server.ajaxAddSerie(keys, onAddSuccess);
}
function doRemove(keys) {
	server.ajaxRemoveSerie(keys, null);
}
function doDrop(keys) {
	server.ajaxDropSerie(keys, null);
}
function doEditEpisodes(keys, val) {
	server.ajaxEditEpisodes(keys, val, null);
}
function doViewSerie(key) {
	server.ajaxViewSerie(key, onViewSuccess);
}

//Callback for after a successful viewSerie
function onViewSuccess(response) {
	document.getElementById("view-serie").innerHTML = response;

	$(function() {
		$('#view-serie').dialog('open');
	});
}


// Callback for after a successful doAdd
function onAddSuccess(response) {
	if (response == '-1') {
//		document.getElementById('error').style.display = 'block';
		$(function() {
			$("#error").css( {
				display : 'block'
			}).animate( {
				opacity : 1
			}, 500, 'swing');
		});
		document.getElementById("error-message").innerHTML = 'You are not logged in !';
		return false;
	}
//	document.getElementById('feedback').style.display = 'block';
	$(function() {
		$("#feedback").css( {
			display : 'block'
		}).fadeIn(3000, function() {
	        // Animation complete
		});

	});
	document.getElementById("feedback-message").innerHTML = response + ' serie(s) added';
}

var mySeries = '/mySeries';
var series = '/search';
function redirect(path) {
	window.location = path;
}

function addSelected() {
	$(function() {
		if (jQuery("#selectable").find('tr').hasClass("ui-selected")) {
			document.getElementById("feedback-message").innerHTML = '';
			var keys = new Array();
			jQuery("#selectable").find('tr').each(function() {
				if ($(this).hasClass("ui-selected")) {
					var key = $(this).find('td').html();
					keys.push(key);
				}
			});
			doAdd(keys);
		}
	});
}

function removeSelected() {
	var keys = new Array();
	jQuery("#selectable").find('tr').each(function() {
		if ($(this).hasClass("ui-selected")) {
			var key = $(this).find('td').html();
			keys.push(key);
		}
	});
	doRemove(keys);
	$('#dialog-confirm').dialog('close');
	redirect(mySeries);
}

function dropSelected() {
	var keys = new Array();
	jQuery("#selectable").find('tr').each(function() {
		if ($(this).hasClass("ui-selected")) {
			var key = $(this).find('td').html();
			keys.push(key);
		}
	});
	doDrop(keys);
	$('#dialog-confirm').dialog('close');
	redirect(series);
}

function callFunction(xFunc) {
	eval(xFunc + "()");
}

function confirm(functionName) {
	$(function() {
		if (jQuery("#selectable").find('tr').hasClass("ui-selected")) {
			jQuery("#dialog-confirm").dialog( {
				resizable : false,
				height : 150,
				modal : true,
				buttons : {
					'OK' : function() {
						callFunction(functionName);
					},
					Cancel : function() {
						$(this).dialog('close');
					}
				}
			});
		}
	});
}

function updateTips(t) {
	tips.text(t).effect("highlight", {}, 1500);
}

function checkLength(o, n, min, max) {
	if (o.val().length > max || o.val().length < min) {
		o.addClass('ui-state-error');
		updateTips("Length of " + n + " must be between " + min + " and " + max
				+ ".");
		return false;
	} else {
		return true;
	}
}

function checkRegexp(o, regexp, n) {
	if (!(regexp.test(o.val()))) {
		o.addClass('ui-state-error');
		updateTips(n);
		return false;
	} else {
		return true;
	}
}

function addSerie(key) {
	var keys = new Array(key);
	doAdd(keys);
}

function viewSerie(key) {
	doViewSerie(key);
}

function createSerie() {
	$(function() {
		$('#dialog').dialog('open');
	});
}

function editEpisodes() {
	$(function() {
		objects = $("#selectable").find('tr.ui-selected');
		if (objects.length > 0) {
			// fill in the episode field with the value of the selected line
			if (objects.length == 1) {
				var episodes = objects.find('td.episodes');
				if (episodes.html() != "all")
					$('#episodes').val(episodes.html());
			}
			$('#dialog').dialog('open');
		}
	});
}

function init() {
	$(function() {
		$("button.add").button( {
			icons : {
				primary : 'ui-icon-circle-plus'
			}
		});
		$("button.remove").button( {
			icons : {
				primary : 'ui-icon-circle-minus'
			}
		});
		$("button.edit").button( {
			icons : {
				primary : 'ui-icon-wrench'
			}
		});
		$("button.search").button( {
			icons : {
				primary : 'ui-icon-search'
			}
		});
		$("button.short-view").button( {
			text : false,
			icons : {
				primary : 'ui-icon-zoomin'
			}
		});
		$("button.short-add").button( {
			text : false,
			icons : {
				primary : 'ui-icon-plusthick'
			}
		});

		jQuery("#selectable").selectable();

		$('button').hover(function() {
			$(this).addClass("ui-state-hover");
		}, function() {
			$(this).removeClass("ui-state-hover");
		}).mousedown(function() {
			$(this).addClass("ui-state-active");
		}).mouseup(function() {
			$(this).removeClass("ui-state-active");
		});

		$('.bubbleInfo').each(function() {
			var distance = 10;
			var time = 250;
			var hideDelay = 500;

			var hideDelayTimer = null;

			var beingShown = false;
			var shown = false;
			var trigger = $('.trigger', this);
			var info = $('.popup', this).css('opacity', 0);

			$( [ trigger.get(0), info.get(0) ]).mouseover(function() {
				if (hideDelayTimer)
					clearTimeout(hideDelayTimer);
				if (beingShown || shown) {
					// don't trigger the animation again
					return;
				} else {
					// reset position of info box
					beingShown = true;

					info.css( {
						top : 0,
						left : 0,
						display : 'block'
					}).animate( {
						top : '-=' + distance + 'px',
						opacity : 1
					}, time, 'swing', function() {
						beingShown = false;
						shown = true;
					});
				}

				return false;
			}).mouseout(function() {
				if (hideDelayTimer)
					clearTimeout(hideDelayTimer);
				hideDelayTimer = setTimeout(function() {
					hideDelayTimer = null;
					info.animate( {
						top : '-=' + distance + 'px',
						opacity : 0
					}, time, 'swing', function() {
						shown = false;
						info.css('display', 'none');
					});

				}, hideDelay);

				return false;
			});
		});
	});
}
