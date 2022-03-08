import Player from './player.js';


//basics//
const startBtn = document.querySelector('#startBtn');
var playersNum = document.querySelector('#playersnum');
var modeSelect = document.querySelector('#mode');
const openingPage = document.querySelector('#opening-page'); 
const gamePage = document.querySelector('#game-page');
var isAdvancedMode = false;
var tenSecIsOver = false;
var hasClicked = false;
var isGameEnd = false;
const newGameBtn = document.querySelector('#new-game');
var numberOfPlayers;
const selectMode = document.querySelector('#mode');
var isRace;


const isSetBtn = document.querySelector('#isThereSet');
const whereIsSetBtn = document.querySelector('#whereSet');
var tds = document.querySelectorAll('#cardsTable td');
var lastRow;
var cardCounter;

//others
const othersDiv = document.querySelector('#others');
const plusThreeBtn = document.querySelector('#plusThree');
const setButtonBox = document.querySelector('#needIsSet');
const showSetButtonBox = document.querySelector('#needShowSet');
const autoPlusThreeBox = document.querySelector('#autoPlusThree');


selectMode.addEventListener('click',function(){
    if(selectMode.value === 'practice'){
        othersDiv.style.visibility = 'visible';
    } else {
        othersDiv.style.visibility = 'hidden';
    }
}) 

//cards and players//
var deck = [];
var cardsOnTable = [];
var selected =[];
var players = [];
var playerList = document.querySelector('#players');
const cardTable = document.querySelector('#cardsTable');
const basicCardIndeces = [18,19,20,21,22,23,24,25,26,
  45,46,47,48,49,50,51,52,53,72,73,74,75,76,77,78,79,80];
const deckCounter = document.querySelector('#deck span');
var playerButtons = []; 
var currentPlayer;

//timers//

var startingTime = 0;
var finalTime = null;
var timer = document.querySelector('#time > span');
const padTwo = num => num.toString().padStart(2,'0');


var timeUpdate = setInterval(function() {
    if(finalTime === null && !isGameEnd) {
        var now = new Date().getTime();
        var totalTime = now - startingTime;
        var secs = Math.floor(totalTime / 1000);
        var mins = Math.floor(secs / 60);
        var hours = Math.floor(mins / 60);
        var time = padTwo(hours) + ":" + padTwo(mins % 60) + 
          ":" + padTwo(secs % 60);
        timer.style.color='black';
        timer.innerHTML = time;
    } else {
        timer.style.color='red';
        timer.innerHTML = finalTime;
    }
    
}, 1000);


var tenSecTimer = document.querySelector('#tensec > span');
tenSecTimer.innerHTML = 10;
var timeLeft = 11;
var tenSecTimerActivate = setInterval(function(){
    if(hasClicked && playersNum.value != 1 && !isGameEnd){
        tenSecTimer.style.visibility = 'visible';
        if(timeLeft > 0){
            --timeLeft;
            tenSecTimer.innerHTML = timeLeft;
          if(timeLeft <= 3){
              tenSecTimer.style.color = 'red';
          } 
        }else {
            for(let i = 0; i < selected.length;++i){
                var imgNum = selected[i];
                var card = document.querySelector(`#cell${imgNum} > img`);
                card.style.border = '1px black solid';
            } 
            selected = []; 
            --currentPlayer.points;
            currentPlayer.selected = true;
            hasClicked = false;     
            tenSecIsOver = true;
            tenSecTimer.style.color = 'black';
            tenSecTimer.style.visibility = 'hidden';
            checkPlayers();
            refreshPoints();

        }
    } else {
        tenSecIsOver = false;
        tenSecTimer.style.color = 'black';
        tenSecTimer.style.visibility = 'hidden';
        timeLeft = 11;

    }

}, 1000);


function newGame(){
    for (let i = 0; i < tds.length; ++i){
        tds[i].style.visibility = 'hidden';
    }
    whereIsSetBtn.style.visibility = 'hidden';
    isSetBtn.style.visibility = 'hidden';
    plusThreeBtn.style.visibility = 'hidden';
    gamePage.style.visibility = 'hidden';
    timer.style.visibility = 'hidden';
    playerList.innerHTML = '';
    players = [];
    deck = [];
    cardsOnTable = [];
    hasClicked = false;
    isGameEnd = false;
    openingPage.style.visibility= 'visible';
}

newGameBtn.addEventListener('click', newGame);


function startGame(){
    othersDiv.style.visibility = 'hidden';
    isRace = selectMode.value == 'race';
    if(playersNum.value !== ''&& 
    modeSelect.value !== ''){
        numberOfPlayers = playersNum.value;
        if(numberOfPlayers == 1 ){
            if (isRace){
                finalTime = null;
                startingTime = new Date().getTime();
                timer.innerHTML='00:00:00';
                timer.style.visibility = 'visible';
            }
            hasClicked = true;
        } else {
            timer.innerHTML  ='00:00:00';
            timer.style.visibility = 'hidden';
        }
        openingPage.style.visibility = 'hidden';
        gamePage.style.visibility = 'visible';   
        setUpPlayers(numberOfPlayers);
        initDeck();
        handleCards();
        refreshDeckCount();
        if(isRace){
            isSetBtn.style.visibility = 'hidden';
            whereIsSetBtn.style.visibility = 'hidden';
            plusThreeBtn.style.visibility = 'hidden';
        }else {
            isSetBtn.style.visibility = 
               setButtonBox.checked ? 'visible' : 'hidden';
            whereIsSetBtn.style.visibility = 
               showSetButtonBox.checked ? 'visible' : 'hidden';
            plusThreeBtn.style.visibility =
               autoPlusThreeBox.checked ? 'hidden' : 'visible';
                 
        }
    }    
}

startBtn.addEventListener('click',startGame);


function setUpPlayers(num){
    for(let i = 0; i < num; ++i){
        var plName = 'player' + (i+1);
        players[i] = new Player(plName,
        0,false);
        var pl = document.createElement('p');
        var point = document.createElement('p');
        var radio = document.createElement('input');
        radio.type = 'radio';
        pl.innerHTML = plName;
        point.innerHTML = players[i].points;
        playerList.innerHTML += `<tr id="${plName}">
                       <td width="70px" heigth="10px" resize="none" contenteditable="true">${plName}</td>
                       <td id="points">${players[i].points}</td>
                      <td><button id="${plName}">SET</button></td>
                      <tr>`; 
                      
    }
    for (let i = 0; i < num; ++i){
        var setBtn = document.querySelector(`button#player${i+1}`);
        setBtn.addEventListener('click', playerSetAction);
        playerButtons.push(setBtn);
    }
    if (num == 1){
        currentPlayer = players[0];
    }

}



function playerSetAction(e){
    currentPlayer = players.find(pl => pl.name === e.target.id);
    if(!currentPlayer.selected){
        hasClicked = true;
    }

}


function checkPlayers(){
    
    if(players.every(x => x.selected === true)){
        for(let i = 0; i < players.length;++i){
            players[i].selected = false;
        }
    }            
}



function initDeck(){
   deck = [];
   cardsOnTable = [];
   selected = [];
   for(let i = 0; i < 27; ++i){
       deck.push(basicCardIndeces[i]);
   }
   shuffleDeck();
}


function shuffleDeck() {
    for (let i = 0; i < deck.length; i++){
        var change = i + Math.floor(Math.random() * (deck.length - i));
        var temp = deck[i];
        deck[i] = deck[change];
        deck[change] = temp;
    }
}


function handleCards() {
    var ctr = 0;
    for(let r = 0; r < 4 ; ++r){
        for(let c = 0; c < 3; ++c){
            var cardNum = deck.pop();
            var imgCard = 
            `<img visibility="visible" onClick="cardSelector(this, ${ctr})" src="cards/${cardNum}.png">`;
            cardTable.rows[r].cells[c].innerHTML = imgCard;
            cardTable.rows[r].cells[c].style.visibility='visible';
            cardsOnTable.push(cardNum);
            ctr++;
        }
    }
    lastRow = 4;
    cardCounter = ctr;
}

function plusThreeCards(){
    if (deck.length != 0 
      && lastRow < 7 ){
        var r = lastRow;
        for(let c = 0; c < 3; ++c){
            var cardNum = deck.pop();
            var imgCard =
            `<img visibility="visible" onClick="cardSelector(this, ${cardCounter})" src="cards/${cardNum}.png">`;
            cardTable.rows[r].cells[c].innerHTML = imgCard;
            cardTable.rows[r].cells[c].style.visibility = 'visible';
            cardsOnTable.push(cardNum);
            ++cardCounter;
        }
        ++lastRow;
        refreshDeckCount();
    }

}


plusThreeBtn.addEventListener('click',plusThreeCards);

function cardSelector(crd,idx){
   
     if (hasClicked && !isGameEnd){
        if(!tenSecIsOver){
            var currSelect = selected.indexOf(idx);
            
            if(currSelect >= 0){
                selected.splice(currSelect,1);
                crd.style.border = '1px black solid';
            } else if(currSelect < 0 && selected.length < 3) {
                selected.push(idx);
                crd.style.border = '4px yellow solid';
                if(selected.length == 3){
                 checkSelection();
                }
            }  
        }    
    }
    
}

window.cardSelector = cardSelector;

function refreshPoints(){
    for(let i = 0; i < players.length; ++i){
        var actualPoint = document.querySelector(`tr#player${i+1} > #points`);
        actualPoint.innerHTML =
        `<td id="points">${players[i].points}</td>`; 
        
    }

}


function checkSelection(){

    var selectedForSet = selected.map(x => cardsOnTable[x]);
    var details = getCardDetails(selectedForSet);

    if(isSet(details)){

        changeCards(selected);
        ++currentPlayer.points;
        refreshPoints();
    }else {
        for(let i = 0; i < selected.length;++i){
            var imgNum = selected[i];
            var card = document.querySelector(`#cell${imgNum} > img`);
            card.style.border = '1px black solid';
        }
        selected = []; 
        --currentPlayer.points;
        currentPlayer.selected = true;
        checkPlayers();
        checkLeft();
        refreshPoints();
    }
  hasClicked = (numberOfPlayers == 1)? true : false;
}


const sumList = list => list.reduce((a,b) => a + b);


function isSet(details){
    return sumList(details) == 0;
}


function getCardDetails(cards) {

    var num = [];
    var shape = [];
    var color = [];
    var fill = [];
    var details = [];

    for (var x in cards){
        num.push(getNum(cards[x]));
        shape.push(getShape(cards[x]));
        color.push(getColor(cards[x]));
        if (isAdvancedMode){
            shading.push(getFill(cards[x]));
        }
    }

    
    if(isAdvancedMode){
        details.push(sumList(fill) % 3);
     }

    return details = [
        sumList(num) % 3,
        sumList(shape) % 3,
        sumList(color) % 3,
    ];  
    
}


function changeCards(slct){
  if(deck.length > 0){
      for( let i = 0; i < slct.length; ++i){
          var newCard = deck.pop();
          var cellId = `cell${slct[i]}`;
          var imgCard = `<img onClick="cardSelector(this,${slct[i]})" src="cards/${newCard}.png">`;
          var cell = document.querySelector(`#${cellId}`);
          cardsOnTable.splice(slct[i], 1, newCard);
          cell.innerHTML = imgCard;

      }

  } else {
    for( let i = 0; i < slct.length; ++i){
        var cellId = `cell${slct[i]}`;
        var cell = document.querySelector(`#${cellId}`);
        cardsOnTable.splice(slct[i], 1, newCard);
       
    }
    for(var i = cardsOnTable.length - 1; i >= 0; i--){
        if(cardsOnTable[i] == undefined){
            cardsOnTable.splice(i, 1);
        }
    }
    for (var i = 0; i < 21 ; i++){
        var cell = document.querySelector(`#cell${i}`);
        if( i < cardsOnTable.length) {
            cell.innerHTML = 
            `<img onClick="cardSelector(this,${i})" src="cards/${cardsOnTable[i]}.png">`;
        } else {
            cell.style.visibility = 'hidden';
            
        }
    }
  }
  refreshDeckCount();
  selected = [];
  checkLeft();

}


function checkLeft(){
    var leftCard = [];
    leftCard.push(...cardsOnTable)
    if (deck.length === 0){
        if(countSets(leftCard) === 0){
            finalTime = timer.innerHTML;
            isGameEnd = true;
            alert('GAME ENDED!');
        }
    } else if(countSets(leftCard) === 0
      && (isRace || autoPlusThreeBox.checked)) {
          plusThreeCards();
      }


}

function countSets(pack){
var setCounter = 0;
for( let i = 0; i < pack.length; ++i){
    for(let j = i + 1; j < pack.length; ++j){
        for(let k = j + 1; k < pack.length; ++k){
            setCounter = (isSet(getCardDetails(
                [pack[i],pack[j],pack[k]])) ? setCounter + 1 : setCounter);
        }
    }
}
 
 return setCounter;
}



function refreshDeckCount() {
    deckCounter.innerHTML = deck.length; 
  }
  
  
function getShape(crd){

    return Math.floor((crd % 9) / 3);
}

function getNum(crd){
    return crd % 3;
}

function getColor(crd){
    return Math.floor(crd / 27);
}

function getFill(crd){
    return Math.floor((crd % 27) / 9);
}
  
function isThereSet(){
    var msg;
    if(countSets(cardsOnTable) != 0){
        msg = 'There is at least one set!';
    } else {
        msg = 'There is no set';
    }
    alert(msg);    
}

isSetBtn.addEventListener('click',isThereSet);

function showSet(){
    var nums = [];
    if(countSets(cardsOnTable) != 0){
        for( let i = 0; i < cardsOnTable.length; ++i){
            for(let j = i + 1; j < cardsOnTable.length; ++j){
                for(let k = j + 1; k < cardsOnTable.length; ++k){
                    if (isSet(getCardDetails(
                        [cardsOnTable[i],cardsOnTable[j],cardsOnTable[k]]))){
                            nums.push(i+1);
                            nums.push(j+1);
                            nums.push(k+1);
                            alert(`Card indexes:${nums[0]} ${nums[1]} ${nums[2]}`);
                            return;
                        }
                    }
            }
         } 
    }

}


whereIsSetBtn.addEventListener('click',showSet);
