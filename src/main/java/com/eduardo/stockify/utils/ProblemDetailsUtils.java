package com.eduardo.stockify.utils;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.time.Instant;

@NoArgsConstructor
public final class ProblemDetailsUtils {

    public static ProblemDetail build(HttpStatus status, String title, String detail,
                                      String code, String instancePath) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setType(URI.create("https://stockify/errors/" + code.toLowerCase()));
        problemDetail.setProperty("code", code);
        problemDetail.setProperty("timestamp", Instant.now().toString());
        if (instancePath != null) {
            problemDetail.setInstance(URI.create(instancePath));
        }

        return problemDetail;
    }

}
