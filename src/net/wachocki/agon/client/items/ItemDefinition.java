package net.wachocki.agon.client.items;

import net.wachocki.agon.common.types.ItemQuality;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * User: Marty
 * Date: 10/27/13
 * Time: 3:45 PM
 */
public class ItemDefinition {

    private static ItemDefinition[] itemDefinitions;

    private int id;
    private String name;
    private ItemQuality itemQuality;
    private Image groundImage;
    private Image inventoryImage;

    public ItemDefinition(int id, String name, ItemQuality itemQuality) {
        this.id = id;
        this.name = name;
        this.itemQuality = itemQuality;
        try {
            this.groundImage = new Image("resources/items/" + id + ".png");
            this.inventoryImage = groundImage;
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemQuality getItemQuality() {
        return itemQuality;
    }

    public void setItemQuality(ItemQuality itemQuality) {
        this.itemQuality = itemQuality;
    }

    public Image getInventoryImage() {
        return inventoryImage;
    }

    public void setInventoryImage(Image inventoryImage) {
        this.inventoryImage = inventoryImage;
    }

    public Image getGroundImage() {
        return groundImage;
    }

    public void setGroundImage(Image groundImage) {
        this.groundImage = groundImage;
    }

    public static void load(String file) {
        try {
            File fXmlFile = new File(file);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList itemList = doc.getElementsByTagName("item");
            itemDefinitions = new ItemDefinition[itemList.getLength()];
            for (int i = 0; i < itemList.getLength(); i++) {
                Element item = (Element) itemList.item(i);
                int id = Integer.parseInt(item.getAttribute("id"));
                String name = item.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();
                ItemQuality itemQuality = ItemQuality.valueOf(item.getElementsByTagName("quality").item(0).getChildNodes().item(0).getNodeValue());
                if (itemDefinitions[id] == null) {
                    itemDefinitions[id] = new ItemDefinition(id, name, itemQuality);
                } else {
                    System.out.println("Duplicate item definitions for id: " + id);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public static ItemDefinition forId(int id) {
        return itemDefinitions[id];
    }

}
