function addUser(event) {
    event.preventDefault(); 

    let username = document.getElementById("username").value;
    // Dont send a request if the value is empty

    connection.send(JSON.stringify({
        "type": "UserLoginRequest",
        "eventData": {
            "username": username
        }
    }));
    return false;
}

function joinLobby(lobbyId){
    connection.send(JSON.stringify({
        "type": "JoinLobbyRequest",
        "eventData": {
            "lobby": lobbyId
        }
    }));
}

function selectGrid(x, y){
    console.log(x,y)
    connection.send(JSON.stringify({
        "type": "SelectGridRequest",
        "eventData": {
            "x": x,
            "y": y,
        }
    }));
}

function addChat(event) {
    event.preventDefault(); 

    let message = document.getElementById("message").value;
  
    if(message !== "" && message){
          connection.send(JSON.stringify({
              "type": "ChatRequest",
              "eventData": {
                  "message": message
              }
          }));

    }
    document.getElementById("message").value = "";
    return false;

}
