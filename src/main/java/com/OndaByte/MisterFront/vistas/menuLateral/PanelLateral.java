
package com.OndaByte.MisterFront.vistas.menuLateral;

import com.OndaByte.MisterFront.estilos.MisEstilos;
import com.OndaByte.MisterFront.temas.LightDarkMode;
import com.OndaByte.MisterFront.temas.ToolBarAccentColor;
import com.OndaByte.MisterFront.vistas.util.IconSVG;
import com.OndaByte.config.Constantes;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PanelLateral extends JPanel {
    private boolean menuFull = true;
    private final String headerName = "<HTML>Mantenimientos <br> Antártida </HTML>";

    protected final boolean hideMenuTitleOnMinimum = true;
    protected final int menuTitleLeftInset = 5;
    protected final int menuTitleVgap = 5;
    protected final int menuMaxWidth = 250;
    protected final int menuMinWidth = 60;
    protected final int headerFullHgap = 5;
    
    private JLabel header;
    private JScrollPane scroll;
    private JPanel panelMenu;
    private LightDarkMode lightDarkMode;
    private ToolBarAccentColor toolBarAccentColor;

    private static List<JButton> botones  = new ArrayList<>();

    private static Logger logger = LogManager.getLogger(PanelLateral.class.getName());

    static{
        if(logger.isDebugEnabled()){
            logger.debug("Init logger en PanelLateral");
        }
    }
    
    
    public PanelLateral(String rol) {
        init(rol);
    }

    private void init(String rol) {
        putClientProperty(FlatClientProperties.STYLE, MisEstilos.MENU_LATERAL);
        header = new JLabel(headerName);
        header.setIcon(new IconSVG(IconSVG.LOGO));
        header.putClientProperty(FlatClientProperties.STYLE, MisEstilos.MENU_LATERAL_HEADER);

        //  Menu
        scroll = new JScrollPane();
        panelMenu = new JPanel(new MenuItemLayout(this));
        panelMenu.putClientProperty(FlatClientProperties.STYLE, MisEstilos.MENU_LATERAL_MENU);

        scroll.setViewportView(panelMenu);
        scroll.putClientProperty(FlatClientProperties.STYLE, "border:null");
        JScrollBar vscroll = scroll.getVerticalScrollBar();
        vscroll.setUnitIncrement(10);
        vscroll.putClientProperty(FlatClientProperties.STYLE, MisEstilos.MENU_LATERAL_SCROLL);
        createMenu(rol);
        lightDarkMode = new LightDarkMode();
        toolBarAccentColor = new ToolBarAccentColor(this);
        toolBarAccentColor.setVisible(FlatUIUtils.getUIBoolean("AccentControl.show", false));
        setLayout(new MenuLayout());
        add(header);
        add(scroll);
        add(lightDarkMode);
        add(toolBarAccentColor);
    }


    private void createMenu(String role) {
        LinkedHashMap<String, String[][]> menuItems = (LinkedHashMap<String, String[][]>) Constantes.getMenuItems(role);
        ArrayList<String> categorias = new ArrayList<>(menuItems.keySet()); 
        for (int i = 0; i < categorias.size(); i++) {
            panelMenu.add(createTitle(categorias.get(i)));
            String[][] mitems =  menuItems.get(categorias.get(i));
            for (int j = 0; j < mitems.length; j++) {
                MenuItem menuItem = new MenuItem(this, mitems[j]);
                panelMenu.add(menuItem);
            }
        }
    }

    public static void botonesDelPanel(JButton c){
        botones.add(c);
    }

    public static void colorNull(){
        for (JButton com : botones) {
            com.setBackground(null);
            com.setForeground(new Color(64,64,64));

            ((IconSVG)com.getIcon()).setFiltro(1);
        }
    }
    
    public boolean isMenuFull() {
        return menuFull;
    }

    public void setMenuFull(boolean menuFull) {
        this.menuFull = menuFull;
        if (menuFull) {
            header.setText(headerName);
            header.setHorizontalAlignment(getComponentOrientation().isLeftToRight() ? JLabel.LEFT : JLabel.RIGHT);
        } else {
            header.setText("");
            header.setHorizontalAlignment(JLabel.CENTER);
        }
        for (Component com : panelMenu.getComponents()) {
            if (com instanceof MenuItem) {
                ((MenuItem) com).setFull(menuFull);
            }
        }
        lightDarkMode.setMenuFull(menuFull);
        toolBarAccentColor.setMenuFull(menuFull);
    }


    private JLabel createTitle(String title) {
        String menuName = title.substring(1, title.length() - 1);
        JLabel lbTitle = new JLabel(" ~  ~  ~ "+menuName+" ~  ~  ~ ");
        //lbTitle.setIcon(new IconSVG(IconSVG.DEFAULT));//ESTOS LABEL NO TIENEN ICONOS
        lbTitle.putClientProperty(FlatClientProperties.STYLE, MisEstilos.MENU_LATERAL_ITEM);
        return lbTitle;
    }

    public void hideMenuItem() {
        for (Component com : panelMenu.getComponents()) {
            if (com instanceof MenuItem) {
                ((MenuItem) com).hideMenuItem();
            }
        }
        revalidate();
    }

    public boolean isHideMenuTitleOnMinimum() {
        return hideMenuTitleOnMinimum;
    }

    public int getMenuTitleLeftInset() {
        return menuTitleLeftInset;
    }

    public int getMenuTitleVgap() {
        return menuTitleVgap;
    }

    public int getMenuMaxWidth() {
        return menuMaxWidth;
    }

    public int getMenuMinWidth() {
        return menuMinWidth;
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
        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                Insets insets = parent.getInsets();
                int x = insets.left;
                int y = insets.top;
                int gap = UIScale.scale(5);
                int sheaderFullHgap = UIScale.scale(headerFullHgap);
                int width = parent.getWidth() - (insets.left + insets.right);
                int height = parent.getHeight() - (insets.top + insets.bottom);
                int iconWidth = width;
                int iconHeight = header.getPreferredSize().height;
                int hgap = menuFull ? sheaderFullHgap : 0;
                int accentColorHeight = 0;
                if (toolBarAccentColor.isVisible()) {
                    accentColorHeight = toolBarAccentColor.getPreferredSize().height+gap;
                }

                header.setBounds(x + hgap, y, iconWidth - (hgap * 2), iconHeight);
                int ldgap = UIScale.scale(10);
                int ldWidth = width - ldgap * 2;
                int ldHeight = lightDarkMode.getPreferredSize().height;
                int ldx = x + ldgap;
                int ldy = y + height - ldHeight - ldgap  - accentColorHeight;

                int menux = x;
                int menuy = y + iconHeight + gap;
                int menuWidth = width;
                int menuHeight = height - (iconHeight + gap) - (ldHeight + ldgap * 2) - (accentColorHeight);
                scroll.setBounds(menux, menuy, menuWidth, menuHeight);

                lightDarkMode.setBounds(ldx, ldy, ldWidth, ldHeight);

                if (toolBarAccentColor.isVisible()) {
                    int tbheight = toolBarAccentColor.getPreferredSize().height;
                    int tbwidth = Math.min(toolBarAccentColor.getPreferredSize().width, ldWidth);
                    int tby = y + height - tbheight - ldgap;
                    int tbx = ldx + ((ldWidth - tbwidth) / 2);
                    toolBarAccentColor.setBounds(tbx, tby, tbwidth, tbheight);
                }
            }
        }
    }
}
