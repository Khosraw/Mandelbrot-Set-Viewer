# Mandelbrot and Julia Set Viewer

This is a multi-threaded Java program that displays the Mandelbrot and Julia sets, allowing users to explore these fascinating fractals interactively. The application supports panning, zooming, and real-time adjustments of fractal parameters. It also includes controls to adjust the maximum iteration count, change color schemes, and save high-resolution images.

![Mandelbrot Set](https://github.com/user-attachments/assets/c966ffbe-40b8-4adf-9720-86f05e05728f)
![Julia](https://github.com/user-attachments/assets/069f9af6-5faf-4806-8abe-18808af53be9)


## Features

- **Interactive Mandelbrot and Julia Sets**: Switch between Mandelbrot and Julia fractals seamlessly.
- **Real-Time Parameter Adjustment**: Adjust Julia set parameters using intuitive sliders for immediate visual feedback.
- **Click-to-Select Julia Parameters**: In Mandelbrot mode, click on any point to generate the corresponding Julia set.
- **Zoom and Pan**: Use the mouse or keyboard to zoom in/out and navigate around the fractal.
- **Color Schemes**: Choose from multiple color schemes to customize the fractal's appearance.
- **Iteration Control**: Adjust the maximum number of iterations to change the fractal's detail level.
- **Progress Bar**: Monitor the rendering progress, especially useful for high iteration counts or resolutions.
- **Save High-Resolution Images**: Export the current view as a PNG image with customizable resolution.
- **Keyboard Controls**: Use keyboard shortcuts for panning and zooming.

## Getting Started

### Prerequisites

- **Java SE Development Kit (JDK) 8 or higher**: Download the latest version from the [Oracle website](https://www.oracle.com/java/technologies/javase-downloads.html).

### Installation

1. **Clone or Download the Repository**:

   ```bash
   git clone https://github.com/Khosraw/Mandelbrot-Set-Viewer
   ```

2. **Navigate to the Project Directory**:

   ```bash
   cd mandelbrot-julia-viewer
   ```

3. **Compile the Program**:

   ```bash
   javac MandelbrotViewer.java MandelbrotViewerFrame.java MandelbrotPanel.java
   ```

4. **Run the Program**:

   ```bash
   java MandelbrotViewer
   ```

   Ensure all the `.java` files are in the same directory or adjust the classpath accordingly.

## Usage

When the program is launched, it will display the Mandelbrot set in a window. You can use the following controls to explore the fractals:

### Controls

- **Mouse Controls**:
  - **Pan**: Click and drag the mouse to move around the fractal.
  - **Zoom**: Scroll the mouse wheel to zoom in and out.

- **Keyboard Controls**:
  - **Pan**: Use the arrow keys (`←`, `→`, `↑`, `↓`) to pan left, right, up, and down.
  - **Zoom**: Press the `+` key to zoom in and the `-` key to zoom out.

- **Fractal Type**:
  - **Switch Fractals**: Use the "Fractal type" dropdown menu to switch between "mandelbrot" and "julia".
  - **Click-to-Select Julia Parameters**: In Mandelbrot mode, click on any point to generate the corresponding Julia set.

- **Julia Set Parameters**:
  - **Adjust Parameters**: Use the "Julia c (Real)" and "Imag" sliders to adjust the real and imaginary parts of the Julia set parameter `c`. The fractal updates in real-time as you move the sliders.

- **Color Scheme**:
  - **Change Colors**: Select a color scheme from the dropdown menu to change the fractal's appearance.

- **Iterations**:
  - **Adjust Detail**: Use the iterations slider to change the maximum number of iterations, affecting the level of detail in the fractal.

- **Progress Bar**:
  - **Rendering Status**: A progress bar at the bottom displays the rendering progress, especially useful when working with high iterations or resolutions.

- **Save Image**:
  - **Export Fractal**: Click the "Save" button to export the current fractal view as a PNG image.
  - **Set Resolution**: Input the desired image width and height when prompted.

- **Reset View**:
  - **Restore Defaults**: Click the "Reset" button to restore the default view settings.

## Examples

### Exploring Julia Sets

1. **Start in Mandelbrot Mode**: Ensure "mandelbrot" is selected in the "Fractal type" dropdown.
2. **Select a Point**: Click on an interesting point in the Mandelbrot set.
3. **View Corresponding Julia Set**: The application switches to Julia mode, displaying the Julia set for the selected parameter.
4. **Adjust Parameters**: Use the sliders to fine-tune the Julia set parameters and observe changes in real-time.

### Saving High-Resolution Images

1. **Navigate to Desired View**: Pan and zoom to frame the fractal as desired.
2. **Set Iterations**: Increase the maximum iterations for more detail if needed.
3. **Click "Save"**: Click the "Save" button.
4. **Set Resolution**: Enter the desired width and height for the image when prompted.
5. **Save the Image**: Choose a file location and name to save the PNG image.

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you'd like to change.

Please ensure that your contributions align with the project's coding standards and include appropriate tests.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- **Fractal Exploration**: Inspired by the beauty and complexity of fractal geometry.
- **Contributors**: Thanks to all contributors who have helped improve this application.

If you have any questions or need further assistance, please don't hesitate to ask!
