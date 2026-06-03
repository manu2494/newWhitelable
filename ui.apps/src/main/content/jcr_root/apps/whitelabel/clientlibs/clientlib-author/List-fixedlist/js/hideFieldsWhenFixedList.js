(function (document, $) {
    "use strict";
    $(document).on("foundation-contentloaded", function (e) {
        Coral.commons.ready(function () {
			let selectfield = document.querySelector('.cq-dialog-dropdown-showhide');
            fixedListFieldsShowHideHandle(selectfield);
        });
    });

    $(document).on("change", ".cq-dialog-dropdown-showhide", function () {
        	let selectfield = document.querySelector('.cq-dialog-dropdown-showhide');
            fixedListFieldsShowHideHandle(selectfield);
    });

    function fixedListFieldsShowHideHandle(el) {
        let parent = el.parentElement.parentElement;
        let children = parent.querySelectorAll(".coral-Form-fieldlabel")
        if(el.value === "static") {
            for (const element of children) {
                if (element.innerHTML == "Order By" || element.innerHTML == "Sort Order") {
                    element.parentElement.classList.add("hide");
                }
            }
        }
        else {
            for (const element of children) {
                if (element.innerHTML == "Order By" || element.innerHTML == "Sort Order") {
                    element.parentElement.classList.remove("hide");
                }
            }
        }
    }

})(document, Granite.$);