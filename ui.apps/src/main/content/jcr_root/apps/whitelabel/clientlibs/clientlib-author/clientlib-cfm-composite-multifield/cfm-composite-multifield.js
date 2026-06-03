(function ($) {
    let CFM = window.Dam.CFM,
        COMPOSITE_ITEM_VALUE = 'data-composite-item-value',
        DEFAULT_VARIATION = 'master',
        MF_NAME_ATTR = 'data-granite-coral-multifield-name';

    let config = {};

    config.form = document.querySelector('.content-fragment-editor');
    config.multiComposites = config.form.querySelectorAll('[data-granite-coral-multifield-composite]');

    /**
     * EXIT when composite mulltifields do not exist
     **/
     if (config.multiComposites.length === 0) {
        return;
    }

    CFM.Core.registerReadyHandler(getMultifieldsContent);
    extendRequestSave();

    function extendRequestSave() {

        let origFn = CFM.editor.Page.requestSave;

        CFM.editor.Page.requestSave = requestSave;

        function extend() {
            let extended = {},
                i = 0;

            // merge the object into the extended object
            function merge(obj) {
                for (let prop in obj) {
                    if (obj.hasOwnProperty(prop)) {
                        extended[prop] = obj[prop];
                    }
                }
            }

            for (; i < arguments.length; i++) {
                merge(arguments[i]);
            }
            return extended;
        }

        function requestSave(callback, options) {
            if(options.type==='pageleave'){
                return;
            }
            origFn.call(this, callback, options);

            let mfData = getMultifieldData();

            if (!mfData) {
                return;
            }

            let url = CFM.EditSession.fragment.urlBase + '.cfm.content.json',
                variation = getVariation(),
                createNewVersion = (options && !!options.newVersion) || false;

            let data = {
                ':type': 'multiple',
                ':newVersion': createNewVersion,
                '_charset_': 'utf-8'
            };

            if (variation !== DEFAULT_VARIATION) {
                data[':variation'] = variation;
            }

            let request = {
                url: url,
                method: 'post',
                dataType: 'json',
                data: extend(data, mfData),
                cache: false
            };
            CFM.RequestManager.schedule({
                request: request,
                type: CFM.RequestManager.REQ_BLOCKING,
                condition: CFM.RequestManager.COND_EDITSESSION,
                ui: (options && options.ui)
            })
        }
    }

    function getMultifieldsContent() {
        $.ajax(`${CFM.EditSession.fragment.urlBase}/jcr:content/data.2.json`)
        .done(loadContentIntoMultiFields);
    }

    function getMultifieldData() {
        let value,
            values,
            data = {};

        config.multiComposites.forEach(function (el) {
            values = [];

            let fields,
                items = el.items.getAll();

            items.forEach(function (item) {

                value = {};

                fields = item.content.querySelectorAll('[name]');
                fields.forEach(function (field) {
                    if (canSkip(field)) {
                        return;
                    }

                    value[getNameDotSlashRemoved(field.getAttribute('name'))] = getFieldValue(field);
                });
                values.push(JSON.stringify(value));
            });

            data[getNameDotSlashRemoved((el.getAttribute(MF_NAME_ATTR)))] = values;
        });

        return data;
    }

    function loadContentIntoMultiFields(data) {
        let mfValArr, mfAdd,
            vData = data[getVariation()];

        if (!vData) {
            return;
        }

        config.multiComposites.forEach(function (el) {
            mfValArr = vData[getNameDotSlashRemoved((el.getAttribute(MF_NAME_ATTR)))];

            if (!mfValArr) {
                return;
            }

            mfAdd = el.querySelector('[coral-multifield-add]');

            mfValArr.forEach(function (item) {
                mfAdd.click();

                let $lastItem = $(el).find('coral-multifield-item').last();

                $lastItem.attr(COMPOSITE_ITEM_VALUE, item);

                Coral.commons.ready($lastItem[0], function (component) {
                    fillMultifieldItems(component);
                });
            });
        });
    }

    function fillMultifieldItems(mfItem) {
        if (mfItem == null) {
            return;
        }

        let mfMap = mfItem.getAttribute(COMPOSITE_ITEM_VALUE);

        if (!mfMap) {
            return;
        }

        mfMap = JSON.parse(mfMap);

        for (const key in mfMap) {
            let field = mfItem.querySelector(`[name$='${key}']`);

            field && setFieldValue(field, mfMap[key]);
        }
    }

    function canSkip(field) {
        switch (field.type) {
            case 'checkbox':
            case 'hidden':
                return true;
            default:
                return false;
        }
    }

    function getFieldValue(field){
        let value;

        if (field.tagName == 'CORAL-CHECKBOX') {
            value = field.checked ? field.getAttribute('value') : '';
        } else if(field.closest('[multiple].cq-ui-tagfield')) {
            value =[];
            field.closest('[multiple]').querySelectorAll('[foundation-autocomplete-value] coral-tag').forEach( (field) => {
                value.push(field.value);
            });
        } else {
            value = field.value;
        }

        return value;
    }

    function setFieldValue(field, value) {
        if (field.tagName == 'CORAL-CHECKBOX') {
            field.checked = (field.getAttribute('value') == value);
        } else if(field.name.includes('assetCategory') && field.multiple && Array.isArray(value)) {
            field.values = value;
        } else {
            field.value = value;
        }
    }

    function getNameDotSlashRemoved(name) {
        if (!name) {
            return;
        }

        let parts = name.split('/');
        return parts[parts.length-1];
    }

    function getVariation() {
        let variation = config.form.dataset.variation;
        variation = variation || DEFAULT_VARIATION;
        return variation;
    }

}(jQuery));
