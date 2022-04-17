package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MandelbrotVisualiser extends JFrame {
    private final int rows;
    private final int cols;
    private final int[] matrix;

    public MandelbrotVisualiser(int[] image, int rows, int cols) {

        super("Mandelbrot set");
        this.rows = rows;
        this.cols = cols;
        matrix = new int[rows * cols];
        setSize(cols, rows);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        BufferedImage renderImage = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_BGR);
        renderImage.setRGB(0, 0, renderImage.getWidth(), renderImage.getHeight(), image, 0, renderImage.getWidth());

        add(new GraphicsPanel(renderImage));
        pack();
    }

    class GraphicsPanel extends JPanel {

        private BufferedImage image;

        public GraphicsPanel(BufferedImage image) {
            this.image = image;
            setPreferredSize(new Dimension(MandelbrotVisualiser.WIDTH, MandelbrotVisualiser.HEIGHT));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, null);
        }
    }
}
