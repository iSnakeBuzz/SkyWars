package com.isnakebuzz.skywars.Utils.Strings;

import java.security.AlgorithmParameterGenerator;
import java.util.Arrays;
import java.util.Optional;

public enum Alphabet {

    A(1, "A"),
    B(2, "B"),
    C(3, "C"),
    D(4, "D"),
    E(5, "E"),
    F(6, "F"),
    G(7, "G"),
    H(8, "H"),
    I(9, "I"),
    J(10, "J"),
    K(11, "K"),
    L(12, "L"),
    M(13, "M"),
    N(14, "N"),
    O(15, "O"),
    P(16, "P"),
    Q(17, "Q"),
    R(18, "R"),
    S(19, "S"),
    T(20, "T"),
    U(21, "U"),
    V(22, "V"),
    W(23, "W"),
    X(24, "X"),
    Y(25, "Y"),
    Z(26, "Z");

    private int id;
    private String name;

    Alphabet(int i, String name) {
        this.id = i;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Alphabet getById(Integer id) {
        for (Alphabet e : values()) {
            if (e.id == id) return e;
        }
        return A;
    }

}
