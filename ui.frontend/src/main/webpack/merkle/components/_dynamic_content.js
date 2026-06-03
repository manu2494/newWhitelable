
function updatepegaContent(){
    const elementsDataMapArray = [];
    const items = [];
    Array.from(document.querySelectorAll('.pegaContent')).forEach(dc =>{
        const item = dc.querySelector('.cmp-dynamic-browser').getAttribute('data-dynamic');
        items.push(item);
        const elementDataMap = {};
        elementDataMap.item = item;
        elementDataMap.dc = dc;
        elementsDataMapArray.push(elementDataMap);
    });

    const url = '/pegadataservice?items='+items.join(',');
    const userID = sessionStorage.getItem("userID");

    fetch(url, {
                 method: "GET",
                 headers: {
                   "Content-Type": "application/json;charset=UTF-8",
                   "user-id": userID?userID:"anonymous"
                 }})
    .then(response => response.json())
    .then(jsonResponse => {
        const contentMapping = {};
        jsonResponse.forEach(pegaContent => {
            contentMapping[pegaContent.id] = pegaContent.content;
        });
        return contentMapping;
    })
    .then(mappedResponse => {
        elementsDataMapArray.forEach(elementDataMap => {
            const dcElement = elementDataMap.dc;
            const item = elementDataMap.item;
            const htmlSrc = mappedResponse[item];
            if(htmlSrc){
                dcElement.innerHTML = htmlSrc;
                dcElement.classList.remove('pegaContent');
            }
        });
    });
}

updatepegaContent();

function submitUser() {
    document.getElementById('#useridsubmit');
    var userID = document.getElementById('userid');
    window.sessionStorage.setItem('userID',userID.value);
}


let element = document.getElementById('useridsubmit')
// Binding a click event
if (element) {
	element.addEventListener('click', submitUser)
}
