/* SPDX-License-Identifier: MIT */

package com.jasonhong.fx.main.theme;

import javafx.css.PseudoClass;
import javafx.scene.paint.Color;

public record AccentColor(Color primaryColor, PseudoClass pseudoClass,Type type ) {
public enum Type {
    noset,
    primerPurple,
    primerPink,
    primerCoral
}
    public static AccentColor primerPurple() {
        return new AccentColor(Color.web("#8250df"), PseudoClass.getPseudoClass("accent-primer-purple"),Type.primerPurple);
    }

    public static AccentColor primerPink() {
        return new AccentColor(Color.web("#bf3989"), PseudoClass.getPseudoClass("accent-primer-pink"),Type.primerPink);
    }

    public static AccentColor primerCoral() {
        return new AccentColor(Color.web("#c4432b"), PseudoClass.getPseudoClass("accent-primer-coral"),Type.primerCoral);
    }

    public static AccentColor getAccentColorByType(Type type) {
        AccentColor ac = null;
        switch (type) {
            case type.primerCoral -> {
                ac = primerCoral();
            }
            case type.primerPink -> {
                ac = primerPink();
            }
            case primerPurple -> {
                ac = primerPurple();
            }
        }
        return ac;
    }
}
