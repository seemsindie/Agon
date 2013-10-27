package net.wachocki.agon.server.map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import sun.misc.BASE64Encoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * User: Marty
 * Date: 10/26/13
 * Time: 12:12 AM
 */
public class MapGenerator {

    public void writeTMX(String file, int width, int height) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element mapElement = doc.createElement("map");
            doc.appendChild(mapElement);

            mapElement.setAttribute("version","1.0");
            mapElement.setAttribute("orientation", "orthogonal");
            mapElement.setAttribute("width", String.valueOf(width));
            mapElement.setAttribute("height", String.valueOf(height));
            mapElement.setAttribute("tilewidth", "32");
            mapElement.setAttribute("tileheight", "32");

            Element tileset = doc.createElement("tileset");
            tileset.setAttribute("firstgid", "1");
            tileset.setAttribute("name", "tileset");
            tileset.setAttribute("tilewidth", "32");
            tileset.setAttribute("tileheight", "32");

            Element image = doc.createElement("image");
            image.setAttribute("source", "resources/tileset.png");
            image.setAttribute("width", "629");
            image.setAttribute("height", "679");

            tileset.appendChild(image);

            mapElement.appendChild(tileset);

            Element layer = doc.createElement("layer");
            layer.setAttribute("name", "layer1");
            layer.setAttribute("width", String.valueOf(width));
            layer.setAttribute("height", String.valueOf(height));

            Element data = doc.createElement("data");
            data.setAttribute("encoding", "base64");
            data.setAttribute("compression", "gzip");

            byte[] bytes = new byte[width * height * 4];
            int index = 0;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bytes[index] = 52;
                    bytes[index + 1] = 1;
                    bytes[index + 2] = 0;
                    bytes[index + 3] = 0;
                    index += 4;
                }
            }

            Text value = doc.createTextNode(compress(bytes));
            data.appendChild(value);

            layer.appendChild(data);

            mapElement.appendChild(layer);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(file));

            transformer.transform(source, result);

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }

    private String compress(byte[] byteAry) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            OutputStream deflater = new GZIPOutputStream(buffer);
            deflater.write(byteAry);
            deflater.close();
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return new BASE64Encoder().encode(buffer.toByteArray());
    }


}
