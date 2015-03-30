package myMaven;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class JSONSource extends DataSource{

	AdvertList adv = AdvertList.getInstance();

	@SuppressWarnings("unchecked")
	public void readFromFile(String fileName) {
		ArrayList<JSONObject> temp = new ArrayList<>();
		ArrayList<Advertisement> temp2 = new ArrayList<>();
		JSONParser parser = new JSONParser();

		try {

			Object obj = parser.parse(new FileReader(fileName));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray companyList = (JSONArray) jsonObject.get("Объявления");

			Iterator<JSONObject> iterator = companyList.iterator();
			while (iterator.hasNext()) {
				temp.add(iterator.next());
			}

			for (int i = 0; i < companyList.size(); i++) {
				Advertisement ad = new Advertisement(temp.get(i)
						.get(" Имя_автора").toString(), Long.parseLong(temp
						.get(i).get("Дата_публикации").toString()), temp.get(i)
						.get("Рубрика").toString(), temp.get(i)
						.get("Заголовок").toString(), temp.get(i).get("Текст")
						.toString(), Integer.parseInt(temp.get(i)
						.get("Ид.номер").toString()));
				temp2.add(ad);

				for (int j = 0; j < AdvertList.getSectionsCount(); j++) {
					if (temp.get(i).get("Рубрика")
							.equals(adv.listOfSections[j].getSection())) {
						adv.setMaxSize(adv.getMaxSize()+1);
						adv.listOfSections[j].listOfAds.add(temp2.get(i));
						break;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void writeToFile(String fileName) {

		JSONObject obj = new JSONObject();
		JSONArray array = new JSONArray();

		for (int j = 0; j < AdvertList.getSectionsCount(); j++) {

			for (Advertisement ad : adv.listOfSections[j].listOfAds) {

				JSONObject obja = new JSONObject();
				obja.put(" Имя_автора", ad.getAuthorsName());
				obja.put("Дата_публикации", ad.getPublicationDate());
				obja.put("Рубрика", ad.getSection());
				obja.put("Заголовок", ad.getHeader());
				obja.put("Текст", ad.getText());
				obja.put("Ид.номер", ad.getId());

				array.add(obja);
			}
		}
		obj.put("Объявления", array);

		FileWriter file = null;
		try {
			file = new FileWriter(fileName);
			file.write(obj.toJSONString());

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			try {
				file.flush();
				file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}

}

