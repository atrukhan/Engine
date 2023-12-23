package org.example;

import java.awt.Color;
public class Triangle {

    Vector v1;
    Vector v2;
    Vector v3;

    Vector l1;
    Vector l2;
    Vector l3;

    Vector c1;
    Vector c2;
    Vector c3;

    Vector vn1;
    Vector vn2;
    Vector vn3;

    Vector vt1;
    Vector vt2;
    Vector vt3;

    String textureFilename;
    int textureIndex = -1;
    Color color;
    Triangle(Vector v1, Vector v2, Vector v3, Vector vn1, Vector vn2, Vector vn3, Vector vt1, Vector vt2, Vector vt3, Color color) {

        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;

        this.vn1 = vn1;
        this.vn2 = vn2;
        this.vn3 = vn3;

        this.vt1 = vt1;
        this.vt2 = vt2;
        this.vt3 = vt3;

        this.color = color;
        this.textureFilename = null;
    }

    Triangle(Vector v1, Vector v2, Vector v3, Vector vn1, Vector vn2, Vector vn3, Vector vt1, Vector vt2, Vector vt3, Color color, String textureFilename) {

        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;

        this.vn1 = vn1;
        this.vn2 = vn2;
        this.vn3 = vn3;

        this.vt1 = vt1;
        this.vt2 = vt2;
        this.vt3 = vt3;

        this.color = color;
        this.textureFilename = textureFilename;
    }



    public static Color getShade(Color color, double shade){
        int red = (int) Math.min(255.d, color.getRed() * shade);
        int green = (int) Math.min(255.d, color.getGreen() * shade);
        int blue = (int) Math.min(255.d, color.getBlue() * shade);

        return new Color(red, green, blue);
    }

    public static Color getMediumColor( Color... colors){
        int red = 0, green = 0, blue = 0;
        for (Color c : colors){
            red += c.getRed();
            green += c.getGreen();
            blue += c.getBlue();
        }
        red = (int) Math.round((double) red/colors.length);
        green = (int) Math.round((double) green/colors.length);
        blue = (int) Math.round((double) blue/colors.length);

        return new Color(red, green, blue);
    }

    public static Triangle interpolate(Triangle t1, Triangle t2, double k){
        return new Triangle(
                Vector.interpolate(t1.v1, t2.v1, k), Vector.interpolate(t1.v2, t2.v2, k), Vector.interpolate(t1.v3, t2.v3, k),
                Vector.normalize(Vector.interpolate(t1.vn1, t2.vn1, k)), Vector.normalize(Vector.interpolate(t1.vn2, t2.vn2, k)), Vector.normalize(Vector.interpolate(t1.vn3, t2.vn3, k)),
                t1.vt1, t1.vt2, t1.vt3,
                t1.color,
                t1.textureFilename
        );
    }

    public Vector getV1() {
        return v1;
    }

    public void setV1(Vector v1) {
        this.v1 = v1;
    }

    public Vector getV2() {
        return v2;
    }

    public void setV2(Vector v2) {
        this.v2 = v2;
    }

    public Vector getV3() {
        return v3;
    }

    public void setV3(Vector v3) {
        this.v3 = v3;
    }

    public Vector getL1() {
        return l1;
    }

    public void setL1(Vector l1) {
        this.l1 = l1;
    }

    public Vector getL2() {
        return l2;
    }

    public void setL2(Vector l2) {
        this.l2 = l2;
    }

    public Vector getL3() {
        return l3;
    }

    public void setL3(Vector l3) {
        this.l3 = l3;
    }

    public Vector getC1() {
        return c1;
    }

    public void setC1(Vector c1) {
        this.c1 = c1;
    }

    public Vector getC2() {
        return c2;
    }

    public void setC2(Vector c2) {
        this.c2 = c2;
    }

    public Vector getC3() {
        return c3;
    }

    public void setC3(Vector c3) {
        this.c3 = c3;
    }

    public Vector getVn1() {
        return vn1;
    }

    public void setVn1(Vector vn1) {
        this.vn1 = vn1;
    }

    public Vector getVn2() {
        return vn2;
    }

    public void setVn2(Vector vn2) {
        this.vn2 = vn2;
    }

    public Vector getVn3() {
        return vn3;
    }

    public void setVn3(Vector vn3) {
        this.vn3 = vn3;
    }

    public Vector getVt1() {
        return vt1;
    }

    public void setVt1(Vector vt1) {
        this.vt1 = vt1;
    }

    public Vector getVt2() {
        return vt2;
    }

    public void setVt2(Vector vt2) {
        this.vt2 = vt2;
    }

    public Vector getVt3() {
        return vt3;
    }

    public void setVt3(Vector vt3) {
        this.vt3 = vt3;
    }

    public String getTextureFilename() {
        return textureFilename;
    }

    public void setTextureFilename(String textureFilename) {
        this.textureFilename = textureFilename;
    }

    public int getTextureIndex() {
        return textureIndex;
    }

    public void setTextureIndex(int textureIndex) {
        this.textureIndex = textureIndex;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}