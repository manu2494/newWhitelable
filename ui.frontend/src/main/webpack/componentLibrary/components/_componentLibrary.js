let contentArea = document.querySelector('.cl-content-area');
let contentOptions = document.querySelector('.cl-content-selection');
if (contentArea) {
	let subca = contentArea.querySelector('.cl-content-area');
	if (subca) {
		contentArea = subca;
	}
}
if (contentOptions) {
	let subco = contentOptions.querySelector('.cl-content-selection');
	if (subco) {
		contentOptions = subco;
	}
}
const updateContent = (options, components) => {
	let classList = '';
	options.forEach(option=>{
		if (option.checked) {
			classList +=' ' + option.value;
		}
	});
	components.forEach(component => {
		component.setAttribute('class',component.getAttribute('class-save')+classList);
	});
	document.dispatchEvent(new Event('restart'));
};
if (contentArea && contentOptions) {
	const components = Array.from(contentArea.querySelectorAll('.aem-GridColumn'));
	components.forEach(component => {
		component.setAttribute('class-save',component.getAttribute('class'));
	});
	const options = Array.from(contentOptions.querySelectorAll('input'));
	options.forEach(option=>{
		option.addEventListener('change',() =>updateContent(options,components));
	});
}

let themeOptions = document.querySelectorAll('.cmp-form-options__field-label input[name="theme"]');

const updateBodyClass = (themeOptions, bodyElement) => {
    let classList = '';
	themeOptions.forEach(option=>{
		if (option.checked) {
			classList +=' ' + option.value;
		}
	});
	bodyElement.setAttribute('class', bodyElement.getAttribute('class-save')+classList);
	document.dispatchEvent(new Event('restart'));
};

if (themeOptions) {
	const bodyElement = document.querySelector('body');
	bodyElement.setAttribute('class-save',bodyElement.getAttribute('class'));
	themeOptions.forEach(option=>{
		option.addEventListener('change',() =>updateBodyClass(themeOptions,bodyElement));
	});
}
