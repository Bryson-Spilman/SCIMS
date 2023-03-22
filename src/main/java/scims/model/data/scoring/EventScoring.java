package scims.model.data.scoring;

public interface EventScoring<T> {
    void setScore(T score);
    T getScore();

    default String getScoreType() {
        String className = this.getClass().getSimpleName();
        return className.substring(0, className.indexOf("Scoring"));
    }

}
