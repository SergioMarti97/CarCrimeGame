package base.vectors.points3d;

public class Vec3di implements Vec3d {

    /**
     * the x component.
     */
    private int x;

    /**
     * the y component.
     */
    private int y;

    /**
     * the z component.
     */
    private int z;

    /**
     * Void constructor
     */
    public Vec3di() {
        x = 0;
        y = 0;
        z = 0;
    }

    /**
     * Constructor
     * @param x the x component.
     * @param y the y component.
     */
    public Vec3di(int x, int y) {
        this.x = x;
        this.y = y;
        z = 0;
    }

    /**
     * Constructor
     * @param x the x component.
     * @param y the y component.
     * @param z the z component.
     */
    public Vec3di(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Constructor with same value for the three components
     * @param v same value for the three components
     */
    public Vec3di(int v) {
        this.x = v;
        this.y = v;
        this.z = v;
    }

    /**
     * Constructor passed by parameter
     * @param vec3di the object which we need to copy
     */
    public Vec3di(Vec3di vec3di) {
        this.x = vec3di.getX();
        this.y = vec3di.getY();
        this.z = vec3di.getZ();
    }

    /**
     * This method add to the components the amount
     * pass as a parameter
     * @param amount the amount to add to both components
     */
    public void add(float amount) {
        x += amount;
        y += amount;
        z += amount;
    }

    /**
     * This method add to the x component the amount
     * pass as a parameter
     * @param amount the amount to add to the x component
     */
    public void addToX(int amount) {
        x += amount;
    }

    /**
     * This method add to the y component the amount
     * pass as a parameter
     * @param amount the amount to add to the y component
     */
    public void addToY(int amount) {
        y += amount;
    }

    /**
     * This method add to the z component the amount
     * pass as a parameter
     * @param amount the amount to add to the z component
     */
    public void addToZ(int amount) {
        z += amount;
    }

    /**
     * This method multiply the two components by the amount
     * pass as a parameter
     * @param amount the amount to multiply both components
     */
    public void multiply(int amount) {
        x *= amount;
        y *= amount;
        z *= amount;
    }

    /**
     * This method multiply the x component by the amount pass
     * as a parameter
     * @param amount the amount to multiply the x component
     */
    public void multiplyXBy(int amount) {
        x *= amount;
    }

    /**
     * This method multiply the y component by the amount pass
     * as a parameter
     * @param amount the amount to multiply the y component
     */
    public void multiplyYBy(int amount) {
        y *= amount;
    }

    /**
     * This method multiply the z component by the amount pass
     * as a parameter
     * @param amount the amount to multiply the z component
     */
    public void multiplyZBy(int amount) {
        z *= amount;
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
    public int mag() {
        return (int)(Math.sqrt(mag2()));
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
    public int mag2() {
        return (x * x) + (y * y) + (z * z);
    }

    /**
     * The dot product is a way to measure how similar are
     * two vectors.
     * @return an amount that represents the similarity.
     */
    public int dotProduct(Vec3di vec) {
        return x * vec.getX() + y * vec.getY() + z * vec.getZ();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void set(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(int v) {
        this.x = v;
        this.y = v;
        this.z = v;
    }


    @Override
    public void set(Vec3d vec3d) {
        if ( vec3d instanceof Vec3di) {
            Vec3di vec3di = (Vec3di) vec3d;
            this.x = vec3di.getX();
            this.y = vec3di.getY();
            this.z = vec3di.getZ();
        } else {
            // todo: put here the rest of vector types.
        }
    }

    @Override
    public void add(Vec3d vec3d) {
        if ( vec3d instanceof Vec3df) {
            Vec3df vec3df = (Vec3df) vec3d;
            this.x += vec3df.getX();
            this.y += vec3df.getY();
            this.z += vec3df.getZ();
        }
    }

    @Override
    public void sub(Vec3d vec3d) {
        if ( vec3d instanceof Vec3di) {
            Vec3di vec3di = (Vec3di) vec3d;
            this.x -= vec3di.getX();
            this.y -= vec3di.getY();
            this.z -= vec3di.getZ();
        }
    }

    @Override
    public void multiply(Vec3d vec3d) {
        if ( vec3d instanceof Vec3di) {
            Vec3di vec3di = (Vec3di) vec3d;
            this.x *= vec3di.getX();
            this.y *= vec3di.getY();
            this.z *= vec3di.getZ();
        }
    }

    @Override
    public void divide(Vec3d vec3d) {
        if ( vec3d instanceof Vec3di) {
            Vec3di vec3di = (Vec3di) vec3d;
            this.x /= vec3di.getX();
            this.y /= vec3di.getY();
            this.z /= vec3di.getZ();
        }
    }

    @Override
    public void normalize() {
        int l = mag();
        this.x /= l;
        this.y /= l;
        this.z /= l;
    }

    @Override
    public Vec3d normal() {
        float r = 1 / mag();
        return new Vec3df(x * r,y * r,z * r);
    }

    @Override
    public Vec3d perpendicular() {
        return null;
    }

    @Override
    public String toString() {
        return "X: " + x + " Y: " + y + " Z: " + z;
    }

}
