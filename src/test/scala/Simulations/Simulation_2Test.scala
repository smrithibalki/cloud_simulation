package Simulations

import com.typesafe.config.{Config, ConfigFactory}
import org.scalatest.{BeforeAndAfter}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.slf4j.{Logger, LoggerFactory}

class Simulation_2Test extends AnyFlatSpec with Matchers {
  behavior of "configuration parameters module"
  val SIM = "my_simulation2";

  val conf: Config = ConfigFactory.load(SIM + ".conf")
  val LOG: Logger = LoggerFactory.getLogger(getClass)
  it should "obtain the utilization ratio" in {
    conf.getDouble("my_simulation2.utilizationRatio") shouldBe 1
  }

  it should "obtain the MIPS capacity" in {
    conf.getLong("my_simulation2.vm.mips") shouldBe 1000
  }

  it should "obtain the storage capacity" in {
    conf.getLong("my_simulation2.host.storage") shouldBe 1000000
  }
}
