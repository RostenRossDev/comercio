package programar.app.view.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Chunk;

import com.lowagie.text.pdf.draw.LineSeparator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import programar.app.entities.Direccion;
import programar.app.entities.Factura;

import java.awt.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Log4j2
@Component("invoicePdfView")
public class InvoicePdfView  extends AbstractPdfView {

//    @Override
//    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
//                                    HttpServletRequest request, HttpServletResponse response) throws Exception {
//
//        Factura factura= (Factura) model.get("factura");
//
//        PdfPTable tabla= new PdfPTable(1);
//
//        PdfPCell cell = null;
//
//        cell= new PdfPCell(new Phrase("Datos del cliente"));
//        cell.setBackgroundColor(new Color(184,218,255));
//        cell.setPadding(8f);
//        tabla.addCell(cell);
//
//        tabla.addCell(factura.getVenta().getCliente().getNombre()+" "+factura.getVenta().getCliente().getApellido());
//        tabla.addCell(factura.getVenta().getCliente().getEmail());
//
//        PdfPTable tabla2= new PdfPTable(1);
//        tabla.setSpacingAfter(20);
//
//
//        cell= new PdfPCell(new Phrase("Datos de la factura"));
//        cell.setBackgroundColor(new Color(195, 230, 203));
//        cell.setPadding(8f);
//        tabla.addCell(cell);
//
//
//        tabla2.addCell("folio"+": "+ factura.getId());
//        tabla2.addCell("descripción"+": "+ "Pendiente de descripcion");
//        tabla2.addCell("fecha"+": "+factura.getFechaEmision());
//
//        document.add(tabla);
//        document.add(tabla2);
//
//        PdfPTable tabla3=  new PdfPTable(4);
//        tabla3.setWidths(new float[] {3.5f, 1 , 1, 1});
//
//        tabla3.addCell("Producto");
//        tabla3.addCell("Precio");
//        tabla3.addCell("Cantidad");
//        tabla3.addCell("Descuento");
//        tabla3.addCell("Total");
//
//        for (Item item : factura.getVenta().getItems()) {
//            tabla3.addCell(item.getTitle());
//            tabla3.addCell(item.getUnitPrice().toString());
//            tabla3.addCell(item.getDiscount().toString());
//            cell= new PdfPCell(new Phrase(item.getQuantity()));
//
//            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//
//            tabla3.addCell(cell);
//            tabla3.addCell(item.calcularImporte().toString());
//        }
//
//        cell = new PdfPCell(new Phrase("Gran Total: "));
//
//        cell.setColspan(3);
//        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
//        tabla3.addCell(cell);
//        tabla3.addCell(factura.getVenta().getTotal().toString()
//        );
//
//        document.add(tabla3);
//    }

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception {

        Factura factura= (Factura) model.get("factura");

        // Cabecera con logo, constancia de pago, fecha y número de comprobante
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{2, 3});

        InputStream input = getClass().getResourceAsStream("/static/img/logo.png");

        byte[] bytes = IOUtils.toByteArray(input);

        Image logo = Image.getInstance(bytes);
        logo.scaleToFit(200, 100);
        PdfPCell logoCell = new PdfPCell(logo);
        logoCell.setTop(0f); // Negative padding to move the logo upwards
        logoCell.setBorder(PdfPCell.NO_BORDER);
        headerTable.addCell(logoCell);

        // Agregar constancia de pago, fecha y número de comprobante
        PdfPCell constanciaCell = new PdfPCell();
        constanciaCell.setBorder(PdfPCell.NO_BORDER);
        constanciaCell.addElement(new Phrase("CONSTANCIA DE PAGO", new Font(Font.HELVETICA, 14, Font.BOLD)));

        constanciaCell.addElement(new Phrase("Fecha: " + getNow(), new Font(Font.HELVETICA, 10)));

        String numeroComprobante = String.format("#   %08d", factura.getId()); // Formatear el número de comprobante
        constanciaCell.addElement(new Phrase(numeroComprobante, new Font(Font.HELVETICA, 10)));

        constanciaCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        constanciaCell.setPaddingLeft(100);
        headerTable.addCell(constanciaCell);

        document.add(headerTable);

        // Espacio después de la cabecera
        document.add(Chunk.NEWLINE);

        // Sección de información del local
        PdfPTable localInfoTable = new PdfPTable(1);
        localInfoTable.setWidthPercentage(100);

        PdfPCell localNameCell = new PdfPCell(new Phrase("Nombre del Local", new Font(Font.HELVETICA, 12, Font.BOLD)));
        localNameCell.setBorder(PdfPCell.NO_BORDER);
        localInfoTable.addCell(localNameCell);

        PdfPCell cuitCell = new PdfPCell(new Phrase("CUIT: 20-12345678-9", new Font(Font.HELVETICA, 10)));
        cuitCell.setBorder(PdfPCell.NO_BORDER);
        localInfoTable.addCell(cuitCell);

        PdfPCell addressCell = new PdfPCell(new Phrase("Calle Falsa 1234, Resistencia, Chaco", new Font(Font.HELVETICA, 10)));
        addressCell.setBorder(PdfPCell.NO_BORDER);
        localInfoTable.addCell(addressCell);

        // Agregar 2 saltos de línea y una raya horizontal
        localInfoTable.addCell(createEmptyCell(2));
        PdfPCell separatorCell = new PdfPCell(new Phrase(new Chunk(new LineSeparator())));
        separatorCell.setBorder(PdfPCell.NO_BORDER);
        separatorCell.setPaddingTop(10f);
        separatorCell.setPaddingBottom(10f);
        localInfoTable.addCell(separatorCell);

        document.add(localInfoTable);

        // Espacio antes de la siguiente sección
        document.add(Chunk.NEWLINE);

        // Sección de información del cliente
        PdfPTable clienteTable = new PdfPTable(1);
        clienteTable.setWidthPercentage(100);

        PdfPCell clienteInfoCell = new PdfPCell(new Phrase("Nombre: ".concat(factura.getVenta().getCliente().getApellido()).concat(", ")
                .concat(factura.getVenta().getCliente().getNombre()), new Font(Font.HELVETICA, 10)));
        clienteInfoCell.setBorder(PdfPCell.NO_BORDER);
        clienteTable.addCell(clienteInfoCell);

        List<Direccion> direcciones = factura.getVenta().getCliente().getDirecciones();
        Direccion  direccion = direcciones.size() > 0 ?  direcciones.get(0) : null;
        PdfPCell clienteAddresCell = new PdfPCell(new Phrase("Domicilio del Cliente: "
                .concat(direccion != null ? direccion.getCalle().getNombre().concat(" ").concat(String.valueOf(direccion.getAltura().getNumero())) : "-------------"), new Font(Font.HELVETICA, 10)));
        clienteAddresCell.setBorder(PdfPCell.NO_BORDER);
        clienteTable.addCell(clienteAddresCell);

        PdfPCell clienteTelCell = new PdfPCell(new Phrase("Telefono: ".concat(factura.getVenta().getCliente().getTelefono()), new Font(Font.HELVETICA, 10)));
        clienteTelCell.setBorder(PdfPCell.NO_BORDER);
        clienteTable.addCell(clienteTelCell);

        PdfPCell clienteEmailCell = new PdfPCell(new Phrase("Email: ".concat(factura.getVenta().getCliente().getEmail()), new Font(Font.HELVETICA, 10)));
        clienteEmailCell.setBorder(PdfPCell.NO_BORDER);
        clienteTable.addCell(clienteEmailCell);

        // Agregar 2 saltos de línea
        clienteTable.addCell(createEmptyCell(2));

        PdfPCell medioPagoCell = new PdfPCell(new Phrase("Medio de Pago: Tarjeta de Crédito", new Font(Font.HELVETICA, 10)));
        medioPagoCell.setBorder(PdfPCell.NO_BORDER);
        clienteTable.addCell(medioPagoCell);

        document.add(clienteTable);

        // Tabla de productos
        PdfPTable productTable = new PdfPTable(4);
        productTable.setWidthPercentage(100);
        productTable.setSpacingBefore(20f);
        productTable.setWidths(new float[]{3.5f, 1, 1, 1});

        productTable.addCell(createHeaderCell("Detalle"));
        productTable.addCell(createHeaderCell("Descuento"));
        productTable.addCell(createHeaderCell("Precio Unitario"));
        productTable.addCell(createHeaderCell("Importe Total"));



        factura.getVenta().getItems().forEach(item ->{
            Double percentDiscount = (double) (item.getDiscount()/100);
            Double total  = (item.getUnitPrice() * item.getQuantity())-(item.getUnitPrice() * percentDiscount);
            productTable.addCell(item.getTitle().concat(" x").concat(String.valueOf(item.getQuantity())));
            productTable.addCell(String.valueOf(item.getDiscount()).concat("%"));
            productTable.addCell("$".concat(String.valueOf(item.getUnitPrice())));
            productTable.addCell("$".concat(String.valueOf( total))) ;
        });
//        // Agregar filas de ejemplo (mock)
//        productTable.addCell("Producto 1 x 2");
//        productTable.addCell("10%");
//        productTable.addCell("$50.00");
//        productTable.addCell("$90.00");
//
//        productTable.addCell("Producto 2 x 1");
//        productTable.addCell("5%");
//        productTable.addCell("$100.00");
//        productTable.addCell("$95.00");

        // Celda de Total
        PdfPCell totalLabelCell = new PdfPCell(new Phrase("Total", new Font(Font.HELVETICA, 10, Font.BOLD)));
        totalLabelCell.setColspan(3);
        logoCell.setVerticalAlignment(Element.ALIGN_TOP); // Alineación vertical al top
        totalLabelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        productTable.addCell(totalLabelCell);

        productTable.addCell("$".concat(String.valueOf(factura.getVenta().getTotal())));

        document.add(productTable);
    }

    // Método para crear celdas de encabezado
    private PdfPCell createHeaderCell(String content) {
        PdfPCell cell = new PdfPCell(new Phrase(content, new Font(Font.HELVETICA, 10, Font.BOLD)));
        cell.setBackgroundColor(new Color(230, 230, 230));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5f);
        return cell;
    }

    // Método para crear celdas vacías
    private PdfPCell createEmptyCell(int lines) {
        PdfPCell emptyCell = new PdfPCell(new Phrase(Chunk.NEWLINE));
        emptyCell.setBorder(PdfPCell.NO_BORDER);
        emptyCell.setColspan(lines);
        return emptyCell;
    }

    private String getNow(){
        // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();

        // Formatear la fecha al formato deseado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaFormateada = fechaActual.format(formatter);
        return fechaFormateada;
    }
}
