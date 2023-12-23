package org.example;

public class Mat4x4 {
    double[][] v = new double[4][4];

    public Mat4x4() {
        for (int i = 0; i < 15; i++){
            v[i/4][i%4] = 0.d;
        }
    }

    public Mat4x4(double[][] value) {
        this.v = value;
    }

    public static Vector multiplyVector(Vector in, Mat4x4 m){

        double x = in.x * m.v[0][0] + in.y * m.v[1][0] + in.z * m.v[2][0] + in.w * m.v[3][0];
        double y = in.x * m.v[0][1] + in.y * m.v[1][1] + in.z * m.v[2][1] + in.w * m.v[3][1];
        double z = in.x * m.v[0][2] + in.y * m.v[1][2] + in.z * m.v[2][2] + in.w * m.v[3][2];
        double w = in.x * m.v[0][3] + in.y * m.v[1][3] + in.z * m.v[2][3] + in.w * m.v[3][3];

        return new Vector(x, y, z, w);
    }

    public static Vector divVector(Vector in, Mat4x4 m){

        double x = in.x / m.v[0][0] + in.y / m.v[1][0] + in.z / m.v[2][0] + in.w / m.v[3][0];
        double y = in.x / m.v[0][1] + in.y / m.v[1][1] + in.z / m.v[2][1] + in.w / m.v[3][1];
        double z = in.x / m.v[0][2] + in.y / m.v[1][2] + in.z / m.v[2][2] + in.w / m.v[3][2];
        double w = in.x / m.v[0][3] + in.y / m.v[1][3] + in.z / m.v[2][3] + in.w / m.v[3][3];

        return new Vector(x, y, z, w);
    }

    public static Mat4x4 multiplyMatrix(Mat4x4 m1, Mat4x4 m2){
        Mat4x4 res = new Mat4x4();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                res.v[j][i] = m1.v[j][0] * m2.v[0][i] + m1.v[j][1] * m2.v[1][i] + m1.v[j][2] * m2.v[2][i] + m1.v[j][3] * m2.v[3][i];
        return res;
    }

    public static Mat4x4 getPointAtMat(Vector pos, Vector target, Vector up){
        Vector newForward = Vector.normalize(Vector.sub(pos, target));
        Vector newRight = Vector.normalize(Vector.cross(up, newForward));
        Vector newUp = Vector.normalize(Vector.cross(newForward, newRight));

        return new Mat4x4(new double[][]{
                {newRight.x, newUp.x, newForward.x, 0},
                {newRight.y, newUp.y, newForward.y, 0},
                {newRight.z, newUp.z, newForward.z, 0},
                {-(pos.dot(newRight)), -pos.dot(newUp), -pos.dot(newForward), 1}
        });
    }

    public static Mat4x4 inverse(Mat4x4 mat){
        return new Mat4x4(new double[][]{
                {mat.v[0][0], mat.v[1][0], mat.v[2][0], 0},
                {mat.v[0][1], mat.v[1][1], mat.v[2][1], 0},
                {mat.v[0][2], mat.v[1][2], mat.v[2][2], 0},
                {
                        -(mat.v[3][0] * mat.v[0][0] + mat.v[3][1] * mat.v[0][1] + mat.v[3][2] * mat.v[0][2]),
                        -(mat.v[3][0] * mat.v[1][0] + mat.v[3][1] * mat.v[1][1] + mat.v[3][2] * mat.v[1][2]),
                        -(mat.v[3][0] * mat.v[2][0] + mat.v[3][1] * mat.v[2][1] + mat.v[3][2] * mat.v[2][2]),
                        1
                }
        });
    }

    public static Mat4x4 getIdentityMat(){
        return new Mat4x4(new double[][]{
                {1.d, 0.d, 0.d, 0.d},
                {0.d, 1.d, 0.d, 0.d},
                {0.d, 0.d, 1.d, 0.d},
                {0.d, 0.d, 0.d, 1.d}
        });
    }

    public static Mat4x4 getScaleMat(double scaleX, double scaleY, double scaleZ){
        return new Mat4x4(new double[][]{
                {scaleX, 0.d, 0.d, 0.d},
                {0.d, scaleY, 0.d, 0.d},
                {0.d, 0.d, scaleZ, 0.d},
                {0.d, 0.d, 0.d, 1.d}
        });
    }

    public static Mat4x4 getRotationZMat(double angleRad){
        return new Mat4x4(new double[][]{
                {Math.cos(angleRad), Math.sin(angleRad), 0.d, 0.d},
                {-Math.sin(angleRad), Math.cos(angleRad), 0.d, 0.d},
                {0.d, 0.d, 1.d, 0.d},
                {0.d, 0.d, 0.d, 1.d}
        });
    }

    public static Mat4x4 getRotationYMat(double angleRad){
        return new Mat4x4(new double[][]{
                {Math.cos(angleRad), 0.d, Math.sin(angleRad), 0.d},
                {0.d, 1.d, 0.d, 0.d},
                {-Math.sin(angleRad), 0.d, Math.cos(angleRad), 0.d},
                {0.d, 0.d, 0.d, 1.d}
        });
    }

    public static Mat4x4 getRotationXMat(double angleRad){
        return new Mat4x4(new double[][]{
                {1.d, 0.d, 0.d, 0.d},
                {0.d, Math.cos(angleRad), Math.sin(angleRad), 0.d},
                {0.d, -Math.sin(angleRad), Math.cos(angleRad), 0.d},
                {0.d, 0.d, 0.d, 1.d}
        });
    }

    public static Mat4x4 getTranslationMat(double x, double y, double z){
        return new Mat4x4(new double[][]{
                {1.d, 0.d, 0.d, 0.d},
                {0.d, 1.d, 0.d, 0.d},
                {0.d, 0.d, 1.d, 0.d},
                {x, y, z, 1.d}
        });
    }

    public static Mat4x4 getProjectionMat(double fovDeg, double aspect, double zNear, double zFar){
        return new Mat4x4(new double[][]{
                {1/ aspect / Math.tan(Math.toRadians(fovDeg/2)), 0.d, 0.d, 0.d},
                {0.d, 1/Math.tan(Math.toRadians(fovDeg/2)), 0.d, 0.d},
                {0.d, 0.d, zFar / (zNear - zFar), -1.d},
                {0.d, 0.d, (zFar * zNear) / (zNear - zFar), 0.d}
        });
    }

    public static Mat4x4 getViewPort(double width, double height){
        return new Mat4x4(new double[][]{
                {width/2, 0.d, 0.d, 0.d},
                {0.d, -height/2, 0.d, 0.d},
                {0.d, 0.d, 1.d, 0.d},
                {width/2, height/2, 0.d, 1.d}
        });
    }
}
