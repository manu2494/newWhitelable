

document.addEventListener("DOMContentLoaded", function () {
  const typeContainers = document.querySelectorAll(
    "#teaser-type-container .cmp-form-options--radio"
  );

  let toggleLegendDisplay = (legendText, displayValue) => {
    let legends = document.querySelectorAll(
      ".options .cmp-form-options__legend"
    );
    legends.forEach((legend) => {
      if (legend.textContent.trim() === legendText) {
        legend.parentElement.style.display = displayValue;
      }
    });
  };

  // Capture initial states of combination types
  const initialStates = {};
  let combinationsTypeInputs = document.querySelectorAll(
    `#teaser-type-container .cmp-form-options--radio [name*="Combinations Type"]`
  );
  combinationsTypeInputs.forEach((input) => {
    initialStates[input.name] = input.checked;
  });

  let resetCombinationType = () => {
    let combinationsTypeInputs = document.querySelectorAll(
      `#teaser-type-container .cmp-form-options--radio [name*="Combinations Type"]`
    );
    combinationsTypeInputs.forEach((input) => {
      if (input.value !== "none" && input.checked !== initialStates[input.name]) {
        input.checked = false;
      }
    });
  };

  document.querySelectorAll('input[name="type"]').forEach(function (radio) {
    radio.addEventListener("change", function () {
      // Reset combinations type input value to "none" only for combination types
      resetCombinationType();

      // Hide all other type containers
      for (let i = 1; i < typeContainers.length; i++) {
        typeContainers[i].style.display = "none";
      }

      // Get the selected type
      const selectedType = document.querySelector(
        'input[name="type"]:checked'
      ).value;

      // Show the corresponding type container and set legends display
      switch (selectedType) {
        case "image-position":
          typeContainers[1].style.display = "block"; // Show Image Position Combination Types
          toggleLegendDisplay("Width", "none");
          toggleLegendDisplay("Vertical", "block");
          break;
        case "teaser-hero":
          typeContainers[2].style.display = "block"; // Show Teaser Hero Combination Types
          toggleLegendDisplay("Width", "block");
          toggleLegendDisplay("Vertical", "block");
          break;
        case "teaser-card":
          typeContainers[3].style.display = "block"; // Show Teaser Card Combinations Type
          toggleLegendDisplay("Width", "none");
          toggleLegendDisplay("Vertical", "none");
          break;
        case "teaser-cvp":
          typeContainers[4].style.display = "block"; // Show Teaser CVP Combinations Type
          toggleLegendDisplay("Width", "block");
          toggleLegendDisplay("Vertical", "block");
          break;
        default:
          if (typeContainers[0]) {
            typeContainers[0].style.display = "block";
          }
          toggleLegendDisplay("Vertical", "block");
          toggleLegendDisplay("Width", "block");
      }
    });
  });
});
