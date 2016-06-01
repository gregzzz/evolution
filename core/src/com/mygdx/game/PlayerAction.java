package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.logic.Mouse;
import components.enums.Card;
import com.mygdx.game.managers.*;
import components.enums.GameState;
import components.objects.Animal;
import components.objects.Player;

import java.util.Random;


public class PlayerAction {
    public FlagManager flagManager;
    public GameManager gameManager;
    public ButtonManager buttonManager;
    private Mouse mouse;
    public Player player;
    public Player otherPlayer;

    // zmienne do do funkcji z uzyciem kart i wybranego zwierzęcia
    public int chosenCard=99;
    public int selectedAnimal=99;


    public PlayerAction(FlagManager flagManager, GameManager gameManager, Mouse mouse, ButtonManager buttonManager){
        this.flagManager=flagManager;
        this.gameManager=gameManager;
        this.mouse=mouse;
        this.buttonManager=buttonManager;
        this.player=gameManager.player;
    }


    //guzik do wracania do menu pod koniec gry
    public void returnToMenu(){
        if(buttonManager.cancelButton.isTouched(mouse)){
            gameManager.state=GameState.BEGIN;
            flagManager.lookingForGames=false;
            flagManager.chooseMainMenuOption=false;
            gameManager.disconnect();
        }
    }

    //wybierz zwierze podczas fazy zywienia
    public void chooseAnimalForAction(){
        if(!flagManager.chooseAnimalForAction){
            //wybor zwierzecia
            for(int i=0;i<4;i++) {
                for (int j = 0; j < 5; j++) {
                    if (buttonManager.animalButtons[i][j] != null && buttonManager.animalButtons[i][j].isTouched(mouse)) {
                        if (i > 0) {
                            otherPlayer = gameManager.otherPlayers.elementAt(i - 1);
                            selectedAnimal = j;
                            flagManager.printSelectedAnimal=true;
                            flagManager.printFeedingChoices = false;
                            flagManager.chooseAnimalAction = true;
                        } else if(player.animals[j].symbiosis[0]==null || player.animals[j-1].isFeeded()) {
                            flagManager.printSelectedAnimal=true;
                            if(gameManager.turn==player.number) {
                                flagManager.printFeedingChoices = true;
                                flagManager.chooseAnimalAction = false;
                            }
                            selectedAnimal = j;
                            otherPlayer=player;
                        }
                    }
                }
            }
            //opis karty
            for (int i = 0; i < player.cardsNumber(); i++) {
                if (buttonManager.cardButtons[i].isTouched(mouse)) {
                    chosenCard = i;
                    flagManager.chooseCard();
                    flagManager.printFeedingChoices=false;
                    flagManager.chooseAnimalAction=true;
                }
            }
            if(buttonManager.pass.isTouched(mouse)&&gameManager.turn==player.number){
                boolean allAnimalsFeeded=true;
                for(int i=0;i<5;i++){
                    if(player.animals[i]!=null && !player.animals[i].isFeeded()){
                        allAnimalsFeeded=false;
                    }
                }
                if(gameManager.amountOfFood==0 || allAnimalsFeeded) {
                    flagManager.actionDone = false;
                    flagManager.passOrEndRound();
                    gameManager.pass();
                    chosenCard = 99;
                }
            }
            if(buttonManager.endRound.isTouched(mouse)&&gameManager.turn==player.number){
                boolean allAnimalsFeeded=true;
                for(int i=0;i<5;i++){
                    if(player.animals[i]!=null && !player.animals[i].isFeeded()){
                        allAnimalsFeeded=false;
                    }
                }
                if(gameManager.amountOfFood==0 || allAnimalsFeeded || flagManager.actionDone) {
                    gameManager.endRound();
                    flagManager.passOrEndRound();
                    flagManager.actionDone=false;
                    chosenCard=99;
                }
            }
        }
    }

    public void useCommunication(){
        if(!flagManager.useCommunication) {
            Animal boostedAnimal;
            flagManager.askComm = true;
            if (buttonManager.yes.isTouched(mouse)&&player.animals[selectedAnimal].commWith[0]!=null&&!player.animals[selectedAnimal].commUsed[0]) {
                boostedAnimal=player.animals[selectedAnimal-1];
                if (!boostedAnimal.isFeeded() || boostedAnimal.fat < boostedAnimal.fatTotal && (player.animals[selectedAnimal].symbiosis[0]==null || player.animals[selectedAnimal-1].isFeeded())) {
                    gameManager.amountOfFood--;
                    boostedAnimal.feed(1);
                    gameManager.feed(selectedAnimal - 1, 1);
                    player.animals[selectedAnimal].commUsed[0]=true;
                    player.animals[selectedAnimal-1].commUsed[1]=true;
                    flagManager.communicationDone();
                }
            } else if (buttonManager.no.isTouched(mouse)&&player.animals[selectedAnimal].commWith[1]!=null&&!player.animals[selectedAnimal].commUsed[0]) {
                boostedAnimal=player.animals[selectedAnimal+1];
                if (!boostedAnimal.isFeeded() || boostedAnimal.fat < boostedAnimal.fatTotal && (player.animals[selectedAnimal].symbiosis[0]==null || player.animals[selectedAnimal-1].isFeeded())) {
                    gameManager.amountOfFood--;
                    boostedAnimal.feed(1);
                    gameManager.feed(selectedAnimal + 1, 1);
                    player.animals[selectedAnimal].commUsed[1]=true;
                    player.animals[selectedAnimal+1].commUsed[0]=true;
                    flagManager.communicationDone();
                }
            } else if (buttonManager.cancelButton.isTouched(mouse)) {
                flagManager.communicationDone();
            }
        }
    }

    public void useCooperation() {
        if(!flagManager.useCooperation) {
            Animal boostedAnimal;
            flagManager.askCoop = true;
            if (buttonManager.yes.isTouched(mouse)&&player.animals[selectedAnimal].coopWith[0]!=null&&!player.animals[selectedAnimal].coopUsed[0]) {
                boostedAnimal=player.animals[selectedAnimal-1];
                if (!boostedAnimal.isFeeded() || boostedAnimal.fat < boostedAnimal.fatTotal) {
                    boostedAnimal.feed(1);
                    gameManager.feed(selectedAnimal - 1, 1);
                    player.animals[selectedAnimal].coopUsed[0]=true;
                    player.animals[selectedAnimal-1].coopUsed[1]=true;
                    flagManager.cooperationDone();
                }
            } else if (buttonManager.no.isTouched(mouse)&&player.animals[selectedAnimal].coopWith[1]!=null&&!player.animals[selectedAnimal].coopUsed[1]) {
                boostedAnimal=player.animals[selectedAnimal+1];
                if (!boostedAnimal.isFeeded() || boostedAnimal.fat < boostedAnimal.fatTotal) {
                    boostedAnimal.feed(1);
                    gameManager.feed(selectedAnimal + 1, 1);
                    player.animals[selectedAnimal].coopUsed[1]=true;
                    player.animals[selectedAnimal+1].coopUsed[0]=true;
                    flagManager.cooperationDone();
                }
            } else if (buttonManager.cancelButton.isTouched(mouse)) {
                flagManager.cooperationDone();
            }
        }
    }

    //wybierz co robisz zwirzeciem podczas fazy zywienia
    public void chooseAnimalAction(){
        if(!flagManager.chooseAnimalAction){
            //jedz
            if(buttonManager.feedChoices[0].isTouched(mouse) && !flagManager.actionDone && gameManager.amountOfFood>0){
                if(!player.animals[selectedAnimal].isFeeded() || player.animals[selectedAnimal].fat<player.animals[selectedAnimal].fatTotal) {
                    gameManager.amountOfFood--;
                    player.animals[selectedAnimal].feed(1);
                    gameManager.feed(selectedAnimal, 1);
                    flagManager.actionDone = true;
                    player.animals[selectedAnimal].realFoodRecieved=true;
                }
                //drapieznik
            }else if(buttonManager.feedChoices[1].isTouched(mouse) && player.animals[selectedAnimal].carnivore&&!player.animals[selectedAnimal].isFeeded() && !flagManager.actionDone){
                flagManager.chooseTarget=false;
                flagManager.chooseTarget();
                //piractwo
            }else if(buttonManager.feedChoices[2].isTouched(mouse) && player.animals[selectedAnimal].have(Card.PIRACY)&&!player.animals[selectedAnimal].isFeeded()&& !player.animals[selectedAnimal].piracy){
                flagManager.choosePiracyTarget=false;
                flagManager.chooseTarget();
                //wypas
            }else if(buttonManager.feedChoices[3].isTouched(mouse) && player.animals[selectedAnimal].have(Card.PASTURAGE)&&gameManager.amountOfFood>0&& !player.animals[selectedAnimal].pasturage){
                gameManager.amountOfFood--;
                //updatuje u innych ilosc zarcia
                gameManager.feed(selectedAnimal,0);
                player.animals[selectedAnimal].pasturage=true;
                //hibernacja
            }else if(buttonManager.feedChoices[4].isTouched(mouse) && player.animals[selectedAnimal].have(Card.HIBERNATION)&& !player.animals[selectedAnimal].hibernation){
                player.animals[selectedAnimal].hibernation=true;
                player.animals[selectedAnimal].hibernationUsed=true;
                player.animals[selectedAnimal].feed(player.animals[selectedAnimal].foodNeeded-player.animals[selectedAnimal].food);
                gameManager.feed(selectedAnimal,player.animals[selectedAnimal].foodNeeded-player.animals[selectedAnimal].food);
                //padlinozerca
            }else if(buttonManager.feedChoices[5].isTouched(mouse) && player.animals[selectedAnimal].have(Card.SCAVENGER)&& !player.animals[selectedAnimal].scavenger && gameManager.corpse){
                player.animals[selectedAnimal].scavenger=true;
                player.animals[selectedAnimal].feed(1);
                gameManager.feed(selectedAnimal,1);
                gameManager.corpse=false;
                gameManager.scavenge();
                //kooperacja
            }else if(buttonManager.feedChoices[6].isTouched(mouse) && (player.animals[selectedAnimal].coopWith[0]!=null ||  player.animals[selectedAnimal].coopWith[1]!=null) && player.animals[selectedAnimal].foodRecieved && (!player.animals[selectedAnimal].coopUsed[0]||!player.animals[selectedAnimal].coopUsed[1])){
                flagManager.chooseTarget();
                flagManager.useCooperation=false;
                //komunikacja
            }else if(buttonManager.feedChoices[7].isTouched(mouse) && (player.animals[selectedAnimal].commWith[0]!=null ||  player.animals[selectedAnimal].commWith[1]!=null) && player.animals[selectedAnimal].realFoodRecieved && (!player.animals[selectedAnimal].commUsed[0]||!player.animals[selectedAnimal].commUsed[1]) && gameManager.amountOfFood>0){
                flagManager.chooseTarget();
                flagManager.useCommunication=false;
            }
        }
    }

    //zwraca true jesli zwierze chronione przez mimikre
    private boolean protectedByMimicry(Player otherPlayer, int atttacker, int defender){
        int availableTargetsWithoutMimicry=0;
        for(int i=0; i<5; i++){
            if(otherPlayer.animals[i]!=null && otherPlayer.animals[i].canBeAttacked(player.animals[atttacker]) && !otherPlayer.animals[i].have(Card.MIMICRY)){
                availableTargetsWithoutMimicry++;
            }
        }
        if(availableTargetsWithoutMimicry>0 && otherPlayer.animals[defender].have(Card.MIMICRY)){
            return true;
        }else{
            return false;
        }
    }

    //wybierz zwierze do ataku i zaatakuj
    public void chooseTarget(){
        Player otherPlayer;
        if(!flagManager.chooseTarget){
            for(int i=0;i<4;i++) {
                for (int j = 0; j < 5; j++) {
                    if (buttonManager.animalButtons[i][j] != null && buttonManager.animalButtons[i][j].isTouched(mouse)) {
                        if(i>0) {
                            otherPlayer = gameManager.otherPlayers.elementAt(i - 1);
                        }else{
                            otherPlayer=player;
                        }
                        if(otherPlayer.animals[j]!=player.animals[selectedAnimal] && otherPlayer.animals[j].canBeAttacked(player.animals[selectedAnimal]) && !protectedByMimicry(otherPlayer, selectedAnimal, j)){
                            int attackType=player.animals[selectedAnimal].attack(otherPlayer.animals[j]);
                            if(attackType==1 || attackType==3){
                                if(attackType==3){
                                    gameManager.poison(selectedAnimal);
                                }
                                player.animals[selectedAnimal].feed(2);
                                gameManager.feed(selectedAnimal,2);
                                otherPlayer.killAnimal(j);
                                gameManager.kill(otherPlayer.number,j);
                                gameManager.corpse=true;
                                buttonManager.updateAnimalButtons();
                                flagManager.actionDone=true;
                            }else if(attackType==2){
                                Random generator = new Random();
                                int randInt=generator.nextInt(otherPlayer.animals[j].features.size());
                                otherPlayer.animals[j].removeFeature(randInt);
                                gameManager.tailToss(otherPlayer.number, j, randInt);
                                player.animals[selectedAnimal].feed(1);
                                gameManager.feed(selectedAnimal,1);
                                flagManager.actionDone=true;
                            }else{
                                flagManager.actionDone=true;
                            }
                            flagManager.targetChosen();
                        }
                    }
                }
            }
            if(buttonManager.cancelButton.isTouched(mouse)) {
                flagManager.targetChosen();
            }
        }
    }

    //kogo piracic
    public void choosePiracyTarget(){
        Player otherPlayer;
        if(!flagManager.choosePiracyTarget){
            for(int i=0;i<4;i++) {
                for (int j = 0; j < 5; j++) {
                    if (buttonManager.animalButtons[i][j] != null && buttonManager.animalButtons[i][j].isTouched(mouse)) {
                        if(i>0) {
                            otherPlayer = gameManager.otherPlayers.elementAt(i - 1);
                        }else{
                            otherPlayer=player;
                        }
                        if(otherPlayer.animals[j]!=player.animals[selectedAnimal] && !otherPlayer.animals[j].isFeeded() && otherPlayer.animals[j].food>0){
                            player.animals[selectedAnimal].feed(1);
                            gameManager.feed(selectedAnimal,1);
                            otherPlayer.animals[j].feed(-1);
                            gameManager.steal(otherPlayer.number,j);
                            flagManager.piracyTargetChosen();
                            player.animals[selectedAnimal].piracy=true;
                        }else{
                            flagManager.piracyTargetChosen();
                        }
                    }
                }
            }
            if(buttonManager.cancelButton.isTouched(mouse)) {
                flagManager.piracyTargetChosen();
            }
        }
    }

    //wybierz gdzie chcesz postawic nowe zwierze
    public void chooseAnimalPlace(){
        if(!flagManager.chooseAnimalPlace) {
            for (int i = 0; i < 5; i++) {
                if (buttonManager.animalPlaces[i].isTouched(mouse) && player.animals[i]==null) {
                    //akcja guzika add animal
                    player.addAnimal(i);
                    player.removeCard(chosenCard);
                    gameManager.addAnimal(i);
                    buttonManager.addAnimalButton(i);
                    flagManager.chooseAnimalPlace();
                }
            }
            if(buttonManager.cancelButton.isTouched(mouse)) {
                flagManager.chooseAnimalPlace();
            }
        }
    }

    //wybierz zwierze ktoremu chcesz dodac ceche
    public void chooseMyAnimal(){
        if(!flagManager.chooseMyAnimal) {
            Player otherPlayer;
            for (int j = 0; j < 4; j++) {
                for (int i = 0; i < 5; i++) {
                    if (buttonManager.animalButtons[j][i] != null && buttonManager.animalButtons[j][i].isTouched(mouse)) {
                        if(j>0) {
                            otherPlayer = gameManager.otherPlayers.elementAt(j - 1);
                        }else{
                            otherPlayer=player;
                        }
                        Card addedCard=player.getCards(chosenCard);
                        //czy zwykla cecha
                        if (!flagManager.secondaryPerk) {
                            //czy pasozyt do wroga lub czy zwykla dla siebie
                            if((j==0 && addedCard!=Card.PARASITEF &&addedCard!=Card.PARASITEC) || (j>0 && (addedCard==Card.PARASITEF || addedCard==Card.PARASITEC))) {
                                //czy juz ma taka ceche
                                if (addedCard == Card.PARASITEF || addedCard == Card.PARASITEC || !otherPlayer.animals[i].have(addedCard)) {
                                    //czy to nie sa duze masy ciala tylko z inna druga cecha
                                    if(!(player.getCards(chosenCard)==Card.MASSIVEC&&otherPlayer.animals[i].have(Card.MASSIVEF)||addedCard==Card.MASSIVEF&&otherPlayer.animals[i].have(Card.MASSIVEC))) {
                                        //czy to nie cecha podwojna
                                        if(addedCard!=Card.COMMUNICATION&&addedCard!=Card.COOPERATIONC&&addedCard!=Card.COOPERATIONF&&addedCard!=Card.SYMBIOSIS) {
                                            otherPlayer.animals[i].addFeature(addedCard);
                                            gameManager.addFeature(otherPlayer.number, i, addedCard);
                                            player.removeCard(chosenCard);
                                        }else if(i<5 && otherPlayer.animals[i+1]!=null){
                                            //podwojne
                                            if(otherPlayer.addDoubleCard(addedCard,i)){
                                                player.removeCard(chosenCard);
                                                gameManager.addDouble(i,addedCard);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            //drapieznik
                            if (addedCard == Card.MASSIVEC || addedCard == Card.PARASITEC || addedCard == Card.COOPERATIONC || addedCard == Card.COMMUNICATION || addedCard == Card.TOXIC || addedCard == Card.HIBERNATION) {
                                if (!player.animals[i].carnivore && j==0) {
                                    player.animals[i].addFeature(Card.CARNIVORE);
                                    gameManager.addFeature(player.number, i, Card.CARNIVORE);
                                    player.removeCard(chosenCard);
                                }
                                //tkaneczka
                            } else if (addedCard == Card.MASSIVEF || addedCard == Card.PARASITEF || addedCard == Card.COOPERATIONF || addedCard == Card.CAMOUFLAGE || addedCard == Card.ROAR || addedCard == Card.PASTURAGE || addedCard == Card.SHARPSIGHT) {
                                if(j==0) {
                                    player.animals[i].addFeature(Card.FAT);
                                    gameManager.addFeature(player.number, i, Card.FAT);
                                    player.removeCard(chosenCard);
                                }
                            }
                        }
                        flagManager.chooseMyAnimal();
                    }
                }
            }
            if(buttonManager.cancelButton.isTouched(mouse)) {
                flagManager.chooseMyAnimal();
            }
        }
    }

    //wybierz co chcesz zrobic z kartą
    public void chooseAction(){
        if(chosenCard!=99 && !flagManager.chooseAction) {
            if (buttonManager.cardChoices[0].isTouched(mouse) && player.animalsNumber()<5) {
                flagManager.addAnimal();
            }
            else if (buttonManager.cardChoices[1].isTouched(mouse) && player.animalsNumber()>0){
                flagManager.secondaryPerk=false;
                flagManager.addPerk();
            }
            else if (buttonManager.cardChoices[2].isTouched(mouse) && player.animalsNumber()>0){
                flagManager.secondaryPerk=true;
                flagManager.addPerk();
            }
        }

    }

    //wybierz karte z łapy albo spasuj albo zwierze
    public void chooseCardFromHand(){
        //wybor karty
        if(!flagManager.chooseCardFromHand) {
            for (int i = 0; i < player.cardsNumber(); i++) {
                if (buttonManager.cardButtons[i].isTouched(mouse)) {
                    chosenCard = i;
                    flagManager.chooseCard();
                    //jesli twoja tura to mozemy cos z ta karta zrobic
                    if(gameManager.turn==player.number){
                        flagManager.chooseAction = false;
                    }
                }
            }
            if(gameManager.turn==player.number&&buttonManager.pass.isTouched(mouse)&&player.animalsNumber()>0){
                gameManager.pass();
                flagManager.printSelectedAnimal=false;
                chosenCard=99;
            }
            for(int i=0;i<4;i++) {
                for (int j = 0; j < 5; j++) {
                    if (buttonManager.animalButtons[i][j] != null && buttonManager.animalButtons[i][j].isTouched(mouse)) {
                        if (i > 0) {
                            otherPlayer = gameManager.otherPlayers.elementAt(i - 1);
                            selectedAnimal = j;
                            flagManager.printSelectedAnimal=true;
                        } else{
                            flagManager.printSelectedAnimal=true;
                            selectedAnimal = j;
                            otherPlayer=player;
                        }
                    }
                }
            }
        }
    }
    //wybierz opcje z glownego menu
    public void chooseMainMenuOption(){
        if(!flagManager.chooseMainMenuOption) {
            if(buttonManager.menuButtons[0].isTouched(mouse)){
                flagManager.chooseMainMenuOption=true;
                flagManager.login=true;
                gameManager.state= GameState.LOGIN;
            }
            if(buttonManager.menuButtons[1].isTouched(mouse)){

            }
            if(buttonManager.menuButtons[2].isTouched(mouse)){
                if(gameManager.playerName!=null) {
                    flagManager.login=false;
                    flagManager.password=false;
                    flagManager.chooseMainMenuOption=true;
                    gameManager.startClient();
                    flagManager.lookingForGames=true;
                    gameManager.saveConfiguration();
                }
            }
            if(buttonManager.menuButtons[3].isTouched(mouse)){
                flagManager.chooseMainMenuOption=true;
                flagManager.server=true;
                gameManager.state= GameState.SERVER;
            }
            if(buttonManager.menuButtons[4].isTouched(mouse)){

            }
            if(buttonManager.menuButtons[5].isTouched(mouse)){

            }
        }
    }

    public void enterMessage(){
        if(buttonManager.chat.isTouched(mouse)){
            flagManager.chatboxPressed=true;
        }
        if(buttonManager.chatMessage.isTouched(mouse)){
            gameManager.chatMessageDelivered=false;
        }
    }


}
