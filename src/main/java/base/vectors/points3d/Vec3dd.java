package base.vectors.points3d;

public class Vec3dd implements Vec3d {

    /**
     * the x component.
     */
    private double x;

    /**
     * the y component.
     */
    private double y;

    /**
     * the z component.
     */
    private double z;

    /**
     * Void constructor
     */
    public Vec3dd() {
        x = 0.0;
        y = 0.0;
        z = 1.0;
    }

    /**
     * Constructor
     * @param x the x component.
     * @param y the y component.
     */
    public Vec3dd(double x, double y) {
        this.x = x;
        this.y = y;
        z = 1.0;
    }

    /**
     * Constructor
     * @param x the x component.
     * @param y the y component.
     * @param z the z component.
     */
    public Vec3dd(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Constructor with same value for the three components
     * @param v same value for the three components
     */
    public Vec3dd(double v) {
        this.x = v;
        this.y = v;
        this.z = v;
    }

    /**
     * Constructor passed by parameter
     * @param vec3dd the object which we need to copy
     */
    public Vec3dd(Vec3dd vec3dd) {
        this.x = vec3dd.getX();
        this.y = vec3dd.getY();
        this.z = vec3dd.getZ();
    }

    /**
     * This method add to the components the amount
     * pass as a parameter
     * @param amount the amount to add to both components
     */
    public void add(double amount) {
        x += amount;
        y += amount;
        z += amount;
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
     * This method add to the z component the amount
     * pass as a parameter
     * @param amount the amount to add to the z component
     */
    public void addToZ(double amount) {
        z += amount;
    }

    /**
     * This method multiply the two components by the amount
     * pass as a parameter
     * @param amount the amount to multiply both components
     */
    public void multiply(double amount) {
        x *= amount;
        y *= amount;
        z *= amount;
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
     * This method multiply the z component by the amount pass
     * as a parameter
     * @param amount the amount to multiply the z component
     */
    public void multiplyZBy(double amount) {
        z *= amount;
    }

    /**
     * @return the x component.
     */
    public double getX() {
        return x;
    }

    /**
     * @return the y component.
     */
    public double getY() {
        return y;
    }

    /**
     * @return the z component.
     */
    public double getZ() {
        return z;
    }

    /**
     * @param x the new value of the x component.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @param y the new value of the y component.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @param z the new value of the z component.
     */
    public void setZ(double z) {
        this.z = z;
    }

    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(double v) {
        this.x = v;
        this.y = v;
        this.z = v;
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
        return (x * x) + (y * y) + (z * z);
    }

    /**
     * The dot product is a way to measure how similar are
     * two vectors.
     * @return an amount that represents the similarity.
     */
    public double dotProduct(Vec3dd vec) {
        return x * vec.getX() + y * vec.getY() + z * vec.getZ();
    }


    @Override
    public void set(Vec3d vec3d) {
        if (vec3d instanceof Vec3dd) {
            Vec3dd vec = (Vec3dd) vec3d;
            this.x = vec.getX();
            this.y = vec.getY();
            this.z = vec.getZ();
        } else {
            // todo: put here the rest of vector types.
        }
    }

    @Override
    public void add(Vec3d vec3d) {
        if (vec3d instanceof Vec3dd) {
            Vec3dd vec = (Vec3dd) vec3d;
            this.x += vec.getX();
            this.y += vec.getY();
            this.z += vec.getZ();
        }
    }

    @Override
    public void sub(Vec3d vec3d) {
        if (vec3d instanceof Vec3dd) {
            Vec3dd vec = (Vec3dd) vec3d;
            this.x -= vec.getX();
            this.y -= vec.getY();
            this.z -= vec.getZ();
        }
    }

    @Override
    public void multiply(Vec3d vec3d) {
        if (vec3d instanceof Vec3dd) {
            Vec3dd vec = (Vec3dd) vec3d;
            this.x *= vec.getX();
            this.y *= vec.getY();
            this.z *= vec.getZ();
        }
    }

    @Override
    public void divide(Vec3d vec3d) {
        if (vec3d instanceof Vec3dd) {
            Vec3dd vec = (Vec3dd) vec3d;
            this.x /= vec.getX();
            this.y /= vec.getY();
            this.z /= vec.getZ();
        }
    }

    @Override
    public void normalize() {
        double l = mag();
        this.x /= l;
        this.y /= l;
        this.z /= l;
    }

    @Override
    public Vec3d normal() {
        double r = 1 / mag();
        return new Vec3dd(x * r,y * r,z * r);
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
