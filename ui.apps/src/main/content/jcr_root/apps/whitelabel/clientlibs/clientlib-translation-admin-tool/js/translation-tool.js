const addFavicon = () => {
    let link = document.querySelector("link[rel~='icon']");
    if (!link) {
        link = document.createElement('link');
        link.rel = 'icon';
        document.head.appendChild(link);
    }
    link.href = '/apps/whitelabel/clientlibs/clientlib-componentLibrary/resources/images/icons/favicon.ico';
};

const selectTranslationInputsListener = (e) => {
    e.preventDefault();
    processTranslationInputs();
};

const processTranslationInputs = () => {
    const translationExcelFilePath = document.querySelector("foundation-autocomplete#translationExcelFilePath input").value;
    const selectedPlatform = document.querySelector("coral-select[name='./selectedPlatform']").value;
    if (translationExcelFilePath != '' && selectedPlatform != '') {
        $.ajax({
            type: "POST",
            url: "/bin/getLanguagesForTranslation",
            data: {
                "translationExcelFilePath": translationExcelFilePath,
                "selectedPlatform": selectedPlatform
            },
    		dataType: "json",
            success: (data) => {
    			copyDialog('add-translation-inputs');
    			if (data.length === 0) {
                    replaceHtmlWithNoMatches();
                } else {
                    data.forEach((entry) => {
                        cloneAndReplaceHTML(entry);
                    });
                }
            },
            error: (e) => {
                alert("Error Occurred: " + e.responseText);
            }
        });
    } else {
        alert("Please, fill the mandatory fields");
    }
};

const replaceHtmlWithNoMatches = () => {
    var container = document.querySelector('.edit-details .coral-Form');
    container.innerHTML = '<p>No matches found</p>';
};

const cloneAndReplaceHTML = (locale) => {
    var container = document.querySelector('.edit-details .coral-Form');
    var originalCheckbox = document.querySelector('coral-checkbox[name="./all"]');

    var newCheckbox = originalCheckbox.cloneNode(true);
    newCheckbox.setAttribute('name', './' + locale);
    newCheckbox.querySelector('.coral3-Checkbox-input').setAttribute('id', 'coral-id-' + Math.floor(Math.random() * 1000));
    newCheckbox.querySelector('coral-checkbox-label').textContent = locale;

    container.insertBefore(newCheckbox, container.lastElementChild);
};

const updateListeners = () => {
    // select translation inputs
    Array.from(document.querySelectorAll('.selectTranslationsBtn')).forEach((selectTranslationInputs) => {
		selectTranslationInputs.removeEventListener('click',selectTranslationInputsListener);
		selectTranslationInputs.addEventListener('click',selectTranslationInputsListener);
    });
    // select All listener         
	var selectAllCheckbox = document.querySelector('coral-checkbox[name="./all"]');
    selectAllCheckbox.removeEventListener('click',selectAllLanguages(selectAllCheckbox));
    selectAllCheckbox.addEventListener('click',selectAllLanguages(selectAllCheckbox));
    // run translations listener
	var runTranslationsBtn = document.querySelector('.runTranslationsBtn');
    runTranslationsBtn.removeEventListener('click',runTranslationsListener);
    runTranslationsBtn.addEventListener('click',runTranslationsListener);
};

const runTranslationsListener = (e) => {
    e.preventDefault();
    runTranslations();
};

let pollInterval;

const pollForTranslationsStatus = () => {
    const editDetailsDiv = document.querySelector('.edit-details');
    pollInterval = setInterval(() => {
        $.ajax({
            type: "GET",
            url: "/bin/runTranslations",
            success: (response) => {
                const jsonResponse = JSON.parse(response);
                let innerHTMLContent = `Time: ${new Date().toLocaleTimeString()} <br\> State: ${jsonResponse.state}`;
                if (jsonResponse.output) {
                    innerHTMLContent += `<div>${jsonResponse.output}</div>`;
                }
                editDetailsDiv.innerHTML = innerHTMLContent;
                if (jsonResponse.state === 'inactive' || jsonResponse.state === 'complete') {
                    
                    clearInterval(pollInterval);

                    if (jsonResponse.state === 'inactive') {
                        enableFormFields();
                    }

                    if (jsonResponse.xls && jsonResponse.platform) {
                        const autocompleteInput = document.querySelector("foundation-autocomplete#translationExcelFilePath input");
                        if (autocompleteInput) {
                            autocompleteInput.value = jsonResponse.xls;
                        }
                        const selectPlatform = document.querySelector("coral-select[name='./selectedPlatform']");
                        if (selectPlatform) {
                            const platformOption = Array.from(selectPlatform.querySelectorAll('coral-select-item'))
                                .find(option => option.value === jsonResponse.platform);
                            if (platformOption) {
                                platformOption.setAttribute('selected', '');
                                const selectLabel = selectPlatform.querySelector('.coral3-Select-label');
                                selectLabel.textContent = jsonResponse.platform;
                            }
                        }
                        disableFormFields();
                    }
                }
            },
            error: (e) => {
                editDetailsDiv.innerHTML = "Unable to check isTranslationsRunning: " + e.responseText;
                enableFormFields();
            }
        });
    }, 5000);
};

const runTranslations = () => {
    const runButton = document.querySelector('.runTranslationsBtn');
    runButton.innerHTML = 'Running...';
    disableFormFields();

    const checkedCheckboxes = $(":checkbox:checked").not("[name='./all']");

    if (checkedCheckboxes.length === 0) {
        alert("Please select at least one language");
        runButton.innerHTML = 'Run';
        enableFormFields();
        return;
    }

    const selectedLanguages = [...new Set(checkedCheckboxes.map(function () {
        return this.name.replace(/^\.\//, ''); // Remove the "./" prefix
    }).get())];

    const dataString = selectedLanguages.join(",");
    const translationExcelFilePath = document.querySelector("foundation-autocomplete#translationExcelFilePath input").value;
    const selectedPlatform = document.querySelector("coral-select[name='./selectedPlatform']").value;

    const data = {
        selectedLanguages: dataString,
        translationExcelFilePath: translationExcelFilePath,
        selectedPlatform: selectedPlatform
    };

    const editDetailsDiv = document.querySelector('.edit-details');

    const postCall = $.ajax({
        type: "POST",
        url: "/bin/runTranslations",
        data: data,
        dataType: "html"
    });

    postCall.done((response) => {
        enableFormFields();
        runButton.innerHTML = 'Run';
        const jsonResponse = JSON.parse(response);
        let innerHTMLContent = `Time: ${new Date().toLocaleTimeString()} <br\> State: ${jsonResponse.state}`;
        if (jsonResponse.output) {
            innerHTMLContent += `<div>${jsonResponse.output}</div>`;
        }
        editDetailsDiv.innerHTML = innerHTMLContent;
        clearInterval(pollInterval);
    });

    postCall.fail((e) => {
        editDetailsDiv.innerHTML = "Unable to run translations: " + e.responseText;
        enableFormFields();
        runButton.innerHTML = 'Run';
    });

    pollForTranslationsStatus();
};

const selectAllLanguages = (selectAllCheckbox) => {
	selectAllCheckbox.addEventListener("change", function () {
    	var siblingCheckboxes = document.querySelectorAll('.coral-Form-field.coral3-Checkbox:not(#selectAllCheckbox) input[type="checkbox"]');
        for (var i = 0; i < siblingCheckboxes.length; i++) {
        	siblingCheckboxes[i].checked = selectAllCheckbox.checked;
        }
    });
};

const copyDialog = (dialogName) => {
    const detailsArea = document.querySelector('.edit-details');
    if (detailsArea) {
        detailsArea.innerHTML = document.querySelector('.' + dialogName).innerHTML;
        Array.from(detailsArea.querySelectorAll('input')).forEach((input) => {
            input.value = '';
        });
        updateListeners();
    }
};

const isTranslationsRunning = () => {
    const editDetailsDiv = document.querySelector('.edit-details');
    $.ajax({
        type: "GET",
        url: "/bin/runTranslations",
        success: (response) => {
            const jsonResponse = JSON.parse(response);
            let innerHTMLContent = `Time: ${new Date().toLocaleTimeString()} <br\> State: ${jsonResponse.state}`;
            if (jsonResponse.output) {
                innerHTMLContent += `<div>${jsonResponse.output}</div>`;
            }
    		editDetailsDiv.innerHTML = innerHTMLContent;
        	if (jsonResponse.state === 'inactive') {
        		enableFormFields();
        		resetFormFields();
    		}
        	if (jsonResponse.state === 'running') {
        		setTimeout(isTranslationsRunning,5000);
    		}
            if (jsonResponse.xls && jsonResponse.platform) {
				const autocompleteInput = document.querySelector("foundation-autocomplete#translationExcelFilePath input");
                if (autocompleteInput) {
                    autocompleteInput.value = jsonResponse.xls;
                }
                const selectPlatform = document.querySelector("coral-select[name='./selectedPlatform']");
                if (selectPlatform) {
                    const platformOption = Array.from(selectPlatform.querySelectorAll('coral-select-item'))
                        .find(option => option.value === jsonResponse.platform);
                    if (platformOption) {
                        platformOption.setAttribute('selected', '');
                        const selectLabel = selectPlatform.querySelector('.coral3-Select-label');
                        selectLabel.textContent = jsonResponse.platform;
                    }
                }
                disableFormFields();
            }
        },
        error: (e) => {
            editDetailsDiv.innerHTML = "Unable to check isTranslationsRunning: " + e.responseText;
            enableFormFields();
        }
    });
};

const resetFormFields = () => {
    const autocompleteInput = document.querySelector("foundation-autocomplete#translationExcelFilePath input");
    if (autocompleteInput) {
        autocompleteInput.value = "";
    }
    const selectPlatform = document.querySelector("coral-select[name='./selectedPlatform']");
    if (selectPlatform) {
        const firstOption = selectPlatform.querySelector('coral-select-item');
        if (firstOption) {
            firstOption.setAttribute('selected', '');
        }
        const selectLabel = selectPlatform.querySelector('.coral3-Select-label');
        if (selectLabel && firstOption) {
            selectLabel.textContent = firstOption.textContent.trim();
        }
    }
};

const disableFormFields = () => {
    const form = document.querySelector('form');
    if (form) {
        const formElements = form.elements;
        for (let i = 0; i < formElements.length; i++) {
            formElements[i].disabled = true;
        }
    }
};

const enableFormFields = () => {
    const form = document.querySelector('form');
    if (form) {
        const formElements = form.elements;
        for (let i = 0; i < formElements.length; i++) {
            formElements[i].disabled = false;
        }
    }
};

$(document).ready(function() {
    addFavicon();
    updateListeners();
    isTranslationsRunning();
});