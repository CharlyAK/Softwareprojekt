package soprowerwolf.Classes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.widget.Button;

/**
 * Created by Alex on 09.09.2016.
 */
public class GlobalVariables extends Application {
    private static GlobalVariables instance;

    // Global variables: Game
    private int ownPlayerID;
    private String ownRole;
    private int gameID;
    private int numPlayers;
    private String[] cards;
    private Button currentlySelectedPlayer;
    private String[] Phases;
    private String currentPhase = "Game";
    private Activity currentContext;
    private String winner;
    private boolean spielleiter;

    // Global variables: Dieb
    private boolean DiebChoosen;

    // Global variables: Amor
    private Button lover1;
    private Button lover2;
    private Button OK;

    // Global variables: Seherin
    private AlertDialog PopUpSeherinIdentity;

    // Global variables: Jäger
    private boolean JaegerDies = false;
    private boolean victimJaeger = false;


    // Restrict the constructor from being instantiated
    private GlobalVariables() {
    }

    public static synchronized GlobalVariables getInstance() {
        if (instance == null) {
            instance = new GlobalVariables();
        }
        return instance;
    }

    /*
     * getter and setter for Game - Variables
     */
    public void setOwnPlayerID(int ownPlayerID) {
        this.ownPlayerID = ownPlayerID;
    }

    public int getOwnPlayerID() {
        return this.ownPlayerID;
    }

    public void setOwnRole(String ownRole) {
        this.ownRole = ownRole;
    }

    public String getOwnRole() {
        return this.ownRole;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public int getGameID() {
        return this.gameID;
    }

    public void setCards(String[] cards) {
        this.cards = cards;
    }

    public String[] getCards() {
        return this.cards;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public int getNumPlayers() {
        return this.numPlayers;
    }

    public void setCurrentlySelectedPlayer(Button currentlySelectedPlayer) {
        this.currentlySelectedPlayer = currentlySelectedPlayer;
    }

    public Button getCurrentlySelectedPlayer() {
        return this.currentlySelectedPlayer;
    }

    public void setPhases(String[] Phases) {
        this.Phases = Phases;
    }

    public String[] getPhases() {
        return this.Phases;
    }

    public void setCurrentPhase(String currentPhase) {
        this.currentPhase = currentPhase;
    }

    public String getCurrentPhase() {
        return this.currentPhase;
    }

    public void setCurrentContext(Activity currentContext) {
        this.currentContext = currentContext;
    }

    public Activity getCurrentContext() {
        return this.currentContext;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getWinner() {
        return this.winner;
    }

    public boolean isSpielleiter() {
        return spielleiter;
    }

    public void setSpielleiter(boolean spielleiter) {
        this.spielleiter = spielleiter;
    }


    /*
     *   getter and setter for Dieb - Variables
     */
    public void setDiebChoosen(boolean DiebChoosen) {
        this.DiebChoosen = DiebChoosen;
    }

    public boolean getDiebChoosen() {
        return this.DiebChoosen;
    }


    /*
     *  getter and setter for Amor - Variables
     */
    public void setLover1(Button lover1) {
        this.lover1 = lover1;
    }

    public Button getLover1() {
        return this.lover1;
    }

    public void setLover2(Button lover2) {
        this.lover2 = lover2;
    }

    public Button getLover2() {
        return this.lover2;
    }

    public void setOK(Button OK) {
        this.OK = OK;
    }

    public Button getOK() {
        return this.OK;
    }


    /*
     *  getter and setter for Seherin - Variables
     */
    public void setPopUpSeherinIdentity(AlertDialog PopUpSeherinIdentity) {
        this.PopUpSeherinIdentity = PopUpSeherinIdentity;
    }

    public AlertDialog getPopUpSeherinIdentity() {
        return this.PopUpSeherinIdentity;
    }


    /*
     *  getter and setter for Jäger - Variables
     */
    public void setJaegerDies(boolean JaegerDies) {
        this.JaegerDies = JaegerDies;
    }
    public boolean getJaegerDies() {
        return this.JaegerDies;
    }

    public void setVictimJaeger(boolean victimJaeger) {
        this.victimJaeger = victimJaeger;
    }
    public boolean getVictimJaeger() {
        return this.victimJaeger;
    }
}
