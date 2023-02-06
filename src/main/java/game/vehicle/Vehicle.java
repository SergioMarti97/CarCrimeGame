package game.vehicle;

import base.graphics.image.Image;
import base.vectors.points2d.Vec2df;
import collisions.Polygon;
import collisions.PolygonFactory;
import engine3d.PipeLine;
import engine3d.matrix.Mat4x4;
import engine3d.matrix.MatrixMath;
import engine3d.mesh.Mesh;
import engine3d.mesh.MeshFactory;
import engine3d.mesh.Model;
import engine3d.transforms.Rotation;
import engine3d.transforms.Scale;
import engine3d.transforms.Transform;
import engine3d.transforms.Translation;
import engine3d.vectors.Vec4df;

public class Vehicle {

    /**
     * The bounding box
     */
    private Mesh flat;

    /**
     * The image of the car
     */
    private Image carImage;

    /**
     * The position
     */
    private Vec2df position;

    /**
     * The velocity
     */
    private Vec2df velocity;

    /**
     * The rotation of the car
     */
    private float rotation;

    /**
     * The speed of the car
     */
    private float speed;

    private Polygon boundingBox;

    public Vehicle() {
        flat = MeshFactory.getFlat();
        Mat4x4 matOffset = MatrixMath.matrixMakeTranslation(-0.5f, -0.5f, 0.0f);
        Mat4x4 matScale = MatrixMath.matrixMakeScale(0.4f, 0.2f, 1.0f);
        Mat4x4 matOffsetScale = MatrixMath.matrixMultiplyMatrix(matOffset, matScale);
        MatrixMath.transformMesh(matOffsetScale, flat);

        position = new Vec2df();
        velocity = new Vec2df();
        rotation = 0.0f;
        speed = 2.0f;

        boundingBox = PolygonFactory.buildCenteredSquare(position.getX(), position.getY());
        // Shape the bounding box to the shape of the car
        // Vec2df offset = new Vec2df(-0.5f, -0.5f);
        Vec2df scale = new Vec2df(0.4f, 0.2f);
        for (var p : boundingBox.getModel()) {
            // p.add(offset);
            p.multiply(scale);
        }
    }

    /**
     * Constructor
     * @param carImage the image of the car
     */
    public Vehicle(Image carImage) {
        this.carImage = carImage;
        flat = MeshFactory.getFlat();
        Mat4x4 matOffset = MatrixMath.matrixMakeTranslation(-0.5f, -0.5f, 0.0f);
        Mat4x4 matScale = MatrixMath.matrixMakeScale(0.4f, 0.2f, 1.0f);
        Mat4x4 matOffsetScale = MatrixMath.matrixMultiplyMatrix(matOffset, matScale);
        MatrixMath.transformMesh(matOffsetScale, flat);

        position = new Vec2df();
        velocity = new Vec2df();
        rotation = 0.0f;
        speed = 2.0f;

        boundingBox = PolygonFactory.buildCenteredSquare(position.getX(), position.getY());
        // Shape the bounding box to the shape of the car
        // Vec2df offset = new Vec2df(-0.5f, -0.5f);
        Vec2df scale = new Vec2df(0.4f, 0.2f);
        for (var p : boundingBox.getModel()) {
            // p.add(offset);
            p.multiply(scale);
        }
    }

    /**
     * This method rotates the car, and updates the
     * velocity vector of the car
     * @param rotation the angle of rotation
     */
    public void rotate(float rotation) {
        Vec4df vec4df = new Vec4df(1.0f, 0.0f, 0.0f);
        Mat4x4 matRotZ = MatrixMath.matrixMakeRotationZ(rotation);
        this.rotation = rotation;
        Vec4df velocity = MatrixMath.matrixMultiplyVector(matRotZ, vec4df);
        this.velocity.set(velocity.getX(), velocity.getY());
    }

    public void updateBoundingBox() {
        boundingBox.getPos().set(position);
        boundingBox.setAngle(rotation);
        boundingBox.update();
    }

    public void setPosToBuildingBox() {
        position.set(boundingBox.getPos());
    }

    /**
     * The method for drawing the car on screen
     * @param pipeLine the pipeLine object for draw the 3D mesh
     */
    public void render(PipeLine pipeLine) {
        Mat4x4 matRotZ = MatrixMath.matrixMakeRotationZ(rotation);
        Mat4x4 matTrans = MatrixMath.matrixMakeTranslation(position.getX(), position.getY(), -0.1f);
        Mat4x4 matCar = MatrixMath.matrixMultiplyMatrix(matRotZ, matTrans);
        pipeLine.setTransform(matCar);
        pipeLine.renderMesh(flat, carImage);
    }

    public void render(Model model, PipeLine pipeLine) {
        Rotation rot = new Rotation(0, 0, rotation);
        Scale scale = new Scale(0.05f, 0.05f, 0.05f);
        Translation trans = new Translation(position.getX(), position.getY(), -0.1f);

        rot.update();
        scale.update();
        trans.update();

        Transform t = new Transform();

        pipeLine.setTransform(t.combine(rot).combine(scale).combine(trans).getMat());
        pipeLine.renderModel(model);
    }

    ///////////////////////////////////////////////////////////////////////////

    public float getRotation() {
        return rotation;
    }

    public float getSpeed() {
        return speed;
    }

    public Vec2df getVelocity() {
        return velocity;
    }

    public Vec2df getPosition() {
        return position;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setPosition(Vec2df position) {
        this.position = position;
    }

    public void setVelocity(Vec2df velocity) {
        this.velocity = velocity;
    }

    ///////////////////////////////////////////////////////////////////////////

    public Polygon getBoundingBox() {
        return boundingBox;
    }

}
