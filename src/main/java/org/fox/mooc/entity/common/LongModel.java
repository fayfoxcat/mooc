package org.fox.mooc.entity.common;

import java.io.Serializable;

public class LongModel implements Identifier<Long> ,Serializable{

    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }
}
