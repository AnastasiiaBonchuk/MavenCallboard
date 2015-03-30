package myMaven;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class View {

	static String userName;
	static AdvertList adv;
	static DataSource xml;
	static DataSource json;
	
	static String xmlFile = "dataCopy.xml";
	static String jsonFile= "data.json";

	public static void main(String arg[]) {
		adv = AdvertList.getInstance();
		View v = new View();
		xml = new XMLSource();
		//json = new JSONSource();
		xml.readFromFile(xmlFile);
		//json.readFromFile(jsonFile);
		v.Start();
		while (true) {
			while (!v.eventer(v.getMode()));
		}
	}

	public View() {
		setUserName();
	}
	
	public View(String forTest) {
		
	}

	@SuppressWarnings("resource")
	public void setUserName() {
		System.out
				.println(">Введите имя пользователя (от 4 до 20 символов латинницей, первый символ - буква): ");
		String input = new Scanner(System.in).nextLine();
		String regex = "[A-Za-z][A-Za-z0-9]{4,20}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		if (matcher.matches()) {
			userName = input;
		} else {
			System.out.println(">Вы ввели некорректное имя.");
			setUserName();
		}

	}

	public String getUserName() {
		return userName;
	}
	
	/**
	 * Show available modes for getting started
	 * */

	public void Start() {
		System.out
				.println(">Для смены пользователя введите               rename");
		System.out.println(">Для создания нового объявления введите       new");
		System.out
				.println(">Для просмотра объявлений ведите              view");
		System.out
				.println(">Для редактирования объявлений введите        edit");
		System.out
				.println(">Для удаления объявлений введите              delete");
		System.out
				.println(">Для просмотра текущего пользователя введите  user");
		System.out
				.println(">Для просмотра рубрик                         sections");
	}

	@SuppressWarnings("resource")
	public String getMode() {
		String mode = new Scanner(System.in).nextLine();
		return mode;
	}

	@SuppressWarnings("resource")
	public String getViewMode() {
		String viewMode = new Scanner(System.in).nextLine();
		return viewMode;
	}

	@SuppressWarnings("resource")
	public String getEditMode() {
		String editMode = new Scanner(System.in).nextLine();
		return editMode;
	}
	
	/**
	 * Carry out certain actions depending on chosen mode. 
	 * Displays the message about absence notexisting mode.
	 * @param mode name of mode
	 * @return true or false
	 * */

	public boolean eventer(String mode) {

		boolean res = true;

		switch (mode) {
		case "rename":
			setUserName();
			System.out.println(">Пользователь изменен");
			break;
		case "new":
			newMode();
			System.out.println(">Объявление добавлено");
			break;
		case "view":
			viewMode();
			break;
		case "edit":
			editMode();
			break;
		case "delete":
			deleteMode();
			break;
		case "user":
			System.out.println("Текущий пользователь - " + getUserName());
			break;
		case "sections":
			for (int i = 0; i < AdvertList.getSectionsCount(); i++) {
				System.out.println(adv.listOfSections[i].getSection());
			}
			break;
		case "save":
			xml.writeToFile(xmlFile);
			//json.writeToFile("data.json");
			System.out.println("Изменения сохранены");
			break;
		default:
			res = false;
			System.out.println(">Такого рeжима нет. Введите корректный режим");
			break;
		}
		return res;
	}

	public void newMode() {
		adv.addNewAdvertisement(enterSection(), enterHeader(), enterText());
	}
	
	/**
	 * Determines the existing of entered section for new advertisement in available sections list.
	 * If find returns its name, if not - displays the message about absence.
	 * @return name of section
	 * */

	@SuppressWarnings("resource")
	public String enterSection() {
		String section = "";
		while (section.equals("")) {
			System.out.println(">Введите рубрику");
			String tmpSection = new Scanner(System.in).nextLine();
			for (int i = 0; i < AdvertList.getSectionsCount(); i++) {
				if (tmpSection.equals(adv.listOfSections[i].getSection())) {
					section = tmpSection;
					break;
				} else {
					if (i == AdvertList.getSectionsCount() - 1) {
						System.out.println(">Такой рубрики нет");
					} else
						continue;
				}
			}
		}
		return section;
	}
	
	/**
	 * Validate the entered header.
	 * If header is correct returns it, if not - displays the message about discrepancy. 
	 * End when correct header will be entered.
	 * @return header
	 * */

	@SuppressWarnings("resource")
	public String enterHeader() {
		String header = "";
		while (header.equals("")) {
			System.out.println(">Введите заголовок (от 10 до 30 символов)");
			header = new Scanner(System.in).nextLine();
			if (header.length() < 10 || header.length() > 30) {
				System.out
						.println(">Вы ввели некорректный заголовок. Заголовок должен содержать от 10 до 30 символов");
				header = "";
				continue;
			}
		}
		return header;
	}

	/**
	 * Validate the entered text.
	 * If text is correct returns it, if not - displays the message about discrepancy. 
	 * End when correct text will be entered.
	 * @return text
	 * */
	@SuppressWarnings("resource")
	public String enterText() {
		String text = "";
		while (text.equals("")) {
			System.out.println(">Введите текст (от 20 до 400 символов)");
			text = new Scanner(System.in).nextLine();
			if (text.length() < 20 || text.length() > 400) {
				System.out
						.println(">Текст должен содержать от 20 до 400 символов");
				text = "";
				continue;
			}
		}
		return text;
	}

	public void viewMode() {
		boolean stay = true;
		while (stay) {
			System.out
					.println(">Для просмотра своих объявлений введите       my");
			System.out
					.println(">Для просмотра объявлений в рубрике введите   sections");
			System.out
					.println(">Для просмотра объявлений автора введите      author");
			switch (getViewMode()) {
			case "my":
				viewMyMode();
				stay = false;
				break;
			case "sections":
				viewSectionMode();
				stay = false;
				break;
			case "author":
				viewAuthorMode();
				stay = false;
				break;
			default:
				System.out
						.println(">Такого режима нет. Выберите корректный режим");
			}
		}
	}

	public void viewMyMode() {
		if (!showMyAdverts().equals("")) {
			System.out.println(">Мои объявления\n");
			System.out.println(showMyAdverts());
		} else {
			System.out.println(">У вас нет объявлений");
		}
	}

	@SuppressWarnings("resource")
	public void viewAuthorMode() {
		System.out.println(">Введите имя автора");
		String authorName = new Scanner(System.in).nextLine();
		System.out.println(showAdvertsOfAuthor(authorName));
	}

	@SuppressWarnings("resource")
	public void editMode() {
		boolean stay = true;
		String section;
		String header;
		String text;
		Integer id;
		System.out.println(">Введите номер объявления");
		id = new Scanner(System.in).nextInt();
		while (stay) {
			if (adv.findId(id) == true) {
				System.out.println(">Изменить рубрику             section");
				System.out.println(">Изменить заголовок           header");
				System.out.println(">Изменить текст объявления    text");
				switch (getEditMode()) {
				case "section":
					boolean exit = true;
					while (exit) {
						System.out.println(">Введите новую рубрику");
						section = new Scanner(System.in).nextLine();
						if (adv.findSection(section) == true) {
							adv.changeSectionFor(id, section);
							System.out.println(showAdvert(id));
							exit = false;

						} else {
							System.out
									.println(">Такой категории не существует");
						}
					}
					break;
				case "header":
					header = enterHeader();
					adv.changeHeaderFor(id, header);
					System.out.println(showAdvert(id));
					stay = false;
					break;

				case "text":
					text = enterText();
					adv.changeTextFor(id, text);
					System.out.println(showAdvert(id));
					stay = false;
					break;
				default:
					stay = false;
					System.out
							.println(">Такого рeжима нет. Введите корректный режим");
					break;
				}
			} else {
				System.out.println(">Не найдено объявления с таким номером");
				stay = false;
			}
		}
	}

	@SuppressWarnings("resource")
	public void viewSectionMode() {
		boolean exit = true;
		while (exit) {
			System.out.println(">Введите рубрику");
			String rubric = new Scanner(System.in).nextLine();
			if (adv.findSection(rubric) == true) {
				if (!showAdvertsInCategory(rubric).equals("")) {
					System.out.println(">Объявления из рубрики " + rubric
							+ "\n");
					System.out.println(showAdvertsInCategory(rubric));
					exit = false;
				} else {
					System.out.println(">Нет объявлений по данной рубрике");
					exit = false;
				}

			} else {
				System.out.println(">Такой рубрики не существует");
			}
		}
	}

	public void deleteMode() {
		Integer id;
		boolean exit = true;
		while (exit) {
			System.out.println(">Введите номер объявления");
			id = new Scanner(System.in).nextInt();
			if (adv.findMyId(id) == true) {
				adv.removeAdvertisement(id);
				System.out.println(">Объявление удалено");
				exit = false;
			} else {
				System.out.println(">У вас нет объявления с таким номером");
				exit = false;
			}
		}
	}
	
	public String showMyAdverts() {
		String output = "";
		for (int sectionIndex = 0; sectionIndex < AdvertList.getSectionsCount(); sectionIndex++) {
			for (int indexAdv = 0; indexAdv < adv.listOfSections[sectionIndex].listOfAds
					.size(); indexAdv++) {
				if (getUserName().equals(
						adv.listOfSections[sectionIndex].listOfAds.get(indexAdv)
								.getAuthorsName())) {
					Integer id = adv.listOfSections[sectionIndex].listOfAds.get(
							indexAdv).getId();
					long pubDate = adv.listOfSections[sectionIndex].listOfAds.get(
							indexAdv).getPublicationDate();
					String section = adv.listOfSections[sectionIndex].listOfAds.get(
							indexAdv).getSection();
					String header = adv.listOfSections[sectionIndex].listOfAds.get(
							indexAdv).getHeader();
					String text = adv.listOfSections[sectionIndex].listOfAds.get(
							indexAdv).getText();
					output = output + "Ид.номер" + id
							+ "\nДата публикации: " + adv.convertDate(pubDate)
							+ "\nРубрика: " + section + "\n" + header + "\n"
							+ text + "\n-------------\n";
				}
			}
		}
		return output;
	}
	
	public String showAdvertsOfAuthor(String authorName) {
		String output = "Объявления, опубликованные " + authorName + "\n";
		for (int indexCat = 0; indexCat < AdvertList.getSectionsCount(); indexCat++) {
			for (int indexAdv = 0; indexAdv < adv.listOfSections[indexCat].listOfAds
					.size(); indexAdv++) {
				if (authorName.equals(adv.listOfSections[indexCat].listOfAds.get(
						indexAdv).getAuthorsName())) {
					Integer id = adv.listOfSections[indexCat].listOfAds.get(
							indexAdv).getId();
					long pubDate = adv.listOfSections[indexCat].listOfAds.get(
							indexAdv).getPublicationDate();
					String section = adv.listOfSections[indexCat].listOfAds.get(
							indexAdv).getSection();
					String header = adv.listOfSections[indexCat].listOfAds.get(
							indexAdv).getHeader();
					String text = adv.listOfSections[indexCat].listOfAds.get(
							indexAdv).getText();
					output = output + "\nИд.номер " + id
							+ "\nДата публикации: " + adv.convertDate(pubDate)
							+ "\nРубрика: " + section + "\n" + header + "\n"
							+ text + "\n-------------\n";
				}
			}
		}
		return output;
	}
	
	public String showAdvert(Integer id) {
		String output = "";
		for (int indexCat = 0; indexCat < AdvertList.getSectionsCount(); indexCat++) {
			for (int indexAdv = 0; indexAdv < adv.listOfSections[indexCat].listOfAds
					.size(); indexAdv++) {
				if (id.equals(adv.listOfSections[indexCat].listOfAds
						.get(indexAdv).getId())) {
					id = adv.listOfSections[indexCat].listOfAds.get(indexAdv)
							.getId();

					long pubDate = adv.listOfSections[indexCat].listOfAds.get(
							indexAdv).getPublicationDate();
					String section = adv.listOfSections[indexCat].listOfAds.get(
							indexAdv).getSection();
					String header = adv.listOfSections[indexCat].listOfAds.get(
							indexAdv).getHeader();
					String text = adv.listOfSections[indexCat].listOfAds.get(
							indexAdv).getText();
					output = output + "\nИд.номер " + id
							+ "\nДата публикации: " + adv.convertDate(pubDate)
							+ "\nРубрика: " + section + "\n" + header + "\n"
							+ text + "\n-------------\n";
					return output;
				} else {
					continue;
				}
			}
		}
		return output;

	}
	
	public String showAdvertsInCategory(String category) {
		String output = "";
		for (int indexCat = 0; indexCat < AdvertList.getSectionsCount(); indexCat++) {
			if (category.equals(adv.listOfSections[indexCat].getSection())) {
				for (int indexAdv = 0; indexAdv < adv.listOfSections[indexCat].listOfAds
						.size(); indexAdv++) {
					Integer id = adv.listOfSections[indexCat].listOfAds.get(
							indexAdv).getId();
					String name = adv.listOfSections[indexCat].listOfAds.get(
							indexAdv).getAuthorsName();
					long pubDate = adv.listOfSections[indexCat].listOfAds.get(
							indexAdv).getPublicationDate();
					String header = adv.listOfSections[indexCat].listOfAds.get(
							indexAdv).getHeader();
					String text = adv.listOfSections[indexCat].listOfAds.get(
							indexAdv).getText();
					output = output + "Ид.номер " + id
							+ "\nАвтор: " + name + "\nДата публикации: "
							+ adv.convertDate(pubDate) + "\n" + header + "\n"
							+ text + "\n-------------\n";
				}
			}
		}
		return output;
	}

}
