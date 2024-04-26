package com.wak.game.domain.color;

import com.wak.game.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class ColorService {

    private final ColorRepository colorRepository;

    public Color findById(long id){
        return colorRepository.findById(id).orElseThrow(); // ErrorCode
    }

    public Color save(String hexColor) {
        if (!validCheckDuplicate(hexColor)) // ErrorCode
            return null;

        return colorRepository.save(Color.builder()
                .hexColor(hexColor)
                .build());
    }

    private boolean validCheckDuplicate(String hexColor){
        if (colorRepository.findByHexColor(hexColor) == null)
            return true;
        else return false;
    }
}
