package com.fincons.taskmanager.fileTest;

import com.fincons.taskmanager.entity.Attachment;
import com.fincons.taskmanager.service.attachmentService.impl.AttachmentDecoding;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class fileTest {
    @Test
    public void testCalculatePDFByteArray() throws IOException {
        Path pdfFilePath = Path.of("C:\\Users\\lorenzo.gaetani\\Desktop\\POC-Athon\\Task-manager-BE\\src\\test\\resources\\file_5mb.pdf");

        // Calcola il byte array del file PDF
        byte[] pdfByteArray = Files.readAllBytes(pdfFilePath);

        // Verifica che la dimensione del byte array sia corretta (5 MB)
        long expectedSize = 5 * 1024 * 1024; // 5 MB in byte
        long allowableDifference = 100000; // 100 KB di tolleranza
        long actualSize = pdfByteArray.length;
        assertTrue(Math.abs(actualSize - expectedSize) <= allowableDifference,
                "Il byte array del PDF non ha la dimensione corretta");

        System.out.println("Lunghezza del byte array decodificato: " + pdfByteArray.length + " byte");



        String base64Encoded  = Base64.getEncoder().encodeToString(pdfByteArray);
        char[] base64Chars = base64Encoded.toCharArray();
        System.out.println("Length of char array: " + base64Chars.length);
        System.out.println("Length of Base64 string: " + base64Encoded.length());

    }
}
