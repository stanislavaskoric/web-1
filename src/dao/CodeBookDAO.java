package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import beans.CodeBook;

public class CodeBookDAO {

	private List<CodeBook> codebooks;
	private String path;
	
	public CodeBookDAO() {
		super();
		this.codebooks = new ArrayList<CodeBook>();
	}

	public CodeBookDAO(String path) {
		super();
		this.path = path;
		this.codebooks = new ArrayList<CodeBook>();
		this.loadCodeBooks();
	}

	public List<CodeBook> getCodebooks() {
		return codebooks;
	}

	public String getPath() {
		return path;
	}

	public void setCodebooks(List<CodeBook> codebooks) {
		this.codebooks = codebooks;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	
	
	public List<LocalDate> getHolidayDayes(){
		List<LocalDate> ret = new ArrayList<LocalDate>();
		for(CodeBook cb : codebooks) {
			ret.add(cb.getDate());
		}
		
		return ret;
	}
	
	
	
	public boolean addCodeBook(String d, String desc) {
		    boolean fleg = false;
			String []dateA = d.split("-");
			LocalDate ld = LocalDate.of(Integer.parseInt(dateA[0]),Integer.parseInt(dateA[1]),Integer.parseInt(dateA[2]));
			if(!isDateExist(ld)) {
				CodeBook cb = new CodeBook(ld,desc);
				codebooks.add(cb);
				saveCodeBook();
				fleg = true;
			}
		
		return fleg;
	}
	
	public boolean isDateExist(LocalDate date) {
		
		for(CodeBook cb: codebooks) {
			if(date.equals(cb.getDate())) {
				return true;
			}
		}
		return false;
	}
	
	
	
	public void saveCodeBook() {
		System.out.println("---save codebook");
		//System.out.println(path);
		BufferedWriter bwriter=null;
		File file = new File(path+ "/codebook.txt");
		String line="";
		try {
			for(CodeBook oneBook : codebooks) {
				line  += oneBook.getDate()+";"+oneBook.getDescription();
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
	
	
	public void loadCodeBooks() {
		BufferedReader in = null;
		System.out.println("---load codebooks");
		try {
			File file = new File(path + "/codebook.txt");
			in = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					String date = st.nextToken().trim();
					String name = st.nextToken().trim();
					String []dateA = date.split("-");
					LocalDate ld = LocalDate.of(Integer.parseInt(dateA[0]),Integer.parseInt(dateA[1]),Integer.parseInt(dateA[2]));
	                CodeBook cb = new CodeBook(ld, name);
					codebooks.add(cb);
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
