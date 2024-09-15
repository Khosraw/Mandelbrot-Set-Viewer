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
 * There is also support for Julia sets. A Julia set is a fractal that is defined by the following
 * equation: z = z^2 + c where z and c are complex numbers. The Julia set is the set of complex numbers
 * c for which the function does not diverge when iterated from z = 0. The viewer allows the user to
 * change the value of c to generate different Julia sets. The user can change the Julia Constant value
 * and the Julia imag value.
 *
 * @author Khosraw Azizi
 * @version 1.4
 * @bug No known bugs.
 * @keywords Mandelbrot set, julia, julia set, fractal, complex number, viewer, zoom, pan, color
 * scheme, multithreading, multi-threading, Swing, JFrame, JPanel, ExecutorService, Executors, Future, Dark Mode, Dark
 * @license MIT License
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
 * @since 2023-03-23
 */

import javax.swing.*;

public class MandelbrotViewer {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MandelbrotViewerFrame viewer = new MandelbrotViewerFrame();
            viewer.setVisible(true);
        });
    }
}
