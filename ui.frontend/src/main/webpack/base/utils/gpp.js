window.GPP = window.GPP || {};

GPP.games = GPP.games || {}
GPP.games.validateTime = 0;
GPP.games.validate = (callback) => {
	setTimeout(() => {
		if (window.sessionStorage.getItem('entryId')) {
			GPP.games.validateTime=0;
			const xmlHttp = new XMLHttpRequest();
	    	xmlHttp.onload = () => {
				if(xmlHttp.readyState === 4 && xmlHttp.status === 200) {
					if (callback) {
						const resp = JSON.parse(xmlHttp.responseText)
						if (resp.message.indexOf('{') >-1) {
							resp.message = JSON.parse(resp.message);
						}
						callback(true, resp);
					}
				} else {
					if (callback) {
						const resp = JSON.parse(xmlHttp.responseText)
						if (resp.message.indexOf('{') >-1) {
							resp.message = JSON.parse(resp.message);
						}
						callback(false, resp);
					}
				}
			};
			xmlHttp.open("GET",`/bin/engage2/validateToken?token=${sessionStorage.getItem("token")}&path=${sessionStorage.getItem("path")}`);
			xmlHttp.setRequestHeader('Cache-Control', 'no-cache');
			xmlHttp.send();
		} else {
			GPP.games.validateTime+=50;
			if (GPP.games.validateTime < 30000) {
				GPP.games.validate(callback);
			} else {
				if (callback) {
					callback(false, {"message":"timedout waiting for entry"});
				}
			}
		}
	},50);
}
GPP.games.saveScore = (score, callback) =>{
	const xmlHttp = new XMLHttpRequest();
	xmlHttp.onload = () => {
		if(xmlHttp.readyState === 4 && xmlHttp.status === 200) {
			sessionStorage.setItem('lastScore',""+(score/1000));
			  window.dataLayer = window.dataLayer || []; // Check if dataLayer exists; if not, declare it

			  window.dataLayer.push({
			      "event": "competition_entry", // Event name
			      "competition_entered": window.location.pathname.split('/')[2]  // Competition name
			  });
			if (callback) {
				callback(true, JSON.parse(xmlHttp.responseText));
			}
		} else {
			if (callback) {
				callback(false, JSON.parse(xmlHttp.responseText));
			}
		}
	};
	const data = new URLSearchParams();
	data.append('entryId',sessionStorage.getItem('entryId'))
	data.append('path',sessionStorage.getItem("path"));
	data.append('score',score);
	xmlHttp.open("POST",`/bin/engage2/saveScore`);
	xmlHttp.setRequestHeader('Cache-Control', 'no-cache');
	xmlHttp.setRequestHeader('Content-Type','application/x-www-form-urlencoded; charset=UTF-8');
	xmlHttp.send(data);
};
GPP.games.readState = (gameId, callback) => {
	const xmlHttp = new XMLHttpRequest();
	xmlHttp.onload = () => {
		if(xmlHttp.readyState === 4 && xmlHttp.status === 200) {
			if (callback) {
				const resp = JSON.parse(xmlHttp.responseText);
				callback(true, JSON.parse(resp.message));
			}
		} else {
			if (callback) {
				callback(true, {});
			}
		}
	};
	xmlHttp.open("GET",`/bin/engage2/minigame?token=${sessionStorage.getItem('token')}&gameId=${gameId}&path=${sessionStorage.getItem('path')}`);
	xmlHttp.setRequestHeader('Cache-Control', 'no-cache');
	xmlHttp.send();

};
GPP.games.saveState = (gameId, value,callback) =>{
	const xmlHttp = new XMLHttpRequest();
	xmlHttp.onload = () => {
		if(xmlHttp.readyState === 4 && xmlHttp.status === 200) {
			if (callback) {
				const resp = JSON.parse(xmlHttp.responseText);
				callback(true, JSON.parse(resp.message));
			}
		} else {
			if (callback) {
				callback(false, JSON.parse(xmlHttp.responseText));
			}
		}
	};
	const data = new URLSearchParams();
	data.append('token',sessionStorage.getItem('token'))
	data.append('path',sessionStorage.getItem("path"));
	data.append('gameId',gameId);
	data.append('data',JSON.stringify(value));
	xmlHttp.open("POST",`/bin/engage2/minigame`);
	xmlHttp.setRequestHeader('Cache-Control', 'no-cache');
	xmlHttp.setRequestHeader('Content-Type','application/x-www-form-urlencoded; charset=UTF-8');
	xmlHttp.send(data);
};

GPP.games.isLoggedIn = () => {
	return (BGC.cookie("profileID")!= null && sessionStorage.getItem("token") != null);
};
