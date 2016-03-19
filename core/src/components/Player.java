package components;

import java.util.Vector;


public class Player {
    public int number;
    public String name;

    public int numberOfCards = 0;
    public Vector cards = new Vector();
    public Vector animals = new Vector();

    public void addCard(String card){
        cards.addElement(card);
    }

    public void addAnimal(){
        animals.addElement(new Animal(number));
    }

    public Object getCards(int i) {
        return cards.get(i);
    }

    public void removeCard(int i){
        cards.remove(i);
    }

    public int cardsNumber(){
        return cards.size();
    }
}
