window.addEventListener('load', () =>{
    window.MerkleUtils = {
        LoadExternalContent : (url, location, callbackArray) => {
            const xmlHttp = new XMLHttpRequest();
            xmlHttp.onload = () => {
		    if(xmlHttp.readyState === 4 && xmlHttp.status === 200) {
                const temp = document.createElement('div');
                temp.innerHTML = xmlHttp.responseText;
                location.innerHTML = temp.querySelector('.root').innerHTML;
                if(callbackArray) {
                    Array.from(callbackArray).forEach((callback) => {
                        callback();
                    });
                    }
                }
            };
            xmlHttp.open("GET", url);
            xmlHttp.send();
        },
        replaceListener: (clazz, event, listener) => {
			Array.from(document.querySelectorAll(clazz)).forEach((item) => {
				item.removeEventListener(event,listener);
				item.addEventListener(event,listener);
			});
		},
        UserAuthentication: () => {
                if(document.body.classList.contains('publish') && document.body.classList.contains('live')) {
                    if(window.BGC.cookie('accessToken')) {
                        document.querySelectorAll('.cnt-loggedin').forEach((item) => {
                            item.classList.add('show');
                        });
                        document.querySelectorAll('.cnt-loggedout').forEach((item) => {
                            item.classList.remove('show');
                        });
                    } else {
                        document.querySelectorAll('.cnt-loggedin').forEach((item) => {
                            item.classList.remove('show');
                        });
                        document.querySelectorAll('.cnt-loggedout').forEach((item) => {
                            item.classList.add('show');
                        });
                    }
                } else {
                    var preferenceList = document.querySelectorAll('#headerMenu > .aem-Grid > .container:nth-child(2) > .container-wrapper > .cmp-container');
                    if (preferenceList) {
                        var listItem = preferenceList[0] ? preferenceList[0].querySelector('ul li:first-child') : '';
                        if (listItem) {
                            listItem.classList.add("hide");
                        }
                    }
                    document.querySelectorAll('.cnt-loggedin').forEach((item) => {
                        item.classList.add('show', 'author');
                    });
                    document.querySelectorAll('.cnt-loggedout').forEach((item) => {
                        item.classList.add('show', 'author');
                    });
                }
        }
    };
    MerkleUtils.UserAuthentication();
    Array.from(document.querySelectorAll('a[href="https://cookie.com/"]')).forEach((a)=>{
		a.addEventListener('click',(e)=>{
			e.preventDefault();
			setTimeout(() => {
				var content = document.querySelector("#onetrust-consent-sdk #onetrust-pc-sdk");
				if (content) {
					content.setAttribute('style', '');
					content.classList.remove("ot-hide");
				}
			}, 500);
		});
	});
    const cookieteaser = document.querySelector('.teaser.modal');
    if(cookieteaser) {
        cookieteaser.addEventListener('click', function(event) {
            setTimeout(() => {
                const modalContainer = cookieteaser.querySelector('.modal-container');
                if(modalContainer && modalContainer.classList.contains('show-modal')) {
                    Array.from(document.querySelectorAll('a[href="https://cookie.com/"]')).forEach((a)=>{
                        a.addEventListener('click',(e)=>{
                            e.preventDefault();
                            var content = document.querySelector("#onetrust-consent-sdk #onetrust-pc-sdk");
                            if (content) {
                                content.setAttribute('style', '');
                                content.classList.remove("ot-hide");
                            }
                        });
                    });
                }
            }, 500);
        });
    }
	const replayButton = document.getElementById('replay-cookie');
	if (replayButton) {
		replayButton.addEventListener('click', () =>{
			const path = window.location.pathname.split('/')[1];
			BGC.cookie('introPlayedOnce','',{path:path, expires:-1});
		});
	}
});

