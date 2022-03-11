// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package lucuma.ags.api

import lucuma.core.enum.GuideProbe
import lucuma.core.model.Target.Sidereal
import lucuma.core.math.Angle
import lucuma.core.enum.Instrument
import lucuma.core.enum.Site
import lucuma.ags.api.SingleProbeStrategyParams._
import cats.Applicative
// import edu.gemini.ags.api.AgsAnalysis.NotReachable
// import edu.gemini.ags.api.AgsMagnitude.{MagnitudeCalc, MagnitudeTable}
// import edu.gemini.catalog.api.CatalogQuery
// import edu.gemini.spModel.ags.AgsStrategyKey
// import edu.gemini.spModel.core.{Angle, BandsList, Coordinates, SiderealTarget}
// import edu.gemini.spModel.guide.{GuideProbe, GuideStarValidation, ValidatableGuideProbe}
// import edu.gemini.spModel.obs.context.ObsContext
// import edu.gemini.spModel.rich.shared.immutable._
// import edu.gemini.shared.util.immutable.{Option => JOption, Pair => JPair, Some => JSome}
// import edu.gemini.spModel.target.SPTarget
// import edu.gemini.spModel.target.env._
//
// import scala.collection.JavaConverters._
// import scala.concurrent.duration._
//
// import scala.concurrent.{Await, ExecutionContext, Future}
//
/**
 * Pairs a `GuideProbe` with candidate guide stars.
 */
final case class ProbeCandidates(guideProbe: GuideProbe, targets: List[Sidereal])

/**
 * An assignment of a guide star to a particular guide probe.
 */
case class Assignment(guideProbe: GuideProbe, guideStar: Sidereal)

/**
 * Results of running an AGS selection. The position angle for which the results are valid along
 * with all assignments of guide probes to stars.
 */
case class Selection(posAngle: Angle, assignments: List[Assignment])

trait AgsStrategy[F[_]] {
//   def key: AgsStrategyKey
//
//   def magnitudes(ctx: ObsContext, mt: MagnitudeTable): List[(GuideProbe, MagnitudeCalc)]
//
//   def analyze(ctx: ObsContext, mt: MagnitudeTable): List[AgsAnalysis]
//
//   def analyze(ctx: ObsContext, mt: MagnitudeTable, guideProbe: ValidatableGuideProbe, guideStar: SiderealTarget): Option[AgsAnalysis]
//
//   def analyzeForJava(ctx: ObsContext, mt: MagnitudeTable, guideProbe: ValidatableGuideProbe, guideStar: SiderealTarget): JOption[AgsAnalysis] = {
//     val spTarget = new SPTarget(SiderealTarget.empty.copy(coordinates = Coordinates(guideStar.coordinates.ra, guideStar.coordinates.dec)))
//     if (guideProbe.validate(spTarget, ctx) != GuideStarValidation.VALID) new JSome(NotReachable(guideProbe, guideStar))
//     else analyze(ctx, mt, guideProbe, guideStar).asGeminiOpt
//   }
//
//   def analyzeMagnitude(ctx: ObsContext, mt: MagnitudeTable, guideProbe: ValidatableGuideProbe, guideStar: SiderealTarget): Option[AgsAnalysis]
//
//   def candidates(ctx: ObsContext, mt: MagnitudeTable)(ec: ExecutionContext): Future[List[ProbeCandidates]]
//
//   def candidatesForJava(ctx: ObsContext, mt: MagnitudeTable, timeoutSec: Int, ec: ExecutionContext): java.util.List[ProbeCandidates] =
//     Await.result(candidates(ctx, mt)(ec), timeoutSec.seconds).asJava
//
//   /**
//    * Returns a list of catalog queries that would be used to search for guide stars with the given context
//    */
//   def catalogQueries(ctx: ObsContext, mt: MagnitudeTable): List[CatalogQuery]
//
//   def estimate(ctx: ObsContext, mt: MagnitudeTable)(ec: ExecutionContext): Future[AgsStrategy.Estimate]
//
  type ObsContext     = String
  type MagnitudeTable = String
  def select(ctx: ObsContext, mt: MagnitudeTable): F[Selection]
//
//   def selectForJava(ctx: ObsContext, mt: MagnitudeTable, timeoutSec: Int, ec: ExecutionContext): JOption[JPair[Angle, java.util.List[AgsStrategy.Assignment]]] =
//     Await.result(select(ctx, mt)(ec), timeoutSec.seconds).map { sel =>
//       new JPair(sel.posAngle, sel.assignments.asJava)
//     }.asGeminiOpt
//
//
//   def guideProbes: List[GuideProbe]
//
//   /**
//    * Indicates the bands that will be used for a given probe
//    */
//   def probeBands: BandsList
//
}
//
// object AgsStrategy {
//   object Estimate {
//     val CompleteFailure   = Estimate(0.0)
//     val GuaranteedSuccess = Estimate(1.0)
//
//     def toEstimate(probability: Double): Estimate =
//       Estimate(probability).normalize
//   }
//
//   /**
//    * Estimation of success of finding a guide star at phase 2 time.
//    */
//   case class Estimate(probability: Double) extends AnyVal {
//     def normalize: Estimate =
//       if (probability <= 0) Estimate.CompleteFailure
//       else if (probability >= 1) Estimate.GuaranteedSuccess
//       else this
//   }
//
//   /**
//    * An assignment of a guide star to a particular guide probe.
//    */
//   case class Assignment(guideProbe: GuideProbe, guideStar: SiderealTarget)
//
//   /**
//    * Results of running an AGS selection.  The position angle for which the
//    * results are valid along with all assignments of guide probes to stars.
//    */
//   case class Selection(posAngle: Angle, assignments: List[Assignment]) {
//     /**
//      * Creates a new TargetEnvironment with guide stars for each assignment in
//      * the Selection.
//      */
//     def applyTo(env: TargetEnvironment): TargetEnvironment = {
//       import AutomaticGroup.Active
//
//       val targetMap = ==>>.fromList(assignments.map { case Assignment(gp,gs) =>
//         gp -> new SPTarget(gs)
//       })
//       val newAuto = Active(targetMap, posAngle): AutomaticGroup
//       val oldAuto = TargetEnv.auto.get(env)
//
//       // True if the pos angle differs.
//       def posAngleUpdated = oldAuto match {
//         case Active(_, oldPa) => oldPa =/= posAngle
//         case _                => true
//       }
//
//       // SPTargets are compared by references, so we extract the names and compare.
//       def extractNames(auto: AutomaticGroup) = auto.targetMap.map(_.getName)
//
//       // If this is different from the old automatic GG, then replace.
//       val updated = (extractNames(oldAuto) =/= extractNames(newAuto)) || posAngleUpdated
//       if (updated) TargetEnv.auto.set(env, newAuto) else env
//     }
//
//     def applyTo(ctx: ObsContext): ObsContext = {
//       // Make a new TargetEnvironment with the guide probe assignments.  Update
//       // the position angle as well if the automatic group is primary.
//       applyTo(ctx.getTargets) |> ctx.withTargets |> { ctx0 =>
//         val auto = ctx0.getTargets.getGuideEnvironment.guideEnv.primaryGroup.isAutomatic
//         auto ? ctx0.withPositionAngle(posAngle) | ctx0
//       }
//     }
//   }
// }

object AgsStrategy {
  // val Pwfs1North     = SingleProbeStrategy(Pwfs1NorthKey, PwfsParams(Site.GN, PwfsGuideProbe.pwfs1))
  // val Pwfs2North     = SingleProbeStrategy(Pwfs2NorthKey, PwfsParams(Site.GN, PwfsGuideProbe.pwfs2))
  // val Pwfs1South     = SingleProbeStrategy(Pwfs1SouthKey, PwfsParams(Site.GS, PwfsGuideProbe.pwfs1))
  // val Pwfs2South     = SingleProbeStrategy(Pwfs2SouthKey, PwfsParams(Site.GS, PwfsGuideProbe.pwfs2))
  def GmosNorthOiwfs[F[_]: Applicative] = SingleProbeStrategy[F](GmosOiwfsParams(Site.GN))
  def GmosSouthOiwfs[F[_]: Applicative] = SingleProbeStrategy[F](GmosOiwfsParams(Site.GS))

  def siderealStrategy[F[_]: Applicative](instrument: Instrument): List[AgsStrategy[F]] =
    instrument match {

      case Instrument.GmosSouth => List(GmosSouthOiwfs[F]) // , Pwfs2South, Pwfs1South)
      case Instrument.GmosNorth => List(GmosNorthOiwfs[F]) // , Pwfs2North, Pwfs1Northe)
      case _                    => Nil
    }
}
