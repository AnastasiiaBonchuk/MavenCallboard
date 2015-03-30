package myMaven;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLSource extends DataSource{

	AdvertList adv = AdvertList.getInstance();

	public void readFromFile(String fileName) {
		ArrayList<Advertisement> temp = new ArrayList<>();
		try {
			Document doc;
			DocumentBuilder db = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			doc = db.parse(new File(fileName));
			NodeList groupList = doc.getElementsByTagName("Advertisement");
			for (int i = 0; i < groupList.getLength(); i++) {
				Node person = groupList.item(i);
				Element elem = (Element) person;
				NodeList champ = elem.getChildNodes();
				Node n = champ.item(0);
				Advertisement ad = new Advertisement(
						n.getAttributes().getNamedItem("Имя_автора")
								.getNodeValue(),
						Long.parseLong(n.getAttributes()
								.getNamedItem("Дата_публикации").getNodeValue()),
						n.getAttributes().getNamedItem("Рубрика")
								.getNodeValue(), n.getAttributes()
								.getNamedItem("Заголовок").getNodeValue(), n
								.getAttributes().getNamedItem("Текст")
								.getNodeValue(), Integer.parseInt(n
								.getAttributes().getNamedItem("Ид.номер")
								.getNodeValue()));
				temp.add(ad);

				for (int j = 0; j < AdvertList.getSectionsCount(); j++) {
					if (n.getAttributes().getNamedItem("Рубрика")
							.getNodeValue()
							.equals(adv.listOfSections[j].getSection())) {
						adv.setMaxSize(adv.getMaxSize()+1);
						adv.listOfSections[j].listOfAds.add(temp.get(i));
						break;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void writeToFile(String fileName) {
		try {
			
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();

			Element champions = document.createElement("Adverts");
			document.appendChild(champions);
			for (int j = 0; j < AdvertList.getSectionsCount(); j++) {

				for (Advertisement ad : adv.listOfSections[j].listOfAds) {

					Element advertisement = document
							.createElement("Advertisement");
					champions.appendChild(advertisement);

					Element data = document.createElement("AdvertisementData");
					advertisement.appendChild(data);

					data.setAttribute("Имя_автора", ad.getAuthorsName());
					data.setAttribute("Дата_публикации",
							ad.getPublicationDate() + "");
					data.setAttribute("Рубрика", ad.getSection());
					data.setAttribute("Заголовок", ad.getHeader());
					data.setAttribute("Текст", ad.getText());
					data.setAttribute("Ид.номер", ad.getId() + "");

				}
			}

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			File file = new File(fileName);
			file.delete();
			StreamResult result = new StreamResult(new File(fileName));
			transformer.transform(source, result);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
