package com.wak.game.application.facade;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wak.game.domain.color.Color;
import com.wak.game.domain.color.ColorService;
import com.wak.game.domain.color.ColorWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class InitFacade {

    private final ObjectMapper objectMapper;
    private final ColorService colorService;

    public void run() throws IOException {
        saveColors();
    }

    /**
     * Color 값 초기화 메서드
     * @throws IOException
     */
    private void saveColors() throws IOException {
        List<String> list = loadColorFile();
        for (String color : list) {
            colorService.save(color);
        }
    }

    /**
     * "color.json" read method
     * @return color hex value list
     * @throws IOException
     */
    private List<String> loadColorFile() throws IOException{
        ClassPathResource resource = new ClassPathResource("asset/color.json");
        ColorWrapper wrapper = objectMapper.readValue(resource.getInputStream(), ColorWrapper.class);
        return wrapper.colors();
    }

}
