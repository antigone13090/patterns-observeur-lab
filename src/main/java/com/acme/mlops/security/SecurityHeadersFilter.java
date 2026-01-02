package com.acme.mlops.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityHeadersFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    response.setHeader("X-Frame-Options", "DENY");
    response.setHeader("X-Content-Type-Options", "nosniff");
    response.setHeader("Permissions-Policy", "geolocation=(), microphone=(), camera=()");

    response.setHeader("Cross-Origin-Opener-Policy", "same-origin");
    response.setHeader("Cross-Origin-Embedder-Policy", "require-corp");
    response.setHeader("Cross-Origin-Resource-Policy", "same-origin");

    response.setHeader("Content-Security-Policy",
        "default-src 'self'; " +
        "script-src 'self'; " +
        "style-src 'self'; " +
        "img-src 'self' data:; " +
        "font-src 'self'; " +
        "connect-src 'self'; " +
        "object-src 'none'; " +
        "base-uri 'self'; " +
        "frame-ancestors 'none'; " +
        "form-action 'self'");

    String uri = request.getRequestURI();
    if (uri != null && uri.startsWith("/api/")) {
      response.setHeader("Cache-Control", "no-store, max-age=0");
      response.setHeader("Pragma", "no-cache");
      response.setHeader("Expires", "0");
    } else if (uri != null && (uri.endsWith(".js") || uri.endsWith(".css"))) {
      response.setHeader("Cache-Control", "public, max-age=3600");
    } else {
      response.setHeader("Cache-Control", "no-store, max-age=0");
      response.setHeader("Pragma", "no-cache");
      response.setHeader("Expires", "0");
    }

    chain.doFilter(request, response);
  }

  // IMPORTANT: appliquer aussi sur les réponses d'erreur (404/405/etc)
  @Override
  protected boolean shouldNotFilterErrorDispatch() {
    return false;
  }
}
