// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package lucuma.ags.api

import lucuma.core.enum.Site
import lucuma.core.enum.GuideProbe

sealed trait SingleProbeStrategyParams {
  def site: Site
  def guideProbe: GuideProbe
}

object SingleProbeStrategyParams {
  // case object AltairAowfsParams extends SingleProbeStrategyParams {
  //   override val guideProbe  = AltairAowfsGuider.instance
  //   override val site        = Site.GN
  //   override def stepSize    = Angle.fromDegrees(90)
  //   override def minDistance = Some(Angle.zero)
  // }
  //
  // case object Flamingos2OiwfsParams extends SingleProbeStrategyParams {
  //   override val guideProbe = Flamingos2OiwfsGuideProbe.instance
  //   override val site       = Site.GS
  // }
  //
  case class GmosOiwfsParams(site: Site) extends SingleProbeStrategyParams {
    override val guideProbe = GuideProbe.OIWFS
  }

  // case object GnirsOiwfsParams extends SingleProbeStrategyParams {
  //   override val guideProbe = GnirsOiwfsGuideProbe.instance
  //   override val site       = Site.GN
  //   override val probeBands = SingleBand(MagnitudeBand.K)
  // }
  //
  // case object NifsOiwfsParams extends SingleProbeStrategyParams {
  //   override val guideProbe = NifsOiwfsGuideProbe.instance
  //   override val site       = Site.GN
  //   override val probeBands = SingleBand(MagnitudeBand.K)
  // }
  //
  // case object NiriOiwfsParams extends SingleProbeStrategyParams {
  //   override val guideProbe = NiriOiwfsGuideProbe.instance
  //   override val site       = Site.GN
  //   override val probeBands = SingleBand(MagnitudeBand.K)
  // }

// case class PwfsParams(site: Site, guideProbe: PwfsGuideProbe) extends SingleProbeStrategyParams {
//   override def stepSize = Angle.fromDegrees(360)
//
//   private def vignettingProofPatrolField(ctx: ObsContext): PatrolField = {
//     val min = guideProbe.getVignettingClearance(ctx)
//     guideProbe.getCorrectedPatrolField(PatrolField.fromRadiusLimits(min, PwfsGuideProbe.PWFS_RADIUS), ctx)
//   }
//
//   override def radiusConstraint(ctx: ObsContext): Option[RadiusConstraint] =
//     RadiusLimitCalc.getAgsQueryRadiusLimits(Some(vignettingProofPatrolField(ctx)), ctx)
//
//   // We have a special validator for Pwfs.
//   override def validator(ctx: ObsContext): GuideStarValidator =
//     vignettingProofPatrolField(ctx).validator(ctx)
//   }
}
