package Simulations

import Simulations.Simulation_1.LOG

import java.util
import com.typesafe.config.{Config, ConfigFactory}
import org.cloudbus.cloudsim.allocationpolicies.{VmAllocationPolicyBestFit, VmAllocationPolicyFirstFit, VmAllocationPolicyRoundRobin, VmAllocationPolicySimple}
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple
import org.cloudbus.cloudsim.cloudlets.{Cloudlet, CloudletSimple}
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.datacenters.DatacenterSimple
import org.cloudbus.cloudsim.distributions.{ContinuousDistribution, UniformDistr}
import org.cloudbus.cloudsim.hosts.{Host, HostSimple}
import org.cloudbus.cloudsim.resources.{Pe, PeSimple}
import org.cloudbus.cloudsim.schedulers.cloudlet.{CloudletSchedulerSpaceShared, CloudletSchedulerTimeShared}
import org.cloudbus.cloudsim.provisioners.{PeProvisionerSimple, ResourceProvisionerSimple}
import org.cloudbus.cloudsim.resources.{Pe, PeSimple, Processor}
import org.cloudbus.cloudsim.schedulers.vm.{VmSchedulerSpaceShared, VmSchedulerTimeShared}
import org.cloudbus.cloudsim.utilizationmodels.{UtilizationModelDynamic, UtilizationModelFull}
import org.cloudbus.cloudsim.vms.{Vm, VmSimple}
import org.cloudsimplus.autoscaling.{HorizontalVmScaling, HorizontalVmScalingSimple, VerticalVmScaling, VerticalVmScalingSimple}
import org.cloudsimplus.builders.tables.{CloudletsTableBuilder, TextTableColumn}
import org.slf4j.{Logger, LoggerFactory}
import Simulations.helputils.{HOSTS, LOG, SIM, VMS, VM_PES, conf, createCloudlets, createHost, simulation}

import scala.jdk.CollectionConverters.*
import scala.jdk.javaapi.CollectionConverters.asJava

/**
 * A custom example using CloudSim Plus framework which has a predefined DataCenter configuration
 * having First Fit VM Allocation Policy, Full Cloudlet Utilization Model, Space-Shared
 * Cloudlet Scheduler Type, Time-Shared VM Scheduler Type
 *
 */
object horizontalVmScaling_simulation {
  
  val broker0 = new DatacenterBrokerSimple(simulation)
  val datacenter: DatacenterSimple = createDatacenter(HOSTS)
  val vmList: util.List[Vm] = createVms
  val cloudletList: util.List[CloudletSimple] = createCloudlets
  
  broker0.setVmDestructionDelayFunction((vm: Vm) => 10.0)
  broker0.submitVmList(vmList)
  broker0.submitCloudletList(cloudletList)
  simulation.start
  


  val finishedCloudlets: util.List[Cloudlet] = broker0.getCloudletFinishedList
  new CloudletsTableBuilder(finishedCloudlets).addColumn(new TextTableColumn("Actual CPU Time"), (cloudlet: Cloudlet) =>  BigDecimal(cloudlet.getActualCpuTime).setScale(2, BigDecimal.RoundingMode.HALF_UP)).addColumn(new TextTableColumn("Total Cost"), (cloudlet: Cloudlet) =>  BigDecimal(cloudlet.getTotalCost).setScale(2, BigDecimal.RoundingMode.HALF_UP)).build()
  

  /**
   * Creates a Datacenter and its Hosts.
   */
  def createDatacenter(numHosts: Int): DatacenterSimple = {

    
    val hostList_new = (1 to numHosts).map(host => createHost).toList

    val dc = new DatacenterSimple(simulation, hostList_new.asJava, new VmAllocationPolicyRoundRobin())
    dc.setSchedulingInterval(1)
    dc.getCharacteristics
      .setCostPerBw(conf.getInt(SIM + ".datacenter.costPerBw"))
      .setCostPerMem(conf.getInt(SIM + ".datacenter.costPerMem"))
      .setCostPerSecond(conf.getInt(SIM + ".datacenter.cost"))
      .setCostPerStorage(conf.getInt(SIM + ".datacenter.costPerStorage"))
    dc
  }
  
  /**
   * Creates a list of VMs.
   */
  def createVms = {
    val list = (1 to VMS)
      .map(vm => new VmSimple(conf.getInt(SIM +".vm.mips"), VM_PES).setCloudletScheduler(new CloudletSchedulerSpaceShared).setHorizontalScaling(createHorizontalVmScaling)).toList

    list.asJava
  }
  
   def isVmOverloaded(vm: Vm) = vm.getCpuPercentUtilization > 0.7

  def createHorizontalVmScaling = {
      val horizontalScaling = new HorizontalVmScalingSimple
      horizontalScaling.setOverloadPredicate(this.isVmOverloaded)
      horizontalScaling
    }

  def main(args: Array[String]): Unit = {
    horizontalVmScaling_simulation
  }
}
