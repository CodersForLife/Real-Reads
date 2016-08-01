package com.vizy.newsapp.realread.DataBase;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

@Database(version = DatabseDefinition.VERSION)
public class DatabseDefinition {
    private DatabseDefinition() {
    }
    public static final int VERSION = 1;
    @Table(DatabseColumns.class) public static final String QUOTES = "quotes";
}
