package com.OndaByte.AntartidaFront.vistas.util.tabla;

import java.awt.event.ActionListener;

import com.OndaByte.AntartidaFront.vistas.util.IconSVG;
import java.util.LinkedHashMap;
import java.util.Map;
import net.miginfocom.swing.MigLayout;
import javax.swing.JPanel;

public class PanelAccion extends JPanel {

    private final Map<String, ActionButton> botones = new LinkedHashMap<>();

    public PanelAccion() {
        setLayout(new MigLayout("insets 0, align center center, gap 5", "[]", "[]"));
        this.setFocusable(false);
    }

    public void agregarBoton(String clave, String iconoSVG, ActionListener accion) {
        if (botones.containsKey(clave)) return;

        ActionButton boton = new ActionButton();
        boton.setIcon(new IconSVG(iconoSVG));
        boton.addActionListener(accion);
        boton.setToolTipText(clave);
        botones.put(clave, boton);
        add(boton, "center");
    }

    public ActionButton getBoton(String clave) {
        return botones.get(clave);
    }

    public void removerBoton(String clave) {
        ActionButton boton = botones.remove(clave);
        if (boton != null) {
            remove(boton);
            revalidate();
            repaint();
        }
    }
}