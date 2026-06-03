(function (document, $) {
    "use strict";

    var altCheckboxSelector = 'coral-checkbox[name="./altValueFromDAM"]';
    var isDecorativeCheckBoxSelector = 'coral-checkbox[name="./isDecorative"]';
    // When the dialog is loaded, add the listener
    $(document).on("foundation-contentloaded", function (e) {
        var $altCheckBox = $(altCheckboxSelector);
        var $isDecorativeCheckBox = $(isDecorativeCheckBoxSelector);

        $isDecorativeCheckBox.on("change", () => {
            if ($altCheckBox.prop("checked") && $isDecorativeCheckBox.prop("checked")) {
                $altCheckBox.prop("checked", false);
            }
        });
    });
})(document, Granite.$);