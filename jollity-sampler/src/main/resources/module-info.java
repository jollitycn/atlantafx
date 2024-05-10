/* SPDX-License-Identifier: MIT */

module com.jasonhong.fx {

    requires atlantafx.base;
    requires java.desktop;
    requires java.prefs;
    requires javafx.swing;
    requires javafx.media;
    requires javafx.web;
    requires javafx.fxml;
    requires jdk.zipfs;

    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.feather;
    requires org.kordamp.ikonli.material2;
    requires org.jetbrains.annotations;

    requires fr.brouillard.oss.cssfx;
    requires datafaker;
    requires org.fxmisc.richtext;
//    requires com.jasonhong.ocr;
    exports com.jasonhong.fx.main;
    exports com.jasonhong.fx.main.fake.domain;
    exports com.jasonhong.fx.main.event;
    exports com.jasonhong.fx.main.layout;
    exports com.jasonhong.fx.main.page;
    exports com.jasonhong.fx.main.fxml.ocr;
    exports com.jasonhong.fx.main.page.general;
    exports com.jasonhong.fx.main.page.components;
    exports com.jasonhong.fx.main.page.showcase;
    exports com.jasonhong.fx.main.theme;
    exports com.jasonhong.fx.main.util;

    opens com.jasonhong.fx.main.fake.domain;

    // resources
    opens com.jasonhong.fx.main;
    opens com.jasonhong.fx.main.assets.highlightjs;
    opens com.jasonhong.fx.main.assets.styles;
    opens com.jasonhong.fx.main.images;
    opens com.jasonhong.fx.main.images.modena;
    opens com.jasonhong.fx.main.media;
    opens com.jasonhong.fx.main.page.general;
    opens com.jasonhong.fx.main.page.showcase;
    opens com.jasonhong.fx.main.layout;
    opens com.jasonhong.fx.main.util;
    opens com.jasonhong.fx.main.fxml.ocr to   javafx.fxml;
}
