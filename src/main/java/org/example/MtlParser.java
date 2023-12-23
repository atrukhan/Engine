package org.example;

import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.List;

public class MtlParser {

    private static final String FILE_EXTENSION = ".mtl";
    private static final String RESOURCES_PATH = "src/main/resources/";

    private final String filename;
    private final String folder;
    private Texture[] textures;
    private HashMap<String, Integer> texturesMap;

    public MtlParser(String folder, String filename) throws IOException {
        this.filename = filename;
        this.folder = folder;
        this.texturesMap = new HashMap<>();

        int size = 0;
        File file = new File(RESOURCES_PATH + folder + filename);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("newmtl")){
                    size++;
                }
            }
        }finally {
            this.textures = new Texture[size];
        }
    }

    public void parse() throws IOException {

        Texture.TextureBuilder tb = null;
        int index = -1;


        File file = new File(RESOURCES_PATH + folder + filename);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String newLine = line.trim().replaceAll(" +", " ");
                String[] strings = newLine.split(" ");
                switch (strings[0]) {
                    case "newmtl" -> {
                        if (tb != null){
                            textures[index] = tb.build();
                        }
                        tb = new Texture.TextureBuilder(folder).setMaterial(strings[1]);
                        index++;
                    }
                    case "map_Kd" -> {
                        tb = tb.setColorImage(strings[1]);
                    }
                    case "map_Ks" -> {
                        tb = tb.setSpecularImage(strings[1]);
                    }
                    case "map_Ns" -> {
                        tb = tb.setGlossImage(strings[1]);
                    }
                    case "map_Ke" -> {
                        tb = tb.setGlowImage(strings[1]);
                    }
                    case "Ks" -> {
                        tb = tb.setKs(new Vector(Double.parseDouble(strings[1]), Double.parseDouble(strings[2]), Double.parseDouble(strings[3])));
                    }
                }
            }
            textures[index] = tb.build();
        }
    }

    public Texture[] getTextures(){
        return textures;
    }


}
