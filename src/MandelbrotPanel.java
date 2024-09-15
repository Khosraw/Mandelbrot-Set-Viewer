import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MandelbrotPanel extends JPanel {

    // Executor service for multithreading
    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    // Setting the initial values for the fractal viewer (zoom, offset, iterations)
    private double zoom = 250.0;
    private double offsetX = -1.0;
    private double offsetY = 0.0;
    private int maxIterations = 250;
    private ColorScheme colorScheme = ColorScheme.RED;
    // Variables to handle panning
    private Point lastMousePosition;

    // BufferedImage to store the fractal image
    private BufferedImage fractalImage;

    // Flag to indicate if a computation is in progress
    private volatile boolean isComputing = false;

    // Progress bar
    private JProgressBar progressBar;
    private FractalType fractalType = FractalType.MANDELBROT;
    // Julia set parameters
    private double juliaCReal = -0.4;
    private double juliaCImag = 0.6;
    public MandelbrotPanel() {
        setPreferredSize(new Dimension(800, 800));

        // Mouse wheel zooms in and out.
        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double factor = 1.1;
                zoom *= (e.getWheelRotation() < 0) ? factor : 1.0 / factor;
                computeFractal();
            }
        });

        // Mouse click selects Julia parameters when in Mandelbrot mode
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastMousePosition = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                lastMousePosition = null;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (fractalType == FractalType.MANDELBROT && SwingUtilities.isLeftMouseButton(e)) {
                    // Only switch to Julia mode on a single click without dragging
                    if (e.getClickCount() == 1) {
                        // Convert pixel coordinates to complex plane
                        double cX = (e.getX() - getWidth() / 2.0) / zoom + offsetX;
                        double cY = (e.getY() - getHeight() / 2.0) / zoom + offsetY;
                        setJuliaCReal(cX);
                        setJuliaCImag(cY);
                        setFractalType(FractalType.JULIA);
                    }
                }
            }
        });

        // Mouse drag pans the image.
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // If the user is dragging the mouse, pan the image.
                if (lastMousePosition != null) {
                    // Calculate the amount to pan the image.
                    double deltaX = (e.getX() - lastMousePosition.x) / zoom;
                    double deltaY = (e.getY() - lastMousePosition.y) / zoom;
                    offsetX -= deltaX;
                    offsetY -= deltaY;
                    lastMousePosition = e.getPoint();
                    computeFractal();
                }
            }
        });

        // Add keyboard controls
        setupKeyBindings();

        // Add a ComponentListener to detect when the panel is resized.
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                computeFractal();
            }
        });
    }

    /**
     * Sets the fractal type.
     *
     * @param fractalType The new fractal type.
     */
    public void setFractalType(FractalType fractalType) {
        this.fractalType = fractalType;
        computeFractal();
    }

    /**
     * Gets the Julia set parameter c (real part).
     *
     * @return The real part of c.
     */
    public double getJuliaCReal() {
        return juliaCReal;
    }

    /**
     * Sets the Julia set parameter c (real part).
     *
     * @param real The real part of c.
     */
    public void setJuliaCReal(double real) {
        this.juliaCReal = real;
        if (fractalType == FractalType.JULIA) {
            computeFractal();
        }
    }

    /**
     * Gets the Julia set parameter c (imaginary part).
     *
     * @return The imaginary part of c.
     */
    public double getJuliaCImag() {
        return juliaCImag;
    }

    /**
     * Sets the Julia set parameter c (imaginary part).
     *
     * @param imag The imaginary part of c.
     */
    public void setJuliaCImag(double imag) {
        this.juliaCImag = imag;
        if (fractalType == FractalType.JULIA) {
            computeFractal();
        }
    }

    /**
     * Sets the color scheme.
     *
     * @param colorScheme The new color scheme.
     */
    public void setColorScheme(ColorScheme colorScheme) {
        this.colorScheme = colorScheme;
        computeFractal();
    }

    /**
     * Sets the maximum number of iterations to use when calculating the fractal.
     *
     * @param maxIterations the maximum number of iterations to use when calculating the fractal.
     */
    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
        computeFractal();
    }

    /**
     * Sets the progress bar.
     *
     * @param progressBar The progress bar.
     */
    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    /**
     * Resets the view to default settings.
     */
    public void resetView() {
        zoom = 250.0;
        offsetX = -1.0;
        offsetY = 0.0;
        maxIterations = 250;
        computeFractal();
    }

    /**
     * Shuts down the executor service.
     */
    public void shutdown() {
        executorService.shutdownNow();
    }

    /**
     * Computes the fractal image in a background thread.
     */
    private void computeFractal() {
        if (isComputing) {
            return;
        }

        final int width = getWidth();
        final int height = getHeight();

        if (width <= 0 || height <= 0) {
            // Width and height are invalid, so we cannot proceed.
            return;
        }

        isComputing = true;

        // Create a new image
        fractalImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final int[] pixels = ((DataBufferInt) fractalImage.getRaster().getDataBuffer()).getData();

        // Create a SwingWorker to compute the image
        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                List<Future<Void>> futures = new ArrayList<>(height);
                for (int y = 0; y < height; y++) {
                    final int currentY = y;

                    // Submit a task to the executor service to calculate the pixels for the current row
                    futures.add(executorService.submit(() -> {
                        for (int x = 0; x < width; x++) {
                            double zx, zy, cX, cY;
                            if (fractalType == FractalType.MANDELBROT) {
                                zx = 0;
                                zy = 0;
                                cX = (x - width / 2.0) / zoom + offsetX;
                                cY = (currentY - height / 2.0) / zoom + offsetY;
                            } else { // Julia
                                zx = (x - width / 2.0) / zoom + offsetX;
                                zy = (currentY - height / 2.0) / zoom + offsetY;
                                cX = juliaCReal;
                                cY = juliaCImag;
                            }

                            int iter = 0;

                            while (zx * zx + zy * zy < 4 && iter < maxIterations) {
                                double tmp = zx * zx - zy * zy + cX;
                                zy = 2.0 * zx * zy + cY;
                                zx = tmp;
                                iter++;
                            }

                            if (iter == maxIterations) {
                                // Set the pixel to black for points inside the set
                                pixels[x + currentY * width] = 0;
                            } else {
                                // Use the selected color scheme for points outside the set
                                Color color = colorScheme.getColor();
                                int colorValue = (int) (255.0 * iter / maxIterations);
                                int r = color.getRed() * colorValue / 255;
                                int g = color.getGreen() * colorValue / 255;
                                int b = color.getBlue() * colorValue / 255;
                                int rgb = (r << 16) | (g << 8) | b;
                                pixels[x + currentY * width] = rgb;
                            }
                        }
                        return null;
                    }));

                    // Update progress
                    int progress = (int) ((currentY / (double) height) * 100);
                    publish(progress);
                }

                // Wait for all threads to finish
                try {
                    for (Future<Void> future : futures) {
                        future.get();
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void process(List<Integer> chunks) {
                if (progressBar != null) {
                    int latestProgress = chunks.get(chunks.size() - 1);
                    progressBar.setValue(latestProgress);
                }
            }

            @Override
            protected void done() {
                isComputing = false;
                if (progressBar != null) {
                    progressBar.setValue(100);
                }
                repaint();
            }
        };

        worker.execute();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if (fractalImage != null) {
            graphics.drawImage(fractalImage, 0, 0, null);
        }
    }

    /**
     * Renders the fractal image at the specified size.
     *
     * @param width  The width of the image.
     * @param height The height of the image.
     * @return The rendered fractal image.
     */
    public BufferedImage renderFractalImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        List<Future<Void>> futures = new ArrayList<>(height);
        for (int y = 0; y < height; y++) {
            final int currentY = y;

            // Submit a task to the executor service to calculate the pixels for the current row
            futures.add(executorService.submit(() -> {
                for (int x = 0; x < width; x++) {
                    double zx, zy, cX, cY;
                    if (fractalType == FractalType.MANDELBROT) {
                        zx = 0;
                        zy = 0;
                        cX = (x - width / 2.0) / zoom + offsetX;
                        cY = (currentY - height / 2.0) / zoom + offsetY;
                    } else { // Julia
                        zx = (x - width / 2.0) / zoom + offsetX;
                        zy = (currentY - height / 2.0) / zoom + offsetY;
                        cX = juliaCReal;
                        cY = juliaCImag;
                    }

                    int iter = 0;

                    while (zx * zx + zy * zy < 4 && iter < maxIterations) {
                        double tmp = zx * zx - zy * zy + cX;
                        zy = 2.0 * zx * zy + cY;
                        zx = tmp;
                        iter++;
                    }

                    if (iter == maxIterations) {
                        // Set the pixel to black for points inside the set
                        pixels[x + currentY * width] = 0;
                    } else {
                        // Use the selected color scheme for points outside the set
                        Color color = colorScheme.getColor();
                        int colorValue = (int) (255.0 * iter / maxIterations);
                        int r = color.getRed() * colorValue / 255;
                        int g = color.getGreen() * colorValue / 255;
                        int b = color.getBlue() * colorValue / 255;
                        int rgb = (r << 16) | (g << 8) | b;
                        pixels[x + currentY * width] = rgb;
                    }
                }
                return null;
            }));
        }

        // Wait for all threads to finish
        try {
            for (Future<Void> future : futures) {
                future.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return image;
    }

    /**
     * Sets up keyboard controls for panning and zooming.
     */
    private void setupKeyBindings() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "panLeft");
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "panRight");
        inputMap.put(KeyStroke.getKeyStroke("UP"), "panUp");
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "panDown");
        inputMap.put(KeyStroke.getKeyStroke('+'), "zoomIn");
        inputMap.put(KeyStroke.getKeyStroke('-'), "zoomOut");

        actionMap.put("panLeft", new PanAction(-10 / zoom, 0));
        actionMap.put("panRight", new PanAction(10 / zoom, 0));
        actionMap.put("panUp", new PanAction(0, -10 / zoom));
        actionMap.put("panDown", new PanAction(0, 10 / zoom));
        actionMap.put("zoomIn", new ZoomAction(1.1));
        actionMap.put("zoomOut", new ZoomAction(1 / 1.1));
    }

    // Fractal type
    public enum FractalType {
        MANDELBROT,
        JULIA;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    // The color schemes that can be used to color the fractal.
    public enum ColorScheme {
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

    private class PanAction extends AbstractAction {

        private final double dx;
        private final double dy;

        public PanAction(double dx, double dy) {
            this.dx = dx;
            this.dy = dy;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            offsetX -= dx;
            offsetY -= dy;
            computeFractal();
        }
    }

    private class ZoomAction extends AbstractAction {

        private final double factor;

        public ZoomAction(double factor) {
            this.factor = factor;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            zoom *= factor;
            computeFractal();
        }
    }
}
