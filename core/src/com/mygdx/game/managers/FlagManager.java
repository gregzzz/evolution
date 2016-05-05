package com.mygdx.game.managers;

public class FlagManager {

    public boolean chooseCardFromHand;
    public boolean chooseAction;
    public boolean chooseAnimalPlace;
    public boolean chooseMyAnimal;
    public boolean chooseAnimalForAction;
    public boolean chooseAnimalAction;
    public boolean chooseTarget;
    public boolean choosePiracyTarget;

    public boolean printAnimalsSlots;
    public boolean printChoosenCard;
    public boolean printSelectedAnimal;
    public boolean printFeedingChoices;
    public boolean printCancelButton;

    public FlagManager(){
        printAnimalsSlots=false;
        printChoosenCard=false;
        printSelectedAnimal=false;
        printFeedingChoices=false;
        printCancelButton=false;

        chooseCardFromHand=false;
        chooseAction=true;
        chooseAnimalPlace=true;
        chooseMyAnimal=true;
        chooseAnimalForAction=false;
        chooseAnimalAction=true;
        chooseTarget=true;
        choosePiracyTarget=true;
    }

    public void passOrEndRound(){
        printSelectedAnimal = false;
        printFeedingChoices = false;
    }

    public void chooseAnimalForAction(){
        printSelectedAnimal=true;
        printFeedingChoices=true;
        chooseAnimalAction=false;
    }

    public void chooseTarget(){
        chooseAnimalForAction=true;
        chooseAnimalAction=true;
        printFeedingChoices=false;
        printCancelButton=true;
    }

    public void targetChosen(){
        chooseTarget=true;
        printSelectedAnimal=false;
        chooseAnimalForAction=false;
        printCancelButton=false;
    }
    public void piracyTargetChosen(){
        choosePiracyTarget=true;
        printSelectedAnimal=false;
        chooseAnimalForAction=false;
        printCancelButton=false;
    }

    public void chooseAnimalPlace(){
        chooseAction=true;
        chooseAnimalPlace=true;
        chooseCardFromHand=false;
        printAnimalsSlots = false;
    }

    public void chooseMyAnimal(){
        chooseCardFromHand = false;
        chooseMyAnimal = true;
        printCancelButton = false;
    }

    public void addPerk(){
        chooseAction=true;
        chooseCardFromHand=true;
        printChoosenCard=false;
        chooseMyAnimal=false;
        printCancelButton=true;
    }

    public void addAnimal(){
        chooseCardFromHand=true;
        chooseAnimalPlace=false;
        printChoosenCard = false;
        printAnimalsSlots = true;
        chooseAction=true;
    }

    public void chooseCard(){
        chooseAction = false;
        printChoosenCard = true;
        printSelectedAnimal=false;
    }
}
