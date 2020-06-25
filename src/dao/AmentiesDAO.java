package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import java.util.Random;

import beans.Amenties;


public class AmentiesDAO {

	private List<Amenties> amenties;  //sifarnik sadrzaja apartmana
	private String path;
	
		
	public AmentiesDAO() {
		super();
		this.amenties = new ArrayList();
	}
	
	public AmentiesDAO(String path) {
		super();
		this.amenties = new ArrayList();
		this.path = path;
		this.loadAmenties();
	}

	public List<Amenties>  getAmenties() {
		return amenties;
	}
	
	public void setAmenties(List<Amenties> amenties) {
		this.amenties = amenties;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	
	public long generateID() {  
		return amenties.size()+1;
	}
	
	
	public boolean isActiveAndUnique(String name) {
		for(Amenties a: amenties) {
			if(a.getName().equals(name) && a.isActive()) {
				return false;
			}
		}
		return true;
	}
	
	
	public boolean addAmentie(String name) {
		if(isActiveAndUnique(name)) {
			Amenties newAmentie = new Amenties(generateID(),name);
			amenties.add(newAmentie);
			saveAmenties();
			return true;	
		}else {
			return false;
		}	
	}
	
	
	public int updateAmentie(String oldName, String newName) {
		for(Amenties a: amenties) {
			if(a.getName().equals(oldName) && a.isActive()) { //pronalazim stavku za izmenu
				if(isActiveAndUnique(newName)) {
					a.setName(newName);
					saveAmenties();
					return 1; //sve ok
				}else {
					return 3; //novo ime je zauzeto
				}
			}
		}
		return 2;  
	}
	
	
	
	public boolean deleteAmentie(String name) {
		for(Amenties a : amenties) {
			if(a.getName().equals(name) && a.isActive()) {
				a.setActive(false);
				saveAmenties();
				return true;
			}
		}
		return false;
	}
	
	
	public List<Amenties> findAllActiveAmenties(){
	   List<Amenties>returnList = new ArrayList<Amenties>();
	   for(Amenties a:amenties) {
		 if(a.isActive()) {
			 returnList.add(a);
		 }
	   }
       return returnList;				
	}
	
	
	public Amenties findById(Long id) {
		for(Amenties a: amenties) {
			if(id == a.getId()) {
				return a;
			}
		}
		return null;
	}
	
	public void saveAmenties() {
		System.out.println("---save amenties");
		//System.out.println(path);
		BufferedWriter bwriter=null;
		File file = new File(path+ "/amenties.txt");
		String line="";
		try {
			for(Amenties oneAmentie : amenties) {
				line  += oneAmentie.getId()+";"+oneAmentie.getName()+";"+oneAmentie.isActive();
				line += "\n";
			}
			bwriter = new BufferedWriter(new FileWriter(file));
			bwriter.write(line);
			bwriter.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(bwriter != null)
				try {
					bwriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}	
	 }
	
	
	
	
	public void loadAmenties() {
		BufferedReader in = null;
		System.out.println("---load amenties");
		try {
			File file = new File(path + "/amenties.txt");
			in = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					Long id = Long.parseLong(st.nextToken().trim());
					String name = st.nextToken().trim();
					boolean active = true;
					String active_temp = st.nextToken().trim();
	                if(active_temp.equals("false")) {
	                	active = false;
	                }
	               // System.out.println(name + ", "+id);
					Amenties oneAmentie = new Amenties(id, name, active);
					amenties.add(oneAmentie);
				}
			}
		}catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				}
				catch (Exception e) { }
			}
     	}		
	}
	

	
	
	
	
}
