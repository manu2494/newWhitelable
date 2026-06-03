/**
 * Extension to the standard dropdown/select component. It enabled hidding/unhidding of other components based on the
 * selection made in the dropdown/select.
 *
 * How to use:
 *
 * - add the class cq-dialog-dropdown3-showhide to the dropdown/select element
 * - add the data attribute cq-dialog-dropdown3-showhide-target to the dropdown/select element, value should be the
 *   selector, usually a specific class name, to find all possible target elements that can be shown/hidden.
 * - add the target class to each target component that can be shown/hidden
 * - add the class hidden to each target component to make them initially hidden
 * - add the attribute showhidetargetvalue to each target component, the value should equal the value of the select 
 * - OR add the attribute showhidetargetvalues to each target component, value should be a comma-separated string and includes the values of the select
 *   option that will unhide this element.
 */
(function (document, $) {
    "use strict";
    // when dialog gets injected
    $(document).on("foundation-contentloaded", function (e) {
        // if there is already an inital value make sure the according target element becomes visible
        showHideHandler($(".cq-dialog-dropdown3-showhide", e.target));
    });

    $(document).on("selected", ".cq-dialog-dropdown3-showhide", function (e) {
        showHideHandler($(this));
    });

    function showHideHandler(el) {
        el.each(function (i, element) {
            if ($(element).is("coral-select")) {
                // handle Coral3 base drop-down
                Coral.commons.ready(element, function (component) {
                    showHide(component, element);
                    component.on("change", function () {
                        showHide(component, element);
                    });
                });
            } else {
                // handle Coral2 based drop-down
                var component = $(element).data("select");
                if (component) {
                    showHide(component, element);
                }
            }
        })
    }

    function showHide(component, element) {
        // get the selector to find the target elements. its stored as data-.. attribute
        var target = $(element).data("cqDialogDropdown3ShowhideTarget");
        var $target = $(target);

        if (target) {
            var value;
            if (typeof component.value !== "undefined") {
                value = component.value;
            } else if (typeof component.getValue === "function") {
                value = component.getValue();
            }

            $target.each(function (index, element) {
                // make sure all unselected target elements are hidden.
                // unhide the target elements that contains the selected value as data-showhidetargetvalue attribute and data-showhidetargetvalues attribute
                var singleTarget = element.dataset.showhidetargetvalue;
                var multiTargets = element.dataset.showhidetargetvalues;
                var show = false;
                if (multiTargets) {
                    show = element && multiTargets.split(",").includes(value);
                } else {
                    show = element && singleTarget === value;
                }
                setVisibilityAndHandleFieldValidation($(element), show);
            });
        }
    }

    /**
     * Shows or hides an element based on parameter "show" and toggles validations if needed. If element
     * is being shown, all VISIBLE fields inside it whose validation is false would be changed to set the validation
     * to true. If element is being hidden, all fields inside it whose validation is true would be changed to
     * set validation to false.
     *
     * @param {jQuery} $element Element to show or hide.
     * @param {Boolean} show <code>true</code> to show the element.
     */
    function setVisibilityAndHandleFieldValidation($element, show) {
        if ($element.is('coral-select,._coral-Textfield,coral-checkbox')) {
            $element = $element.parent();
        }
        if (show) {
            $element.removeClass("hide");
            $element.find("input[aria-required=false], coral-multifield[aria-required=false], foundation-autocomplete[aria-required=false], coral-select[required]")
                .filter(":not(.hide>input)").filter(":not(input.hide)")
                .filter(":not(foundation-autocomplete[aria-required=false] input)")
                .filter(":not(.hide>coral-multifield)").filter(":not(input.coral-multifield)").each(function (index, field) {
                toggleValidation($(field), false);
            });
        } else {
            $element.addClass("hide");
            $element.find("input[aria-required=true], coral-multifield[aria-required=true], foundation-autocomplete[required], coral-select[required]")
                .filter(":not(foundation-autocomplete[required] input)")
                .each(function (index, field) {
                    toggleValidation($(field), true);
                });
        }
    }

    /**
     * If the form element is not shown we have to disable the required validation for that field.
     *
     * @param {jQuery} $field To disable / enable required validation.
     * @param {Boolean} forceHiding value used to set the disabled attribute
     *
     */
    function toggleValidation($field, forceHiding) {
        var required = $field.prop("required");
        var ariaRequired = $field.attr('aria-required');
        var notRequired = ariaRequired === 'true';

        if ($field.is("foundation-autocomplete") && required !== 'undefined') {
            if (required === true) {
                $field[0].required = false;
                $field.attr('aria-required', false);
            } else if (required === false) {
                $field[0].required = true;
                $field.removeAttr('aria-required');
            }
        } else if (typeof ariaRequired !== 'undefined') {
            $field.attr('aria-required', String(!notRequired));
        } else if ($field.is('coral-select')) {
            $field.attr('disabled', forceHiding);
        }

        var api = $field.adaptTo("foundation-validation");
        if (api) {
            if (notRequired) {
                api.checkValidity();
            }
            api.updateUI();
        }
    }
})(document, Granite.$);
