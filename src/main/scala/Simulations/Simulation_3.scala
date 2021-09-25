package Simulations

import Simulations.helputils.{LOG,simulation,CLOUDLETS,CLOUDLET_PES, HOSTS, SIM, VMS, VM_PES, conf, createHost}
import com.typesafe.config.{Config, ConfigFactory}
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicyRoundRobin
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicyBestFit
import org.cloudbus.cloudsim.allocationpolicies.{VmAllocationPolicyFirstFit, VmAllocationPolicySimple}
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicyRandom
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple
import org.cloudbus.cloudsim.cloudlets.{Cloudlet, CloudletSimple}
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.datacenters.DatacenterSimple
import org.cloudbus.cloudsim.hosts.{Host, HostSimple}
import org.cloudbus.cloudsim.resources.{Pe, PeSimple}
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerSpaceShared
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerSpaceShared
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerTimeShared
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelDynamic
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelStochastic
import org.cloudbus.cloudsim.distributions.{ContinuousDistribution, UniformDistr}
import org.cloudbus.cloudsim.vms.{Vm, VmSimple}
import org.cloudsimplus.builders.tables.{CloudletsTableBuilder, TextTableColumn}
import org.slf4j.{Logger, LoggerFactory}

import scala.jdk.CollectionConverters.*
import java.util
import scala.jdk.javaapi.CollectionConverters.asJava

object Simulation_3 {

  val CLOUDLET_LENGTH: Int = conf.getInt(SIM + ".cloudlet.length")
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

  /**
   * Creates a Datacenter and its Hosts.
   */
  def createDatacenter(numHosts: Int): DatacenterSimple = {

    LOG.info("Creating Datacenter.")
    
    val hostList_new = (1 to numHosts).map(host => createHost).toList

    val dc = new DatacenterSimple(simulation, hostList_new.asJava, new VmAllocationPolicyRoundRobin())
    dc.getCharacteristics
      .setCostPerBw(conf.getInt(SIM + ".datacenter.costPerBw"))
      .setCostPerMem(conf.getInt(SIM + ".datacenter.costPerMem"))
      .setCostPerSecond(conf.getInt(SIM + ".datacenter.cost"))
      .setCostPerStorage(conf.getInt(SIM + ".datacenter.costPerStorage"))
    dc
  }
 

  def createVms: util.List[Vm] = {
    val list = (1 to VMS).map(vm => new VmSimple(conf.getInt(SIM + ".vm.mips"), VM_PES).setCloudletScheduler(new CloudletSchedulerSpaceShared)).toList
    list.asJava
  }
  /**
   * Creates a list of Cloudlets.
   */
  def createCloudlets: util.List[CloudletSimple] = {
    // UtilizationModel defining the Cloudlets use 75% of any resource all the time
    val utilizationModel = new UtilizationModelDynamic(0.75)
    val list = (1 to CLOUDLETS).map(c => new CloudletSimple(CLOUDLET_LENGTH, CLOUDLET_PES, utilizationModel)).toList
    list.asJava
  }

  def main(args: Array[String]): Unit = {
    Simulation_3
  }

}
