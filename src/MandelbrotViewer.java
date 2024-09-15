import javax.swing.*;

public class MandelbrotViewer {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MandelbrotViewerFrame viewer = new MandelbrotViewerFrame();
            viewer.setVisible(true);
        });
    }
}
