package com.impactzb.productpricecalculator.data.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Builder
@Data
public class ProblemDetails {
    private HttpStatus status;
    private String title;
    private List<String> details;

    public ProblemDetails(HttpStatus status, String title, List<String> details) {
        super();
        this.status = status;
        this.title = title;
        this.details = details;
    }
}
