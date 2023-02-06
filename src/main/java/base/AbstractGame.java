package base;

public abstract class AbstractGame {

    public abstract void initialize(GameApplication gc);

    public abstract void update(GameApplication gc, float elapsedTime);

    public abstract void render(GameApplication gc);

    public void stop(GameApplication gc) {}

}
