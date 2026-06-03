(function ($, Granite) {
    "use strict";

    let dialogContentSelector = ".cmp-image-included__editor";
    let smartCropRenditionDropDownSelector = ".cmp-image-included__editor-dynamicmedia-smartcroprendition";
    let presetTypeSelector = ".cmp-image-included__editor-dynamicmedia-presettype";
    let imagePresetDropDownSelector = ".cmp-image-included__editor-dynamicmedia-imagepreset";
    let altCheckboxSelector = ".cmp-image-included__editor-alt-text-dam";
    let altInputSelector = ".cmp-image-included__editor-alt-text";
    let altInputAlertIconSelector = altInputSelector + " + coral-icon[icon='alert']";
    let $dialog;
    let CheckboxTextfieldTuple;

    $(document).on("dialog-loaded", function (e) {
        CheckboxTextfieldTuple = window.CQ.CoreComponents.CheckboxTextfieldTuple.v1;
        $dialog = e.dialog;
        let $dialogContent = $dialog.find(dialogContentSelector);

        $dialogContent.each(function () {
            if (this.querySelector('[aria-required="true"]')) {
                new ImageAltTextValidator($(this));
            }
        });
    });

    let ImageAltTextValidator = function ($el) {
        this.$el = $el;
        this.altTuple = new CheckboxTextfieldTuple(this.$el[0], altCheckboxSelector, altInputSelector);
        this.$cqFileUpload = this.$el.find(".cmp-image-included__editor-file-upload");
        this.$cqFileUploadEdit = this.$el.find(".cq-FileUpload-edit");
        this.$dynamicMediaGroup = this.$el.find(".cmp-image-included__editor-dynamicmedia");
        this.$dynamicMediaGroup.hide();
        this.areDMFeaturesEnabled = (this.$dynamicMediaGroup.length === 1);
        this.smartCropRenditionsDropDown;
        this.imagePath;
        this.fileReference;
        this.altTextFromDAM = undefined;
        this.smartCropRenditionFromJcr;
        this.imagePropertiesRequest;

        let _this = this;

        if (this.areDMFeaturesEnabled) {
            this.smartCropRenditionsDropDown = this.$dynamicMediaGroup.find(smartCropRenditionDropDownSelector).get(0);
        }

        if (this.$cqFileUpload.length) {
            this.imagePath = this.$cqFileUpload.data("cqFileuploadTemporaryfilepath").slice(0, this.$cqFileUpload.data("cqFileuploadTemporaryfilepath").lastIndexOf("/"));
            this.retrieveInstanceInfo(this.imagePath);
            this.$cqFileUpload.on("assetselected", function (e) {
                _this.fileReference = e.path;
                _this.retrieveDAMInfo(_this.fileReference).then(
                    function () {
                        _this.altTuple.reinitCheckbox();
                        if (_this.areDMFeaturesEnabled) {
                            this.selectPresetType(_this.$el.find(presetTypeSelector), "imagePreset");
                            this.resetSelectField(_this.$dynamicMediaGroup.find(smartCropRenditionDropDownSelector));
                        }
                    }
                );
            });
            this.$cqFileUpload.on("click", "[coral-fileupload-clear]", function () {
                _this.altTuple.reset();
                _this.$el.find(altInputSelector).adaptTo("foundation-field").setRequired(false);
            });
            this.$cqFileUpload.on("change", function () {
                _this.$el.find(altInputSelector).adaptTo("foundation-field").setRequired(true);
            });
        }

        if (this.$cqFileUploadEdit) {
            this.fileReference = this.$cqFileUploadEdit.data("cqFileuploadFilereference");
            if (this.fileReference === "") {
                this.fileReference = undefined;
                _this.$el.find(altInputSelector).adaptTo("foundation-field").setRequired(false);
            }
            if (this.fileReference) {
                this.retrieveDAMInfo(this.fileReference);
            } else {
                _this.$el.find(altInputSelector).adaptTo("foundation-field").setRequired(false);
            }
        }

        this.bindEvents();
        this.improveAltTextValidation();
    };

    ImageAltTextValidator.prototype.bindEvents = function () {
        let _this = this;

        $(window).adaptTo("foundation-registry").register("foundation.validation.selector", {
            submittable: ".cmp-image-included__editor-alt-text",
            candidate: ".cmp-image-included__editor-alt-text",
            exclusion: ".cmp-image-included__editor-alt-text *"
        });

        $(window).on("focus", function () {
            if (_this.fileReference) {
                _this.retrieveDAMInfo(_this.fileReference);
            }
        });

        $(window).adaptTo("foundation-registry").register("foundation.validation.validator", {
            selector: altInputSelector,
            validate: function () {
                let seededValue = _this.$el.find(altInputSelector).attr("data-seeded-value");
                let isAltCheckboxChecked = _this.$el.find(altCheckboxSelector).attr("checked");
                let assetWithoutDescriptionErrorMessage = "Error: Please provide an asset which has a description that can be used as alt text.";
                if (isAltCheckboxChecked && !seededValue) {
                    return Granite.I18n.get(assetWithoutDescriptionErrorMessage);
                }
            }
        });

        $(document).on("dialog-beforeclose", function () {
            $(window).off("focus");
        });

        $(document).on("change", dialogContentSelector + " " + presetTypeSelector, function (e) {
            switch (e.target.value) {
                case "imagePreset":
                    this.$dynamicMediaGroup.find(imagePresetDropDownSelector).parent().show();
                    this.$dynamicMediaGroup.find(smartCropRenditionDropDownSelector).parent().hide();
                    this.resetSelectField(this.$dynamicMediaGroup.find(smartCropRenditionDropDownSelector));
                    break;
                case "smartCrop":
                    this.$dynamicMediaGroup.find(imagePresetDropDownSelector).parent().hide();
                    this.$dynamicMediaGroup.find(smartCropRenditionDropDownSelector).parent().show();
                    this.resetSelectField(this.$dynamicMediaGroup.find(imagePresetDropDownSelector));
                    break;
                default:
                    break;
            }
        });
    };

    ImageAltTextValidator.prototype.retrieveDAMInfo = function (fileReference) {
        let _this = this;

        return $.ajax({
            url: fileReference + "/_jcr_content/metadata.json"
        }).done(function (data) {
            if (data) {
                if (_this.altTuple) {
                    _this.altTextFromDAM = data["dc:description"];
                    if (_this.altTextFromDAM === undefined || _this.altTextFromDAM.trim() === "") {
                        _this.altTextFromDAM = data["dc:title"];
                    }
                    _this.altTuple.seedTextValue(_this.altTextFromDAM);
                    _this.altTuple.update();
                }
                // show or hide "DynamicMedia section" depending on whether the file is DM
                let isFileDM = data["dam:scene7File"];
                if (isFileDM === undefined || isFileDM.trim() === "" || !_this.areDMFeaturesEnabled) {
                    _this.$dynamicMediaGroup.hide();
                } else {
                    _this.$dynamicMediaGroup.show();
                    this.getSmartCropRenditions(data["dam:scene7File"]);
                }
            }
        });
    };


    /**
     * Helper function to get core image instance 'smartCropRendition' property
     * @param filePath
     */
    ImageAltTextValidator.prototype.retrieveInstanceInfo = function (filePath) {
        let _this = this;

        return $.ajax({
            url: filePath + ".json"
        }).done(function (data) {
            if (data) {
                // we need to get saved value of 'smartCropRendition' of Core Image component
                _this.smartCropRenditionFromJcr = data["smartCropRendition"];
            }
        });
    };

    /**
     * Get the list of available image's smart crop renditions and fill drop-down list
     * @param imageUrl The link to image asset
     */
    ImageAltTextValidator.prototype.getSmartCropRenditions = function (imageUrl) {
        let _this = this;
        if (this.imagePropertiesRequest) {
            this.imagePropertiesRequest.abort();
        }
        this.imagePropertiesRequest = new XMLHttpRequest();
        let url = window.location.origin + "/is/image/" + imageUrl + "?req=set,json";
        this.imagePropertiesRequest.open("GET", url, true);
        this.imagePropertiesRequest.onload = function () {
            if (_this.imagePropertiesRequest.status >= 200 && _this.imagePropertiesRequest.status < 400) {
                // success status
                let responseText = _this.imagePropertiesRequest.responseText;
                let rePayload = new RegExp(/^(?:\/\*jsonp\*\/)?\s*([^()]+)\(([\s\S]+),\s*"[0-9]*"\);?$/gmi);
                let rePayloadJSON = new RegExp(/^{[\s\S]*}$/gmi);
                let resPayload = rePayload.exec(responseText);
                let payload;
                if (resPayload) {
                    let payloadStr = resPayload[2];
                    if (rePayloadJSON.test(payloadStr)) {
                        payload = JSON.parse(payloadStr);
                    }

                }
                // check "relation" - only in case of smartcrop renditions
                if (payload !== undefined && payload.set.relation && payload.set.relation.length > 0) {
                    if (_this.smartCropRenditionsDropDown.items) {
                        _this.smartCropRenditionsDropDown.items.clear();
                    }
                    // we need to add "NONE" item first in the list
                    this.addSmartCropDropDownItem("NONE", "", true);
                    // "AUTO" would trigger automatic smart crop operation; also we need to check "AUTO" was chosed in previous session
                    this.addSmartCropDropDownItem("Auto", "SmartCrop:Auto", (_this.smartCropRenditionFromJcr === "SmartCrop:Auto"));
                    for (const element of payload.set.relation) {
                        _this.smartCropRenditionsDropDown.items.add({
                            content: {
                                innerHTML: element.userdata.SmartCropDef
                            },
                            disabled: false,
                            selected: (_this.smartCropRenditionFromJcr === element.userdata.SmartCropDef)
                        });
                    }
                    _this.$dynamicMediaGroup.find(presetTypeSelector).parent().show();
                } else {
                    _this.$dynamicMediaGroup.find(presetTypeSelector).parent().hide();
                    this.selectPresetType(_this.$el.find(presetTypeSelector), "imagePreset");
                }
                this.prepareSmartCropPanel();
            } else {
                // error status
            }
        };
        _this.imagePropertiesRequest.send();
    };

    /**
     * Helper function for populating dropdown list
     */
    ImageAltTextValidator.prototype.addSmartCropDropDownItem = function (label, value, selected) {
        this.smartCropRenditionsDropDown.items.add({
            content: {
                innerHTML: label,
                value: value
            },
            disabled: false,
            selected: selected
        });
    };

    /**
     * Helper function to show/hide UI-elements of dialog depending on the chosen radio button
     */
    ImageAltTextValidator.prototype.prepareSmartCropPanel = function () {
        let presetType = this.getSelectedPresetType(this.$el.find(presetTypeSelector));
        switch (presetType) {
            case undefined:
                this.selectPresetType(this.$el.find(presetTypeSelector), "imagePreset");
                this.$dynamicMediaGroup.find(smartCropRenditionDropDownSelector).parent().hide();
                break;
            case "imagePreset":
                this.$dynamicMediaGroup.find(imagePresetDropDownSelector).parent().show();
                this.$dynamicMediaGroup.find(smartCropRenditionDropDownSelector).parent().hide();
                break;
            case "smartCrop":
                this.$dynamicMediaGroup.find(imagePresetDropDownSelector).parent().hide();
                this.$dynamicMediaGroup.find(smartCropRenditionDropDownSelector).parent().show();
                break;
            default:
                break;
        }
    };

    /**
     * Get selected radio option helper
     * @param component The radio option component
     * @returns {String} Value of the selected radio option
     */
    ImageAltTextValidator.prototype.getSelectedPresetType = function (component) {
        let radioComp = component.find('[type="radio"]');
        for (const element of radioComp) {
            if ($(element).prop("checked")) {
                return $(element).val();
            }
        }
        return undefined;
    };

    /**
     * Select radio option helper
     * @param component
     * @param val
     */
    ImageAltTextValidator.prototype.selectPresetType = function (component, val) {
        let radioComp = component.find('[type="radio"]');
        radioComp.each(function () {
            $(this).prop("checked", ($(this).val() === val));
        });
    };

    /**
     * Reset selection field
     * @param field
     */
    ImageAltTextValidator.prototype.resetSelectField = function (field) {
        if (field[0]) {
            field[0].clear();
        }
    };

    /**
     * Improve error validation for alternative text inherited from asset's description
     */
    ImageAltTextValidator.prototype.improveAltTextValidation = function () {
        let _this = this;

        let MutationObserver = window.MutationObserver || window.WebKitMutationObserver || window.MozMutationObserver;
        let observer = new MutationObserver(function (mutations) {
            mutations.forEach(function (mutation) {
                if (mutation.type === "attributes") {
                    let isAltCheckboxChecked = _this.$el.find(altCheckboxSelector).attr("checked");
                    let alertIcon = _this.$el.find(altInputAlertIconSelector);
                    let assetTabId = _this.$el.closest("coral-panel").attr('id');
                    let assetTab = $dialog.find("[aria-controls=" + assetTabId + "]");
                    let assetTabAlertIcon = assetTab.find("coral-icon[icon='alert']");
                    if (mutation.attributeName === "data-seeded-value") {
                        if (isAltCheckboxChecked) {
                            if (_this.$el.find(altInputSelector).val()) {
                                if (alertIcon.length) {
                                    _this.$el.find(altInputSelector).removeClass("is-invalid");
                                    alertIcon.hide();
                                    assetTab.removeClass("is-invalid");
                                    assetTabAlertIcon.hide();
                                }
                            } else {
                                if (alertIcon.length) {
                                    _this.$el.find(altInputSelector).addClass("is-invalid");
                                    alertIcon.show();
                                    assetTab.addClass("is-invalid");
                                    assetTabAlertIcon.show();
                                }
                            }
                        }
                    }

                    if (mutation.attributeName === "disabled") {
                        if (_this.$el.find(altInputSelector).val()) {
                            if (alertIcon.length) {
                                _this.$el.find(altInputSelector).removeClass("is-invalid");
                                alertIcon.hide();
                                assetTab.removeClass("is-invalid");
                                assetTabAlertIcon.hide();
                            }
                        }
                    }

                    if (mutation.attributeName === "invalid") {
                        if (!_this.$el.find(altInputSelector).val()) {
                            if (alertIcon.length) {
                                _this.$el.find(altInputSelector).addClass("is-invalid");
                                alertIcon.show();
                                assetTab.addClass("is-invalid");
                                assetTabAlertIcon.show();
                            }
                        }
                    }
                }
            });
        });

        let altInput = document.querySelector(altInputSelector);
        if (altInput) {
            observer.observe(altInput, {
                attributeFilter: ["data-seeded-value", "disabled", "invalid"]
            });
        }
    }

})(jQuery, Granite);
