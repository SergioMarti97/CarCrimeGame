package base.vectors.points2d;

public class Vec2dd implements Vec2d {

    /**
     * x component
     */
    private double x;

    /**
     * y component
     */
    private double y;

    /**
     * Void constructor
     */
    public Vec2dd() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Constructor
     * @param x the value of x component
     * @param y the value of y component
     */
    public Vec2dd(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor with same value for x and y
     * @param v value for x and y
     */
    public Vec2dd(double v) {
        this.x = v;
        this.y = v;
    }

    /**
     * Copy constructor
     * @param vec2dd the instance of the same object to copy the values
     */
    public Vec2dd(Vec2dd vec2dd) {
        this.x = vec2dd.getX();
        this.y = vec2dd.getY();
    }

    /**
     * This method add to the components the amount
     * pass as a parameter
     * @param amount the amount to add to both components
     */
    public void add(double amount) {
        x += amount;
        y += amount;
    }

    /**
     * This method add to the x component the amount
     * pass as a parameter
     * @param amount the amount to add to the x component
     */
    public void addToX(double amount) {
        x += amount;
    }

    /**
     * This method add to the y component the amount
     * pass as a parameter
     * @param amount the amount to add to the y component
     */
    public void addToY(double amount) {
        y += amount;
    }

    /**
     * This method multiply the two components by the amount
     * pass as a parameter
     * @param amount the amount to multiply both components
     */
    public void multiply(double amount) {
        x *= amount;
        y *= amount;
    }

    /**
     * This method multiply the x component by the amount pass
     * as a parameter
     * @param amount the amount to multiply the x component
     */
    public void multiplyXBy(double amount) {
        x *= amount;
    }

    /**
     * This method multiply the y component by the amount pass
     * as a parameter
     * @param amount the amount to multiply the y component
     */
    public void multiplyYBy(double amount) {
        y *= amount;
    }

    /**
     * Getter for the x component
     * @return the x component
     */
    public double getX() {
        return x;
    }

    /**
     * Getter for the y component
     * @return the y component
     */
    public double getY() {
        return y;
    }

    /**
     * Setter for the x component
     * @param x the new value for the x component
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Setter for the y component
     * @param y the new value for the y component
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Setter for the two components
     * @param x new value for x
     * @param y new value for y
     */
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * This method returns the magnitude of the vector.
     * It uses the pythagorean theorem to calculate
     * the module of the vector.
     *
     * h^2 = a^2 + b^2
     *
     * @return the magnitude of the vector
     */
    public double mag() {
        return Math.sqrt(mag2());
    }

    /**
     * This method returns the magnitude of the vector
     * without make the root square. It uses the
     * pythagorean theorem to calculate the module
     * of the vector.
     *
     * (h * h) = (a * a) + (b * b)
     *
     * As the root square is a potential high cost
     * operation, is interesting have this method.
     *
     * @return two times the magnitude of the vector
     */
    public double mag2() {
        return (x * x) + (y * y);
    }

    public double dist(double x, double y) {
        return Math.sqrt(dist2(x, y));
    }

    public double dist2(double x, double y) {
        double a = this.x - x;
        double b = this.y - y;
        return (a * a) + (b * b);
    }

    public double dist(Vec2dd pos) {
        return Math.sqrt(dist2(pos));
    }

    public double dist2(Vec2dd pos) {
        double a = this.x - pos.getX();
        double b = this.y - pos.getY();
        return (a * a) + (b * b);
    }

    @Override
    public void set(Vec2d vec2d) {
        if ( vec2d instanceof Vec2dd) {
            Vec2dd vec2dd = (Vec2dd) (vec2d);
            setX((float) (vec2dd.getX()));
            setY((float) (vec2dd.getY()));
        } else if ( vec2d instanceof Vec2df ) {
            Vec2df vec2df = (Vec2df)(vec2d);
            setX(vec2df.getX());
            setY(vec2df.getY());
        } else if ( vec2d instanceof Vec2di ) {
            Vec2di vec2di = (Vec2di)(vec2d);
            setX((float)vec2di.getX());
            setY((float)vec2di.getY());
        } else {
            this.x = 0;
            this.y = 0;
        }
    }

    @Override
    public void add(Vec2d vec2d) {
        if ( vec2d instanceof Vec2dd) {
            Vec2dd vec2dd = (Vec2dd)(vec2d);
            this.x += vec2dd.getX();
            this.y += vec2dd.getY();
        }
    }

    @Override
    public void sub(Vec2d vec2d) {
        if ( vec2d instanceof Vec2dd) {
            Vec2dd vec2dd = (Vec2dd)(vec2d);
            this.x -= vec2dd.getX();
            this.y -= vec2dd.getY();
        }
    }

    @Override
    public void multiply(Vec2d vec2d) {
        if ( vec2d instanceof Vec2dd) {
            Vec2dd vec2dd = (Vec2dd)(vec2d);
            this.x *= vec2dd.getX();
            this.y *= vec2dd.getY();
        }
    }

    @Override
    public void divide(Vec2d vec2d) {
        if ( vec2d instanceof Vec2dd) {
            Vec2dd vec2dd = (Vec2dd)(vec2d);
            this.x /= vec2dd.getX();
            this.y /= vec2dd.getY();
        }
    }

    @Override
    public void normalize() {
        double l = mag();
        this.x /= l;
        this.y /= l;
    }

    @Override
    public Vec2d normal() {
        double r = 1 / mag();
        return new Vec2dd(x * r,y * r);
    }

    @Override
    public Vec2d perpendicular() {
        return new Vec2dd(-y, x);
    }

    @Override
    public void translateThisAngle(float angle) {
        angle *= (Math.PI / 180.0f);
        double x = (this.x * Math.cos(angle)) - (this.y * Math.sin(angle));
        double y = (this.x * Math.sin(angle)) + (this.y * Math.cos(angle));
        this.x = x;
        this.y = y;
    }

    /**
     * The dot product is a way to measure how similar are
     * two vectors.
     * @return an amount that represents the similarity.
     */
    public double dotProduct(Vec2dd vec) {
        return x * vec.getX() + y * vec.getY();
    }

    /**
     * The cross product I don't know what is at all but is
     * the opposite of the dot product.
     * @return an amount.
     */
    public double crossProduct(Vec2dd vec) {
        return x * vec.getY() - y * vec.getX();
    }

    @Override
    public String toString() {
        return x + "x " + y + "y";
    }

}
