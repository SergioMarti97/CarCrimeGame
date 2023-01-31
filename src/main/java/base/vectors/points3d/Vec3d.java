package base.vectors.points3d;

public interface Vec3d {

    /**
     * General setter
     * @param vec3d the vector to set this values.
     */
    void set(Vec3d vec3d);

    /**
     * This method add the components of the vector to
     * the components of the vector pass as a parameter
     * @param vec3d the three dimensions vector to add
     *              to this vector.
     */
    void add(Vec3d vec3d);

    /**
     * This method subtract the components of the vector to
     * the components of the vector pass as a parameter
     * @param vec3d the three dimensions vector to add
     *              to this vector.
     */
    void sub(Vec3d vec3d);

    /**
     * This method multiply the components of the vector to
     * the components of the vector pass as a parameter
     * @param vec3d the three dimensions vector to add
     *              to this vector.
     */
    void multiply(Vec3d vec3d);

    /**
     * This method divide the components of the vector to
     * the components of the vector pass as a parameter
     * @param vec3d the three dimensions vector to add
     *              to this vector.
     */
    void divide(Vec3d vec3d);

    /**
     * This method normalize the vector. Normalize
     * consist in divide the components by the magnitude
     * of the vector. In this way, the values of the
     * components of the vector will be between 0 and 1.
     */
    void normalize();

    /**
     * This method is similar to the normalize method.
     * But it returns this vector normalized, and don't
     * modify the values of the components.
     * @return a new vector which is this vector but normalized.
     */
    Vec3d normal();

    /**
     * Returns the perpendicular vector of this vector.
     * In three dimensions calculate the perpendicular vector
     * consists to exchange the three components and change
     * the sign of one of them. That last part will change
     * in the same vector, but with different direction.
     * @return the perpendicular vector to this.
     */
    Vec3d perpendicular();
}
