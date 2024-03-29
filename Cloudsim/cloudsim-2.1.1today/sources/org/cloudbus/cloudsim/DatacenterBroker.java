package org.cloudbus.cloudsim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.lists.CloudletList;
import org.cloudbus.cloudsim.lists.VmList;

/**
 * DatacentreBroker represents a broker
 * acting on behalf of a user. It hides VM management,
 * as vm creation, sumbission of cloudlets to this VMs
 * and destruction of VMs.
 *
 * @author              Rodrigo N. Calheiros
 * @author              Anton Beloglazov
 * @since               CloudSim Toolkit 1.0
 */
public class DatacenterBroker extends SimEntity {

        // TODO: remove unnecessary variables
	public static int totalDelay1 = 50;
	public static final int PERIODIC_EVENT1 = 67567;
        /** The vm list. */
        private List<? extends Vm> vmList;

        /** The vms created list. */
        private List<? extends Vm> vmsCreatedList;

        /** The cloudlet list. */
        private List<? extends Cloudlet> cloudletList;

        /** The cloudlet submitted list. */
        private List<? extends Cloudlet> cloudletSubmittedList;

        /** The cloudlet received list. */
        private List<? extends Cloudlet> cloudletReceivedList;

        /** The cloudlets submitted. */
        private int cloudletsSubmitted;

        /** The vms requested. */
        private int vmsRequested;

        /** The vms acks. */
        private int vmsAcks;

        /** The vms destroyed. */
        private int vmsDestroyed;

        /** The datacenter ids list. */
        private List<Integer> datacenterIdsList;

        /** The datacenter requested ids list. */
        private List<Integer> datacenterRequestedIdsList;

        /** The vms to datacenters map. */
        private Map<Integer, Integer> vmsToDatacentersMap;

        /** The datacenter characteristics list. */
        private Map<Integer, DatacenterCharacteristics> datacenterCharacteristicsList;


        /**
         * Created a new DatacenterBroker object.
         *
         * @param name  name to be associated with this entity (as
         * required by Sim_entity class from simjava package)
         *
         * @throws Exception the exception
         *
         * @pre name != null
         * @post $none
         */
        public DatacenterBroker(String name) throws Exception {
                super(name);

                setVmList(new ArrayList<Vm>());
                setVmsCreatedList(new ArrayList<Vm>());
                setCloudletList(new ArrayList<Cloudlet>());
                setCloudletSubmittedList(new ArrayList<Cloudlet>());
                setCloudletReceivedList(new ArrayList<Cloudlet>());

                cloudletsSubmitted=0;
                setVmsRequested(0);
                setVmsAcks(0);
                setVmsDestroyed(0);

                setDatacenterIdsList(new LinkedList<Integer>());
                setDatacenterRequestedIdsList(new ArrayList<Integer>());
                setVmsToDatacentersMap(new HashMap<Integer, Integer>());
                setDatacenterCharacteristicsList(new HashMap<Integer, DatacenterCharacteristics>());
        }

        /**
         * This method is used to send to the broker the list with
         * virtual machines that must be created.
         *
         * @param list the list
         *
         * @pre list !=null
         * @post $none
         */
        public void submitVmList(List<? extends Vm> list) {
                getVmList().addAll(list);
        }

        /**
         * This method is used to send to the broker the list of
         * cloudlets.
         *
         * @param list the list
         *
         * @pre list !=null
         * @post $none
         */
        public void submitCloudletList(List<? extends Cloudlet> list){
                getCloudletList().addAll(list);
        }

        /**
         * Specifies that a given cloudlet must run in a specific virtual machine.
         *
         * @param cloudletId ID of the cloudlet being bount to a vm
         * @param vmId the vm id
         *
         * @pre cloudletId > 0
         * @pre id > 0
         * @post $none
         */
        public void bindCloudletToVm(int cloudletId, int vmId){
                CloudletList.getById(getCloudletList(), cloudletId).setVmId(vmId);
        }

    /**
     * Processes events available for this Broker.
     *
     * @param ev    a SimEvent object
     *
     * @pre ev != null
     * @post $none
     */
        @Override
        public void processEvent(SimEvent ev) {
                //Log.printLine(CloudSim.clock()+"[Broker]: event received:"+ev.getTag());
                switch (ev.getTag()){
                	case PERIODIC_EVENT1:
                		this.processPeriodicEvent1(ev);
                		break;
                        // Resource characteristics request
                    case CloudSimTags.RESOURCE_CHARACTERISTICS_REQUEST:
                        processResourceCharacteristicsRequest(ev);
                        break;
                        // Resource characteristics answer
                    case CloudSimTags.RESOURCE_CHARACTERISTICS:
                        processResourceCharacteristics(ev);
                        break;
                // VM Creation answer
                    case CloudSimTags.VM_CREATE_ACK:
                        processVmCreate(ev);
                        break;
                //A finished cloudlet returned
                    case CloudSimTags.CLOUDLET_RETURN:
                        processCloudletReturn(ev);
                        break;
                // if the simulation finishes
                    case CloudSimTags.END_OF_SIMULATION:
                        shutdownEntity();
                        break;
            // other unknown tags are processed by this method
                    default:
                    	//processOtherEvent(ev);
                    	//break;
                }
        }
        int cloudlets;
        public static int cloudletID1;
		 private void processPeriodicEvent1(SimEvent ev) {
			 totalDelay1 += 50;     // for stopping condition
	// working for cloudlet 		 
			cloudlets = 450;
			LinkedList<Cloudlet> list = new LinkedList<Cloudlet>();
			//if(totalDelay1 > 200)
				//cloudlets = 20 ;
		/*	if(totalDelay1 > 300)
				cloudlets = 30;
			if(totalDelay1 > 400)
				cloudlets += 30; 
			if(totalDelay1 > 500)
				cloudlets += 10;*/
			long length;
			long fileSize = 300;
			long outputSize = 300;
			int pesNumber = 1;
			UtilizationModel utilizationModel = new UtilizationModelFull();
			Cloudlet[] cloudlet = new Cloudlet[cloudlets];
			//int c=0;
			for(int i = 0;i < cloudlets;cloudletID1++, i++)
			{
				
				length = 100000;/*-(c*100);
				c= (c+1)%10;*/
				cloudlet[i] = new Cloudlet(cloudletID1, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
				cloudlet[i].setUserId(this.getId());
				list.add(cloudlet[i]);
			}
			this.setCloudletList(list);
			this.submitCloudlets();
			 boolean generatePeriodicEvent;
		    if(totalDelay1 <= 500)
		    	generatePeriodicEvent=true;
		   else
		    	generatePeriodicEvent=false;
			if (generatePeriodicEvent) 
				send(getId(),50,PERIODIC_EVENT1,ev);        // for scheduling for next event	    
		 }

        /**
         * Process the return of a request for the characteristics of a PowerDatacenter.
         *
         * @param ev a SimEvent object
         *
         * @pre ev != $null
         * @post $none
         */
        protected void processResourceCharacteristics(SimEvent ev) {
                DatacenterCharacteristics characteristics = (DatacenterCharacteristics) ev.getData();
                getDatacenterCharacteristicsList().put(characteristics.getId(), characteristics);

                if (getDatacenterCharacteristicsList().size() == getDatacenterIdsList().size()) {
                        setDatacenterRequestedIdsList(new ArrayList<Integer>());
                        createVmsInDatacenter(getDatacenterIdsList().get(0));
                }
        }

        /**
         * Process a request for the characteristics of a PowerDatacenter.
         *
         * @param ev a SimEvent object
         *
         * @pre ev != $null
         * @post $none
         */
        protected void processResourceCharacteristicsRequest(SimEvent ev) {
                setDatacenterIdsList(CloudSim.getCloudResourceList());
                setDatacenterCharacteristicsList(new HashMap<Integer, DatacenterCharacteristics>());

                Log.printLine(CloudSim.clock()+": "+getName()+ ": Cloud Resource List received with "+getDatacenterIdsList().size()+" resource(s)");

                for (Integer datacenterId : getDatacenterIdsList()) {
                        sendNow(datacenterId, CloudSimTags.RESOURCE_CHARACTERISTICS, getId());
                }
        }

        /**
         * Process the ack received due to a request for VM creation.
         *
         * @param ev a SimEvent object
         *
         * @pre ev != null
         * @post $none
         */
        protected void processVmCreate(SimEvent ev) {
                int[] data = (int[]) ev.getData();
                int datacenterId = data[0];
                int vmId = data[1];
                int result = data[2];

                if (result == CloudSimTags.TRUE) {
                        getVmsToDatacentersMap().put(vmId, datacenterId);
                        getVmsCreatedList().add(VmList.getById(getVmList(), vmId));
                        Log.printLine(CloudSim.clock()+": "+getName()+ ": VM #"+vmId+" has been created in Datacenter #" + datacenterId + ", Host #" + VmList.getById(getVmsCreatedList(), vmId).getHost().getId());
                } else {
                        Log.printLine(CloudSim.clock()+": "+getName()+ ": Creation of VM #"+vmId+" failed in Datacenter #" + datacenterId);
                }

                incrementVmsAcks();

                if (getVmsCreatedList().size() == getVmList().size() - getVmsDestroyed()) { // all the requested VMs have been created
                        submitCloudlets();
                } else {
                        if (getVmsRequested() == getVmsAcks()) { // all the acks received, but some VMs were not created
                                // find id of the next datacenter that has not been tried
                                for (int nextDatacenterId : getDatacenterIdsList()) {
                                        if (!getDatacenterRequestedIdsList().contains(nextDatacenterId)) {
                                                createVmsInDatacenter(nextDatacenterId);
                                                return;
                                        }
                                }

                                //all datacenters already queried
                                if (getVmsCreatedList().size() > 0) { //if some vm were created
                                        submitCloudlets();
                                } else { //no vms created. abort
                                        Log.printLine(CloudSim.clock() + ": " + getName() + ": none of the required VMs could be created. Aborting");
                                        finishExecution();
                                }
                        }
                }
        }

        /**
         * Process a cloudlet return event.
         *
         * @param ev a SimEvent object
         *
         * @pre ev != $null
         * @post $none
         */
        protected void processCloudletReturn(SimEvent ev) {
                Cloudlet cloudlet = (Cloudlet) ev.getData();
                getCloudletReceivedList().add(cloudlet);
                Log.printLine(CloudSim.clock()+": "+getName()+ ": Cloudlet "+cloudlet.getCloudletId()+" received");
                cloudletsSubmitted--;
                if (getCloudletList().size()==0&&cloudletsSubmitted==0) { //all cloudlets executed
                        Log.printLine(CloudSim.clock()+": "+getName()+ ": All Cloudlets executed. Finishing...");
                        clearDatacenters();
                        finishExecution();
                } else { //some cloudlets haven't finished yet
                        if (getCloudletList().size()>0 && cloudletsSubmitted==0) {
                                //all the cloudlets sent finished. It means that some bount
                                //cloudlet is waiting its VM be created
                                clearDatacenters();
                                createVmsInDatacenter(0);
                        }

                }
        }

        /**
         * Overrides this method when making a new and different type of Broker.
         * This method is called by {@link #body()} for incoming unknown tags.
         *
         * @param ev   a SimEvent object
         *
         * @pre ev != null
         * @post $none
         */
    protected void processOtherEvent(SimEvent ev){
        if (ev == null){
            Log.printLine(getName() + ".processOtherEvent(): " + "Error - an event is null.");
            return;
        }

        Log.printLine(getName() + ".processOtherEvent(): " + "Error - event unknown by this DatacenterBroker.");
    }

    /**
     * Create the virtual machines in a datacenter.
     *
     * @param datacenterId Id of the chosen PowerDatacenter
     *
     * @pre $none
     * @post $none
     */
    protected void createVmsInDatacenter(int datacenterId) {
                // send as much vms as possible for this datacenter before trying the next one
                int requestedVms = 0;
                String datacenterName = CloudSim.getEntityName(datacenterId);
                for (Vm vm : getVmList()) {
                        if (!getVmsToDatacentersMap().containsKey(vm.getId())) {
                                Log.printLine(CloudSim.clock() + ": " + getName() + ": Trying to Create VM #" + vm.getId() + " in " + datacenterName);
                                sendNow(datacenterId, CloudSimTags.VM_CREATE_ACK, vm);
                                requestedVms++;
                        }
                }

                getDatacenterRequestedIdsList().add(datacenterId);

                setVmsRequested(requestedVms);
                setVmsAcks(0);
        }

    /**
     * Submit cloudlets to the created VMs.
     *
     * @pre $none
     * @post $none
     */public static int vmIndex = 0;
        protected void submitCloudlets() {
        	double k = 0;
    		int temp;
    		int numberVMscounter;
    		int vmid;
    		Vm vm =null;
    		vmIndex = vmIndex % getVmList().size();										//unique index for each VM
    		int negServiceTime = 3000;													//Ts
    		int id =0;
                
                for (Cloudlet cloudlet : getCloudletList()) {
                	id = cloudlet.getCloudletId();
                	for(numberVMscounter = 0;numberVMscounter < getVmsCreatedList().size();numberVMscounter++)
            			{// Allocating particular vm to a cloudlet
            				
            				vm = this.getVmList().get(vmIndex);
            			//	System.out.println(" VMIndex is " + vmIndex +"VM id in loop is and size is " + vm.getId() + " " + this.getVmsCreatedList().size());
            				if(vm.getDeleteStatus() == false)
            				{					
            					k = negServiceTime / (cloudlet.getCloudletLength()/vm.getMips());					//re
            					
            					vmid = vm.getId();
            					temp = vm.getvmQueue();
            					if(temp < 30)
            					{
            						//System.out.println("DCID " this.getVmsToDatacentersMap().get);
            						cloudlet.setVmId(vmid);	
            						getCloudletSubmittedList().add(cloudlet);
            						this.cloudletsSubmitted++;
            						sendNow(2, CloudSimTags.CLOUDLET_SUBMIT, cloudlet);
            						vm.setvmQueue(temp+1);
            						if (vm == null)
            						{ // vm was not created
            							Log.printLine(CloudSim.clock()+": "+getName()+ ": Postponing execution of cloudlet "+cloudlet.getCloudletId()+": bount VM not available");
            						}
            						break;
            					}
            					else
            					{
            						vmIndex = (vmIndex + 1) % getVmList().size();
            					}
            				}
            			}                // remove submitted cloudlets from waiting list
                }
                	List<Cloudlet> CList = new ArrayList<Cloudlet>();
            		CList.addAll(getCloudletList());
            		// remove submitted cloudlets from waiting list
            		for (Cloudlet cloudlet1 : CList)
            		{	if(cloudlet1.getCloudletId() == id )
            			break;
            			getCloudletList().remove(cloudlet1);			
            		}
        }

        /**
         * Destroy the virtual machines running in datacenters.
         *
         * @pre $none
         * @post $none
         */
        protected void clearDatacenters() {
                for (Vm vm : getVmsCreatedList()) {
                        Log.printLine(CloudSim.clock() + ": " + getName() + ": Destroying VM #" + vm.getId());
                        sendNow(getVmsToDatacentersMap().get(vm.getId()), CloudSimTags.VM_DESTROY, vm);
                }

                getVmsCreatedList().clear();
        }

        /**
         * Send an internal event communicating the end of the simulation.
         *
         * @pre $none
         * @post $none
         */
        private void finishExecution() {
                sendNow(getId(), CloudSimTags.END_OF_SIMULATION);
        }

        /* (non-Javadoc)
         * @see cloudsim.core.SimEntity#shutdownEntity()
         */
        @Override
        public void shutdownEntity() {
        Log.printLine(getName() + " is shutting down...");
        }

        /* (non-Javadoc)
         * @see cloudsim.core.SimEntity#startEntity()
         */
        @Override
        public void startEntity() {
                Log.printLine(getName() + " is starting...");
                schedule(getId(), 0, CloudSimTags.RESOURCE_CHARACTERISTICS_REQUEST);
                schedule(getId(), 50, PERIODIC_EVENT1);
        }

        /**
         * Gets the vm list.
         *
         * @param <T> the generic type
         * @return the vm list
         */
        @SuppressWarnings("unchecked")
        public <T extends Vm> List<T> getVmList() {
                return (List<T>) vmList;
        }

        /**
         * Sets the vm list.
         *
         * @param <T> the generic type
         * @param vmList the new vm list
         */
        protected <T extends Vm> void setVmList(List<T> vmList) {
                this.vmList = vmList;
        }


        /**
         * Gets the cloudlet list.
         *
         * @param <T> the generic type
         * @return the cloudlet list
         */
        @SuppressWarnings("unchecked")
        public <T extends Cloudlet> List<T> getCloudletList() {
                return (List<T>) cloudletList;
        }


        /**
         * Sets the cloudlet list.
         *
         * @param <T> the generic type
         * @param cloudletList the new cloudlet list
         */
        protected <T extends Cloudlet> void setCloudletList(List<T> cloudletList) {
                this.cloudletList = cloudletList;
        }

        /**
         * Gets the cloudlet submitted list.
         *
         * @param <T> the generic type
         * @return the cloudlet submitted list
         */
        @SuppressWarnings("unchecked")
        public <T extends Cloudlet> List<T> getCloudletSubmittedList() {
                return (List<T>) cloudletSubmittedList;
        }


        /**
         * Sets the cloudlet submitted list.
         *
         * @param <T> the generic type
         * @param cloudletSubmittedList the new cloudlet submitted list
         */
        protected <T extends Cloudlet> void setCloudletSubmittedList(List<T> cloudletSubmittedList) {
                this.cloudletSubmittedList = cloudletSubmittedList;
        }

        /**
         * Gets the cloudlet received list.
         *
         * @param <T> the generic type
         * @return the cloudlet received list
         */
        @SuppressWarnings("unchecked")
        public <T extends Cloudlet> List<T> getCloudletReceivedList() {
                return (List<T>) cloudletReceivedList;
        }

        /**
         * Sets the cloudlet received list.
         *
         * @param <T> the generic type
         * @param cloudletReceivedList the new cloudlet received list
         */
        protected <T extends Cloudlet> void setCloudletReceivedList(List<T> cloudletReceivedList) {
                this.cloudletReceivedList = cloudletReceivedList;
        }

        /**
         * Gets the vm list.
         *
         * @param <T> the generic type
         * @return the vm list
         */
        @SuppressWarnings("unchecked")
        public <T extends Vm> List<T> getVmsCreatedList() {
                return (List<T>) vmsCreatedList;
        }

        /**
         * Sets the vm list.
         *
         * @param <T> the generic type
         * @param vmsCreatedList the vms created list
         */
        protected <T extends Vm> void setVmsCreatedList(List<T> vmsCreatedList) {
                this.vmsCreatedList = vmsCreatedList;
        }

        /**
         * Gets the vms requested.
         *
         * @return the vms requested
         */
        protected int getVmsRequested() {
                return vmsRequested;
        }

        /**
         * Sets the vms requested.
         *
         * @param vmsRequested the new vms requested
         */
        protected void setVmsRequested(int vmsRequested) {
                this.vmsRequested = vmsRequested;
        }

        /**
         * Gets the vms acks.
         *
         * @return the vms acks
         */
        protected int getVmsAcks() {
                return vmsAcks;
        }

        /**
         * Sets the vms acks.
         *
         * @param vmsAcks the new vms acks
         */
        protected void setVmsAcks(int vmsAcks) {
                this.vmsAcks = vmsAcks;
        }

        /**
         * Increment vms acks.
         */
        protected void incrementVmsAcks() {
                this.vmsAcks++;
        }

        /**
         * Gets the vms destroyed.
         *
         * @return the vms destroyed
         */
        protected int getVmsDestroyed() {
                return vmsDestroyed;
        }

        /**
         * Sets the vms destroyed.
         *
         * @param vmsDestroyed the new vms destroyed
         */
        protected void setVmsDestroyed(int vmsDestroyed) {
                this.vmsDestroyed = vmsDestroyed;
        }

        /**
         * Gets the datacenter ids list.
         *
         * @return the datacenter ids list
         */
        protected List<Integer> getDatacenterIdsList() {
                return datacenterIdsList;
        }

        /**
         * Sets the datacenter ids list.
         *
         * @param datacenterIdsList the new datacenter ids list
         */
        protected void setDatacenterIdsList(List<Integer> datacenterIdsList) {
                this.datacenterIdsList = datacenterIdsList;
        }

        /**
         * Gets the vms to datacenters map.
         *
         * @return the vms to datacenters map
         */
        protected Map<Integer, Integer> getVmsToDatacentersMap() {
                return vmsToDatacentersMap;
        }

        /**
         * Sets the vms to datacenters map.
         *
         * @param vmsToDatacentersMap the vms to datacenters map
         */
        protected void setVmsToDatacentersMap(Map<Integer, Integer> vmsToDatacentersMap) {
                this.vmsToDatacentersMap = vmsToDatacentersMap;
        }

        /**
         * Gets the datacenter characteristics list.
         *
         * @return the datacenter characteristics list
         */
        protected Map<Integer, DatacenterCharacteristics> getDatacenterCharacteristicsList() {
                return datacenterCharacteristicsList;
        }

        /**
         * Sets the datacenter characteristics list.
         *
         * @param datacenterCharacteristicsList the datacenter characteristics list
         */
        protected void setDatacenterCharacteristicsList(Map<Integer, DatacenterCharacteristics> datacenterCharacteristicsList) {
                this.datacenterCharacteristicsList = datacenterCharacteristicsList;
        }

        /**
         * Gets the datacenter requested ids list.
         *
         * @return the datacenter requested ids list
         */
        protected List<Integer> getDatacenterRequestedIdsList() {
                return datacenterRequestedIdsList;
        }

        /**
         * Sets the datacenter requested ids list.
         *
         * @param datacenterRequestedIdsList the new datacenter requested ids list
         */
        protected void setDatacenterRequestedIdsList(List<Integer> datacenterRequestedIdsList) {
                this.datacenterRequestedIdsList = datacenterRequestedIdsList;
        }
}
