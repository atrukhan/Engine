package org.example;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Texture {
    private static final String RESOURCES_PATH = "src/main/resources/";
    private BufferedImage colorImage;
    private BufferedImage normalImage;
    private BufferedImage specularImage;
    private BufferedImage glossImage;
    private BufferedImage glowImage;
    private Vector ks;
    private String material;

    public Texture(TextureBuilder builder) {
        this.colorImage = builder.colorImage;
        this.normalImage = builder.normalImage;
        this.specularImage = builder.specularImage;
        this.glossImage = builder.glossImage;
        this.glowImage = builder.glowImage;
        this.ks = builder.ks;
        this.material = builder.material;
    }

    public BufferedImage getColorImage() {
        return colorImage;
    }

    public BufferedImage getNormalImage() {
        return normalImage;
    }

    public BufferedImage getSpecularImage() {
        return specularImage;
    }

    public BufferedImage getGlossImage() {
        return glossImage;
    }

    public BufferedImage getGlowImage() {
        return glowImage;
    }

    public Vector getKs() {
        return ks;
    }

    public String getMaterial() {
        return material;
    }

    public static class TextureBuilder {
        private BufferedImage colorImage;
        private BufferedImage normalImage;
        private BufferedImage specularImage;
        private BufferedImage glossImage;
        private BufferedImage glowImage;
        private Vector ks;
        private String material;

        private String folder;

        public TextureBuilder(String folder) {
            this.folder = folder;
        }

        public Texture.TextureBuilder setColorImage(String path) throws IOException {
            File file = new File(RESOURCES_PATH + folder + path);
            this.colorImage = ImageIO.read(file);
            return this;
        }

        public Texture.TextureBuilder setNormalImage(String path) throws IOException {
            File file = new File(RESOURCES_PATH + folder + path);
            this.normalImage = ImageIO.read(file);
            return this;
        }

        public Texture.TextureBuilder setSpecularImage(String path) throws IOException {
            File file = new File(RESOURCES_PATH + folder + path);
            this.specularImage = ImageIO.read(file);
            return this;
        }

        public Texture.TextureBuilder setGlossImage(String path) throws IOException {
            File file = new File(RESOURCES_PATH + folder + path);
            this.glossImage = ImageIO.read(file);
            return this;
        }

        public Texture.TextureBuilder setGlowImage(String path) throws IOException {
            File file = new File(RESOURCES_PATH + folder + path);
            this.glowImage = ImageIO.read(file);
            return this;
        }

        public Texture.TextureBuilder setKs(Vector ks) {
            this.ks = ks;
            return this;
        }
        public Texture.TextureBuilder setMaterial(String material) {
            this.material = material;
            return this;
        }

        public Texture build() {
            return new Texture(this);
        }
    }

}
