package magikcard;

import javax.swing.Timer;
import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.awt.event.*;
import magikcard.ImageButton;

public class Card {
    private String imagePath;
    private int cardIndex;
    private String cardName;
    private ImageButton associatedButton;
    private ArrayList<String> cardsName;
    private ArrayList<Card> cards;
    private String[] temp = {
        "darkness",
        "double",
        "fairy",
        "fighting",
        "fire",
        "grass",
        "lighting",
        "metal",
        "psyhic",
        "water"
    };
    
    private String backCard = "back";
    private boolean isFaceUp;

    public Card(String imagePath, boolean isCard, int cardIndex, String cardName, ImageButton cardIcon) {
        this.imagePath = imagePath;
        this.isFaceUp = false;
        this.cardIndex = cardIndex;
        this.cardName = cardName;
    }

    public Card(String folderPath,int width,int height) {
        this.cardsName = new ArrayList<>();
        this.cards = new ArrayList<>();
        setArray(); 
        loadCards(folderPath,width,height,null);
    }
    private void loadCards(String folderPath, int cardWidth, int cardHeight, ActionListener listener) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        int index = 0;

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".jpg")) {
                    String imagePath = file.getAbsolutePath();
                    String cardName = file.getName().replace(".jpg", "");
                    if (cardsName.contains(cardName)) {
                        ImageButton card1 = new ImageButton(getBackCardImagePath(folderPath), listener, cardWidth, cardHeight);
                        ImageButton card2 = new ImageButton(getBackCardImagePath(folderPath), listener, cardWidth, cardHeight);
                        cards.add(new Card(imagePath, true, index++, cardName, card1));
                        cards.add(new Card(imagePath, true, index++, cardName, card2));
                    }
                }
            }
        }
    }
    private void setArray() {
        cardsName.clear();
        Collections.addAll(cardsName, temp);
    }
    public void shuffleCards() {
        Collections.shuffle(cards);
    }

    public ArrayList<Card> getShuffledCards() {
        return cards;
    }

    public String getImagePath() {
        return imagePath; 
    }

    public String getBackCardImagePath(String folderPath) {
        return folderPath + File.separator + backCard + ".jpg";
    }

    public boolean isFaceUp() {
        return isFaceUp;
    }

    public void setFaceUp(boolean faceUp) {
        this.isFaceUp = faceUp;
    }

    public int getCardIndex() {
        return cardIndex;
    }

    public String getCardName() {
        return cardName;
    }
    public void flipBack(ImageButton cardIcon,String folderPath,int cardWidth,int cardHeight) { 
        System.out.println("Fliped : " + cardIcon);
        cardIcon.setImage(getBackCardImagePath(folderPath), cardWidth, cardHeight);
    }
    public boolean isMatch(Card otherCard) {
        return this.getCardName() == otherCard.getCardName();
    }
    public void handleCardClick(ImageButton cardIcon, int width, int height, ArrayList<Card> flippedCards, ArrayList<ImageButton> flippedButtons) {
        if (flippedCards.size() >= 2 || flippedCards.contains(this)) {
            return;
        }
        flippedButtons.add(cardIcon);
        flippedCards.add(this);
        cardIcon.setImage(getImagePath(), width, height);
        String folderPath = "..\\Assets\\card";
        if (flippedCards.size() == 2) {
            Timer timer = new Timer(250, e -> {
                Card firstCard = flippedCards.get(0);
                Card secondCard = flippedCards.get(1);
                ImageButton firstButton = flippedButtons.get(0);
                ImageButton secondButton = flippedButtons.get(1);
                if (firstCard.isMatch(secondCard)) {
                    System.out.println("It's a match!");
                } else {
                    firstButton.setImage(firstCard.getBackCardImagePath(folderPath), width, height);
                    secondButton.setImage(secondCard.getBackCardImagePath(folderPath), width, height);
                }
                flippedCards.clear();
                flippedButtons.clear();
            });
            timer.setRepeats(false);
            timer.start();
        }
    }
}
