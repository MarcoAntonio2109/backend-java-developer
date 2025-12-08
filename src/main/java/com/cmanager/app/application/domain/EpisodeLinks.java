package com.cmanager.app.application.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class EpisodeLinks {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "href", column = @Column(name = "self_href"))
    })
    private Self self;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "href", column = @Column(name = "show_href")),
            @AttributeOverride(name = "name", column = @Column(name = "show_name"))
    })
    private Show show;

    @Embeddable
    @Getter
    @Setter
    public static class Self {
        private String href;
    }

    @Embeddable
    @Getter
    @Setter
    public static class Show {
        private String href;
        private String name;
    }
}


