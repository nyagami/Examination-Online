var modal = document.getElementById("modal");
var btn = document.getElementById("btnModal");
var btn1 = document.getElementById("trolai");
var modal_1 = document.getElementById("modal_1");
var btn = document.getElementById("btnModal");
var btn1 = document.getElementById("trolai");
var btn_sua = document.getElementById("btn_sua");
var btn_1 = document.getElementById("trolai_1");
btn1.onclick = function() {
    modal.style.display = "none";
}
btn.onclick = function() {
    modal.style.display = "flex";
}
window.onclick = function(event) {
    if (event.target == modal) {
      modal.style.display = "none";
    }
}
btn_1.onclick = function() {
    modal_1.style.display = "none";
}
btn_sua.onclick = function() {
    modal_1.style.display = "flex";
}
window.onclick = function(event) {
    if (event.target == modal_1) {
      modal_1.style.display = "none";
    }
}