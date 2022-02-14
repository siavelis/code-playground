package org.prs.hashcode.onepizza.query;

public interface IngredientsSelector<T, R> {
    R selectIngredients(T data);
}
