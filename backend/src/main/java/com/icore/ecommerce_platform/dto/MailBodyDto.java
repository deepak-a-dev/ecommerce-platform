package com.icore.ecommerce_platform.dto;

import lombok.Builder;

@Builder
public record MailBodyDto(String to, String subject, String text) {
}

