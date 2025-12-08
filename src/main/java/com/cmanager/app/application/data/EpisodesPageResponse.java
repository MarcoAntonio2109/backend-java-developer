package com.cmanager.app.application.data;

import com.cmanager.app.application.domain.Episodes;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EpisodesPageResponse {
    private List<Episodes> episodes;
    private double media;
    private int totalElements;
    private int totalPages;

}