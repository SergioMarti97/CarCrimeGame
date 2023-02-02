package game.car;

import base.graphics.image.Image;
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

public class Car {

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
    private Vec4df position;

    /**
     * The velocity
     */
    private Vec4df velocity;

    /**
     * The rotation of the car
     */
    private float rotation;

    /**
     * The speed of the car
     */
    private float speed;

    /**
     * Constructor
     * @param carImage the image of the car
     */
    public Car(Image carImage) {
        this.carImage = carImage;
        flat = MeshFactory.getFlat();
        Mat4x4 matOffset = MatrixMath.matrixMakeTranslation(-0.5f, -0.5f, 0.0f);
        Mat4x4 matScale = MatrixMath.matrixMakeScale(0.4f, 0.2f, 1.0f);
        Mat4x4 matOffsetScale = MatrixMath.matrixMultiplyMatrix(matOffset, matScale);
        MatrixMath.transformMesh(matOffsetScale, flat);

        position = new Vec4df();
        velocity = new Vec4df();
        rotation = 0.0f;
        speed = 2.0f;

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
        velocity = MatrixMath.matrixMultiplyVector(matRotZ, vec4df);
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

    public Vec4df getVelocity() {
        return velocity;
    }

    public Vec4df getPosition() {
        return position;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setPosition(Vec4df position) {
        this.position = position;
    }

    public void setVelocity(Vec4df velocity) {
        this.velocity = velocity;
    }

}
