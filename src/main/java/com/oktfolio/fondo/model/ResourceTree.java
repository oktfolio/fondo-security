package com.oktfolio.fondo.model;

import java.util.List;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/11/08
 */
public class ResourceTree {

    private Integer id;
    private String title;
    private boolean disabled;
    List<ResourceChild> children;
}

class ResourceChild{
    private Integer id;
    private String title;
    private boolean isLeaf;
    private boolean disableCheckbox;
    List<ResourceChild> children;
}
