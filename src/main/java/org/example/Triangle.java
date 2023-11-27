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


}