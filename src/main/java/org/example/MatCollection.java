package org.example;

public class MatCollection {
    private Mat4x4 angle;
    private Mat4x4 scale;
    private Mat4x4 transition;
    private Mat4x4 world;
    private Mat4x4 view;
    private Mat4x4 projection;
    private Mat4x4 viewport;
    private Mat4x4 finalView;
    private Mat4x4 finalProjection;
    private Mat4x4 finalViewport;
    public MatCollection(MatCollectionBuilder builder) {
        this.angle = builder.angle;
        this.scale = builder.scale;
        this.transition = builder.transition;
        this.view = builder.view;
        this.projection = builder.projection;
        this.viewport = builder.viewport;

        if(builder.world != null)
            this.world = builder.world;
        else if (this.angle != null && this.transition != null && this.scale != null)
            this.world = Mat4x4.multiplyMatrix(Mat4x4.multiplyMatrix(this.angle, this.scale), this.transition);
        else if (this.angle != null && this.transition != null)
            this.world = Mat4x4.multiplyMatrix(this.angle, this.transition);
        else
            this.world = null;

        if(builder.finalView != null)
            this.finalView = builder.finalView;
        else if (this.view != null && this.world != null)
            this.finalView = Mat4x4.multiplyMatrix(this.world, this.view);
        else
            this.finalView = null;

        if(builder.finalProjection != null)
            this.finalProjection = builder.finalProjection;
        else if (this.finalView != null && this.projection != null)
            this.finalProjection = Mat4x4.multiplyMatrix(this.finalView, this.projection);
        else
            this.finalProjection = null;

        if(builder.finalViewport != null)
            this.finalViewport = builder.finalViewport;
        else if (this.finalProjection != null && this.viewport != null)
            this.finalViewport = Mat4x4.multiplyMatrix(this.finalProjection, this.viewport);
        else
            this.finalViewport = null;

    }

    public Mat4x4 getAngle() {
        return angle;
    }

    public Mat4x4 getScale(){
        return scale;
    }

    public Mat4x4 getTransition() {
        return transition;
    }

    public Mat4x4 getWorld() {
        return world;
    }

    public Mat4x4 getView() {
        return view;
    }

    public Mat4x4 getProjection() {
        return projection;
    }

    public Mat4x4 getViewport() {
        return viewport;
    }

    public Mat4x4 getFinalView() {
        return finalView;
    }

    public Mat4x4 getFinalProjection() {
        return finalProjection;
    }

    public Mat4x4 getFinalViewport() {
        return finalViewport;
    }

    public static class MatCollectionBuilder {
        private Mat4x4 angle;
        private Mat4x4 scale;
        private Mat4x4 transition;
        private Mat4x4 world;
        private Mat4x4 view;
        private Mat4x4 projection;
        private Mat4x4 viewport;
        private Mat4x4 finalView;
        private Mat4x4 finalProjection;
        private Mat4x4 finalViewport;

        public MatCollectionBuilder() {
        }

        public MatCollectionBuilder setAngle(double angleX, double angleY, double angleZ) {
            Mat4x4 rx = Mat4x4.getRotationXMat(angleX);
            Mat4x4 ry = Mat4x4.getRotationYMat(angleY);
            Mat4x4 rz = Mat4x4.getRotationYMat(angleZ);
            this.angle = Mat4x4.multiplyMatrix(Mat4x4.multiplyMatrix(rx, ry), rz);
            return this;
        }

        public MatCollectionBuilder setScale(double scaleX, double scaleY, double scaleZ){
            this.scale = Mat4x4.getScaleMat(scaleX, scaleY, scaleZ);
            return this;
        }

        public MatCollectionBuilder setTransition(Vector pos) {
            this.transition = Mat4x4.getTranslationMat(pos.x, pos.y, pos.z);
            return this;
        }

        public MatCollectionBuilder setWorld(Mat4x4 world) {
            this.world = world;
            return this;
        }

        public MatCollectionBuilder setView(Mat4x4 view) {
            this.view = view;
            return this;
        }

        public MatCollectionBuilder setProjection(Mat4x4 projection) {
            this.projection = projection;
            return this;
        }

        public MatCollectionBuilder setViewport(Mat4x4 viewport) {
            this.viewport = viewport;
            return this;
        }

        public MatCollectionBuilder setFinalView(Mat4x4 finalView) {
            this.finalView = finalView;
            return this;
        }

        public MatCollectionBuilder setFinalProjection(Mat4x4 finalProjection) {
            this.finalProjection = finalProjection;
            return this;
        }

        public MatCollectionBuilder setFinalViewport(Mat4x4 finalViewport) {
            this.finalViewport = finalViewport;
            return this;
        }

        public MatCollection build() {
            return new MatCollection(this);
        }
    }
}
