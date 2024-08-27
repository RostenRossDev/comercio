package programar.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import programar.app.view.pdf.InvoicePdfView;

import java.util.Locale;

@Configuration
public class PdfViewResolver implements ViewResolver {

    @Bean
    public PdfViewResolver  customPdfViewResolver() {
        return new PdfViewResolver();
    }

    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        if (viewName.equals("invoicePdfView")) {
            return new InvoicePdfView();
        } else {
            return null;
        }
    }
}