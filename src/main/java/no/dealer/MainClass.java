package no.dealer;

import no.dealer.utils.ReadFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MainClass {
    public static void main(String[] args) throws IOException {
        String filePath = ReadFile.getFilePathFromConsole();
        String[] deckStr;
        List<String> deck;

        if ("".equals(filePath.trim())){
            deck = new Deck().generateShuffledDeck();
        }else {
            deckStr = ReadFile.readFileFromCode(filePath).split(",");
            deck = Arrays.stream(deckStr)
                    .map(String::trim)
                    .collect(Collectors.toList());
        }

        play(deck);
    }

    private static void play(List<String> deck) {
        Player sam = new Player("Sam");
        Player dealer = new Player("Dealer");
        List<Player> playerList = new ArrayList<>();
        playerList.add(sam);
        playerList.add(dealer);

        dealsCard(deck, playerList , 2);

        determineScore(playerList);

        Optional<Player> winnerPlayer = checkForWinner(playerList);
        if(winnerPlayer.isPresent()){
            System.out.println("Winner is " + winnerPlayer.get().getPlayerName() +  " with score of " + winnerPlayer.get().getScore());
            return;
        }

        if ("lost".equalsIgnoreCase(pickUntil(deck,sam, 17))){
            printInfo(dealer, sam);
            return;
        }

        if("lost".equalsIgnoreCase(pickUntil(deck,dealer, sam.getScore() + 1 ))){
            printInfo(sam , dealer);
            return;
        }


        if (sam.getScore() > dealer.getScore()){
            printInfo(sam, dealer);
        }else if(sam.getScore() < dealer.getScore()){
            printInfo(dealer, sam);
        }else{
            System.out.println("Both equal!");
        }


    }

    public static void printInfo(Player winner, Player looser){
        System.out.println( winner.getPlayerName() + " has won the game!");

        printPlayerCards(winner);
        System.out.println();
        printPlayerCards(looser);
    }

    public static void printPlayerCards(Player player){
        System.out.print( player.getPlayerName() + " cards : " );
        player.getCards().forEach(x -> System.out.print(x + " ,"));
    }

    public static String pickUntil(List<String> deck, Player player, int until) {
        while (player.getScore() < until ) {
            playerPickCard(deck, player);
            determineScorePlayer(player);
        }
        if (player.getScore() > 21){
            return "lost";
        }
        return "continue";
    }

    public static Optional<Player> checkForWinner(List<Player> playerList) {
        Optional<Player> backJakcPlayer = checkBlackJack(playerList);
        if (backJakcPlayer.isPresent()){
            return backJakcPlayer;
        }

        Optional<Player> player22 = check22(playerList);
        if (player22.isPresent()){
            return player22;
        }

        return Optional.empty();
    }

    public static Optional<Player> check22(List<Player> playerList) {
        List<Player> players22 = playerList.stream()
                .filter(x -> x.getScore() == 22)
                .collect(Collectors.toList());

        if (players22.size() == 2){
            return playerList.stream()
                    .filter(x -> x.getPlayerName().equalsIgnoreCase("Dealer"))
                    .findFirst();
        }
        return Optional.empty();
    }

    public static Optional<Player> checkBlackJack(List<Player> playerList) {
        List<Player> blackJackList = playerList.stream()
                .filter(x -> x.getScore() == 21)
                .collect(Collectors.toList());

        if (blackJackList.size() == 0) {
            return Optional.empty();

        }else if(blackJackList.size() == 1){
            return Optional.of(blackJackList.get(0));

        }else if (blackJackList.size() > 1){
            return blackJackList.stream().filter(x -> x.getPlayerName().equalsIgnoreCase("Sam"))
                    .findFirst();
        }
        return Optional.empty();
    }

    public static void determineScore(List<Player> playerList) {
        playerList.forEach(MainClass::determineScorePlayer);
    }

    private static void determineScorePlayer(Player player) throws IllegalArgumentException{
            int playerSum = player.getCards().stream()
                    .mapToInt(x -> getIntValue(x.substring(1))).sum();
            player.setScore(playerSum);
    }

    public static void dealsCard(List<String> deck, List<Player> playerList, int numberOfRound) {
        for(int i = 1; i <= numberOfRound; i++){

            playerList.forEach(player -> {
                playerPickCard(deck, player);
            });

        }
    }

    private static void playerPickCard(List<String> deck, Player player){
        if (!deck.isEmpty()) {
            String dealtCard = deck.remove(0);
            player.getCards().add(dealtCard);
        }
    }

    private static int getIntValue(String value) {
        if ("A".equalsIgnoreCase(value))
            return 11;
        if ("J".equalsIgnoreCase(value) || "Q".equalsIgnoreCase(value) || "K".equalsIgnoreCase(value))
            return 10;
        char c = value.charAt(0);
        if(!Character.isDigit(c))
            throw new IllegalArgumentException("Wrong char : " + value);
        return Integer.valueOf(value);
    }
}
