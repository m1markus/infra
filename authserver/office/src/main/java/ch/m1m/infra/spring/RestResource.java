package ch.m1m.infra.spring;

import ch.m1m.infra.billing.PdfGenerator;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

// http://localhost:8080/pdf

@RestController("/")
public class RestResource {

    private final PdfGenerator pdfGenerator = new PdfGenerator();

    @GetMapping("/pdf")
    public ResponseEntity<Resource> download(String param) throws IOException {
        byte[] pdfBytes = pdfGenerator.generateSimplePdfAsByteArray();
        ByteArrayResource resource = new ByteArrayResource(pdfBytes);
        return ResponseEntity.ok()
                .contentLength(pdfBytes.length)
                // to save on disc
                //.contentType(MediaType.APPLICATION_OCTET_STREAM)
                // show content in browser
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}

/*

to save the pdf:
content-type: application octet stream
content-disposition: "attachment; filename="simple.pdf"



 */
