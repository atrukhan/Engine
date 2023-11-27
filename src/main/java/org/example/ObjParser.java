package org.example;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ObjParser {

    private static final String FILE_EXTENSION = ".obj";
    private static final String RESOURCES_PATH = "src/main/resources/";

    private static final List<Vector> vertices = new ArrayList<>();
    private static final List<Vector> normalVectors = new ArrayList<>();
    private static final List<Vector> textureVectors = new ArrayList<>();
    private static final List<Triangle> triangles = new ArrayList<>();

    public static List<Triangle> parse(String filename) {
        vertices.clear();
        triangles.clear();
        normalVectors.clear();

        File file = new File(RESOURCES_PATH + filename + FILE_EXTENSION);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String newLine = line.trim().replaceAll(" +", " ");
                String[] strings = newLine.split(" ");
                switch (strings[0]) {
                    case "v" -> {
                        vertices.add(new Vector(
                                Double.parseDouble(strings[1]),
                                Double.parseDouble(strings[2]),
                                Double.parseDouble(strings[3])

                        ));
                    }
                    case "vn" -> {
                        normalVectors.add(new Vector(
                                Double.parseDouble(strings[1]),
                                Double.parseDouble(strings[2]),
                                Double.parseDouble(strings[3])

                        ));
                    }
                    case "vt" -> {
                        textureVectors.add(new Vector(
                                Double.parseDouble(strings[1]),
                                Double.parseDouble(strings[2]),
                                Double.parseDouble(strings[3])
                        ));
                    }
                    case "f" -> {
                        if (strings.length == 4) {

                            triangles.add(new Triangle(
                                    vertices.get(Integer.parseInt(strings[1].split("/")[0]) - 1),
                                    vertices.get(Integer.parseInt(strings[2].split("/")[0]) - 1),
                                    vertices.get(Integer.parseInt(strings[3].split("/")[0]) - 1),
                                    normalVectors.get(Integer.parseInt(strings[1].split("/")[2]) - 1),
                                    normalVectors.get(Integer.parseInt(strings[2].split("/")[2]) - 1),
                                    normalVectors.get(Integer.parseInt(strings[3].split("/")[2]) - 1),
//                                null, null, null,
                                    textureVectors.get(Integer.parseInt(strings[1].split("/")[1]) - 1),
                                    textureVectors.get(Integer.parseInt(strings[2].split("/")[1]) - 1),
                                    textureVectors.get(Integer.parseInt(strings[3].split("/")[1]) - 1),
                                    Color.white
                            ));
                        }
                        if (strings.length == 5) {

                            triangles.add(new Triangle(
                                    vertices.get(Integer.parseInt(strings[1].split("/")[0]) - 1),
                                    vertices.get(Integer.parseInt(strings[2].split("/")[0]) - 1),
                                    vertices.get(Integer.parseInt(strings[3].split("/")[0]) - 1),
                                    normalVectors.get(Integer.parseInt(strings[1].split("/")[2]) - 1),
                                    normalVectors.get(Integer.parseInt(strings[2].split("/")[2]) - 1),
                                    normalVectors.get(Integer.parseInt(strings[3].split("/")[2]) - 1),
//                                    null, null, null,
                                    textureVectors.get(Integer.parseInt(strings[1].split("/")[1]) - 1),
                                    textureVectors.get(Integer.parseInt(strings[2].split("/")[1]) - 1),
                                    textureVectors.get(Integer.parseInt(strings[3].split("/")[1]) - 1),
                                    Color.white
                            ));
                            triangles.add(new Triangle(
                                    vertices.get(Integer.parseInt(strings[1].split("/")[0]) - 1),
                                    vertices.get(Integer.parseInt(strings[3].split("/")[0]) - 1),
                                    vertices.get(Integer.parseInt(strings[4].split("/")[0]) - 1),
                                    normalVectors.get(Integer.parseInt(strings[1].split("/")[2]) - 1),
                                    normalVectors.get(Integer.parseInt(strings[3].split("/")[2]) - 1),
                                    normalVectors.get(Integer.parseInt(strings[4].split("/")[2]) - 1),
//                                    null, null, null,
                                    textureVectors.get(Integer.parseInt(strings[1].split("/")[1]) - 1),
                                    textureVectors.get(Integer.parseInt(strings[2].split("/")[1]) - 1),
                                    textureVectors.get(Integer.parseInt(strings[3].split("/")[1]) - 1),
                                    Color.white
                            ));
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("IO exception");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bound exception");
        } finally {
            return triangles;
        }
    }

}