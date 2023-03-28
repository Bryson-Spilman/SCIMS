package scims.model.data.scoring;

public interface EventScoring<T> {
    void setScore(T score);
    T getScore();

    default String getScoreType() {
        String className = this.getClass().getSimpleName();
        String retVal =  className.substring(0, className.indexOf("Scoring"));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < retVal.length(); i++) {
            char c = retVal.charAt(i);
            if (Character.isUpperCase(c) && i != 0) {
                sb.append(" ");
            }
            sb.append(c);
        }
        retVal = sb.toString();
        return retVal;

    }

}
