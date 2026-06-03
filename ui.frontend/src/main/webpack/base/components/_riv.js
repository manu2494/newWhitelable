import * as rive from "@rive-app/canvas";

// Define Fit and Alignment options
const Fit = rive.Fit;
const Alignment = rive.Alignment;

window.DynamicRiveLoader = {
  loadRiveComponents: async () => {
    const riveComponents = document.querySelectorAll(".rive-component");

    if (riveComponents.length === 0) {
      return;
    }

    const rivePromises = Array.from(riveComponents).map(async (riveComponent, index) => {
      const canvasElement = riveComponent.querySelector("canvas");

      if (!canvasElement) {
        console.error(`Canvas element not found in the Rive component at index ${index}.`);
        return null;
      }

      canvasElement.id += `_${index}`;

      try {
        const src =
          riveComponent.getAttribute("data-rive-file-path") ||
          riveComponent.getAttribute("data-rive-url");
        const animationDuration =
          parseFloat(riveComponent.getAttribute("data-animation-duration")) || 0;

        if (!src) {
          throw new Error("Rive source file path not provided");
        }

        // Define layout options
        const layout = new rive.Layout({
          fit: Fit.FitWidth, // Change this to your desired Fit option
          alignment: Alignment.TopCenter, // Change this to your desired Alignment option
        });

        const r = new rive.Rive({
          src,
          canvas: canvasElement,
          layout, // Pass the layout object
          autoplay: true,
          onLoad: () => handleRiveLoad(r, riveComponent, animationDuration),
          onError: (error) => handleRiveError(canvasElement.id, error),
        });

        window.addEventListener("resize", () => handleResize(r));

        return r;
      } catch (error) {
        console.error("Error creating Rive instance:", error.message);
        return null;
      }
    });

    const riveInstances = await Promise.all(rivePromises.filter(Boolean));
  },
};

function handleResize(riveInstance) {
  riveInstance.resizeDrawingSurfaceToCanvas();
}

function handleRiveLoad(riveInstance, riveComponent, animationDuration) {
  riveComponent.closest('.rive').classList.add("show-rive-animation");
  // Add a class to show the component
  handleResize(riveInstance);
  // Set a timer to remove the class after animationDuration seconds
  setTimeout(() => {
    riveInstance.pause();
    // Remove the class to hide the component
    riveComponent.closest('.rive').classList.remove("show-rive-animation");
    riveComponent.closest('.rive').classList.add("rive-animation-completed");
  }, animationDuration * 1000); // Convert seconds to milliseconds
}

function handleRiveError(canvasId, error) {
  console.error(`Error loading Rive animation for ${canvasId}:`, error);
}

window.addEventListener("load", () => {
  window.DynamicRiveLoader.loadRiveComponents();
});
