// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package lucuma.catalog

import cats.Eq
import cats.syntax.all._
import lucuma.core.enum.Band
import lucuma.core.model.SourceProfile
import lucuma.core.math.BrightnessValue

/**
 * Defines a list of bands It is used, e.g. to extract a brightness from a target
 */
sealed trait BandsList {
  def bands: List[Band]
  def extract(s: SourceProfile): Option[(Band, BrightnessValue)] = {
    val integrated = bands
      .map(b =>
        SourceProfile
          .integratedBrightnessIn(b)
          .getAll(s)
          .map(_.value)
          .tupleLeft(b)
      )
      .flatten

    val surface = bands
      .map(b =>
        SourceProfile
          .surfaceBrightnessIn(b)
          .getAll(s)
          .map(_.value)
          .tupleLeft(b)
      )
      .flatten

    integrated.headOption.orElse(surface.headOption)
  }

  def bandSupported(b: Band) = bands.contains(b)
}

/**
 * Extracts a single band from a target if available
 */
case class SingleBand(band: Band) extends BandsList {
  val bands = List(band)
}

case object NoBands extends BandsList {
  val bands = Nil
}

object BandsList {
  implicit val eqBandList: Eq[BandsList] = Eq.instance((a, b) => a.bands === b.bands)
}
