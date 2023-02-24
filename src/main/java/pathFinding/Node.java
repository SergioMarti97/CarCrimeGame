package pathFinding;

import base.vectors.points2d.Vec2di;

import java.util.ArrayList;

public class Node {

    private Vec2di pos;

    private float globalGoal;

    private float localGoal;

    private boolean visited;

    private final ArrayList<Node> neighbours;

    private Node parent;

    public Node(Vec2di pos) {
        this.pos = pos;
        neighbours = new ArrayList<>();
        setToDefault();
    }

    public Node(Node other) {
        this.pos = new Vec2di(other.pos);
        this.neighbours = new ArrayList<>(other.neighbours);
        this.parent = other.parent;
        this.globalGoal = other.globalGoal;
        this.localGoal = other.localGoal;
        this.visited = other.visited;
    }

    public void setToDefault() {
        globalGoal = Float.MAX_VALUE;
        localGoal = Float.MAX_VALUE;
        visited = false;
        parent = null;
    }

    // Getters and Setters

    public Vec2di getPos() {
        return pos;
    }

    public void setPos(Vec2di pos) {
        this.pos = pos;
    }

    public float getGlobalGoal() {
        return globalGoal;
    }

    public void setGlobalGoal(float globalGoal) {
        this.globalGoal = globalGoal;
    }

    public float getLocalGoal() {
        return localGoal;
    }

    public void setLocalGoal(float localGoal) {
        this.localGoal = localGoal;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public ArrayList<Node> getNeighbours() {
        return neighbours;
    }

}
