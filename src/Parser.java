import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Parser {
    public static void main(String[] args) {
        int spell_level = 0;
        String spell_abbr = "0th";
        String color = "";
        FileReader reader = null;
        String curr = null;
        ArrayList<String> spells = new ArrayList<String>();
        ArrayList<String> rawJas = new ArrayList<String>();
        try {
            reader = new FileReader("spells.txt");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        BufferedReader buff = new BufferedReader(reader);
        try {
            curr = buff.readLine();
            while (curr != null) {
                spells.add(curr);
                curr = buff.readLine();
            }
        } catch (IOException a) {
        }
        spell_level = Integer.parseInt(spells.get(0));
        spell_abbr = spells.get(1);
        color = spells.get(2);
        spells.remove(0);
        spells.remove(0);
        spells.remove(0);
        Iterator<String> iter = spells.iterator();
        while (iter.hasNext()) {

            ArrayList<String> content = new ArrayList<String>();
            ArrayList<String> tags = new ArrayList<String>();

            tags.add("illusions");

            String[] parts = {"Range", "Area", "Effect", "Casting Time", "Duration", "Saving Throw", "Spell Resistance", "Components","Target"};
            ArrayList<String> allowable = new ArrayList<String>(Arrays.asList(parts));
            String url= iter.next();
            System.out.println("url is:"+url);
            Document doc = null;
            try {
                doc = Jsoup.connect(url).get();
            } catch (java.io.IOException a) {
                System.out.println("failure to connect");
            }
            Elements a = doc.getElementsByTag("p");
            Element data = doc.getElementById("content");

            Iterator<Element> it = a.iterator();

            //System.out.println("");
            //System.out.println(data.text());

            String name = doc.title();
            name = name.substring(0, name.indexOf('–'));
            System.out.println(name);
            content.add("subtitle | " + spell_abbr + " Level Spell");
            content.add("rule");


            String raw = data.text();
            raw = raw.substring(raw.indexOf(':', raw.indexOf(':') + 1) - 10);
            ArrayList<String> manip = new ArrayList<String>(Arrays.asList(raw.split(":")));

            while (manip.size() > 2 && manip.get(0).indexOf("Material Component") == -1) {
                String tmp = "property | ";
                tmp = tmp + manip.get(0) + " | ";
                manip.remove(0);

                int split = manip.get(0).lastIndexOf(' ');
                String firstHalf = manip.get(0).substring(0, split).trim();
                String secondHalf = manip.get(0).substring(split);
                if (allowable.contains(secondHalf.trim())) {
                    tmp = tmp + firstHalf;
                    manip.set(0, secondHalf);
                } else {
                    split = firstHalf.lastIndexOf(' ');
                    System.out.println(firstHalf);
                    secondHalf = firstHalf.substring(split) + secondHalf;
                    firstHalf = firstHalf.substring(0, split);
                    tmp = tmp + firstHalf;
                    manip.set(0, secondHalf);

                }

                if (manip.size() == 2) {
                    if(tmp.indexOf(' ', tmp.lastIndexOf('|') + 2)!=-1)
                    tmp = tmp.substring(0, tmp.indexOf(' ', tmp.lastIndexOf('|') + 2));

                }
                //System.out.println(tmp);

                content.add(tmp);
            }
            content.add("rule");
            content.add("fill | 2");

            it = a.iterator();
            while (it.hasNext()) {
                content.add("text |" + it.next().text());
            }


            Spell test = new Spell(content, name, tags, spell_level,color);
            Gson gson = new Gson();

            String json = gson.toJson(test);
            rawJas.add(json);
        }
        PrintWriter writer;
        try{
            writer = new PrintWriter("spells.json", "UTF-8");

            writer.write('[');
            iter = rawJas.iterator();
            while(iter.hasNext()){
                String toWrite = iter.next();
                System.out.println(toWrite);
                writer.write(toWrite);
                if(iter.hasNext())writer.write(',');
            }
            writer.write(']');
            writer.close();
        }
        catch(IOException ioex){

        }


    }
}
