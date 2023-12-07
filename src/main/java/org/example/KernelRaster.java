package org.example;

import com.aparapi.Kernel;
import com.aparapi.Range;

import java.time.Instant;
import java.util.Arrays;

public class KernelRaster extends Kernel {

//    Instant start2 = Instant.now();
//    double[] xyzArr = new double[toRaster.size()*9];
//    double[] tArr = new double[toRaster.size()*9];
//    double[] wArr = new double[toRaster.size()*3];
//    double[] nArr = new double[toRaster.size()*9];
//    double[] lArr = new double[toRaster.size()*9];
//    double[] cArr = new double[toRaster.size()*9];
//    int[] finImgArr = new int[SCREEN_SIZE.width * SCREEN_SIZE.height];
//    int[] imgCol = new int[colorMap.getWidth()*colorMap.getHeight()];
//    int[] nCol = new int[normalMap.getWidth()*normalMap.getHeight()];
//    int[] specCol = new int[specularMap.getWidth()*specularMap.getHeight()];
//        Arrays.fill(finImgArr, 0);
//    double[] angleMat = new double[16];
//    double[] zb = new double[Z_BUFFER_WIDTH* Z_BUFFER_HEIGHT];
//        Arrays.fill(zb, Double.MIN_VALUE);
//    int wSize = toRaster.size()*3;
//    int tSize = toRaster.size()*9;
//    Vector[] vv = new Vector[toRaster.size()*3];
//        for (int i = 0; i < wSize; i = i+3){
//        int ii = i/3;
//        wArr[i] = toRaster.get(ii).v1.w;
//        wArr[i+1] = toRaster.get(ii).v2.w;
//        wArr[i+2] = toRaster.get(ii).v3.w;
//        vv[i] = toRaster.get(ii).v1;
//        vv[i+1] = toRaster.get(ii).v2;
//        vv[i+2] = toRaster.get(ii).v3;
//
//    }
//        for (int i = 0; i < 4; i++){
//        for (int j = 0; j < 4; j++){
//            angleMat[i * 4 + j] = matCollection.getAngle().v[i][j];
//        }
//    }
//    int mapWidth = colorMap.getWidth();
//    int mapHeight = colorMap.getHeight();
//        for (int i = 0; i < mapHeight; i++){
//        for (int j = 0; j < mapWidth; j++){
//            imgCol[i *mapHeight + j] = colorMap.getRGB(j,i);
//            nCol[i *mapHeight + j] = normalMap.getRGB(j,i);
//            specCol[i *mapHeight + j] = specularMap.getRGB(j,i);
//        }
//    }
//
//        for (int i = 0; i < tSize; i = i+9){
//        int ii = i/9;
//        xyzArr[i] = toRaster.get(ii).v1.x;
//        xyzArr[i+1] = toRaster.get(ii).v1.y;
//        xyzArr[i+2] = toRaster.get(ii).v1.z;
//        xyzArr[i+3] = toRaster.get(ii).v2.x;
//        xyzArr[i+4] = toRaster.get(ii).v2.y;
//        xyzArr[i+5] = toRaster.get(ii).v2.z;
//        xyzArr[i+6] = toRaster.get(ii).v3.x;
//        xyzArr[i+7] = toRaster.get(ii).v3.y;
//        xyzArr[i+8] = toRaster.get(ii).v3.z;
//
//        tArr[i] = toRaster.get(ii).vt1.x;
//        tArr[i+1] = toRaster.get(ii).vt1.y;
//        tArr[i+2] = toRaster.get(ii).vt1.z;
//        tArr[i+3] = toRaster.get(ii).vt2.x;
//        tArr[i+4] = toRaster.get(ii).vt2.y;
//        tArr[i+5] = toRaster.get(ii).vt2.z;
//        tArr[i+6] = toRaster.get(ii).vt3.x;
//        tArr[i+7] = toRaster.get(ii).vt3.y;
//        tArr[i+8] = toRaster.get(ii).vt3.z;
//
//        nArr[i] = toRaster.get(ii).vn1.x;
//        nArr[i+1] = toRaster.get(ii).vn1.y;
//        nArr[i+2] = toRaster.get(ii).vn1.z;
//        nArr[i+3] = toRaster.get(ii).vn2.x;
//        nArr[i+4] = toRaster.get(ii).vn2.y;
//        nArr[i+5] = toRaster.get(ii).vn2.z;
//        nArr[i+6] = toRaster.get(ii).vn3.x;
//        nArr[i+7] = toRaster.get(ii).vn3.y;
//        nArr[i+8] = toRaster.get(ii).vn3.z;
//
//        lArr[i] = toRaster.get(ii).l1.x;
//        lArr[i+1] = toRaster.get(ii).l1.y;
//        lArr[i+2] = toRaster.get(ii).l1.z;
//        lArr[i+3] = toRaster.get(ii).l2.x;
//        lArr[i+4] = toRaster.get(ii).l2.y;
//        lArr[i+5] = toRaster.get(ii).l2.z;
//        lArr[i+6] = toRaster.get(ii).l3.x;
//        lArr[i+7] = toRaster.get(ii).l3.y;
//        lArr[i+8] = toRaster.get(ii).l3.z;
//
//        cArr[i] = toRaster.get(ii).c1.x;
//        cArr[i+1] = toRaster.get(ii).c1.y;
//        cArr[i+2] = toRaster.get(ii).c1.z;
//        cArr[i+3] = toRaster.get(ii).c2.x;
//        cArr[i+4] = toRaster.get(ii).c2.y;
//        cArr[i+5] = toRaster.get(ii).c2.z;
//        cArr[i+6] = toRaster.get(ii).c3.x;
//        cArr[i+7] = toRaster.get(ii).c3.y;
//        cArr[i+8] = toRaster.get(ii).c3.z;
//    }
//    int zbWidth = Z_BUFFER_WIDTH;
//    int scrWidth = SCREEN_SIZE.width;
//    int scrHeight = SCREEN_SIZE.height;
//    Instant finish2 = Instant.now();
//
//    Kernel k = new Kernel() {
//        private int kernelShading(int col, int nCol, int specCol, double[] angle, double lx, double ly, double lz, double cx, double cy, double cz){
//
//            int mask = 0xFF;
//
//            double nx = ((nCol >> 16) & mask) / 255.d * 2 - 1;
//            double ny = ((nCol >> 8) & mask) / 255.d * 2 - 1;
//            double nz = (nCol & mask) / 255.d * 2 - 1;
//
//            nx = nx * angle[0] + ny * angle[4] + nz * angle[8];
//            ny = nx * angle[1] + ny * angle[5] + nz * angle[9];
//            nz = nx * angle[2] + ny * angle[6] + nz * angle[10];
//
////        double lengthN = Math.sqrt((nx * nx) + (ny * ny) + (nz * nz));
//            double lengthN = 1;
//            nx = nx / lengthN;
//            ny = ny / lengthN;
//            nz = nz / lengthN;
//
//
//            double spec = (specCol & mask) / 255.d;
//            double kFon = 0.1d;
//
//
//
//            double kDiffuse = 1.d;
//            double lightDot =  lx * nx + ly * ny + lz * nz;
//            double cos = Math.max(lightDot, 0.d);
//
//
//            double kMirror = 0.8d, kShine = 30.d;
//
//            double mx = lx - (nx * lightDot * 2);
//            double my = ly - (ny * lightDot * 2);
//            double mz = lz - (nz * lightDot * 2);
//
//            double mirLightDot =  mx * cx + my * cy + mz * cz;
//            double cosMir = spec * Math.pow(Math.max(0.d, mirLightDot) , kShine);
////                double cosMir = 0;
//            double shade = cosMir + kFon + kDiffuse * cos;
//
//            int red = (int) Math.min(255.d, ((col >> 16) & mask) * shade);
//            int green = (int) Math.min(255.d, ((col >> 8) & mask) * shade);
//            int blue = (int) Math.min(255.d, (col & mask) * shade);
//
//            int res = red << 16;
//            res += green << 8;
//            res += blue;
////                int res =0;
//
//            return res;
//        }
//
//        private void kernelRaster(double[] angle, int i, int[] img, int screen_width, int[] imgCol, int[] imgN, int[] imgSpec, int width, int height, double[] zb, int bufWidth, double[] tri, int tLength, double[] tArr, double[] wArr, double[] nArr, double[] lArr, double[] cArr){
//
//
////                    for (int tInd = 0; tInd < tLength; tInd= tInd + 9){
////                        double triangleArea = (tri[tInd+1] - tri[tInd+7]) * (tri[tInd+3] - tri[tInd+6]) + (tri[tInd+4] - tri[tInd+7]) * (tri[tInd+6] - tri[tInd]);
////
////                        int y = i / screen_width;
////                        int x = i % screen_width;
////
////                        double b1 = ((y - tri[tInd+7]) * (tri[tInd+3] - tri[tInd+6]) + (tri[tInd+4] - tri[tInd+7]) * (tri[tInd+6] - x)) / triangleArea;
////                        double b2 = ((y - tri[tInd+1]) * (tri[tInd+6] - tri[tInd]) + (tri[tInd+7] - tri[tInd+1]) * (tri[tInd] - x)) / triangleArea;
////                        double b3 = ((y - tri[tInd+4]) * (tri[tInd] - tri[tInd+3]) + (tri[tInd+1] - tri[tInd+4]) * (tri[tInd+3] - x)) / triangleArea;
////
////                        if (b1 >= 0 && b2 >= 0 && b3 >= 0) {
////                            double zDepth = tri[tInd+2] * b1 + tri[tInd+5] * b2 + tri[tInd+8] * b3;
////                            if(zDepth > zb[x + y * bufWidth]) {
////                                zb[x + y * bufWidth] = zDepth;
////
////                                int ind = tInd / 3;
////                                double w = 1 / (wArr[ind] * b1 + wArr[ind+1] * b2 + wArr[ind+2] * b3);
////
////                                int tx = (int) Math.floor((tArr[tInd] * b1 / wArr[ind] + tArr[tInd+3] * b2 / wArr[ind+1] + tArr[tInd+6] * b3 / wArr[ind+2]) * w * width-1);
////                                int ty = (int) Math.floor((tArr[tInd+1] * b1 / wArr[ind] + tArr[tInd+4] * b2 / wArr[ind+1] + tArr[tInd+7] * b3 / wArr[ind+2]) * w * height-1);
////
////                                double nx = nArr[tInd] * b1 + nArr[tInd+3] * b2 + nArr[tInd+6] * b3;
////                                double ny = nArr[tInd+1] * b1 + nArr[tInd+4] * b2 + nArr[tInd+7] * b3;
////                                double nz = nArr[tInd+2] * b1 + nArr[tInd+5] * b2 + nArr[tInd+8] * b3;
////                                double lengthN = Math.sqrt((nx * nx) + (ny * ny) + (nz * nz));
//////                                double lengthN = 1;
////                                nx = nx / lengthN;
////                                ny = ny / lengthN;
////                                nz = nz / lengthN;
////
////                                double lx = lArr[tInd] * b1 + lArr[tInd+3] * b2 + lArr[tInd+6] * b3;
////                                double ly = lArr[tInd+1] * b1 + lArr[tInd+4] * b2 + lArr[tInd+7] * b3;
////                                double lz = lArr[tInd+2] * b1 + lArr[tInd+5] * b2 + lArr[tInd+8] * b3;
////                                double lengthL = Math.sqrt((lx * lx) + (ly * ly) + (lz * lz));
//////                                double lengthL = 1;
////                                lx = lx / lengthL;
////                                ly = ly / lengthL;
////                                lz = lz / lengthL;
////
////                                double cx = cArr[tInd] * b1 + cArr[tInd+3] * b2 + cArr[tInd+6] * b3;
////                                double cy = cArr[tInd+1] * b1 + cArr[tInd+4] * b2 + cArr[tInd+7] * b3;
////                                double cz = cArr[tInd+2] * b1 + cArr[tInd+5] * b2 + cArr[tInd+8] * b3;
////                                double lengthC = Math.sqrt((cx * cx) + (cy * cy) + (cz * cz));
//////                                double lengthC = 1;
////                                cx = cx / lengthC;
////                                cy = cy / lengthC;
////                                cz = cz / lengthC;
////
////                                int triColor = imgCol[ty*(height - ty) + tx];
////                                int normalColor = imgN[ty*(height - ty) + tx];
////                                int specularColor = imgSpec[ty*(height - ty) + tx];
////
////                                int resCol = kernelShading(triColor, normalColor, specularColor, angle, lx, ly, lz, cx, cy, cz);
////                                img[i] = resCol;
////                            }
////                        }
////                    }
////                }
//
//        }
//        @Override
//        public void run() {
////                kernelRaster(angleMat, getGlobalId(0), finImgArr, scrWidth,  imgCol, nCol, specCol, mapWidth, mapHeight, zb, zbWidth, xyzArr, tSize, tArr, wArr, nArr, lArr, cArr);
//            int i = getGlobalId();
//            for (int tInd = 0; tInd < tSize; tInd= tInd + 9){
//                double triangleArea = (xyzArr[tInd+1] - xyzArr[tInd+7]) * (xyzArr[tInd+3] - xyzArr[tInd+6]) + (xyzArr[tInd+4] - xyzArr[tInd+7]) * (xyzArr[tInd+6] - xyzArr[tInd]);
//
//                int y = i / scrWidth;
//                int x = i % scrWidth;
//
//                double b1 = ((y - xyzArr[tInd+7]) * (xyzArr[tInd+3] - xyzArr[tInd+6]) + (xyzArr[tInd+4] - xyzArr[tInd+7]) * (xyzArr[tInd+6] - x)) / triangleArea;
//                double b2 = ((y - xyzArr[tInd+1]) * (xyzArr[tInd+6] - xyzArr[tInd]) + (xyzArr[tInd+7] - xyzArr[tInd+1]) * (xyzArr[tInd] - x)) / triangleArea;
//                double b3 = ((y - xyzArr[tInd+4]) * (xyzArr[tInd] - xyzArr[tInd+3]) + (xyzArr[tInd+1] - xyzArr[tInd+4]) * (xyzArr[tInd+3] - x)) / triangleArea;
//
//                if (b1 >= 0 && b2 >= 0 && b3 >= 0) {
//                    double zDepth = xyzArr[tInd+2] * b1 + xyzArr[tInd+5] * b2 + xyzArr[tInd+8] * b3;
//                    if(zDepth > zb[x + y * zbWidth]) {
//                        zb[x + y * zbWidth] = zDepth;
//
//                        int ind = tInd / 3;
//                        double w = 1 / (vv[ind].w * b1 + wArr[ind+1] * b2 + wArr[ind+2] * b3);
//
//                        int tx = (int) Math.floor((tArr[tInd] * b1 / wArr[ind] + tArr[tInd+3] * b2 / wArr[ind+1] + tArr[tInd+6] * b3 / wArr[ind+2]) * w * mapWidth-1);
//                        int ty = (int) Math.floor((tArr[tInd+1] * b1 / wArr[ind] + tArr[tInd+4] * b2 / wArr[ind+1] + tArr[tInd+7] * b3 / wArr[ind+2]) * w * mapHeight-1);
//
//                        double nx = nArr[tInd] * b1 + nArr[tInd+3] * b2 + nArr[tInd+6] * b3;
//                        double ny = nArr[tInd+1] * b1 + nArr[tInd+4] * b2 + nArr[tInd+7] * b3;
//                        double nz = nArr[tInd+2] * b1 + nArr[tInd+5] * b2 + nArr[tInd+8] * b3;
//                        double lengthN = Math.sqrt((nx * nx) + (ny * ny) + (nz * nz));
////                                double lengthN = 1;
//                        nx = nx / lengthN;
//                        ny = ny / lengthN;
//                        nz = nz / lengthN;
//
//                        double lx = lArr[tInd] * b1 + lArr[tInd+3] * b2 + lArr[tInd+6] * b3;
//                        double ly = lArr[tInd+1] * b1 + lArr[tInd+4] * b2 + lArr[tInd+7] * b3;
//                        double lz = lArr[tInd+2] * b1 + lArr[tInd+5] * b2 + lArr[tInd+8] * b3;
//                        double lengthL = Math.sqrt((lx * lx) + (ly * ly) + (lz * lz));
////                                double lengthL = 1;
//                        lx = lx / lengthL;
//                        ly = ly / lengthL;
//                        lz = lz / lengthL;
//
//                        double cx = cArr[tInd] * b1 + cArr[tInd+3] * b2 + cArr[tInd+6] * b3;
//                        double cy = cArr[tInd+1] * b1 + cArr[tInd+4] * b2 + cArr[tInd+7] * b3;
//                        double cz = cArr[tInd+2] * b1 + cArr[tInd+5] * b2 + cArr[tInd+8] * b3;
//                        double lengthC = Math.sqrt((cx * cx) + (cy * cy) + (cz * cz));
////                                double lengthC = 1;
//                        cx = cx / lengthC;
//                        cy = cy / lengthC;
//                        cz = cz / lengthC;
//
//                        int triColor = imgCol[ty*(mapHeight - ty) + tx];
//                        int normalColor = nCol[ty*(mapHeight - ty) + tx];
//                        int specularColor = specCol[ty*(mapHeight - ty) + tx];
//
//                        int resCol = kernelShading(triColor, normalColor, specularColor, angleMat, lx, ly, lz, cx, cy, cz);
//                        finImgArr[i] = resCol;
//                    }
//                }
//            }
//        }
//
//    };
//
//        k.execute(Range.create(scrWidth*scrHeight));
//        k.dispose();
//        for (int i = 0; i < scrHeight; i++){
//        for (int j = 0; j < scrWidth; j++){
//            img.setRGB(j,i,finImgArr[i*scrWidth + j]);
//        }
//    }

    @Override
    public void run() {

    }
}
