package org.example.pdf.Controller;

import com.lowagie.text.pdf.BaseFont;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.pdf.Service.PdfService;
import org.example.pdf.pdf.ImgReplaceElementFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class IndexController {

    private final PdfService pdfService;
    private final ImgReplaceElementFactory imgReplaceElementFactory;

    @GetMapping("/index")
    public void index(HttpServletResponse response) throws IOException {
        Map<String, Object> map = Map.of(
                "title", "타이틀 테스트",
                "message", "메세지 테스트 ",
                "imagename", "~/images/test_image.jpg"
        );
        String processHtml = pdfService.createPdf("index", map);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();

        // 이미지 설정
        SharedContext sharedContext = renderer.getSharedContext();
        sharedContext.setPrint(true);
        sharedContext.setInteractive(false);
        sharedContext.setReplacedElementFactory(imgReplaceElementFactory);
        sharedContext.getTextRenderer().setSmoothingThreshold(0);           // 라운드로 깎는거?


        // 폰트 설정
        renderer.getFontResolver()
                        .addFont(new ClassPathResource("/static/font/NanumGothic.ttf").getURL().toString(),
                                BaseFont.IDENTITY_H,
                                BaseFont.EMBEDDED);

        renderer.setDocumentFromString(processHtml);   // 입력

        renderer.layout();
        renderer.createPDF(outputStream);
        response.setContentType("application/pdf");

        // UTF-8로 인코딩된 파일명 설정
        String encodedFileName = URLEncoder.encode("피디에프-한글테스트.pdf", StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);


        response.getOutputStream().write(outputStream.toByteArray());
        response.getOutputStream().flush();

        renderer.finishPDF();
        outputStream.close();
    }

    @GetMapping("/download-pdf")
    public ResponseEntity<byte[]> downloadPdf() {
        Map<String, Object> map = Map.of(
                "title", "타이틀 테스트",
                "message", "메세지 테스트",
                "imagename", "~/images/test_image.jpg"
        );

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            // HTML로부터 PDF 생성
            String processHtml = pdfService.createPdf("index", map);
            ITextRenderer renderer = new ITextRenderer();

            // 이미지 설정
            SharedContext sharedContext = renderer.getSharedContext();
            sharedContext.setPrint(true);
            sharedContext.setInteractive(false);
            sharedContext.setReplacedElementFactory(imgReplaceElementFactory);
            sharedContext.getTextRenderer().setSmoothingThreshold(0);

            // 폰트 설정
            renderer.getFontResolver().addFont(
                    new ClassPathResource("/static/font/NanumGothic.ttf").getURL().toString(),
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED
            );

            // PDF 렌더링
            renderer.setDocumentFromString(processHtml);
            renderer.layout();
            renderer.createPDF(outputStream);

            // PDF 바이트 배열 생성
            byte[] pdfBytes = outputStream.toByteArray();

            // 성공 응답
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String encodedFileName = URLEncoder.encode("피디에프-한글테스트.pdf", StandardCharsets.UTF_8);
            headers.setContentDispositionFormData("attachment", encodedFileName);
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            // 실패 응답 (예: PDF 생성 실패)
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    String username = "username";
    @GetMapping("/download-pdf2")
    public ResponseEntity<byte[]> downloadPdf2() {
        Map<String, Object> map = Map.of(
                "title", "타이틀 테스트",
                "message", "메세지 테스트",
                "imagename", "~/images/test_image.jpg"
        );

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            // HTML로부터 PDF 생성
            String processHtml = pdfService.createPdf("index", map);
            ITextRenderer renderer = new ITextRenderer();

            // 이미지 설정
            SharedContext sharedContext = renderer.getSharedContext();
            sharedContext.setPrint(true);
            sharedContext.setInteractive(false);
            sharedContext.setReplacedElementFactory(imgReplaceElementFactory);
            sharedContext.getTextRenderer().setSmoothingThreshold(0);

            // 폰트 설정
            renderer.getFontResolver().addFont(
                    new ClassPathResource("/static/font/NanumGothic.ttf").getURL().toString(),
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED
            );

            // PDF 렌더링
            renderer.setDocumentFromString(processHtml);
            renderer.layout();
            renderer.createPDF(outputStream);

            // PDF 바이트 배열 생성
            byte[] pdfBytes = outputStream.toByteArray();

            // 파일명 설정 (예: {user의 이름}_이력서.pdf)
            String fileName = URLEncoder.encode(username + "_이력서.pdf", StandardCharsets.UTF_8);

            // 성공 응답
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.set("Content-Disposition", "attachment; filename*=UTF-8''" + fileName); // 수정된 부분
            System.out.println(fileName);
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
