package myMaven;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class AdvertList {

	private String section;
	private String userName;
	private static int sectionsCount;
	private int maxSize = 0;
	private static volatile AdvertList instance;
	List<Advertisement> listOfAds = new ArrayList<>();
	AdvertList listOfSections[];

	public List<Advertisement> getListOfAds() {
		return listOfAds;
	}

	public void setListOfAds(List<Advertisement> listOfAds) {
		this.listOfAds = listOfAds;
	}

	public static int getSectionsCount() {
		return sectionsCount;
	}

	public static void setSectionsCount(int sectionsCount) {
		AdvertList.sectionsCount = sectionsCount;
	}

	public AdvertList[] getListOfSections() {
		return listOfSections;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public AdvertList() {
		readSectionsFromFile();
	}

	public AdvertList(String category) {
		this.section = category;
	}

	public static AdvertList getInstance() {

		if (instance == null) {
			synchronized (AdvertList.class) {
				if (instance == null) {
					instance = new AdvertList();
				}
			}
		}
		return instance;
	}

	public String getSection() {
		return section;
	}
	
	/**
	 * Write all sections that are saved in file to listOfSections. Create separate list for every section.
	 * The access to these lists will carry out using the name of these sections.
	 * */

	public void readSectionsFromFile() {
		try {
			FileReader fr = new FileReader("rubrics.txt");
			BufferedReader br = new BufferedReader(fr);
			String s = br.readLine();
			try {
				StringTokenizer st = new StringTokenizer(s);
				ArrayList<String> l = new ArrayList<>();

				while (st.hasMoreTokens()) {
					l.add(st.nextToken());
					sectionsCount++;
				}

				listOfSections = new AdvertList[sectionsCount];

				for (int i = 0; i < sectionsCount; i++) {
					String str = l.get(i);
					listOfSections[i] = new AdvertList(str);
				}

			} finally {
				br.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getUserName() {
		userName = View.userName;
		return userName;
	}
	
	public void setUserName(String name) {
		View.userName = name;
	}
	
	/**
	 * Found the section's list and add new advertisement to this list if it was founded. 
	 * @param category advertisement section
	 * @param header advertisement header
	 * @param text advertisement text
	 * */

	public void addNewAdvertisement(String category, String header, String text) {
		for (int i = 0; i < sectionsCount; i++) {
			if (category.equals(listOfSections[i].getSection())) {
				Advertisement ad = new Advertisement(getUserName(),
						calculateDate(), category, header, text, ++maxSize);
				listOfSections[i].listOfAds.add(ad);
				break;
			}
		}
	}

	/**
	 * Remove advertisement from list using its id and change id for other advertisements. 
	 * If the advertisement was removed return true, else - false
	 * @param id advertisement id
	 * @return removed or not
	 * */

	public boolean removeAdvertisement(Integer id) {
		boolean isRemoved = false;
		for (int indexCat = 0; indexCat < sectionsCount; indexCat++) {
			for (int indexAdv = 0; indexAdv < listOfSections[indexCat].listOfAds
					.size(); indexAdv++) {
				if (id.equals(listOfSections[indexCat].listOfAds.get(indexAdv)
						.getId())) {
					listOfSections[indexCat].listOfAds.remove(indexAdv);
					isRemoved = true;
					maxSize = listOfSections[indexCat].listOfAds.size();

					for (int indexAdvReID = id - 1; indexAdvReID < maxSize; indexAdvReID++) {
						listOfSections[indexCat].listOfAds.get(indexAdvReID)
								.setId(listOfSections[indexCat].listOfAds.get(
										indexAdvReID).getId() - 1);
					}
					return isRemoved;
				}
			}
		}
		return isRemoved;
	}

	/**
	 * Found advertisement using its id and set new sectionr for advertisement
	 * with such id.
	 * 
	 * @param id
	 *            advertisement id
	 * @param newSection
	 *            new section
	 * */

	public void changeSectionFor(Integer id, String newSection) {
		for (int indexCat = 0; indexCat < sectionsCount; indexCat++) {
			for (int indexAdv = 0; indexAdv < listOfSections[indexCat].listOfAds
					.size(); indexAdv++) {
				if (id.equals(listOfSections[indexCat].listOfAds.get(indexAdv)
						.getId()) && findSection(newSection) == true) {
					listOfSections[indexCat].listOfAds.get(indexAdv)
							.setSection(newSection);
					break;
				}
			}
		}
	}

	/**
	 * Search the advertisement with such id at adverts list. If found return
	 * true, else - false.
	 * 
	 * @param id advertisement
	 *            id
	 * @return found or not
	 * */
	public boolean findId(Integer id) {
		boolean res = false;
		for (int indexCat = 0; indexCat < sectionsCount; indexCat++) {
			for (int indexAdv = 0; indexAdv < listOfSections[indexCat].listOfAds
					.size(); indexAdv++) {
				if (id.equals(listOfSections[indexCat].listOfAds.get(indexAdv)
						.getId())) {
					res = true;
				}
			}
		}
		return res;
	}

	/**
	 * Search the advertisement of current user using its id.
	 * 
	 * @param id advertisement
	 *            id
	 * @return found or not
	 * */
	public boolean findMyId(Integer id) {
		boolean res = false;
		for (int indexCat = 0; indexCat < sectionsCount; indexCat++) {
			for (int indexAdv = 0; indexAdv < listOfSections[indexCat].listOfAds
					.size(); indexAdv++) {
				if (id.equals(listOfSections[indexCat].listOfAds.get(indexAdv)
						.getId())) {
					if (listOfSections[indexCat].listOfAds.get(indexAdv)
							.getAuthorsName().equals(getUserName())) {
						res = true;
						break;
					}
					break;
				}
			}
		}
		return res;
	}

	/**
	 * Search section in section's file. If found it return true, else false.
	 * 
	 * @param section
	 *            name of section
	 * @return found or not
	 * */

	public boolean findSection(String section) {
		boolean res = false;
		try {
			FileReader fr = new FileReader("rubrics.txt");
			BufferedReader br = new BufferedReader(fr);
			String s = br.readLine();
			try {
				StringTokenizer st = new StringTokenizer(s);

				while (st.hasMoreTokens()) {
					if (st.nextToken().equals(section)) {
						res = true;
						break;
					}
				}
			} finally {
				br.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * Found advertisement using its id and set new header for advertisement
	 * with such id.
	 * 
	 * @param id
	 *            advertisement id
	 * @param newHeader
	 *            new header
	 * */
	public void changeHeaderFor(Integer id, String newHeader) {
		for (int indexCat = 0; indexCat < sectionsCount; indexCat++) {
			for (int indexAdv = 0; indexAdv < listOfSections[indexCat].listOfAds
					.size(); indexAdv++) {
				if (id.equals(listOfSections[indexCat].listOfAds.get(indexAdv)
						.getId())) {
					listOfSections[indexCat].listOfAds.get(indexAdv).setHeader(
							newHeader);
					break;
				}
			}
		}
	}

	/**
	 * Found advertisement using its id and set new text for advertisement with
	 * such id.
	 * 
	 * @param id
	 *            advertisement id
	 * @param newText
	 *            new text
	 * */
	public void changeTextFor(Integer id, String newText) {
		for (int indexCat = 0; indexCat < sectionsCount; indexCat++) {
			for (int indexAdv = 0; indexAdv < listOfSections[indexCat].listOfAds
					.size(); indexAdv++) {
				if (id.equals(listOfSections[indexCat].listOfAds.get(indexAdv)
						.getId())) {
					listOfSections[indexCat].listOfAds.get(indexAdv).setText(
							newText);
					break;

				}
			}
		}

	}

	/**
	 * Convert unix-time to readable format.
	 * @param unixEpoch date saved like long type
	 * @return date in format dd-MM-yyyy at kk:mm
	 * */
	public String convertDate(long unixEpoch) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy â kk:mm");
		String date = sdf.format(unixEpoch);
		return date;
	}

	/**
	 * Calculate present date and time for new advertisement.
	 * 
	 * @return date and time
	 * */
	public long calculateDate() {
		Date d = new Date();
		return d.getTime();
	}

}
