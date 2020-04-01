package org.fox.mooc.entity.common;

import java.io.Serializable;

/**
 * @param <KEY>
 */
public interface Identifier<KEY extends Serializable> {

    public KEY getId();

}
