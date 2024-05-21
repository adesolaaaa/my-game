function updatePlayerList(playerList){
    playerListElement = document.getElementById("playerList");
    playerListElement.innerHTML = "";
    for(let i = 0; i < playerList.length; i++){
        playerListElement.innerHTML +=
        `<p>${playerList[i]}</p>`;
    }
}

function updateLobbies(lobbyLists){
    lobbies = [document.getElementById("lobbyOnePlayers"), document.getElementById("lobbyTwoPlayers"), document.getElementById("lobbyThreePlayers")]

    for(let i = 0; i < lobbyLists.length; i++){
        lobbies[i].innerHTML = "";
        for(let j = 0; j < lobbyLists[i].length; j++){
            lobbies[i].innerHTML += `<p>${lobbyLists[i][j]}</p>`;
        }
    }
}


function updateLeaderboard(leaderBoardInfo){
    let leaderBoardElement = document.getElementById("leaderboard");
    leaderBoardElement.innerHTML = "";
    for(let i = 0; i < leaderBoardInfo["usernames"].length; i++){
        leaderBoardElement.innerHTML += `<p>${leaderBoardInfo["usernames"][i]}: ${leaderBoardInfo["scores"][i]}</p>`;
    }
}



function updateGame(grid, wordList, players, timeToCreate, uniformity, intersections, density){
    let timeToCreateDiv = document.getElementById("timeToCreate");
    timeToCreateDiv.innerHTML = timeToCreate;
    let uniformityDiv = document.getElementById("uniformity");
    uniformityDiv.innerHTML = uniformity;
    let intersectionsDiv = document.getElementById("intersections");
    intersectionsDiv.innerHTML = intersections;
    let densityDiv = document.getElementById("density");
    densityDiv.innerHTML = density;

    // Update leaderboard
    let gameLeaderboardDiv = document.getElementById("gameLeaderboard");
    gameLeaderboardDiv.innerHTML = "";
    for(let i = 0; i < players.length; i++){
        gameLeaderboardDiv.innerHTML += `<p><strong>${players[i].name}:</strong> ${players[i].currentGameScore}</p>`;
    }
    // Update word grid
    let gridDiv = document.getElementById("gameGrid");
    gridDiv.innerHTML = "";
    for (let i = 0; i < 20; i++) {
        const row = document.createElement("div");
        row.classList.add("grid-row");
        gridDiv.appendChild(row);
        for (let j = 0; j < 20; j++) {
            let button = document.createElement('button');
            button.style.border = '2px solid #FFFFFF';
            if(grid[i][j]["foundBy"].length){
                button.style.border = '2px solid #000000';
                addBackground(button, grid[i][j]["foundBy"]);
            }
            if(grid[i][j]["selectedBy"].length){
                button.style.border = '2px solid #CC9900';
            }
            button.type = 'button';
            button.textContent = grid[i][j]["letter"]; 
            
            button.addEventListener('click', function() {
                selectGrid(i, j);
            });
            
            button.classList.add('grid-button');
            row.appendChild(button);
  
        }
    }
    // update word list
    let wordListDiv = document.getElementById("wordList");
    wordListDiv.innerHTML = "";
    for (let i = 0; i < wordList.length; i++) {
        wordListDiv.innerHTML += wordList[i].word + " ";
    }
}
function updateChat(user, message){
    let wordListDiv = document.getElementById("chatBox");
    wordListDiv.innerHTML += `<p><strong>${user}:</strong> ${message}</p>`;
}

function updateUser(name, color, score, gameScore){
    let usernameLobby = document.getElementById("usernameLobby");
    let scoreLobby = document.getElementById("scoreLobby");

    usernameLobby.innerHTML = name;
    scoreLobby.innerHTML = "Score: " + score;

    let usernameGame = document.getElementById("usernameGame");
    let scoreGame = document.getElementById("scoreGame");

    usernameGame.innerHTML = name;
    let button = document.createElement("button");
    button.classList.add("grid-button");
    button.style.backgroundColor = colors[color];  
    usernameGame.appendChild(button);
    scoreGame.innerHTML = "Game Score: " + gameScore;


}

function updateTimer(time){
    let timer = document.getElementById("timer");
    timer.innerHTML = time;
}