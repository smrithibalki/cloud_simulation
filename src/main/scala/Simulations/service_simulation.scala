package Simulations
import com.typesafe.config.{Config, ConfigFactory}
import org.cloudbus.cloudsim.allocationpolicies.{VmAllocationPolicy, VmAllocationPolicyBestFit, VmAllocationPolicyFirstFit, VmAllocationPolicyRoundRobin, VmAllocationPolicySimple}
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple
import org.cloudbus.cloudsim.cloudlets.{Cloudlet, CloudletSimple}
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.datacenters.DatacenterSimple
import org.cloudbus.cloudsim.hosts.{Host, HostSimple}
import org.cloudbus.cloudsim.resources.{Pe, PeSimple}
import org.cloudbus.cloudsim.schedulers.cloudlet.{CloudletScheduler, CloudletSchedulerSpaceShared, CloudletSchedulerTimeShared}
import org.cloudbus.cloudsim.schedulers.vm.{VmScheduler, VmSchedulerSpaceShared, VmSchedulerTimeShared}
import org.cloudbus.cloudsim.utilizationmodels.{UtilizationModelDynamic, UtilizationModelFull}
import org.cloudbus.cloudsim.vms.{Vm, VmSimple}
import org.cloudsimplus.builders.tables.{CloudletsTableBuilder, TextTableColumn}
import org.slf4j.{Logger, LoggerFactory}
import scala.jdk.CollectionConverters.*
import scala.jdk.javaapi.CollectionConverters.asJava
import java.util
import scala.jdk.javaapi.CollectionConverters.asJava


object service_simulation {


  val SIM = "service_simulation"
  val conf: Config = ConfigFactory.load(SIM + ".conf")
  val CLOUDLET_PES =conf.getList(SIM+".cloudlet.numberPES")
  val r = scala.util.Random
  val default =conf.getInt(SIM + ".default_list_index")
  val VMS: Int = conf.getInt(SIM + ".numVMs")
  val VM_PES:Int = conf.getInt(SIM + ".vm.numberPES")
  val service = conf.getString(SIM +".service")
  val CLOUDLETS = conf.getList(SIM+".numCloudlets")


  val CLOUDLET_LENGTH = conf.getList(SIM + ".cloudlet.length")

  val LOG: Logger = LoggerFactory.getLogger(getClass)

  val simulation = new CloudSim
  val broker0 = new DatacenterBrokerSimple(simulation)

  val vmList: util.List[Vm] = createVms
  broker0.submitVmList(vmList)


  def objectToInt(s: Object) :Int ={
    s match {
      case n: java.lang.Integer => n
    }

  }
  def objectToString(s1: Object) :String ={
    print("string is",s1)
    s1 match {
      case str: java.lang.String => str
    }

  }
  if (service=="SaaS"){
    LOG.info("service is SaaS")
    val a =objectToInt(CLOUDLETS.get(default).unwrapped())
    val b =objectToInt(CLOUDLET_LENGTH.get(default).unwrapped())
    val c =objectToInt(CLOUDLET_PES.get(default).unwrapped())
    val os =conf.getString(SIM + ".datacenter0.os")
    createCloudlets(a,b,c)

    createDatacenter(conf.getInt(SIM + ".datacenter0.numberHosts"), conf.getInt(SIM + ".host.pes"),
      os,"datacenter0")
  }
  else if (service=="PaaS"){
    LOG.info("service is PaaS")
    val a =objectToInt(CLOUDLETS.get(r.nextInt(3)).unwrapped())
    val b =objectToInt(CLOUDLET_LENGTH.get(r.nextInt(3)).unwrapped())
    val c =objectToInt(CLOUDLET_PES.get(r.nextInt(3)).unwrapped())
    val os =conf.getString(SIM + ".datacenter1.os")

    createCloudlets(a,b,c)

    createDatacenter(conf.getInt(SIM + ".datacenter1.numberHosts"), conf.getInt(SIM + ".host.pes"),
      os,"datacenter1")
  }
  else if (service=="IaaS"){
    LOG.info("service is IaaS")
    val num_cloudlets =objectToInt(CLOUDLETS.get(r.nextInt(3)).unwrapped())
    val cloudlet_len =objectToInt(CLOUDLET_LENGTH.get(r.nextInt(3)).unwrapped())
    val cloud_pes =objectToInt(CLOUDLET_PES.get(r.nextInt(3)).unwrapped())
    val os = objectToString((conf.getList(SIM+".datacenter2.os")).get(r.nextInt(4)).unwrapped())
    createCloudlets(num_cloudlets,cloudlet_len,cloud_pes)

    createDatacenter(conf.getInt(SIM + ".datacenter2.numberHosts"), conf.getInt(SIM + ".host.pes"),
      os,"datacenter2")
  }
  simulation.start()

  val finishedCloudlets: util.List[Cloudlet] = broker0.getCloudletFinishedList
  new CloudletsTableBuilder(finishedCloudlets).addColumn(new TextTableColumn("Actual CPU Time"), (cloudlet: Cloudlet) =>  BigDecimal(cloudlet.getActualCpuTime).setScale(2, BigDecimal.RoundingMode.HALF_UP)).addColumn(new TextTableColumn("Total Cost"), (cloudlet: Cloudlet) =>  BigDecimal(cloudlet.getTotalCost).setScale(2, BigDecimal.RoundingMode.HALF_UP)).build()
  LOG.info("The Simulation has ended.")

  def createDatacenter(numberHosts: Int, hostPES: Int, os:String,selectedDC: String): DatacenterSimple = {

    val hostList_new = (1 to numberHosts).map(host => createHost(hostPES)).toList
    val dc = new DatacenterSimple(simulation, hostList_new.asJava,
      new VmAllocationPolicyRoundRobin)
    dc.getCharacteristics
      .setCostPerBw(conf.getInt(SIM + "." + selectedDC + ".costPerBw"))
      .setCostPerMem(conf.getInt(SIM + "." + selectedDC + ".costPerMem"))
      .setCostPerSecond(conf.getInt(SIM + "." + selectedDC + ".cost"))
      .setCostPerStorage(conf.getInt(SIM + "." + selectedDC + ".costPerStorage"))
      .setOs(os)
    dc
  }
  def createHost(hostPES: Int): Host = {
    val peList = (1 to hostPES).map(pe => new PeSimple(conf.getInt(SIM + ".host.mips"))).toList
    val ram = conf.getInt(SIM + ".host.ram")
    val bw = conf.getInt(SIM + ".host.bw")
    val storage = conf.getInt(SIM + ".host.storage")
    new HostSimple(ram, bw, storage, asJava[Pe](peList))
      .setVmScheduler( new VmSchedulerSpaceShared())
  }

  def createVms : util.List[Vm] = {
    val list = (1 to VMS).map(vm => new VmSimple(conf.getInt(SIM + ".vm.mips"), VM_PES)
      .setSize(conf.getInt(SIM + ".vm.size"))
      .setBw(conf.getInt(SIM + ".vm.bw"))
      .setRam(conf.getInt(SIM + ".vm.ram"))
      .setCloudletScheduler(new CloudletSchedulerTimeShared)).toList

    list.asJava
  }

  def createCloudlets(num_clouldlets:Int,cloudlet_len:Int,clouldlet_pes:Int) :Unit =  {
    val utilizationModel = new UtilizationModelDynamic(0.5)

    val list = (1 to num_clouldlets).map(c => new CloudletSimple(cloudlet_len, clouldlet_pes, utilizationModel)).toList
    broker0.submitCloudletList(list.asJava)
    }


  def main(args: Array[String]): Unit = {
    service_simulation

  }
}
