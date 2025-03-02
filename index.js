let html = '<ul>';
["JavaAufgaben.pdf", "Woche 1.pdf", "Woche 2.pdf", "Woche 3.pdf"].forEach(item => {
    html += `<li>
                 <a href="#" onclick="displayPdf('${item}'); return false;">
                     ${item.replace(/\.[^/.]+$/, "")}
                 </a>
             </li>`;
});
html += '</ul>';
document.getElementById('repo-content').innerHTML = html;

function displayPdf(url) {   
    document.getElementById('pdf-frame').data = url;
    document.getElementById('pdf-viewer').style.display = 'block';
    document.getElementById('pdf-viewer').scrollIntoView({ behavior: 'smooth' });
}

function hidePdfViewer() {
    document.getElementById('pdf-viewer').style.display = 'none';
    document.getElementById('pdf-frame').data = '';
}