package com.cdel.format.bean;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class InputBean {
    String content;
    String chapter;
    boolean paragraphIndent;
    boolean replaceTagU;
}
