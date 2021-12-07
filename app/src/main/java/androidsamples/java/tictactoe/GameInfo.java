package androidsamples.java.tictactoe;

public class GameInfo {

    private int mLastMove;
    private int mGameState;
    private String mPlayer1;
    private boolean mP2Joined;

    public GameInfo(){
    }
    public GameInfo(String email){
        mLastMove = -1;
        mGameState = 0;
        this.mPlayer1 = email;
        mP2Joined = false;
    }

    public int getGameState() {
        return mGameState;
    }
    public void setGameState(int gameState) {
        this.mGameState = gameState;
    }


    public int getLastMove() {
        return mLastMove;
    }
    public void setLastMove(int lastMove) {
        this.mLastMove = lastMove;
    }

    public String getPlayer1() {
        return mPlayer1;
    }
    public void setPlayer1(String mPlayer1) {
        this.mPlayer1 = mPlayer1;
    }

    public boolean isP2Joined() {
        return mP2Joined;
    }
    public void setP2Joined(boolean mP2Joined) {
        this.mP2Joined = mP2Joined;
    }

}
