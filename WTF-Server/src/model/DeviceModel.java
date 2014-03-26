package model;

import model.bean.Device;
import model.dao.DeviceDAO;

public class DeviceModel extends Model<DeviceDAO> {

	public DeviceModel() {
		setDAO(new DeviceDAO());
	}
	
	public boolean createDevice(String macAddress, int idPlayer) {
		Device device = new Device(macAddress, idPlayer);

		getDAO().setBean(device);
		
		return getDAO().save();
	}
	
}
