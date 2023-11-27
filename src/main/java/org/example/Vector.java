package org.example;

public class Vector {
    double x;
    double y;
    double z;

    double w = 1;

    public Vector(double x, double y, double z, double... w) {
        this.x = x;
        this.y = y;
        this.z = z;
        //default value 1
        this.w = w.length>0?w[0]:1;
    }
    public Vector(Vector v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public Vector sum(Vector v){
        return new Vector(x + v.x, y + v.y, z + v.z);
    }
    public double dot(Vector other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public double mod(){
        return Math.sqrt(x*x + y*y + z*z);
    }

    public static Vector add(Vector v1, Vector v2){
        return new Vector(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }
    public static Vector sub(Vector reduced, Vector deductible){
        return new Vector(reduced.x - deductible.x, reduced.y - deductible.y, reduced.z - deductible.z);
    }

    public static Vector prod(Vector v1, Vector v2){
        return new Vector(v1.x * v2.x, v1.y * v2.y, v1.z * v2.z);
    }
    public static Vector prod(Vector v, double d){
        return new Vector(v.x * d, v.y * d, v.z * d);
    }

    public static Vector cross(Vector v1, Vector v2){
        return new Vector(
                v1.y*v2.z - v1.z*v2.y,
                v1.z*v2.x - v1.x*v2.z,
                v1.x*v2.y - v1.y*v2.x
        );
    }
    public static Vector normalize(Vector v) {
        double length_of_v = Math.sqrt((v.x * v.x) + (v.y * v.y) + (v.z * v.z));
        return new Vector(v.x / length_of_v, v.y / length_of_v, v.z / length_of_v);
    }



    public static Vector div(Vector v, double d){
        return new Vector(v.x / d, v.y / d, v.z / d, v.w / d);
    }





    public static Vector intersectPlane(Vector plane1, Vector plane2, Vector lineStart, Vector lineEnd){
        Vector tempPlane = normalize(plane2);
        double planeDot = -plane1.dot(tempPlane);
        double ad = lineStart.dot(tempPlane);
        double bd = lineEnd.dot(tempPlane);
        double temp = (-planeDot - ad) / (bd - ad);
        Vector lineStartToEnd = sub(lineEnd, lineStart);
        Vector lineToIntersect = prod(lineStartToEnd, temp);

        return add(lineStart, lineToIntersect);
    }

    private static double dist(Vector plane1, Vector plane2, Vector v){
        Vector temp = normalize(v);
        return (plane2.x * temp.x + plane2.y * temp.y + plane2.z * temp.z - plane2.dot(plane1));
    }
    public static int countTriangleClipAgainstPlane(Vector plane1, Vector plane2, Triangle inTriangle, Triangle outTriangle1, Triangle outTriangle2){
        Vector tempPlane = normalize(plane2);

        Vector[] insidePoints = new Vector[3];
        Vector[] outsidePoints = new Vector[3];
        int insidePointCount = 0, outsidePointCount = 0;


        double d1 = dist(plane1, tempPlane, inTriangle.v1);
        double d2 = dist(plane1, tempPlane, inTriangle.v2);
        double d3 = dist(plane1, tempPlane, inTriangle.v3);

        if (d1 >= 0)
            insidePoints[insidePointCount++] = inTriangle.v1;
        else
            outsidePoints[outsidePointCount++] = inTriangle.v1;

        if (d2 >= 0)
            insidePoints[insidePointCount++] = inTriangle.v2;
        else
            outsidePoints[outsidePointCount++] = inTriangle.v2;

        if (d3 >= 0)
            insidePoints[insidePointCount++] = inTriangle.v3;
        else
            outsidePoints[outsidePointCount++] = inTriangle.v3;



        if (insidePointCount == 0){
            return 0;
        }
        if(insidePointCount == 3){
            outTriangle1.color = inTriangle.color;

            outTriangle1.v1 = inTriangle.v1;
            outTriangle1.v2 = inTriangle.v2;
            outTriangle1.v3 = inTriangle.v3;
            return -1;
        }
        if(insidePointCount == 1 && outsidePointCount == 2){
             //copy raster color here for outTriangle1
            outTriangle1.color = inTriangle.color;

            outTriangle1.v1 = insidePoints[0];
            outTriangle1.v2 = intersectPlane(plane1, tempPlane, insidePoints[0], outsidePoints[0]);
            outTriangle1.v3 = intersectPlane(plane1, tempPlane, insidePoints[0], outsidePoints[1]);
            return 1;
        }
        if(insidePointCount == 2 && outsidePointCount == 1){
            //copy raster color here for outTriangle1, outTriangle2
            outTriangle1.color = inTriangle.color;
            outTriangle2.color = inTriangle.color;

            outTriangle1.v1 = insidePoints[0];
            outTriangle1.v2 = insidePoints[1];
            outTriangle1.v3 = intersectPlane(plane1, tempPlane, insidePoints[0], outsidePoints[0]);

            outTriangle2.v1 = insidePoints[1];
            outTriangle2.v2 = outTriangle1.v3;
            outTriangle2.v3 = intersectPlane(plane1, tempPlane, insidePoints[1], outsidePoints[0]);

            return 2;
        }
        return -2;
    }
}
