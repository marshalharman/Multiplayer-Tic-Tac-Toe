package androidsamples.java.tictactoe;

public class PlayerInfo {
    private String email;
    private int wins;
    private int losses;

    public PlayerInfo(){}

    public PlayerInfo(String email){
        this.email = email;
        wins = 0;
        losses = 0;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }
    public int getWins() {
        return wins;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }
    public int getLosses() {
        return losses;
    }

}
