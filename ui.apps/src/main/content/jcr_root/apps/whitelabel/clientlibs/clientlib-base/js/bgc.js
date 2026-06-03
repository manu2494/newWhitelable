
// BGC.js.min    // contains Tom Deater's race condition hack
// create our namespace

var BGC = BGC || {};

BGC.config = {
	firebugURL: "http://getfirebug.com/releases/lite/1.2/firebug-lite-compressed.js",

	gaURL: (("https:" == document.location.protocol) ? "https://ssl." : "http://www.") + "google-analytics.com/ga.js",

	debug: false
};

// console logging (loads firebug lite if needed)

BGC.log = function () {
	if (BGC.config.debug || window.location.hash.match(/debug/i)) {
		try {
			if (typeof loadFirebugConsole === "function" && typeof console === "undefined") {
				window.loadFirebugConsole();
			}
			console.log.apply(this, arguments);
		} catch (err) {
			BGC.log._arg.push(arguments);
			if (!BGC.loadscript[BGC.config.firebugURL]) {
				BGC.loadscript(BGC.config.firebugURL, function () {
					try {
						var a;
						firebug.init();
						while ((a = BGC.log._arg.shift())) {
							BGC.log(a);
						}
					} catch (err) {}
				});
			}
		}
	}
};

BGC.log._arg = [];


// Override BGC.track functions to use Google Tag Manager, code by Sean McMillan
// Cut out other BGC.track associated functions and vars; no longer used.
// no need to init with GTM; jQuery tracking code setup is in tracking-init.js

window.BGC = window.BGC || {};

window.dataLayer = window.dataLayer || [];

BGC.track=function(str,callback){
	if (!str || str.match(/\//)) {
		// natural (null) or virtual pageView ('action/foo/bar')
		str = str || location.pathname + location.search;
		dataLayer.push({
			event: 'pageview',
			url: str
		});
	} else if (str.match(/\|/)) {
		// event (foo|click|bar)
		str = str.split('|');

		if (str.length === 3) {
			dataLayer.push({
				event: 'event',
				category: str[0],
				action: str[1],
				label: str[2]
			});
		} else if (str.length === 4) {
			dataLayer.push({
				event: 'event',
				category: str[0],
				action: str[1],
				label: str[2],
				value: parseInt(str[3], 10)
			});
		} else {
			throw new Error('Invalid event tracking string', str.join('|'));
		}
	} else {
		throw new Error('Invalid event tracking string', str);
	}

	if (typeof callback === 'function') {
		window.setTimeout(callback, 500);
	}
};


// on-demand script loading

BGC.loadscript = function (url, callback) {
	if (!BGC.loadscript[url]) {
		BGC.loadscript[url] = "loading";
		var done = false, head = document.getElementsByTagName('head')[0], script = document.createElement('script');
		script.setAttribute('type', 'text/javascript');
		script.setAttribute('async', true);
		script.setAttribute('src', url);
		script.onload = script.onreadystatechange = script.onerror = function () {
			BGC.log(url, "readystate", this.readyState);
			if (!done && (!this.readyState || this.readyState == "loaded" || this.readyState == "complete")) {
				done = true;
				BGC.loadscript[url] = "complete";
				if (typeof callback === "function") {
					callback();
				}
				script.onload = script.onreadystatechange = script.onerror = null;
				head.removeChild(script);
			}
		};

		head.appendChild(script);
	}
};

// client-side caching, see http://www.dustindiaz.com/javascript-cache-provider/
BGC.CacheProvider = function () {
	this._cache = {};
};

try {
	BGC.CacheProvider.hasLocalStorage = ('localStorage' in window) && window['localStorage'] !== null && typeof Storage !== 'undefined';
} catch (ex) {
	BGC.CacheProvider.hasLocalStorage = false;
}

if (BGC.CacheProvider.hasLocalStorage) {
	Storage.prototype.setObject = function (key, value) {
		this.setItem( key, JSON.stringify(value) );
	};

	Storage.prototype.getObject = function (key) {
		return JSON.parse( this.getItem(key) );
	};
}

BGC.CacheProvider.prototype = {

	/**
	 * {String} k - the key
	 * {Boolean} local - get this from local storage?
	 * {Boolean} o - is the value you put in local storage an object?
	 */
	get: function (k, local, o) {
		if (local && BGC.CacheProvider.hasLocalStorage) {
			var action = o ? 'getObject' : 'getItem';
			return localStorage[action](k) || undefined;
		} else {
			return this._cache[k] || undefined;
		}
	},

	/**
	 * {String} k - the key
	 * {Object} v - any kind of value you want to store
	 * however only objects and strings are allowed in local storage
	 * {Boolean} local - put this in local storage
	 */
	set: function (k, v, local) {
		if (local && BGC.CacheProvider.hasLocalStorage) {
			if (typeof v !== 'string') {
				// make assumption if it's not a string, then we're storing an object
				localStorage.setObject(k, v);
			} else {
				try {
					localStorage.setItem(k, v);
				} catch (ex) {
					if (ex.name == 'QUOTA_EXCEEDED_ERR') {
						// developer needs to figure out what to start invalidating
						throw new Exception(v);
						return;
					}
				}
			}
		} else {
			// put in our local object
			this._cache[k] = v;
		}
		// return our newly cached item
		return v;
	},

	/**
	 * {String} k - the key
	 * {Boolean} local - put this in local storage
	 * {Boolean} o - is this an object you want to put in local storage?
	 */
	clear: function (k, local, o) {
		if (local && BGC.CacheProvider.hasLocalStorage) {
			localStorage.removeItem(k);
		}
		// delete in both caches - doesn't hurt.
		delete this._cache[k];
	}

};

// cookie, querystring, and dom utilities

BGC.cookie = function (name, value, options) { // adapted from http://www.stilbuero.de/2006/09/17/cookie-plugin-for-jquery/
	if (typeof value != 'undefined') { // name and value given, set cookie
		options = options || BGC.cookie.options;
		if (!isNaN(options)) { // options can be an object, or a number representing days to expiration
			options = {
				expires: options * 1 // coerce to number in case we've received a string from Flash
			};
		}
		if (value === null) {
			value = '';
			options.expires = -1;
		}
		var expires = '';
		if (options.expires && (!isNaN(options.expires) || options.expires.toUTCString)) {
			var date;
			if (!isNaN(options.expires)) {
				date = new Date();
				date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
			} else {
				date = options.expires;
			}
			expires = '; expires=' + date.toUTCString(); // use expires attribute, max-age is not supported by IE
		}
		var path = options.path ? '; path=' + options.path : '';
		var domain = options.domain ? '; domain=' + options.domain : '';
		var secure = options.secure ? '; secure' : '';
		document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
	} else { // only name given, get cookie
		var cookieValue = null;
		if (document.cookie && document.cookie !== '') {
			var cookies = document.cookie.split(';');
			for (var i = 0, l = cookies.length; i < l; i++) {
				var cookie = cookies[i].replace(/^\s+|\s+$/g, "");
				// Does this cookie string begin with the name we want?
				if (cookie.substring(0, name.length + 1) == (name + '=')) {
					cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
					break;
				}
			}
		}
		return cookieValue;
	}
};

// extending cookie object to make it possible to set cookie options from Flash externalInterface

BGC.cookie.options = {};

BGC.cookie.setOption = function (name, value) {
	BGC.cookie.options[name] = value;
};

BGC.cookie.clearOptions = function () {
	BGC.cookie.options = {};
};

BGC.query = (function () {
	var qString, queryStart, query, parts, bits, subbits, returnVals = {};
	qString = window.location.toString();
	queryStart = qString.indexOf('?');
	if (queryStart === -1) {
		return returnVals;
	}
	query = qString.substring(queryStart + 1, qString.length);
	parts = query.split("&");
	for (var i = 0; i < parts.length; i++) {
		bits = parts[i].split("=");
		if (bits[1]) {
			subbits = bits[1].split("#");
			returnVals[bits[0].toLowerCase()] = subbits[0]; // query properties are lowercase!
		}
	}
	return returnVals;
}) ();

BGC.getPageSize = function () {
	var x = Math.max(document.documentElement.scrollWidth || document.body.scrollWidth, document.body.offsetWidth);
	var y = Math.max(document.documentElement.scrollHeight || document.body.scrollHeight, document.body.offsetHeight);
	return {"x": x, "y": y};
};

BGC.getViewportSize = function () {
	var x = self.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
	var y = self.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
	return {"x": x, "y": y};
};

BGC.getScrollOffset = function () {
	var x = self.pageXOffset || document.documentElement.scrollLeft || document.body.scrollLeft;
	var y = self.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
	return {"x": x, "y": y};
};

BGC.getElemPosition = function (el) {
	var x = 0, y = 0;
	if (el.offsetParent) {
		do {
			x += el.offsetLeft;
			y += el.offsetTop;
		} while ((el = el.offsetParent));
	}
	return {"x": x, "y": y};
};

BGC.popup = function (URL, windowName, width, height) {
	var options = "scrollbars,resizable,menubar,toolbar,status";
	if (width && height) {
		var w = screen.availWidth;
		var h = screen.availHeight;
		var leftPos = Math.round( (w - width) / 2 );
		var topPos = Math.round( (h - height) / 2 );
		var centerOnScreen = "top=" + topPos + ",left=" + leftPos + ",width=" + width + ",height=" + height;
		var options = centerOnScreen + "," + options;
	}
	var msgWindow = window.open(URL, windowName, options);
	if(!msgWindow) {
		return false;
	} else {
		msgWindow.focus();
	}
	return true;
};
