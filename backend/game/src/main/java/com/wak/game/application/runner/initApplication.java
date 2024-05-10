package com.wak.game.application.runner;

import com.wak.game.application.facade.InitFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class initApplication implements ApplicationRunner {
    private final InitFacade initFacade;
    public void run(ApplicationArguments args)throws IOException{
        initFacade.run();
    }
}
