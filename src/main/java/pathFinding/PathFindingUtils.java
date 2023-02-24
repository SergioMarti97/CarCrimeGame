package pathFinding;

import java.util.LinkedList;

public class PathFindingUtils {

    public static float distance(Node a, Node b) {
        return (float) Math.sqrt(a.getPos().dist(b.getPos()));
    }

    public static boolean solveAStar(Node start, Node end) {
        Node current = start;
        start.setLocalGoal(0.0f);
        start.setGlobalGoal(distance(start, end));

        LinkedList<Node> notTestedNodes = new LinkedList<>();
        notTestedNodes.add(start);

        while (
             !notTestedNodes.isEmpty() &&
             !current.equals(end)
        ) {
            notTestedNodes.sort((Node a, Node b) -> Float.compare(a.getGlobalGoal(), b.getGlobalGoal()));
            while (!notTestedNodes.isEmpty() && !notTestedNodes.peekFirst().isVisited()) {
                notTestedNodes.pollFirst();
            }

            if (notTestedNodes.isEmpty()) {
                break;
            }

            current = notTestedNodes.peekFirst();
            current.setVisited(true);

            for (var neighbour : current.getNeighbours()) {
                if (!neighbour.isVisited()) {  // !neighbour.isObstacle()
                    notTestedNodes.addLast(neighbour);
                }
                float possiblyLowerGoal = current.getLocalGoal() + distance(current, neighbour);
                if (possiblyLowerGoal < neighbour.getLocalGoal()) {
                    neighbour.setParent(current);
                    neighbour.setLocalGoal(possiblyLowerGoal);
                    neighbour.setGlobalGoal(neighbour.getLocalGoal() + distance(neighbour, end));
                }
            }
        }

        return true;
    }

}
