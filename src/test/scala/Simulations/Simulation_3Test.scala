package Simulations

import com.typesafe.config.{Config, ConfigFactory}
import org.scalatest.{BeforeAndAfter}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.slf4j.{Logger, LoggerFactory}


class Simulation_3Test extends AnyFunSuite with BeforeAndAfter {

  val SIM = "my_simulation3";

  val conf: Config = ConfigFactory.load(SIM + ".conf")
  val LOG: Logger = LoggerFactory.getLogger(getClass)

  test("Simulation_3.createDatacenter") {

    val name = "datacenter"

    val datacenter = Simulation_3.createDatacenter(5)

    val numHosts = conf.getInt(SIM + ".datacenter.numHosts")

    LOG.debug("Testing whether data center got created successfully")
    println(assert(datacenter != null))

    LOG.debug("Testing whether the number of hosts matches the config")
    println(assert(datacenter.getHostList.size() == numHosts))
  }

  test("Simulation_3.createVms") {

    val num_vms = conf.getInt(SIM + ".numVMs")

    // VMs creation call
    val vmlist = Simulation_3.createVms

    LOG.debug("Testing whether vmlist not empty ")
    assert(vmlist.size() != 0)

    LOG.debug("Testing whether number of VMs matches config")
    assert(vmlist.size == num_vms)
  }



  test("Simulation_3.createCloudlets") {

    val num_cloudlets =conf.getInt(SIM + "." + "numCloudlets")

    // Cloudlets creation call
    val cloudlets = Simulation_1.createCloudlets

    LOG.debug("Testing whether cloudlets list not empty ")
    assert(cloudlets.size() != 0)

    LOG.debug("Testing whether cloudlets list size matches config")
    assert(cloudlets.size() == num_cloudlets)
  }


}
