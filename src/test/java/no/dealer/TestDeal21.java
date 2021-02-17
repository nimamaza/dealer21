package no.dealer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.fail;

public class TestDeal21 {

    Player sam;
    Player dealer;
    List<Player> playerList;
    List<String> deck;

    @BeforeEach
    public void before() throws IOException {
        sam = new Player("Sam");
        dealer = new Player("Dealer");
        playerList = new ArrayList<>();
        playerList.add(sam);
        playerList.add(dealer);
        deck = Arrays.stream(("CA, D5, H9, HQ, S8").split(","))
                .map(String::trim).collect(Collectors.toList());
    }

    @Test
    public void testDetermineScore(){
        MainClass.dealsCard(deck, playerList,2);
        MainClass.determineScore(playerList);
        Assertions.assertThat(sam.getScore()).isEqualTo(20);
        Assertions.assertThat(dealer.getScore()).isEqualTo(15);

    }

    @Test
    public void testDetermineScore_ChangeD6(){
        deck = Arrays.stream(("CA, D6, H9, HQ, S8").split(","))
                .map(String::trim).collect(Collectors.toList());
        MainClass.dealsCard(deck, playerList,2);
        MainClass.determineScore(playerList);
        Assertions.assertThat(sam.getScore()).isEqualTo(20);
        Assertions.assertThat(dealer.getScore()).isEqualTo(16);

    }

    @Test
    public void testDetermineScore_OneTimeDeals(){
        deck = Arrays.stream(("CA, D5, H9, HQ, S8").split(","))
                .map(String::trim).collect(Collectors.toList());
        MainClass.dealsCard(deck, playerList,1);
        MainClass.determineScore(playerList);
        Assertions.assertThat(sam.getScore()).isEqualTo(11);
        Assertions.assertThat(dealer.getScore()).isEqualTo(5);

    }

    @Test
    public void testDetermineScore_DoubleLengthDeck(){
        deck = Arrays.stream(("CA, D5, H9, HQ, S8, CA, D5, H9, HQ, S8").split(","))
                .map(String::trim).collect(Collectors.toList());
        MainClass.dealsCard(deck, playerList,3);
        MainClass.determineScore(playerList);
        Assertions.assertThat(sam.getScore()).isEqualTo(28);
        Assertions.assertThat(dealer.getScore()).isEqualTo(26);

    }

    @Test
    public void testDetermineScore_addExtraCard(){
        MainClass.dealsCard(deck, playerList,2);
        List<String> samCards = playerList.get(0).getCards();
        samCards.add("C2");
        MainClass.determineScore(playerList);
        Assertions.assertThat(sam.getScore()).isEqualTo(22);
        Assertions.assertThat(dealer.getScore()).isEqualTo(15);

    }

    @Test
    public void testDetermineScore_CaseSensitive(){
        deck = Arrays.stream(("Ca, D5, H9, Hq, S8").split(","))
                .map(String::trim).collect(Collectors.toList());
        MainClass.dealsCard(deck, playerList,2);
        MainClass.determineScore(playerList);
        Assertions.assertThat(sam.getScore()).isEqualTo(20);
        Assertions.assertThat(dealer.getScore()).isEqualTo(15);
    }

    @Test
    public void testDetermineScore_WrongChar(){
        deck = Arrays.stream(("Ca, D5, H9, H(, S8").split(","))
                .map(String::trim).collect(Collectors.toList());
        MainClass.dealsCard(deck, playerList,2);
        try {
            MainClass.determineScore(playerList);
            fail("Expecting IllegalArgumentException ");
        }catch (IllegalArgumentException e){}
    }


    @Test
    public void testDealsCard_4Rround(){
        MainClass.dealsCard(deck, playerList, 4);
        Assertions.assertThat(playerList.get(0).getCards().size()).isEqualTo(3);
        Assertions.assertThat(playerList.get(1).getCards().size()).isEqualTo(2);

    }

    @Test
    public void testDealsCard_4RoundLargerDeck(){
        deck = Arrays.stream(("CA, D5, H9, HQ, S8, CA, D5, H9, HQ, S8").split(","))
                .map(String::trim).collect(Collectors.toList());
        MainClass.dealsCard(deck, playerList, 4);
        Assertions.assertThat(playerList.get(0).getCards().size()).isEqualTo(4);
        Assertions.assertThat(playerList.get(1).getCards().size()).isEqualTo(4);
    }

    @Test
    public void testCheckBlackJack(){
        MainClass.dealsCard(deck, playerList,2);
        MainClass.determineScore(playerList);
        Optional<Player> playerBlackJack = MainClass.checkBlackJack(playerList);
        Assertions.assertThat(playerBlackJack.isEmpty()).isTrue();
    }

    @Test
    public void testCheckBlackJack_SamBlackJack(){
        deck = Arrays.stream(("CA, D5, HQ, HQ, S8").split(","))
                .map(String::trim).collect(Collectors.toList());
        MainClass.dealsCard(deck, playerList,2);
        MainClass.determineScore(playerList);
        Optional<Player> playerBlackJack = MainClass.checkBlackJack(playerList);
        Assertions.assertThat(playerBlackJack.isEmpty()).isFalse();
        Assertions.assertThat(playerBlackJack.get().getPlayerName().equalsIgnoreCase("Sam"));
    }

    @Test
    public void testCheckBlackJack_DealerBlackJack(){
        deck = Arrays.stream(("CA, D5, H9, HQ, S8, D6").split(","))
                .map(String::trim).collect(Collectors.toList());
        MainClass.dealsCard(deck, playerList,3);
        MainClass.determineScore(playerList);
        Optional<Player> playerBlackJack = MainClass.checkBlackJack(playerList);
        Assertions.assertThat(playerBlackJack.isEmpty()).isFalse();
        Assertions.assertThat(playerBlackJack.get().
                getPlayerName().equalsIgnoreCase("dEaleR")).isTrue();
    }

    @Test
    public void testCheckBlackJack_SamBlackJackDifferentCard(){
        deck = Arrays.stream(("CA, D5, H10, HQ, S8").split(","))
                .map(String::trim).collect(Collectors.toList());
        MainClass.dealsCard(deck, playerList,2);
        MainClass.determineScore(playerList);
        Optional<Player> playerBlackJack = MainClass.checkBlackJack(playerList);
        Assertions.assertThat(playerBlackJack.isEmpty()).isFalse();
        Assertions.assertThat(playerBlackJack.get().
                getPlayerName().equalsIgnoreCase("Sam")).isTrue();
    }

    @Test
    public void testCheckBlackJack_SamWinBothStartBlackJack(){
        deck = Arrays.stream(("CA, Da, H10, HQ, S8").split(","))
                .map(String::trim).collect(Collectors.toList());
        MainClass.dealsCard(deck, playerList,2);
        MainClass.determineScore(playerList);
        Optional<Player> playerBlackJack = MainClass.checkBlackJack(playerList);
        Assertions.assertThat(playerBlackJack.isEmpty()).isFalse();
        Assertions.assertThat(playerBlackJack.get().
                getPlayerName().equalsIgnoreCase("Sam")).isTrue();
    }

    @Test
    public void testCheck22(){
        deck = Arrays.stream(("CA, Da, Ha, HA, S8").split(","))
                .map(String::trim).collect(Collectors.toList());
        MainClass.dealsCard(deck, playerList,2);
        MainClass.determineScore(playerList);
        Optional<Player> playerCheck22 = MainClass.check22(playerList);
        Assertions.assertThat(playerCheck22.isEmpty()).isFalse();
        Assertions.assertThat(playerCheck22.get().
                getPlayerName().equalsIgnoreCase("Dealer")).isTrue();

    }

    @Test
    public void testPickUntil(){
        deck = Arrays.stream(("CA, D5, H9, HQ, S8").split(","))
                .map(String::trim).collect(Collectors.toList());

        MainClass.dealsCard(deck, playerList,2);
        MainClass.determineScore(playerList);
        MainClass.pickUntil(deck, sam, 17);
        Assertions.assertThat(sam.getScore()).isEqualTo(20);
    }

    @Test
    public void testPickUntil_SamScore17(){
        deck = Arrays.stream(("CA, D5, H6, HQ, S8").split(","))
                .map(String::trim).collect(Collectors.toList());

        MainClass.dealsCard(deck, playerList,2);
        MainClass.determineScore(playerList);
        MainClass.pickUntil(deck, sam, 17);
        Assertions.assertThat(sam.getScore()).isEqualTo(17);
    }

    @Test
    public void testPickUntil_SamScore16(){
        deck = Arrays.stream(("CA, D5, H5, HQ, S8").split(","))
                .map(String::trim).collect(Collectors.toList());

        MainClass.dealsCard(deck, playerList,2);
        MainClass.determineScore(playerList);
        MainClass.pickUntil(deck, sam, 17);
        Assertions.assertThat(sam.getScore()).isEqualTo(24);
    }

    @Test
    public void testPickUntil_SamScore16LostGame(){
        deck = Arrays.stream(("CA, D5, H5, HQ, S8").split(","))
                .map(String::trim).collect(Collectors.toList());

        MainClass.dealsCard(deck, playerList,2);
        MainClass.determineScore(playerList);

        Assertions.assertThat(MainClass.pickUntil(deck, sam, 17)).isEqualTo("lost");
    }

    @Test
    public void testPickUntil_Dealer(){
        deck = Arrays.stream(("CA, D5, H9, HQ, S8, D7").split(","))
                .map(String::trim).collect(Collectors.toList());

        MainClass.dealsCard(deck, playerList,2);
        MainClass.determineScore(playerList);
        Assertions.assertThat(MainClass.pickUntil(deck, dealer, sam.getScore())).isEqualTo("lost");
    }

    @Test
    public void testPickUntil_DealerEqualSamIs17(){
        deck = Arrays.stream(("CA, D5, H6, HQ, S2, D2").split(","))
                .map(String::trim).collect(Collectors.toList());

        MainClass.dealsCard(deck, playerList,2);
        MainClass.determineScore(playerList);
        Assertions.assertThat(MainClass.pickUntil(deck, dealer, sam.getScore() +1 )).isEqualTo("continue");
    }

    @Test
    public void testPickUntil_DealerEqualSamIs17_DealerLostGame(){
        deck = Arrays.stream(("CA, D5, H6, HQ, S2, D5").split(","))
                .map(String::trim).collect(Collectors.toList());

        MainClass.dealsCard(deck, playerList,2);
        MainClass.determineScore(playerList);
        Assertions.assertThat(MainClass.pickUntil(deck, dealer, sam.getScore() +1 )).isEqualTo("lost");
        Assertions.assertThat(dealer.getScore()).isEqualTo(22);
    }

}
