/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;

import items.MuseumItem;
import jade.core.Agent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Igor
 */
public class InventoryAgent extends Agent {
    private static final String DELIMETERS = ",";
    private Map<String, MuseumItem> items;

    @Override
    protected void setup() {
        try {
            initItems();
        } catch (Exception ex) {
            Logger.getLogger(InventoryAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initItems() throws FileNotFoundException, IOException {
        items = new HashMap<String, MuseumItem>();
        File itemsFile = new File("data/items.csv");

        BufferedReader br = new BufferedReader(new FileReader(itemsFile));

        String line;
        while ((line = br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line, DELIMETERS);

            String id = st.nextToken();
            String title = st.nextToken();
            StringTokenizer st2 = new StringTokenizer(st.nextToken(), ";");
            String[] subject = new String[st2.countTokens()];
            for (int i = 0; i < st2.countTokens(); i++) {
                subject[i] = st2.nextToken();
            }
            String objectType1 = st.nextToken();
            String objectType2 = st.nextToken();
            String objectType3 = st.nextToken();
            String[] objectType = new String[]{objectType1, objectType2, objectType3};
            String material1 = st.nextToken();
            String material2 = st.nextToken();
            String material3 = st.nextToken();
            String material4 = st.nextToken();
            String material5 = st.nextToken();
            String[] material = new String[]{material1, material2, material3, material4, material5};

            items.put(id, new MuseumItem(id, title, subject, objectType, material));
        }

        br.close();
    }

    private MuseumItem getItem(String id) {
        return items.get(id);
    }
//    public static void main(String[] args) throws IOException {
//        InventoryAgent a = new InventoryAgent();
//        a.setup();
//
//        while (true) {
//            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//            String id = br.readLine();
//
//            System.out.println(a.getItem(id));
//        }
//    }
}
