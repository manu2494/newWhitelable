import { tns } from 'tiny-slider';
let carousels = [];
// rewind
var restart = () => {
	carousels.forEach(carousel => {
		carousel.destroy();
	});
	carousels = [];

	Array.from(document.querySelectorAll('.rewind-items')).forEach(carouselContainer => {
		carouselContainer.classList.remove("rewind-items");
	});

	Array.from(document.querySelectorAll('.loop-items')).forEach(carouselContainer => {
		carouselContainer.classList.remove("loop-items");
	});

	Array.from(document.querySelectorAll('.no-loop-items')).forEach(carouselContainer => {
		carouselContainer.classList.remove("no-loop-items");
	});

	Array.from(document.querySelectorAll('.center-items')).forEach(carouselContainer => {
		carouselContainer.classList.remove("center-items");
	});

	Array.from(document.querySelectorAll('.auto-play-item')).forEach(carouselContainer => {
		carouselContainer.classList.remove("auto-play-item");
	});

	Array.from(document.querySelectorAll('.no-controls-item')).forEach(carouselContainer => {
		carouselContainer.classList.remove("no-controls-item");
	});

	Array.from(document.querySelectorAll('.no-indicators-item')).forEach(carouselContainer => {
		carouselContainer.classList.remove("no-indicators-item");
	});

	carousel();
};

document.addEventListener('restart', restart);

var carousel = () => {
	if (document.querySelectorAll(".rewind")) {

		Array.from(document.querySelectorAll('.rewind .cmp-container .aem-Grid')).forEach(carouselContainer => {
			carouselContainer.classList.add("rewind-items");
		});

		document.querySelectorAll('.rewind-items').forEach(rewindSlider => {
			carousels.push(tns({
				container: rewindSlider,
				responsive: {
					250: {
						items: 1
					},
					650: {
						items: 2
					},
					1199: {
						items: 4,
					}
				},
				mouseDrag: true,
				loop: false,
				rewind: true,
				swipeAngle: false,
				slideBy: "page",
				speed: 400,
			}));
		});
	}


	// Loop

	if (document.querySelector(".loop")) {
		Array.from(document.querySelectorAll('.loop .cmp-container .aem-Grid')).forEach(carouselContainer => {

			carouselContainer.classList.add("loop-items");

		});
		document.querySelectorAll('.loop-items').forEach(loopSlider => {

			carousels.push(tns({
				container: loopSlider,
				responsive: {
					250: {
						items: 1
					},
					650: {
						items: 2
					},
					1199: {
						items: 4,
					}
				},
				mouseDrag: true,
				loop: true,
				swipeAngle: false,
				slideBy: "page",
				speed: 400,
			}));
		});
	}


	// no loop

	if (document.querySelector(".no-loop")) {
		Array.from(document.querySelectorAll('.no-loop .cmp-container .aem-Grid')).forEach(carouselContainer => {

			carouselContainer.classList.add("no-loop-items");

		});
		document.querySelectorAll('.no-loop-items').forEach(noloopSlider => {
			carousels.push(tns({
				container: noloopSlider,
				responsive: {
					250: {
						items: 1
					},
					650: {
						items: 2
					},
					1199: {
						items: 4,
					}
				},
				mouseDrag: true,
				loop: false,
				swipeAngle: false,
				slideBy: "page",
				speed: 400,
			}));
		});
	}

	// center

	if (document.querySelector(".center")) {
		Array.from(document.querySelectorAll('.center .cmp-container .aem-Grid')).forEach(carouselContainer => {

			carouselContainer.classList.add("center-items");

		});
		document.querySelectorAll('.center-items').forEach(centerSlider => {
			carousels.push(tns({
				container: centerSlider,
				responsive: {
					250: {
						items: 1
					},
					650: {
						items: 2
					},
					1199: {
						items: 4,
					}
				},
				center: true,
				swipeAngle: false,
				speed: 400,
			}));
		});
	}


	// autoplay

	if (document.querySelector(".auto-play")) {
		Array.from(document.querySelectorAll('.auto-play .cmp-container .aem-Grid')).forEach(carouselContainer => {

			carouselContainer.classList.add("auto-play-item");

		});
		document.querySelectorAll('.auto-play-item').forEach(autoplaySlider => {

			carousels.push(tns({
				container: autoplaySlider,
				responsive: {
					250: {
						items: 1
					},
					650: {
						items: 2
					},
					1199: {
						items: 4,
					}
				},
				mouseDrag: true,
				loop: true,
				autoplay: true,
				autoplayTimeout: 3500,
				swipeAngle: false,
				slideBy: "page",
				speed: 400,
			}));
		});
	}

	// no control

	if (document.querySelector(".no-controls")) {
		Array.from(document.querySelectorAll('.no-controls .cmp-container .aem-Grid')).forEach(carouselContainer => {

			carouselContainer.classList.add("no-controls-item");

		});
		document.querySelectorAll('.no-controls-item').forEach(nocontrolSlider => {

			carousels.push(tns({
				container: nocontrolSlider,
				responsive: {
					250: {
						items: 1
					},
					650: {
						items: 2
					},
					1199: {
						items: 4,
					}
				},
				mouseDrag: true,
				loop: true,
				autoplay: true,
				autoplayTimeout: 3500,
				swipeAngle: false,
				slideBy: "page",
				speed: 400,
				controls: false,
			}));
		});
	}

	// no indicator

	if (document.querySelector(".no-indicators")) {
		Array.from(document.querySelectorAll('.no-indicators .cmp-container .aem-Grid')).forEach(carouselContainer => {

			carouselContainer.classList.add("no-indicators-item");

		});
		document.querySelectorAll('.no-indicators-item').forEach(noindicatorSlider => {

			carousels.push(tns({
				container: noindicatorSlider,
				responsive: {
					250: {
						items: 1
					},
					650: {
						items: 2
					},
					1199: {
						items: 4,
					}
				},
				mouseDrag: true,
				loop: true,
				autoplay: true,
				autoplayTimeout: 3500,
				swipeAngle: false,
				slideBy: "page",
				speed: 400,
				nav: false,
			}));

		});
	}

};

carousel();
