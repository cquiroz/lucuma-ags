// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package lucuma.catalog

import cats.Order
import cats.syntax.all._
import munit.ScalaCheckSuite
import lucuma.core.math.Angle
import lucuma.core.math.Coordinates
import lucuma.core.math.RightAscension
import lucuma.core.math.Declination
import lucuma.core.math.arb.ArbCoordinates._
import lucuma.core.math.arb.ArbAngle._
import lucuma.core.math.arb.ArbOffset._
import org.scalacheck.Prop._
import lucuma.core.math.Offset

class RadiusConstraintSuite extends ScalaCheckSuite {
  val min: Angle = Angle.fromDoubleDegrees(10 / 60.0)
  val max: Angle = Angle.fromDoubleDegrees(20 / 60.0)

  implicit val order: Order[Angle] = Angle.AngleOrder

  // "RadiusConstraint" should {
  test("filter at 0,0") {
    val rad = RadiusConstraint.between(max, min)

    val outMaxNeg = Angle.fromDoubleDegrees(-20.1 / 60)
    val outMaxPos = Angle.fromDoubleDegrees(20.1 / 60)

    val brdMaxNeg = Angle.fromDoubleDegrees(-19.99999 / 60)
    val brdMaxPos = Angle.fromDoubleDegrees(19.99999 / 60)

    val inMaxNeg = Angle.fromDoubleDegrees(-19.9 / 60)
    val inMaxPos = Angle.fromDoubleDegrees(19.9 / 60)

    val outMinNeg = Angle.fromDoubleDegrees(-9.9 / 60)
    val outMinPos = Angle.fromDoubleDegrees(9.9 / 60)

    val brdMinNeg = Angle.fromDoubleDegrees(-10.0001 / 60)
    val brdMinPos = Angle.fromDoubleDegrees(10.0001 / 60)

    val inMinNeg = Angle.fromDoubleDegrees(-10.1 / 60)
    val inMinPos = Angle.fromDoubleDegrees(10.1 / 60)

    val out = List(outMaxNeg, outMaxPos, outMinNeg, outMinPos)
    val in  =
      List(brdMaxNeg, brdMaxPos, inMaxNeg, inMaxPos, brdMinNeg, brdMinPos, inMinNeg, inMinPos)
    val f   = rad.targetsFilter(Coordinates.Zero)

    assert(ras(out, Angle.Angle0).filter(f).isEmpty)
    assert(decs(Angle.Angle0, out).filter(f).isEmpty)
    assertEquals(ras(in, Angle.Angle0).filter(f).length, 8)
    assertEquals(decs(Angle.Angle0, in).filter(f).length, 8)

    // in the middle of the limits along the diagonals
    val mins = List(outMinNeg, outMinPos, brdMinNeg, brdMinPos, inMinNeg, inMinPos)
    assertEquals(perms(mins).filter(f).length, 36)

    // out of the max limits along the diagonals
    val maxs = List(outMaxNeg, outMaxPos, brdMaxNeg, brdMaxPos, inMaxNeg, inMaxPos)
    assert(perms(maxs).filter(f).isEmpty)
  }
  test("filter at 0,90") {
    val base  = coords(Angle.Angle0, Angle.Angle90)
    val rad   = RadiusConstraint.between(max, min)
    val f     = rad.targetsFilter(base)
    val ra    = Angle.fromDoubleDegrees(180)
    val deg90 = Angle.fromDoubleDegrees(90)
    val dec9  = deg90 + Angle.fromDoubleDegrees(-9.0 / 60)
    val dec11 = deg90 + Angle.fromDoubleDegrees(-11.0 / 60)
    val dec19 = deg90 + Angle.fromDoubleDegrees(-19.0 / 60)
    val dec21 = deg90 + Angle.fromDoubleDegrees(-21.0 / 60)
    assert(!f.apply(coords(ra, dec9)))
    assert(f.apply(coords(ra, dec11)))
    assert(f.apply(coords(ra, dec19)))
    assert(!f.apply(coords(ra, dec21)))

    assert(!f(coords(ra, dec9)))
    assert(f(coords(ra, dec11)))
    assert(f(coords(ra, dec19)))
    assert(!f(coords(ra, dec21)))
  }
  property("fail for different coordinates if range is 0") {
    forAll { (c: Coordinates, a: Angle, b: Angle) =>
      val notTheSame = c != coords(a, b)
      val rad        = RadiusConstraint.between(Angle.Angle0, Angle.Angle0)
      val f          = rad.targetsFilter(c)
      assertEquals(!notTheSame, (f(coords(a, b))))
    }
  }
  property("work for all if range is full") {
    forAll { (c: Coordinates, a: Angle, b: Angle) =>
      // 360 gets turned to 0, we try instead to get as close as possible to 360
      val rad =
        RadiusConstraint.between(Angle.fromDoubleDegrees(360 - 0.00000001), Angle.Angle0)
      val f   = rad.targetsFilter(c)
      assert(f(coords(a, b)))
    }
  }
  property("for any pair of angles preserve order") {
    forAll { (a: Angle, b: Angle) =>
      val rad = RadiusConstraint.between(a, b)
      assert(rad.maxLimit >= rad.minLimit)
    }
  }
  property("for any pair of angles an offset preserves order") {
    forAll { (o: Offset, a: Angle, b: Angle) =>
      val rad = RadiusConstraint.between(a, b).adjust(o)
      assert(rad.maxLimit >= rad.minLimit)
    }
  }

  private def ras(ras: List[Angle], dec: Angle): List[Coordinates] =
    for (ra <- ras)
      yield coords(ra, dec)

  private def decs(ra: Angle, decs: List[Angle]): List[Coordinates] =
    for (dec <- decs)
      yield coords(ra, dec)

  private def perms(a: List[Angle]): List[Coordinates] =
    for {
      ra  <- a
      dec <- a
    } yield coords(ra, dec)

  private def coords(ra: Angle, dec: Angle): Coordinates =
    Coordinates(RightAscension.fromAngleExact.getOption(ra).getOrElse(RightAscension.Zero),
                Declination.fromAngle.getOption(dec).getOrElse(Declination.Zero)
    )
  //
  // private def name(ra: Angle, dec: Angle): String =
  //   s"ra=${ra.toDegrees}, dec=${dec.toDegrees}"
  //
  // private def coords(ra: Angle, dec: Angle): SiderealTarget =
  //   coords(name(ra, dec),
  //   )
  //
  // private def coords(name: String, c: Coordinates): SiderealTarget =
  //   SiderealTarget.empty.copy(name = name, coordinates = c)
}
