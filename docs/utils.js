const colors = {
    0: "#90ee90",
    1: "#e6e6fa",
    2: "#ffcccb",
    3: "#ffffe0",
    4: "#ffa500",
}

function addBackground(button, list){
    if(list.length === 1){
        button.style.backgroundColor = colors[list[0]];
    } else if(list.length === 2){
        button.style.backgroundImage = `conic-gradient(
            ${colors[list[0]]} 0deg,
            ${colors[list[0]]} 180deg,
            ${colors[list[1]]} 180deg,
            ${colors[list[1]]} 360deg
        )`;
    } else if(list.length === 3){
        button.style.backgroundImage = `conic-gradient(
            ${colors[list[0]]} 0deg,
            ${colors[list[0]]} 120deg,
            ${colors[list[1]]} 120deg,
            ${colors[list[1]]} 240deg,
            ${colors[list[2]]} 240deg,
            ${colors[list[2]]} 360deg
        )`;
    } else if(list.length === 4){
        button.style.backgroundImage = `conic-gradient(
            ${colors[list[0]]} 0deg,
            ${colors[list[0]]} 90deg,
            ${colors[list[1]]} 90deg,
            ${colors[list[1]]} 180deg,
            ${colors[list[2]]} 180deg,
            ${colors[list[2]]} 270deg,
            ${colors[list[3]]} 270deg,
            ${colors[list[3]]} 360deg
        )`;
    }

}

function toggleStats(){
    let statsDiv = document.getElementById("stats");
    let displayStyle = window.getComputedStyle(statsDiv).display;
    if (statsDiv.style.display === "none" || statsDiv.style.display === "") {
        statsDiv.style.display = "flex"; 
    } else {
        statsDiv.style.display = "none";
    }
}