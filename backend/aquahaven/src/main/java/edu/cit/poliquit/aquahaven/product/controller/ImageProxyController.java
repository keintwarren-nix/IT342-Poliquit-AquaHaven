package edu.cit.poliquit.aquahaven.product.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Set;

/**
 * DESIGN PATTERN — Proxy (Structural)
 *
 * Fetches remote images server-side and streams them back under the
 * trusted localhost origin, bypassing CORS and hotlink restrictions.
 *
 * GET /api/v1/images/proxy?url=https://example.com/fish.jpg
 */
@RestController
@RequestMapping("/api/v1/images")
public class ImageProxyController {

    private static final Set<String> ALLOWED_SCHEMES = Set.of("http", "https");
    private static final Set<String> IMAGE_TYPES = Set.of(
            "image/jpeg", "image/png", "image/webp",
            "image/gif", "image/svg+xml", "image/avif"
    );

    private final HttpClient httpClient = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(6))
            .build();

    @GetMapping("/proxy")
    public ResponseEntity<byte[]> proxy(@RequestParam String url) {

        URI uri;
        try {
            uri = URI.create(url);
            if (!ALLOWED_SCHEMES.contains(uri.getScheme())) {
                return ResponseEntity.badRequest().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .timeout(Duration.ofSeconds(8))
                    .header("User-Agent",
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                            "AppleWebKit/537.36 (KHTML, like Gecko) " +
                            "Chrome/124.0.0.0 Safari/537.36")
                    .header("Accept", "image/avif,image/webp,image/apng,image/*,*/*;q=0.8")
                    .header("Referer", uri.getScheme() + "://" + uri.getHost() + "/")
                    .GET()
                    .build();

            HttpResponse<byte[]> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
            }

            String contentType = response.headers()
                    .firstValue("Content-Type")
                    .orElse("image/jpeg")
                    .split(";")[0].trim().toLowerCase();

            if (!IMAGE_TYPES.contains(contentType)) {
                contentType = guessFromUrl(url);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.set(HttpHeaders.CACHE_CONTROL, "public, max-age=86400");
            headers.set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");

            return new ResponseEntity<>(response.body(), headers, HttpStatus.OK);

        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }

    private String guessFromUrl(String url) {
        String lower = url.toLowerCase();
        if (lower.contains(".png"))  return "image/png";
        if (lower.contains(".webp")) return "image/webp";
        if (lower.contains(".gif"))  return "image/gif";
        if (lower.contains(".svg"))  return "image/svg+xml";
        return "image/jpeg";
    }
}