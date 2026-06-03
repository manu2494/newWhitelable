const setup = () => {
	if (document.querySelectorAll(".language-primary")) {
		document.querySelectorAll(".language-primary").forEach(element => {

			var languageDiv = document.createElement("div");
			languageDiv.classList.add("languageDiv");


			var paraTag = document.createElement("p");
			paraTag.classList.add("languageText");
			var label = element.getAttribute('aria-label') || '';
			var paraText = document.createTextNode(label);
			paraTag.appendChild(paraText);
			languageDiv.appendChild(paraTag);

			var spanTag = document.createElement("span");
			spanTag.classList.add("languageSpan");
			languageDiv.appendChild(spanTag);

			element.prepend(languageDiv);


			languageDiv.addEventListener("click", (event) => {
				var navBlock = languageDiv.nextElementSibling;
				var closeLanguage = languageDiv.children[1];
				expendLanguages(navBlock, event, closeLanguage);
			});
		});
	}
};

const expendLanguages = (event, ev, close) => {
	if (event && event.style.display == "") {
		event.style.display = "block";
		close.classList.add("languageSpan-close");
	} else if (event && event.style.display == "block") {
		event.style.display = "none";
		close.classList.remove("languageSpan-close");
	} else if (event && event.style.display == "none") {
		event.style.display = "block";
		close.classList.add("languageSpan-close");
	}
};


setup();
const restart = () => {
	if (document.querySelectorAll(".languagenavigation")) {
		document.querySelectorAll(".languagenavigation").forEach(element => {
			var languageDiv = element.querySelector('.languageDiv');
			if (languageDiv) {
				element.removeChild(languageDiv);
				element.querySelector('nav').style='';
			}

		});

	}
	setup();
};

document.addEventListener('restart', restart);
