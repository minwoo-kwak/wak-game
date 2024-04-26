package com.wak.game.domain.color;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ColorWrapper(@JsonProperty List<String> colors) {
    public ColorWrapper(List<String> colors) { this.colors = colors; }
}
