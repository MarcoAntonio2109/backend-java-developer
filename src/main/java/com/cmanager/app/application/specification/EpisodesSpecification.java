package com.cmanager.app.application.specification;

import com.cmanager.app.application.domain.Episodes;
import org.springframework.data.jpa.domain.Specification;

public class EpisodesSpecification {

    public static Specification<Episodes> hasName(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Episodes> hasSeason(Integer season) {
        return (root, query, cb) ->
                season == null ? null : cb.equal(root.get("season"), season);
    }

    public static Specification<Episodes> hasType(String type) {
        return (root, query, cb) ->
                type == null ? null : cb.equal(root.get("type"), type);
    }

    public static Specification<Episodes> hasRuntimeGreaterThan(Integer runtime) {
        return (root, query, cb) ->
                runtime == null ? null : cb.greaterThan(root.get("runtime"), runtime);
    }
}
