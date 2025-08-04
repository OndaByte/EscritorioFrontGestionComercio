package com.OndaByte.AntartidaFront.vistas.util;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;
import javax.swing.text.JTextComponent;
import javax.swing.text.MaskFormatter;
import net.miginfocom.swing.MigLayout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FormularioBuilder {

    private JPanel formPanel;
    private JPanel headerPanel;
    private JPanel bodyPanel;
    private JPanel footerPanel;

    private JButton btnGuardar, btnCancelar;

    private JLabel mensajeError = new JLabel("");
    private Dimension tamCampo = new Dimension(230, 33);
    private Map<String, JComponent> componentes = new LinkedHashMap<>();
    private Map<String, ButtonGroup> radios = new HashMap<>();

    private static Logger logger = LogManager.getLogger(FormularioBuilder.class.getName());

    static {
        if (logger.isDebugEnabled()) {
            logger.debug("Init logger en FormularioBuilder");
        }
    }

    public FormularioBuilder() {
        formPanel = new JPanel(new BorderLayout());

        headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bodyPanel = new JPanel(new MigLayout("wrap 2, insets 15", "[right][grow, fill]"));
        footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        mensajeError.setForeground(Color.RED);
    }

    public void agregarTitulo(String id, String titulo) {
        JLabel lblID = new JLabel(titulo);
        lblID.setFont(lblID.getFont().deriveFont(Font.BOLD, 16));
        JPanel panel = new JPanel();
        panel.add(lblID);
        componentes.put(id, lblID);
        headerPanel.add(panel);
    }

    public void agregarComponenteCustom(String id, JComponent input, String label) {
        JLabel l = new JLabel(label);
        bodyPanel.add(l, "align right, aligny top");
        bodyPanel.add(input, "growx, pushx");
        componentes.put(id, input);
        restaurarBordeSiChildTieneFocus(input);
    }

    public void agregarComponenteCustom(String id, JComponent input) {
        bodyPanel.add(input, "span, growx, pushx");
        componentes.put(id, input);
        restaurarBordeSiChildTieneFocus(input);
    }
    /**
     * 
     * @param id
     * @param tipoComponente 
     * @param label
     * @param opciones
     * @param rows 
     */
    public void agregarComponente(String id, String tipoComponente, String label, Object[] opciones, int rows) {
        JComponent input = null;
        switch (tipoComponente.toLowerCase()) {
            case ("textfield") ->
                input = new JTextField();
            case ("passwordfield") ->
                input = new JPasswordField();
            case "textarea" -> {
                JScrollPane scroll = (JScrollPane) crearTA(rows);
                JTextArea area = (JTextArea) scroll.getViewport().getView();
                restaurarBordeAlFocus(area);
                componentes.put(id, area); // Guarda el JTextArea, no el scroll
                input = scroll;
                break;
            }
            case "combobox" ->
                input = crearCB(opciones);
            case "checkbox" ->
                input = crearCKB(opciones);
            case "radiobutton" ->
                input = crearRB(opciones);
            case "calendar" ->
                input = crearFechaConJCalendar();
            case "label" -> 
                input = new JLabel(label);
//            case "spinnerfechayhora" ->
//                input = crearFechaYHora();
            default ->
                logger.error("Tipo de componente no soportado: " + tipoComponente);
        }

        if (input != null) {
            JLabel l = new JLabel(label);
            bodyPanel.add(l);
            bodyPanel.add(input);
            componentes.putIfAbsent(id, input); // Lo pone si no existe caso textArea :O
            restaurarBordeAlFocus(input);
        }
    }

    public JComponent getComponente(String id) {
        return componentes.get(id);
    }

    public <T extends JComponent> T getComponenteByClass(String id) {
        return (T) componentes.get(id);
    }

    public void agregarMenuBotones() {


        btnGuardar = new JButton("Guardar");
        btnGuardar.setIcon(new IconSVG(IconSVG.GUARDAR));
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setIcon(new IconSVG(IconSVG.CANCELAR));
        JPanel left = new JPanel();
        left.add(btnCancelar);
        JPanel right = new JPanel();
        right.add(btnGuardar);
        footerPanel.add(left);
        footerPanel.add(right);
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError.setText(mensajeError); //cuando sesetteaba se sale el label .. 
    }

    public JPanel construir() {
        formPanel.add(headerPanel, BorderLayout.NORTH);
        formPanel.add(bodyPanel, BorderLayout.CENTER);
        formPanel.add(footerPanel, BorderLayout.SOUTH);
        return formPanel;
    }

    public void setTamCampo(Dimension tamCampo) {
        this.tamCampo = tamCampo;
    }

    /**
     * Text Area.
     */
    public JComponent crearTA(int alto) {
        int rows = alto != 0 ? alto : 3;
        JTextArea area = new JTextArea(rows, 20);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(area);
        return scroll;
    }

    /**
     * Combo Box.
     */
    public JComponent crearCB(Object[] opciones) {
        return new JComboBox<>(opciones);
    }

    /**
     * Radio Button.
     */
    public JComponent crearRB(Object[] opciones) {
        if (opciones == null) {
            throw new IllegalArgumentException("RadioButton requiere opciones.");
        }
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ButtonGroup grupo = new ButtonGroup();
        for (Object opcion : opciones) {
            JRadioButton radio = new JRadioButton("" + opcion);
            grupo.add(radio);
            radioPanel.add(radio);
        }
        radios.put("radiobutton", grupo);
        return radioPanel;
    }

    public JComponent crearCKB(Object[] opciones) {
        JPanel checkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (Object opcion : opciones) {
            JCheckBox check = new JCheckBox("" + opcion);
            checkPanel.add(check);
        }
        return checkPanel;
    }

    public JComponent crearFechaConJCalendar() {
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd"); // Mostrar formato yyyy-MM-dd
        return dateChooser;
    }
    
    /**
     * Deprecated, obligaba al usuario a insertar una fecha dentro de la expresion regular y la spinnereaba.
     * @return 
     */
    public JComponent crearFechaSpinner() {
        SpinnerDateModel model = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "yyyy-MM-dd");
        spinner.setEditor(editor);

        JFormattedTextField tf = editor.getTextField();
        ((AbstractDocument) tf.getDocument()).setDocumentFilter(new FiltroRegex("^\\d{0,4}(-\\d{0,2}){0,2}$")); 
        return spinner;
    }
    /**
     * Not used for now, its crea la fecha con jcalendar y spinner para la hora ...
     * @return 
     */
    public JComponent crearFechaYHora() {
         JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0)); // Ajuste más compacto

        // DateChooser para fecha
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setPreferredSize(new Dimension(140, 30)); // Fecha compacta

        // Spinner para hora
        SpinnerDateModel modelHora = new SpinnerDateModel();
        JSpinner spinnerHora = new JSpinner(modelHora);
        JSpinner.DateEditor editorHora = new JSpinner.DateEditor(spinnerHora, "HH:mm");
        spinnerHora.setEditor(editorHora);
        spinnerHora.setPreferredSize(new Dimension(80, 30)); // Hora compacta

        panel.add(dateChooser);
        panel.add(spinnerHora);

        // Para tu uso posterior (obtener componentes fácilmente)
        panel.putClientProperty("fecha", dateChooser);
        panel.putClientProperty("hora", spinnerHora);

        return panel;
    }
    
    public void setPlaceholder(JTextComponent textField, String placeholder) {
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);

        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
    }

    private void restaurarBordeAlFocus(JComponent campo) {
        campo.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                campo.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
            }
        });
    }
    
    private void restaurarBordeSiChildTieneFocus(JComponent panel) {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("permanentFocusOwner", evt -> {
            if (evt.getNewValue() instanceof Component comp && SwingUtilities.isDescendingFrom(comp, panel)) {
                panel.setBorder(BorderFactory.createEmptyBorder()); // Ejemplo de foco activo
            }
        });
    }
    
    private class FiltroRegex extends DocumentFilter {

        private final Pattern pattern;

        public FiltroRegex(String regex) {
            this.pattern = Pattern.compile(regex);
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
            sb.insert(offset, string);
            if (pattern.matcher(sb.toString()).matches() || sb.toString().isEmpty()) {
                super.insertString(fb, offset, string, attr);
            }
            // Si no matchea, NO insertamos
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
            sb.replace(offset, offset + length, text);
            if (pattern.matcher(sb.toString()).matches() || sb.toString().isEmpty()) {
                super.replace(fb, offset, length, text, attrs);
            }
            // Si no matchea, NO reemplazamos
        }
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }
}
