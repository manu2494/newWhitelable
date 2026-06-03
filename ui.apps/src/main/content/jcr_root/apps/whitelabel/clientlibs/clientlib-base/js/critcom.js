var critCom = {
	log: function(term) { sconsole.log('CritCom', term, 'background:#eee;color:#d00') },
    init: function() {
        critCom.log('init')
        if (!document.getElementById('error_wp')) {
            critCom.log('init failed no config');
            return false;
        }
        var domainURL = window.location.host
        domainURL = domainURL ? domainURL : window.location.protocol+"\\"+window.location.hostname;
        var serviceUrl = document.getElementById('error_wp').getAttribute('data-service-url');
        serviceUrl = serviceUrl+"/bin/ccquerybuilder.jsonp";
        critCom.log("Checking Critical Messages for this domain.");

        var data = {
            propertyName: "sitepath",
            propertyValue: domainURL,
            callback: 'critCom.handleResponse',
            'cachebuster': Math.round(new Date().getTime() / 1000)
        }

        var params = new URLSearchParams(data).toString();
        var apiUrl = serviceUrl + "?" + params;

        critCom.log('callService '+ apiUrl);

        var s = document.createElement("script");
        s.type = "text/javascript";
        s.src = apiUrl;
        s.defer = true;
        document.head.appendChild(s)
    },
    handleResponse: function(e) {
        var crit_json = JSON.parse(e);
        var crit_html = document.createElement("div");
        crit_html.id = "critical_communications";
        var n;
        var i = 0;
        for (n = 0, i = crit_json.hits.length; n < i; n++) {
            var img_url = crit_json.hits[n].alertImgURL;
            var crit_href = crit_json.hits[n].detMsgURL;
            var crit_msg = crit_json.hits[n].shortMsg;
            crit_html.innerHTML = '<div class="critical-icon"><img src="' + img_url + '" alt="Alert Image" /> </div><div class="critical-text"><p><a class="error_msg" href="' + crit_href + '" target="_blank"> ' + crit_msg + '</a></div></div>';
        }
        if (crit_html.innerHTML.length>0) {
            document.querySelector("header").parentNode.prepend(crit_html);
            document.documentElement.classList.add("critcomm-enabled");
        }
    },
    test: function() {
        getAlert('{"success":true,"propertyName":"sitepath","propertyValue":"https://www.cheezit.com","groupPath":"/content/Global/cc/admin/ccadmin/jcr:content/par","hits":[{"detMsgURL":"#link","alertImgURL":"https://services.kelloggs.com/content/dam/ccadmin/alert2.JPG","shortMsg":"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam sit amet est ac ex commodo ullamcorper. Aliquam eu hendrerit augue. Nullam id elementum massa. Suspendisse potenti."}]}');
    }
}
var getAlert = critCom.handleResponse;