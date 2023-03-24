import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MandelbrotViewerFrame extends JFrame {
    // constants
    private static final int MIN_ITERATIONS = 25;
    private static final int MAX_ITERATIONS = 5000;
    private static final int INITIAL_ITERATIONS = 250;

    // The color schemes that can be used to color the Mandelbrot set.
    private static final MandelbrotViewer.MandelbrotPanel.ColorScheme[] COLOR_SCHEMES = MandelbrotViewer.MandelbrotPanel.ColorScheme.values();

    public MandelbrotViewerFrame() {
        setTitle("Mandelbrot Set Viewer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);

        // Create the Mandelbrot set panel.
        MandelbrotViewer.MandelbrotPanel panel = new MandelbrotViewer.MandelbrotPanel();
        add(panel, BorderLayout.CENTER);

        // Create the control panel.
        JComboBox<MandelbrotViewer.MandelbrotPanel.ColorScheme> colorSchemeComboBox = createColorSchemeComboBox(panel);
        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Color scheme: "));
        controlPanel.add(colorSchemeComboBox);
        add(controlPanel, BorderLayout.NORTH);

        // Create the button to save the image.
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                saveImage(panel);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving image.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        controlPanel.add(saveButton, BorderLayout.NORTH);

        // Create the label for the number of iterations.
        JLabel iterationsLabel = new JLabel("Iterations: ");
        controlPanel.add(iterationsLabel, BorderLayout.SOUTH);

        // Create the slider for the number of iterations.
        JSlider slider = createIterationsSlider(panel);
        add(slider, BorderLayout.SOUTH);
    }

    /**
     * Creates a combo box that allows the user to select a color scheme.
     * @param panel The panel that displays the Mandelbrot set.
     * @return The combo box.
     */
    private JComboBox<MandelbrotViewer.MandelbrotPanel.ColorScheme> createColorSchemeComboBox(MandelbrotViewer.MandelbrotPanel panel) {
        JComboBox<MandelbrotViewer.MandelbrotPanel.ColorScheme> comboBox = new JComboBox<>(COLOR_SCHEMES);

        comboBox.addActionListener(e -> {
            MandelbrotViewer.MandelbrotPanel.ColorScheme colorScheme = (MandelbrotViewer.MandelbrotPanel.ColorScheme) comboBox.getSelectedItem();
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
    private JSlider createIterationsSlider(MandelbrotViewer.MandelbrotPanel panel) {


        JSlider slider = new JSlider(JSlider.HORIZONTAL, MIN_ITERATIONS, MAX_ITERATIONS, INITIAL_ITERATIONS);
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

    /**
     * Saves the Mandelbrot set image to a file.
     * @param panel The panel that displays the Mandelbrot set.
     * @throws IOException If an error occurs while saving the image.
     */
    private void saveImage(MandelbrotViewer.MandelbrotPanel panel) throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            panel.paint(g);
            g.dispose();
            ImageIO.write(image, "png", file);
        }
    }

}
