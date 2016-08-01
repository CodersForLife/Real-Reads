package com.vizy.newsapp.realread.DataBase;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

public class DatabseColumns {
    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    public static final String _ID = "_id";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String TITLE = "newsTitle";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String DESCRIPTION = "newsDescription";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String URL = "newsImageUrl";
}
