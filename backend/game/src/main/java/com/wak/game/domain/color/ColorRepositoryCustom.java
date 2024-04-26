package com.wak.game.domain.color;

import java.util.Optional;

public interface ColorRepositoryCustom {

    Color findByHexColor(String hexColor);
}
