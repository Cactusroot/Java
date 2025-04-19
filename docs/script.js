const files = ["Java", "Aufgaben", "Bonus", "Infos"];

let html = '';
files.forEach(item => {
    html += `<a href="#" onclick="displayPdf('${item}.pdf'); return false;">${item}</a>`;
});

document.getElementById('repo-content').innerHTML = html;

function displayPdf(url) {   
    document.getElementById('pdf-frame').data = url;
    document.getElementById('pdf-viewer').style.display = 'block';
}

function hidePdfViewer() {
    document.getElementById('pdf-viewer').style.display = 'none';
    document.getElementById('pdf-frame').data = '';
}