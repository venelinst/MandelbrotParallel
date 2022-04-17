package com.company;

import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Mandelbrot {
    public final static int maxIterations = 256;
    public final int rows;
    public final int cols;
    private final int[] matrix;
    private final double xBegin;
    private final double yBegin;
    private final double xStep;
    private final double yStep;
    private final int cores;
    private final int granularity;
    private final int chunksize;
    private final boolean visualise = true;
    BufferedImage renderImage = null;


    public Mandelbrot(double xBegin, double yBegin, double xEnd, int cores, int granularity, int width, int height) {
        rows = height;
        cols = width;
        matrix = new int[rows * cols];
        this.xBegin = xBegin;
        this.yBegin = yBegin;
        this.cores = cores;
        this.granularity = granularity;
        chunksize = rows / (cores * granularity);
        double yEnd = yBegin + (xEnd - xBegin) * ((1.0 * rows) / cols);

        this.xStep = (xEnd - xBegin) / cols;
        this.yStep = (yEnd - yBegin) / rows;
    }

    public void render() {

        long startTime = System.currentTimeMillis();

        ExecutorService executor = null;
        if (cores > 1) {
            executor = Executors.newFixedThreadPool(cores - 1);
            for (int i = 1; i < cores; i++) {
                executor.submit(new MandelbrotWorker(rows, cols, matrix, xBegin, yBegin, xStep, yStep, i, cores, granularity));
            }
        }

        for (int i = 0; i < granularity; i++) {
            for (int r = 0; r < chunksize; r++) {
                int row = i * cores * chunksize + r;
                int add = cols * row;

                for (int j = 0; j < cols; j++) {
                    matrix[add + j] = maxIterations-calculatePoint(xBegin + j * xStep, yBegin + row * yStep, maxIterations);

                }
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Thread number " + 0 + " has finished " + (endTime - startTime));


        if (cores > 1) {
            executor.shutdown();
            try {
                executor.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        endTime = System.currentTimeMillis();
        System.out.println("All threads have finished in time " + (endTime - startTime));

        if (visualise) {
            MandelbrotVisualiser visual = new MandelbrotVisualiser(matrix, rows, cols);
        }
    }

    public static int calculatePoint(double x, double y, int maxIter) {
        double x0 = x;
        double y0 = y;
        int iteration = 0;

        // compute sequence terms until one "escapes"
        while (x*x + y*y < 4 && iteration < maxIter) {
            double xt = x*x - y*y + x0;
            double yt = 2*x*y + y0;

            x = xt;
            y = yt;

            iteration += 1;
        }

        return iteration;
        /*Complex z = z0;
        for (int t = 0; t < maxIter; t++) {
            if (z.abs() > 2.0) return t;
            z = z.times(z).plus(z0);
        }
        return maxIter;*/
    }
}
