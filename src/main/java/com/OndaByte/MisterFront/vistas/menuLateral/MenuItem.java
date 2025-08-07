
package com.OndaByte.MisterFront.vistas.menuLateral;

import com.OndaByte.MisterFront.estilos.MisEstilos;
import com.OndaByte.MisterFront.vistas.MiFrame;
import com.OndaByte.MisterFront.vistas.menuLateral.PopupSubmenu;
import com.OndaByte.MisterFront.vistas.util.EventosInterface;
import com.OndaByte.MisterFront.vistas.util.IconSVG;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuItem extends JPanel implements EventosInterface{

    private final PanelLateral menu;
    private final String menus[];
    private String nombre;
    private final int menuItemHeight = 38;
    private final int subMenuItemHeight = 35;
    private final int subMenuLeftGap = 34;
    private final int firstGap = 5;
    private final int bottomGap = 5;
    private boolean menuShow;
    private float animate;

    private PopupSubmenu popup;

    public boolean isMenuShow() {
        return menuShow;
    }

    public void setMenuShow(boolean menuShow) {
        this.menuShow = menuShow;
    }

    public float getAnimate() {
        return animate;
    }

    public void setAnimate(float animate) {
        this.animate = animate;
    }

    public String[] getMenus() {
        return menus;
    }

    public MenuItem(PanelLateral menu, String menus[] ) {
        this.menu = menu;
        this.nombre = menus[0];
        this.menus = menus;
        init();
    }

    private String getNumeroIcon(String nombreIcon){
        String numero = "plus.svg";
        switch (nombreIcon) {
            case "Dashboard":
                numero = "icon/bulletin-notice.svg";
                break;
            case "Ordenes":
                numero = "icon/list.svg";
                break;
            case "Pedidos":
                numero = "icon/bookmark.svg";
                break;
            case "Turnos":
                numero = "icon/calendar.svg";
                break;
            case "Presupuestos":
                numero = "icon/clipboard-check.svg";
                break;
            case "Remitos":
                numero = "icon/clipboard-check.svg";
                break;
            case "Reparaciones":
                numero = "icon/hammer-claw.svg";
                break;
            case "Historial/Reportes":
                numero = "icon/clipboard-check.svg";
                break;
            case "Clientes":
                numero = "icon/persons.svg";
                break;
            case "Empleados":
                numero = "icon/person-checkmark.svg";
                break;
            case "Insumos":
                numero = "icon/hammer-claw.svg";
                break;
            case "Gastos Fijos":
                numero = "icon/coins.svg";
                break;
            case "Perfil":
                numero = "icon/perfil.svg";
                break;
            case "Cerrar Sesion":
                numero = "icon/down-left.svg";
                break;
            default:
                numero = "icon/plus.svg";
                break;
        }
        return numero;
    }

    @Override
    public void runEvento(String evento) {
        switch (evento){
                case "Ordenes":
                    if (menu.isMenuFull()) {
                            MenuAnimation.animate(MenuItem.this, !menuShow);
                        } else {
                            popup.show(MenuItem.this, (int) MenuItem.this.getWidth() + UIScale.scale(5), UIScale.scale(menuItemHeight) / 2);
                        }
                    break;
                default : MiFrame.getInstance().getAplicacion().runEvento(evento);
        }
    }

    private void init() {
        setLayout(new MenuLayout());
        putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.background;"
                + "foreground:$Menu.lineColor");
        for (int i = 0; i < menus.length; i++) {
            String menuActual = menus[i];
            JButton menuItem = createButtonItem(menuActual);
            menuItem.setHorizontalAlignment(menuItem.getComponentOrientation().isLeftToRight() ? JButton.LEADING : JButton.TRAILING);
            menuItem.setIcon(new IconSVG(getNumeroIcon(menuActual)));
            menuItem.addActionListener((ActionEvent e) -> {
                PanelLateral.colorNull();
                menuItem.setBackground(new Color(220, 220, 180));
                menuItem.setForeground(Color.BLACK);
                ((IconSVG)menuItem.getIcon()).setFiltro(3);
                runEvento(menuActual);
            });
            PanelLateral.botonesDelPanel(menuItem);
            add(menuItem);
        }
        popup = new PopupSubmenu(getComponentOrientation(), menu, menus);
    }

    /*
     * SIN USO
     */
    public void setSelectedIndex(int index) {
        int size = getComponentCount();
        boolean selected = false;
        for (int i = 0; i < size; i++) {
            Component com = getComponent(i);
            if (com instanceof JButton) {
                ((JButton) com).setSelected(i == index);
                if (i == index) {
                    selected = true;
                }
            }
        }
        ((JButton) getComponent(0)).setSelected(selected);
        popup.setSelectedIndex(index);
    }

    private JButton createButtonItem(String text) {
        JButton button = new JButton(text);
        button.putClientProperty(FlatClientProperties.STYLE, MisEstilos.BOTON_MENU_ITEM);
        return button;
    }

    public void hideMenuItem() {
        animate = 0;
        menuShow = false;
    }

    public void setFull(boolean full) {
        if (full) {
            int size = getComponentCount();
            for (int i = 0; i < size; i++) {
                Component com = getComponent(i);
                if (com instanceof JButton) {
                    JButton button = (JButton) com;
                    button.setText(menus[i]);
                    button.setHorizontalAlignment(getComponentOrientation().isLeftToRight() ? JButton.LEFT : JButton.RIGHT);
                }
            }
        } else {
            for (Component com : getComponents()) {
                if (com instanceof JButton) {
                    JButton button = (JButton) com;
                    button.setText("");
                    button.setHorizontalAlignment(JButton.CENTER);
                }
            }
            animate = 0f;
            menuShow = false;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (animate > 0) {
            int ssubMenuItemHeight = UIScale.scale(subMenuItemHeight);
            int ssubMenuLeftGap = UIScale.scale(subMenuLeftGap);
            int smenuItemHeight = UIScale.scale(menuItemHeight);
            int sfirstGap = UIScale.scale(firstGap);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Path2D.Double p = new Path2D.Double();
            int last = getComponent(getComponentCount() - 1).getY() + (ssubMenuItemHeight / 2);
            boolean ltr = getComponentOrientation().isLeftToRight();
            int round = UIScale.scale(10);
            int x = ltr ? (ssubMenuLeftGap - round) : (getWidth() - (ssubMenuLeftGap - round));
            p.moveTo(x, smenuItemHeight + sfirstGap);
            p.lineTo(x, last - round);
            for (int i = 1; i < getComponentCount(); i++) {
                int com = getComponent(i).getY() + (ssubMenuItemHeight / 2);
                p.append(createCurve(round, x, com, ltr), false);
            }
            g2.setColor(getForeground());
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2.setStroke(new BasicStroke(UIScale.scale(1f)));
            g2.draw(p);
            g2.dispose();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (menus.length > 1) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2.setColor(FlatUIUtils.getUIColor("Menu.arrowColor", getForeground()));
            int smenuItemHeight = UIScale.scale(menuItemHeight);
            boolean ltr = getComponentOrientation().isLeftToRight();
            g2.setStroke(new BasicStroke(UIScale.scale(1f)));
            if (menu.isMenuFull()) {
                int arrowWidth = UIScale.scale(10);
                int arrowHeight = UIScale.scale(5);
                int ax = ltr ? (getWidth() - arrowWidth * 2) : arrowWidth;
                int ay = (smenuItemHeight - arrowHeight) / 2;
                Path2D p = new Path2D.Double();
                p.moveTo(0, animate * arrowHeight);
                p.lineTo(arrowWidth / 2, (1f - animate) * arrowHeight);
                p.lineTo(arrowWidth, animate * arrowHeight);
                g2.translate(ax, ay);
                g2.draw(p);
            } else {
                int arrowWidth = UIScale.scale(4);
                int arrowHeight = UIScale.scale(8);
                int ax = ltr ? (getWidth() - arrowWidth - UIScale.scale(3)) : UIScale.scale(3);
                int ay = (smenuItemHeight - arrowHeight) / 2;
                Path2D p = new Path2D.Double();
                if (ltr) {
                    p.moveTo(0, 0);
                    p.lineTo(arrowWidth, arrowHeight / 2);
                    p.lineTo(0, arrowHeight);
                } else {
                    p.moveTo(arrowWidth, 0);
                    p.lineTo(0, arrowHeight / 2);
                    p.lineTo(arrowWidth, arrowHeight);
                }
                g2.translate(ax, ay);
                g2.draw(p);
            }
            g2.dispose();
        }
    }

    private Shape createCurve(int round, int x, int y, boolean ltr) {
        Path2D p2 = new Path2D.Double();
        p2.moveTo(x, y - round);
        p2.curveTo(x, y - round, x, y, x + (ltr ? round : -round), y);
        return p2;
    }

    private class MenuLayout implements LayoutManager {

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                Insets inset = parent.getInsets();
                int width = parent.getWidth();
                int height = inset.top + inset.bottom;
                int size = parent.getComponentCount();
                Component item = parent.getComponent(0);
                height += UIScale.scale(menuItemHeight);
                if (item.isVisible()) {
                    int subMenuHeight = size > 1 ? UIScale.scale(firstGap) + UIScale.scale(bottomGap) : 0;
                    for (int i = 1; i < size; i++) {
                        Component com = parent.getComponent(i);
                        if (com.isVisible()) {
                            subMenuHeight += UIScale.scale(subMenuItemHeight);
                        }
                    }
                    height += (subMenuHeight * animate);
                } else {
                    height = 0;
                }
                return new Dimension(width, height);
            }
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(0, 0);
            }
        }

        @Override
        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                boolean ltr = parent.getComponentOrientation().isLeftToRight();
                Insets insets = parent.getInsets();
                int x = insets.left;
                int y = insets.top;
                int width = parent.getWidth() - (insets.left + insets.right);
                int size = parent.getComponentCount();
                for (int i = 0; i < size; i++) {
                    Component com = parent.getComponent(i);
                    if (com.isVisible()) {
                        if (i == 0) {
                            int smenuItemHeight = UIScale.scale(menuItemHeight);
                            int sfirstGap = UIScale.scale(firstGap);
                            com.setBounds(x, y, width, smenuItemHeight);
                            y += smenuItemHeight + sfirstGap;
                        } else {
                            int ssubMenuLeftGap = UIScale.scale(subMenuLeftGap);
                            int subMenuX = ltr ? ssubMenuLeftGap : 0;
                            int ssubMenuItemHeight = UIScale.scale(subMenuItemHeight);
                            com.setBounds(x + subMenuX, y, width - ssubMenuLeftGap, ssubMenuItemHeight);
                            y += ssubMenuItemHeight;
                        }
                    }
                }
            }
        }
    }
}
