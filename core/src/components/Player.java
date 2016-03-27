package components;

import components.enums.Card;

import java.util.Vector;


public class Player {
    public int number;
    public String name;

    public int numberOfCards = 0;
    public Vector<Card> cards = new Vector<Card>();
    public Animal [] animals = new Animal[5];

    private int animalsNumber = 0;

    public void addCard(Card card){
        cards.addElement(card);
    }

    public void addAnimal(int slot){
        animals[slot] = new Animal(number);
        animalsNumber ++;
    }

    public void killAnimal(int slot){
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
