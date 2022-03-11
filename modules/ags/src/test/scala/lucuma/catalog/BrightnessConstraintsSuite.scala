// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package lucuma.catalog

import cats.syntax.all._
import coulomb._
import lucuma.core.enum.Band
import lucuma.core.math.BrightnessUnits._
import lucuma.core.math.BrightnessValue
import lucuma.core.model.SourceProfile
import lucuma.core.model.SourceProfile._
import lucuma.core.model.SpectralDefinition._
import lucuma.core.model.UnnormalizedSED
import lucuma.core.enum.StellarLibrarySpectrum
import lucuma.core.math.dimensional._
import lucuma.core.math.units._
import scala.collection.immutable.SortedMap

class BrightnessConstraintsSuite extends munit.FunSuite {
  val sed                               = UnnormalizedSED.StellarLibrary(StellarLibrarySpectrum.A0V)
  val profile                           = Point(BandNormalized(sed, SortedMap.empty))
  def setBrightness(b: Band, v: Double) =
    SourceProfile.integratedSpectralDefinition
      .modify {
        case sd: BandNormalized[Integrated] =>
          BandNormalized
            .brightnesses[Integrated]
            .modify(bm =>
              bm + (b -> BrightnessValue.fromDouble(v).withUnit[VegaMagnitude].toMeasureTagged)
            )(sd)
        case x                              => x
      }

  test("filter targets on band and faintness") {
    val ml =
      BrightnessConstraints(SingleBand(Band.GaiaG), FaintnessConstraint(10.0), None)

    assert(!ml.filter(profile))

    assert(ml.filter(setBrightness(Band.GaiaG, 4.999)(profile)))

    assert(!ml.filter(setBrightness(Band.GaiaG, 10.001)(profile)))

  }
  test("filter targets on band, faintness and saturation") {
    val ml =
      BrightnessConstraints(SingleBand(Band.GaiaG),
                            FaintnessConstraint(10.0),
                            SaturationConstraint(5).some
      )

    assert(!ml.filter(profile))
    assert(!ml.filter(setBrightness(Band.GaiaG, 4.999)(profile)))
    assert(ml.filter(setBrightness(Band.GaiaG, 5.001)(profile)))
  }
  test("support the union operation") {
    val m1 = BrightnessConstraints(SingleBand(Band.GaiaG), FaintnessConstraint(10.0), None)
    val m2 = BrightnessConstraints(SingleBand(Band.J), FaintnessConstraint(10.0), None)
    // Different band
    assert(m1.union(m2).isEmpty)

    val m3 = BrightnessConstraints(SingleBand(Band.GaiaG), FaintnessConstraint(5.0), None)
    assertEquals(
      m1.union(m3),
      Some(BrightnessConstraints(SingleBand(Band.GaiaG), FaintnessConstraint(10.0), None))
    )

    val m4 = BrightnessConstraints(SingleBand(Band.GaiaG), FaintnessConstraint(15.0), None)
    assertEquals(
      m1.union(m4),
      Some(BrightnessConstraints(SingleBand(Band.GaiaG), FaintnessConstraint(15.0), None))
    )

    val m5 =
      BrightnessConstraints(Band.GaiaG, FaintnessConstraint(15.0), Some(SaturationConstraint(10.0)))
    assertEquals(m1.union(m5),
                 BrightnessConstraints(Band.GaiaG, FaintnessConstraint(15.0), None).some
    )

    val m6 =
      BrightnessConstraints(Band.GaiaG, FaintnessConstraint(15.0), Some(SaturationConstraint(15.0)))
    assertEquals(m5.union(m6),
                 BrightnessConstraints(Band.GaiaG,
                                       FaintnessConstraint(15.0),
                                       SaturationConstraint(10.0).some
                 ).some
    )
  }
}
