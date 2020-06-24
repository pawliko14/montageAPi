package com.example.montageApi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import Connection.DBConnection;
import object.DataObject;

@RestController
public class GreetingController {

	private static final String template = "Hello, %s";
	private static final AtomicLong counter = new AtomicLong();
	private static Connection myConn;
	
	private List<DataObject> dataObjects = new ArrayList<DataObject>();

	
	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value ="name", defaultValue = "World") String name)
	{
		return new Greeting(counter.getAndIncrement(), String.format(template, name));
	}
	
	@GetMapping("/test")
	public String sayHello() throws SQLException, ParseException {
		
		gatherData();
		
		
	    HashMap<Integer, DataObject> map = new HashMap<>();
	    
	    for(int i = 0 ; i < dataObjects.size();i++)
	    {
	    	map.put(i, dataObjects.get(i));
	    }
	  
	    Gson gson = new Gson(); 
	    String jsonString = gson.toJson(dataObjects);
	    
	    int jsonLength = jsonString.length();
	    System.out.println("lenght:" + jsonLength);
	
	  /*       //part of code to create a header that suists for CORS requierments, not needed when CORS is turned on directly onto web browser
	    		// CAREFUL - it was tasted on Firefox, and deployed into chromium 
	    ResponseBuilder resp;
	    
	    resp = Response.ok(jsonString);
	    resp.header("Access-Control-Allow-Origin", "*")
      .header("Access-Control-Allow-Methods", "GET, POST");
	    
        return resp.build();
	    */
	    
	    dataObjects.clear(); 
	    
	 return jsonString;
	}
	
	
	private void gatherData() throws SQLException, ParseException
	{
		myConn = DBConnection.dbConnector();
		
		String sql = "select \"delayed\" as Status ,Concat(NrMaszyny, '-', Opis) as Machine,\r\n" + 
				"DataKoniecMontazu as MontageDate, DataKontrakt as ShipmentDate from calendar\r\n" + 
				"where Zakonczone <> '1'\r\n" + 
				"and length(NrMaszyny) = 8\r\n" + 
				"and DataProdukcji between \r\n" + 
				"(SELECT date_add(date_add(LAST_DAY(current_date),interval 1 DAY),interval -1 MONTH) AS first_day) \r\n" + 
				"and \r\n" + 
				"(SELECT date_add(LAST_DAY(current_date),interval 1 DAY) AS first_day)";
		
		Statement takeDate = myConn.createStatement();
		ResultSet r = takeDate.executeQuery(sql);
		
		while(r.next()) {
			
			
			String status = r.getString("Status");
			String machine = r.getString("Machine");		
			String montageDate = r.getString("MontageDate");		
			String shipmentdate = r.getString("ShipmentDate");		

			status = statusAnalize(shipmentdate);
			
			DataObject obj  = new DataObject(status, machine, montageDate, shipmentdate);
			
			dataObjects.add(obj);
		}	
		
		
	}
	
	
	
	public String statusAnalize(String shipmentdate) throws ParseException {
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		
		if(shipmentdate.length() >8 )
		{
			String dateinfuture  =shipmentdate;
			Date dateinfuture_date = formatter.parse(dateinfuture);
			
			
			Date date = new Date();
	
			if(date.after(dateinfuture_date))
			{
				return "ON TIME";
			}
			else if(dateinfuture_date.after(date))
			{
				return "DELAYED";
			}
			else
			{
				return "ON TIME";
			}
		}
		else
		{
			return "ON TIME";
		}

	}
	
	
}
