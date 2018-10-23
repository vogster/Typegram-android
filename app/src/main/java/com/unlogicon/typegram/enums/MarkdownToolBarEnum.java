package com.unlogicon.typegram.enums;

import android.support.annotation.DrawableRes;

import com.unlogicon.typegram.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Nikita Korovkin 22.10.2018.
 */
public enum MarkdownToolBarEnum {

    ATTACH (0, "Attach image",
            R.drawable.vector_attach);


    private int id;
    private String description;
    @DrawableRes
    private int drawableId;

    MarkdownToolBarEnum(int id, String description, int drawableId) {
        this.id = id;
        this.description = description;
        this.drawableId = drawableId;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public static List<MarkdownToolBarEnum> asList(){
        return new ArrayList<>(Arrays.asList(MarkdownToolBarEnum.values()));
    }
}
