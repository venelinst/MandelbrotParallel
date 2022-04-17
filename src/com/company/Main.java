package com.company;


public class Main {

    public static void main(String[] args) throws InterruptedException {

        double xb = -0.93, xe = -0.85, yb = -0.257;

        int cores = 4;

        int width = 1600;

        int height = width / 2;
        int granularity = 10;
        //java -classpath . com.company.Main "4" "1" "3600x1800" "-0.93x-0.257x-0.85"
        //cores = Runtime.getRuntime().availableProcessors();

        if (args.length == 4) {
            cores = Integer.parseInt(args[0]);
            granularity = Integer.parseInt(args[1]);
            String[] res = args[2].split("x");
            width = Integer.parseInt(res[0]);
            height = Integer.parseInt(res[1]);
            String[] location = args[3].split("x");
            xb = Double.parseDouble(location[0]);
            yb = Double.parseDouble(location[1]);
            xe = Double.parseDouble(location[2]);
        }

        Mandelbrot m = new Mandelbrot(xb, yb, xe, cores, granularity, width, height);
        m.render();
    }
}
