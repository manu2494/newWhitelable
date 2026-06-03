(function ($, CUI, $document) {
    let GROUP = "coreFramework-aem-fonts",
        FONT_FEATURE = "applyFont",
        TEXT_COLOR_FEATURE = "textColor",
        TEXT_BG_COLOR_FEATURE = "textBackgroundColor",
        TEXT_SIZE_FEATURE = "textSize",
        APPLY_FONT_DIALOG = "coreFrameworkTouchUIApplyFontDialog",
        SENDER = "coreFramework-aem", REQUESTER = "requester", $eaemFontPicker,
        CANCEL_CSS = "[data-foundation-wizard-control-action='cancel']",
        FONT_SELECTOR_URL = "/apps/core-framework/clientlibs/clientlib-author/rte-font-extension/font-selector.html",
        url = document.location.pathname,
        IS_CFM = typeof(Dam) !== "undefined" && typeof(Dam.CFM) !== "undefined";

    if (IS_CFM) {
        extendStyledTextEditor();
        addPlugin();
    } else if (url.indexOf(FONT_SELECTOR_URL) == 0) {
        handlePicker();
    } else {
        addPlugin();
        addPluginToDefaultUISettings();
        addDialogTemplate();
    }

    function handlePicker() {
        $document.on("foundation-contentloaded", fillDefaultValues);

        $document.on("click", CANCEL_CSS, sendCancelMessage);

        $document.submit(sentTextAttributes);
    }

    function queryParameters() {
        let result = {}, param,
            params = document.location.search.split(/\?|\&/);

        params.forEach(function (it) {
            if (_.isEmpty(it)) {
                return;
            }

            param = it.split("=");
            result[param[0]] = param[1];
        });

        return result;
    }

    function setWidgetValue(form, selector, value, enable) {
        Coral.commons.ready(form.querySelector(selector), function (field) {
            if (field.tagName == "CORAL-CHECKBOX") {
                if (value == "true") {
                    field.checked = true;
                }
            } else {
                field.value = _.isEmpty(value) ? "" : decodeURIComponent(value);
            }

            if (enable) {
                delete field.disabled;
            } else {
                field.disabled = "disabled";
            }
        });
    }

    function fillDefaultValues() {
        let queryParams = queryParameters(),
            $form = $("form");

        if (_.isEmpty(queryParams.features)) {
            return;
        }

        let features = queryParams.features.split(",");

        setWidgetValue($form[0], "[name='./size']", queryParams?.size?.replace("px", ""), features.includes(TEXT_SIZE_FEATURE));

        setWidgetValue($form[0], "[name='./color']", queryParams.color, features.includes(TEXT_COLOR_FEATURE));

        setWidgetValue($form[0], "[name='./bgColor']", queryParams.bgColor, features.includes(TEXT_BG_COLOR_FEATURE));

        $form.css("background-color", "#fff");
    }

    function sentTextAttributes() {
        let message = {
            sender: SENDER,
            action: "submit",
            data: {}
        }, $form = $("form"), $field;

        _.each($form.find("[name^='./']"), function (field) {
            $field = $(field);
            message.data[$field.attr("name").substr(2)] = $field.val();
        });

        getParent().postMessage(JSON.stringify(message), "*");
    }

    function sendCancelMessage() {
        let message = {
            sender: SENDER,
            action: "cancel"
        };

        getParent().postMessage(JSON.stringify(message), "*");
    }

    function getParent() {
        if (window.opener) {
            return window.opener;
        }

        return parent;
    }

    function addDialogTemplate() {
        let url = Granite.HTTP.externalize(FONT_SELECTOR_URL) + "?" + REQUESTER + "=" + SENDER;

        let html = "<iframe width='600px' height='450px' frameBorder='0' src='" + url + "'></iframe>";

        if (_.isUndefined(CUI.rte.Templates)) {
            CUI.rte.Templates = {};
        }

        if (_.isUndefined(CUI.rte.templates)) {
            CUI.rte.templates = {};
        }

        try {
            CUI.rte.templates['dlg-' + APPLY_FONT_DIALOG] = CUI.rte.Templates['dlg-' + APPLY_FONT_DIALOG] = Handlebars.compile(html);
        } catch (err) {
            console.log("Ignoring font plugin error", err);
        }
    }

    function rgbToHex(color) {
        if (_.isEmpty(color)) {
            return color;
        }

        if (color.indexOf("rgb") == 0) {
            color = CUI.util.color.RGBAToHex(color);
        }

        return color;
    }

    function addPluginToDefaultUISettings() {
        let groupFeature = GROUP + "#" + FONT_FEATURE,
            toolbar = CUI.rte.ui.cui.DEFAULT_UI_SETTINGS.dialogFullScreen.toolbar;

        if (toolbar.includes(groupFeature)) {
            return;
        }

        toolbar.splice(3, 0, groupFeature);
    }

    function extendStyledTextEditor() {
        let origFn = Dam.CFM.StyledTextEditor.prototype._start;

        Dam.CFM.StyledTextEditor.prototype._start = function () {
            addTextFontPluginSettings(this);
            origFn.call(this);
        }
    }

    function addTextFontPluginSettings(editor) {
        let config = editor.$editable.data("config");

        config.rtePlugins[GROUP] = {
            features: "*"
        };

        let icon = GROUP + "#" + FONT_FEATURE;
        config.uiSettings.cui.multieditorFullscreen.toolbar.push(icon);
    }

    function closePicker(event) {
        event = event.originalEvent || {};

        if (_.isEmpty(event.data)) {
            return;
        }

        let message, action;

        try {
            message = JSON.parse(event.data);
        } catch (err) {
            return;
        }

        if (!message || message.sender !== SENDER) {
            return;
        }

        action = message.action;

        if (action === "submit") {
            $eaemFontPicker.eaemFontPlugin.editorKernel.execCmd(FONT_FEATURE, message.data);
        }

        let modal = $eaemFontPicker.data('modal');
        modal.hide();
        modal.$element.remove();
    }

    function addPlugin() {
        let EAEMTouchUIFontPlugin = new Class({
            toString: "EAEMTouchUIFontPlugin",

            extend: CUI.rte.plugins.Plugin,

            pickerUI: null,

            getFeatures: function () {
                return [FONT_FEATURE];
            },

            initializeUI: function (tbGenerator) {
                let plg = CUI.rte.plugins;

                addPluginToDefaultUISettings();

                if (!this.isFeatureEnabled(FONT_FEATURE)) {
                    return;
                }

                this.pickerUI = tbGenerator.createElement(FONT_FEATURE, this, false, this.config.tooltips[FONT_FEATURE]);
                tbGenerator.addElement(GROUP, plg.Plugin.SORT_FORMAT, this.pickerUI, 10);

                let groupFeature = GROUP + "#" + FONT_FEATURE;
                tbGenerator.registerIcon(groupFeature, "textColor");

                $(window).off('message', closePicker).on('message', closePicker);
            },

            notifyPluginConfig: function (pluginConfig) {
                pluginConfig = pluginConfig || {};

                CUI.rte.Utils.applyDefaults(pluginConfig, {
                    'tooltips': {
                        applyFont: {
                            'title': 'Apply Font',
                            'text': 'Apply Font to selected text'
                        }
                    }
                });

                this.config = pluginConfig;
            },

            isValidSelection: function () {
                let winSel = window.getSelection();
                return winSel && winSel.rangeCount == 1 && winSel.getRangeAt(0).toString().length > 0;
            },

            execute: function (pluginCommand, value, envOptions) {
                let context = envOptions.editContext;

                if (pluginCommand != FONT_FEATURE) {
                    return;
                }

                if (!this.isValidSelection()) {
                    return;
                }

                let selection = CUI.rte.Selection.createProcessingSelection(context),
                    startNode = selection.startNode;

                if ((selection.startOffset === startNode.length) && (startNode != selection.endNode)) {
                    startNode = startNode.nextSibling;
                }

                let $tag = $(CUI.rte.Common.getTagInPath(context, startNode, "span")),
                    clazz = $tag.attr("class"),
                    size = $tag.css("font-size"),
                    color = this.getColorAttributes($tag);

                if ($('.eaem-cfm-font-size.coral-Modal').length) {
                    return;
                }
                this.showFontModal(this.getPickerIFrameUrl(this.config.features, size, clazz, color.color, color.bgColor));
            },

            getColorAttributes: function ($tag) {
                let key, color = {color: "", bgColor: ""};

                if (!$tag.attr("style")) {
                    return color;
                }

                //donot use .css("color"), it returns default font color, if color is not set
                let parts = $tag.attr("style").split(";");

                _.each(parts, function (value) {
                    value = value.split(":");

                    key = value[0] ? value[0].trim() : "";
                    value = value[1] ? value[1].trim() : "";

                    if (key == "color") {
                        color.color = rgbToHex(value);
                    } else if (key == "background-color") {
                        color.bgColor = rgbToHex(value);
                    }
                });

                return color;
            },

            showFontModal: function (url) {
                let self = this, $iframe = $('<iframe>'),
                    $modal = $('<div>').addClass('eaem-cfm-font-size coral-Modal');

                $iframe.attr('src', url).appendTo($modal);

                $modal.appendTo('body').modal({
                    type: 'default',
                    buttons: [],
                    visible: true
                });

                $eaemFontPicker = $modal;

                $eaemFontPicker.eaemFontPlugin = self;

                $modal.nextAll(".coral-Modal-backdrop").addClass("cfm-coral2-backdrop");
            },

            getPickerIFrameUrl: function (features, size, clazz, color, bgColor) {
                let url = Granite.HTTP.externalize(FONT_SELECTOR_URL) + "?" + REQUESTER + "=" + SENDER;

                if (features === "*") {
                    features = [TEXT_COLOR_FEATURE, TEXT_BG_COLOR_FEATURE, TEXT_SIZE_FEATURE];
                }

                url = url + "&features=" + features.join(",");

                if (!_.isEmpty(color)) {
                    url = url + "&color=" + encodeURIComponent(color);
                }

                if (!_.isEmpty(bgColor)) {
                    url = url + "&bgColor=" + encodeURIComponent(bgColor);
                }

                if (!_.isEmpty(size)) {
                    url = url + "&size=" + size;
                }

                if (!_.isEmpty(clazz)) {
                    url = url + "&class=" + clazz;
                }

                return url;
            },

            updateState: function (selDef) {
                let hasUC = this.editorKernel.queryState(FONT_FEATURE, selDef);

                if (this.pickerUI != null) {
                    this.pickerUI.setSelected(hasUC);
                }
            }
        });

        let EAEMTouchUIFontCmd = new Class({
            toString: "EAEMTouchUIFontCmd",

            extend: CUI.rte.commands.Command,

            isCommand: function (cmdStr) {
                return (cmdStr.toLowerCase() == FONT_FEATURE);
            },

            getProcessingOptions: function () {
                let cmd = CUI.rte.commands.Command;
                return cmd.PO_SELECTION | cmd.PO_BOOKMARK | cmd.PO_NODELIST;
            },

            getTagObject: function (textData) {
                let style = "";

                if (!_.isEmpty(textData.color)) {
                    style = "color: " + textData.color + ";";
                }

                if (!_.isEmpty(textData.size)) {
                    style = style + "font-size: " + textData.size + "px;";
                }

                if (!_.isEmpty(textData.bgColor)) {
                    style = style + "background-color: " + textData.bgColor;
                }

                return {
                    "tag": "span",
                    "attributes": {
                        "style": style
                    }
                };
            },

            execute: function (execDef) {
                let textData = execDef.value, selection = execDef.selection,
                    nodeList = execDef.nodeList;

                if (!selection || !nodeList) {
                    return;
                }

                let common = CUI.rte.Common,
                    context = execDef.editContext,
                    tagObj = this.getTagObject(textData);

                if (_.isEmpty(textData.size) && _.isEmpty(textData.color) && _.isEmpty(textData.bgColor)) {
                    nodeList.removeNodesByTag(execDef.editContext, tagObj.tag, undefined, true);
                    return;
                }

                let tags = common.getTagInPath(context, selection.startNode, tagObj.tag);

                //remove existing color before adding new color
                if (tags != null) {
                    nodeList.removeNodesByTag(execDef.editContext, tagObj.tag, tags.attributes ? tags.attributes : undefined, true);
                }

                nodeList.surround(execDef.editContext, tagObj.tag, tagObj.attributes);
            },

            queryState: function (selectionDef, cmd) {
                return false;
            }
        });

        CUI.rte.commands.CommandRegistry.register(FONT_FEATURE, EAEMTouchUIFontCmd);

        CUI.rte.plugins.PluginRegistry.register(GROUP, EAEMTouchUIFontPlugin);
    }
}(jQuery, window.CUI, jQuery(document)));
