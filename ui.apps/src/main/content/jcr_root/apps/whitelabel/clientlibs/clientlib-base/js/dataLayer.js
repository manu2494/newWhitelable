// https://github.com/kaimallea/isMobile
!(function(a) {
  var b = /iPhone/i,
    c = /iPod/i,
    d = /iPad/i,
    e = /(?=.*\bAndroid\b)(?=.*\bMobile\b)/i,
    f = /Android/i,
    g = /(?=.*\bAndroid\b)(?=.*\bSD4930UR\b)/i,
    h = /(?=.*\bAndroid\b)(?=.*\b(?:KFOT|KFTT|KFJWI|KFJWA|KFSOWI|KFTHWI|KFTHWA|KFAPWI|KFAPWA|KFARWI|KFASWI|KFSAWI|KFSAWA)\b)/i,
    i = /Windows Phone/i,
    j = /(?=.*\bWindows\b)(?=.*\bARM\b)/i,
    k = /BlackBerry/i,
    l = /BB10/i,
    m = /Opera Mini/i,
    n = /(CriOS|Chrome)(?=.*\bMobile\b)/i,
    o = /(?=.*\bFirefox\b)(?=.*\bMobile\b)/i,
    p = new RegExp("(?:Nexus 7|BNTV250|Kindle Fire|Silk|GT-P1000)", "i"),
    q = function(a, b) {
      return a.test(b);
    },
    r = function(a) {
      var r = a || navigator.userAgent,
        s = r.split("[FBAN");
      if (
        ("undefined" != typeof s[1] && (r = s[0]),
        (s = r.split("Twitter")),
        "undefined" != typeof s[1] && (r = s[0]),
        (this.apple = {
          phone: q(b, r),
          ipod: q(c, r),
          tablet: !q(b, r) && q(d, r),
          device: q(b, r) || q(c, r) || q(d, r)
        }),
        (this.amazon = {
          phone: q(g, r),
          tablet: !q(g, r) && q(h, r),
          device: q(g, r) || q(h, r)
        }),
        (this.android = {
          phone: q(g, r) || q(e, r),
          tablet: !q(g, r) && !q(e, r) && (q(h, r) || q(f, r)),
          device: q(g, r) || q(h, r) || q(e, r) || q(f, r)
        }),
        (this.windows = {
          phone: q(i, r),
          tablet: q(j, r),
          device: q(i, r) || q(j, r)
        }),
        (this.other = {
          blackberry: q(k, r),
          blackberry10: q(l, r),
          opera: q(m, r),
          firefox: q(o, r),
          chrome: q(n, r),
          device: q(k, r) || q(l, r) || q(m, r) || q(o, r) || q(n, r)
        }),
        (this.seven_inch = q(p, r)),
        (this.any =
          this.apple.device ||
          this.android.device ||
          this.windows.device ||
          this.other.device ||
          this.seven_inch),
        (this.phone =
          this.apple.phone || this.android.phone || this.windows.phone),
        (this.tablet =
          this.apple.tablet || this.android.tablet || this.windows.tablet),
        "undefined" == typeof window)
      )
        return this;
    },
    s = function() {
      var a = new r();
      return (a.Class = r), a;
    };
  "undefined" != typeof module && module.exports && "undefined" == typeof window
    ? (module.exports = r)
    : "undefined" != typeof module &&
      module.exports &&
      "undefined" != typeof window
    ? (module.exports = s())
    : "function" == typeof define && define.amd
    ? define("isMobile", [], (a.isMobile = s()))
    : (a.isMobile = s());
})(this);

//Enumuration data for contries and languages
var isoCountries = {
  GB: "United Kingdom",
  US: "United States",
  NL: "Netherlands",
  IT: "Italy",
  NO: "Norway",
  IE: "Ireland",
  CA: "Canada",
  RU: "Russia",
  DE: "Germany",
  CH: "Switzerland",
  AT: "Austria",
  SE: "Sweden",
  DK: "Denmark",
  ES: "Spain",
  FR: "France",
  PT: "Portugal",
  BE: "Belgium",
  HU: "Hungary",
  GR: "Greece",
  JP: "Japan",
  AE: "United Arab Emirates",
  AU: "Australia",
  TW: "Taiwan",
  TR: "Turkey"
};
var isoLanguages = {
  en: "English",
  nl: "Dutch",
  it: "Italian",
  no: "Norwegian",
  fr: "French",
  ru: "Russian",
  de: "German",
  sv: "Swedish",
  da: "Danish",
  es: "Spanish",
  it: "Italian",
  pt: "Portuguese",
  hu: "Hungarian",
  el: "Greek",
  ja: "Japanese",
  ar: "Arabic",
  zh: "Chinese",
  tr: "Turkish"
};

var engage = engage || {};

//Globle object for digital data layer
window.digitalData = window.digitalData || {};
window.dataLayer = window.dataLayer || [];
document.addEventListener('DOMContentLoaded',() =>{
	const promoDetails = document.querySelector('.datalayer');
	const loginProfileId = BGC.cookie('profileID');
	let pageUrl = window.location.pathname.substring(window.location.pathname.lastIndexOf('/')+1);
	let pageType = promoDetails.getAttribute('data-type');
	if (pageUrl == 'home.html') {
		pageType ='Homepage';
	} else if (pageUrl == 'play.html') {
		pageType = 'Game';
	} else if (pageUrl == 'login.html' || pageUrl == 'register.html') {
		pageType = 'Login';
	} else if (window.location.pathname.split('/').length > 3) {
		pageType = 'Competition';
	} 
	dataLayer.push( {
		'page_type': pageType,
		'page_title': document.title,
		'login_status': loginProfileId !== null,
		'language': promoDetails.getAttribute('data-locale')
	});
	/*Initializing global objects */
	digitalData.page = {
	  pageTitle: document.title,
	  pageURL: location.protocol + "//" + location.hostname + location.pathname,
	  contentHierarchy: {
	    level1: null,
	    level2: null,
	    level3: null,
	    level4: null,
	    level5: null
	  },
	  urlHash: "",
	  environment: promoDetails.getAttribute('data-environment'),
	  propertyID: "Promotion: "+promoDetails.getAttribute('data-promotionName'),
	  siteRegion: promoDetails.getAttribute('data-region'),
	  referringURL: document.referrer,
	  previousPage: document.referrer,
	  hostname: document.location.hostname,
	  pageName: location.pathname.substring(location.pathname.lastIndexOf("/") + 1),
	  server: "Apache",
	  mktVisId: "",
	  mktOrgId: "6206682859721E8F0A495C3A",
	  pageType: "",
	  siteType: promoDetails.getAttribute('data-type'),
	  userAgent: navigator.userAgent,
	  focusBrand: "",
	  browser: navigator.appVersion,
	  browserType: navigator.appName,
	  siteCountry: "",
	  siteLanguage: "",
	  visitorState: "",
	  visitorZip: "",
	  visitorType: "",
	  section: "",
	  deviceOS: navigator.platform,
	  pageType: document.contentType,
	  dlError: window.onerror,
	  assets: {
	    id: "",
	    friendlyName: "",
	    focusBrand: ""
	  },
	  promotionname: promoDetails.getAttribute('data-promotionName'),
	  locale: "",
	  timezone: promoDetails.getAttribute('data-timezone'),
	  platform: promoDetails.getAttribute('data-platform'),
	  promotionPlatform: promoDetails.getAttribute('data-platform'),
	  promotionId: promoDetails.getAttribute('data-promotionId')
	};
	
	digitalData.user = {
	  language: navigator.language,
	  userCity: "",
	  userState: "",
	  userZip: "",
	  userID: "",
	  profile: {
	    profileID: "",
	    rewardsPoints: "",
	    userTitle: "",
	    userType: ""
	  },
	  attributes: {
	    isLoggedIn: "",
	    registrationDate: "",
	    registrationType: "new",
	    loginMethod: "",
	
	    socialLogin: {
	      socialName: "",
	      socialNetwork: ""
	    }
	  }
	};
	const creds = sessionStorage.getItem('newPromoCreds')?JSON.parse(sessionStorage.getItem('newPromoCreds')):{};
	if (creds) {
		digitalData.user.profile.profileID = creds.UsernameSHA256;
		digitalData.user.attributes.loginMethod = creds.LoginMethod;	
	}

	digitalData.video = {
	  videoname: "",
	  videoplayer: "",
	  videoduration: ""
	};
	
	digitalData.product = {
	  productInfo: {
	    productId: "NA",
	    productName: "",
	    productCategory: "",
	    productSubCategory: "NA"
	  }
	};
	
	digitalData.search = {
	  searchResults: {
	    resultsCount: "",
	    resultPageCount: "",
	    searchFiltersApplied: ""
	  },
	  searchParams: {
	    searchInfo: {
	      searchTermEntered: "",
	      searchCategory: "",
	      searchRank: "",
	      searchPageNumber: ""
	    }
	  }
	};
	
	digitalData.social = {
	  socialNetwork: "",
	  event: "",
	  contentType: ""
	};
	
	digitalData.listing = {
	  event: "",
	  listingResults: {
	    resultsCount: "",
	    resultsCountOnline: ""
	  },
	  item: {
	    retailerID: "",
	    availability: "",
	    storeLocation: "",
	
	    productInfo: {
	      productName: ""
	    }
	  },
	  listingParams: {
	    searchInfo: {
	      searchTermEntered: "",
	      searchFiltersApplied: "NA"
	    }
	  }
	};
	
	digitalData.userRegistration = {
	  custKey: ""
	};
	
	//Device type detection @Ravi(M1050926)
	(function() {
	  digitalData.user.device = navigator.platform;
	
	  if (isMobile.phone) {
	    digitalData.user.deviceType = "Mobile";
	  } else if (isMobile.tablet) {
	    digitalData.user.deviceType = "Tablet";
	  } else {
	    digitalData.user.deviceType = "Desktop";
	  }
	})();
	
	(function() {
	  if (isMobile.phone) {
	    digitalData.page.deviceType = "Mobile";
	  } else if (isMobile.tablet) {
	    digitalData.page.deviceType = "Tablet";
	  } else {
	    digitalData.page.deviceType = "Desktop";
	  }
	})();
	
	//focus brannd value @Ravi(M1050926)
	(function() {
	  if (typeof document.getElementsByName("gsaBrand")[0] !== "undefined") {
	    var focusBrand = document
	      .getElementsByName("gsaBrand")[0]
	      .getAttribute("content");
	    digitalData.page.focusBrand = focusBrand;
	  }
	})();
	
	// Visitor Type  @Ravi(M1050926)
	(function() {
	  var visitor_type = (function() {
	    if ("localStorage" in window && window["localStorage"] !== null) {
	      if ("counter" in localStorage && localStorage["counter"] !== null) {
	        return "repeated-user";
	      } else {
	        localStorage.setItem("counter", "visited");
	        return "new-user";
	      }
	    }
	  })();
	
	  digitalData.page.visitorType = visitor_type;
	})();
	
	//sitecountry and language for data layer @Ravi(M1050926)
	(function() {
	  var urlPath = window.location.pathname.split("/");
	  for (i = 0; i < urlPath.length; i++) {
	    if (urlPath[i].indexOf("_") != -1 && urlPath[i].length == 5) {
	      var localeVar = urlPath[i].split("_");
	      var language = getLanguageName(localeVar[0], isoLanguages);
	      var country = getCountryName(localeVar[1], isoCountries);
	      digitalData.page.locale = localeVar;
	      digitalData.page.siteCountry = country;
	      digitalData.page.siteLanguage = language;
	    }
		 //in case of eden
		 if(urlPath[i].length == 5&&urlPath[i].indexOf("-") != -1){
			var eden=urlPath[i].split("-");
			edenlang=eden[0]
			edencountry=eden[1].toUpperCase();
			var country = getCountryName(edencountry, isoCountries);
	        var language = getLanguageName(edenlang, isoLanguages);
			digitalData.page.siteCountry = country;
	        digitalData.page.siteLanguage = language;
		 }
	    //in case of merkle
	    if (urlPath[i].length == 2 && urlPath[i] == urlPath[i].toLowerCase()) {
	      var list = navigator.language;
	
	      if (list.indexOf("-") != -1) {
	        var lang = list.split("-");
	        languageCode = lang[0];
	        countryCode = lang[1];
	        var country = getCountryName(countryCode, isoCountries);
	        var language = getLanguageName(languageCode, isoLanguages);
	        digitalData.page.siteCountry = country;
	        digitalData.page.siteLanguage = language;
	      }
		  else {
	        var language = getLanguageName(lang, isoLanguages);
	        digitalData.page.siteLanguage = language;
	        var path = window.location.pathname.split("/");
	        var country = path[1];
	        digitalData.page.siteCountry = country;
	      }
	    }
	  }
	})();
	
	//get the content heirarchy for data layer @Ravi(M1050926)
	(function() {
	  // parse path
	  var path = location.pathname.split("/").splice(2);
	  while (path.length > 5) {
	    path.pop();
	  }
	  path.forEach(function(p, i) {
	    p = p
	      .replace(/\-/g, " ")
	      .replace(/\.html/, "")
	      .replace(/(^| )([a-z])/g, function(l) {
	        return l.toUpperCase();
	      });
	    path[i] = p;
	  });
	
	  // generate content heirarchy
	  path.forEach(function(p, i) {
	    digitalData.page.contentHierarchy["level" + (i + 1)] = p;
	    if (i == 0) {
	      digitalData.page.section = p;
	    }
	  });
	})();
	
	function getCountryName(countryCode, countryArray) {
	  if (countryArray.hasOwnProperty(countryCode)) {
	    return countryArray[countryCode];
	  }
	  return countryCode;
	}
	
	function getLanguageName(LanguageCode, languageArray) {
	  if (languageArray.hasOwnProperty(LanguageCode)) {
	    return languageArray[LanguageCode];
	  }
	  return LanguageCode;
	}
	
	//Adobe marketing organisation id and visitor id @Ravi(M1050926)
	(function() {
	  var allCookies = document.cookie;
	  cookieList = allCookies.split(";");
	  // Search for organisation id for the given key
	  for (var i = 0; i < cookieList.length; i++) {
	    arr1 = cookieList[i].split("=");
	    if (arr1[0].indexOf("AMCV_6206682859721E8F0A495C3A%40AdobeOrg") != -1) {
	      var value = arr1[1];
	      valueArray = value.split("%");
	      var adobeOrgId = valueArray[4];
	      digitalData.page.mktVisId = adobeOrgId.substr(2);
	    }
	  }
	})();
	
	//social share: @Nikitha
	(function() {
	  var parentSocialEle = document.querySelectorAll(".social_icons");
	  for (var j = 0; j < parentSocialEle.length; j++) {
	    var anchorEle = parentSocialEle[j].querySelectorAll("a");
	    for (var i = 0; i < anchorEle.length; i++) {
	      anchorEle[i].addEventListener("click", function(event) {
	        var clickedItem = event.target;
	        if (
	          clickedItem.parentElement.hasAttribute("data-tracking") &&
	          clickedItem.parentElement
	            .getAttribute("data-tracking")
	            .indexOf("socialLinks|linkout") != -1
	        ) {
	          digitalData.social.event = "Social link clicked";
	          var dataTrackingValues = clickedItem.parentElement
	            .getAttribute("data-tracking")
	            .split("|");
	          if (dataTrackingValues[2].toLowerCase().indexOf("facebook") != -1) {
	            var socialNetwork = "Facebook";
	          } else if (
	            dataTrackingValues[2].toLowerCase().indexOf("twitter") != -1
	          ) {
	            var socialNetwork = "Twitter";
	          } else if (
	            dataTrackingValues[2].toLowerCase().indexOf("instagram") != -1
	          ) {
	            var socialNetwork = "Instagram";
	          } else if (
	            dataTrackingValues[2].toLowerCase().indexOf("youtube") != -1
	          ) {
	            var socialNetwork = "YouTube";
	          } else if (
	            dataTrackingValues[2].toLowerCase().indexOf("pinterest") != -1
	          ) {
	            var socialNetwork = "Pinterest";
	          } else {
	            var socialNetwork = "";
	          }
	          digitalData.social.socialNetwork = socialNetwork;
	        }
	      });
	    }
	  }
	})();
	
	(function() {
	  var parentAddthisEle = document.querySelectorAll(".addthis_toolbox");
	  for (var j = 0; j < parentAddthisEle.length; j++) {
	    var anchorEle = parentAddthisEle[j].querySelectorAll("a");
	    for (var i = 0; i < anchorEle.length; i++) {
	      anchorEle[i].addEventListener("click", function(event) {
	        var clickedItem = event.target;
	        console.log(clickedItem.getAttribute("title"));
	        digitalData.social.event = "Social Content Shared";
	        var socialNetwork = clickedItem.getAttribute("title");
	        digitalData.social.socialNetwork = socialNetwork;
	        var sn = socialNetwork.toLowerCase();
	        if (sn === "facebook") {
	          digitalData.social.contentType = "post";
	        } else if (sn === "twitter") {
	          digitalData.social.contentType = "tweet";
	        } else if (sn === "youtube") {
	          digitalData.social.contentType = "video";
	        } else if (sn === "email") {
	          digitalData.social.contentType = "message";
	        } else if (sn === "print") {
	          digitalData.social.contentType = "text";
	        } else {
	          digitalData.social.contentType = "post";
	        }
	      });
	    }
	  }
	})();
	
	//Getting digital authorable properties @Ravi
	var elem = document.querySelector("#propid");
	if (elem != null) {
		if(elem.getAttribute("data-locale") !==null || elem.getAttribute("data-locale") !==undefined){
		    var pageLocale = elem.getAttribute("data-locale");
		}
		if(elem.getAttribute("data-userlang") !==null || elem.getAttribute("data-userlang") !==undefined){
		    var userLanguage = elem.getAttribute("data-userlang");
		}
		if(elem.getAttribute("data-sitecountry") !==null || elem.getAttribute("data-sitecountry") !==undefined){
		    var siteCountry = elem.getAttribute("data-sitecountry");
		}
		if(elem.getAttribute("data-entity") !==null || elem.getAttribute("data-entity") !==undefined){
		    var entityType = elem.getAttribute("data-entity");
		}
	}
	(function() {
	  if (elem != null) {
	    var env = elem.getAttribute("data-env");
	    var propid = elem.getAttribute("data-propid");
	    var siteReg = elem.getAttribute("data-sitereg");
		var siteType = elem.getAttribute("data-sitetype");
	
	    digitalData.page.environment = env;
	    digitalData.page.propertyID = "Promotion: " + propid;
	    digitalData.page.siteRegion = siteReg;
	
	    //adding entity, userlanguage, pagelocale,, sitecountry to authoring
	
	    if(userLanguage != null){
	    	digitalData.user.language=userLanguage
	    } else if (pageLocale != null) {
	    	digitalData.user.language=pageLocale;
		}
	    if(pageLocale != null){
			digitalData.page.locale=pageLocale
	    }
	    if(siteCountry != null){
			digitalData.page.siteCountry=siteCountry
	    }
		if(entityType != null){
			digitalData.page.entity = entityType;
	  	}
		if(siteType != null){
			digitalData.page.siteType = siteType;
	  }
		
	  }
	})();
	
	//Product details: @Mayank
	(function() {
	  if (
	    typeof document.getElementsByClassName("product-category-back")[0] !==
	    "undefined"
	  ) {
	    var prodCategory = document.getElementsByClassName(
	      "product-category-back"
	    )[0].text;
	    console.log("product Category::" + prodCategory);
	    digitalData.product.productInfo.productCategory = prodCategory;
	
	    var prodName = document.getElementsByTagName("h1")[0].innerText;
	    console.log("product Name::" + prodName);
	    digitalData.product.productInfo.productName = prodName;
	  }
	})();
	
	//Where to buy @Nikitha
	(function() {
	  var whereToBuyArray = document.querySelectorAll(".link--where-to-buy");
	  for (var i = 0; i < whereToBuyArray.length; i++) {
	    whereToBuyArray[i].addEventListener("click", function(event) {
	      setTimeout(function() {
	        populateListing();
	        //on change of dropdown
	        var skuSelector = document.querySelector("#__ps-sku-selector-1_0");
	        if (skuSelector !== null) {
	          skuSelector.addEventListener("change", function(event) {
				digitalData.listing.item.productInfo.productName = skuSelector.selectedOptions[0].getAttribute("aria-label");
	            setTimeout(function() {
	              populateListing();
	            }, 4000);
	          });
	        }
	
	        //on click of search
	        var wtbSearchButton = document.querySelector(".ps-map-location-button");
	        if (wtbSearchButton !== null) {
	          wtbSearchButton.addEventListener("click", function(event) {
				digitalData.listing.item.productInfo.productName = document.querySelector("#__ps-sku-selector-1_0").selectedOptions[0].getAttribute("aria-label");
	            setTimeout(function() {
	              populateListing();
	            }, 4000);
	          });
	        }
	      }, 10000);
	    });
	  }
	})();
	
	//On click of product where to buy
	(function() {
	  var whereToBuyElement = document.querySelector(".button-wtb");
	  if (whereToBuyElement !== null) {
	    whereToBuyElement.addEventListener("click", function(event) {
	      setTimeout(function() {
	        populateListing();
	        //on change of dropdown
	        var skuSelector = document.querySelector("#__ps-sku-selector-1_0");
	        if (skuSelector !== null) {
	          skuSelector.addEventListener("change", function(event) {
				digitalData.listing.item.productInfo.productName = skuSelector.selectedOptions[0].getAttribute("aria-label");
	            setTimeout(function() {
	              populateListing();
	            }, 4000);
	          });
	        }
	
	        //on click of search
	        var wtbSearchButton = document.querySelector(".ps-map-location-button");
	        if (wtbSearchButton !== null) {
	          wtbSearchButton.addEventListener("click", function(event) {
				 digitalData.listing.item.productInfo.productName = document.querySelector("#__ps-sku-selector-1_0").selectedOptions[0].getAttribute("aria-label");
	            setTimeout(function() {
	              populateListing();
	            }, 4000);
	          });
	        }
	      }, 4000);
	    });
	  }
	})();
	
	function populateListing() {
		if(document.querySelector("#__ps-sku-selector-1_0") !== null)
			  digitalData.listing.item.productInfo.productName = document.querySelector("#__ps-sku-selector-1_0").selectedOptions[0].getAttribute("aria-label");
		  else if(document.querySelector(".ps-product-name") != null)
			  digitalData.listing.item.productInfo.productName = document.querySelector(".ps-product-name").innerHTML;
	  if (document.querySelector(".ps-product-name") != null) {    
	    var onlineSellers = document.querySelector(".ps-online-sellers");
	    if (onlineSellers !== null)
	      var onlineSellersCount = onlineSellers.childElementCount;
	    else var onlineSellersCount = "";
	    var localSellers = document.querySelector(".ps-local-sellers");
	    if (localSellers !== null)
	      var localSellersCount = localSellers.childElementCount;
	    else var localSellersCount = "";
	    digitalData.listing.listingResults.resultsCount = localSellersCount;
	    digitalData.listing.listingResults.resultsCountOnline = onlineSellersCount;
	    digitalData.listing.listingParams.searchInfo.searchTermEntered = document.querySelector(
	      ".ps-map-location-textbox"
	    ).value;
	  }
	}
	
	//Getting content type value for page @Ravi(M1050926)
	(function() {
	  var metas = document.getElementsByTagName("meta");
	  if (metas.length > 0) {
	    for (i = 0; i < metas.length; i++) {
	      if (
	        metas[i].hasAttribute("http-equiv") &&
	        metas[i].getAttribute("http-equiv") == "content-type"
	      ) {
	        digitalData.page.pageType = metas[i]
	          .getAttribute("content")
	          .split(";")[0];
	      }
	    }
	  }
	})();
	//Getting urlHash @Ravi(M1050926)
	(function() {
	  if (window.location.hash != undefined && window.location.hash != "") {
	    var hashVal = window.location.hash;
	    digitalData.page.urlHash = hashVal.substr(1);
	  }
	})();
});
document.addEventListener('DOMContentLoaded',() =>{
	setTimeout(() =>{
		const launch = document.querySelector('.adobelaunchconfig');
		if (launch) {
			const footer = document.getElementById('footerbvscipt');
			const src = footer.getAttribute('src');
			const script = document.createElement('script');
			script.setAttribute('async','');
			script.setAttribute('src',src);
			
			script.setAttribute('type',"text/javascript");
			footer.appendChild(script);
		}
	},250);
});