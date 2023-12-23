package org.example;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ObjParser {

    private static final String FILE_EXTENSION = ".obj";
    private static final String RESOURCES_PATH = "src/main/resources/";

    private int framesCount = -1;

    private String material;

    private Texture[] textures;
    private final List<List<Triangle>> animation = new ArrayList<>();

    private final List<Vector> vertices = new ArrayList<>();
    private final List<Vector> normalVectors = new ArrayList<>();
    private final List<Vector> textureVectors = new ArrayList<>();
    private final List<Triangle> triangles = new ArrayList<>();

    public List<Triangle> parse(String folder, String filename) throws IOException, IndexOutOfBoundsException {
        vertices.clear();
        triangles.clear();
        normalVectors.clear();
        material = null;
        File file = new File(RESOURCES_PATH + folder + filename + FILE_EXTENSION);

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
                                0
                        ));
                    }
                    case "usemtl" -> {
                        material = strings[1];
                    }
                    case "mtllib" -> {
                        MtlParser mp = new MtlParser(folder, strings[1]);
                        mp.parse();
                        textures = mp.getTextures();

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
                                    textureVectors.get(Integer.parseInt(strings[1].split("/")[1]) - 1),
                                    textureVectors.get(Integer.parseInt(strings[2].split("/")[1]) - 1),
                                    textureVectors.get(Integer.parseInt(strings[3].split("/")[1]) - 1),
                                    Color.white,
                                    material
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
                                    textureVectors.get(Integer.parseInt(strings[1].split("/")[1]) - 1),
                                    textureVectors.get(Integer.parseInt(strings[2].split("/")[1]) - 1),
                                    textureVectors.get(Integer.parseInt(strings[3].split("/")[1]) - 1),
                                    Color.white,
                                    material
                            ));
                            triangles.add(new Triangle(
                                    vertices.get(Integer.parseInt(strings[1].split("/")[0]) - 1),
                                    vertices.get(Integer.parseInt(strings[3].split("/")[0]) - 1),
                                    vertices.get(Integer.parseInt(strings[4].split("/")[0]) - 1),
                                    normalVectors.get(Integer.parseInt(strings[1].split("/")[2]) - 1),
                                    normalVectors.get(Integer.parseInt(strings[3].split("/")[2]) - 1),
                                    normalVectors.get(Integer.parseInt(strings[4].split("/")[2]) - 1),
                                    textureVectors.get(Integer.parseInt(strings[1].split("/")[1]) - 1),
                                    textureVectors.get(Integer.parseInt(strings[2].split("/")[1]) - 1),
                                    textureVectors.get(Integer.parseInt(strings[3].split("/")[1]) - 1),
                                    Color.white,
                                    material
                            ));
                        }
                    }
                }
            }
        }
        return triangles;

    }

    public List<List<Triangle>> parseAnimation(String folder, String filename) throws IOException, IndexOutOfBoundsException {

        animation.clear();

        MtlParser mp = null;
        boolean flag = true;
        int i = 1;
        while (flag){
            try {
                vertices.clear();
                triangles.clear();
                normalVectors.clear();
                material = null;
                File file = new File(RESOURCES_PATH + folder + filename + " (" + i + ")" + FILE_EXTENSION);

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
                                        Math.max(Double.parseDouble(strings[1]), 0),
                                        Math.min(Double.parseDouble(strings[2]), 1),
                                        0
                                ));
                            }
                            case "usemtl" -> {
                                material = strings[1];
                            }
                            case "mtllib" -> {
                                if(mp == null) {
                                    mp = new MtlParser(folder, strings[1]);
                                    mp.parse();
                                    textures = mp.getTextures();
                                }
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
                                            textureVectors.get(Integer.parseInt(strings[1].split("/")[1]) - 1),
                                            textureVectors.get(Integer.parseInt(strings[2].split("/")[1]) - 1),
                                            textureVectors.get(Integer.parseInt(strings[3].split("/")[1]) - 1),
                                            Color.white,
                                            material
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
                                            textureVectors.get(Integer.parseInt(strings[1].split("/")[1]) - 1),
                                            textureVectors.get(Integer.parseInt(strings[2].split("/")[1]) - 1),
                                            textureVectors.get(Integer.parseInt(strings[3].split("/")[1]) - 1),
                                            Color.white,
                                            material
                                    ));
                                    triangles.add(new Triangle(
                                            vertices.get(Integer.parseInt(strings[1].split("/")[0]) - 1),
                                            vertices.get(Integer.parseInt(strings[3].split("/")[0]) - 1),
                                            vertices.get(Integer.parseInt(strings[4].split("/")[0]) - 1),
                                            normalVectors.get(Integer.parseInt(strings[1].split("/")[2]) - 1),
                                            normalVectors.get(Integer.parseInt(strings[3].split("/")[2]) - 1),
                                            normalVectors.get(Integer.parseInt(strings[4].split("/")[2]) - 1),
                                            textureVectors.get(Integer.parseInt(strings[1].split("/")[1]) - 1),
                                            textureVectors.get(Integer.parseInt(strings[2].split("/")[1]) - 1),
                                            textureVectors.get(Integer.parseInt(strings[3].split("/")[1]) - 1),
                                            Color.white,
                                            material
                                    ));
                                }
                            }

                        }
                    }
                }
                animation.add(List.copyOf(triangles));
            }catch (Exception e){
                flag = false;
            }

            i++;
        }
        framesCount = i - 2;
        return animation;
    }

    public int getFramesCount() {
        return framesCount;
    }

    public Texture[] getTextures(){
        return textures;
    }
}