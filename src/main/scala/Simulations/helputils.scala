package Simulations

import com.typesafe.config.{Config, ConfigFactory}
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicyRoundRobin
import org.cloudbus.cloudsim.cloudlets.CloudletSimple
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.datacenters.DatacenterSimple
import org.cloudbus.cloudsim.distributions.UniformDistr
import org.cloudbus.cloudsim.hosts.{Host, HostSimple}
import org.cloudbus.cloudsim.resources.{Pe, PeSimple}
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerSpaceShared
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerTimeShared
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelDynamic
import org.cloudbus.cloudsim.vms.{Vm, VmSimple}
import org.slf4j.{Logger, LoggerFactory}
import java.util
import scala.jdk.CollectionConverters.*
import scala.jdk.javaapi.CollectionConverters.asJava
object helputils {

  val SIM = "my_simulation3"
  val simulation = new CloudSim
  val conf: Config = ConfigFactory.load(SIM + ".conf")
  val HOSTS: Int = conf.getInt(SIM + "." + "datacenter" + ".numHosts")
  val HOST_PES: Int = conf.getInt(SIM + "." + "host" + ".pes")
  val VMS: Int = conf.getInt(SIM + "." + "numVMs")
  val VM_PES: Int = conf.getInt(SIM + "." + "vm" + ".pesNumber")
  val CLOUDLETS: Int = conf.getInt(SIM + "." + "numCloudlets")
  val CLOUDLET_PES: Int = conf.getInt(SIM + "." + "cloudlet" + ".pesNumber")
  val CLOUDLET_LENGTHS: Array[Long] = Array(2000, 4000, 10000, 16000, 2000, 30000, 20000)
  val rand = new UniformDistr(0, CLOUDLET_LENGTHS.length, 1)
  val LOG: Logger = LoggerFactory.getLogger(getClass)


  /**
   * Creates a list of Hosts (Physical entities to which VMs will be allocated to be run inside Data centers).
   * Sets all the values retrieved from the config file
   */

  def createHost: Host = {
    
    val peList = (1 to HOST_PES).map(pe => new PeSimple(1000)).toList
    val ram = conf.getInt(SIM + "." + "host" + ".ram")
    val bw = conf.getInt(SIM + "." + "host" + ".bw")
    val storage = conf.getInt(SIM + "." + "host" + ".storage")
    new HostSimple(ram, bw, storage, asJava[Pe](peList)).setVmScheduler(new VmSchedulerTimeShared())
  }
  /**
   * Creates a list of Cloudlets.
   */
  def createCloudlets = {
    val utilizationModel = new UtilizationModelDynamic(0.5)
    val list = (1 to CLOUDLETS).map(c => new CloudletSimple(CLOUDLET_LENGTHS(rand.sample.toInt), CLOUDLET_PES, utilizationModel)).toList
    list.asJava
  }


}
