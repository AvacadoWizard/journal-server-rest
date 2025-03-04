package com.journal;

import javax.swing.*;
import java.awt.*;

public class EntriesPanel extends JPanel {
    public EntriesPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Entries Panel"), BorderLayout.CENTER);
    }
}