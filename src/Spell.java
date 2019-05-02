import java.util.ArrayList;
import java.util.List;

public class Spell {
    private int count= 1;
    private String color = "royalblue";
    private String title = "";
    private String icon = "white-book-";
    private String icon_back = "robe";
    private List<String> contents;
    private List<String> tags;

    public Spell(ArrayList<String> data, String name,ArrayList<String> tag,int spellLevel, String col){
        icon = icon +spellLevel;
        contents = data;
        title = name;
        tags = tag;
        color = col;
    }
}
