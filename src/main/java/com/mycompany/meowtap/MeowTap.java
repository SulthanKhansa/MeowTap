package com.mycompany.meowtap;

import ui.WelcomeScreen;

public class MeowTap {

    public static void main(String[] args) {
        // Aktifkan FlatLaf Modern Look and Feel
        com.formdev.flatlaf.FlatIntelliJLaf.setup();
        
        java.awt.EventQueue.invokeLater(() -> {
            // Mulai dari halaman WelcomeScreen
            new WelcomeScreen().setVisible(true);
        });
    }
}
