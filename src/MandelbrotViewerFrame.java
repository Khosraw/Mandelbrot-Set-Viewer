import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MandelbrotViewerFrame extends JFrame {
    // Constants
    private static final int MIN_ITERATIONS = 25;
    private static final int MAX_ITERATIONS = 5000;
    private static final int INITIAL_ITERATIONS = 250;

    // The color schemes that can be used to color the fractal.
    private static final MandelbrotPanel.ColorScheme[] COLOR_SCHEMES = MandelbrotPanel.ColorScheme.values();

    public MandelbrotViewerFrame() {
        // Set the look and feel
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set the colors for the UI.
        Color background = new Color(32, 32, 32);
        Color foreground = new Color(224, 224, 224);
        Color accent = new Color(153, 20, 0);
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

        setTitle("Fractal Viewer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create the fractal panel.
        MandelbrotPanel fractalPanel = new MandelbrotPanel();

        // Create the control panel.
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(background);

        // Fractal type combo box
        JComboBox<MandelbrotPanel.FractalType> fractalTypeComboBox = new JComboBox<>(MandelbrotPanel.FractalType.values());
        fractalTypeComboBox.setBackground(accent);
        fractalTypeComboBox.setForeground(accentForeground);
        fractalTypeComboBox.addActionListener(e -> {
            MandelbrotPanel.FractalType fractalType = (MandelbrotPanel.FractalType) fractalTypeComboBox.getSelectedItem();
            fractalPanel.setFractalType(fractalType);
        });
        controlPanel.add(new JLabel("Fractal type: "));
        controlPanel.add(fractalTypeComboBox);

        // Julia set sliders
        JSlider realSlider = createJuliaParameterSlider(-2.0, 2.0, fractalPanel.getJuliaCReal(), "Real Part");
        JSlider imagSlider = createJuliaParameterSlider(-2.0, 2.0, fractalPanel.getJuliaCImag(), "Imaginary Part");

        realSlider.addChangeListener(e -> {
            double value = realSlider.getValue() / 1000.0;
            fractalPanel.setJuliaCReal(value);
        });

        imagSlider.addChangeListener(e -> {
            double value = imagSlider.getValue() / 1000.0;
            fractalPanel.setJuliaCImag(value);
        });

        // Julia parameters panel
        JPanel juliaParamsPanel = new JPanel();
        juliaParamsPanel.setBackground(background);
        juliaParamsPanel.add(new JLabel("Julia c (Real): "));
        juliaParamsPanel.add(realSlider);
        juliaParamsPanel.add(new JLabel("Imag: "));
        juliaParamsPanel.add(imagSlider);

        controlPanel.add(juliaParamsPanel);

        // Color scheme combo box
        JComboBox<MandelbrotPanel.ColorScheme> colorSchemeComboBox = createColorSchemeComboBox(fractalPanel);
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
                saveImage(fractalPanel);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving image.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        controlPanel.add(saveButton);

        // Create the reset button.
        JButton resetButton = new JButton("Reset");
        resetButton.setBackground(accent);
        resetButton.setForeground(accentForeground);

        resetButton.addActionListener(e -> {
            fractalPanel.resetView();
        });
        controlPanel.add(resetButton);

        // Create the panel for the iterations' slider.
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.X_AXIS));
        sliderPanel.setBackground(background);
        JLabel iterationsLabel = new JLabel("Iterations: ");
        iterationsLabel.setForeground(foreground);
        iterationsLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        sliderPanel.add(iterationsLabel);
        JSlider slider = createIterationsSlider(fractalPanel);
        slider.setBackground(background);
        slider.setForeground(accent);
        sliderPanel.add(slider);

        // Progress bar
        JProgressBar progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setBackground(background);
        progressBar.setForeground(accent);
        fractalPanel.setProgressBar(progressBar);

        // Create the wrapper panel with margins.
        JPanel wrapperPanel = new JPanel(new BorderLayout(5, 5));
        wrapperPanel.setBackground(background);
        wrapperPanel.add(controlPanel, BorderLayout.NORTH);
        wrapperPanel.add(fractalPanel, BorderLayout.CENTER);
        wrapperPanel.add(sliderPanel, BorderLayout.SOUTH);
        wrapperPanel.add(new JPanel(), BorderLayout.WEST);
        wrapperPanel.add(new JPanel(), BorderLayout.EAST);

        // Add progress bar to the bottom
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(background);
        bottomPanel.add(sliderPanel, BorderLayout.NORTH);
        bottomPanel.add(progressBar, BorderLayout.SOUTH);

        wrapperPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(wrapperPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Add a window listener to shutdown the executor service when the window is closed.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                fractalPanel.shutdown();
            }
        });
    }

    /**
     * Creates a combo box that allows the user to select a color scheme.
     * @param panel The panel that displays the fractal.
     * @return The combo box.
     */
    private JComboBox<MandelbrotPanel.ColorScheme> createColorSchemeComboBox(MandelbrotPanel panel) {
        JComboBox<MandelbrotPanel.ColorScheme> comboBox = new JComboBox<>(COLOR_SCHEMES);

        comboBox.addActionListener(e -> {
            MandelbrotPanel.ColorScheme colorScheme = (MandelbrotPanel.ColorScheme) comboBox.getSelectedItem();
            panel.setColorScheme(colorScheme);
        });

        return comboBox;
    }

    /**
     * Creates a slider that allows the user to select the number of iterations.
     * @param panel The panel that displays the fractal.
     * @return The slider.
     */
    private JSlider createIterationsSlider(MandelbrotPanel panel) {
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
     * Creates a slider for Julia set parameters.
     * @param min The minimum value.
     * @param max The maximum value.
     * @param initialValue The initial value.
     * @param label The label for the slider.
     * @return The slider.
     */
    private JSlider createJuliaParameterSlider(double min, double max, double initialValue, String label) {
        int minValue = (int) (min * 1000);
        int maxValue = (int) (max * 1000);
        int initial = (int) (initialValue * 1000);
        JSlider slider = new JSlider(JSlider.HORIZONTAL, minValue, maxValue, initial);
        slider.setMajorTickSpacing((maxValue - minValue) / 4);
        slider.setMinorTickSpacing((maxValue - minValue) / 20);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setBackground(new Color(32, 32, 32));
        slider.setForeground(new Color(153, 20, 0));
        return slider;
    }

    /**
     * Saves the fractal image to a file.
     * @param panel The panel that displays the fractal.
     * @throws IOException If an error occurs while saving the image.
     */
    private void saveImage(MandelbrotPanel panel) throws IOException {
        // Create a file chooser.
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            // Prompt for image size
            String widthStr = JOptionPane.showInputDialog(this, "Enter image width:", panel.getWidth());
            String heightStr = JOptionPane.showInputDialog(this, "Enter image height:", panel.getHeight());
            int imageWidth = Integer.parseInt(widthStr);
            int imageHeight = Integer.parseInt(heightStr);

            // Save the image.
            File file = fileChooser.getSelectedFile();

            BufferedImage image = panel.renderFractalImage(imageWidth, imageHeight);

            // Write the image to the file.
            ImageIO.write(image, "png", file);
        }
    }
}
