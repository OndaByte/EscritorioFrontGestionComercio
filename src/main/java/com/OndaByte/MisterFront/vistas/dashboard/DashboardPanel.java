
package com.OndaByte.MisterFront.vistas.dashboard;

import com.OndaByte.MisterFront.controladores.TurnoController;
import com.OndaByte.MisterFront.estilos.MisEstilos;
import com.OndaByte.MisterFront.modelos.Cliente;
import com.OndaByte.MisterFront.modelos.Orden;
import com.OndaByte.MisterFront.modelos.Pedido;
import com.OndaByte.MisterFront.modelos.Turno;
import com.OndaByte.MisterFront.vistas.DatosListener;
import com.OndaByte.MisterFront.vistas.MiFrame;
import com.OndaByte.MisterFront.vistas.util.Dialogos;
import com.OndaByte.MisterFront.vistas.util.FechaUtils;
import com.OndaByte.MisterFront.vistas.util.Paginado;
import net.miginfocom.swing.MigLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class DashboardPanel extends JPanel {

    private JLabel mesLabel;
    private JPanel calendarioPanel;
    private JPanel eventosPanel;
    private JButton btnAnterior, btnSiguiente;

    private JPanel kpiPanel;     
    private YearMonth currentYearMonth;
    private LocalDate selectedDate;
    private LocalDate hoy;
    private String filtroSelectedDate;
    private HashMap<LocalDate, List<Turno>> turnosPorFecha;
    private ArrayList<HashMap<String, Object>> turnos;

    private TurnoController turnoControlador;

    //Colores

    private Color pendiente = new Color(100, 200, 255);
    private Color terminado = new Color(100, 255, 180);
    private Color atrasado = new Color(255, 80, 80);
    private Color total = new Color(255, 200, 0);

    public DashboardPanel() {
        this.setLayout(new BorderLayout());
        currentYearMonth = YearMonth.now();
        hoy = LocalDate.now();
        selectedDate = hoy;
        turnosPorFecha = new HashMap<>();
        turnoControlador = TurnoController.getInstance();
        this.filtroSelectedDate = FechaUtils.ldToString(selectedDate);

        turnos = new ArrayList<>();
        initTop();
        initKpiPanel();
        initBody();
        renderCalendario();
        renderEventos();
//        turnoControlador.filtrar("", filtroSelectedDate, filtroSelectedDate, "", "" + 1, "" + 20, new DatosListener<List<HashMap<String, Object>>>() {
//            @Override
//            public void onSuccess(List<HashMap<String, Object>> datos) {
//            }
//
//            @Override
//            public void onError(String mensajeError) {
//                Dialogos.mostrarError(mensajeError);
//                revalidate();
//                repaint();
//            }
//
//            @Override
//            public void onSuccess(List<HashMap<String, Object>> datos, Paginado p) {
//                turnos = new ArrayList<>(datos);
//                renderEventos();
//            }
//        });
    }

    private void initTop() {
        JPanel top = new JPanel(new MigLayout("insets 10", "[]push[]push[]", ""));
        btnAnterior = new JButton("<");
        mesLabel = new JLabel("", SwingConstants.CENTER);
        mesLabel.setFont(mesLabel.getFont().deriveFont(Font.BOLD, 18f));
        btnSiguiente = new JButton(">");

        top.add(btnAnterior);
        top.add(mesLabel, "center");
        top.add(btnSiguiente, "right");

        btnAnterior.addActionListener(e -> {
            currentYearMonth = currentYearMonth.minusMonths(1);
            renderCalendario();
        });

        btnSiguiente.addActionListener(e -> {
            currentYearMonth = currentYearMonth.plusMonths(1);
            renderCalendario();
        });

        this.add(top, BorderLayout.NORTH);
    }

    private void initBody() {
        JPanel body = new JPanel(new MigLayout("insets 0", "[fill, grow 30][fill, grow 70]", ""));

        calendarioPanel = new JPanel(new MigLayout("wrap 7, gap 5", "[]"));
        //calendarioPanel.setBackground(Color.RED);
        MisEstilos.aplicarEstilo(this, MisEstilos.MENU_LATERAL);
        eventosPanel = new JPanel(new BorderLayout());
        eventosPanel.setBorder(BorderFactory.createTitledBorder("Ventas del día"));

        eventosPanel.add(new JScrollPane(new JPanel()), BorderLayout.CENTER); //

        body.add(calendarioPanel, "grow");
        body.add(eventosPanel, "grow");

        this.add(body, BorderLayout.CENTER);
    }
	
    /*
     Leer como panel del capi.
    */
    private void initKpiPanel() {
        kpiPanel = new JPanel(new MigLayout("insets 10, center", "5[]10[]10[]10[]5"));
        MisEstilos.aplicarEstilo(kpiPanel, MisEstilos.MENU_LATERAL);
        this.add(kpiPanel, BorderLayout.SOUTH);

        // Placeholder data
        agregarCard("Ventas Efectivo", "12", pendiente);
        agregarCard("Ventas Transferencia", "3", atrasado);
        agregarCard("Cantidad Ventas", "27", terminado);
        agregarCard("Ventas Totales", "40", total);
    }

    private void agregarCard(String titulo, String valor, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(150, 80)); //TODO flatlafearlo 
        card.setBackground(color);
        card.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD));
        JLabel lblValor = new JLabel(valor, SwingConstants.CENTER);
        lblValor.setFont(lblValor.getFont().deriveFont(Font.BOLD, 24f));

        card.add(lblTitulo, BorderLayout.NORTH);
        card.add(lblValor, BorderLayout.CENTER);

        kpiPanel.add(card);
    }

	
    private void renderCalendario() {
        calendarioPanel.removeAll();

        mesLabel.setText(currentYearMonth.getMonth().getDisplayName(TextStyle.FULL, new Locale("es")).toUpperCase()
                + " " + currentYearMonth.getYear());

        // Día de la semana
        for (DayOfWeek dow : DayOfWeek.values()) {
            JLabel lbl = new JLabel(dow.getDisplayName(TextStyle.SHORT, new Locale("es")), SwingConstants.CENTER);
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
            calendarioPanel.add(lbl, "grow");
        }

        LocalDate primerDiaMes = currentYearMonth.atDay(1);
        int startDayOfWeek = primerDiaMes.getDayOfWeek().getValue(); // 1 = lunes
        int diasEnMes = currentYearMonth.lengthOfMonth();

        for (int i = 1; i < startDayOfWeek; i++) {
            calendarioPanel.add(new JLabel(""));
        }

        for (int day = 1; day <= diasEnMes; day++) {
            LocalDate fecha = currentYearMonth.atDay(day);
            JButton btnDia = new JButton(String.valueOf(day));


            if (fecha.equals(LocalDate.now())) {
                MisEstilos.aplicarEstilo(btnDia, MisEstilos.BOTON_MENU_ITEM);
            }

            if (turnosPorFecha.containsKey(fecha)) {
                btnDia.setBackground(new Color(0, 200, 255));
                btnDia.setForeground(Color.WHITE);
            }

            btnDia.addActionListener(e -> {
                selectedDate = fecha;
                this.filtroSelectedDate = FechaUtils.ldToString(selectedDate);
//                turnoControlador.filtrar("",filtroSelectedDate,filtroSelectedDate,"", "" + 1, "" + 20,new DatosListener<List<HashMap<String,Object>>>(){
//                    @Override
//                    public void onSuccess(List<HashMap<String,Object>> datos) {
//                    }
//
//                    @Override
//                    public void onError(String mensajeError) {
//                        Dialogos.mostrarError(mensajeError);
//                        revalidate();
//                        repaint();
//                    }
//
//                    @Override
//                    public void onSuccess(List<HashMap<String,Object>> datos, Paginado p) {
//                        turnos = new ArrayList<>(datos);
//                        renderEventos();
//                    }
//                });

            });

            calendarioPanel.add(btnDia, "grow");
        }

        calendarioPanel.revalidate();
        calendarioPanel.repaint();
    }

    private void renderEventos() {
        JPanel listaPanel = new JPanel(new MigLayout("wrap 1, fillx", "[grow, fill]"));
        for (HashMap<String, Object> map : turnos) {
            Turno t = (Turno) map.get("turno");
            //Cliente c = (Cliente) map.get("cliente");
            Pedido p = (Pedido) map.get("pedido");
            Orden o = (Orden) map.get("orden");

            String estado = t.getEstadoTurno();
            String desc;

            if(o!=null)
                desc = o.getDescripcion();
            else
                desc = p.getDescripcion();

            JLabel lbl = new JLabel( "("+estado+")"+ " - " +desc);
            JButton btn = new JButton( "("+estado+")"+ " - " +desc);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.addActionListener(e -> {
                /*
                if(o!=null){
                    OrdenModal modal = new OrdenModal(MiFrame.getInstance(), o, false);
                    modal.setVisible(true);
                }
                else{
                    PedidoModal modal = new PedidoModal(MiFrame.getInstance(), p, false);
                    modal.setVisible(true);
                }

                 */

            });
            //lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, queColor(t)));

            listaPanel.add(btn, "growx");
        }

        // Forzar crecimiento vertical (opcional)
        listaPanel.setPreferredSize(new Dimension(listaPanel.getPreferredSize().width, turnos.size() * 40));

        JScrollPane scroll = new JScrollPane(listaPanel);
        eventosPanel.removeAll();
        eventosPanel.add(scroll, BorderLayout.CENTER);
        eventosPanel.revalidate();
        eventosPanel.repaint();
    }

    private Color queColor(Turno t){
        Color color = pendiente;
        String estado = t.getEstadoTurno();
        String fechaHoraStr = t.getFechaInicio();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        LocalDateTime dateTime = LocalDateTime.parse(fechaHoraStr, formatter);
        LocalDate fecha = dateTime.toLocalDate();
        switch (estado){
            case "PENDIENTE":
                if(fecha.isBefore(hoy))
                    color =atrasado;
                break;
            case "TERMINADO":
                color = terminado;
                break;
        }
        return color;
    }
}  

