package com.wak.game.global.util;

import io.swagger.v3.oas.models.examples.Example;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExampleHolder {
	private Example example;
	private Integer code;
	private String name;
}
