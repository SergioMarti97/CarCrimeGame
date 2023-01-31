package base;

public interface AbstractGame {

    void initialize(GameApplication gc);

    void update(GameApplication gc, float elapsedTime);

    void render(GameApplication gc);

}
