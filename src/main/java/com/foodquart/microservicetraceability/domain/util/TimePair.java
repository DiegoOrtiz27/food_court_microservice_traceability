package com.foodquart.microservicetraceability.domain.util;

import java.time.LocalDateTime;

public record TimePair(LocalDateTime start, LocalDateTime end) {
}