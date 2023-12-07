package org.example;

import com.aparapi.Kernel;
import com.aparapi.Range;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.List;
// a - part of source image with size of pattern, b - pattern
//cor = sum(a,b)(a*b)/sqrt(sum(a)(a^2)) + sqrt(sum(b)(b^2))
public class Screen extends JPanel implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener, ActionListener{

    private final Dimension SCREEN_SIZE;

    private final Mat4x4 matProjection;
    private final Mat4x4 matViewport;
    private int camSpeed = 1;
    private Vector modelPos = new Vector(0.d, 0.d, -10.d);
    private Vector cameraPos = new Vector(0.d,0.d,0.d);
    private Vector target = new Vector(0.d, 0.d, -1.d);
    private Vector lookDir;
    private Vector rightTarget = new Vector(1.d, 0.d, 0.d);
    private Vector rightDir;
    private Vector upTarget = new Vector(0.d, 1.d, 0.d);
    private Vector upDir;
    private final Vector up = new Vector(0.d,1.d,0.d);
    private Vector lightPos = new Vector(0.d, -10.d, 0.d);
    private double camAngleY = 0;
    private static final int Z_BUFFER_HEIGHT = 1080;
    private static final int Z_BUFFER_WIDTH = 1920;
    private final double[][] zBuffer = new double[Z_BUFFER_WIDTH][Z_BUFFER_HEIGHT];
    private static final int THREAD_COUNT = 5;
    private final List<List<Triangle>> models;
    private final List<Triangle> model;
    private final List<Thread> threads = new ArrayList<>();
    private Instant lastTime = Instant.now();
    private double time = 0;
    private double angle = 0.d;

    private final BufferedImage colorMap;
    private final BufferedImage normalMap;
    private final BufferedImage specularMap;

    public Screen(Dimension screenSize) throws IOException {
        super();

        SCREEN_SIZE = screenSize;


        matProjection = Mat4x4.getProjectionMat(45.d, screenSize.getWidth() / screenSize.getHeight(), 0.1d, 1000.d);
        matViewport = Mat4x4.getViewPort(screenSize.getWidth(), screenSize.getHeight());

        String filename = "head";
        String f = "head";
        model = ObjParser.parse(f);
        models = partition(model);

        File file = new File("src/main/resources/" + filename + "_color.png");
        colorMap = ImageIO.read(file);
        file = new File("src/main/resources/" + filename + "_normal.png");
        normalMap = ImageIO.read(file);
        file = new File("src/main/resources/" + filename + "_specular.png");
        specularMap = ImageIO.read(file);

        Timer timer = new Timer(10, this);
        timer.setRepeats(true);
        timer.start();
    }

    public void paintComponent(Graphics g)
    {
        Instant start = Instant.now();
        time = Duration.between(lastTime, start).toMillis();
        lastTime = start;

        BufferedImage img = new BufferedImage(SCREEN_SIZE.width, SCREEN_SIZE.height, BufferedImage.TYPE_INT_RGB);

        angle += Math.PI / 8 * time / 1000;

//--------------------update camera lookDir && create view matrix--------------------
        Mat4x4 matCameraRotate = Mat4x4.getRotationYMat(camAngleY);
        lookDir = Mat4x4.multiplyVector(Vector.sub(cameraPos,target), matCameraRotate);
        rightDir = Mat4x4.multiplyVector(Vector.sub(cameraPos,rightTarget), matCameraRotate);
        upDir = Vector.sub(cameraPos,upTarget);
        Vector targetNew = Vector.add(cameraPos, lookDir);
        Mat4x4 matView = Mat4x4.getPointAtMat(cameraPos, targetNew, up);
//-----------------------------------------------------------------------------------

        MatCollection matCollection = new MatCollection.MatCollectionBuilder()
                .setAngle(Math.toRadians(180), angle, Math.toRadians(0))
                .setTransition(modelPos)
                .setView(matView)
                .setProjection(matProjection)
                .setViewport(matViewport)
                .build();



        List<Triangle> toClipping = new ArrayList<>();
        threadTransformation(toClipping, matCollection);

        fillZBuffer(zBuffer);

        List<Triangle> toRaster = new ArrayList<>();
        threadClipping(toClipping, toRaster);

        for (Triangle triangle : toRaster)
            rasterTriangle(img, triangle, matCollection.getAngle());

        Instant finish = Instant.now();

        img.getGraphics().drawString(String.valueOf(1000 / Duration.between(start, finish).toMillis()), 10, 10);

        g.drawImage(img, 0, 0, null);
    }

    private List<List<Triangle>> partition(List<Triangle> model){
        List<List<Triangle>> models = new ArrayList<>();
        int size = model.size();
        for (int i = 0; i < THREAD_COUNT-1; i++){
            models.add(model.subList(size/THREAD_COUNT*i, size/THREAD_COUNT*(i+1)));
        }
        models.add(model.subList(size/THREAD_COUNT*(THREAD_COUNT-1), size));
        return models;
    }

    private void threadTransformation(List<Triangle> toClipping, MatCollection matCollection){
        List<List<Triangle>> toClippings = new ArrayList<>();

        for (int i = 0; i < THREAD_COUNT; i++) {
            toClippings.add(new ArrayList<>());
            List<Triangle> tempM = models.get(i);
            List<Triangle> tempC = toClippings.get(i);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    transformation(tempM, tempC, matCollection);
                }
            });
            threads.add(thread);
        }
        for(Thread thread : threads)
            thread.start();

        try {
            for(Thread thread : threads)
                thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        threads.clear();
        for (int i = 0; i < THREAD_COUNT; i++)
            toClipping.addAll(toClippings.get(i));
    }
    private void transformation(List<Triangle> model, List<Triangle> toRaster, MatCollection matCollection){

        for (Triangle tri : model) {

            Triangle triViewed = new Triangle(
                    Mat4x4.multiplyVector(tri.v1, matCollection.getFinalView()),
                    Mat4x4.multiplyVector(tri.v2, matCollection.getFinalView()),
                    Mat4x4.multiplyVector(tri.v3, matCollection.getFinalView()),
                    tri.vn1,
                    tri.vn2,
                    tri.vn3,
                    tri.vt1,
                    tri.vt2,
                    tri.vt3,
                    tri.color
            );


            int clippedTriangles = 0;
            Triangle[] clipped = new Triangle[]{
                    new Triangle(new Vector(0.d,0.d,0.d),new Vector(0.d,0.d,0.d),new Vector(0.d,0.d,0.d), null, null, null, null, null, null, null),
                    new Triangle(new Vector(0.d,0.d,0.d),new Vector(0.d,0.d,0.d),new Vector(0.d,0.d,0.d), null, null, null, null, null, null, null)
            };

            clippedTriangles = Vector.countTriangleClipAgainstPlane(new Vector(0.d, 0.d, 0.1d), new Vector(0.d, 0.d, 0.1d), triViewed, clipped[0], clipped[1]);

            if(clippedTriangles == -1) {

                Triangle newTri = new Triangle(
                        Mat4x4.multiplyVector(tri.v1, matCollection.getFinalViewport()),
                        Mat4x4.multiplyVector(tri.v2, matCollection.getFinalViewport()),
                        Mat4x4.multiplyVector(tri.v3, matCollection.getFinalViewport()),
                        Mat4x4.multiplyVector(tri.vn1, matCollection.getAngle()),
                        Mat4x4.multiplyVector(tri.vn2, matCollection.getAngle()),
                        Mat4x4.multiplyVector(tri.vn3, matCollection.getAngle()),
                        tri.vt1,
                        tri.vt2,
                        tri.vt3,
                        tri.color
                );
//                double w1 = 1 / triViewed.v1.w;
//                double w2 = 1 / triViewed.v2.w;
//                double w3 = 1 / triViewed.v3.w;
                double w1 = Mat4x4.multiplyVector(tri.v1, matCollection.getFinalProjection()).w;
                double w2 = Mat4x4.multiplyVector(tri.v2, matCollection.getFinalProjection()).w;
                double w3 = Mat4x4.multiplyVector(tri.v3, matCollection.getFinalProjection()).w;

                newTri.v1 = Vector.div(newTri.v1, newTri.v1.w);
                newTri.v2 = Vector.div(newTri.v2, newTri.v2.w);
                newTri.v3 = Vector.div(newTri.v3, newTri.v3.w);
                newTri.v1.w = Math.abs(w1);
                newTri.v2.w = Math.abs(w2);
                newTri.v3.w = Math.abs(w3);

                if (!polygonRejection(newTri)) {

                    newTri.l1 = Vector.normalize(Vector.sub(lightPos, Mat4x4.multiplyVector(tri.v1, matCollection.getWorld())));
                    newTri.l2 = Vector.normalize(Vector.sub(lightPos, Mat4x4.multiplyVector(tri.v2, matCollection.getWorld())));
                    newTri.l3 = Vector.normalize(Vector.sub(lightPos, Mat4x4.multiplyVector(tri.v3, matCollection.getWorld())));
                    newTri.c1 = Vector.normalize(Vector.sub(cameraPos, Mat4x4.multiplyVector(tri.v1, matCollection.getWorld())));
                    newTri.c2 = Vector.normalize(Vector.sub(cameraPos, Mat4x4.multiplyVector(tri.v2, matCollection.getWorld())));
                    newTri.c3 = Vector.normalize(Vector.sub(cameraPos, Mat4x4.multiplyVector(tri.v3, matCollection.getWorld())));

                    toRaster.add(newTri);
                }
            }
        }
    }

    private void threadClipping(List<Triangle> toClipping, List<Triangle> toRaster){
        List<List<Triangle>> toClippings = partition(toClipping);
        List<List<Triangle>> toRasters = new ArrayList<>();

        for (int i = 0; i < THREAD_COUNT; i++) {
            toRasters.add(new ArrayList<>());
            List<Triangle> tempC = toClippings.get(i);
            List<Triangle> tempR = toRasters.get(i);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    clipping(tempC, tempR);
                }
            });
            threads.add(thread);
        }
        for(Thread thread : threads)
            thread.start();

        try {
            for(Thread thread : threads)
                thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        threads.clear();
        for (int i = 0; i < THREAD_COUNT; i++)
            toRaster.addAll(toRasters.get(i));
    }
    private void clipping(List<Triangle> toClipping, List<Triangle> toRaster){
        Triangle[] clipped = new Triangle[]{
                new Triangle(new Vector(0.d, 0.d, 0.d), new Vector(0.d, 0.d, 0.d), new Vector(0.d, 0.d, 0.d),null, null, null, null, null, null, null),
                new Triangle(new Vector(0.d, 0.d, 0.d), new Vector(0.d, 0.d, 0.d), new Vector(0.d, 0.d, 0.d),null, null, null, null, null, null, null)
        };

        Deque<Triangle> listTriangles = new ArrayDeque<>();
        for (Triangle triToClipping : toClipping) {

            listTriangles.addLast(triToClipping);

            int newTriangles = 1;

            for (int p = 0; p < 4; p++) {
                int trisToAdd = 0;
                while (newTriangles > 0) {

                    Triangle test = listTriangles.pop();
                    newTriangles--;

                    switch (p) {
                        case 0 ->
                                trisToAdd = Vector.countTriangleClipAgainstPlane(new Vector(0.d, 0.d, 0.d), new Vector(0.d, 1.d, 0.d), test, clipped[0], clipped[1]);
                        case 1 ->
                                trisToAdd = Vector.countTriangleClipAgainstPlane(new Vector(0.d, SCREEN_SIZE.getHeight() - 1, 0.d), new Vector(0.d, -1.d, 0.d), test, clipped[0], clipped[1]);
                        case 2 ->
                                trisToAdd = Vector.countTriangleClipAgainstPlane(new Vector(0.d, 0.d, 0.d), new Vector(1.d, 0.d, 0.d), test, clipped[0], clipped[1]);
                        case 3 ->
                                trisToAdd = Vector.countTriangleClipAgainstPlane(new Vector(SCREEN_SIZE.getWidth() - 1, 0.d, 0.d), new Vector(-1.d, 0.d, 0.d), test, clipped[0], clipped[1]);
                    }

                    if(trisToAdd == -1) {
                        trisToAdd = 1;
                        listTriangles.add(test);
                    }
                }
                newTriangles = listTriangles.size();
            }

            toRaster.addAll(listTriangles);

            listTriangles.clear();
        }
    }

    private boolean polygonRejection(Triangle triangle){
        Vector v1 = triangle.v1;
        Vector v2 = triangle.v2;
        Vector v3 = triangle.v3;

        Vector a = new Vector(v1.x - v3.x, v1.y - v3.y, 1);
        Vector b = new Vector(v2.x - v3.x, v2.y - v3.y, 1);
        // a90 = (-ay;ax)
        // a90 * b = by * ax - bx * ay

        return b.x * a.y - b.y * a.x > 0;
    }

    private void fillZBuffer(double[][] buffer){
        for(int x = 0; x < Z_BUFFER_WIDTH; x++){
            Arrays.fill(buffer[x], Double.MIN_VALUE);
        }
    }

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

    private Color phongShading(Vector light, Vector camera, Vector vn, Vector vt, Mat4x4 angle){



        Color triColor = new Color(colorMap.getRGB((int)vt.x, colorMap.getHeight() - (int)vt.y));
        Color normalColor = new Color(normalMap.getRGB((int)vt.x, normalMap.getHeight() - (int)vt.y));
        Color specularColor = new Color(specularMap.getRGB((int)vt.x, specularMap.getHeight() - (int)vt.y));
        Vector n = new Vector(normalColor.getRed() / 255.d * 2 - 1, normalColor.getGreen() / 255.d * 2 - 1, normalColor.getBlue() / 255.d * 2 - 1);
        n = Vector.normalize(Mat4x4.multiplyVector(n, angle));
        double spec = specularColor.getRed() / 255.d;
//        Color triColor = Color.white;
        double kFon = 0.7d;



        double kDiffuse = 1.d;
        double cos = Math.max(light.dot(n), 0.d);


        double kMirror = 0.8d, kShine = 30.d;
        Vector mirLight = Vector.sub(light, Vector.prod(n, 2 * light.dot(n)));
        double cosMir = spec * Math.pow(Math.max(0.d, mirLight.dot(camera)) , kShine);



//        return fon;
//        return diffuse;
//        return mirror;

        return Triangle.getShade(triColor,  cosMir + kFon + kDiffuse * cos);
    }




    private void rasterTriangle(BufferedImage img, Triangle triangle, Mat4x4 angle){
        Vector v1 = triangle.v1;
        Vector v2 = triangle.v2;
        Vector v3 = triangle.v3;

        int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
        int maxX = (int) Math.min(img.getWidth() - 1, Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
        int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
        int maxY = (int) Math.min(img.getHeight() - 1, Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));

        double triangleArea = (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x);


        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                double b1 = ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triangleArea;
                double b2 = ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triangleArea;
                double b3 = ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triangleArea;


                if (Double.compare(b1, 0.d) >= 0 && Double.compare(b2, 0.d) >= 0 && Double.compare(b3, 0.d) >= 0) {
                    double zDepth = v1.z * b1 + v2.z * b2 + v3.z * b3;

                    if(Double.compare(zDepth, zBuffer[x][y]) > 0) {
                        zBuffer[x][y] = zDepth;


                        Vector normir1 = v1;
                        Vector normir2 = v2;
                        Vector normir3 = v3;


                        double w = 1 / (normir1.w * b1 + normir2.w * b2 + normir3.w * b3);

                        Vector b = new Vector(b1 / v1.w, b2 / v2.w, b3 / v3.w);
                        b = Vector.div(b, b1 / v1.w + b2 / v2.w + b3 / v3.w);

//                        int textureX = (int) Math.floor((triangle.vt1.x * b1 / normir1.w + triangle.vt2.x * b2 / normir2.w + triangle.vt3.x * b3 / normir3.w) * w * colorMap.getWidth()-1);
//                        int textureY = (int) Math.floor((triangle.vt1.y * b1 / normir1.w + triangle.vt2.y * b2 / normir2.w + triangle.vt3.y * b3 / normir3.w) * w * colorMap.getHeight()-1);

                        int textureX = (int) Math.floor((triangle.vt1.x * b.x + triangle.vt2.x * b.y + triangle.vt3.x * b.z) * colorMap.getWidth()-1);
                        int textureY = (int) Math.floor((triangle.vt1.y * b.x + triangle.vt2.y * b.y + triangle.vt3.y * b.z) * colorMap.getHeight()-1);



                        Vector n = Vector.normalize(Vector.add(Vector.add(Vector.prod(triangle.vn1, b1), Vector.prod(triangle.vn2, b2)), Vector.prod(triangle.vn3, b3)));
                        Vector l = Vector.normalize(Vector.add(Vector.add(Vector.prod(triangle.l1, b1), Vector.prod(triangle.l2, b2)), Vector.prod(triangle.l3, b3)));
                        Vector c = Vector.normalize(Vector.add(Vector.add(Vector.prod(triangle.c1, b1), Vector.prod(triangle.c2, b2)), Vector.prod(triangle.c3, b3)));
                        img.setRGB(
                                x, y,
                                phongShading(
                                        l,
                                        c,
                                        n,
                                        new Vector(textureX, textureY, 0),
                                        angle
                                ).getRGB()
                        );
                    }
                }
            }
        }
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


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        int camSpeedMax = 50;
        if (e.getKeyCode() == KeyEvent.VK_DOWN){
            Vector forward = Vector.prod(upDir, 0.05d + camSpeed * 0.001d);
            modelPos = Vector.add(modelPos, forward);
            lightPos = Vector.add(lightPos, forward);
//            camera = Vector.sub(camera, forward);
//            target = Vector.sub(target, forward);
//            upTarget = Vector.sub(upTarget, forward);
//            rightTarget = Vector.sub(rightTarget, forward);
            if (camSpeed < camSpeedMax + 1)
                camSpeed++;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP){
            Vector forward = Vector.prod(upDir, 0.05d + camSpeed * 0.001d);
            modelPos = Vector.sub(modelPos, forward);
            lightPos = Vector.sub(lightPos, forward);
//            camera = Vector.add(camera, forward);
//            target = Vector.add(target, forward);
//            upTarget = Vector.add(upTarget, forward);
//            rightTarget = Vector.add(rightTarget, forward);
            if (camSpeed < camSpeedMax + 1)
                camSpeed++;

        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT){
            Vector forward = Vector.prod(rightDir, 0.05d + camSpeed * 0.001d);
            modelPos = Vector.add(modelPos, forward);
            lightPos = Vector.add(lightPos, forward);
//            camera = Vector.sub(camera, forward);
//            target = Vector.sub(target, forward);
//            upTarget = Vector.sub(upTarget, forward);
//            rightTarget = Vector.sub(rightTarget, forward);
            if (camSpeed < camSpeedMax + 1)
                camSpeed++;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT){
            Vector forward = Vector.prod(rightDir, 0.05d + camSpeed * 0.001d);
            modelPos = Vector.sub(modelPos, forward);
            lightPos = Vector.sub(lightPos, forward);
//            camera = Vector.add(camera, forward);
//            target = Vector.add(target, forward);
//            upTarget = Vector.add(upTarget, forward);
//            rightTarget = Vector.add(rightTarget, forward);
            if (camSpeed < camSpeedMax + 1)
                camSpeed++;
        }
        if (e.getKeyCode() == KeyEvent.VK_W){

            Vector forward = Vector.prod(lookDir, 0.05d + camSpeed * 0.001d);
            modelPos = Vector.add(modelPos, forward);
            lightPos = Vector.add(lightPos, forward);
//            camera = Vector.sub(camera, forward);
//            target = Vector.sub(target, forward);
//            upTarget = Vector.sub(upTarget, forward);
//            rightTarget = Vector.sub(rightTarget, forward);


            if (camSpeed < camSpeedMax + 1)
                camSpeed++;
        }
        if (e.getKeyCode() == KeyEvent.VK_S){
            Vector forward = Vector.prod(lookDir, 0.05d + camSpeed * 0.001d);
            modelPos = Vector.sub(modelPos, forward);
            lightPos = Vector.sub(lightPos, forward);
//            camera = Vector.add(camera, forward);
//            target = Vector.add(target, forward);
//            upTarget = Vector.add(upTarget, forward);
//            rightTarget = Vector.add(rightTarget, forward);


            if (camSpeed < camSpeedMax + 1)
                camSpeed++;
        }
        if (e.getKeyCode() == KeyEvent.VK_D){
            camAngleY += 0.00125d + camSpeed * 0.001d;
            if (camSpeed < camSpeedMax + 1)
                camSpeed++;
        }
        if (e.getKeyCode() == KeyEvent.VK_A){
            camAngleY -= 0.00125d + camSpeed * 0.001d;
            if (camSpeed < camSpeedMax + 1)
                camSpeed++;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        camSpeed = 1;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
