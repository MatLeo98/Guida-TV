function messageFunction(message) {
  alert(message);
}

document.getElementById("hamburger").onclick = function (){
            document.getElementById("menu").classList = "showing flex-column";
}
document.getElementById("chiudi").onclick = function (){
            document.getElementById("menu").classList = "hiding flex-column";
}




