package components.objects;

import components.enums.Card;
import components.objects.Animal;

import java.util.Vector;


public class Player {
    public int number;
    public String name;

    public boolean pass = false;

    public Player() {}
    public Player(int number,String name,int numberOfCards ){
        this.number = number;
        this.name = name;
        this.numberOfCards = numberOfCards;
    }
    public int numberOfCards = 0;
    public Vector<Card> cards = new Vector<Card>();
    public Animal[] animals = new Animal[5];

    private int animalsNumber = 0;

    public void addCard(Card card){
        cards.addElement(card);
    }

    public boolean addDoubleCard(Card card, int animalPlace){
        if(card==Card.COOPERATIONC || card==Card.COOPERATIONF){
            if(animals[animalPlace].coopWith[1]==null){
                animals[animalPlace].coopWith[1]=animalPlace+1;
                animals[animalPlace+1].coopWith[0]=animalPlace;
                return true;
            }else{
                return false;
            }
        }else if(card==Card.COMMUNICATION){
            if(animals[animalPlace].commWith[1]==null){
                animals[animalPlace].commWith[1]=animalPlace+1;
                animals[animalPlace+1].commWith[0]=animalPlace;
                return true;
            }else{
                return false;
            }
        }else if(card==Card.SYMBIOSIS){
            if(animals[animalPlace].symbiosis[1]==null){
                animals[animalPlace].symbiosis[1]=animalPlace+1;
                animals[animalPlace+1].symbiosis[0]=animalPlace;
                return true;
            }else{
                return false;
            }
        }else return false;
    }

    public void addAnimal(int slot){
        animals[slot] = new Animal(number, slot, this);
        animalsNumber ++;
    }

    public void killAnimal(int slot) {
        if (slot > 0) {
            if (animals[slot - 1] != null) {
                animals[slot - 1].commWith[1] = null;
                animals[slot - 1].coopWith[1] = null;
                animals[slot - 1].symbiosis[1] = null;
            }
        }
        if (slot < 5) {
            if (animals[slot + 1] != null) {
                animals[slot + 1].commWith[0] = null;
                animals[slot + 1].coopWith[0] = null;
                animals[slot + 1].symbiosis[0] = null;
            }
        }
        animals[slot]=null;
        animalsNumber --;
    }

    public Card getCards(int i) {
        return cards.get(i);
    }

    public void removeCard(int i){
        cards.remove(i);
    }

    public int animalsNumber() { return animalsNumber;}
    public int cardsNumber(){
        return cards.size();
    }
}
