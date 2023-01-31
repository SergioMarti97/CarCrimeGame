package base.vectors.points3d;

public class Vec3df implements Vec3d {

    /**
     * the x component.
     */
    private float x;

    /**
     * the y component.
     */
    private float y;

    /**
     * the z component.
     */
    private float z;

    /**
     * Void constructor
     */
    public Vec3df() {
        x = 0.0f;
        y = 0.0f;
        z = 1.0f;
    }

    /**
     * Constructor
     * @param x the x component.
     * @param y the y component.
     */
    public Vec3df(float x, float y) {
        this.x = x;
        this.y = y;
        z = 1.0f;
    }

    /**
     * Constructor
     * @param x the x component.
     * @param y the y component.
     * @param z the z component.
     */
    public Vec3df(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Constructor with same value for the three components
     * @param v same value for the three components
     */
    public Vec3df(float v) {
        this.x = v;
        this.y = v;
        this.z = v;
    }

    /**
     * Constructor passed by parameter
     * @param vec3df the object which we need to copy
     */
    public Vec3df(Vec3df vec3df) {
        this.x = vec3df.getX();
        this.y = vec3df.getY();
        this.z = vec3df.getZ();
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
     * This method add to the z component the amount
     * pass as a parameter
     * @param amount the amount to add to the z component
     */
    public void addToZ(float amount) {
        z += amount;
    }

    /**
     * This method multiply the two components by the amount
     * pass as a parameter
     * @param amount the amount to multiply both components
     */
    public void multiply(float amount) {
        x *= amount;
        y *= amount;
        z *= amount;
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
     * This method multiply the z component by the amount pass
     * as a parameter
     * @param amount the amount to multiply the z component
     */
    public void multiplyZBy(float amount) {
        z *= amount;
    }

    /**
     * @return the x component.
     */
    public float getX() {
        return x;
    }

    /**
     * @return the y component.
     */
    public float getY() {
        return y;
    }

    /**
     * @return the z component.
     */
    public float getZ() {
        return z;
    }

    /**
     * @param x the new value of the x component.
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * @param y the new value of the y component.
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * @param z the new value of the z component.
     */
    public void setZ(float z) {
        this.z = z;
    }

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(float v) {
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
        return (x * x) + (y * y) + (z * z);
    }

    /**
     * The dot product is a way to measure how similar are
     * two vectors.
     * @return an amount that represents the similarity.
     */
    public float dotProduct(Vec3df vec) {
        return x * vec.getX() + y * vec.getY() + z * vec.getZ();
    }

    @Override
    public void set(Vec3d vec3d)  {
        if (vec3d instanceof Vec3df) {
            Vec3df vec3df = (Vec3df) vec3d;
            this.x = vec3df.getX();
            this.y = vec3df.getY();
            this.z = vec3df.getZ();
        } else {
            // todo: put here the rest of vector types.
        }
    }

    @Override
    public void add(Vec3d vec3d)  {
        if (vec3d instanceof Vec3df) {
            Vec3df vec3df = (Vec3df) vec3d;
            this.x += vec3df.getX();
            this.y += vec3df.getY();
            this.z += vec3df.getZ();
        }
    }

    @Override
    public void sub(Vec3d vec3d)  {
        if (vec3d instanceof Vec3df) {
            Vec3df vec3df = (Vec3df) vec3d;
            this.x -= vec3df.getX();
            this.y -= vec3df.getY();
            this.z -= vec3df.getZ();
        }
    }

    @Override
    public void multiply(Vec3d vec3d)  {
        if (vec3d instanceof Vec3df) {
            Vec3df vec3df = (Vec3df) vec3d;
            this.x *= vec3df.getX();
            this.y *= vec3df.getY();
            this.z *= vec3df.getZ();
        }
    }

    @Override
    public void divide(Vec3d vec3d)  {
        if (vec3d instanceof Vec3df) {
            Vec3df vec3df = (Vec3df) vec3d;
            this.x /= vec3df.getX();
            this.y /= vec3df.getY();
            this.z /= vec3df.getZ();
        }
    }

    @Override
    public void normalize() {
        float l = mag();
        this.x /= l;
        this.y /= l;
        this.z /= l;
    }

    @Override
    public Vec3df normal() {
        float r = 1 / mag();
        return new Vec3df(x * r,y * r,z * r);
    }

    @Override
    public Vec3df perpendicular() {
        return null;
    }

    @Override
    public String toString() {
        return "X: " + x + " Y: " + y + " Z: " + z;
    }

}
