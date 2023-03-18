package scims.model.data.scoring;

public interface EventScoring<T> {
    void setScore(T score);
    String getDisplayScore();
    T getScore();

    String getScoreType();

}
