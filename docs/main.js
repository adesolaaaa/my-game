var serverUrl = "ws://" + location.hostname + ":" + (Number(location.port) + 100);

var connection = new WebSocket(serverUrl);
showLoginPage();
connection.onopen = function (evt) {
    console.log("open");
}
connection.onclose = function (evt) {
    console.log("close");
    document.getElementById("errorBox").innerHTML += "lost connection to server\n";
}

connection.onmessage = function (evt) {
    var msg =  evt.data;
    const obj = JSON.parse(msg);
    if (obj.type != "TimerResponse")
        console.log(obj);
    switch (obj.type) {
        case "ConnectionStartResponse":
            document.getElementById("title").innerHTML += obj.eventData.version;
            break;
        case "LoginResponse":
            if (obj.eventData.loggedIn) {
                showgridPage();
                showLobbyPage();
            } else{
                let errorMessage = document.getElementById("loginError");
                errorMessage.innerHTML =
                `<p>Please enter a different username, the one you entered is already taken</p>`;
            }
            break;
        case "PlayerListResponse":
            updatePlayerList(obj.eventData.onlineUsers);
            break;
        case "LobbyUpdateResponse":
            updateLobbies(obj.eventData.lobbies);
            break;
        case "LeaderBoardResponse":
            updateLeaderboard(obj.eventData);
            break;
        case "StartGameResponse":
            showGamePage();
            break;
        case "GameOverResponse":
            showLobbyPage();
            break;
        case "GameResponse":
            updateGame(obj.eventData.grid.grid, obj.eventData.grid.wordIndices, obj.eventData.players, obj.eventData.timeToCreate, obj.eventData.uniformity, obj.eventData.intersections, obj.eventData.density);
            break;
        case "ChatResponse":
            updateChat(obj.eventData.user, obj.eventData.message);
            break;
        case "UserResponse":
            updateUser(obj.eventData.name, obj.eventData.color, obj.eventData.totalScore, obj.eventData.currentGameScore);
            break;
        case "TimerResponse":
            updateTimer(obj.eventData);
            break;
        default:
            console.log(`unknown message type: ${obj.type}`);
            break;
    }
}

function showgridPage() {
    document.getElementById('loginForm').style.display = 'none';
    document.getElementById('lobby').style.display = 'block';
    document.getElementById('welcome').style.display = 'none';
}

function showLoginPage() {
    document.getElementById('game').style.display = 'none';
    document.getElementById('lobby').style.display = 'none';
}
function showLobbyPage() {
    document.getElementById('game').style.display = 'none';
    document.getElementById('lobby').style.display = 'block';
    document.getElementById('welcome').style.display = 'none';
}
function showGamePage() {
    document.getElementById('lobby').style.display = 'none';
    document.getElementById('game').style.display = 'block';
    document.getElementById('welcome').style.display = 'none';
}



let grid = [];
for (let i = 0; i < 20; i++) {
    grid[i] = [];
    for (let j = 0; j < 20; j++) {
        let gridElement = {
            letter: "X",
            selectedBy: [],
            foundBy: []
        };
        grid[i].push(gridElement);
    }
}





