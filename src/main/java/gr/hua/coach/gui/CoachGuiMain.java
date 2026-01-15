package gr.hua.coach.gui;

import javax.swing.SwingUtilities;

public class CoachGuiMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CoachFrame frame = new CoachFrame();
            frame.setVisible(true);
        });
    }
}
