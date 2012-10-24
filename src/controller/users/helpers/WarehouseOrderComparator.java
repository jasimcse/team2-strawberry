package controller.users.helpers;

import java.util.Comparator;

import com.google.appengine.api.datastore.KeyFactory;

import model.WarehouseOrder;

public class WarehouseOrderComparator implements Comparator<WarehouseOrder>{

	@Override
	public int compare(WarehouseOrder o1, WarehouseOrder o2) {
		return KeyFactory.keyToString(o1.getID()).compareTo(KeyFactory.keyToString(o2.getID()));
	}

}
