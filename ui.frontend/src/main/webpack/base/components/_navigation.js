let originalNavPrimary = null;
let originalNavSecondary = null;

/* eslint-disable-next-line max-len */
const svgIcon = "<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 384 512'><path d='M342.6 233.4c12.5 12.5 12.5 32.8 0 45.3l-192 192c-12.5 12.5-32.8 12.5-45.3 0s-12.5-32.8 0-45.3L274.7 256 105.4 86.6c-12.5-12.5-12.5-32.8 0-45.3s32.8-12.5 45.3 0l192 192z'/></svg>";

const createSpanIcon = () => {
  const spanIcon = document.createElement("span");
  spanIcon.setAttribute("class", "nav-spanIcon");
  spanIcon.innerHTML = svgIcon;
  return spanIcon;
};

const loadNavIcon = () => {
  document
    .querySelectorAll(".nav-primary .cmp-navigation__item")
    .forEach((item) => {
      if (item.childElementCount > 1 && !item.querySelector(".nav-spanIcon")) {
        item.appendChild(createSpanIcon());
      }
    });
};

const removeArrowIcon = () => {
  document.querySelectorAll(".nav-spanIcon").forEach((icon) => icon.remove());
};

const createSubListWrapper = (item) => {
  if (
    item.childElementCount > 1 &&
    !item.querySelector(".navSublist-wrapper")
  ) {
    const wrapper = document.createElement("div");
    wrapper.classList.add("navSublist-wrapper");

    const subListGrid = document.createElement("div");
    subListGrid.classList.add("navsubList-grid");

    const navTitleWrapper = document.createElement("div");
    navTitleWrapper.classList.add("navsubList-Title");
    const navTitle = document.createElement("span");

    navTitle.innerHTML = item.querySelector(
      ".cmp-navigation__item-link"
    ).innerHTML;

    navTitleWrapper.appendChild(navTitle);

    const subList1 = item.lastElementChild;

    wrapper.appendChild(subListGrid);
    subListGrid.appendChild(navTitleWrapper);
    subListGrid.appendChild(subList1);

    item.appendChild(wrapper);
  }
};

const addIconToNavSecondary = () => {
  const navIcon = document.querySelectorAll(
    ".nav-secondary .cmp-navigation__item"
  );

  navIcon.forEach((item) => {
    const spanIcon = document.createElement("span");
    spanIcon.classList.add("nav-spanIcon");
    if (item.childElementCount > 1 && !item.querySelector(".nav-spanIcon")) {
      spanIcon.innerHTML = svgIcon;
      item.appendChild(spanIcon);
    }
  });
};

const loadSubListWrapper = () => {
  const subListWrapper = document.querySelectorAll(
    ".nav-secondary > .cmp-navigation > .cmp-navigation__group > .cmp-navigation__item--level-0"
  );

  subListWrapper.forEach(createSubListWrapper);
  addIconToNavSecondary();
};

const resetNavStructure = () => {
  const currentNavPrimary = document.querySelector(
    ".navigation .cmp-navigation"
  );
  if (originalNavPrimary && currentNavPrimary) {
    currentNavPrimary.innerHTML = originalNavPrimary.innerHTML;
  }

  const currentNavSecondary = document.querySelectorAll(
    ".nav-secondary .cmp-navigation__item"
  );
  if (
    originalNavSecondary &&
    currentNavSecondary.length === originalNavSecondary.length
  ) {
    currentNavSecondary.forEach((item, index) => {
      item.innerHTML = originalNavSecondary[index].innerHTML;
    });
  }

  // Additional logic to remove or reset any other modifications made
};

// Mobile Nav

const loadMobileView = () => {
  var mobItem = document.querySelectorAll(".mobileNav .cmp-navigation__item a");

  mobItem.forEach((item) => {
    // create a span for Icon
    var spanIcon = document.createElement("span");
    spanIcon.setAttribute("class", "nav-spanIcon");
    item.appendChild(spanIcon);
    //var spanItem=item.querySelector('.nav-spanIcon');
    if (item.childElementCount > 2) {
      var itemAnchor = item.querySelector(".cmp-navigation__item-link");
      if (itemAnchor) {
        itemAnchor.style.pointerEvents = "none";
      }
      spanIcon.innerHTML = svgIcon;
    } else {
      var itemAnchor = item.querySelector(".cmp-navigation__item-link");
      if (itemAnchor) {
        itemAnchor.style.pointerEvents = "auto";
      }
      spanIcon.innerHTML = svgIcon;
    }
  });

  // mobile-view
  const expendList = (event, ev) => {
    if (event && event.style.display == "") {
      event.style.display = "block";
      ev.target.style.backgroundPosition = "right 18px";
      ev.target.classList.add("closeSub-menu");
      ev.target.classList.remove("showSub-menu");
    } else if (event && event.style.display == "block") {
      event.style.display = "none";
      ev.target.style.backgroundPosition = "right 50%";
      ev.target.classList.remove("closeSub-menu");
      ev.target.classList.add("showSub-menu");
    } else if (event && event.style.display == "none") {
      event.style.display = "block";
      ev.target.style.backgroundPosition = "right 18px";
      ev.target.classList.add("closeSub-menu");
      ev.target.classList.remove("showSub-menu");
    }
  };

  if (document.getElementsByClassName("mobileNav")) {
    document
      .querySelectorAll(".mobileNav .cmp-navigation__item")
      .forEach((data) => {
        if (
          data.children.length >= 2 &&
          document.getElementsByClassName("mobileNav")
        ) {
          data.classList.add("showSub-menu");
          data.classList.remove("subMenu");
        } else {
          data.classList.remove("showSub-menu");
          data.classList.add("subMenu");
        }
        data.addEventListener("click", (ev) => {
          // ev.target.style.backgroundPosition="right 4%";
          var event = ev.target.querySelector(".cmp-navigation__group");
          expendList(event, ev);
          ev.target
            .querySelectorAll(".cmp-navigation__item")
            .forEach((data1) => {
              data1.addEventListener("click", (ev1) => {
                var event1 = ev1.target.querySelector(".cmp-navigation__group");
                expendList(event1, ev1);
              });
            });
        });
      });
  }

  document.querySelectorAll("#mobileHeader-container").forEach((item) => {
    item.querySelector(".hamburger").addEventListener("click", () => {
      if (
        item.querySelector(".mobileNav").style.display == "none" ||
        item.querySelector(".mobileNav").style.display == ""
      ) {
        item.querySelector(".mobileNav").style.display = "block";
        item.querySelector(".hamburger").classList.add("hamburger-close");
      } else {
        item.querySelector(".mobileNav").style.display = "none";
        item.querySelector(".hamburger").classList.remove("hamburger-close");
      }
      // button.nextElementSibling.style.borderColor="red";
      // console.log(button.nextSibling.classList.contains("mobileNav"))
    });
    // button.
  });
};

document.addEventListener("DOMContentLoaded", function () {
  document.querySelectorAll('input[name="type"]').forEach(function (radio) {
    radio.addEventListener("change", function () {
      const selectedType = document.querySelector(
        'input[name="type"]:checked'
      ).value;
      removeArrowIcon(); // Always remove arrow icons first
      resetNavStructure(); // Reset the structure before applying changes

      switch (selectedType) {
        case "nav-primary":
          if (!originalNavPrimary) {
            const navPrimaryItem = document.querySelector(
              ".navigation .cmp-navigation"
            );
            if (navPrimaryItem) {
              originalNavPrimary = navPrimaryItem.cloneNode(true);
            }
          }
          loadNavIcon();
          break;
        case "nav-secondary":
          if (!originalNavSecondary) {
            originalNavSecondary = Array.from(
              document.querySelectorAll(".nav-secondary .cmp-navigation__item")
            ).map((item) => item.cloneNode(true));
          }
          loadSubListWrapper();
          break;
        case "mobileNav":
          loadMobileView();
          break;
        default:
          // Handle other cases or do nothing
          break;
      }
    });
  });
});
