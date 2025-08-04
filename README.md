# AntartidaFront
Front-End con Swing-Flatlaf

# Layout: MigLayout

http://miglayout.com/
http://www.miglayout.com/mavensite/docs/QuickStart.pdf
https://www.youtube.com/watch?v=Efjl5uSDrPk

#Config Flatlaf temas - GUIDE
# Guía de Personalización de FlatLaf con Temas Dinámicos

Esta guía proporciona una explicación detallada sobre cómo configurar y personalizar FlatLaf para que los colores y estilos cambien dinámicamente según el tema activo (modo claro u oscuro).

---

## 1. Estructura de Archivos de Configuración
Para lograr una personalización completa y dinámica en FlatLaf, se utilizan **tres archivos de configuración** dentro de `config/`:

- **`flatlaf.properties`** → Contiene la configuración base y colores de acento.
- **`flatDarkLaf.properties`** → Configuración y colores personalizados para Modo Oscuro.
- **`flatLightLaf.properties`** → Configuración y colores personalizados para Modo Claro.

FlatLaf selecciona automáticamente `flatDarkLaf.properties` o `flatLightLaf.properties` según el tema activo.

---

## 2. Variables Globales `@background`, `@foreground`

Definir colores clave como `@background` y `@foreground` permite referenciarlos en otros valores sin repetir código:

### **Modo Oscuro (`flatDarkLaf.properties`)**
```properties
@background=rgb(49,62,74)
@foreground=rgb(240,240,240)
Menu.background=darken(@background,10%)
```

### **Modo Claro (`flatLightLaf.properties`)**
```properties
@background=rgb(250,250,250)
@foreground=rgb(60,60,60)
Menu.background=rgb(36, 104, 155)
```

✅ **Ventaja:** Si cambias `@background`, todos los elementos que lo usen se actualizarán automáticamente.

---

## 3. Cálculos de Color Dinámicos
FlatLaf permite modificar los colores en función de otros colores con:
- `darken(color, porcentaje)` → Oscurece el color.
- `lighten(color, porcentaje)` → Aclara el color.
- `tint(color, porcentaje)` → Aumenta el tinte.
- `shade(color, porcentaje)` → Reduce el brillo.
- `mix(color1, color2, porcentaje)` → Mezcla dos colores.

Ejemplo:
```properties
Menu.lightdark.background=lighten($Menu.background,5%)
Menu.lightdark.button.background=tint($Menu.lightdark.background,20%)
```
✅ **Ventaja:** Permite generar variaciones de color sin definirlos manualmente.

---

## 4. Uso de `@accentColor` para Cambios Rápidos de Color
`@accentColor` define colores de énfasis que pueden cambiar en tiempo de ejecución.

Definiendo colores de acento en **Modo Oscuro**:
```properties
App.accent.default = #4B6EAF
App.accent.blue = #0A84FF
App.accent.purple = #BF5AF2
```

Definiendo colores de acento en **Modo Claro**:
```properties
App.accent.default = #2675BF
App.accent.blue = #007AFF
```

Asignando `@accentColor`:
```properties
@accentColor=$App.accent.orange
```

✅ **Ventaja:** Puedes cambiar el color de acento sin tocar el código.

**Cambio en tiempo real desde Java:**
```java
FlatLaf.setGlobalExtraDefaults(Collections.singletonMap("@accentColor", "#FF4500"));
FlatLaf.updateUI();
```

---

## 5. Cambio de Tema en Tiempo de Ejecución
Para cambiar entre **modo claro y oscuro**, usa:

```java
public class ThemeManager {
    public static void cambiarTema(boolean oscuro) {
        EventQueue.invokeLater(() -> {
            FlatAnimatedLafChange.showSnapshot();
            
            if (oscuro) {
                FlatDarkLaf.setup();
            } else {
                FlatLightLaf.setup();
            }

            FlatLaf.updateUI();
            
            for (Window window : Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(window);
            }
            
            FlatAnimatedLafChange.hideSnapshotWithAnimation();
        });
    }
}
```

**Ejemplo de uso:**
```java
boolean modoOscuro = true;
ThemeManager.cambiarTema(modoOscuro);
```

✅ **Ventaja:** FlatLaf cambia los colores y estilos según el tema activo.

---

## 6. Personalización de Componentes Específicos
FlatLaf permite personalizar componentes específicos como `ScrollBar`, `Toast`, `Login`:

```properties
ScrollBar.trackArc=999
ScrollBar.thumbArc=999
Login.background=lighten(@background,5%)
Toast.success.outlineColor=#1b5e20
Toast.error.outlineColor=#c62828
```
✅ **Ventaja:** Permite tener una UI coherente con el tema.

---

## 7. Carga Correcta de FlatLaf y Configuraciones
Para asegurarte de que los temas y colores se carguen correctamente, **inicializa FlatLaf así en `main()`**:

```java
public static void main(String[] args) {
    FlatLaf.registerCustomDefaultsSource("config"); // Registrar configuraciones
    FlatLightLaf.setup(); // Iniciar con tema claro
    
    SwingUtilities.invokeLater(() -> new MiFrame().setVisible(true));
}
```

✅ **Ventaja:** Carga las configuraciones antes de iniciar la UI.

---

# 🎯 Conclusión
✔ **Soporte dinámico para Modo Claro/Oscuro** 🎭  
✔ **Cambio automático de colores y estilos** 🎨  
✔ **Flexibilidad total para modificar cualquier detalle sin tocar el código** 🔥  

### 🚀 **¿Cómo jugar con estas configuraciones?**
1️⃣ **Modifica `@background` y `@foreground`** en los archivos de cada tema.  
2️⃣ **Prueba `lighten()`, `darken()`, `shade()`, `mix()`** para ver cambios dinámicos.  
3️⃣ **Cambia `@accentColor` en tiempo de ejecución** para personalizar la UI rápidamente.  
4️⃣ **Personaliza `Toast`, `Menu`, `ScrollBar`, `Login` y otros elementos** con colores dinámicos.  

🔹 **Con este sistema, tienes control total sobre el diseño de tu UI y puedes personalizarla sin tocar el código Java.** 🚀


