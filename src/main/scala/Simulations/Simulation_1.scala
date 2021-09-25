package Simulations

import com.typesafe.config.{Config, ConfigFactory}
import org.cloudbus.cloudsim.allocationpolicies.{VmAllocationPolicyRoundRobin, VmAllocationPolicySimple}
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple
import org.cloudbus.cloudsim.cloudlets.{Cloudlet, CloudletSimple}
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter
import org.cloudbus.cloudsim.hosts.{Host, HostSimple}
import org.cloudbus.cloudsim.resources.{File, Pe, PeSimple}
import org.cloudbus.cloudsim.schedulers.cloudlet.{CloudletSchedulerSpaceShared,CloudletSchedulerTimeShared}
import org.cloudbus.cloudsim.schedulers.vm.{VmSchedulerSpaceShared, VmSchedulerTimeShared}
import org.cloudbus.cloudsim.utilizationmodels.{UtilizationModelDynamic, UtilizationModelFull}
import org.cloudbus.cloudsim.vms.{Vm, VmSimple}
import org.cloudsimplus.builders.tables.{CloudletsTableBuilder, TextTableColumn}
import org.slf4j.{Logger, LoggerFactory}
import scala.jdk.CollectionConverters.*
import java.util
import scala.jdk.javaapi.CollectionConverters.asJava
import org.cloudsimplus.builders.tables.CloudletsTableBuilder
import org.cloudsimplus.builders.tables.CsvTable
import java.io.IOException
import java.io.PrintStream

object Simulation_1 {

  val SIM = "my_simulation1"
  val conf: Config = ConfigFactory.load(SIM + ".conf")
  /**
   * All the required configuration values are retrieved and stored in their respective values
   */
  val HOST_PES: Int = conf.getInt(SIM + ".host.pes")
  val VMS: Int = conf.getInt(SIM + ".numVMs")
  val VM_PES: Int = conf.getInt(SIM + ".vm.pesNumber")
  val CLOUDLETS: Int = conf.getInt(SIM + ".numCloudlets")
  val CLOUDLET_PES: Int = conf.getInt(SIM + ".cloudlet.pesNumber")
  val CLOUDLET_LENGTH: Int = conf.getInt(SIM + ".cloudlet.length")

  val LOG: Logger = LoggerFactory.getLogger(getClass)


  val simulation = new CloudSim
  val broker0 = new DatacenterBrokerSimple(simulation)

  val list_datacenter = createDatacenters(conf.getInt(SIM+".num_dcs"))
  val cloudletList: util.List[CloudletSimple] = createCloudlets
  val vmList: util.List[Vm] = createVms

  broker0.submitVmList(vmList)
  broker0.submitCloudletList(cloudletList)

  simulation.start
  LOG.info("The Simulation has started.")

  val finishedCloudlets: util.List[Cloudlet] = broker0.getCloudletFinishedList
  new CloudletsTableBuilder(finishedCloudlets).addColumn(new TextTableColumn("Actual CPU Time"), (cloudlet: Cloudlet) =>  BigDecimal(cloudlet.getActualCpuTime).setScale(2, BigDecimal.RoundingMode.HALF_UP)).addColumn(new TextTableColumn("Total Cost"), (cloudlet: Cloudlet) =>  BigDecimal(cloudlet.getTotalCost).setScale(2, BigDecimal.RoundingMode.HALF_UP)).build()
  LOG.info("The Simulation has ended.")

  /**
   * Sends the datacenter number from 1 to number of datacenters to be created to createDatacenter function.
   */
  def createDatacenters(num_DCs: Int) ={
    val dcs = (1 to num_DCs).map(f=> createDatacenter("datacenter"+(f-1)))
  }
  /**
   * Creates a Network Datacenter and its Hosts
   * parameter - datacenter name, here its datacenter0,datcenter1 depending on the number of datacenters.
   * returns - Network Datacenter
   */
  def createDatacenter(datacenter:String): NetworkDatacenter = {
    LOG.info("Creating Datacenter.")
    val HOSTS: Int = conf.getInt(SIM + "." + datacenter + ".numHosts")
    val hostList_new = (1 to HOSTS).map(host => createHost).toList

    val dc = new NetworkDatacenter(simulation, hostList_new.asJava, new VmAllocationPolicyRoundRobin())
    dc.getCharacteristics
      .setCostPerBw(conf.getInt(SIM + "." + datacenter + ".costPerBw"))
      .setCostPerMem(conf.getInt(SIM + "." + datacenter + ".costPerMem"))
      .setCostPerSecond(conf.getInt(SIM + "." + datacenter + ".cost"))
      .setCostPerStorage(conf.getInt(SIM + "." + datacenter + ".costPerStorage"))
    dc
  }
  /**
   * Creates a list of Hosts (Physical entities to which VMs will be allocated to be run inside Data centers).
   * Sets all the values retrieved from the config file
   */
  def createHost: Host = {
    LOG.info("Creating Host.")
    val peList = (1 to HOST_PES).map(pe => new PeSimple(conf.getInt(SIM + ".host.mips"))).toList
    val ram = conf.getInt(SIM + ".host.ram")
    val bw = conf.getInt(SIM + ".host.bw")
    val storage = conf.getInt(SIM + ".host.storage")
    new HostSimple(ram, bw, storage, asJava[Pe](peList)).setVmScheduler(new VmSchedulerTimeShared())
  }
  
  /**
   * Creates a list of VMs.
   */
  def createVms = {
    LOG.info("Creating VMs.")
    val list = (1 to VMS)
      .map(vm => new VmSimple(conf.getInt(SIM +".vm.mips"), VM_PES).setCloudletScheduler(new CloudletSchedulerSpaceShared)).toList
    list.asJava
  }
  
  /**
   * Creates a list of Cloudlets.
   */
  def createCloudlets = {
    LOG.info("Creating Cloudlets.")
    // Uses the Full Utilization Model meaning a Cloudlet always utilizes a given allocated resource from its Vm at 100%, all the time.
    val utilizationModel = new UtilizationModelFull()
    val list = (1 to CLOUDLETS).map(c => new CloudletSimple(CLOUDLET_LENGTH, CLOUDLET_PES, utilizationModel)).toList
    list.asJava
  }

  def main(args: Array[String]): Unit = {
    Simulation_1
  }

}
