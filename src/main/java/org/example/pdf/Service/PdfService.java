package org.example.pdf.Service;

import lombok.RequiredArgsConstructor;
import org.example.pdf.pdf.TemplateParser;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PdfService {
    private final TemplateParser templateParser;

    public String createPdf(String thymeleafHtml, Map<String, Object> map) {
        String processHtml = templateParser.parserHtmlFileToString("index", map);
        return processHtml;
    }
}
