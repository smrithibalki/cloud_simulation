package Simulations

import com.typesafe.config.{Config, ConfigFactory}
import org.cloudbus.cloudsim.allocationpolicies.{VmAllocationPolicyBestFit, VmAllocationPolicyRandom}
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple
import org.cloudbus.cloudsim.cloudlets.{Cloudlet, CloudletSimple}
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.datacenters.DatacenterSimple
import org.cloudbus.cloudsim.hosts.{Host, HostSimple}
import org.cloudbus.cloudsim.resources.{Pe, PeSimple}
import org.cloudbus.cloudsim.schedulers.cloudlet.{CloudletSchedulerTimeShared,CloudletSchedulerSpaceShared}
import org.cloudbus.cloudsim.schedulers.vm.{VmSchedulerSpaceShared,VmSchedulerTimeShared}
import org.cloudbus.cloudsim.utilizationmodels.{UtilizationModelDynamic,UtilizationModelFull}
import org.cloudbus.cloudsim.vms.{Vm, VmSimple}
import org.cloudsimplus.builders.tables.{CloudletsTableBuilder, TextTableColumn}
import org.slf4j.{Logger, LoggerFactory}
import scala.jdk.CollectionConverters.*
import java.util
import scala.jdk.javaapi.CollectionConverters.asJava

object Simulation_2 {
  val SIM = "my_simulation2"
  val conf: Config = ConfigFactory.load(SIM + ".conf")

  val HOSTS: Int = conf.getInt(SIM + ".datacenter.numHosts")
  val HOST_PES: Int = conf.getInt(SIM + ".host.pes")
  val VMS: Int = conf.getInt(SIM + ".numVMs")
  val VM_PES: Int = conf.getInt(SIM + ".vm.pesNumber")
  val CLOUDLETS: Int = conf.getInt(SIM + ".numCloudlets")
  val CLOUDLET_PES: Int = conf.getInt(SIM + ".cloudlet.pesNumber")
  val CLOUDLET_LENGTH: Int = conf.getInt(SIM + ".cloudlet.length")

  val LOG: Logger = LoggerFactory.getLogger(getClass)


  val simulation = new CloudSim
  val broker0 = new DatacenterBrokerSimple(simulation)
  val datacenter0: DatacenterSimple = createDatacenter(HOSTS)

  val vmList: util.List[Vm] = createVms
  val cloudletList: util.List[CloudletSimple] = createCloudlets

  broker0.submitVmList(vmList)
  broker0.submitCloudletList(cloudletList)
  simulation.start
  LOG.info("The Simulation has started.")


  val finishedCloudlets: util.List[Cloudlet] = broker0.getCloudletFinishedList
  new CloudletsTableBuilder(finishedCloudlets).addColumn(new TextTableColumn("Actual CPU Time"), (cloudlet: Cloudlet) =>  BigDecimal(cloudlet.getActualCpuTime).setScale(2, BigDecimal.RoundingMode.HALF_UP)).addColumn(new TextTableColumn("Total Cost"), (cloudlet: Cloudlet) =>  BigDecimal(cloudlet.getTotalCost).setScale(2, BigDecimal.RoundingMode.HALF_UP)).build()
  LOG.info("The Simulation has ended.")

  /**
   * Creates a Datacenter and its Hosts.
   * parameter => number of hosts in each datacenter
   * returns datacenter
   */
  def createDatacenter(numHosts: Int): DatacenterSimple = {

    LOG.info("Creating Datacenter.")

    val hostList_new = (1 to numHosts).map(host => createHost).toList
    val dc = new DatacenterSimple(simulation, hostList_new.asJava, new VmAllocationPolicyBestFit)
    dc.getCharacteristics
      .setCostPerBw(conf.getInt(SIM + ".datacenter.costPerBw"))
      .setCostPerMem(conf.getInt(SIM + ".datacenter.costPerMem"))
      .setCostPerSecond(conf.getInt(SIM + ".datacenter.cost"))
      .setCostPerStorage(conf.getInt(SIM + ".datacenter.costPerStorage"))
    dc
  }

  /**
   * Creates a list of Hosts (Physical entities to which VMs will be allocated to be run inside Data centers).
   * Sets all the values retrieved from the config file
   */
  def createHost: Host = {

    LOG.info("Creating Hosts.")

    val peList = (1 to HOST_PES).map(pe => new PeSimple(conf.getInt(SIM + ".host.mips"))).toList
    val ram = conf.getInt(SIM + ".host.ram")
    val bw = conf.getInt(SIM + ".host.bw")
    val storage = conf.getInt(SIM + ".host.storage")
    new HostSimple(ram, bw, storage, asJava[Pe](peList)).setVmScheduler(new VmSchedulerTimeShared())
  }

  /**
   * Creates a list of Vms
   * Sets all the values retrieved from the config file
   * returns the list of Vms to be submitted to broker
   */
  def createVms: util.List[Vm] = {

    LOG.info("Creating Vms.")

    val list = (1 to VMS).map(vm => new VmSimple(conf.getInt(SIM + ".vm.mips"), VM_PES).setCloudletScheduler(new CloudletSchedulerSpaceShared)
    .setSize(conf.getInt(SIM + ".vm.size"))
    .setBw(conf.getInt(SIM + ".vm.bw"))
    .setRam(conf.getInt(SIM + ".vm.ram"))
    ).toList
    list.asJava
  }

  /**
   * Creates a list of Cloudlets.
   * returns the list of cloudlets to be submitted to broker
   */
  def createCloudlets: util.List[CloudletSimple] = {

    LOG.info("Creating Cloudlets.")

    val utilizationModel = new UtilizationModelDynamic(conf.getInt(SIM+".utilizationRatio"))  // same as UtilizationModelFull
    val list = (1 to CLOUDLETS).map(c => new CloudletSimple(CLOUDLET_LENGTH, CLOUDLET_PES, utilizationModel)).toList
    list.asJava
  }

  def main(args: Array[String]): Unit = {
    Simulation_2
  }
}
