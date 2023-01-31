package base.vectors.points2d;

public class Vec2df implements Vec2d {

    /**
     * x component
     */
    private float x;

    /**
     * y component
     */
    private float y;

    /**
     * Void constructor
     */
    public Vec2df() {
        this.x = 0.0f;
        this.y = 0.0f;
    }

    /**
     * Constructor
     * @param x the value of x component
     * @param y the value of y component
     */
    public Vec2df(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor with same value for x and y
     * @param v value for x and y
     */
    public Vec2df(float v) {
        this.x = v;
        this.y = v;
    }

    /**
     * Copy constructor
     * @param vec2df the instance of the same object to copy
     */
    public Vec2df(Vec2df vec2df) {
        this.x = vec2df.getX();
        this.y = vec2df.getY();
    }

    /**
     * This method add to the components the amount
     * pass as a parameter
     * @param amount the amount to add to both components
     */
    public void add(float amount) {
        x += amount;
        y += amount;
    }

    /**
     * This method add to the x component the amount
     * pass as a parameter
     * @param amount the amount to add to the x component
     */
    public void addToX(float amount) {
        x += amount;
    }

    /**
     * This method add to the y component the amount
     * pass as a parameter
     * @param amount the amount to add to the y component
     */
    public void addToY(float amount) {
        y += amount;
    }

    /**
     * This method multiply the two components by the amount
     * pass as a parameter
     * @param amount the amount to multiply both components
     */
    public void multiply(float amount) {
        x *= amount;
        y *= amount;
    }

    /**
     * This method multiply the x component by the amount pass
     * as a parameter
     * @param amount the amount to multiply the x component
     */
    public void multiplyXBy(float amount) {
        x *= amount;
    }

    /**
     * This method multiply the y component by the amount pass
     * as a parameter
     * @param amount the amount to multiply the y component
     */
    public void multiplyYBy(float amount) {
        y *= amount;
    }

    /**
     * Getter for the x component
     * @return the x component
     */
    public float getX() {
        return x;
    }

    /**
     * Getter for the y component
     * @return the y component
     */
    public float getY() {
        return y;
    }

    /**
     * Setter for the x component
     * @param x the new value for the x component
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Setter for the y component
     * @param y the new value for the y component
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Setter for the two components
     * @param x new value for x
     * @param y new value for y
     */
    public void set(float x, float y) {
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
    public float mag() {
        return (float)(Math.sqrt(mag2()));
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
    public float mag2() {
        return (x * x) + (y * y);
    }

    public float dist(float x, float y) {
        return (float)Math.sqrt(dist2(x, y));
    }

    public float dist2(float x, float y) {
        float a = this.x - x;
        float b = this.y - y;
        return (a * a) + (b * b);
    }

    public float dist(Vec2df pos) {
        return (float)Math.sqrt(dist2(pos));
    }

    public float dist2(Vec2df pos) {
        float a = this.x - pos.getX();
        float b = this.y - pos.getY();
        return (a * a) + (b * b);
    }

    @Override
    public void set(Vec2d vec2d) {
        if ( vec2d instanceof Vec2df) {
            Vec2df vec2df = (Vec2df)(vec2d);
            setX(vec2df.getX());
            setY(vec2df.getY());
        } else if ( vec2d instanceof Vec2di ) {
            Vec2di vec2di = (Vec2di)(vec2d);
            setX((float)vec2di.getX());
            setY((float)vec2di.getY());
        } else if ( vec2d instanceof Vec2dd) {
            Vec2dd vec2dd = (Vec2dd)(vec2d);
            setX((float)(vec2dd.getX()));
            setY((float)(vec2dd.getY()));
        } else {
            this.x = 0;
            this.y = 0;
        }
    }

    @Override
    public void add(Vec2d vec2d) {
        if ( vec2d instanceof Vec2df) {
            Vec2df vec2df = (Vec2df)(vec2d);
            this.x += vec2df.getX();
            this.y += vec2df.getY();
        }
    }

    @Override
    public void sub(Vec2d vec2d) {
        if ( vec2d instanceof Vec2df) {
            Vec2df vec2df = (Vec2df)(vec2d);
            this.x -= vec2df.getX();
            this.y -= vec2df.getY();
        }
    }

    @Override
    public void multiply(Vec2d vec2d) {
        if ( vec2d instanceof Vec2df) {
            Vec2df vec2df = (Vec2df)(vec2d);
            this.x *= vec2df.getX();
            this.y *= vec2df.getY();
        }
    }

    @Override
    public void divide(Vec2d vec2d) {
        if ( vec2d instanceof Vec2df) {
            Vec2df vec2df = (Vec2df)(vec2d);
            this.x /= vec2df.getX();
            this.y /= vec2df.getY();
        }
    }

    @Override
    public void normalize() {
        float l = mag();
        this.x /= l;
        this.y /= l;
    }

    @Override
    public Vec2d normal() {
        float r = 1 / mag();
        return new Vec2df(x * r,y * r);
    }

    @Override
    public Vec2d perpendicular() {
        return new Vec2df(-y, x);
    }

    @Override
    public void translateThisAngle(float angle) {
        angle *= (Math.PI / 180.0f);
        float x = (float)((this.x * Math.cos(angle)) - (this.y * Math.sin(angle)));
        float y = (float)((this.x * Math.sin(angle)) + (this.y * Math.cos(angle)));
        this.x = x;
        this.y = y;
    }

    /**
     * The dot product is a way to measure how similar are
     * two vectors.
     * @return an amount that represents the similarity.
     */
    public float dotProduct(Vec2df vec) {
        return x * vec.getX() + y * vec.getY();
    }

    /**
     * The cross product I don't know what is at all but is
     * the opposite of the dot product.
     * @return an amount.
     */
    public float crossProduct(Vec2df vec) {
        return x * vec.getY() - y * vec.getX();
    }

    @Override
    public String toString() {
        return x + "x " + y + "y";
    }

}
