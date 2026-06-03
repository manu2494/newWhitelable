document.addEventListener("DOMContentLoaded", () => {
    const getCookie = (cookieName) => {
        const cookiesArray = document.cookie.split('; ');
        for (const cookie of cookiesArray) {
            const [name, value] = cookie.split('=');
            if (name === cookieName) {
                return decodeURIComponent(value);
            }
        }
        return null;
    };
 
    const firstName = getCookie('firstname');
    const lastName = getCookie('lastname');
    const yourscore = sessionStorage.getItem('lastScore');
 
    let replaceText = document.querySelectorAll(".text");
    for (const textElement of replaceText) {
        if (textElement.textContent.includes('${firstname}') && firstName) {
            textElement.innerHTML = textElement.innerHTML.replace(/\${firstname}/g, firstName);
        } else {
            textElement.innerHTML = textElement.innerHTML.replace(/\${firstname}/g, '');
        }
        if (textElement.textContent.includes('${lastname}') && lastName) {
            textElement.innerHTML = textElement.innerHTML.replace(/\${lastname}/g, lastName);
        } else {
            textElement.innerHTML = textElement.innerHTML.replace(/\${lastname}/g, '');
        }
        if (textElement.textContent.includes('${score}') && yourscore) {
            const minutes = Math.floor(yourscore / 60).toString();
            const seconds = (yourscore % 60).toFixed(2);
            const [wholeSeconds, decimalSeconds] = seconds.split('.');
            const timeString = `${minutes.padStart(2, '0')}:${wholeSeconds.toString().padStart(2, '0')}<span>${"." + decimalSeconds}</span>`;
            textElement.innerHTML = textElement.innerHTML.replace(/\${score}/g, timeString);
            textElement.classList.add('yourscore');
        } else {
            textElement.innerHTML = textElement.innerHTML.replace(/\${score}/g, '');
        }
    }
    replaceText = document.querySelectorAll(".title");
    
    for (const title of replaceText) {
		for (const child of title.children) {
			for (const header of child.children) {
		        if (header.innerHTML.includes('${firstname}') && firstName) {
		            header.innerHTML = header.innerHTML.replace(/\${firstname}/g, firstName);
		        } else {
		            header.innerHTML = header.innerHTML.replace(/\${firstname}/g, '');
		        }
		        if (header.innerHTML.includes('${lastname}') && lastName) {
		            header.innerHTML = header.innerHTML.replace(/\${lastname}/g, lastName);
		        } else {
		            header.innerHTML = header.innerHTML.replace(/\${lastname}/g, '');
		        }
		        if (header.innerHTML.includes('${score}') && yourscore) {
		            header.innerHTML = header.innerHTML.replace(/\${score}/g, yourscore);
		        } else {
		            header.innerHTML = header.innerHTML.replace(/\${score}/g, '');
		        }
			}
		}
    }
});
