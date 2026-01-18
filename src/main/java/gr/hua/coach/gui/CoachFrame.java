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

import java.time.LocalDate;
import java.util.Map;
import java.util.HashMap;

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

       
        for (Activity a : all) {
            ActivityStats st = statsCalculator.calculate(a, weight);

            double calories = -1;

            if (weight != null) {
                if ("Default (MU)".equals(method)) {
                    calories = gr.hua.coach.stats.CaloriesCalculator.calculateDefault(
                            a.getType(), weight, st.getTotalTime()
                    );
                } else {
                    calories = gr.hua.coach.stats.CaloriesCalculator.calculateAlternative(
                            a.getType(),
                            weight,
                            st.getTotalTime(),
                            gender,
                            age,
                            st.getAvgHeartRate()
                    );
                }
            }

            sb.append("Activity: ").append(a.getType()).append("\n");
            sb.append("Total Time: ")
                    .append(ActivityStats.formatDuration(st.getTotalTime()))
                    .append("\n");
            sb.append("Total Distance: ").append(st.getTotalDistanceKm()).append(" km\n");
            sb.append("Avg Pace: ").append(st.getAvgPaceMinPerKm()).append(" min/km\n");
            if (st.getAvgHeartRate() != null) {
                sb.append("Avg Heart Rate: ").append(st.getAvgHeartRate()).append(" bpm\n");
            }


            sb.append("Calories method: ").append(method).append("\n");
            if (weight == null) {
                sb.append("Calories: - (enter weight)\n");
            } else if (!"Default (MU)".equals(method)) {
                if (age == null || gender == null || "-".equals(gender) || st.getAvgHeartRate() == null) {
                    sb.append("Calories: - (need age, gender and avg heart rate)\n");
                } else {
                    sb.append(String.format("Calories: %.0f kcal%n", calories));
                }
            } else {
                sb.append(String.format("Calories: %.0f kcal%n", calories));
            }

            sb.append("\n");
        }

        
        if (goal != null) {
            sb.append("Daily goal: ").append(goal).append(" kcal\n");
        }

        if (goal != null && weight != null) {
            Map<LocalDate, Double> caloriesPerDay = new HashMap<>();

            for (Activity a : all) {
                ActivityStats st = statsCalculator.calculate(a, weight);

                double kcal;
                if ("Default (MU)".equals(method)) {
                    kcal = gr.hua.coach.stats.CaloriesCalculator.calculateDefault(
                            a.getType(), weight, st.getTotalTime()
                    );
                } else {
                    kcal = gr.hua.coach.stats.CaloriesCalculator.calculateAlternative(
                            a.getType(), weight, st.getTotalTime(),
                            gender, age, st.getAvgHeartRate()
                    );
                }

                LocalDate day = a.getStartTime().toLocalDate();
                caloriesPerDay.put(day, caloriesPerDay.getOrDefault(day, 0.0) + kcal);
            }

            sb.append("\n=== Daily goal analysis ===\n");
            for (Map.Entry<LocalDate, Double> e : caloriesPerDay.entrySet()) {
                double total = e.getValue();
                double remaining = goal - total;

                sb.append(e.getKey()).append(": ")
                        .append(String.format("%.0f", total)).append(" / ")
                        .append(String.format("%.0f", goal)).append(" kcal ");

                if (remaining <= 0) {
                    sb.append("(GOAL ACHIEVED)\n");
                } else {
                    sb.append("(remaining: ").append(String.format("%.0f", remaining)).append(")\n");
                }
            }

            // how many days remaining toaday
            LocalDate today = LocalDate.now();
            double todayTotal = caloriesPerDay.getOrDefault(today, 0.0);
            double todayRemaining = goal - todayTotal;
            sb.append("\nToday (").append(today).append(") remaining: ")
                    .append(String.format("%.0f", Math.max(0, todayRemaining))).append(" kcal\n");
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
