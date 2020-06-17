package dao;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import beans.Apartment;
import beans.Comment;
import beans.Guest;

public class CommentDAO {

	private HashMap<Long,Comment> comments;
	private String path;
	
	public CommentDAO(String path,HashMap<String,Guest>guests , HashMap<Long,Apartment>apartments) {
		this.comments = new HashMap<Long, Comment>();
		this.path = path;
		this.loadComments(guests, apartments);
	}
	
	public void addComment(Comment comment, Guest g, Apartment a) {
		System.out.println("--add comment--");
		//proveriti da li nad ovim apartmanom gost moze da ostavi komenatar
		comment.setId(comments.size()+1);      //postaviti na slucajnu vrednost
		comment.setGuest(g);
        comment.setApartment(a);	
        comments.put(comment.getId(), comment);
        saveComments();
	}
	
	
	@SuppressWarnings({ "unchecked", "resource" })
	public void saveComments() {
		System.out.println("--save comment--");
		JSONArray listComment = new JSONArray();
		for(Long id_comment : comments.keySet()) {
			Comment c = comments.get(id_comment);
			JSONObject json_c = new JSONObject();
			json_c.put("id", c.getId());
			json_c.put("guest",c.getGuest().getUsername());
			json_c.put("apartment", c.getApartment().getId());
			json_c.put("text", c.getText());
			json_c.put("evaluation", c.getEvaluation());
			listComment.add(json_c);
		} try{
			System.out.println(path+"/comments.json");		
			FileWriter fw = new FileWriter(path+"/comments.json"); 
			fw.write(listComment.toJSONString());
			fw.flush();
		}catch(Exception e) {
			e.printStackTrace();
		} 
	}
	
	@SuppressWarnings("unchecked")
	public void loadComments(HashMap<String,Guest>guests , HashMap<Long,Apartment>apartments) {
		System.out.println("--load comments--");
		JSONParser parser = new JSONParser(); 
		try {
			Object obj = parser.parse(new FileReader(path+"/comments.json"));
			JSONArray jsonArray = (JSONArray) obj;
			System.out.println(jsonArray);
			
			Iterator<JSONObject> iterator = jsonArray.iterator();
			while (iterator.hasNext()) {
				JSONObject jsonObject = iterator.next();
				Comment c = new Comment();
		
				c.setId((Long)jsonObject.get("id"));
				c.setGuest(guests.get((String)jsonObject.get("guest")));
				c.setApartment(apartments.get((Long)jsonObject.get("apartment")));
				c.setText((String)jsonObject.get("text"));
				c.setEvaluation(((Long)jsonObject.get("evaluation")).intValue());
				
				comments.put(c.getId(), c);
				
			}
			
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
