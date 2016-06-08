package com.mygdx.game.logic;


import com.mygdx.game.PlayerAction;
import com.mygdx.game.managers.FlagManager;
import com.mygdx.game.managers.GameManager;
import components.enums.Card;
import components.enums.GameState;


public class AutoPlayer {
    PlayerAction playerAction;
    GameManager gameManager;
    FlagManager flagManager;

    public AutoPlayer(PlayerAction playerAction){
        this.playerAction=playerAction;
        gameManager=this.playerAction.gameManager;
        flagManager=this.playerAction.flagManager;
    }
    public void evolutionPhasePass(){
        if(gameManager.state== GameState.EVOLUTION)
        if(gameManager.turn==playerAction.player.number&&playerAction.player.cardsNumber()==0&&playerAction.player.animalsNumber()>0){
            gameManager.pass();
            flagManager.printSelectedAnimal=false;
            playerAction.chosenCard=99;
        }
    }
    public void feedingPhasePass(){
        if(gameManager.state== GameState.FEEDING)
            if(gameManager.turn==playerAction.player.number){
                boolean allAnimalsFeeded=true;
                boolean noPasturage=true;
                for(int i=0;i<5;i++){
                    if(playerAction.player.animals[i]!=null) {
                        if (!playerAction.player.animals[i].isFeeded()) {
                            allAnimalsFeeded = false;
                        }
                        if (playerAction.player.animals[i].have(Card.PASTURAGE)) {
                            noPasturage = false;
                        }
                    }
                }
                if(noPasturage && allAnimalsFeeded) {
                    flagManager.actionDone = false;
                    flagManager.passOrEndRound();
                    gameManager.pass();
                    playerAction.chosenCard = 99;
                }
            }
    }
}
