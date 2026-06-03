const classExists = document.getElementsByClassName('cmp-progressbar ').length > 0;
if(classExists) {
    const progressBar = document.querySelectorAll('.cmp-progressbar');
    for (let i = 0; i < progressBar.length; i ++) {
        let progressDiv = progressBar[i].querySelector('.cmp-progressbar__label--completed');
        const progressWidth = progressDiv.innerText;
        progressDiv.innerText = `${progressWidth}%`;
        progressBar[i].querySelector('.cmp-progressbar__bar').style.width = `${progressWidth}%`;
    }
}
