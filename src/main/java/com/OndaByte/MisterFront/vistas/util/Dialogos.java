
package com.OndaByte.MisterFront.vistas.util;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;

public class Dialogos {
    
    public static boolean confirmar(String mensaje, String titulo) {
        int r = JOptionPane.showConfirmDialog(null, mensaje, titulo, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return r == JOptionPane.YES_OPTION;
    }

    public static void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Información", JOptionPane.PLAIN_MESSAGE);
    }
    public static String mostrarInput(String mensaje) {
        return JOptionPane.showInputDialog(null, mensaje, "Entrada", JOptionPane.PLAIN_MESSAGE);
    }
    // ========================= TOAST ========================= //

    public static void mostrarToast(String mensaje) {
        mostrarToast(mensaje, 3000);
    }

    public static void mostrarToast(String mensaje, int duracionMs) {
        JWindow toast = new JWindow();
        toast.setAlwaysOnTop(true);
        toast.setBackground(new Color(0, 0, 0, 0));

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "background:lighten(@background,5%);" +
                "border:round;" +
                "arc:10;" +
                "shadow:popup");

        JLabel label = new JLabel(mensaje);
        panel.add(label);
        toast.add(panel);
        toast.pack();

        // Posición bottom center
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - toast.getWidth()) / 2;
        int y = screenSize.height - toast.getHeight() - 80;
        toast.setLocation(x, y);

        toast.setVisible(true);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.setVisible(false);
                toast.dispose();
            }
        }, duracionMs);
    }
}