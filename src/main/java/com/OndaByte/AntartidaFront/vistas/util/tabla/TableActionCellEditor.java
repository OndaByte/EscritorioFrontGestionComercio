package com.OndaByte.AntartidaFront.vistas.util.tabla;

import java.awt.Component;
import java.util.function.BiConsumer;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;


public class TableActionCellEditor extends DefaultCellEditor {

    private final BiConsumer<PanelAccion, Integer> configurador;

    public TableActionCellEditor(BiConsumer<PanelAccion, Integer> configurador) {
        super(new JCheckBox());
        this.configurador = configurador;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        PanelAccion panel = new PanelAccion();
        configurador.accept(panel, row);
        panel.setBackground(table.getSelectionBackground());
        return panel;
    }
}
