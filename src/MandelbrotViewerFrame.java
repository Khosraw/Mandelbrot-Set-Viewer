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
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set the colors for the UI.
        Color background = new Color(32, 32, 32);
        Color foreground = new Color(224, 224, 224);
        Color accent = new Color(200, 24, 0);
        Color accentForeground = new Color(240, 240, 240);
        UIManager.put("Panel.background", background);
        UIManager.put("Label.foreground", foreground);
        UIManager.put("Slider.background", background);
        UIManager.put("Slider.foreground", accent);
        UIManager.put("Slider.trackForeground", accent);
        UIManager.put("Button.background", accent);
        UIManager.put("Button.foreground", accentForeground);
        UIManager.put("ComboBox.background", background);
        UIManager.put("ComboBox.foreground", foreground);
        UIManager.put("ComboBox.selectionBackground", accent);
        UIManager.put("ComboBox.selectionForeground", accentForeground);
        UIManager.put("ComboBox.buttonBackground", accent);
        UIManager.put("ComboBox.buttonForeground", accentForeground);
        UIManager.put("Button.defaultButtonFollowsFocus", true);

        setTitle("Mandelbrot Set Viewer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create the Mandelbrot set panel.
        MandelbrotViewer.MandelbrotPanel mandelbrotPanel = new MandelbrotViewer.MandelbrotPanel();

        // Create the control panel.
        JComboBox<MandelbrotViewer.MandelbrotPanel.ColorScheme> colorSchemeComboBox = createColorSchemeComboBox(mandelbrotPanel);
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(background);
        controlPanel.add(new JLabel("Color scheme: "));
        colorSchemeComboBox.setBackground(accent);
        colorSchemeComboBox.setForeground(accentForeground);
        controlPanel.add(colorSchemeComboBox);

        // Create the button to save the image.
        JButton saveButton = new JButton("Save");
        saveButton.setBackground(accent);
        saveButton.setForeground(accentForeground);

        saveButton.addActionListener(e -> {
            try {
                saveImage(mandelbrotPanel);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving image.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        controlPanel.add(saveButton);

        // Create the panel for the iterations' slider.
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.X_AXIS));
        sliderPanel.setBackground(background);
        JLabel iterationsLabel = new JLabel("Iterations: ");
        iterationsLabel.setForeground(foreground);
        iterationsLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        sliderPanel.add(iterationsLabel);
        JSlider slider = createIterationsSlider(mandelbrotPanel);
        slider.setBackground(background);
        slider.setForeground(accent);
        sliderPanel.add(slider);

        // Create the wrapper panel with margins.
        JPanel wrapperPanel = new JPanel(new BorderLayout(5, 5));
        wrapperPanel.setBackground(background);
        wrapperPanel.add(controlPanel, BorderLayout.NORTH);
        wrapperPanel.add(mandelbrotPanel, BorderLayout.CENTER);
        wrapperPanel.add(sliderPanel, BorderLayout.SOUTH);
        wrapperPanel.add(new JPanel(), BorderLayout.WEST);
        wrapperPanel.add(new JPanel(), BorderLayout.EAST);

        add(wrapperPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
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
        // Create a file chooser.
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            // Save the image.
            File file = fileChooser.getSelectedFile();

            BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();

            // Draw the panel to the image.
            panel.paint(g);
            g.dispose();

            // Write the image to the file.
            ImageIO.write(image, "png", file);
        }
    }
}
