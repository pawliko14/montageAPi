package object;

public class DataObject {

	private String Status;
	private String Machine;
	private String MontageDate;
	private String ShipmentDate;
	

	public DataObject(String status, String machine, String montageDate, String shipmentDate) {
		super();
		Status = status;
		Machine = machine;
		MontageDate = montageDate;
		ShipmentDate = shipmentDate;
	}
	public String getStatus() {
		return Status;
	}
	public String getMachine() {
		return Machine;
	}
	public String getMontageDate() {
		return MontageDate;
	}
	public String getShipmentDate() {
		return ShipmentDate;
	}
	
	
	@Override
	public String toString() {
		return "DataObject [Status=" + Status + ", Machine=" + Machine + ", MontageDate=" + MontageDate
				+ ", ShipmentDate=" + ShipmentDate + "]";
	}
	
	
	
}
