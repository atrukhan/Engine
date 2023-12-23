package org.example;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Utils {

    private Vector lightPos = new Vector(0,0,0);
    private void flatShading(Triangle triangle){

        Vector lightToV1 = Vector.normalize(Vector.sub(lightPos, triangle.v1));
        Vector lightToV2 = Vector.normalize(Vector.sub(lightPos, triangle.v2));
        Vector lightToV3 = Vector.normalize(Vector.sub(lightPos, triangle.v3));


        double cos1 = Math.max(lightToV1.dot(triangle.vn1) / (lightToV1.mod()*triangle.vn1.mod()), 0.01d);
        double cos2 = Math.max(lightToV2.dot(triangle.vn2) / (lightToV2.mod()*triangle.vn2.mod()), 0.01d);
        double cos3 = Math.max(lightToV3.dot(triangle.vn3) / (lightToV3.mod()*triangle.vn3.mod()), 0.01d);

        Color triColor = Color.white;
        Color color1 = Triangle.getShade(triColor, cos1);
        Color color2 = Triangle.getShade(triColor, cos2);
        Color color3 = Triangle.getShade(triColor, cos3);

        triangle.color = Triangle.getMediumColor(color1, color2, color3);
    }

    private void dda(BufferedImage img, double x1, double y1, double x2, double y2){

        int ix1 = (int) Math.round(x1);
        int iy1 = (int) Math.round(y1);
        int ix2 = (int) Math.round(x2);
        int iy2 = (int) Math.round(y2);

        int deltaX = Math.abs(ix1 - ix2);
        int deltaY = Math.abs(iy1 - iy2);

        int length = Math.max(deltaX, deltaY);
        if (length == 0){
            if(ix1 >= 0 && ix1 < img.getWidth() && iy1 >= 0 && iy1 < img.getHeight() ){
                img.setRGB(ix1, iy1, 0xFFFFFF);
            }
        }else{
            double dx = (x2-x1) / length;
            double dy = (y2-y1) / length;

            int roundX;
            int roundY;

            double x = x1;
            double y = y1;

            int width = img.getWidth();
            int height = img.getHeight();

            length++;
            while (length-- > 0){
                x += dx;
                y += dy;

                roundX = (int) Math.round(x);
                roundY = (int) Math.round(y);

                if(roundX >= 0 && roundX < width && roundY >= 0 && roundY < height ){
                    img.setRGB(roundX, roundY, 0xFFFFFF);
                }

            }
        }


    }
}
