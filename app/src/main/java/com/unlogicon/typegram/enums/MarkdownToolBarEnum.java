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

    BOLD (0, "Bold",
            R.drawable.vector_edtoolbar_bold),

    ITALIC (1,"Italic",
          R.drawable.vector_edtoolbar_italic),

    CODE (2,"Code",
            R.drawable.vector_edtoolbar_code),

    ATTACH (3,"Attach image",
            R.drawable.vector_edtoolbar_attach),

    LINK (4,"Link",
            R.drawable.vector_edtoolbar_link),

    MORE (5,"More",
            R.drawable.vector_edtoolbar_more),

    STRIKETHROUGHT (6,"strikethrough",
            R.drawable.vector_edtoolbar_strikethrough),


    QUOTE (12,"Quote",
            R.drawable.vector_edtoolbar_quote),

    H1 (7,"H1",
            R.drawable.vector_edtoolbar_h1),

    H2 (8,"H2",
            R.drawable.vector_edtoolbar_h2),

    H3 (9,"H3",
            R.drawable.vector_edtoolbar_h3),

    LIST (10,"List",
            R.drawable.vector_edtoolbar_list),

    NUMBER_LIST (11,"Number list",
            R.drawable.vector_edtoolbar_number_list);



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
