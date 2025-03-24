package com.jnulocker.common.swagger;

import io.swagger.v3.oas.models.examples.Example;
import lombok.Builder;

@Builder
public record SwaggerExampleHolder(Example examplesItem, String name, int code) {}
