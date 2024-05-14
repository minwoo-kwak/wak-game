package com.wak.game.application.response.socket;


import com.wak.game.domain.rank.dto.RankInfo;

import java.util.List;

public record RankListResponse (List<RankInfo> ranks){
    public RankListResponse(List<RankInfo> ranks) {
        this.ranks = ranks;
    }
}