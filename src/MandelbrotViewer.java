/**
 * MandelbrotViewer.java - A simple Mandelbrot set viewer using Swing. The
 * Mandelbrot set is a fractal that is defined by the following equation: z = z^2 + c
 * where z and c are complex numbers. The Mandelbrot set is the set of complex numbers c
 * for which the function does not diverge when iterated from z = 0. The viewer allows the
 * user to zoom in and out of the set, as well as pan around the set. The user can also
 * change the color scheme of the set.
 * Description: This program displays a Mandelbrot set viewer using Swing. The user can zoom
 * in and out of the set, as well as pan around the set. The user can also change the color scheme
 * of the set.
 * @author  Khosraw Azizi
 * @version 1.0
 * @since 2023-03-23
 * @see MandelbrotPanel
 * @see MandelbrotPanel.ColorScheme
 * @see <a href="https://en.wikipedia.org/wiki/Mandelbrot_set">Mandelbrot Set</a>
 * @see <a href="https://en.wikipedia.org/wiki/Complex_number">Complex Number</a>
 * @see <a href="https://en.wikipedia.org/wiki/Fractal">Fractal</a>
 * @see <a href="https://en.wikipedia.org/wiki/Julia_set">Julia Set</a>
 * @see <a href="https://en.wikipedia.org/wiki/Newton_fractal">Newton Fractal</a>
 * @see <a href="https://en.wikipedia.org/wiki/Tricorn">Tricorn</a>
 * @see <a href="https://en.wikipedia.org/wiki/Burning_Ship_fractal">Burning Ship Fractal</a>
 * @see <a href="https://en.wikipedia.org/wiki/Mandelbar">Mandelbar</a>
 * @see <a href="https://en.wikipedia.org/wiki/Mandelbrot_set#Mandelbrot_set">Mandelbrot Set</a>
 * @see <a href="https://en.wikipedia.org/wiki/Mandelbrot_set#Julia_set">Julia Set</a>
 * @see <a href="https://en.wikipedia.org/wiki/Mandelbrot_set#Newton_fractal">Newton Fractal</a>
 * @see <a href="https://en.wikipedia.org/wiki/Mandelbrot_set#Tricorn">Tricorn</a>
 * @see <a href="https://en.wikipedia.org/wiki/Mandelbrot_set#Burning_Ship_fractal">Burning Ship Fractal</a>
 * @see <a href="https://en.wikipedia.org/wiki/Mandelbrot_set#Mandelbar">Mandelbar</a>
 * @see <a href="https://en.wikipedia.org/wiki/Mandelbrot_set#Mandelbrot_set">Mandelbrot Set</a>
 * @see <a href="https://en.wikipedia.org/wiki/Mandelbrot_set#Julia_set">Julia Set</a>
 * @see <a href="https://en.wikipedia.org/wiki/Mandelbrot_set#Newton_fractal">Newton Fractal</a>
 * @see <a href="https://en.wikipedia.org/wiki/Mandelbrot_set#Tricorn">Tricorn</a>
 * @see <a href="https://en.wikipedia.org/wiki/Mandelbrot_set#Burning_Ship_fractal">Burning Ship Fractal</a>
 * @see <a href="https://en.wikipedia.org/wiki/Mandelbrot_set#Mandelbar">Mandelbar</a>
 * @see <a href="https://en.wikipedia.org/wiki/Mandelbrot_set#Mandelbrot_set">Mandelbrot Set</a>
 * @see <a href="https://en.wikipedia.org/wiki/Mandelbrot_set#Julia_set">Julia Set</a>
 * @bug No known bugs.
 * @keywords Mandelbrot set, fractal, complex number, viewer, zoom, pan, color scheme
 * @license MIT License
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class MandelbrotViewer extends JFrame {
    // The last mouse position when the user pressed the mouse button.
    private static Point lastMousePosition;

    // The color schemes that can be used to color the Mandelbrot set.
    private static final MandelbrotPanel.ColorScheme[] COLOR_SCHEMES = MandelbrotPanel.ColorScheme.values();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MandelbrotViewer viewer = new MandelbrotViewer();
            viewer.setVisible(true);
        });
    }

    // Creates a new Mandelbrot set viewer.
    public MandelbrotViewer() {
        setTitle("Mandelbrot Set Viewer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);

        // Create the Mandelbrot set panel.
        MandelbrotPanel panel = new MandelbrotPanel();
        add(panel, BorderLayout.CENTER);

        // Create the control panel.
        JComboBox<MandelbrotPanel.ColorScheme> colorSchemeComboBox = createColorSchemeComboBox(panel);
        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Color scheme: "));
        controlPanel.add(colorSchemeComboBox);
        add(controlPanel, BorderLayout.NORTH);

        // Create the slider for the number of iterations.
        JSlider slider = createIterationsSlider(panel);
        add(slider, BorderLayout.SOUTH);
    }

    /**
     * Creates a combo box that allows the user to select a color scheme.
     * @param panel The panel that displays the Mandelbrot set.
     * @return The combo box.
     */
    private JComboBox<MandelbrotPanel.ColorScheme> createColorSchemeComboBox(MandelbrotPanel panel) {
        JComboBox<MandelbrotPanel.ColorScheme> comboBox = new JComboBox<>(COLOR_SCHEMES);

        comboBox.addActionListener(e -> {
            JComboBox<MandelbrotPanel.ColorScheme> source = (JComboBox<MandelbrotPanel.ColorScheme>) e.getSource();
            MandelbrotPanel.ColorScheme colorScheme = (MandelbrotPanel.ColorScheme) source.getSelectedItem();
            panel.setColorScheme(colorScheme);
            panel.repaint();
        });

        return comboBox;
    }

    /**
     * Creates a slider that allows the user to select the number of iterations.
     * @param panel The panel that displays the Mandelbrot set.
     * @return The slider.
     */
    private JSlider createIterationsSlider(MandelbrotPanel panel) {
        int minIterations = 25;
        int maxIterations = 10000;
        int initialIterations = 250;

        JSlider slider = new JSlider(JSlider.HORIZONTAL, minIterations, maxIterations, initialIterations);
        slider.setMajorTickSpacing(1000);
        slider.setMinorTickSpacing(100);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        slider.addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
            if (!source.getValueIsAdjusting()) {
                int newIterations = source.getValue();
                panel.setMaxIterations(newIterations);
            }
        });

        return slider;
    }

    // The panel that displays the Mandelbrot set.
    static class MandelbrotPanel extends JPanel {
        // setting the initial values for the Mandelbrot set viewer (zoom, offset, iterations)
        private double zoom = 250.0;
        private double offsetX = -1.0;
        private double offsetY = 0.0;
        private int maxIterations = 250;

        private ColorScheme colorScheme = ColorScheme.RED;

        // The color schemes that can be used to color the Mandelbrot set.
        private enum ColorScheme {
            RED(Color.RED),
            BLUE(Color.BLUE),
            GREEN(Color.GREEN),
            PURPLE(new Color(128, 0, 128)),
            ORANGE(new Color(255, 165, 0));

            private final Color color;

            ColorScheme(Color color) {
                this.color = color;
            }

            public Color getColor() {
                return color;
            }

            @Override
            public String toString() {
                return name().toLowerCase();
            }
        }

        /**
         * Sets the maximum number of iterations.
         * @param colorScheme The new color scheme.
         */
        public void setColorScheme(ColorScheme colorScheme) {
            this.colorScheme = colorScheme;
        }

        public MandelbrotPanel() {
            // Mouse wheel zooms in and out.
            addMouseWheelListener(new MouseAdapter() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    double factor = 1.1;
                    zoom *= (e.getWheelRotation() < 0) ? factor : 1.0 / factor;
                    repaint();
                }
            });

                // Mouse drag pans the image.
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        lastMousePosition = e.getPoint();
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        lastMousePosition = null;
                    }
            });

            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    // If the user is dragging the mouse, pan the image.
                    if (lastMousePosition != null) {
                        // Calculate the amount to pan the image.
                        // Equation: delta = (new position - old position) / zoom
                        // Explanation: The new position is calculated by subtracting the offset from the mouse position.
                        // This works because the offset is the distance from the center of the image to the top left corner.
                        // The old position is calculated by subtracting the offset from the last mouse position.
                        double deltaX = (e.getX() - lastMousePosition.x) / zoom;
                        double deltaY = (e.getY() - lastMousePosition.y) / zoom;
                        offsetX -= deltaX;
                        offsetY -= deltaY;
                        lastMousePosition = e.getPoint();
                        repaint();
                    }
                }
            });
        }

        /**
         * Sets the maximum number of iterations to use when calculating the Mandelbrot set.
         * @param maxIterations the maximum number of iterations to use when calculating the Mandelbrot set.
         */
        public void setMaxIterations(int maxIterations) {
            this.maxIterations = maxIterations;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            // Paint the background.
            super.paintComponent(graphics);

            // Create the image.
            BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

            int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
            int width = image.getWidth();
            int height = image.getHeight();

            // Calculate the Mandelbrot set.
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    double zx = (x - width / 2.0) / zoom + offsetX;
                    double zy = (y - height / 2.0) / zoom + offsetY;

                    double cX = zx;
                    double cY = zy;
                    int iter = 0;

                    // interate until the point is outside the set or the maximum number of iterations is reached
                    // z = z^2 + c - the Mandelbrot set is the set of points that do not diverge when iterated
                    // over the function z = z^2 + c (where z and c are complex numbers) will be inside the set
                    // colored black, and points that do diverge will be colored according to the color scheme
                    while (zx * zx + zy * zy < 4 && iter < maxIterations) {
                        double tmp = zx * zx - zy * zy + cX;
                        zy = 2.0 * zx * zy + cY;
                        zx = tmp;
                        iter++;
                    }

                    if (iter == maxIterations) {
                        // set the pixel to black for points inside the set
                        pixels[x + y * width] = 0;
                    } else {
                        // use the selected color scheme for points outside the set
                        Color color = colorScheme.getColor();
                        int colorValue = (int) (255.0 * iter / maxIterations);
                        int r = color.getRed() * colorValue / 255;
                        int g = color.getGreen() * colorValue / 255;
                        int b = color.getBlue() * colorValue / 255;
                        int rgb = (r << 16) | (g << 8) | b;
                        pixels[x + y * width] = rgb;
                    }
                }
            }

            graphics.drawImage(image, 0, 0, null);
        }

    }
}
