package com.example.demoelastic.restclient;

import java.util.Collections;
import java.util.List;
import lombok.Getter;

@Getter
public class Page<T> {

  public static final Page EMPTY = new Page(Collections.emptyList(), null, 0, 0);

  private final List<T> products;
  private final String input;
  private final int from;
  private final int size;

  public Page(List<T> products, String input, int from, int size) {
    this.products = products;
    this.input = input;
    this.from = from;
    this.size = size;
  }

  List<T> get() {
    return Collections.unmodifiableList(products);
  }
}