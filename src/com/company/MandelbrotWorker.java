package com.company;

public class MandelbrotWorker implements Runnable {
    private final int maxIterations = Mandelbrot.maxIterations;
    private final int rows;
    private final int cols;
    private final int[] matrix;
    private final double xBegin;
    private final double yBegin;
    private final double xStep;
    private final double yStep;
    private final int index;
    private final int cores;
    private final int granularity;
    private final int chunksize;

    public MandelbrotWorker(int rows, int cols, int[] matrix, double xBegin, double yBegin, double xStep, double yStep, int index, int cores, int granularity) {
        this.rows = rows;
        this.cols = cols;
        this.matrix = matrix;
        this.xBegin = xBegin;
        this.yBegin = yBegin;
        this.xStep = xStep;
        this.yStep = yStep;
        this.index = index;
        this.cores = cores;
        this.granularity = granularity;
        chunksize = rows / (cores * granularity);
    }

    public static int calculatePoint(double x, double y, int maxIter) {
        double x0 = x;
        double y0 = y;
        int iteration = 0;


        while (x*x + y*y < 4 && iteration < maxIter) {
            double xt = x*x - y*y + x0;
            double yt = 2*x*y + y0;

            x = xt;
            y = yt;

            iteration += 1;
        }

        return iteration;
    }

    @Override
    public void run() {

        long before = System.currentTimeMillis();

        for (int i = 0; i < granularity; i++) {
            for (int r = 0; r < chunksize; r++) {
                int row = (i * cores + index)* chunksize + r;
                int add = cols * row;
                for (int j = 0; j < cols; j++) {
                    matrix[add + j] = maxIterations-calculatePoint(xBegin + j * xStep, yBegin + row * yStep, maxIterations);

                }
            }
        }
        long after = System.currentTimeMillis();
        System.out.println("Thread number " + index + " has finished " + (after - before));
    }
}
