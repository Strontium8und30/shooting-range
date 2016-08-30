package common;

import java.util.*;


public enum DataType {
	ID(0),
	NAME(1),
	MESSAGE(2),
	POSITION(3),
	RECEIPT(4),
	CONNECTION_CLOSE(5),
	CLIENT_ADD(6),
	CLIENT_REMOVE(7),
	PLAYER_SHOOT(8),
	WEAPON_CHANGE(9),
	SEND_DATA(10);
	
	private int typeID;
	
	private DataType(int typeID) {
		this.typeID = typeID;
	}
	
	public int getTypeID() {
		return typeID;
	}
	
	public static DataType getTypeByID(int id) {
		for (DataType type : values()) {
			if (type.getTypeID() == id) {
				return type;
			}
		}
		throw new NoSuchElementException("Kein entsprechender Datentyp wurde gefunden. " + id);
	}
}
