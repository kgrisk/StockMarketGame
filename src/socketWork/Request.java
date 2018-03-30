package socketWork;

import java.util.Arrays;

public class Request {
	//type of input and other info that it has
	public RequestType type;
	public String[] params;

	public Request(RequestType type, String... params) {
		this.type = type;
		this.params = params;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(); 
		sb.append(type); 
		for (int i = 0; i < params.length; i++)
		sb.append(" " + params[i]); 
		return sb.toString(); 
	}
	/*
	splits the given string and analyzes that kind of info should be send to request constructor
 	*/
	public static Request parse(String line) {
		try {
			String[] items = line.trim().split("\\s+");
			switch (items[0].toUpperCase()) {
			case "BUY":
				return new Request(RequestType.BUY, items[1], items[2]);
			case "SELL":
				return new Request(RequestType.SELL, items[1], items[2]);
			case "QUIT":
				return new Request(RequestType.QUIT);
			case "VOTE":
				return new Request(RequestType.VOTE, items[1], items[2]);
			case "FINISHED":
				return new Request(RequestType.FINISHED);

			default:
			}
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		return new Request(RequestType.INVALID, line);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Request other = (Request) obj;
		if (!Arrays.equals(params, other.params))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
}
