package com.OndaByte.MisterFront.vistas.util.tabla;

import java.awt.Component;
import java.util.function.BiConsumer;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class TableActionCellRender extends DefaultTableCellRenderer {

    private final BiConsumer<PanelAccion, Integer> configurador;

    public TableActionCellRender(BiConsumer<PanelAccion, Integer> configurador) {
        this.configurador = configurador;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        PanelAccion panel = new PanelAccion();
        configurador.accept(panel, row);
        panel.setBackground(c.getBackground());
        return panel;
    }
}
