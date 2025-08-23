
package com.OndaByte.MisterFront.vistas;

import com.OndaByte.MisterFront.sesion.SesionController;
import com.OndaByte.MisterFront.estilos.MisEstilos;
import com.OndaByte.MisterFront.vistas.caja.CajaPanel;
import com.OndaByte.MisterFront.vistas.movimientos.MovimientoPanel;
import com.OndaByte.MisterFront.vistas.caja.MostradorCajaPanel;
import com.OndaByte.MisterFront.vistas.clientes.ClientePanel;
import com.OndaByte.MisterFront.vistas.categorias.CategoriaPanel;
import com.OndaByte.MisterFront.vistas.gastosFijos.GastoFijoPanel;
import com.OndaByte.MisterFront.vistas.menuLateral.PanelLateral;
import com.OndaByte.MisterFront.vistas.usuarios.UsuarioTabs;
import com.OndaByte.MisterFront.vistas.dashboard.DashboardPanel;
import com.OndaByte.MisterFront.vistas.empleados.EmpleadoPanel;
import com.OndaByte.MisterFront.vistas.productos.ProductoPanel;
import com.OndaByte.MisterFront.vistas.util.EventosInterface;
import com.OndaByte.MisterFront.vistas.ventas.VentaPanel;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.util.UIScale;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/*
 *
 * Sería la aplicación, la ventana principal que pinta el Frame luego de Iniciar Sesion.
 */
public class ContenedorPrincipal extends JLayeredPane implements ContenedorPrincipalView, EventosInterface {

    private PanelLateral lateral;
    private JPanel central;
    private JButton botoncito;
    private JLabel titulo;

    private SesionController sesionControlador;

    public ContenedorPrincipal() {
        setBorder(new EmptyBorder(7, 7, 7, 7));
        setLayout(new ContenedorPrincipalLayout());//Inner class
        central = new JPanel(new BorderLayout());
        sesionControlador = SesionController.getInstance();
        sesionControlador.setvista(this);
        sesionControlador.initMenuRol();
    }

    @Override
    public void applyComponentOrientation(ComponentOrientation o) { // no se bien para que sirve todavía, ¿sacar?
        super.applyComponentOrientation(o);
    }

    private void initBotoncito() {
        if (botoncito == null) {
            botoncito = new JButton();
        }
        //String icon = (getComponentOrientation().isLeftToRight()) ? "menu_left.svg" : "menu_right.svg";
        //String icon = (getComponentOrientation().isLeftToRight()) ? "menu_left.svg" : "menu_right.svg";
        //botoncito.setIcon(new FlatSVGIcon("com/OndaByte/MisterFront/icon/svg/9.svg", 0.8f));
        //botoncito.setIcon(new FlatSVGIcon(getClass().getClassLoader().getResource("icon/menu_left.svg")));
        botoncito.setText("<");
        botoncito.putClientProperty(FlatClientProperties.STYLE, MisEstilos.BOTONCITO);
        botoncito.addActionListener((ActionEvent e) -> {
            setMenuFull(!lateral.isMenuFull());
        });
    }

    private void setMenuFull(boolean full) {
        String icon;
        if (getComponentOrientation().isLeftToRight()) {
            //icon = (full) ? "menu_left.svg" : "menu_right.svg";
            icon = (full) ? "<" : ">";
        } else {
            //icon = (full) ? "menu_right.svg" : "menu_left.svg";
            icon = (full) ? "<" : ">";
        }
        //botoncito.setIcon(new FlatSVGIcon("com/OndaByte/MisterFront/icon/svg/" + icon, 0.8f));
        //botoncito.setIcon(new FlatSVGIcon(getClass().getClassLoader().getResource("icon/"+ icon)));
        botoncito.setText(icon);
        lateral.setMenuFull(full);
        revalidate();
    }

    public void hideMenu() {
        lateral.hideMenuItem();
    }

    public void renderCentral(String titulo, Component component) {
        central.removeAll();
        central.setLayout(new BorderLayout());
        this.titulo.setText("Gestión de " + titulo);
        this.titulo.putClientProperty(FlatClientProperties.STYLE, MisEstilos.TITULO);
        this.titulo.setHorizontalAlignment(SwingConstants.CENTER);
        central.add(this.titulo,BorderLayout.NORTH);
        central.add(component,BorderLayout.CENTER);
        central.repaint();
        central.revalidate();
    }


    private void initCentral(){
        this.titulo = new JLabel();
        this.add(central);
    }
    @Override
    public void renderMenuLateral(String rolname) {
        lateral = new PanelLateral(rolname);
        initBotoncito();
        this.setLayer(botoncito, JLayeredPane.POPUP_LAYER); // Manejo de eje Z
        this.add(botoncito);
        this.add(lateral);
        initCentral();
    }

    @Override
    public void runEvento(String evento) {
        switch (evento) {
            case "Dashboard":
                this.renderCentral(evento,new DashboardPanel());
                break;
            case "Caja":
                this.renderCentral(evento,new MostradorCajaPanel());
                break;
            case "Resumen Cajas":
                this.renderCentral(evento,new CajaPanel());
                break;
            case "Movimientos":
                this.renderCentral(evento,new MovimientoPanel());
                break;
            case "Ventas":
                this.renderCentral(evento,new VentaPanel());
                break;
//            case "Historial/Reportes":
//                this.renderCentral(evento,new MovimientoPanel());
//                break;
            case "Productos":
                this.renderCentral(evento,new ProductoPanel());
                break;
            case "Empleados":
                this.renderCentral(evento,new EmpleadoPanel());
                break;
            case "Categorias":
                this.renderCentral(evento,new CategoriaPanel());
                break;
            case "Clientes":
                this.renderCentral(evento,new ClientePanel());
                break;
            case "Gastos Fijos":
                this.renderCentral(evento,new GastoFijoPanel());
                break;
            case "Perfil":
                this.renderCentral(evento,new UsuarioTabs());
                break;
            case "Cerrar Sesion":
                MiFrame.logout();
                break;
            default:
                break;
        }
    }

    /**
     * Layout Choreado, Implementa el layout manager para:
     .
     * settear las dimenciones
     * y animaciones del menu
     * y disposicion de la pantalla
     * y el botoncito.
     */
    private class ContenedorPrincipalLayout implements LayoutManager {

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(5, 5);
            }
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(0, 0);
            }
        }

        @Override
        public void layoutContainer(Container parent) { // Lógica renderizado de Menú lateral, cálculos de dimensiones
            if(lateral != null && central != null && botoncito != null){
                synchronized (parent.getTreeLock()) {
                    boolean ltr = parent.getComponentOrientation().isLeftToRight();
                    Insets insets = parent.getInsets();

                    insets = UIScale.scale(insets);
                    int x = insets.left;
                    int y = insets.top;
                    int width = parent.getWidth() - (insets.left + insets.right);
                    int height = parent.getHeight() - (insets.top + insets.bottom);
                    int menuWidth = UIScale.scale(lateral.isMenuFull() ? lateral.getMenuMaxWidth() : lateral.getMenuMinWidth());
                    int menuX = ltr ? x : x + width - menuWidth;
                    lateral.setBounds(menuX, y, menuWidth, height); // settea bounds
                    int menuButtonWidth = botoncito.getPreferredSize().width;
                    int menuButtonHeight = botoncito.getPreferredSize().height;
                    int menubX;
                    if (ltr) {
                        menubX = (int) (x + menuWidth - (menuButtonWidth * (lateral.isMenuFull() ? 0.5f : 0.3f)));
                    } else {
                        menubX = (int) (menuX - (menuButtonWidth * (lateral.isMenuFull() ? 0.5f : 0.7f)));
                    }
                    botoncito.setBounds(menubX, UIScale.scale(30), menuButtonWidth, menuButtonHeight); // settea bounds
                    int gap = UIScale.scale(5);
                    int bodyWidth = width - menuWidth - gap;
                    int bodyHeight = height;
                    int bodyx = ltr ? (x + menuWidth + gap) : x;
                    int bodyy = y;
                    central.setBounds(bodyx, bodyy, bodyWidth, bodyHeight); // settea bounds
                }
            }
        }
    }
}