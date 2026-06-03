(function (document, $) {
    "use strict";

    // when dialog gets injected
    $(document).on("foundation-contentloaded", function () {
        // if there is already an inital value make sure the according target element becomes visible
        $(".cq-dialog-checkbox-showhide").each(function () {
            showHide($(this));
        });

    });

    //when checkbox clicked
    $(document).on("change", ".cq-dialog-checkbox-showhide", function () {
        showHide($(this));
    });


    function showHide($el) {
        let isChecked = $el.prop("checked");
        let targetElementSelector = $el.data('show-hide-target');
        let targetElement = $(targetElementSelector);

        if (isChecked) {
            showField(targetElement);
        } else {
            hideField(targetElement);
        }
    }

    function showField($targetEl) {
        if ($targetEl.attr('data-cfminput') === 'true') {
            $targetEl.parent().removeClass('hide');
        }
        $targetEl.removeClass('hide');
        $targetEl.removeAttr('disabled');
    }

    function hideField($targetEl) {
        if ($targetEl.attr('data-cfminput') === 'true') {
            $targetEl.parent().addClass('hide');
        }
        $targetEl.addClass('hide');
        $targetEl.attr('disabled', 'disabled');
    }
})(document, Granite.$);
