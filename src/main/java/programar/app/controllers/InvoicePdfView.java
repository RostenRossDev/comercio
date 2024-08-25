package programar.app.controllers;

import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import programar.app.entities.Factura;
import programar.app.entities.Item;

import java.awt.*;
import java.util.Map;

@Component("factura/ver")
public class InvoicePdfView  extends AbstractPdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception {

        Factura factura= (Factura) model.get("factura");

        MessageSourceAccessor mensajes = getMessageSourceAccessor();

        PdfPTable tabla= new PdfPTable(1);

        PdfPCell cell = null;

        cell= new PdfPCell(new Phrase(mensajes.getMessage("Datos del cliente")));
        cell.setBackgroundColor(new Color(184,218,255));
        cell.setPadding(8f);
        tabla.addCell(cell);

        tabla.addCell(factura.getVenta().getCliente().getNombre()+" "+factura.getVenta().getCliente().getApellido());
        tabla.addCell(factura.getVenta().getCliente().getEmail());

        PdfPTable tabla2= new PdfPTable(1);
        tabla.setSpacingAfter(20);


        cell= new PdfPCell(new Phrase(mensajes.getMessage("Datos de la factura")));
        cell.setBackgroundColor(new Color(195, 230, 203));
        cell.setPadding(8f);
        tabla.addCell(cell);


        tabla2.addCell(mensajes.getMessage("folio")+": "+ factura.getId());
        tabla2.addCell(mensajes.getMessage("descripción")+": "+ "Pendiente de descripcion");
        tabla2.addCell(mensajes.getMessage("fecha")+": "+factura.getFechaEmision());

        document.add(tabla);
        document.add(tabla2);

        PdfPTable tabla3=  new PdfPTable(4);
        tabla3.setWidths(new float[] {3.5f, 1 , 1, 1});

        tabla3.addCell(mensajes.getMessage("Producto"));
        tabla3.addCell(mensajes.getMessage("Precio"));
        tabla3.addCell(mensajes.getMessage("Cantidad"));
        tabla3.addCell(mensajes.getMessage("Descuento"));
        tabla3.addCell(mensajes.getMessage("Total"));

        for (Item item : factura.getVenta().getItems()) {
            tabla3.addCell(item.getTitle());
            tabla3.addCell(item.getUnitPrice().toString());
            tabla3.addCell(item.getDiscount().toString());
            cell= new PdfPCell(new Phrase(item.getQuantity()));

            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

            tabla3.addCell(cell);
            tabla3.addCell(item.calcularImporte().toString());
        }

        cell = new PdfPCell(new Phrase(mensajes.getMessage("Gran Total")+": "));

        cell.setColspan(3);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tabla3.addCell(cell);
        tabla3.addCell(factura.getVenta().getTotal().toString()
        );

        document.add(tabla3);

    }



}
