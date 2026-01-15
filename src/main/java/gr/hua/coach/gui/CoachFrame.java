package gr.hua.coach.gui;

import gr.hua.coach.model.Activity;
import gr.hua.coach.parser.ActivityParser;
import gr.hua.coach.parser.TcxActivityParser;
import gr.hua.coach.stats.ActivityStats;
import gr.hua.coach.stats.DefaultStatsCalculator;
import gr.hua.coach.stats.StatsCalculator;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CoachFrame extends JFrame {

    private final JTextField weightField = new JTextField(6);
    private final JTextField ageField = new JTextField(6);
    private final JComboBox<String> genderBox = new JComboBox<>(new String[]{"-", "Male", "Female"});
    private final JComboBox<String> caloriesMethodBox = new JComboBox<>(new String[]{"Default (MU)", "Alternative"});
    private final JTextField goalField = new JTextField(6);

    private final DefaultListModel<File> filesModel = new DefaultListModel<>();
    private final JList<File> filesList = new JList<>(filesModel);

    private final JTextArea output = new JTextArea(18, 60);

    public CoachFrame() {
        super("Coach - TCX Analyzer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        output.setEditable(false);
        output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        JButton chooseBtn = new JButton("Choose .tcx files");
        JButton analyzeBtn = new JButton("Analyze");
        JButton clearBtn = new JButton("Clear");

        chooseBtn.addActionListener(e -> chooseFiles());
        analyzeBtn.addActionListener(e -> analyze());
        clearBtn.addActionListener(e -> {
            filesModel.clear();
            output.setText("");
        });

        JPanel top = new JPanel(new GridLayout(2, 1));
        top.add(buildUserPanel());
        top.add(buildFilesPanel(chooseBtn));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(clearBtn);
        bottom.add(analyzeBtn);

        setLayout(new BorderLayout(8, 8));
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(output), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    private JPanel buildUserPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(new JLabel("Weight (kg):"));
        p.add(weightField);

        p.add(new JLabel("Age:"));
        p.add(ageField);

        p.add(new JLabel("Gender:"));
        p.add(genderBox);

        p.add(new JLabel("Calories method:"));
        p.add(caloriesMethodBox);

        p.add(new JLabel("Daily goal (kcal):"));
        p.add(goalField);

        return p;
    }

    private JPanel buildFilesPanel(JButton chooseBtn) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(chooseBtn, BorderLayout.WEST);

        filesList.setVisibleRowCount(4);
        p.add(new JScrollPane(filesList), BorderLayout.CENTER);

        return p;
    }

    private void chooseFiles() {
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int res = fc.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            filesModel.clear();
            for (File f : fc.getSelectedFiles()) {
                filesModel.addElement(f);
            }
        }
    }

    private void analyze() {
        if (filesModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please choose at least one .tcx file.");
            return;
        }

        Double weight = parseDoubleOrNull(weightField.getText());
        Integer age = parseIntOrNull(ageField.getText());
        String gender = (String) genderBox.getSelectedItem();
        String method = (String) caloriesMethodBox.getSelectedItem();
        Double goal = parseDoubleOrNull(goalField.getText());

        
        ActivityParser parser = new TcxActivityParser();
        StatsCalculator statsCalculator = new DefaultStatsCalculator();

        List<Activity> all = new ArrayList<>();

        try {
            for (int i = 0; i < filesModel.size(); i++) {
                File f = filesModel.get(i);
                all.addAll(parser.parse(f));
            }
        } catch (Exception ex) {
            output.setText("Error while parsing files:\n" + ex.getMessage());
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Loaded activities: ").append(all.size()).append("\n\n");

        // (Α) stats ανά activity
        for (Activity a : all) {
            ActivityStats st = statsCalculator.calculate(a, weight);

            sb.append("Activity: ").append(a.getType()).append("\n");
            sb.append("Total Time: ").append(st.getTotalTime()).append("\n");
            sb.append("Total Distance: ").append(st.getTotalDistanceKm()).append(" km\n");
            sb.append("Avg Pace: ").append(st.getAvgPaceMinPerKm()).append(" min/km\n");
            if (st.getAvgHeartRate() != null) {
                sb.append("Avg Heart Rate: ").append(st.getAvgHeartRate()).append(" bpm\n");
            }

            // (Γ) θερμίδες: θα το κουμπώσουμε στο επόμενο βήμα με “strategy”
            sb.append("Calories method: ").append(method).append("\n");
            if (weight != null) {
                sb.append("Calories: (calculated in next step)\n");
            } else {
                sb.append("Calories: - (enter weight)\n");
            }

            sb.append("\n");
        }

        // (Β)+(Δ) θα τα κάνουμε αμέσως μετά: add activity + daily goal summary
        if (goal != null) {
            sb.append("Daily goal: ").append(goal).append(" kcal\n");
            sb.append("(daily goal analysis in next step)\n");
        }

        output.setText(sb.toString());
    }

    private Double parseDoubleOrNull(String s) {
        try {
            if (s == null) return null;
            s = s.trim();
            if (s.isEmpty()) return null;
            return Double.parseDouble(s);
        } catch (Exception e) {
            return null;
        }
    }

    private Integer parseIntOrNull(String s) {
        try {
            if (s == null) return null;
            s = s.trim();
            if (s.isEmpty()) return null;
            return Integer.parseInt(s);
        } catch (Exception e) {
            return null;
        }
    }
}
