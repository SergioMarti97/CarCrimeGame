package collisions;

import base.vectors.points2d.Vec2df;

public class ConvexPolygonCollisions {

    public static boolean shapeOverlapSAT(Polygon r1, Polygon r2) {
        Polygon poly1 = r1;
        Polygon poly2 = r2;

        for (int shape = 0; shape < 2; shape++) {
            if (shape == 1) {
                poly1 = r2;
                poly2 = r1;
            }

            for (int a = 0; a < poly1.getPoints().size(); a++) {
                int b = (a + 1) % poly1.getPoints().size();
                Vec2df axisProj = new Vec2df(
                        -(poly1.getPoints().get(b).getY() - poly1.getPoints().get(a).getY()),
                        poly1.getPoints().get(b).getX() - poly1.getPoints().get(a).getX()
                );
                axisProj.normalize();

                float minR1 = Float.MAX_VALUE;
                float maxR1 = -Float.MAX_VALUE;
                for (int p = 0; p < poly1.getPoints().size(); p++) {
                    float q = poly1.getPoints().get(p).mag2();
                    minR1 = Math.min(minR1, q);
                    maxR1 = Math.max(maxR1, q);
                }

                float minR2 = Float.MAX_VALUE;
                float maxR2 = -Float.MAX_VALUE;
                for (int p = 0; p < poly2.getPoints().size(); p++) {
                    float q = poly2.getPoints().get(p).mag2();
                    minR2 = Math.min(minR2, q);
                    maxR2 = Math.max(maxR2, q);
                }

                if (!(maxR2 >= minR1 && maxR1 >= minR2)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean shapeOverlapSATStatic(Polygon r1, Polygon r2) {
        Polygon poly1 = r1;
        Polygon poly2 = r2;

        float overlap = Float.MAX_VALUE;

        for (int shape = 0; shape < 2; shape++) {
            if (shape == 1) {
                poly1 = r2;
                poly2 = r1;
            }

            for (int a = 0; a < poly1.getPoints().size(); a++) {
                int b = (a + 1) % poly1.getPoints().size();
                Vec2df axisProj = new Vec2df(
                        -(poly1.getPoints().get(b).getY() - poly1.getPoints().get(a).getY()),
                        poly1.getPoints().get(b).getX() - poly1.getPoints().get(a).getX()
                );
                axisProj.normalize();

                float minR1 = Float.MAX_VALUE;
                float maxR1 = -Float.MAX_VALUE;
                for (int p = 0; p < poly1.getPoints().size(); p++) {
                    float q = poly1.getPoints().get(p).mag2();
                    minR1 = Math.min(minR1, q);
                    maxR1 = Math.max(maxR1, q);
                }

                float minR2 = Float.MAX_VALUE;
                float maxR2 = -Float.MAX_VALUE;
                for (int p = 0; p < poly2.getPoints().size(); p++) {
                    float q = poly2.getPoints().get(p).mag2();
                    minR2 = Math.min(minR2, q);
                    maxR2 = Math.max(maxR2, q);
                }

                overlap = Math.min(Math.min(maxR1, maxR2) - Math.max(minR1, minR2), overlap);

                if (!(maxR2 >= minR1 && maxR1 >= minR2)) {
                    return false;
                }
            }
        }

        Vec2df d = new Vec2df(
                r2.getPos().getX() - r1.getPos().getX(),
                r2.getPos().getY() - r1.getPos().getY()
        );
        float s = d.mag();
        r1.getPos().addToX(-(overlap * d.getX() / s));
        r1.getPos().addToX(-(overlap * d.getY() / s));
        return true;
    }

    public static boolean shapeOverlapDIAGS(Polygon r1, Polygon r2) {
        Polygon poly1 = r1;
        Polygon poly2 = r2;

        for (int shape = 0; shape < 2; shape++) {
            if (shape == 1) {
                poly1 = r2;
                poly2 = r1;
            }

            for (int p = 0; p < poly1.getPoints().size(); p++) {
                Vec2df lineR1s = poly1.getPos();
                Vec2df lineR1e = poly1.getPoints().get(p);

                for (int q = 0; q < poly2.getPoints().size(); q++) {
                    Vec2df lineR2s = poly2.getPoints().get(q);
                    Vec2df lineR2e = poly2.getPoints().get((q + 1) % poly2.getPoints().size());

                    float h = (lineR2e.getX() - lineR2s.getX()) * (lineR1s.getY() - lineR1e.getY()) - (lineR1s.getX() - lineR1e.getX()) * (lineR2e.getY() - lineR2s.getY());
                    float t1 = ((lineR2s.getY() - lineR2e.getY()) * (lineR1s.getX() - lineR2s.getX()) + (lineR2e.getX() - lineR2s.getX()) * (lineR1s.getY() - lineR2s.getY())) / h;
                    float t2 = ((lineR1s.getY() - lineR1e.getY()) * (lineR1s.getX() - lineR2s.getX()) + (lineR1e.getX() - lineR1s.getX()) * (lineR1s.getY() - lineR2s.getY())) / h;

                    if (t1 >= 0.0f && t1 < 1.0f && t2 >= 0.0f && t2 < 1.0f) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean shapeOverlapDIAGSStatic(Polygon r1, Polygon r2) {
        Polygon poly1 = r1;
        Polygon poly2 = r2;

        for (int shape = 0; shape < 2; shape++) {
            if (shape == 1) {
                poly1 = r2;
                poly2 = r1;
            }

            for (int p = 0; p < poly1.getPoints().size(); p++) {
                Vec2df lineR1s = poly1.getPos();
                Vec2df lineR1e = poly1.getPoints().get(p);

                Vec2df displacement = new Vec2df();

                for (int q = 0; q < poly2.getPoints().size(); q++) {
                    Vec2df lineR2s = poly2.getPoints().get(q);
                    Vec2df lineR2e = poly2.getPoints().get((q + 1) % poly2.getPoints().size());

                    float h = (lineR2e.getX() - lineR2s.getX()) * (lineR1s.getY() - lineR1e.getY()) - (lineR1s.getX() - lineR1e.getX()) * (lineR2e.getY() - lineR2s.getY());
                    float t1 = ((lineR2s.getY() - lineR2e.getY()) * (lineR1s.getX() - lineR2s.getX()) + (lineR2e.getX() - lineR2s.getX()) * (lineR1s.getY() - lineR2s.getY())) / h;
                    float t2 = ((lineR1s.getY() - lineR1e.getY()) * (lineR1s.getX() - lineR2s.getX()) + (lineR1e.getX() - lineR1s.getX()) * (lineR1s.getY() - lineR2s.getY())) / h;

                    if (t1 >= 0.0f && t1 < 1.0f && t2 >= 0.0f && t2 < 1.0f) {
                        displacement.addToX((1.0f - t1) * (lineR1e.getX() - lineR1s.getX()));
                        displacement.addToY((1.0f - t1) * (lineR1e.getY() - lineR1s.getY()));
                    }
                }

                r1.getPos().addToX(displacement.getX() * (shape == 0 ? -1 : 1));
                r1.getPos().addToY(displacement.getY() * (shape == 0 ? -1 : 1));
            }
        }

        return false;
    }

}
