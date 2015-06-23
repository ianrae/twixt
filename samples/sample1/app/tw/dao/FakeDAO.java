package tw.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Flight;


public class FakeDAO {

	private static List<Flight> data = new ArrayList<>();

	static int count;

	public Flight findFlightById(long id) 
	{
		for(Flight flight : data)
		{
			if (flight.getId() == id)
			{
				return flight;
			}
		}
		return null;
	}

	public void insertData(Long id, Flight target) 
	{
		int index = 0;
		for(Flight flight : data)
		{
			if (flight.getId() == id)
			{
				//data.set(index, target);
				//copy since id not passed
				flight.setS(target.getS());
				return;
			}
			index++;
		}
	}
	public void insertDataDirect(Long id, Flight target) 
	{
		int index = 0;
		for(Flight flight : data)
		{
			if (flight.getId() == id)
			{
				data.set(index, target);
				return;
			}
			index++;
		}
	}

	public int size()
	{
		return data.size();
	}
	public List<Flight> data()
	{
		return data;
	}

	public void initFlights() 
	{
		Flight flight = new Flight();
		flight.setId(10L);
		flight.setS("abc");
		flight.setLang("German");
		flight.setUserId(100L);
		flight.setIsAdmin(true);
		flight.setStartDate(new Date());
		flight.setAccountTypeId(1L);
		flight.setEmails(buildEmails());
		data.add(flight);

		flight = new Flight();
		flight.setId(11L);
		flight.setS("def");
		flight.setLang("French");
		flight.setUserId(101L);
		flight.setStartDate(new Date());
		flight.setAccountTypeId(1L);
		flight.setEmails(buildEmails());
		data.add(flight);
	}

	private List<String> buildEmails() 
	{
		List<String> L = new ArrayList<>();
		L.add("userA@example.com");
		L.add("userB@example.com");
		return L;
	}

}
