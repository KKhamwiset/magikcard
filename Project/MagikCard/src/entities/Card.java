package entities;

import entities.Player;
import entities.Monster;
import magikcard.ImageButton;
import magikcard.GameScreen;
import magikcard.GameState;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.awt.event.*;

public class Card {
    private String imagePath;
    private String cardName;
    private int cardIndex;
    private int maxCards;
    private boolean isFaceUp;
    private boolean isComparing = false;
    private ArrayList<String> cardsName;
    private ArrayList<Card> cards;
    private ArrayList<File> fileList;
    private String backCard = "back";
    private String folderPath = "..\\Assets\\Card";
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
        "water" };
    
    public Card(String imagePath, boolean isCard, int cardIndex, String cardName, ImageButton cardIcon) {
        this.imagePath = imagePath;
        this.isFaceUp = false;
        this.cardIndex = cardIndex;
        this.cardName = cardName;
    }

    public Card(String folderPath, int width, int height, GameScreen gameScreen) {
        this.cardsName = new ArrayList<>();
        this.cards = new ArrayList<>();
        this.maxCards = gameScreen.maximumCard();
        setArray(); 
        loadCards(folderPath, width, height, null);
    }

    private void loadCards(String folderPath, int cardWidth, int cardHeight, ActionListener listener) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        fileList = new ArrayList<>();
        if (files != null && fileList.size() == 0) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".jpg")) {
                    fileList.add(file);
                }
            }
            int id = 0;
            int addedCards = 0;
            while (addedCards < maxCards) {
                if (addedCards >= maxCards || fileList.size() == 0) {
                    addedCards = 0;
                    break;
                }
                int randomIndex = (int) (Math.random() * fileList.size());
                File selectedFile = fileList.get(randomIndex);
                String imagePath = selectedFile.getAbsolutePath();
                String cardName = selectedFile.getName().replace(".jpg", "");
                if (cardsName.contains(cardName)) {
                    ImageButton card1 = new ImageButton(getBackCardImagePath(folderPath), listener, cardWidth, cardHeight);
                    ImageButton card2 = new ImageButton(getBackCardImagePath(folderPath), listener, cardWidth, cardHeight);
                    cards.add(new Card(imagePath, true, id++, cardName, card1));
                    cards.add(new Card(imagePath, true, id++, cardName, card2));
                    addedCards += 2;
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
        cardIcon.setImage(getBackCardImagePath(folderPath), cardWidth, cardHeight);
    }
    public boolean isMatch(Card otherCard) {
        return this.getCardName().equals(otherCard.getCardName());
    }
    public void actionsBaseOnCard(String cardName, GameContext context) {
        Player player = context.getPlayer();
        Monster monster = context.getCurrentMonster();
        GameState gameResult = context.getCurrentGame().gameResult;
        switch (cardName) {
            case "darkness": 
                context.getPlayer().Attack(context.getCurrentMonster());
                break;
            case "fairy": 
                break;
            case "grass": 
                break;
            case "double": 
                break;
            default: 
                break;
        }
    }
    
 
    public void handleCardClick(ImageButton cardIcon, int width, int height, 
                               ArrayList<Card> flippedCards, ArrayList<ImageButton> flippedButtons, 
                               GameContext context) {
       if (flippedCards.size() >= 2 || flippedCards.contains(this) || this.isFaceUp()) {
           return;
       }

       this.setFaceUp(true);
       flippedCards.add(this);
       flippedButtons.add(cardIcon);
       cardIcon.setImage(getImagePath(), width, height);

       if (flippedCards.size() == 2) {
           compareFlippedCards(context, flippedCards, flippedButtons, width, height);
       }
 
   }

   private void compareFlippedCards(GameContext context, ArrayList<Card> flippedCards, 
                                    ArrayList<ImageButton> flippedButtons, int width, int height) {
      if (isComparing) return;
      isComparing = true;
      Timer timer = new Timer(200, e -> {
          Card firstCard = flippedCards.get(0);
          Card secondCard = flippedCards.get(1);
          if (firstCard.isMatch(secondCard)) {
              context.getPlayer().Attack(context.getCurrentMonster());
              context.getCurrentGame().setMatch(context.getCurrentGame().getcurrentMatch() + 1);
              checkGameWinCondition(context);
          } else {
              context.getCurrentMonster().Attack(context.getPlayer());
              flipCardsBack(firstCard, secondCard, flippedButtons, width, height);
          }
          flippedCards.clear();
          flippedButtons.clear();
          ((Timer) e.getSource()).stop();
          isComparing = false; 
      });
      timer.setRepeats(false);
      timer.start();
    }

   private void flipCardsBack(Card firstCard, Card secondCard, ArrayList<ImageButton> flippedButtons, int width, int height) {
       firstCard.setFaceUp(false);
       secondCard.setFaceUp(false);
       flippedButtons.get(0).setImage(firstCard.getBackCardImagePath(folderPath), width, height);
       flippedButtons.get(1).setImage(secondCard.getBackCardImagePath(folderPath), width, height);
    }

   private void checkGameWinCondition(GameContext context) {
       int maxPair = (int) (context.getCurrentGame().maximumCard() / 2.0);
       if (context.getCurrentGame().getcurrentMatch() == maxPair) {
           Timer timerRest = new Timer(100, e -> {
               context.getCurrentGame().setMatch(0);
               context.getCurrentGame().resetCardUI();
               ((Timer) e.getSource()).stop();
           });
           timerRest.setRepeats(false);
           timerRest.start();
       }
    }
}
