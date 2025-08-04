
package com.OndaByte.AntartidaFront.vistas.util;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLDocument;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URL;

public class VistaPreviaImpresion extends JDialog {
    private JEditorPane editorPane;
    private JButton btnImprimir, btnGuardarPDF;
    private String htmlContent;
    private String nombreArchivo;
    private URL baseUrl = VistaPreviaImpresion.class.getClassLoader().getResource("templates/");

    public VistaPreviaImpresion(Frame owner, String htmlContent, String nombreArchivo) {
        super(owner, "Vista previa de impresión", true);
        this.htmlContent = htmlContent;
        this.nombreArchivo = nombreArchivo;
        initComponents();
    }

    private void initComponents() {
      try{
        setLayout(new BorderLayout());

        HTMLEditorKit kit = new HTMLEditorKit();
        HTMLDocument doc = (HTMLDocument) kit.createDefaultDocument();
        doc.setBase(baseUrl);
        kit.read(new StringReader(htmlContent), doc, 0);

        editorPane = new JEditorPane();
        
        editorPane.setEditorKit(kit);
        editorPane.setContentType("text/html");
        editorPane.setText(htmlContent);
        editorPane.setEditable(false);
        editorPane.setDocument(doc);

        JScrollPane scrollPane = new JScrollPane(editorPane);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnImprimir = new JButton("Imprimir");
        btnGuardarPDF = new JButton("Guardar PDF");

        panelBotones.add(btnGuardarPDF);
        panelBotones.add(btnImprimir);

        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnImprimir.addActionListener(this::imprimir);
        btnGuardarPDF.addActionListener(this::guardarPDF);

        setSize(800, 720);
        setLocationRelativeTo(null);
      }catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al visualizar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void imprimir(ActionEvent e) {
        try {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setJobName("Imprimir vista previa");

            // Se pasa el componente como Printable
            job.setPrintable(new Printable() {
                public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                    if (pageIndex > 0) return NO_SUCH_PAGE;

                    Graphics2D g2d = (Graphics2D) graphics;
                    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                    g2d.scale(0.75, 0.75); // Escalado opcional, ajustalo según el tamaño del contenido
                    editorPane.printAll(g2d);

                    return PAGE_EXISTS;
                }
            });

            boolean doPrint = job.printDialog();
            if (doPrint) {
                job.print();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al imprimir: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void guardarPDF(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(nombreArchivo + ".pdf"));
        int option = chooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try (OutputStream os = new FileOutputStream(file)) {
                PdfRendererBuilder builder = new PdfRendererBuilder();
                builder.useFastMode();
                builder.withHtmlContent(htmlContent, baseUrl.toExternalForm());
                builder.toStream(os);
                builder.run();
                JOptionPane.showMessageDialog(this, "PDF guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
