window.addEventListener('load', () =>{
	window.ModalPopup = {
		addWrapper:(element) => {
			const overlay = document.createElement('div');
			overlay.classList.add('backdrop-overlay');
			element.appendChild(overlay);
			const modalWrapper = document.createElement('div');
			modalWrapper.classList.add('modal-container','activate');
			const dialogWrapper = document.createElement('div');
			dialogWrapper.classList.add('modal-dialog');
			const contentWrapper = document.createElement('div');
			contentWrapper.classList.add('modal-content');
			dialogWrapper.appendChild(contentWrapper);
			modalWrapper.appendChild(dialogWrapper);
			element.appendChild(modalWrapper);
		},
		showPopup : () => {
			if(document.querySelector('.modal-container.activate .modal-content *')) {
				document.querySelector('.modal-container.activate').classList.add('show-modal');
				document.querySelector('.modal-container.activate').previousSibling.classList.add('show-overlay');
				document.body.classList.add('no-scroll');
				MerkleUtils.replaceListener('.modal-container.activate .modal-content .cmp-container .button', 'click', ModalPopup.hidePopup);
			}
		},
		hidePopup : (event) => {
			event.target.closest('.modal-container').classList.remove('show-modal','activate');
			event.target.closest('.modal-container').previousSibling.classList.remove('show-overlay');
			document.body.classList.remove('no-scroll');
		},
        addButton: () => {
            const gridDiv = document.querySelector('.modal-container.activate .modal-content .aem-Grid--12 .modal-content .aem-Grid--12');

            if (gridDiv) {
                const buttonElement = document.createElement('div');
                buttonElement.className = 'button brand-primary model-button';

                const innerButtonElement = document.createElement('button');
                innerButtonElement.type = 'button';
                innerButtonElement.id = 'button-model';
                innerButtonElement.className = 'cmp-button';

                const spanElement = document.createElement('span');
                spanElement.className = 'cmp-button__icon cmp-button__icon--close-icon';
                spanElement.setAttribute('aria-hidden', 'true');

                innerButtonElement.appendChild(spanElement);
                buttonElement.appendChild(innerButtonElement);

				// Add click event listener to the new button
				buttonElement.addEventListener('click', ModalPopup.hidePopup);

                gridDiv.appendChild(buttonElement);
            }
        },
        addModal: (event) => {
            event.preventDefault();
            const data = JSON.parse(event.currentTarget.getAttribute('data-cmp-data-layer'));
            const link = data[Object.keys(data)]['xdm:linkURL'];
            const modalContainer = event.currentTarget.parentElement.querySelector('.modal-container');

            if (!modalContainer) {
                ModalPopup.addWrapper(event.currentTarget.parentElement);
            } else {
                modalContainer.classList.add('show-modal', 'activate');
                modalContainer.previousSibling.classList.add('show-overlay');
            }

            const dataArea = event.currentTarget.parentElement.querySelector('.modal-content');
            MerkleUtils.LoadExternalContent(link, dataArea, [ModalPopup.showPopup, ModalPopup.addButton]);
        },
        modal: {
            addListeners: () => {
                if (document.querySelector('.modal > *')) {
                    MerkleUtils.replaceListener('.modal > *', 'click', ModalPopup.addModal);
                }
            }
        }
    };

    ModalPopup.modal.addListeners();
});
