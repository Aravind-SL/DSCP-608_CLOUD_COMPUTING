package com.cloud;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class CloudAllocation {

    public static void main(String[] args) {
        Log.printLine("Starting CloudSimVMAllocationExample...");
        try {
            int numUsers = 1;
            Calendar calendar = Calendar.getInstance();
            boolean traceFlag = false;
            CloudSim.init(numUsers, calendar, traceFlag);

            Datacenter datacenter = createDatacenter("Datacenter_0");
            DatacenterBroker broker = createBroker();
            int brokerId = broker.getId();

            List<Vm> vmList = new ArrayList<>();
            Vm vm1 = new Vm(0, brokerId, 1000, 1, 512, 1000, 10000, "Xen", new CloudletSchedulerTimeShared());
            Vm vm2 = new Vm(1, brokerId, 1500, 1, 768, 1500, 15000, "Xen", new CloudletSchedulerTimeShared());
            vmList.add(vm1);
            vmList.add(vm2);
            broker.submitVmList(vmList);

            List<Cloudlet> cloudletList = new ArrayList<>();
            Cloudlet cloudlet1 = new Cloudlet(0, 400000, 1, 300, 300, new UtilizationModelFull(), new UtilizationModelFull(), new UtilizationModelFull());
            Cloudlet cloudlet2 = new Cloudlet(1, 200000, 1, 300, 300, new UtilizationModelFull(), new UtilizationModelFull(), new UtilizationModelFull());
            cloudletList.add(cloudlet1);
            cloudletList.add(cloudlet2);
            broker.submitCloudletList(cloudletList);

            CloudSim.startSimulation();
            CloudSim.stopSimulation();

            List<Cloudlet> newList = broker.getCloudletReceivedList();
            printCloudletList(newList);

            Log.printLine("CloudSimVMAllocationExample finished!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("An error occurred");
        }
    }

    private static Datacenter createDatacenter(String name) {
        List<Host> hostList = new ArrayList<>();
        List<Pe> peList = new ArrayList<>();
        peList.add(new Pe(0, new PeProvisionerSimple(1000)));
        peList.add(new Pe(1, new PeProvisionerSimple(1500)));
        int hostId = 0;
        int ram = 2048;
        long storage = 1000000;
        int bw = 10000;
        hostList.add(new Host(hostId, new RamProvisionerSimple(ram), new BwProvisionerSimple(bw), storage,
                peList, new VmSchedulerTimeShared(peList)));

        String arch = "x86";
        String os = "Linux";
        String vmm = "Xen";
        double time_zone = 10.0;
        double cost = 3.0;
        double costPerMem = 0.05;
        double costPerStorage = 0.001;
        double costPerBw = 0.0;
        LinkedList<Storage> storageList = new LinkedList<>();

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(arch, os, vmm, hostList, time_zone,
                cost, costPerMem, costPerStorage, costPerBw);

        try {
            return new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static DatacenterBroker createBroker() {
        try {
            return new DatacenterBroker("Broker");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void printCloudletList(List<Cloudlet> list) {
        Log.printLine();
        for (Cloudlet cloudlet : list) {
            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
                Log.printLine("Cloudlet ID: " + cloudlet.getCloudletId() + "\tSUCCESS");
            } else {
                Log.printLine("Cloudlet ID: " + cloudlet.getCloudletId() + "\tFAILED");
            }
        }
    }
}
